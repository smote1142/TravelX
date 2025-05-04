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

@WebServlet("/ContactusServlet")
public class ContactusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/users_db";
    private static final String USER = "root";
    private static final String PASSWORD = "jnec@cse";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // Insert the contact details into the database
            String sql = "INSERT INTO contacts (name, email, message) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, message);
            int rows = stmt.executeUpdate();

            // CSS for the response HTML
            String css = "<style>"
                    + "body { font-family: Arial, sans-serif; background: linear-gradient(270deg, #030d15, #585470, #21214e); "
                    + "background-size: 300% 300%; animation: gradientAnimation 25s ease infinite; margin: 0; padding: 0; color: #333; }"
                    + "@keyframes gradientAnimation { 0% { background-position: 0% 50%; } 50% { background-position: 100% 50%; } 100% { background-position: 0% 50%; } }"
                    + ".container { max-width: 600px; margin: 100px auto; background: rgba(255, 255, 255, 0.9); padding: 20px; "
                    + "border-radius: 10px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); text-align: center; }"
                    + "h1 { color: #4CAF50; text-align: center; margin-bottom: 20px; }"
                    + "a { display: inline-block; margin-top: 20px; padding: 10px 20px; background-color: #4CAF50; color: white; "
                    + "text-decoration: none; border-radius: 5px; transition: background-color 0.3s; }"
                    + "a:hover { background-color: #45a049; }"
                    + "button { margin-top: 20px; padding: 10px 20px; background-color: #4CAF50; color: white; text-align:center "
                    + "border: none; border-radius: 5px; cursor: pointer; transition: background-color 0.3s; }"
                    + "button:hover { background-color: #45a049; }"
                    + "</style>";


            // HTML content based on the result
            if (rows > 0) {
                out.println("<html><head><title>Message Sent</title>" + css + "</head><body>");
                out.println("<div class='container'><h1>Message Sent Successfully!</h1>");
                out.println("<a href='contact.html'>Go Back</a></div>");
            } else {
                out.println("<html><head><title>Message Failed</title>" + css + "</head><body>");
                out.println("<div class='container'><h1>Failed to send message.</h1>");
                out.println("<a href='contact.html'>Try Again</a></div>");
            }
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<html><body><h1>Error: " + e.getMessage() + "</h1></body></html>");
        } finally {
            // Cleanup resources
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
