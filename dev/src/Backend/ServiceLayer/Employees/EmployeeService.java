package Backend.ServiceLayer.Employees;

import Backend.BusinessLayer.Employees.*;
import Backend.BusinessLayer.Tools.DateConvertor;
import Backend.ServiceLayer.Response;
import Backend.ServiceLayer.ResponseT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EmployeeService {
    private EmployeeController ec;
    private ShiftController sc;
    private WArrangementController wac;

    public EmployeeController getEmployeeController() {return ec;}
    public ShiftController getShiftController() {return sc;}
    public WArrangementController getWarrangementController() {return wac;}

    public EmployeeService() {
        this.sc = new ShiftController();
        this.ec = new EmployeeController();
        this.wac = new WArrangementController(sc,ec);
    }

    //  All employees

    public ResponseT<Employee> login(String eName, String password) {
        try {
            ec.login(eName, password);
            int ids = Integer.parseInt(eName);
            Employee emp = ec.getEmployee(ids);
            return new ResponseT<>(emp);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage(), true);
        }
    }


    public ResponseT<String> logout(int employeeId) {
        try {
            ec.logout(employeeId);
            return new ResponseT<>("Logout successful", false);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage(), true);
        }
    }



    public ResponseT<Boolean> addConstraint(int day, String shift, int employeeID){
        try{
            boolean sec = ec.addConstraint(employeeID,day,shift);
            return new ResponseT<>(sec);

        } catch (Exception e) {
            return new ResponseT<>(e.getMessage(),true);
        }
    }

    public ResponseT<Boolean> removeConstraint(int employeeID, int day, String shift){
        try {
            boolean sec =ec.removeConstraint(employeeID,day,shift);
            return new ResponseT<>(sec);
        }catch (Exception e) {
            return new ResponseT<>(e.getMessage(),true);
        }
    }
    public ResponseT<List<String>> getAllRoles(int employeeID){
        try {
            List<String> roles = ec.getEmployee(employeeID).getRoles();
            if(!(roles == null)){return new ResponseT<>(roles);}
            return new ResponseT<>("This employee does not have any roles", true);
        }catch (Exception e) {
            return new ResponseT<>(e.getMessage(),true);
        }

    }

    public ResponseT<Boolean> getEmployeeWeeklyConstraint(int employeeID){
        try {
            ec.printWeeklyConstraints(employeeID);
            return new ResponseT<>(true);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage(),true);
        }
    }

    /// only for HR Manager

    public ResponseT<String> getEmployee(int employeeID){
        try {
            ec.pirntEmployee(employeeID);
            return new ResponseT<>("Get Employee successful", false);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage(),true);
        }

    }
    public ResponseT<Boolean> addEmployee(int id,String name, int salary, int bankNumber, Date D, String phoneNumber, String branch){
        try {
            boolean sec = ec.addEmployee(id,name,salary,bankNumber,D,phoneNumber,branch);
            return new ResponseT<>(sec);

        } catch (Exception e) {
            return new ResponseT<>(e.getMessage(), true);
        }
    }

    public ResponseT<Boolean> updateEmployeeName(int employeeID, String newName){
        try {
            boolean sec = ec.updateEmployeeName(employeeID,newName);
            return new ResponseT<>(sec);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage(),true);
        }
    }
    public ResponseT<Boolean> updateEmployeeBankAccountNumber(int employeeID, int bankAccountNumber){
        try{
            boolean sec = ec.updateEmployeeBankNumber(employeeID,bankAccountNumber);
            return new ResponseT<>(sec);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage(),true);
        }
    }
    public ResponseT<Boolean> updateEmployeePhoneNumber(int employeeID, String pn){
        try{
            boolean sec = ec.updateEmployeePhoneNumber(employeeID,pn);
            return new ResponseT<>(sec);
        }catch (Exception e){
            return new ResponseT<>(e.getMessage(),true);
        }

    }
    //todo: fix this function
    public ResponseT<Boolean> displayWeekWorkArrangement(Date date, String branch){
        try{
            boolean sec = wac.presentWAByBranch(branch,date);
            return new ResponseT<>(sec);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage(),true);
        }
    }
    public ResponseT<Boolean> addEmployeeToShift(int employeeID,Date date,String st, String branch){
        try {
            boolean sec = sc.addEmployeeToShift(ec.getEmployee(employeeID),date,st,branch);
            return new ResponseT<>(sec);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage(),true);
        }
    }
    public ResponseT<Boolean> addBranch(int branchID,String branchName,String branchAddress){
        try{
            boolean sec = ec.addBranch(branchID,branchName,branchAddress);
            return new ResponseT<>(sec);
        }catch (Exception e) {
            return new ResponseT<>(e.getMessage(),true);
        }
    }

    public ResponseT<Boolean> addShift(Date date, String shift, String branch) {
        try {
            boolean sec = sc.addShift(date,shift,branch);
            return new ResponseT<>(sec);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage(),true);
        }
    }
























    public void initial() {
        addBranch(1,"TelAviv","Hamasger 5");
        addBranch(2,"Haifa","Hadayagim 39");
        addBranch(3,"TelAviv","Derech Hevron 17");
        addEmployee(1,"Rami",20000, 600000,toDate("01-01-2000"),"0510000000","TelAviv" );
        addEmployee(2,"Avi",9000,600001,toDate("02-03-2009"),"0510000001","TelAviv" );
        addEmployee(3,"Ben",8000,600002,toDate("02-03-2009"),"0510000002","TelAviv" );
        addEmployee(4,"Cali",8500,600003,toDate("31-12-2010"),"0510000003","BeerShiva" );
        addEmployee(5,"David",7000,600004,toDate("07-06-2013"),"0510000004","BeerShiva" );
        addEmployee(6,"Erik",7000,600005,toDate("01-04-2015"),"0510000005","Haifa" );
        addEmployee(7,"Fuad",8000,600002,toDate("02-03-2009"),"0510000006","TelAviv" );
        addEmployee(8,"Gidon",8500,600003,toDate("31-12-2010"),"0510000007","BeerShiva" );
        addEmployee(9,"Haviv",7000,600004,toDate("07-06-2013"),"0510000008","BeerShiva" );
        addEmployee(10,"Irit",7000,600005,toDate("01-04-2015"),"0510000009","Haifa" );


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date date = new Date(calendar.getTime().getTime());
        DateConvertor dateConvertor = new DateConvertor();
        String tomorrow = dateConvertor.dateToString(date);
        addShift(toDate(tomorrow),ShiftType.morning(),"BeerShiva");
        addShift(toDate(tomorrow),ShiftType.morning(),"BeerShiva");
        addEmployeeToShift(1,toDate(tomorrow),ShiftType.morning(),"BeerShiva");
        addEmployeeToShift(123456789,toDate(tomorrow),ShiftType.morning(),"BeerShiva");

        addShift(toDate("25-06-2024"),ShiftType.morning(),"BeerShiva");
        addShift(toDate("25-06-2024"),ShiftType.evening(),"BeerShiva");





    }

    private static Date toDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date d = null;
        try {
            d = dateFormat.parse(date);
        }catch (ParseException ignored) {}
        return d;

    }
}
