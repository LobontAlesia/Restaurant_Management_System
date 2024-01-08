/**
 * Clasa {@code Main} conține metoda principală {@code main}, care este punctul de intrare în program.
 * Aici se creează o instanță a clasei {@link Controller_GUI}, inițializând astfel interfața grafică a aplicației.
 *
 * <p>
 * Această clasă servește ca punct de pornire pentru execuția aplicației.
 * </p>
 *
 * @author Alesia Lobont
 * @version 1.0
 */
public class Main {

    /**
     * Metoda principală care inițializează interfața grafică a aplicației, creând o instanță a clasei {@link Controller_GUI}.
     *
     * @param args Argumentele liniei de comandă (nu sunt utilizate în acest context)
     */
    public static void main(String[] args) {
        new Controller_GUI();
    }
}
