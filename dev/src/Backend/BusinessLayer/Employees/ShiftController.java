package Backend.BusinessLayer.Employees;



import Backend.BusinessLayer.CallBacks.EmployeeAvCB;
import Backend.BusinessLayer.Enums.Licens_en;
import Backend.BusinessLayer.Tools.DateConvertor;

import java.util.*;

public class ShiftController {

    private Map<String, Map<String, Shift[]>> shiftByDateAndBranch;
    private Map<Integer, List<Shift>> shiftByEmployeeId;
    private Map<Shift, List<String>> LackRoleByShift;
    private Map<Integer, Constraint> constraintById;


    public ShiftController(Map<Shift, List<Employee>> employeesByShift, Map<Shift, List<String>> lackRoleByShift) {

        LackRoleByShift = lackRoleByShift;
        shiftByDateAndBranch = new HashMap<>();

    }

    public void addShift(Date date, String st, String branch) throws Exception {
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
    }

    public Collection<Shift> getAllShift() throws Exception {
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




}






















