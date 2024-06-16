package Backend.BusinessLayer.Employees;

import java.util.*;

public class Shift {
    private String shiftType;
    private int dayInWeek;
    private Date date;
    private List<Employee> employees;
    private List<Integer> employeesByID ;
    private Dictionary<String,List<Employee>> extraEmployees;
    private String branch;


    public Shift(String shiftType, Date date, String branch) {
        this.shiftType = shiftType;
        this.date = date;
        this.branch = branch;
        this.employees = new ArrayList<Employee>();
        this.dayInWeek = date.getDay();
        initialEEL();
    }

    public String getShiftType() {return shiftType;}
    public int getDayInWeek() {return dayInWeek;}
    public Date getDate() {return date;}
    public List<Employee> getEmployees() {return employees;}
    public List<Integer> getEmployeesByID() {return employeesByID;}
    public String getBranch() {return branch;}

    public boolean hasShiftM(){
        for(Employee e: employees){
            if(e.hasRole(Roles.shiftManager()))return true;
        }
        return false;
    }
    public void setEmployeesId(String dataEmployees) {
        List<String> s = Arrays.asList(dataEmployees.split("-"));
        List<Integer> ids = new ArrayList<>();
        for (String str : s) {
            if (!str.equals("")) ids.add(Integer.valueOf(str));
        }
        employeesByID = ids;
    }
    public void setEmployees(List<Employee> employees) {this.employees = employees;}

    public void addEmployee(Employee e) {employees.add(e);}
    public boolean removeEmployee(Employee e) {return employees.remove(e);}
    public boolean hasEmployee(int id){
        for(Employee e: employees){
            if(e.getId() == id) return true;
        }
        return false;
    }
    public boolean hasRole(String role){
        for(Employee e: employees){
            if(e.hasRole(role))return true;
        }
        return false;
    }
    //ExtraEmployee

    public void initialEEL(){
        for(String role: Roles.getRoles()){
            extraEmployees.put(role, new ArrayList<Employee>());
        }
    }
    public void addExtraEmployee(Employee e){
        for(String role: e.getRoles()){
            extraEmployees.get(role).add(e);
        }
    }
    public boolean hasExtraEmployee(String role){
        return !extraEmployees.get(role).isEmpty();
    }
    public Employee removeExtraEmployee(String role)throws Exception{
        if(extraEmployees.get(role).isEmpty()) throw new Exception("No such Extra employee to this shift");
        return extraEmployees.get(role).remove(0);
    }



}
