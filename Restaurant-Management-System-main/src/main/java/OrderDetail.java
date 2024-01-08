/**
 * Clasa {@code OrderDetail} reprezintă un detaliu al comenzii în cadrul sistemului de gestionare a unui restaurant.
 * Această clasă stochează informații despre un element de meniu din comandă, precum ID-ul, numele, prețul, cantitatea și prețul total.
 *
 * <p>
 * Un detaliu al comenzii este asociat unui element de meniu și păstrează informații despre acesta, precum și cantitatea comandată și prețul total calculat.
 * </p>
 *
 * <p>
 * Această clasă oferă metode pentru obținerea atributelor detaliului comenzii, precum și pentru adăugarea de cantitate la detaliu.
 * </p>
 *
 * @author Numele Tău
 * @version 1.0
 */
public class OrderDetail {

    /** ID-ul elementului de meniu asociat detaliului comenzii. */
    private final int itemID;
    /** Numele elementului de meniu asociat detaliului comenzii. */
    private final String itemName;
    /** Prețul elementului de meniu asociat detaliului comenzii. */
    private final double price;
    /** Cantitatea de elemente de meniu comandate. */
    private byte quantity;
    /** Prețul total calculat pentru detaliul comenzii. */
    private double totalPrice;

    /**
     * Construiește o nouă instanță a clasei {@code OrderDetail} cu elementul de meniu și cantitatea specificate.
     * Inițializează ID-ul, numele, prețul, cantitatea și prețul total ale detaliului comenzii pe baza informațiilor elementului de meniu.
     *
     * @param newMenuItem Elementul de meniu asociat detaliului comenzii
     * @param newQuantity Cantitatea de elemente de meniu comandate
     */
    public OrderDetail(MenuItem newMenuItem, byte newQuantity) {
        this.itemID = newMenuItem.getID();
        this.itemName = newMenuItem.getName();
        this.price = newMenuItem.getPrice();
        this.quantity = newQuantity;
        this.totalPrice = this.price * this.quantity;
    }

    /**
     * Obține ID-ul elementului de meniu asociat detaliului comenzii.
     *
     * @return ID-ul elementului de meniu
     */
    public int getItemID() {
        return this.itemID;
    }

    /**
     * Obține numele elementului de meniu asociat detaliului comenzii.
     *
     * @return Numele elementului de meniu
     */
    public String getItemName() {
        return this.itemName;
    }

    /**
     * Obține cantitatea de elemente de meniu comandate.
     *
     * @return Cantitatea de elemente de meniu comandate
     */
    public byte getQuantity() {
        return this.quantity;
    }

    /**
     * Obține prețul total calculat pentru detaliul comenzii.
     *
     * @return Prețul total calculat
     */
    public double getTotalPrice() {
        return this.totalPrice;
    }

    /**
     * Adaugă o anumită cantitate la detaliul comenzii și recalculează prețul total.
     *
     * @param add Cantitatea adăugată la detaliul comenzii
     */
    public void addQuantity(byte add) {
        quantity += add;
        totalPrice = price * quantity;
    }
}
