import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

// ---------- DATABASE CONFIG ----------
class DBConfig {
    static final String URL =
        "jdbc:mysql://localhost:3306/moviebooking?useSSL=false&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = ""; // 🔴 Put your MySQL password
}

// ---------- MODEL ----------
class Booking {
    private String name;
    private String phone;
    private String movie;
    private String showTime;
    private String seat;
    private int tickets;
    private int total;

    public Booking(String name, String phone, String movie,
                   String showTime, String seat, int tickets, int total) {
        this.name = name;
        this.phone = phone;
        this.movie = movie;
        this.showTime = showTime;
        this.seat = seat;
        this.tickets = tickets;
        this.total = total;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getMovie() { return movie; }
    public String getShowTime() { return showTime; }
    public String getSeat() { return seat; }
    public int getTickets() { return tickets; }
    public int getTotal() { return total; }
}

// ---------- DAO ----------
class BookingDAO {

    private Connection getConnection() throws SQLException {

    try {
        // Explicitly load MySQL Driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Driver Loaded Successfully");
    } 
    catch (ClassNotFoundException e) {
        JOptionPane.showMessageDialog(null,
                "MySQL JDBC Driver not found!\nCheck if JAR is added.");
        throw new SQLException("Driver not found");
    }

    try {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/moviebooking" +
                "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root",
                ""   // ← Put your MySQL password here
        );

        System.out.println("Database Connected Successfully!");
        return con;

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
                "Database Connection Failed!\n" + e.getMessage());
        throw e;
    }
}


    public void addBooking(Booking booking) throws SQLException {

        String sql = "INSERT INTO bookings " +
                "(customer_name, phone, movie, show_time, seat, tickets, total_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, booking.getName());
            ps.setString(2, booking.getPhone());
            ps.setString(3, booking.getMovie());
            ps.setString(4, booking.getShowTime());
            ps.setString(5, booking.getSeat());
            ps.setInt(6, booking.getTickets());
            ps.setInt(7, booking.getTotal());

            ps.executeUpdate();
        }
    }
}

// ---------- MAIN UI ----------
public class MovieBookingSystem extends JFrame {

    private JComboBox<String> movieBox, timeBox, seatBox;
    private JTextField nameField, phoneField, ticketsField;
    private JLabel totalLabel;

    private final int TICKET_PRICE = 150;
    private final BookingDAO dao = new BookingDAO();

    public MovieBookingSystem() {

        setTitle("🎬 Movie Ticket Booking System");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(15, 32, 39));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel title = new JLabel("MOVIE TICKET BOOKING");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        // Name
        gbc.gridy++;
        panel.add(createLabel("Customer Name"), gbc);
        gbc.gridx = 1;
        nameField = createTextField(font);
        panel.add(nameField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Phone Number"), gbc);
        gbc.gridx = 1;
        phoneField = createTextField(font);
        panel.add(phoneField, gbc);

        // Movie
        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Select Movie"), gbc);
        gbc.gridx = 1;
        movieBox = new JComboBox<>(new String[]{"Avengers", "Inception", "Interstellar"});
        panel.add(movieBox, gbc);

        // Time
        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Show Time"), gbc);
        gbc.gridx = 1;
        timeBox = new JComboBox<>(new String[]{"10:00 AM", "2:00 PM", "6:00 PM"});
        panel.add(timeBox, gbc);

        // Seat
        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Seat"), gbc);
        gbc.gridx = 1;
        seatBox = new JComboBox<>(new String[]{"A1", "A2", "B1", "B2", "C1", "C2"});
        panel.add(seatBox, gbc);

        // Tickets
        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Number of Tickets"), gbc);
        gbc.gridx = 1;
        ticketsField = createTextField(font);
        panel.add(ticketsField, gbc);

        // Total
        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Total Price"), gbc);
        gbc.gridx = 1;
        totalLabel = new JLabel("₹0");
        totalLabel.setForeground(Color.GREEN);
        panel.add(totalLabel, gbc);

        // Live Price Calculation
        ticketsField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                    int tickets = Integer.parseInt(ticketsField.getText());
                    totalLabel.setText("₹" + (tickets * TICKET_PRICE));
                } catch (Exception ex) {
                    totalLabel.setText("₹0");
                }
            }
        });

        // Book Button
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JButton bookBtn = new JButton("Book Ticket");
        bookBtn.setBackground(new Color(0, 123, 255));
        bookBtn.setForeground(Color.WHITE);
        panel.add(bookBtn, gbc);

        bookBtn.addActionListener(e -> bookTicket());

        add(panel);
    }

    private void bookTicket() {

        String name = nameField.getText();
        String phone = phoneField.getText();
        String movie = movieBox.getSelectedItem().toString();
        String time = timeBox.getSelectedItem().toString();
        String seat = seatBox.getSelectedItem().toString();
        String ticketText = ticketsField.getText();

        if (name.isEmpty() || phone.isEmpty() || ticketText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            int tickets = Integer.parseInt(ticketText);
            int total = tickets * TICKET_PRICE;

            Booking booking = new Booking(
                name, phone, movie, time, seat, tickets, total
            );

            dao.addBooking(booking);

            JOptionPane.showMessageDialog(this,
                "🎉 Booking Successful!\n\n" +
                "Movie: " + movie +
                "\nSeat: " + seat +
                "\nTime: " + time +
                "\nTotal: ₹" + total
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid ticket number!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Database Error:\n" + ex.getMessage());
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField field = new JTextField(15);
        field.setFont(font);
        return field;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new MovieBookingSystem().setVisible(true)
        );
    }
}

