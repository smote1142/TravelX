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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    String DB_URL = "jdbc:mysql://127.0.0.1:3306/users_db";
    String USER = "root";
    String PASSWORD = "jnec@cse"; // Replace with your actual password

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response); // Forward GET requests to doPost method
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Response setup
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // Admin credentials validation
            if ("admin".equals(username) && "admin123".equals(password)) {
                // If the username and password are correct for admin, redirect to admin dashboard
                response.sendRedirect("admin.html"); // Modify this to your admin dashboard URL
            } else {
                // Regular user login validation
                String sql = "SELECT id, username FROM users WHERE username = ? AND password = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    // Successful login for regular user, create session and store the userId
                    HttpSession session = request.getSession(); // Create a session
                    int userId = rs.getInt("id"); // Fetch userId
                    session.setAttribute("userId", userId); // Store userId in session

                    // Redirect to user profile page (or any other page)
                    out.println("<html><head>");
                    out.println("<script type='text/javascript'>");
                    out.println("alert('Login Successful!');");
                    out.println("window.location.href='index.html';"); // Redirect to user profile page
                    out.println("</script>");
                    out.println("</head><body></body></html>");
                } else {
                    // Invalid credentials message
                    out.println("<html><body><h1>Invalid Username or Password</h1>");
                    out.println("<a href='login.html'>Try Again</a></body></html>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<html><body><h1>Error: " + e.getMessage() + "</h1></body></html>");
        } finally {
            // Cleanup resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
