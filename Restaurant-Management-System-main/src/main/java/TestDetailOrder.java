import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestDetailOrder {

    private MenuItem menuItem;
    private OrderDetail orderDetail;

    @Before
    public void setUp() {
        // Inițializarea elementului de meniu și detaliului comenzii pentru test
        menuItem = new MenuItem(1, "Burger", 10.0);
        orderDetail = new OrderDetail(menuItem, (byte) 2);
    }

    @Test
    public void testGetItemID() {
        // Verificarea dacă ID-ul elementului de meniu este obținut corect
        assertEquals(1, orderDetail.getItemID());
    }

    @Test
    public void testGetItemName() {
        // Verificarea dacă numele elementului de meniu este obținut corect
        assertEquals("Burger", orderDetail.getItemName());
    }

    @Test
    public void testGetQuantity() {
        // Verificarea dacă cantitatea este obținută corect
        assertEquals(2, orderDetail.getQuantity());
    }

    @Test
    public void testGetTotalPrice() {
        // Verificarea dacă prețul total este obținut corect
        assertEquals(20.0, orderDetail.getTotalPrice(), 0.01);
    }

    @Test
    public void testAddQuantity() {
        // Adăugarea unei cantități la detaliul comenzii
        orderDetail.addQuantity((byte) 3);

        // Verificarea dacă cantitatea și prețul total au fost actualizate corect
        assertEquals(5, orderDetail.getQuantity());
        assertEquals(50.0, orderDetail.getTotalPrice(), 0.01);
    }


}
