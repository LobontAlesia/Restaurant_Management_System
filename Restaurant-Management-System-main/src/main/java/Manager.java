/**
 * Clasa {@code Manager} reprezintă un manager în cadrul sistemului de gestionare a unui restaurant și extinde clasa de bază {@link Staff}.
 * Această clasă este utilizată pentru a modela managerii care au funcționalități specifice legate de salarii și calculul acestora.
 *
 * <p>
 * O instanță a acestei clase are o rată minimă de salariu stabilită și oferă metode pentru ajustarea ratei de salariu și calcularea totalului de salarii.
 * </p>
 *
 * <p>
 * De asemenea, managerul are un comportament specific în ceea ce privește calculul salariilor în funcție de starea de muncă.
 * </p>
 *
 * @author Alesia Lobont
 * @version 1.0
 */
public class Manager extends Staff {

    /** Rata minimă de salariu pentru manageri. */
    private static final double MINIMUM_RATE = 100.0;

    /**
     * Construiește o nouă instanță a clasei {@code Manager} cu ID, nume, prenume și parolă specificate.
     * Ratei de salariu i se atribuie valoarea minimă prestabilită.
     *
     * @param newID          ID-ul noului manager
     * @param newFirstName   Prenumele noului manager
     * @param newLastName    Numele noului manager
     * @param newPassword    Parola noului manager
     */
    public Manager(int newID, String newFirstName, String newLastName, String newPassword) {
        super(newID, newFirstName, newLastName, newPassword);
        wagerate = MINIMUM_RATE;
    }

    /**
     * Setează rata de salariu a managerului, asigurându-se că aceasta nu scade sub valoarea minimă.
     *
     * @param newRate Noua rată de salariu
     */
    /**
     * Setează rata de salariu a managerului, asigurându-se că aceasta nu scade sub valoarea minimă.
     *
     * @param newRate Noua rată de salariu
     */
    public void setWagerate(double newRate) {
        if (newRate < MINIMUM_RATE)
            newRate = MINIMUM_RATE;
        wagerate = newRate;
    }


    /**
     * Calculează salariile managerului în funcție de starea de muncă.
     * Dacă starea nu este finalizată, returnează 0, altfel returnează rata de salariu.
     *
     * @return Totalul de salarii al managerului
     */
    public double calculateWages() {
        if (getWorkState() != WORKSTATE_FINISH)
            return 0;

        return this.wagerate;
    }

}
