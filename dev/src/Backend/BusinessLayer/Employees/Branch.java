package Backend.BusinessLayer.Employees;

import java.util.ArrayList;
import java.util.List;

public class Branch {
    private String branchName;
    private int branchID;
    private String branchAddress;
    private List<Employee> employees;
    public Branch(int bID,String branchName, String branchAddress) {
        this.branchName = branchName;
        this.branchAddress = branchAddress;
        this.employees = new ArrayList<>();
        this.branchID = bID;
    }

    public int getBranchID() {
        return branchID;
    }
    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public String getBranchName() {
        return branchName;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    public String getBranchAddress() {
        return branchAddress;

    }
    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }
    public List<Employee> getEmployees() {
        return employees;
    }
    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

}
