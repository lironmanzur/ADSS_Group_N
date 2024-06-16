package Frontend.PresentationLayer.Employees;

import Backend.BusinessLayer.Employees.Employee;

import Backend.ServiceLayer.Employees.EmployeeService;
import Backend.ServiceLayer.ResponseT;

import java.text.SimpleDateFormat;
import java.util.*;

public class EmployeesConsole {
    private EmployeeService es;
    private Scanner scanner;
    public EmployeesConsole(Scanner scanner, EmployeeService es) {
        this.scanner = scanner;
        this.es =es ;
        es.initial();

    }

    public void run(){
        boolean flag = false;
        while(!flag){
            System.out.println("Welcome to the Employee System!\n1. Login\n2. Exit\nPlease enter your choice: ");

            String choice = scanner.nextLine();
            //scanner.nextLine(); // Consume newline left-over

            switch (choice) {
                case "1":
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    try{
                    ResponseT<Employee> response = es.login(username, password);
                    ResponseT<List<String>> responseRoles = es.getAllRoles(response.getValue().getId());

                    if (response.errorOccurred()) {
                        System.out.println("Login failed: " + response.getErrorMessage());
                    } else {
                        System.out.println("Login successful: " + response.getValue().stringToSystem());
                        if(responseRoles.errorOccurred()){responseRoles.getErrorMessage();}
                        else {
                        if(responseRoles.getValue().contains("HR Manager")){ hrManagerMenu(response.getValue());return;}
                        else if(responseRoles.getValue().contains("Shift Manager")){shiftManagerMenu(response.getValue());return;}
                        else{ employeeMenu(response.getValue());return;}}

                    }}catch (Exception e){System.out.println("The username or password is incorrect. Please try again");}break;
                case "2":
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void hrManagerMenu(Employee employee)
    {

    }
    private void shiftManagerMenu(Employee employee)
    {int employeeId = employee.getId();
        String employeeBranch = employee.getBranch();
        boolean flag1 = false;
        while(!flag1){
            System.out.println("Shift Manager Menu:");
            System.out.println("1. Add Constraint\n" +
                    "2. Remove Constraint\n" +
                    "3. Update My Name\n" +
                    "4. Update My Bank Account Number\n" +
                    "5. Update My Phone Number \n" +
                    "6. Show my weekly constraints\n" +
                    "7. Present the weekly work schedule\n" +
                    "8. Adding employees to the current shift\n" +
                    "9. Transferring positions of shift workers\n" +
                    "10. Log Out \n" +
                    "Please enter your choice: ");


            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over
            switch (choice) {
                case 1:

                    System.out.print("Please choose: 0 for Sunday, 1 for Monday, 2 for Tuesday, 3 for Wednesday, 4 for Thursday, 5 for Friday" +
                            "\nPlease Enter your choice: ");
                    String day = scanner.nextLine();
                    System.out.print("For morning Press: m / For Evening Press: e\nEnter your choice:" );
                    String shiftType = scanner.nextLine();
                    try {
                        int dayNum = Integer.parseInt(day);
                        String shift = "";
                        if (shiftType.equals("m")) {
                            shift = "Morning";
                        } else if (shiftType.equals("e")) {
                            shift = "Evening";
                        } else {shift = "None";}
                        ResponseT<Boolean> res = es.addConstraint(dayNum,shift,employeeId);
                        System.out.println("!1!"+res);
                        if (!res.errorOccurred()) {
                            System.out.println("Add Constraint successful");}
                        else {System.out.println("Add Constraint failed: " + res.getErrorMessage());}

                    }catch (Exception e) {
                        System.out.println("Invalid Choice. Please try again.");

                    }break;

                case 2:
                    System.out.print("Please choose: 0 for Sunday, 1 for Monday, 2 for Tuesday, 3 for Wednesday, 4 for Thursday, 5 for Friday" +
                            "\nPlease Enter your choice: ");
                    String day1 = scanner.nextLine();
                    System.out.print("For morning Press: m / For Evening Press: e\nEnter your choice:" );
                    String shiftType1 = scanner.nextLine();
                    try {
                        int dayNum = Integer.parseInt(day1);
                        String shift = "";
                        if (shiftType1.equals("m")) {
                            shift = "Morning";
                        } else if (shiftType1.equals("e")) {
                            shift = "Evening";
                        } else {shift = "None";}
                        ResponseT<Boolean> res = es.removeConstraint(employeeId,dayNum,shift);
                        if (!res.errorOccurred()) {
                            System.out.println("Remove Constraint successful");}
                        else {System.out.println("Remove Constraint failed: " + res.getErrorMessage());}

                    }catch (Exception e) {
                        System.out.println("Invalid Choice. Please try again.");

                    }break;

                case 3:
                    System.out.println("Please enter your new name: ");
                    String name = scanner.nextLine();
                    try {
                        ResponseT<Boolean> newName = es.updateEmployeeName(employeeId,name);
                        if (!newName.errorOccurred()) {
                            System.out.println("Update Name to "+name+" is successful");}
                        else {System.out.println("Update Name failed: " + newName.getErrorMessage());}
                    }catch (Exception e) {
                        System.out.println("Invalid Name. Please try again.");
                    }break;

                case 4:// update bank account number
                    System.out.println("Please enter your new bank account number: ");
                    String accountNumber = scanner.nextLine();
                    try{
                        int an = Integer.parseInt(accountNumber);
                        ResponseT<Boolean> newBAN = es.updateEmployeeBankAccountNumber(employeeId,an);
                        if (!newBAN.errorOccurred()) {
                            System.out.println("Update Bank Account Number to "+accountNumber+" is successful");
                        }
                        else {System.out.println("Update Bank Account Number failed: " + newBAN.getErrorMessage());}
                    }catch (Exception e) {
                        System.out.println("Invalid Bank Account Number. Please try again.");
                    }break;

                case 5: // update phone number
                    System.out.println("Please enter your new phone number: ");
                    String phoneNumber = scanner.nextLine();
                    try {
                        ResponseT<Boolean> newPhone = es.updateEmployeePhoneNumber(employeeId,phoneNumber);
                        if (!newPhone.errorOccurred()) {
                            System.out.println("Update Phone Number to "+phoneNumber+" is successful");
                        }
                        else {System.out.println("Update Phone Number failed: " + newPhone.getErrorMessage());}
                    }catch (Exception e) {
                        System.out.println("Invalid Phone Number. Please try again.");
                    }break;
                case 6:
                    es.getEmployeeWeeklyConstraint(employeeId);break;
                case 7:
                    //todo: is not working until now
                    System.out.println("To see the weekly work schedule, enter the Sunday date on which the week you want to see starts (dd-mm-yyyy):");
                    String startDay = scanner.nextLine();
                    try{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        dateFormat.setLenient(false);
                        Date date = dateFormat.parse(startDay);
                        es.displayWeekWorkArrangement(date,employeeBranch);
                    }catch (Exception e) {
                        System.out.println("Invalid Sunday Date. Please try again.");
                    }break;


                case 8:



                case 9:


                case 10:
                    System.out.println("Are you sure you want to log out? yes/no");
                    String answer = scanner.nextLine();
                    if (answer.equals("yes")) {
                        System.out.println("Thank you for using Employee System!");
                        scanner.close();
                        flag1 = true;
                        break;



                    }else if (answer.equals("no")) {
                        break;
                    }

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        }


    }

    private void employeeMenu(Employee employee)
    {   int employeeId = employee.getId();
        String employeeBranch = employee.getBranch();
        boolean flag1 = false;
        while(!flag1){
        System.out.println("Employee Menu:");
        System.out.println("1. Add Constraint\n" +
                            "2. Remove Constraint\n" +
                            "3. Update My Name\n" +
                            "4. Update My Bank Account Number\n" +
                            "5. Update My Phone Number \n" +
                            "6. Show my weekly constraints\n" +
                            "7. Present the weekly work schedule\n" +
                            "8. Log Out \n" +
                            "Please enter your choice: ");


        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over
        switch (choice) {
            case 1:

                System.out.print("Please choose: 0 for Sunday, 1 for Monday, 2 for Tuesday, 3 for Wednesday, 4 for Thursday, 5 for Friday" +
                        "\nPlease Enter your choice: ");
                String day = scanner.nextLine();
                System.out.print("For morning Press: m / For Evening Press: e\nEnter your choice:" );
                String shiftType = scanner.nextLine();
                try {
                    int dayNum = Integer.parseInt(day);
                    String shift = "";
                    if (shiftType.equals("m")) {
                        shift = "Morning";
                    } else if (shiftType.equals("e")) {
                        shift = "Evening";
                    } else {shift = "None";}
                    ResponseT<Boolean> res = es.addConstraint(dayNum,shift,employeeId);
                    System.out.println("!1!"+res);
                    if (!res.errorOccurred()) {
                        System.out.println("Add Constraint successful");}
                    else {System.out.println("Add Constraint failed: " + res.getErrorMessage());}

                }catch (Exception e) {
                    System.out.println("Invalid Choice. Please try again.");

                }break;

            case 2:
                System.out.print("Please choose: 0 for Sunday, 1 for Monday, 2 for Tuesday, 3 for Wednesday, 4 for Thursday, 5 for Friday" +
                        "\nPlease Enter your choice: ");
                String day1 = scanner.nextLine();
                System.out.print("For morning Press: m / For Evening Press: e\nEnter your choice:" );
                String shiftType1 = scanner.nextLine();
                try {
                    int dayNum = Integer.parseInt(day1);
                    String shift = "";
                    if (shiftType1.equals("m")) {
                        shift = "Morning";
                    } else if (shiftType1.equals("e")) {
                        shift = "Evening";
                    } else {shift = "None";}
                    ResponseT<Boolean> res = es.removeConstraint(employeeId,dayNum,shift);
                    if (!res.errorOccurred()) {
                        System.out.println("Remove Constraint successful");}
                    else {System.out.println("Remove Constraint failed: " + res.getErrorMessage());}

                }catch (Exception e) {
                    System.out.println("Invalid Choice. Please try again.");

                }break;

            case 3:
                System.out.println("Please enter your new name: ");
                String name = scanner.nextLine();
                try {
                    ResponseT<Boolean> newName = es.updateEmployeeName(employeeId,name);
                    if (!newName.errorOccurred()) {
                        System.out.println("Update Name to "+name+" is successful");}
                    else {System.out.println("Update Name failed: " + newName.getErrorMessage());}
                    }catch (Exception e) {
                    System.out.println("Invalid Name. Please try again.");
                     }break;

            case 4:// update bank account number
                System.out.println("Please enter your new bank account number: ");
                String accountNumber = scanner.nextLine();
                try{
                    int an = Integer.parseInt(accountNumber);
                    ResponseT<Boolean> newBAN = es.updateEmployeeBankAccountNumber(employeeId,an);
                    if (!newBAN.errorOccurred()) {
                        System.out.println("Update Bank Account Number to "+accountNumber+" is successful");
                    }
                    else {System.out.println("Update Bank Account Number failed: " + newBAN.getErrorMessage());}
                    }catch (Exception e) {
                    System.out.println("Invalid Bank Account Number. Please try again.");
                    }break;

            case 5: // update phone number
                System.out.println("Please enter your new phone number: ");
                String phoneNumber = scanner.nextLine();
                try {
                    ResponseT<Boolean> newPhone = es.updateEmployeePhoneNumber(employeeId,phoneNumber);
                    if (!newPhone.errorOccurred()) {
                        System.out.println("Update Phone Number to "+phoneNumber+" is successful");
                    }
                    else {System.out.println("Update Phone Number failed: " + newPhone.getErrorMessage());}
                    }catch (Exception e) {
                    System.out.println("Invalid Phone Number. Please try again.");
                    }break;
            case 6:
                es.getEmployeeWeeklyConstraint(employeeId);break;
            case 7:
                //todo: is not working until now
                System.out.println("To see the weekly work schedule, enter the Sunday date on which the week you want to see starts (dd-mm-yyyy):");
                String startDay = scanner.nextLine();
                try{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    dateFormat.setLenient(false);
                    Date date = dateFormat.parse(startDay);
                    es.displayWeekWorkArrangement(date,employeeBranch);
                }catch (Exception e) {
                    System.out.println("Invalid Sunday Date. Please try again.");
                }break;



            case 8:
                System.out.println("Are you sure you want to log out? yes/no");
                String answer = scanner.nextLine();
                if (answer.equals("yes")) {
                    System.out.println("Thank you for using Employee System!");
                    scanner.close();
                    flag1 = true;
                    break;



                }else if (answer.equals("no")) {
                    break;
                }

            default:
                System.out.println("Invalid choice. Please try again.");
        }

    }}


    }