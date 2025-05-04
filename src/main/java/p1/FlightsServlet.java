package p1;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/FlightsServlet")
public class FlightsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Handle GET request to show the flight booking form with basic details
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // Redirect to login if user is not logged in
        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Display flight details form with styling
        response.setContentType("text/html");
        response.getWriter().println("<html>");
        response.getWriter().println("<head>");
        response.getWriter().println("<link rel='stylesheet' type='text/css' href='style.css'>"); // Update the path if necessary
        response.getWriter().println("<title>Book Your Flight</title>");
        response.getWriter().println("</head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<div class='booking-panel'>");
        response.getWriter().println("<h1>Book Your Flight</h1>");
        response.getWriter().println("<form method='POST' action='FlightsServlet'>");
        response.getWriter().println("<label for='flight_name'>Flight Name:</label>");
        response.getWriter().println("<input type='text' name='flight_name' required><br>");
        response.getWriter().println("<label for='departure_time'>Departure Time:</label>");
        response.getWriter().println("<input type='datetime-local' name='departure_time' required><br>");
        response.getWriter().println("<label for='arrival_time'>Arrival Time:</label>");
        response.getWriter().println("<input type='datetime-local' name='arrival_time' required><br>");
        response.getWriter().println("<label for='price'>Price:</label>");
        response.getWriter().println("<input type='number' name='price' required><br>");
        response.getWriter().println("<input type='submit' value='Book Now'>");
        response.getWriter().println("</form>");
        response.getWriter().println("</div>");
        response.getWriter().println("</body></html>");
    }

    // Handle POST request to process flight booking and move to details
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // Redirect to login if user is not logged in
        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Retrieve basic flight booking details
        String flightName = request.getParameter("flight_name");
        String departureTime = request.getParameter("departure_time");
        String arrivalTime = request.getParameter("arrival_time");
        String priceStr = request.getParameter("price");

        // Validate input data
        if (flightName == null || flightName.isEmpty() ||
            departureTime == null || departureTime.isEmpty() ||
            arrivalTime == null || arrivalTime.isEmpty() ||
            priceStr == null || priceStr.isEmpty()) {
            response.sendRedirect("booking_error.html");
            return;
        }

        // Parse price
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("booking_error.html");
            return;
        }

        // Store the flight details in session or pass them forward
        session.setAttribute("flightName", flightName);
        session.setAttribute("departureTime", departureTime);
        session.setAttribute("arrivalTime", arrivalTime);
        session.setAttribute("price", price);

        // Redirect to the next step: ask for payment method and travel date
        response.sendRedirect("payment_and_details.html"); // This should be a new form page
    }
}
