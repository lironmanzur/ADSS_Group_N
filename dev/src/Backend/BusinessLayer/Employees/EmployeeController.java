package Backend.BusinessLayer.Employees;
import Backend.BusinessLayer.CallBacks.GetShiftCB;
import Backend.BusinessLayer.Tools.DateConvertor;




import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EmployeeController {

    private Map<Integer, Employee> employeesById;
    private Map<Integer, Constraint> constraintsByEId;
    private GetShiftCB SCB;

    public EmployeeController() {
        employeesById = new HashMap<Integer, Employee>();

    }

//HR Manager Methods

    public void addEmployee(int id, String name, int salary, int bankNumber, Date D, String phoneNumber, String branch) throws Exception {
        if (employeesById.containsKey(id)) {
            throw new Exception("Employee with ID:" + id + " already exists!");
        } else {
            Employee employee = new Employee(id, name, salary, bankNumber, D, phoneNumber, branch);
            employeesById.put(id, employee);
            constraintsByEId.put(id, employee.getConstraint());
        }
    }

    public Employee getEmployee(int id) throws Exception {
        if (employeesById.containsKey(id)) {
            return employeesById.get(id);
        } else {
            throw new Exception("Employee with ID:" + id + " does not exists!");
        }
    }

    public Employee removeEmployee(int id) throws Exception {
        Employee e = getEmployee(id);
        if (e.isBusy()) throw new Exception("Employee with ID:" + id + " cannot be removed! his work now!");
        Collection<Shift> shifts = SCB.call();
        DateConvertor dateConvertor = new DateConvertor();
        for (Shift shift : shifts) {
            if (dateConvertor.compareDate(dateConvertor.dateToDailyString(shift.getDate()) + " 00:00") < 0)
                if (shift.hasEmployee(id))
                    throw new Exception("the employee is already assigned to shift in: " + shift.getDate().toString());
        }
        employeesById.remove(id);
        constraintsByEId.remove(id);
        return e;
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeesById.values());
    }

    public List<Employee> getAllEmployeesByRole(String role) throws Exception {
        Collection<Employee> employees = employeesById.values();
        List<Employee> res = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.hasRole(role)) res.add(employee);
        }
        return res;
    }

    public List<Employee> getAllEmployeesByBranch(String branch) throws Exception {
        Collection<Employee> employees = employeesById.values();
        List<Employee> res = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getBranch().equals(branch)) res.add(employee);
        }
        return res;
    }
    public List<Employee> getAllEmployeesForShift(int day,String shift) throws Exception {
        Collection<Integer> employeesIDs = constraintsByEId.keySet();
        List<Employee> res = new ArrayList<>();
        for (int employeeID : employeesIDs) {
            Constraint constraint = constraintsByEId.get(employeeID);
            if(constraint.isAvailable(day, shift)) res.add(employeesById.get(employeeID));

        }
        return res;
    }

    public boolean addRoleToEmployee(int id, String role) throws Exception {
        Employee employee = getEmployee(id);
        if (employee.hasRole(role)) throw new Exception("Employee with ID:" + id + " already has this role!");
        if (!Roles.getRoles().contains(role)) throw new Exception("This role is illegal!");
        if (!employee.addRole(role)) {
            employee.addRole(role);
            return true;
        }
        return false;
    }

    public boolean removeRoleFromEmployee(int id, String role) throws Exception {
        Employee employee = getEmployee(id);
        if (!employee.hasRole(role)) throw new Exception("Employee with ID:" + id + " hasn't this role!");
        if (!Roles.getRoles().contains(role)) throw new Exception("This role is illegal!");
        if (employee.addRole(role)) {
            employee.removeRole(role);
            return true;
        }
        return false;
    }

    public void updateEmployeeSalary(int id, int newSalary) throws Exception {
        Employee employee = getEmployee(id);
        employee.setSalary(newSalary);
    }

    public void updateEmployeeBankNumber(int id, int newBankNumber) throws Exception {
        Employee employee = getEmployee(id);
        employee.setBankNumber(newBankNumber);
    }

    public void updateEmployeeBranch(int id, String newBranch) throws Exception {
        Employee employee = getEmployee(id);
        employee.setBranch(newBranch);
    }

    public void updateEmployeeDriverLicense(int id, String driverLicense) throws Exception {
        Employee employee = getEmployee(id);
        employee.setLicense(driverLicense);
    }
    public void updateEmployeeName(int id, String newName) throws Exception {
        Employee employee = getEmployee(id);
        employee.setName(newName);
    }


    // All Employee Methods


    public boolean addConstraint(int id, int day, String shift) throws Exception {
        if (!employeesById.containsKey(id)) {throw new Exception("Employee with ID:" + id + " not exists!");}
        Constraint c = constraintsByEId.get(id);
        if (c.isAvailable(day, shift)) {
            c.setAvailability(day, shift, false);
            return true;
        }
        return false;
    }
    public boolean removeConstraint(int id, int day, String shift) throws Exception {
        if (!employeesById.containsKey(id)) {throw new Exception("Employee with ID:" + id + " not exists!");}
        Constraint c = constraintsByEId.get(id);
        if (! c.isAvailable(day, shift)) {
            c.setAvailability(day, shift, true);
            return true;
        }
        return false;
    }
    // system + HR Manager + Employee
    public boolean removeAllEmployeeConstraints(int id) throws Exception {
        if(!employeesById.containsKey(id)) {throw new Exception("Employee with ID:" + id + " not exists!");}
        Constraint c = constraintsByEId.get(id);
        c.resetConstraint();
        return true;
    }

    public int getShiftNum(int day, String shift) throws Exception {
        if(5<day || day<0 || ShiftType.isShiftValid(shift)) throw new Exception("Invalid shift!");
        if(shift.equals(ShiftType.morning())) return 2*day;
        return (2*day)+1;
    }

    public Constraint getWeekConstraint(int id) throws Exception {
        if(!constraintsByEId.containsKey(id))throw new Exception("Employee with ID:" + id + " not exists!");
        return constraintsByEId.get(id);
    }

    public boolean getShiftConstraint(int id, int day, String shift) throws Exception {
        if(!constraintsByEId.containsKey(id))throw new Exception("Employee with ID:" + id + " not exists!");
        if(day>5 || day<0 || ShiftType.isShiftValid(shift)) throw new Exception("Invalid shift!");
        Constraint c = constraintsByEId.get(id);
        return c.isAvailable(day, shift);
    }



    // System Method

    public boolean initialNewWeek() throws Exception {
        for (int id: employeesById.keySet()) {
            Constraint c = constraintsByEId.get(id);
            c.resetConstraint();
        }
        return true;
        }


}
