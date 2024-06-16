package Backend.BusinessLayer.CallBacks;

import java.util.Collection;
import Backend.BusinessLayer.Employees.Shift;


public interface GetShiftCB {

    public Collection<Shift> call() throws Exception;
}
