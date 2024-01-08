import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestOrder {

    private Order order;
    private MenuItem menuItem;

    @Before
    public void setUp() {
        // Inițializarea comenzii și a elementului de meniu pentru test
        order = new Order(1, "John Doe");
        menuItem = new MenuItem(1, "Burger", 10.0);
    }

    @Test
    public void testAddItem() {
        // Adaugarea unui element de meniu la comandă
        order.addItem(menuItem, (byte) 2);

        // Verificarea dacă detaliul comenzii a fost adăugat corect
        assertEquals(1, order.getOrderDetail().size());
        assertEquals(2, order.getOrderDetail().get(0).getQuantity());
    }

    @Test
    public void testDeleteItem() {
        // Adaugarea unui element de meniu la comandă
        order.addItem(menuItem, (byte) 2);

        // Ștergerea elementului de meniu la indexul 0
        assertTrue(order.deleteItem(0));

        // Verificarea dacă detaliul comenzii a fost șters corect
        assertEquals(0, order.getOrderDetail().size());
    }

    @Test
    public void testCalculateTotal() {
        // Adaugarea mai multor elemente de meniu la comandă
        order.addItem(menuItem, (byte) 2);
        order.addItem(new MenuItem(2, "Fries", 5.0), (byte) 1);

        // Calcularea totalului comenzii
        order.calculateTotal();

        // Verificarea corectitudinii calculului totalului
        assertEquals(25.0, order.getTotal(), 0.01);
    }

    @Test
    public void testSetState() {
        // Setarea stării comenzii la ORDER_CLOSED
        order.setState(Order.ORDER_CLOSED);

        // Verificarea dacă starea comenzii a fost setată corect
        assertEquals(Order.ORDER_CLOSED, order.getState());
    }

}
