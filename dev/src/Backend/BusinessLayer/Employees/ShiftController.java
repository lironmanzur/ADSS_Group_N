package Backend.BusinessLayer.Employees;



import Backend.BusinessLayer.CallBacks.EmployeeAvCB;
import Backend.BusinessLayer.Enums.Licens_en;
import Backend.BusinessLayer.Tools.DateConvertor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class ShiftController {

    private Map<String, Map<String, Shift[]>> shiftByDateAndBranch;
    private Map<Integer, List<Shift>> shiftByEmployeeId;
    private Map<Shift, List<String>> LackRoleByShift;
    private Map<Integer, Constraint> constraintById;


    public ShiftController() {
        shiftByDateAndBranch = new HashMap<>();
        shiftByEmployeeId = new HashMap<>();
        LackRoleByShift = new HashMap<>();
        constraintById = new HashMap<>();

    }
    public Map<String, Map<String, Shift[]>> getShiftByDateAndBranch() {
        return shiftByDateAndBranch;
    }
    public void initWeek(Date startDate) throws Exception {
        DateConvertor dateConvertor = new DateConvertor();
        int day = dateConvertor.dateToDayOfWeek(startDate);
        LocalDate ld = dateConvertor.convertToLocalDate(startDate);
        if(day != 0){throw new Exception("This day is not Sunday!");}
        for(String branch : shiftByDateAndBranch.keySet()){
            for(int i = 0 ; i<6 ; i++){
                Date curDay = dateConvertor.convertToDate(ld.plusDays(i));
                addShift(curDay,"Morning",branch);
                addShift(curDay,"Evening",branch);
            }
        }

    }
    public boolean addShift(Date date, String st, String branch) throws Exception {
        Shift shift = new Shift(st, date, branch);
        if (!shiftByDateAndBranch.containsKey(branch)) {
            shiftByDateAndBranch.put(branch, new HashMap<>());
        }
        Map<String, Shift[]> shiftByDateOnBranch = shiftByDateAndBranch.get(branch);
        DateConvertor dateConvertor = new DateConvertor();
        String dateKey = dateConvertor.dateToDailyString(date);

        if (!shiftByDateOnBranch.containsKey(dateKey)) {
            Shift[] dailyShifts = new Shift[2];
            if (st.equals("Morning")) dailyShifts[0] = shift;
            else dailyShifts[1] = shift;
            shiftByDateOnBranch.put(dateKey, dailyShifts);
        } else {
            if (st.equals("Morning")) {
                if (shiftByDateOnBranch.get(dateKey)[0] == null) shiftByDateOnBranch.get(dateKey)[0] = shift;
                else throw new Exception("Shift already exists!");

            } else if (st.equals("Evening")) {
                if (shiftByDateOnBranch.get(dateKey)[1] == null) shiftByDateOnBranch.get(dateKey)[1] = shift;
                else throw new Exception("Shift already exists!");
            } else throw new Exception("Shift already exists!");
        }
        LackRoleByShift.put(shift, new ArrayList<>());
        return true;
    }

    public List<Shift> getAllShift() throws Exception {
        List<Shift> shifts = new ArrayList<>();
        Set<String> branches = shiftByDateAndBranch.keySet();
        for (String branch : branches) {
            Set<String> dats = shiftByDateAndBranch.get(branch).keySet();
            for (String date : dats) {
                shifts.addAll(Arrays.asList(shiftByDateAndBranch.get(branch).get(date)));
            }
        }
        return shifts;
    }

    public Shift selectShift(Date date, String st, String branch) throws Exception {

        if (!shiftByDateAndBranch.containsKey(branch)) {
            throw new Exception("Branch does not exist!");
        } else {
            Map<String, Shift[]> shiftByDateOnBranch = shiftByDateAndBranch.get(branch);
            DateConvertor dateConvertor = new DateConvertor();
            String dateKey = dateConvertor.dateToDailyString(date);
            if (!shiftByDateOnBranch.containsKey(dateKey)) {
                throw new Exception("There is no shift on this date");
            } else {
                Shift[] shifts = shiftByDateOnBranch.get(dateKey);
                if (st.equals("Morning")) {
                    if (shifts[0] == null) throw new Exception("Shift does not exist!");
                    else {
                        return shifts[0];
                    }
                } else if (st.equals("Evening")) {
                    if (shifts[1] == null) throw new Exception("Shift does not exist!");
                    else {
                        return shifts[1];
                    }
                } else throw new Exception("Shift does not exist!");
            }
        }
    }


    public boolean addEmployeeToShift(Employee employee, Date date, String st, String Branch) throws Exception {
        if (!ShiftType.isShiftValid(st)) throw new Exception(st + "ShiftType is not valid!");
        Shift shift = selectShift(date, st, Branch);
        if (shift.hasEmployee(employee.getId())) {
            throw new Exception("Employee already exists in this shift!");
        }
        Constraint constraint = employee.getConstraint();
        DateConvertor dc = new DateConvertor();
        int day = dc.dateToDayOfWeek(date);
        if (!constraint.isAvailable(day, st)) throw new Exception("Employee has constraint that is not available!");
        shift.addEmployee(employee);
        if (shiftByEmployeeId.containsKey(employee.getId())) {
            shiftByEmployeeId.get(employee.getId()).add(shift);
        } else {
            shiftByEmployeeId.put(employee.getId(), new ArrayList<>());
        }
        return true;
    }


    public void applyConstraintsAndBuildArrangement( Map<Integer, Employee> empById) throws Exception {
        for(String branch : shiftByDateAndBranch.keySet()){
            Map<String, Shift[]> shiftByDate = shiftByDateAndBranch.get(branch);
            for(Shift[] dailyShifts : shiftByDate.values()){
                for(Shift shift : dailyShifts){
                    if(shift != null ){
                        assignEmployeesToShift(shift,empById);

                    }
                }
            }

        }
    }

    private void assignEmployeesToShift(Shift shift, Map<Integer, Employee> empById) throws Exception {
        DateConvertor dc = new DateConvertor();
        int day = dc.dateToDayOfWeek(shift.getDate());
        for(Employee employee : empById.values()){
            Constraint constraint = employee.getConstraint();
            if(constraint.isAvailable(day,shift.getShiftType())&&shift.hasEmployee(employee.getId())){
                shift.addEmployee(employee);
            }
        }
    }

    public void presentWeekArrangement(Date startDate) {
        // Define day names
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        // Format for the dates
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        // Generate the dates for the week starting from the given Sunday
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        String[] dates = new String[6];
        for (int i = 0; i < 6; i++) {
            dates[i] = sdf.format(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }

        // Print the table header with days and dates
        System.out.printf("%-10s", "");
        for (int i = 0; i < 6; i++) {
            System.out.printf("%-15s", days[i] + "\n" + dates[i]);
        }
        System.out.println();

        // Print a separating line
        System.out.println("--------------------------------------------------------------------------------------");

        // Define time periods (rows)
        String[] periods = {ShiftType.morning(), ShiftType.evening()};

        // Print the table rows for each period
        for (String period : periods) {
            System.out.printf("%-10s", period);
            for (String date : dates) {
                Shift[] shifts = shiftByDateAndBranch.values().iterator().next().get(date);
                Shift shift = period.equals(ShiftType.morning()) ? (shifts != null ? shifts[0] : null) : (shifts != null ? shifts[1] : null);
                if (shift != null) {
                    StringBuilder employeeDetails = new StringBuilder();
                    for (Employee employee : shift.getEmployees()) {
                        employeeDetails.append(employee.getId()).append(" ").append(employee.getName()).append(", ");
                    }
                    System.out.printf("%-15s", employeeDetails.length() > 0 ? employeeDetails.substring(0, employeeDetails.length() - 2) : "N/A");
                } else {
                    System.out.printf("%-15s", "N/A");
                }
            }
            System.out.println();

            // Print a separating line
            System.out.println("--------------------------------------------------------------------------------------");
        }
    }
}























