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

@WebServlet("/SubmitBookingServlet")
public class SubmitBookingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("booking_form.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String destination = request.getParameter("destination");
        String travelDate = request.getParameter("travel_date");
        int numberOfDays = Integer.parseInt(request.getParameter("number_of_days"));
        String transportMode = request.getParameter("transport_mode");
        double budget = Double.parseDouble(request.getParameter("budget"));
        String hotelPreference = request.getParameter("hotel_preference");

        String jdbcURL = "jdbc:mysql://localhost:3306/users_db";
        String dbUser = "root";
        String dbPassword = "jnec@cse";

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "INSERT INTO trip_bookings (user_id, destination, travel_date, number_of_days, transport_mode, budget, hotel_preference) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setString(2, destination);
            statement.setString(3, travelDate);
            statement.setInt(4, numberOfDays);
            statement.setString(5, transportMode);
            statement.setDouble(6, budget);
            statement.setString(7, hotelPreference);

            int rowsInserted = statement.executeUpdate();
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
