/**
 * Clasa {@code DatabaseException} reprezintă o excepție specifică operațiunilor de bază de date în cadrul clasei {@link Database}.
 * Aceasta extinde clasa generală {@code Exception} și include un mesaj de eroare pentru o depanare mai bună.
 *
 * @author Alesia Lobont
 * @version 1.0
 */
public class DatabaseException extends Exception {

    /** Mesajul de eroare asociat excepției. */
    public String errMsg;

    /**
     * Construiește o nouă instanță a clasei {@code DatabaseException} cu un mesaj de eroare specificat.
     *
     * @param msg Mesajul de eroare
     */
    public DatabaseException(String msg) {
        errMsg = msg;
    }

    /**
     * Obține mesajul de eroare asociat cu excepția.
     *
     * @return Mesajul de eroare
     */
    public String getErrMessage() {
        return errMsg;
    }
}
