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

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/users_db"; // Your database name
    private static final String USER = "root"; // Your MySQL username
    private static final String PASSWORD = "jnec@cse"; // Your MySQL password

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("passs"); // Ensure this matches the input name
        String confirmPassword = request.getParameter("confirm_pass"); // Ensure this matches the input name

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        // Basic validation for empty fields
        if (username == null || email == null || password == null || confirmPassword == null ||
            username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            out.println("<html><body><h1>All fields are required!</h1>");
            out.println("<a href='signup.html'>Try Again</a></body></html>");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            out.println("<html><body><h1>Passwords do not match!</h1>");
            out.println("<a href='signup.html'>Try Again</a></body></html>");
            return;
        }

        // Proceed with database insertion
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password); // Use hashed password in a real application

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                // Signup successful, display success page
                out.println("<html>");
                out.println("<head>");
                out.println("<style>");
                out.println("body {"
                    + "font-family: Arial, sans-serif;"
                    + "background-color: #f0f8ff;"
                    + "display: flex;"
                    + "justify-content: center;"
                    + "align-items: center;"
                    + "height: 100vh;"
                    + "margin: 0;"
                    + "}");

                out.println(".container {"
                    + "text-align: center;"
                    + "background-color: white;"
                    + "padding: 30px;"
                    + "border-radius: 10px;"
                    + "box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);"
                    + "}");

                out.println("h1 {"
                    + "color: #28a745;"
                    + "}");

                out.println("a {"
                    + "display: inline-block;"
                    + "padding: 10px 20px;"
                    + "background-color: #007bff;"
                    + "color: white;"
                    + "text-decoration: none;"
                    + "border-radius: 5px;"
                    + "margin-top: 20px;"
                    + "}");

                out.println("a:hover {"
                    + "background-color: #0056b3;"
                    + "}");
                out.println("</style>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='container'>");
                out.println("<h1>Signup Successful!</h1>");
                out.println("<p>Thank you for registering. You can now log in to your account.</p>");
                out.println("<a href='login.html'>Login Now</a>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            } else {
                out.println("<html><body><h1>Signup Failed!</h1>");
                out.println("<a href='signup.html'>Try Again</a></body></html>");
            }
        } catch (Exception e) {
            e.printStackTrace(out); // Print stack trace to the output
            out.println("<html><body><h1>Error: " + e.getMessage() + "</h1></body></html>");
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
