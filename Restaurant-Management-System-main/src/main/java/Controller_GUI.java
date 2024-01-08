import java.util.*;
import java.text.*;

/**
 * Clasa {@code Controller_GUI} reprezintă controller-ul din cadrul sistemului de gestionare a unui restaurant.
 * Această clasă oferă funcționalități pentru gestionarea datelor din sistem, precum și pentru interacțiunea cu interfața grafică.
 */
public class Controller_GUI {
    private final UserInterface_GUI view;
    private final Database cDatabase;
    private int current_userid;
    private String current_username;
    private final String today_date;

    private int todaysordercnt;     //Today's order count
    private double totalsales;         //Today's total sales
    private int todaysCancelCnt;    //Today's cancel count
    private double cancelTotal;        //Total cost of today's canceled orders


    private String errorMessage;

    /**
     * Construiește o nouă instanță a clasei {@code Controller_GUI}.
     * Inițializează interfața grafică și baza de date.
     * Dacă baza de date nu poate fi încărcată, se afișează un mesaj de eroare și se închide aplicația.
     * Dacă baza de date este încărcată cu succes, se inițializează datele legate de vânzări.
     */
    public Controller_GUI() {
        try {
            this.cDatabase = new Database();
            cDatabase.loadData();
        } catch (DatabaseException de) {
            System.out.println(de.getErrMessage()); // stack trace better
            System.exit(0);
            throw new IllegalStateException(); // to skip the initialization of cDatabase O_O
        }

        view = new UserInterface_GUI(this);

        Date date = new Date();
        SimpleDateFormat stf = new SimpleDateFormat("yyyy/MM/dd");
        today_date = stf.format(date);
        view.setVisible(true);
        view.setTodaysDate();

        todaysordercnt = 0;
        totalsales = 0;
        todaysCancelCnt = 0;
        cancelTotal = 0;
    }

    /**
     * Setează mesajul de eroare.
     * @param errorMessage
     */
    private void seterrormessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Obține mesajul de eroare.
     * @return
     */
    public String get_error_message() {
        String result = this.errorMessage;
        this.errorMessage = "";
        return result;
    }

    /**
     * Obține numărul comenzilor de astăzi.
     * @return
     */
    public int getTodaysordercnt() {
        return this.todaysordercnt;
    }

    /**
     * Obține numărul anulărilor de astăzi.
     * @return
     */
    public int getTodaysCancelCnt() {
        return this.todaysCancelCnt;
    }

    /**
     * Obține totalul vânzărilor de astăzi.
     * @return
     */
    public double getTotalsales() {
        return this.totalsales;
    }

    /**
     * Obține totalul anulărilor de astăzi.
     * @return
     */
    public double getCancelTotal() {
        return this.cancelTotal;
    }

    public double getOrderTotalCharge(int orderID) {
        return cDatabase.getOrderTotalCharge(orderID);
    }

    public int getOrderState(int orderID) {
        return cDatabase.getOrderState(orderID);
    }

    public String getCurrent_username() {
        return this.current_username;
    }

    /**
     * Verifică dacă utilizatorul curent este un manager.
     * @return
     */
    public boolean checkIfUserClockedOut() {
        Staff rStaff = cDatabase.findStaffByID(current_userid);

        if (rStaff == null) return false;
        return rStaff.getWorkState() == Staff.WORKSTATE_ACTIVE;
    }

    // Login

    // Find user

    /**
     * Verifică dacă utilizatorul curent este un manager.
     * @param inputID
     * @param inputPassword
     * @param isManager
     * @return
     */
    public boolean loginCheck(int inputID, String inputPassword, boolean isManager) {
        String searchClassName;

        //---------search user----------
        Staff rStaff = cDatabase.findStaffByID(inputID);

        if (isManager) searchClassName = "Manager";
        else searchClassName = "Employee";

        if (rStaff != null)//User data is found
        {
            //Search only particular target(Manager or Employee)
            if (rStaff.getClass().getName().equalsIgnoreCase(searchClassName)) {
                if (rStaff.getPassword().equals(inputPassword)) {
                    if (rStaff.getWorkState() == 0)  //Not clocked in yet
                    {
                        rStaff.clockIn();
                    }
                    if (isManager) {
                        view.changeMode(UserInterface_GUI.MODE_MANAGER);
                    } else {
                        view.changeMode(UserInterface_GUI.MODE_EMPLOYEE);
                    }
                    current_userid = inputID;
                    current_username = rStaff.getFullName();
                    view.setLoginUserName(current_username);  //show username on the view

                    return true; //Login success
                } else {
                    seterrormessage("Wrong Password!.");
                    return false;
                }
            } else {
                seterrormessage("Not found.");
                return false;
            }
        } else {
            seterrormessage("Not found.");
            return false;
        }

    }

    // Logout (Set state as Anonymous)
    public void userLogout() {
        current_userid = 0;
        view.setLoginUserName("");
    }

    // Staff management

    /**
     * Adaugă un nou angajat în baza de date.
     * @param newID
     * @param newPassword
     * @param newFirstName
     * @param newLastName
     * @param isManager
     * @return
     */
    public boolean addNewStaff(int newID, String newPassword, String newFirstName, String newLastName, boolean isManager) {
        Staff rStaff = cDatabase.findStaffByID(newID);
        if (rStaff != null) {
            seterrormessage("ID:" + newID + " is already used by " + rStaff.getFullName());
            return false;
        }

        try {
            cDatabase.addStaff(newID, newPassword, newFirstName, newLastName, isManager);
            return true;
        } catch (DatabaseException de) {
            seterrormessage(de.getErrMessage());
            return false;
        }
    }

    /**
     * Actualizează datele unui angajat.
     * @param id
     * @param newPassword
     * @param newFirstName
     * @param newLastName
     * @return
     */
    public boolean updateStaff(int id, String newPassword, String newFirstName, String newLastName) {
        try {
            cDatabase.editStaffData(id, newPassword, newFirstName, newLastName);
            return true;
        } catch (DatabaseException de) {
            seterrormessage(de.getErrMessage());
            return false;
        }
    }

    /**
     * Șterge un angajat din baza de date.
     * @param id
     * @return
     */
    public boolean deleteStaff(int id) {
        Staff rStaff = cDatabase.findStaffByID(id);
        if (rStaff == null) {
            seterrormessage("StaffID:" + id + " is not found.");
            return false;
        }

        try {
            cDatabase.deleteStaff(rStaff);
        } catch (DatabaseException de) {
            seterrormessage(de.getErrMessage());
            return false;
        }
        return true;
    }

    public Staff getStaffData(int staffID) {
        return cDatabase.findStaffByID(staffID);
    }

    public void clockOut() {
        clockOut(current_userid);
    }

    /**
     * Marchează un angajat ca fiind plecat de la serviciu.
     * @param staffID
     * @return
     */
    public boolean clockOut(int staffID) {
        Staff rStaff = cDatabase.findStaffByID(staffID);

        byte state = rStaff.getWorkState();
        boolean result = false;
        switch (state) {
            case Staff.WORKSTATE_ACTIVE -> {
                rStaff.clockOut();
                result = true;
            }
            case Staff.WORKSTATE_FINISH -> seterrormessage("Staff:" + rStaff.getFullName() + " already clocked out.");
            default -> seterrormessage("Staff:" + rStaff.getFullName() + "has not been on work today.");
        }

        return result;
    }

    public void clockOutAll() {
        cDatabase.forthClockOutAllStaff();
    }

    // Menu management

    /**
     * Adaugă un nou element de meniu în baza de date.
     * @param newID
     * @param newName
     * @param newPrice
     * @param menuType
     * @return
     */
    public boolean addNewMenuItem(int newID, String newName, double newPrice, byte menuType) {
        MenuItem rMenuItem = cDatabase.findMenuItemByID(newID);
        if (rMenuItem != null) {
            seterrormessage("ID:" + newID + " is already used by " + rMenuItem.getName());
            return false;
        }

        try {
            cDatabase.addMenuItem(newID, newName, newPrice, menuType);
            return true;
        } catch (DatabaseException de) {
            seterrormessage(de.getErrMessage());
            return false;
        }
    }

    /**
     * Actualizează datele unui element de meniu.
     * @param id
     * @param newName
     * @param newPrice
     * @param menuType
     * @return
     */
    public boolean updateMenuItem(int id, String newName, double newPrice, byte menuType) {
        try {
            cDatabase.editMenuItemData(id, newName, newPrice, menuType);
            return true;
        } catch (DatabaseException de) {
            seterrormessage(de.getErrMessage());
            return false;
        }
    }

    /**
     * Șterge un element de meniu din baza de date.
     * @param id
     * @return
     */
    public boolean deleteMenuItem(int id) {
        MenuItem rMenuItem = cDatabase.findMenuItemByID(id);
        if (rMenuItem == null) {
            seterrormessage("Menu item ID:" + id + " is not found.");
            return false;
        }

        try {
            cDatabase.deleteMenuItem(rMenuItem);
        } catch (DatabaseException de) {
            seterrormessage(de.getErrMessage());
            return false;
        }
        return true;
    }

    public MenuItem getMenuItemData(int menuItemID) {
        return cDatabase.findMenuItemByID(menuItemID);
    }

    // Order management

    /**
     * Creează o nouă comandă.
     * @return
     */
    public int createOrder() {
        return cDatabase.addOrder(current_userid, current_username);
    }

    /**
     * Adaugă un element de meniu la o comandă.
     * @param orderID
     * @param addItemID
     * @param addItemQuantity
     * @return
     */
    public boolean addNewOrderItem(int orderID, int addItemID, byte addItemQuantity) {
        Order rOrder = cDatabase.findOrderByID(orderID);
        if (current_userid != rOrder.getStaffID()) {
            seterrormessage("You are not eligible to edit the order.\nThe order belongs to " + rOrder.getStaffName() + ")");
            return false;
        }

        MenuItem rNewItem;

        rNewItem = cDatabase.findMenuItemByID(addItemID);
        if (rNewItem == null) {
            seterrormessage("MenuID[" + addItemID + "]is not found.");
            return false;
        }
        cDatabase.addOrderItem(orderID, rNewItem, addItemQuantity);
        return true;
    }

    /**
     * Șterge un element de meniu dintr-o comandă.
     * @param orderID
     * @param deleteNo
     * @return
     */
    public boolean deleteOrderItem(int orderID, int deleteNo) {
        Order rOrder = cDatabase.findOrderByID(orderID);
        if (current_userid != rOrder.getStaffID()) {
            seterrormessage("You are not eligible to delete the order.\nThe order belongs to " + rOrder.getStaffName() + ")");
            return false;
        }

        deleteNo -= 1;  //index actually starts from zero
        if (!cDatabase.deleteOrderItem(orderID, deleteNo)) {
            seterrormessage("Not found.");
            return false;
        }
        return true;
    }

    /**
     * Închide o comandă.
     * @param closeOrderID
     * @return
     */
    public boolean closeOrder(int closeOrderID) {
        Order rOrder = cDatabase.findOrderByID(closeOrderID);
        if (current_userid != rOrder.getStaffID()) {
            seterrormessage("You are not eligible to delete the order.\n(The order belongs to " + rOrder.getStaffName() + ")");
            return false;
        }

        if (rOrder.getState() != 0) {
            seterrormessage("The order is already closed or canceled.");
            return false;
        }
        cDatabase.closeOrder(closeOrderID);
        todaysordercnt++;
        totalsales += rOrder.getTotal();
        return true;
    }

    /**
     * Anulează o comandă.
     * @param cancelOrderID
     * @return
     */
    public boolean cancelOrder(int cancelOrderID) {
        Order rOrder = cDatabase.findOrderByID(cancelOrderID);
        if (current_userid != rOrder.getStaffID()) {
            seterrormessage("You are not eligible to delete the order.\n(The order belongs to " + rOrder.getStaffName() + ")");
            return false;
        }

        if (rOrder.getState() != 0) {
            seterrormessage("The order is already closed or canceled.");
            return false;
        }

        cDatabase.cancelOrder(cancelOrderID);
        todaysCancelCnt++;
        cancelTotal += rOrder.getTotal();
        return true;
    }

    public void closeAllOrder() {
        cDatabase.closeAllOrder();
    }

    /**
     * Obține lista de comenzi.
     * @return
     */
    public String generateSalesReport() {
        if (!cDatabase.checkIfAllOrderClosed()) {
            seterrormessage("All orders must be closed or canceled before generate reports.");
            return null;
        }

        try {
            return cDatabase.generateOrderReport(today_date);
        } catch (DatabaseException de) {
            seterrormessage(de.getErrMessage());
            return null;
        }
    }

    /**
     * Obține lista de elemente de meniu.
     * @return
     */
    public String generatePaymentReport() {
        if (!cDatabase.checkIfAllStaffCheckout()) {
            seterrormessage("All staff must be checked out before generate a payment report.");
            return null;
        }

        try {
            return cDatabase.generatePaymentReport(today_date);
        } catch (DatabaseException de) {
            seterrormessage(de.getErrMessage());
            return null;
        }
    }

    // Create string lists

    /**
     * Creează o listă de angajați.
     * @return
     */
    public ArrayList<String> createStaffList() {
        Iterator<Staff> it = cDatabase.getStaffList().iterator();
        ArrayList<String> initData = new ArrayList<>();

        while (it.hasNext()) {
            Staff re = it.next();
            String fullName = re.getFullName();
            String output = String.format("Staff ID:%4d  Name:%-25s",
                    re.getID(), fullName);
            switch (re.getWorkState()) {
                case Staff.WORKSTATE_ACTIVE -> output += "[From:" + re.getStartTime() + "]";
                case Staff.WORKSTATE_FINISH ->
                        output += "[From:" + re.getStartTime() + " to " + re.getFinishTime() + "]";
                default -> output += "[Not on work]";
            }

            if (re instanceof Manager) {
                output += " * Manager *";
            }
            initData.add(output);
        }

        return initData;
    }

    /**
     * Creează o listă de elemente de meniu.
     * @return
     */
    public ArrayList<String> createOrderList() {
        Iterator<Order> it = cDatabase.getOrderList().iterator();
        String state;
        ArrayList<String> initData = new ArrayList<>();
        String output;

        while (it.hasNext()) {
            Order re = it.next();
            state = switch (re.getState()) {
                case Order.ORDER_CLOSED -> "Closed";
                case Order.ORDER_CANCELED -> "Canceled";
                default -> "-";
            };

            output = String.format("Order ID:%4d  StaffName:%-20s  Total:$%5.2f State:%-8s\n",
                    re.getOrderID(), re.getStaffName(), re.getTotal(), state);
            initData.add(output);
        }
        if (initData.isEmpty())
            initData.add("No order.");
        return initData;
    }

    /**
     * Creează o listă de elemente de meniu.
     * @param orderID
     * @return
     */
    public ArrayList<String> createOrderItemlList(int orderID) {
        Order rOrder = cDatabase.findOrderByID(orderID);
        ArrayList<String> initData = new ArrayList<>();

        if (rOrder == null) {
            initData.add("No order information");
            return initData;
        }

        String output;

        Iterator<OrderDetail> it = rOrder.getOrderDetail().iterator();
        OrderDetail re;

        int count = 0;

        while (it.hasNext()) {
            re = it.next();
            output = String.format("%-4d|%-24s|%5d|%5.2f",
                    ++count, re.getItemName(), re.getQuantity(), re.getTotalPrice());
            initData.add(output);
        }
        if (initData.isEmpty())
            initData.add("No item");
        return initData;
    }

    /**
     * Creează o listă de elemente de meniu.
     * @param displayMenuType
     * @return
     */
    public ArrayList<String> createMenuList(int displayMenuType) {
        Iterator<MenuItem> it = cDatabase.getMenuList().iterator();
        ArrayList<String> initData = new ArrayList<>();

        while (it.hasNext()) {
            MenuItem re = it.next();
            byte menuType = re.getType();
            if (displayMenuType != 0 && displayMenuType != menuType)
                continue;
            String strMenuType = switch (menuType) {
                case MenuItem.BREAKFAST -> "Breakfast";
                case MenuItem.LUNCH -> "Lunch";
                case MenuItem.DINNER -> "Dinner";
                case MenuItem.DESSERT -> "Dessert";
                default -> "Undefined";
            };
            String output = String.format("Menu ID:%4d  Name:%-20s  Price:%5.2f Type:%s",
                    re.getID(), re.getName(), re.getPrice(), strMenuType);
            initData.add(output);
        }
        if (initData.isEmpty())
            initData.add("No order.");
        return initData;
    }

    /**
     * Creează o listă de elemente de meniu.
     * @return
     */
    public String createPaymentList() {
        double totalPayment = 0;
        int staffNum = 0;
        StringBuilder output = new StringBuilder();

        for (Staff re : cDatabase.getStaffList()) {
            if (re.getWorkState() == Staff.WORKSTATE_FINISH) {
                double pay = re.calculateWages();
                output.append(String.format("Staff ID:%4d  StaffName:%-20s  Work time:%5.2f Pay:%5.2f\n",
                        re.getID(), re.getFullName(), re.calculateWorkTime(), pay));
                staffNum++;
                totalPayment += pay;
            } else if (re.getWorkState() == Staff.WORKSTATE_ACTIVE) {
                output.append(String.format("Staff ID:%4d  StaffName:%-20s  * On work *\n",
                        re.getID(), re.getFullName()));
                staffNum++;
            }
        }
        output.append("-------------------------------------------------------\n");
        output.append(String.format("Total payment:$%.2f (%d)", totalPayment, staffNum));
        return output.toString();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public double getTotalSales() {
        return totalsales;
    }

    public int getTodaysOrderCnt() {
        return todaysordercnt;
    }

    public String getCurrentUserName() {
        return current_username;
    }
}
