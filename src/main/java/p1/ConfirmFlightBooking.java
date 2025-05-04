package p1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ConfirmFlightBooking")
public class ConfirmFlightBooking extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/users_db";
    private static final String USER = "root";
    private static final String PASSWORD = "jnec@cse";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // Check if user is logged in
        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Retrieve flight and payment details from session and request
        String flightName = (String) session.getAttribute("flightName");
        String departureTime = (String) session.getAttribute("departureTime");
        String arrivalTime = (String) session.getAttribute("arrivalTime");
        Double price = (Double) session.getAttribute("price");

        // Retrieve travel date and payment method from the form
        String travelDate = request.getParameter("travel_date");
        String paymentMethod = request.getParameter("payment_method");

        // Check if any required field is missing
        if (flightName == null || departureTime == null || arrivalTime == null || price == null || 
            travelDate == null || travelDate.isEmpty() || paymentMethod == null || paymentMethod.isEmpty()) {
            response.sendRedirect("booking_error.html");
            return;
        }

        // Database connection and booking confirmation
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "INSERT INTO flight_bookings (user_id, flight_name, departure_time, arrival_time, price, travel_date, payment_method) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, flightName);
            stmt.setString(3, departureTime);
            stmt.setString(4, arrivalTime);
            stmt.setDouble(5, price);
            stmt.setString(6, travelDate);
            stmt.setString(7, paymentMethod);

            // Execute the booking
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                response.sendRedirect("booking_success.html");
            } else {
                response.sendRedirect("booking_error.html");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("booking_error.html");
        }
    }
}
