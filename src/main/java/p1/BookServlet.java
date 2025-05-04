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

@WebServlet("/BookServlet")
public class BookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Retrieve parameters from the request
            String travelMode = request.getParameter("travelMode");
            String hotel = request.getParameter("hotel");
            int noOfDays = Integer.parseInt(request.getParameter("noOfDays"));
            double totalExpense = Double.parseDouble(request.getParameter("totalExpense"));
            String source = request.getParameter("source");
            String destination = request.getParameter("destination");
            String paymentMethod = request.getParameter("paymentMethod"); // Get payment method from request

            // Get userId from session
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                // Simulated for testing - user should be logged in
                request.getSession().setAttribute("userId", 1); 
                userId = 1; // Simulate user ID for testing
            }

            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/users_db", "root", "jnec@cse");
            if (con != null) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Connection failed.");
            }

            // Prepare SQL query to insert booking data into the database
            String query = "INSERT INTO bookings (travel_mode, hotel, no_of_days, total_expense, source, destination, payment_method, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(query);

            // Set the parameters for the SQL query
            ps.setString(1, travelMode);
            ps.setString(2, hotel);
            ps.setInt(3, noOfDays);
            ps.setDouble(4, totalExpense);
            ps.setString(5, source);
            ps.setString(6, destination);
            ps.setString(7, paymentMethod);
            ps.setInt(8, userId);

            // Execute the update query
            int result = ps.executeUpdate();
            
            if (result > 0) {
                // Redirect to confirmation page after successful booking
                response.sendRedirect("confirmation.jsp");
            } else {
                // If no rows were inserted, forward to error page
                request.setAttribute("errorMessage", "Booking failed! Please try again.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log error
            request.setAttribute("errorMessage", "Booking failed: " + e.getMessage()); // Set error message
            request.getRequestDispatcher("error.jsp").forward(request, response); // Forward to error page

        } finally {
            // Close resources in the finally block to ensure they are always closed
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace(); // Log exception
            }
        }
    }
}
