package Backend.BusinessLayer.Employees;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class Constraint {
    private int empId;
    private Map<Integer,Map<String,Boolean>> availability;



    public Constraint(int empId) {
        this.empId = empId;
        this.availability = new HashMap<>();

    }

    public int getEmpId() {
        return empId;
    }
    private void initializeAvailability() {
        for (int day = 0; day< 6 ; day++) {
            Map<String, Boolean> shifts = new HashMap<>();
            shifts.put(ShiftType.morning(), true);
            shifts.put(ShiftType.evening(), true);
            availability.put(day, shifts);
        }
    }
    public void resetConstraint() {
        availability.clear();
        initializeAvailability();
    }
    public Map<Integer, Map<String, Boolean>> getAvailability() {
        return availability;
    }

    public void setAvailability(int day, String shift, boolean isAvailable)throws Exception {
        if(day>5 || day<0 || ShiftType.isShiftValid(shift)) throw new Exception("Invalid shift!");
        availability.get(day).put(shift, isAvailable);
    }

    public boolean isAvailable(int day, String shift) throws Exception {
        if(day>5 || day<0 || !ShiftType.isShiftValid(shift)) throw new Exception("Invalid shift!");
        return availability.getOrDefault(day, new HashMap<>()).getOrDefault(shift, true);
    }

}
