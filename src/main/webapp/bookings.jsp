<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="javax.servlet.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Your Bookings</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background: linear-gradient(270deg, #030d15, #585470, #21214e);
            background-size: 300% 300%;
            animation: gradientAnimation 25s ease infinite;
            margin: 0;
            padding: 0;
            color: #fff;
        }

        @keyframes gradientAnimation {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }

        .navbar {
            display: flex;
            justify-content: space-around;
            background-color: rgba(76, 175, 80, 0.9);
            padding: 15px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);
        }

        .navbar a {
            color: white;
            text-decoration: none;
            padding: 14px 20px;
            text-align: center;
            border-radius: 5px;
            transition: background-color 0.3s;
        }

        .navbar a:hover {
            background-color: rgba(255, 255, 255, 0.2);
        }

        .container {
            max-width: 1000px;
            margin: 30px auto;
            background: rgba(255, 255, 255, 0.95);
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
            color: #000;
        }

        h1 {
            text-align: center;
            margin-bottom: 20px;
            color: #4CAF50;
            font-size: 2em;
        }

        h2 {
            color: #333;
            margin-top: 30px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        th, td {
            padding: 12px;
            border: 1px solid #ccc;
            text-align: left;
        }

        th {
            background-color: #4CAF50;
            color: white;
        }

        tr:hover {
            background-color: #f2f2f2;
        }

        .error {
            color: red;
        }

        .success {
            color: green;
        }
    </style>
</head>
<body>

    <!-- Navigation -->
    <div class="navbar">
        <a href="index.html">Home</a>
        <a href="travelling_mode.html">Rideshare</a>
        <a href="Flight.html">Flights</a>
        <a href="Destination.html">Destinations</a>
        <a href="hotel.html">Hotels</a>
        <a href="contact.html">Contact</a>
    </div>

    <div class="container">
        <h1>Your Bookings</h1>

        <%
            // Get session and user ID
            HttpSession currentSession = request.getSession(false);
            Integer userId = (currentSession != null) ? (Integer) currentSession.getAttribute("userId") : null;

            if (userId == null) {
                response.sendRedirect("login.html");
                return;
            }

            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users_db", "root", "jnec@cse");

                // === FLIGHT BOOKINGS ===
                ps = con.prepareStatement("SELECT * FROM flight_bookings WHERE user_id = ?");
                ps.setInt(1, userId);
                rs = ps.executeQuery();

                if (!rs.isBeforeFirst()) {
                    out.println("<p>No flight bookings found.</p>");
                } else {
        %>
        <h2>Flight Bookings</h2>
        <table>
            <tr>
                <th>Flight Name</th>
                <th>Departure</th>
                <th>Arrival</th>
                <th>Price</th>
                <th>Travel Date</th>
                <th>Payment Method</th>
            </tr>
            <%
                while (rs.next()) {
            %>
            <tr>
                <td><%= rs.getString("flight_name") %></td>
                <td><%= rs.getString("departure_time") %></td>
                <td><%= rs.getString("arrival_time") %></td>
                <td>₹<%= rs.getDouble("price") %></td>
                <td><%= rs.getString("travel_date") %></td>
                <td><%= rs.getString("payment_method") %></td>
            </tr>
            <%
                }
            %>
        </table>
        <%
                }

                rs.close();
                ps.close();

                // === OTHER BOOKINGS ===
                ps = con.prepareStatement("SELECT * FROM bookings WHERE user_id = ?");
                ps.setInt(1, userId);
                rs = ps.executeQuery();

                if (!rs.isBeforeFirst()) {
                    out.println("<p>No other bookings found.</p>");
                } else {
        %>
        <h2>Other Bookings</h2>
        <table>
            <tr>
                <th>Source</th>
                <th>Destination</th>
                <th>No. of Days</th>
                <th>Travel Mode</th>
                <th>Hotel</th>
                <th>Total Expense</th>
                <th>Payment Method</th>
            </tr>
            <%
                while (rs.next()) {
            %>
            <tr>
                <td><%= rs.getString("source") %></td>
                <td><%= rs.getString("destination") %></td>
               <td><%= rs.getInt("no_of_days") %></td>
<td><%= rs.getString("travel_mode") %></td>
<td><%= rs.getString("hotel") %></td>
<td>₹<%= rs.getDouble("total_expense") %></td>
<td><%= rs.getString("payment_method") %></td>

            </tr>
            <%
                }
            %>
        </table>
        <%
                }

            } catch (Exception e) {
                out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
            } finally {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            }
        %>
    </div>
</body>
</html>
