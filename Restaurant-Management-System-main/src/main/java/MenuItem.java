/**
 * Clasa {@code MenuItem} reprezintă un element de meniu în cadrul sistemului de gestionare a unui restaurant.
 * Această clasă oferă funcționalități pentru definirea tipului elementului de meniu și gestionarea detaliilor acestuia, precum numele, prețul, tipul și starea.
 *
 * <p>
 * Tipurile de elemente de meniu sunt definite ca constante în clasă (e.g., BREAKFAST, LUNCH, DINNER, DESSERT).
 * </p>
 *
 * <p>
 * Fiecare element de meniu are un identificator unic, un nume, un preț, un tip, o stare și un preț de promovare (în caz de existență a unei promoții).
 * </p>
 *
 * <p>
 * Această clasă oferă metode pentru setarea și obținerea atributelor elementului de meniu.
 * </p>
 *
 * <p>
 * Starea și prețul de promovare pot fi ajustate pentru a reflecta promoțiile active pe elementul de meniu.
 * </p>
 *
 * @author Alesia Lobont
 * @version 1.0
 */
public class MenuItem {

    /** Tipul de element de meniu: Mic dejun. */
    public final static int BREAKFAST = 1;
    /** Tipul de element de meniu: Prânz. */
    public final static int LUNCH = 2;
    /** Tipul de element de meniu: Cină. */
    public final static int DINNER = 3;
    /** Tipul de element de meniu: Desert. */
    public final static int DESSERT = 4;

    /** Identificatorul unic al elementului de meniu. */
    private final int ID;
    /** Numele elementului de meniu. */
    private String name;
    /** Tipul elementului de meniu. */
    private byte type;
    /** Prețul elementului de meniu. */
    private double price;

    /** Starea curentă a elementului de meniu (e.g., promoție sau fără promoție). */
    private byte state;

    /**
     * Construiește un nou element de meniu cu ID, nume, preț și tip specificate.
     * Starea și prețul de promovare sunt inițializate la 0.
     *
     * @param newID          ID-ul unic al elementului de meniu
     * @param newName        Numele elementului de meniu
     * @param newPrice       Prețul elementului de meniu
     * @param newType        Tipul elementului de meniu
     */
    public MenuItem(int newID, String newName, double newPrice, byte newType) {
        this.ID = newID;
        this.name = newName;
        this.price = newPrice;
        this.type = newType;
        this.state = 0;
    }

    /**
     * Setează numele elementului de meniu.
     *
     * @param newName Noul nume al elementului de meniu
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Setează prețul elementului de meniu.
     *
     * @param newPrice Noul preț al elementului de meniu
     */
    public void setPrice(double newPrice) {
        this.price = newPrice;
    }

    /**
     * Setează tipul elementului de meniu.
     *
     * @param newType Noul tip al elementului de meniu
     */
    public void setType(byte newType) {
        this.type = newType;
    }

    /**
     * Setează starea și prețul de promovare al elementului de meniu.
     *
     * @param newState     Noua stare a elementului de meniu
     * @param tempPrice     Noul preț de promovare (în caz de promoție)
     */
    public void setState(byte newState, double tempPrice) {
        this.state = newState;
    }

    /**
     * Resetează starea și prețul de promovare ale elementului de meniu la valorile implicite (0).
     */
    public void resetState() {
        this.state = 0;
    }

    /**
     * Obține ID-ul unic al elementului de meniu.
     *
     * @return ID-ul elementului de meniu
     */
    int getID() {
        return this.ID;
    }

    /**
     * Obține numele elementului de meniu.
     *
     * @return Numele elementului de meniu
     */
    String getName() {
        return this.name;
    }

    /**
     * Obține prețul elementului de meniu, luând în considerare starea și prețul de promovare (în caz de promoție).
     *
     * @return Prețul actual al elementului de meniu
     */
    double getPrice() {
            return this.price;
    }

    /**
     * Obține tipul elementului de meniu.
     *
     * @return Tipul elementului de meniu
     */
    byte getType() {
        return this.type;
    }

    /**
     * Obține starea elementului de meniu.
     *
     * @return Starea elementului de meniu
     */
    byte getState() {
        return this.state;
    }

    public MenuItem(int newID, String newName, double newPrice) {
        this.ID = newID;
        this.name = newName;
        this.price = newPrice;
    }
}
