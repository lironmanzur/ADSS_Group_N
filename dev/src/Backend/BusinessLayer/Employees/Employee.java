package Backend.BusinessLayer.Employees;
import Backend.BusinessLayer.Enums.Licens_en;

import javax.management.relation.Role;
import java.util.*;

public class Employee {

    private final int id;
    private String name;
    private int salary;
    private int bankNumber;
    private Date firstDay;
    private String license;
    private String phoneNumber;
    private boolean readyToWork;
    private List<String> roles;
    private String branch;
    private boolean isBusy;
    private Constraint constraint;


    public Employee(int id, String name, int salary, int bankNumber, Date firstDay, String phoneNumber,String branch) throws Exception{
        if(salary<0)throw new Exception("salary must be greater than zero");
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.bankNumber = bankNumber;
        this.firstDay = firstDay;
        this.license = "none";
        this.phoneNumber = phoneNumber;
        this.readyToWork = true;
        this.roles = new ArrayList<>();
        this.branch = branch;
        this.isBusy = false;
        this.constraint = new Constraint(id);

    }
    //getters
    public int getId(){return this.id;}
    public String getName(){return this.name;}
    public int getSalary(){return this.salary;}
    public int getBankNumber(){return this.bankNumber;}
    public Date getFirstDay(){return this.firstDay;}
    public String getLicense(){return this.license;}
    public String getPhoneNumber(){return this.phoneNumber;}
    public boolean getReadyToWork(){return this.readyToWork;}
    public List<String> getRoles(){return this.roles;}
    public String getBranch(){return this.branch;}
    public boolean isBusy(){return this.isBusy;}
    public Constraint getConstraint(){return this.constraint;}


    //setters
    public void setName(String name){this.name = name;}
    public void setSalary(int salary) throws Exception{
        if(salary<0)throw new Exception("salary must be greater than zero");
        this.salary = salary;}
    public void setBankNumber(int bankNumber){this.bankNumber = bankNumber;}
    public void setFirstDay(Date firstDay){this.firstDay = firstDay;}
    public void setLicense(String license){this.license = license;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}
    public void setReadyToWork(boolean readyToWork){this.readyToWork = readyToWork;}
    public void setRole(String role) throws Exception {roles = analyzeRoles(role);}
    public void setBranch(String branch){this.branch = branch;}
    public void setIsBusy(boolean isBusy){this.isBusy = isBusy;}


    public boolean hasRole(String role){return roles.contains(role);}
    public boolean removeRole(String role){return roles.remove(role);}
    public boolean addRole(String role) {return roles.add(role);}

    public List<String> analyzeRoles(String role) throws Exception{
        List<String> ro = Arrays.asList(role.split("-"));
        if(ro.get(0).equals("")) return new ArrayList<String>();
        for(String r : ro){
            if(!Roles.getRoles().contains(r)) throw new Exception("Invalid role");
        }
        return ro;
    }

    public String toString() {
        return String.format("Employee{name='%s', id=%d, salary=%.2f, bankAccountNumber='%s', roles=%s, startDate=%s}",
                name, id, salary, bankNumber, roles, firstDay);

}

}



