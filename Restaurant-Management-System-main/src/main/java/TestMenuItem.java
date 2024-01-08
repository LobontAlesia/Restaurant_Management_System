import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestMenuItem {

    private MenuItem menuItem;

    @Before
    public void setUp() {
        // Inițializarea elementului de meniu pentru test
        menuItem = new MenuItem(1, "Burger", 10.0, (byte) MenuItem.LUNCH);
    }

    @Test
    public void testSetName() {
        // Setarea noului nume al elementului de meniu
        menuItem.setName("Cheeseburger");

        // Verificarea dacă numele a fost setat corect
        assertEquals("Cheeseburger", menuItem.getName());
    }

    @Test
    public void testSetPrice() {
        // Setarea noului preț al elementului de meniu
        menuItem.setPrice(12.0);

        // Verificarea dacă prețul a fost setat corect
        assertEquals(12.0, menuItem.getPrice(), 0.01);
    }

    @Test
    public void testSetType() {
        // Setarea noului tip al elementului de meniu
        menuItem.setType((byte) MenuItem.DINNER);

        // Verificarea dacă tipul a fost setat corect
        assertEquals(MenuItem.DINNER, menuItem.getType());
    }

    @Test
    public void testSetState() {
        // Setarea noii stări și a noului preț de promovare al elementului de meniu
        menuItem.setState((byte) 1, 8.0);

        // Verificarea dacă starea a fost setată corect
        assertEquals(1, menuItem.getState());
    }

    @Test
    public void testResetState() {
        // Setarea stării la 1
        menuItem.setState((byte) 1, 8.0);

        // Resetarea stării
        menuItem.resetState();

        // Verificarea dacă starea a fost resetată corect
        assertEquals(0, menuItem.getState());
    }

    @Test
    public void testGetID() {
        // Verificarea dacă ID-ul elementului de meniu este obținut corect
        assertEquals(1, menuItem.getID());
    }

    @Test
    public void testGetName() {
        // Verificarea dacă numele elementului de meniu este obținut corect
        assertEquals("Burger", menuItem.getName());
    }

    @Test
    public void testGetPrice() {
        // Verificarea dacă prețul elementului de meniu este obținut corect
        assertEquals(10.0, menuItem.getPrice(), 0.01);
    }

    @Test
    public void testGetType() {
        // Verificarea dacă tipul elementului de meniu este obținut corect
        assertEquals(MenuItem.LUNCH, menuItem.getType());
    }

    @Test
    public void testGetState() {
        // Verificarea dacă starea elementului de meniu este obținută corect
        assertEquals(0, menuItem.getState());
    }

    // Alte teste pot fi adăugate pentru celelalte metode și scenarii posibile

}
