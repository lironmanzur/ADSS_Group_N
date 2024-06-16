package Backend.BusinessLayer.CallBacks;

import java.util.Date;

public interface EmployeeAvCB {
    public boolean callback(String name, String phone, Date date) throws Exception;
}
