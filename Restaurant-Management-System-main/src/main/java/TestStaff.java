import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

class TestStaff {

    // Testare setare rata salarială peste minimum
    @Test
    void testGetFullName() {
        Staff staff = new Staff(1, "John", "Doe", "password") {
            @Override
            protected void setWagerate(double newRate) {

            }

            @Override
            protected double calculateWages() {
                return 0;
            }
        };
        assertEquals("John Doe", staff.getFullName());
    }

    // Testare setare rata salarială peste minimum
    @Test
    void testClockInAndOut() {
        Staff staff = new Staff(1, "John", "Doe", "password") {
            @Override
            protected void setWagerate(double newRate) {

            }

            @Override
            protected double calculateWages() {
                return 0;
            }
        };
        staff.clockIn();
        assertEquals(Staff.WORKSTATE_ACTIVE, staff.getWorkState());

        staff.clockOut();
        assertEquals(Staff.WORKSTATE_FINISH, staff.getWorkState());
    }

    // Testare setare rata salarială peste minimum
    @Test
    void testCalculateWorkTime() throws ParseException {
        Staff staff = new Staff(1, "John", "Doe", "password") {
            @Override
            protected void setWagerate(double newRate) {

            }

            @Override
            protected double calculateWages() {
                return 0;
            }
        };

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        staff.clockIn();
        staff.clockOut();

        staff.setStartWorkTime(dateFormat.parse("09:00"));
        staff.setFinishWorkTime(dateFormat.parse("10:30"));

        assertEquals(1.5, staff.calculateWorkTime(), 0.01);
    }

}
