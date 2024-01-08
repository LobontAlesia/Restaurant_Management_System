/**
 * Clasa {@code Employee} reprezintă un angajat în cadrul sistemului de gestionare a unui restaurant și extinde clasa de bază {@link Staff}.
 * Această clasă este utilizată pentru a modela angajații care nu au rol de manager și are funcționalități specifice salariilor și calculului acestora.
 *
 * <p>
 * O instanță a acestei clase are un salariu minim stabilit și oferă metode pentru ajustarea ratei de salariu și calcularea totalului de salarii.
 * </p>
 *
 * @author Alesia Lobont
 * @version 1.0
 */
public class Employee extends Staff {

    /** Rata minimă de salariu pentru angajați. */
    private static final double MINIMUM_RATE = 13.5;

    /**
     * Construiește o nouă instanță a clasei {@code Employee} cu ID, nume, prenume și parolă specificate.
     * Ratei de salariu i se atribuie valoarea minimă prestabilită.
     *
     * @param newID          ID-ul noului angajat
     * @param newFirstName   Prenumele noului angajat
     * @param newLastName    Numele noului angajat
     * @param newPassword    Parola noului angajat
     */
    public Employee(int newID, String newFirstName, String newLastName, String newPassword) {
        super(newID, newFirstName, newLastName, newPassword);
        wagerate = MINIMUM_RATE;
    }

    /**
     * Setează rata de salariu a angajatului, asigurându-se că aceasta nu scade sub valoarea minimă.
     *
     * @param newRate Noua rată de salariu
     */
    public void setWagerate(double newRate) {
        wagerate = Math.max(newRate, MINIMUM_RATE);
    }

    /**
     * Calculează salariile angajatului, înmulțind rata de salariu cu timpul total de muncă.
     *
     * @return Totalul de salarii al angajatului
     */
    public double calculateWages() {
        return wagerate * calculateWorkTime();
    }
}
