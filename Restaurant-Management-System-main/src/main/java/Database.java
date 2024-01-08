import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.util.Comparator;

/**
 * Clasa {@code Database} reprezintă baza de date a sistemului de gestionare a unui restaurant.
 * Această clasă oferă funcționalități pentru gestionarea datelor despre angajați, elemente de meniu și comenzi.
 * <p>
 *     Baza de date este implementată folosind MySQL.
 */
public class Database {
    /**
     * Calea către fișierul de raport al comenzilor.
     * <p>
     */
    private final static String DATA_FILES_REPORTS_REPORT = "C:\\Users\\Alesia\\Downloads\\Restaurant-Management-System-main\\Restaurant-Management-System-main\\dataFiles\\reports\\report";
    private final static String DATA_FILES_REPORTS_PAYMENT = "C:\\Users\\Alesia\\Downloads\\Restaurant-Management-System-main\\Restaurant-Management-System-main\\dataFiles\\reports\\payment";

    /**
     * Informații despre conexiunea la baza de date.
     * <p>
     *     Informațiile de conectare sunt stocate în câmpurile statice ale clasei.
     *     Acestea sunt folosite pentru a crea o conexiune la baza de date.
     *     Conexiunea este folosită pentru a executa interogări SQL asupra bazei de date.
     */
    private final static String host = "localhost";
    private final static int port = 3306;
    private final static String database = "restaurant";
    private final static String user = "root";
    private final static String password = "iarparola123";

    private final ArrayList<Staff> staffList = new ArrayList<>();
    private final ArrayList<MenuItem> menuList = new ArrayList<>();
    private final ArrayList<Order> orderList = new ArrayList<>();

    private int todaysOrderCounts;
    private final MysqlDataSource ds;

    /**
     * Construiește o nouă instanță a clasei {@code Database}.
     * @throws DatabaseException
     */
    public Database() throws DatabaseException {
        todaysOrderCounts = 0;  //Load order file?? idk

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ds = new MysqlDataSource();
        ds.setServerName(host);
        ds.setPort(port);
        ds.setDatabaseName(database);
        ds.setUser(user);
        ds.setPassword(password);

        try (Connection connection = ds.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS staff (" +
                    "id INTEGER PRIMARY KEY NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "first_name VARCHAR(20) NOT NULL," +
                    "last_name VARCHAR(20) NOT NULL," +
                    "wage FLOAT(10) NOT NULL," +
                    "is_manager BOOLEAN NOT NULL DEFAULT FALSE" + // max: 99999.99999  : 10 digits, 5 decimals
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS menu (" +
                    "id INTEGER PRIMARY KEY NOT NULL," +
                    "name VARCHAR(20) NOT NULL," +
                    "price DOUBLE(10, 5) NOT NULL," +
                    "type SMALLINT NOT NULL" +
                    ")");
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }


    /**
     * Obține lista de angajați.
     * @return
     */
    public ArrayList<Staff> getStaffList() {
        return staffList;
    }

    /**
     * Obține lista de elemente de meniu.
     * @return
     */
    public ArrayList<MenuItem> getMenuList() {
        return menuList;
    }

    /**
     * Obține lista de comenzi.
     * @return
     */
    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    /**
     * Obține numărul de comenzi înregistrate în ziua curentă.
     * @param id
     * @return
     */
    public Staff findStaffByID(int id) {
        Iterator<Staff> it = staffList.iterator();
        Staff re = null;
        boolean found = false;

        if (id < 0) {
            return null;
        }

        while (it.hasNext() && !found) {
            re = it.next();
            if (re.getID() == id) {
                found = true;
            }
        }

        if (found)
            return re;
        else
            return null;
    }


    /**
     * Obține numărul de comenzi înregistrate în ziua curentă.
     * @param staffID
     * @param newPassword
     * @param newFirstName
     * @param newLastName
     * @throws DatabaseException
     */
    public void editStaffData(int staffID, String newPassword, String newFirstName, String newLastName) throws DatabaseException {
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE staff SET password = ?, first_name = ?, last_name = ? WHERE id = ?")) {
            statement.setString(1, newPassword);
            statement.setString(2, newFirstName);
            statement.setString(3, newLastName);
            statement.setInt(4, staffID);

            if (statement.executeUpdate() == 0)
                throw new DatabaseException("No staff found");

            Staff rStaff = findStaffByID(staffID);
            rStaff.setPassword(newPassword);
            rStaff.setLastName(newLastName);
            rStaff.setFirstName(newFirstName);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Șterge un angajat din baza de date.
     * @param rStaff
     * @throws DatabaseException
     */
    public void deleteStaff(Staff rStaff) throws DatabaseException {
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM staff WHERE id = ?")) {
            statement.setInt(1, rStaff.getID());
            statement.executeUpdate();

            staffList.remove(rStaff);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }


    /**
     * Adaugă un angajat în baza de date.
     * @param newID
     * @param newPassword
     * @param newFirstName
     * @param newLastName
     * @param isManager
     * @throws DatabaseException
     */
    public void addStaff(int newID, String newPassword, String newFirstName, String newLastName, boolean isManager) throws DatabaseException {
        Staff newStaff;
        if (isManager)
            newStaff = new Manager(newID, newLastName, newFirstName, newPassword);
        else
            newStaff = new Employee(newID, newLastName, newFirstName, newPassword);
        staffList.add(newStaff);

        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO staff(id, first_name, last_name, password, wage, is_manager) VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, newStaff.getID());
            statement.setString(2, newStaff.getFirstName());
            statement.setString(3, newStaff.getLastName());
            statement.setString(4, newStaff.getPassword());
            statement.setDouble(5, newStaff.getWagerate());
            statement.setBoolean(6, newStaff instanceof Manager);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // database exception
        }
    }

    /**
     * Încarcă angajații din baza de date.
     * @throws DatabaseException
     */
    private void loadStaff() throws DatabaseException {
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM staff")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String pass = resultSet.getString("password");
                    double wageRate = resultSet.getDouble("wage");
                    boolean isManager = resultSet.getBoolean("is_manager");

                    if (isManager) {
                        Manager rManager = new Manager(id, lastName, firstName, password);
                        staffList.add(rManager);
                        rManager.setWagerate(wageRate);
                    } else {
                        Employee rEmployee = new Employee(id, lastName, firstName, pass);
                        staffList.add(rEmployee);
                        rEmployee.setWagerate(wageRate);
                    }

                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Încarcă elementele de meniu din baza de date.
     * @param id
     * @return
     */
    public MenuItem findMenuItemByID(int id) {
        Iterator<MenuItem> it = menuList.iterator();
        MenuItem re = null;
        boolean found = false;

        if (id < 0) {
            return null;
        }

        while (it.hasNext() && !found) {
            re = it.next();
            if (re.getID() == id) {
                found = true;
            }
        }

        if (found)
            return re;
        else
            return null;
    }

    /**
     * Editează un element de meniu din baza de date.
     * @param id
     * @param newName
     * @param newPrice
     * @param menuType
     * @throws DatabaseException
     */
    public void editMenuItemData(int id, String newName, double newPrice, byte menuType) throws DatabaseException {
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE menu SET name = ?, price = ?, type = ? WHERE id = ?")) {
            statement.setString(1, newName);
            statement.setDouble(2, newPrice);
            statement.setByte(3, menuType);
            statement.setInt(4, id);

            if (statement.executeUpdate() == 0)
                throw new DatabaseException("No item found");

            MenuItem rMenuItem = findMenuItemByID(id);
            rMenuItem.setName(newName);
            rMenuItem.setPrice(newPrice);
            rMenuItem.setType(menuType);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Șterge un element de meniu din baza de date.
     * @param rMenuItem
     * @throws DatabaseException
     */
    public void deleteMenuItem(MenuItem rMenuItem) throws DatabaseException {
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM menu WHERE id = ?")) {
            statement.setInt(1, rMenuItem.getID());
            statement.executeUpdate();

            menuList.remove(rMenuItem);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Adaugă un element de meniu în baza de date.
     * @param newID
     * @param newName
     * @param newPrice
     * @param newType
     * @throws DatabaseException
     */
    public void addMenuItem(int newID, String newName, double newPrice, byte newType) throws DatabaseException {
        MenuItem newMenuItem = new MenuItem(newID, newName, newPrice, newType);
        menuList.add(newMenuItem);
        menuList.sort(new MenuItemComparator());

        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO menu(id, name, price, type) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, newMenuItem.getID());
            statement.setString(2, newMenuItem.getName());
            statement.setDouble(3, newMenuItem.getPrice());
            statement.setByte(4, newMenuItem.getType());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // database exception
        }
    }

    /**
     * Încarcă elementele de meniu din baza de date.
     * @throws DatabaseException
     */
    private void loadMenuFile() throws DatabaseException {
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM menu")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    Double price = resultSet.getDouble("price");
                    Byte type = resultSet.getByte("type");

                    MenuItem rMenuItem = new MenuItem(Integer.parseInt(String.valueOf(id)), name, Double.parseDouble(String.valueOf(price)), Byte.parseByte(String.valueOf(type)));
                    menuList.add(rMenuItem);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    // Order

    /**
     * Obține numărul de comenzi înregistrate în ziua curentă.
     * @param id
     * @return
     */
    public Order findOrderByID(int id) {
        Iterator<Order> it = orderList.iterator();
        Order re = null;
        boolean found = false;

        if (id < 0) {
            return null;
        }

        while (it.hasNext() && !found) {
            re = it.next();
            if (re.getOrderID() == id) {
                found = true;
            }
        }

        if (found)
            return re;
        else
            return null;
    }

    /**
     * Adaugă o comandă în baza de date.
     * @param staffID
     * @param staffName
     * @return
     */
    public int addOrder(int staffID, String staffName) {
        int newOrderID = ++todaysOrderCounts;
        Order newOrder = new Order(staffID, staffName);
        newOrder.setOrderID(newOrderID);
        orderList.add(newOrder);
        return newOrderID;
    }

    /**
     * Adaugă un element de meniu la o comandă.
     * @param orderID
     * @param rItem
     * @param quantity
     */
    public void addOrderItem(int orderID, MenuItem rItem, byte quantity) {
        Order rOrder = findOrderByID(orderID);
        rOrder.addItem(rItem, quantity);
    }

    /**
     * Obține lista de detalii ale unei comenzi.
     * @param orderID
     * @param index
     * @return
     */
    public boolean deleteOrderItem(int orderID, int index) {
        Order rOrder = findOrderByID(orderID);
        if (rOrder == null)
            return false;
        return rOrder.deleteItem(index);
    }


    //Cancel order: order data is not deleted from the database(Just put cancel flag on)

    /**
     * Anulează o comandă.
     * @param orderID
     */
    public void cancelOrder(int orderID) {
        Order rOrder = findOrderByID(orderID);
        if (rOrder == null)
            return;
        rOrder.setState(Order.ORDER_CANCELED);
    }

    /**
     * Închide o comandă.
     * @param orderID
     */
    public void closeOrder(int orderID) {
        Order rOrder = findOrderByID(orderID);
        if (rOrder == null)
            return;
        rOrder.setState(Order.ORDER_CLOSED);
    }

    /**
     * Obține lista de detalii ale unei comenzi.
     */
    public void closeAllOrder() {
        Iterator<Order> it = orderList.iterator();
        Order re;

        while (it.hasNext()) {
            re = it.next();
            if (re.getState() == 0)//neither closed and canceled
            {
                re.setState(Order.ORDER_CLOSED);
            }
        }
    }

    /**
     * Obține lista de detalii ale unei comenzi.
     * @param orderID
     * @return
     */
    public int getOrderState(int orderID) {
        Order re = findOrderByID(orderID);
        if (re == null)
            return -1;
        return re.getState();
    }

    /**
     * Obține lista de detalii ale unei comenzi.
     * @param orderID
     * @return
     */
    public double getOrderTotalCharge(int orderID) {
        Order re = findOrderByID(orderID);
        if (re == null)
            return -1;
        return re.getTotal();
    }

    /**
     * Obține lista de detalii ale unei comenzi.
     * @return
     */
    public boolean checkIfAllOrderClosed() {
        Iterator<Order> it = orderList.iterator();
        Order re;

        while (it.hasNext()) {
            re = it.next();
            if (re.getState() == 0)//neither closed and canceled
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Obține lista de detalii ale unei comenzi.
     * @return
     */
    public boolean checkIfAllStaffCheckout() {
        Iterator<Staff> it = staffList.iterator();
        Staff re;

        while (it.hasNext()) {
            re = it.next();
            if (re.getWorkState() == Staff.WORKSTATE_ACTIVE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Obține lista de detalii ale unei comenzi
     */
    public void forthClockOutAllStaff() {
        Iterator<Staff> it = staffList.iterator();
        Staff re;

        while (it.hasNext()) {
            re = it.next();
            if (re.getWorkState() == Staff.WORKSTATE_ACTIVE) {
                re.clockOut();
            }
        }
    }

    // File load

    /**
     * Încarcă datele din fișierele de date.
     * @throws DatabaseException
     */
    public void loadData() throws DatabaseException {
        loadStaff();
        staffList.sort(new StaffComparator());
        loadMenuFile();
    }

    // File edit

    /**
     * Salvează datele în fișierele de date.
     * @param todaysDate
     * @return
     * @throws DatabaseException
     */
    public String generateOrderReport(String todaysDate) throws DatabaseException {
        Writer writer;
        String line;
        int state;
        double totalAllOrder = 0;
        String generateFileName;
        File newFile;
        int orderCnt = 0;
        int cancelCnt = 0;
        double cancelTotal = 0;

        String[] record = todaysDate.split("/");
        String today = record[0].trim() + "_" + record[1].trim() + "_" + record[2].trim();
        generateFileName = DATA_FILES_REPORTS_REPORT + today + ".txt";
        newFile = new File(generateFileName);

        try {
            writer = new BufferedWriter(new FileWriter(newFile));

            line = "*********** Order List (" + today + ") ***********\r\n";
            writer.write(line);

            for (Order re : orderList) {
                state = re.getState();
                String stateString;
                double totalOfEachOrder = re.getTotal();
                if (state == Order.ORDER_CANCELED) {
                    stateString = "Canceled";
                    cancelTotal += totalOfEachOrder;
                    cancelCnt++;
                } else {
                    stateString = "";
                    totalAllOrder += totalOfEachOrder;
                    orderCnt++;
                }
                String output = String.format("Order ID:%4d  StaffName:%-30s  Total:$%-5.2f %s\r\n",
                        re.getOrderID(), re.getStaffName(), totalOfEachOrder, stateString);
                writer.write(output);


            }
            writer.write("-------------------------------------------------------\r\n");

            writer.write("Total sales:$" + totalAllOrder + "(" + orderCnt + ")" +
                    "  Canceled:$" + cancelTotal + "(" + cancelCnt + ")\r\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            String message = e.getMessage() + Arrays.toString(e.getStackTrace());
            newFile.delete();
            throw new DatabaseException(message);
        }
        return generateFileName;
    }

    /**
     * Salvează datele în fișierele de date.
     * @param todaysDate
     * @return
     * @throws DatabaseException
     */
    public String generatePaymentReport(String todaysDate) throws DatabaseException {
        Writer writer;
        String line;
        double totalPayment = 0;
        String generateFileName;
        File newFile;
        int staffNum = 0;

        String[] record = todaysDate.split("/");
        String today = record[0].trim() + "_" + record[1].trim() + "_" + record[2].trim();
        generateFileName = DATA_FILES_REPORTS_PAYMENT + today + ".txt";
        newFile = new File(generateFileName);

        try {
            writer = new BufferedWriter(new FileWriter(newFile));

            line = "*********** Payment List (" + today + ") ***********\r\n";
            writer.write(line);

            for (Staff re : staffList) {
                if (re.getWorkState() == Staff.WORKSTATE_FINISH) {
                    double pay = re.calculateWages();
                    String output = String.format("Order ID:%4d  StaffName:%-30s  Work time:%-5.2f Pay:%-5.2f\r\n",
                            re.getID(), re.getFullName(), re.calculateWorkTime(), pay);
                    writer.write(output);
                    staffNum++;
                    totalPayment += pay;
                }
            }
            writer.write("-------------------------------------------------------\r\n");

            writer.write("Total payment:$" + totalPayment + "(" + staffNum + ")\r\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            String message = e.getMessage() + Arrays.toString(e.getStackTrace());
            newFile.delete();
            throw new DatabaseException(message);
        }
        return generateFileName;
    }

    // Comparator
    /**
     * Comparator pentru angajați.
     */
    private static class StaffComparator implements Comparator<Staff> {

        @Override
        public int compare(Staff s1, Staff s2) {
            return s1.getID() < s2.getID() ? -1 : 1;
        }
    }

    /**
     * Comparator pentru elemente de meniu.
     */
    private static class MenuItemComparator implements Comparator<MenuItem> {

        @Override
        public int compare(MenuItem m1, MenuItem m2) {
            return m1.getID() < m2.getID() ? -1 : 1;
        }
    }
}
