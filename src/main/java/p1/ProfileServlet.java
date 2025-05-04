package p1;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Get the current session
        HttpSession session = request.getSession(false); 
        if (session == null) {
            out.println("{ \"error\": \"No session found\" }");
            return; // Stop execution if session is not found
        }

        Integer userId = (Integer) session.getAttribute("userId"); // Get the userId from session
        if (userId == null) {
            out.println("{ \"error\": \"No userId found in session\" }");
            return; // Stop execution if no userId is found in session
        }

        // Use try-with-resources to manage database resources
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            String dbUrl = "jdbc:mysql://localhost:3306/users_db";
            String dbUser = "root";
            String dbPassword = "jnec@cse";

            // Fetch user details based on userId
            String query = "SELECT username FROM users WHERE id=?";
            try (Connection con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                 PreparedStatement ps = con.prepareStatement(query)) {
                 
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String userUsername = rs.getString("username");

                        // Send username as JSON
                        out.println("{ \"username\": \"" + userUsername + "\" }");
                    } else {
                        // If no user found, send an error response
                        out.println("{ \"error\": \"User not found\" }");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("{ \"error\": \"An error occurred while fetching data\" }");
        }
    }
}
