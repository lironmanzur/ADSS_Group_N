package Frontend.PresentationLayer.Employees;

import Backend.ServiceLayer.Service;

import Backend.BusinessLayer.Employees.Roles;
import Backend.BusinessLayer.Employees.ShiftType;
import Backend.ServiceLayer.ObjectsEmployees.Result;
import Backend.ServiceLayer.ObjectsEmployees.SEmployee;
import Backend.ServiceLayer.ObjectsEmployees.SShift;
import Backend.ServiceLayer.ObjectsEmployees.ServiceEmployees;
import Backend.ServiceLayer.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class EmployeesConsole {
    private Service serviceControl;
    private Scanner scanner;
    public EmployeesConsole(Scanner scanner, Service service) {
        this.scanner = scanner;
        this.serviceControl = service;
    }

    public void run(){
        boolean flag = false;
        while(!flag){
            System.out.print("Enter Employee ID: ");
        }
    }
}
