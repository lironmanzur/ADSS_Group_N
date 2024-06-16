package Backend.BusinessLayer.Employees;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
import java.util.Calendar;

public class Scheduler {
    private Timer timer;
    private WArrangementController wa;


    public Scheduler(WArrangementController wac) {
        this.wa = wac;
        timer = new Timer();
        scheduleTasks();
    }

    private void scheduleTasks() {
        Calendar calendar = Calendar.getInstance();

        // Schedule for Tuesday 23:59
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        Date tuesdayDeadline = calendar.getTime();
        timer.schedule(new ApplyConstraintsTask(), tuesdayDeadline);

        // Schedule for Wednesday 00:00
        calendar.add(Calendar.MINUTE, 1);
        Date wednesdayStart = calendar.getTime();
        timer.schedule(new RegisterConstraintsTask(), wednesdayStart);

    }

    class RegisterConstraintsTask extends TimerTask {
        public void run() {
            try {
                wa.stopConstraintRegistration();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class ApplyConstraintsTask extends TimerTask {
        public void run() {
            try {
                wa.applyConstraintsAndBuildArrangement();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }




}