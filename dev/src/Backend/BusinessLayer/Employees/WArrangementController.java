package Backend.BusinessLayer.Employees;
import Backend.BusinessLayer.Tools.DateConvertor;
import java.time.LocalDateTime;
import java.util.*;

public class WArrangementController {

    private ShiftController sc;
    private EmployeeController ec;
    private Map<String, Map<String, Shift[]>> shiftsByBranch;
    private Scheduler scheduler;

    public WArrangementController(ShiftController sc, EmployeeController ec) {
        this.sc = sc;
        this.ec = ec;
        shiftsByBranch = sc.getShiftByDateAndBranch();
        scheduler = new Scheduler(this);

    }
    public void applyConstraintsAndBuildArrangement() throws Exception {
        sc.applyConstraintsAndBuildArrangement(ec.getEmployeesById());
    }

    public void stopConstraintRegistration() throws Exception {
        ec.setConstraintRegistrationOpen(false);
        System.out.println("Constraint registration is now closed.");
        applyConstraintsAndBuildArrangement();
        ec.initialNewWeek();
        ec.setConstraintRegistrationOpen(true);}
        
        
    public boolean presentWAByBranch(String branch,Date date) throws Exception {
        DateConvertor dateConvertor = new DateConvertor();
        if(!shiftsByBranch.containsKey(branch)) {throw  new Exception("Branch does not exist");}
        Map<String, Shift[]> shifts = shiftsByBranch.get(branch);
        if (shifts == null) {
            throw new Exception("Branch " + branch + " has no shifts .");
        }
        try {
            int numOfDay = dateConvertor.dateToDayOfWeek(date);
            Date curSun;
            if(numOfDay <5) {
                curSun = getLastSunday(date);
            }else {
                curSun = getNextSunday(date);
            }
            sc.presentWeekArrangement(curSun);
            return true;

        }catch (Exception e) {throw new Exception("There is a problem with displaying the work arrangement");}


    }
    public static Date getLastSunday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // Set to the last Sunday
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // If the input date is before Sunday of the same week, move back to the previous week
        if (calendar.getTime().after(date)) {
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
        }
        return calendar.getTime();
    }
    public static Date getNextSunday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // Set the calendar to the next Sunday
        calendar.add(Calendar.DATE, (Calendar.SUNDAY - calendar.get(Calendar.DAY_OF_WEEK) + 7) % 7);
        return calendar.getTime();
    }

}



