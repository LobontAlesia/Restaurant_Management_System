import java.util.*;

/**
 * Clasa {@code Order} reprezintă o comandă în cadrul sistemului de gestionare a unui restaurant.
 * Această clasă oferă funcționalități pentru gestionarea detaliilor comenzii, precum starea, totalul și lista de detalii ale comenzii.
 *
 * <p>
 * Starea comenzii poate fi una din următoarele: ARRIVE (0), ORDER_CLOSED (1) sau ORDER_CANCELED (2).
 * </p>
 *
 * <p>
 * O comandă are un identificator unic, un ID al personalului asociat comenzii, numele personalului, starea, totalul comenzii și o listă de detalii ale comenzii.
 * </p>
 *
 * <p>
 * Această clasă oferă metode pentru setarea și obținerea atributelor comenzii, adăugarea și ștergerea de detalii ale comenzii, precum și calculul totalului comenzii.
 * </p>
 *
 * @author Numele Tău
 * @version 1.0
 */
public class Order {

    /** Starea comenzii: Arrive (0). */
    final public static int ARRIVE = 0;
    /** Starea comenzii: Closed (1). */
    final public static int ORDER_CLOSED = 1;
    /** Starea comenzii: Canceled (2). */
    final public static int ORDER_CANCELED = 2;

    /** Identificatorul unic al comenzii. */
    private int orderID;
    /** ID-ul personalului asociat comenzii. */
    private final int staffID;
    /** Numele personalului asociat comenzii. */
    private final String staffName;
    /** Starea comenzii. */
    private int state;  //0:arrive 1:closed 2:canceled
    /** Totalul comenzii. */
    private double total;
    /** Lista de detalii ale comenzii. */
    private final ArrayList<OrderDetail> orderDetailList = new ArrayList<>();

    /**
     * Construiește o nouă instanță a clasei {@code Order} cu ID-ul personalului și numele personalului specificate.
     * Inițializează starea comenzii la ARRIVE (0) și totalul comenzii la 0.
     *
     * @param newStaffID   ID-ul personalului asociat comenzii
     * @param newStaffName Numele personalului asociat comenzii
     */
    public Order(int newStaffID, String newStaffName) {
        this.orderID = -1;
        this.state = ARRIVE;
        this.staffID = newStaffID;
        this.staffName = newStaffName;
        this.total = 0;
    }

    /**
     * Obține ID-ul comenzii.
     *
     * @return ID-ul comenzii
     */
    int getOrderID() {
        return this.orderID;
    }

    /**
     * Obține ID-ul personalului asociat comenzii.
     *
     * @return ID-ul personalului asociat comenzii
     */
    int getStaffID() {
        return this.staffID;
    }

    /**
     * Obține numele personalului asociat comenzii.
     *
     * @return Numele personalului asociat comenzii
     */
    String getStaffName() {
        return this.staffName;
    }

    /**
     * Obține starea comenzii.
     *
     * @return Starea comenzii
     */
    int getState() {
        return this.state;
    }

    /**
     * Obține totalul comenzii.
     *
     * @return Totalul comenzii
     */
    double getTotal() {
        return this.total;
    }

    /**
     * Obține lista de detalii ale comenzii.
     *
     * @return Lista de detalii ale comenzii
     */
    ArrayList<OrderDetail> getOrderDetail() {
        return this.orderDetailList;
    }

    /**
     * Setează ID-ul comenzii.
     *
     * @param newID Noul ID al comenzii
     */
    public void setOrderID(int newID) {
        this.orderID = newID;
    }

    /**
     * Setează starea comenzii.
     *
     * @param state Noua stare a comenzii
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Adaugă un element de meniu cu o anumită cantitate la comandă.
     *
     * @param rNewMenuItem Elementul de meniu care va fi adăugat la comandă
     * @param quantity      Cantitatea de elemente de meniu adăugate la comandă
     */
    public void addItem(MenuItem rNewMenuItem, byte quantity) {
        Iterator<OrderDetail> it = orderDetailList.iterator();
        OrderDetail re;

        boolean found = false;

        while (it.hasNext() && !found) {
            re = it.next();
            if (rNewMenuItem.getID() == re.getItemID()) {
                found = true;
                re.addQuantity(quantity);
            }
        }

        if (!found) {
            OrderDetail detail = new OrderDetail(rNewMenuItem, quantity);
            orderDetailList.add(detail);
        }

        calculateTotal();
    }

    /**
     * Șterge un element de meniu din comandă la un anumit index.
     *
     * @param index Indexul la care va fi șters elementul de meniu
     * @return {@code true} dacă ștergerea a avut succes, {@code false} altfel
     */
    public boolean deleteItem(int index) {
        try {
            orderDetailList.remove(index);
            calculateTotal();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Calculează totalul comenzii pe baza prețurilor și cantităților din lista de detalii ale comenzii.
     */
    public void calculateTotal() {
        total = 0;
        OrderDetail re;
        for (OrderDetail orderDetail : orderDetailList) {
            re = orderDetail;
            total += re.getTotalPrice();
        }
    }

}
