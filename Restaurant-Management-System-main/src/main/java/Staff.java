import java.util.*;
import java.text.*;

/**
 * Clasa abstractă {@code Staff} reprezintă un angajat în cadrul sistemului de gestionare a unui restaurant.
 * Această clasă stochează informații generale despre un angajat, precum ID-ul, numele, parola, starea și alte detalii legate de programul de lucru și salarizare.
 *
 * <p>
 * Un angajat este un element central în sistemul de gestionare, iar această clasă oferă funcționalități de bază pentru gestionarea informațiilor despre angajați și calculele legate de programul lor de lucru și salarizare.
 * </p>
 *
 * <p>
 * Această clasă este abstractă și furnizează metode abstracte pentru setarea ratei de salarizare și calcularea salariului, astfel încât subclasele (Employee și Manager) să poată implementa aceste funcționalități specific pentru fiecare tip de angajat.
 * </p>
 *
 * @author Alesia Lobont
 * @version 1.0
 */

public abstract class Staff {
    /** ID-ul angajatului. */
    private int ID;
    /** Numele de familie al angajatului. */
    private String lastName;
    /** Prenumele angajatului. */
    private String firstName;
    /** Parola angajatului. */
    private String password;
    /** Starea angajatului (0 - inactiv, 1 - activ, 2 - a terminat munca). */
    private byte state;


    /** Timpul de începere a programului de lucru al angajatului. */
    protected Date startworktime;
    /** Timpul de încheiere a programului de lucru al angajatului. */
    protected Date finishworktime;
    /** Rata de salarizare a angajatului. */
    protected double wagerate;



    /**
     * Construiește o nouă instanță a clasei {@code Staff} cu informațiile generale ale angajatului.
     *
     * @param newID ID-ul angajatului
     * @param newFirstName Prenumele angajatului
     * @param newLastName Numele de familie al angajatului
     * @param newPassword Parola angajatului
     */
    public Staff(int newID, String newFirstName, String newLastName, String newPassword) {
        setID(newID);
        setFirstName(newFirstName);
        setLastName(newLastName);
        setPassword(newPassword);

        startworktime = null;
        finishworktime = null;
        state = 0;
    }

    public Staff() {
        setID(0);
        setFirstName("John");
        setLastName("Doe");
        setPassword("password");

        startworktime = null;
        finishworktime = null;
        state = 0;
    }

    /**
     * Setează ID-ul angajatului.
     * @param newID
     */
    protected void setID(int newID) {
        this.ID = newID;
    }

    /**
     * Setează numele de familie al angajatului.
     * @param newLastName
     */
    protected void setLastName(String newLastName) {
        this.lastName = newLastName;
    }

    /**
     * Setează prenumele angajatului.
     * @param newFirstName
     */
    protected void setFirstName(String newFirstName) {
        this.firstName = newFirstName;
    }

    /**
     * Setează parola angajatului.
     * @param newPassword
     */
    protected void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Obține ID-ul angajatului.
     * @return
     */
    public int getID() {
        return this.ID;
    }

    /**
     * Obține numele de familie al angajatului.
     * @return
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Obține prenumele angajatului.
     * @return
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Obține numele complet al angajatului.
     * @return
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Obține parola angajatului.
     * @return
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Setează rata de salarizare a angajatului.
     * @return
     */
    public double getWagerate() {
        return this.wagerate;
    }

    /**
     * Setează starea angajatului.
     */
    public static final byte WORKSTATE_ACTIVE = 1;
    public static final byte WORKSTATE_FINISH = 2;

    /**
     * Obține starea angajatului.
     * @return
     */
    public byte getWorkState() {
        return this.state;
    }

    /**
     * Obține timpul de începere a programului de lucru al angajatului.
     * @return
     */
    public String getStartTime() {
        if (startworktime == null)
            return "getStartTime Error";
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(startworktime);
    }

    /**
     * Obține timpul de încheiere a programului de lucru al angajatului.
     * @return
     */
    public String getFinishTime() {
        if (finishworktime == null)
            return "getFinishTime Error";
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(finishworktime);
    }

    /**
     * Obține timpul total de lucru al angajatului.
     */
    public void clockIn() {
        startworktime = new Date(System.currentTimeMillis());
        state = WORKSTATE_ACTIVE;
    }

    /**
     * Obține timpul total de lucru al angajatului.
     */
    public void clockOut() {
        if (state != WORKSTATE_ACTIVE)
            return;
        finishworktime = new Date(System.currentTimeMillis());
        state = WORKSTATE_FINISH;
    }

    /**
     * Obține timpul total de lucru al angajatului.
     * @return
     */
    //startworktime si finishworktime sunt de tip Date
    //getTime() returneaza timpul in milisecunde
    //diferenta dintre finishworktime si startworktime este impartita la 60000 pentru a converti milisecundele in minute
    //daca diferenta este mai mica de 15 minute, addTime este 0
    //daca diferenta este mai mica de 30 minute, addTime este 0.25
    //daca diferenta este mai mica de 45 minute, addTime este 0.5
    //daca diferenta este mai mare de 45 minute, addTime este 0.75
    //baseTime este timpul de lucru in ore
    //fraction este timpul de lucru in minute
    //returneaza timpul de lucru in ore
    public double calculateWorkTime() {
        if (getWorkState() != WORKSTATE_FINISH)
            return 0;

        long diffTimeMin = (finishworktime.getTime() - startworktime.getTime()) / 60000;//convert Milli sec to Minutes
        long baseTime = diffTimeMin / 60;
        long fraction = diffTimeMin % 60;
        double addTime;

        if (fraction < 15)
            addTime = 0;
        else if (fraction < 30)
            addTime = 0.25;
        else if (fraction < 45)
            addTime = 0.5;
        else
            addTime = 0.75;

        return (double) baseTime + addTime;
    }

    protected abstract void setWagerate(double newRate);

    protected abstract double calculateWages();

    public void setStartWorkTime(Date parse) {
        startworktime = parse;
    }

    public void setFinishWorkTime(Date parse) {
        finishworktime = parse;
    }

    public void setWorkState(byte workstateFinish) {
        state = workstateFinish;
    }
}
