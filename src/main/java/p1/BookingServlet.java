package p1;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/users_db"; // Update with your DB name
    private static final String DB_USER = "root"; // Your MySQL username
    private static final String DB_PASSWORD = "jnec@cse"; // Your MySQL password

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get form data
        String tripType = request.getParameter("trip_type");
        String fromLocation = request.getParameter("from");
        String toLocation = request.getParameter("to");
        String departureDate = request.getParameter("departure");
        String returnDate = request.getParameter("return");
        String travellersClass = request.getParameter("travellers_class");
        String fareType = request.getParameter("fare_type");

        // Set response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Database connection and insertion
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Prepare SQL statement
            String sql = "INSERT INTO rideshare (trip_type, from_location, to_location, departure_date, return_date, travellers_class, fare_type) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, tripType);
            preparedStatement.setString(2, fromLocation);
            preparedStatement.setString(3, toLocation);
            preparedStatement.setString(4, departureDate);
            preparedStatement.setString(5, returnDate);
            preparedStatement.setString(6, travellersClass);
            preparedStatement.setString(7, fareType);
            
            // Execute update
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                out.println("<h2>Booking Confirmation</h2>");
                out.println("<p>Your booking was successful!</p>");
            } else {
                out.println("<h2>Error</h2>");
                out.println("<p>Booking failed. Please try again.</p>");
            }
            
            // Close the resources
            preparedStatement.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h2>Error</h2>");
            out.println("<p>There was an error processing your request: " + e.getMessage() + "</p>");
        } finally {
            out.close();
        }
    }
}
