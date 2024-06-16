package Backend.ServiceLayer.Employees;

import Backend.BusinessLayer.Employees.EmployeeController;
import Backend.BusinessLayer.Employees.ShiftController;

public class ShiftService {
    private EmployeeController ec;
    private ShiftController sc;

    public ShiftService(EmployeeController ec, ShiftController sc) {
        this.ec = ec;
        this.sc = sc;
    }
}
