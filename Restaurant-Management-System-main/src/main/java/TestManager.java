import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestManager {

    @Test
    public void testConstructor() {
        Manager manager = new Manager(1, "John", "Doe", "password");
        assertEquals(1, manager.getID());
        assertEquals("John", manager.getFirstName());
        assertEquals("Doe", manager.getLastName());
        assertEquals("password", manager.getPassword());
        assertEquals(100.0, manager.getWagerate());
    }

    @Test
    public void testSetWagerate() {
        Manager manager = new Manager(1, "John", "Doe", "password");

        // Setare rată de salariu mai mică decât MINIMUM_RATE
        manager.setWagerate(90.0);
        assertEquals(100.0, manager.getWagerate());  // Așteaptă-se să fie setată la MINIMUM_RATE

        // Setare rată de salariu mai mare
        manager.setWagerate(120.0);
        assertEquals(120.0, manager.getWagerate());
    }


    @Test
    public void testCalculateWages() {
        Manager manager = new Manager(1, "John", "Doe", "password");

        // Testare pentru starea de muncă neîncheiată
        assertEquals(0.0, manager.calculateWages());

        // Setare starea de muncă la finalizată și testare pentru rata de salariu
        manager.setWorkState(Manager.WORKSTATE_FINISH);
        assertEquals(100.0, manager.calculateWages());
    }
}
