import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

class TestEmployee {

    @Test
    void testSetWagerate() {
        // Crearea unui obiect de tip Employee pentru a testa metoda setWagerate
        Employee employee = new Employee(1, "John", "Doe", "password");

        // Testare setare rata salarială peste minimum
        employee.setWagerate(15.0);
        assertEquals(15.0, employee.getWagerate());

        // Testare setare rata salarială sub minimum
        employee.setWagerate(10.0);
        assertEquals(Employee.MINIMUM_RATE, employee.getWagerate());
    }

    @Test
    void testCalculateWages() throws ParseException {
        // Crearea unui obiect de tip Employee pentru a testa metoda calculateWages
        Employee employee = new Employee(1, "John", "Doe", "password");

        // Crearea unui obiect de tip SimpleDateFormat pentru a manipula formatarea timpului
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        // Marcarea prezenței angajatului
        employee.clockIn();
        employee.clockOut();

        // Setarea orei de început și a orei de sfârșit a programului
        employee.setStartWorkTime(dateFormat.parse("09:00"));
        employee.setFinishWorkTime(dateFormat.parse("10:30"));

        // Presupunând că rata salarială este setată la rata minimă
        assertEquals(20.25, employee.calculateWages(), 0.01);
    }

}
