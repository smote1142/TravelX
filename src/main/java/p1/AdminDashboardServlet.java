package p1;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    String DB_URL = "jdbc:mysql://127.0.0.1:3306/users_db";
    String USER = "root";
    String PASSWORD = "jnec@cse"; // Replace with your actual password

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set content type for the response
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
            ResultSet rs = stmt.executeQuery();

            // HTML structure to display user data
            out.println("<html lang='en'><head>");
            out.println("<meta charset='UTF-8'><title>Admin Dashboard</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("</head><body class='bg-dark text-white p-5'>");
            out.println("<div class='container'>");
            out.println("<h2 class='mb-4'>Admin Dashboard - All Users</h2>");
            out.println("<table class='table table-bordered table-striped bg-white text-dark'>");
            out.println("<thead><tr><th>ID</th><th>Username</th><th>Email</th><th>Role</th></tr></thead>");
            out.println("<tbody>");

            // Loop through the result set to display user data
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("username") + "</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("<td>" + rs.getString("role") + "</td>");
                out.println("</tr>");
            }

            out.println("</tbody></table>");
            out.println("</div></body></html>");

            // Close the resources
            conn.close();
            rs.close();
            stmt.close();
        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
    }
}
