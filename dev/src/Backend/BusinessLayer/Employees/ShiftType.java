package Backend.BusinessLayer.Employees;

public class ShiftType {
    private static String morning_shift = "Morning";
    private static String evening_shift = "Evening";
    private static final int Num_Days_In_Week = 6;
    public static final String[] DAYS_OF_WEEK = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    public static  String morning(){return morning_shift;}
    public static  String evening(){return evening_shift;}
    public static boolean isShiftValid(String shift){return shift.equals(morning_shift) || shift.equals(evening_shift);}
}
