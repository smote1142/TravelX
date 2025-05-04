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

@WebServlet("/BookPackageServlet")
public class BookPackageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String travelMode = request.getParameter("travelMode");
        String hotel = request.getParameter("hotel");
        int noOfDays = Integer.parseInt(request.getParameter("noOfDays"));
        double totalExpense = Double.parseDouble(request.getParameter("totalExpense"));
        String source = request.getParameter("source");
        String destination = request.getParameter("destination");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users_db", "root", "jnec@cse");

            String query = "INSERT INTO bookings (travelMode, hotel, noOfDays, totalExpense, source, destination, userId) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            int userId = (int) request.getSession().getAttribute("userId");

            ps.setString(1, travelMode);
            ps.setString(2, hotel);
            ps.setInt(3, noOfDays);
            ps.setDouble(4, totalExpense);
            ps.setString(5, source);
            ps.setString(6, destination);
            ps.setInt(7, userId);

            ps.executeUpdate();
            response.sendRedirect("confirmation.jsp");
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
