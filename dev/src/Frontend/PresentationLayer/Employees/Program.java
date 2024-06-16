package Frontend.PresentationLayer.Employees;


import Backend.ServiceLayer.Employees.EmployeeService;

import java.util.Scanner;

public class Program {

    public static void main(String[] args) throws Exception {
        EmployeesConsole cli = new EmployeesConsole(new Scanner(System.in),new EmployeeService());
        cli.run();
    }
}
