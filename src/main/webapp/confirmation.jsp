<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="javax.servlet.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmation</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"> <!-- Font Awesome for icons -->
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(270deg, #030d15, #585470, #21214e);
            background-size: 300% 300%;
            animation: gradientAnimation 25s ease infinite;
            margin: 0;
            padding: 0;
            color: #fff;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            overflow: hidden;
        }

        @keyframes gradientAnimation {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }

        .navbar {
            background-color: rgba(0, 0, 0, 0.5);
            position: absolute;
            top: 0;
            width: 100%;
            display: flex;
            justify-content: center;
            padding: 10px 0;
            backdrop-filter: blur(5px);
        }

        .navbar a {
            color: #f2f2f2;
            text-align: center;
            padding: 14px 16px;
            text-decoration: none;
            margin: 0 15px;
            transition: background-color 0.3s;
        }

        .navbar a:hover {
            background-color: rgba(255, 255, 255, 0.2);
            color: #fff;
        }

        .container {
            max-width: 600px;
            padding: 20px;
            border-radius: 15px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
            backdrop-filter: blur(10px);
            background: rgba(255, 255, 255, 0.2);
            text-align: center;
        }

        h1 {
            margin-bottom: 20px;
            font-size: 2.5rem;
            color: #4CAF50; /* Green color for the heading */
        }

        h3 {
            margin-top: 20px;
            color: #ffffff; /* White color for subheadings */
        }

        p {
            margin-bottom: 20px;
            font-size: 1.2rem;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <div class="navbar">
        <a href="index.html">Home</a>
          <a href="hotel.html">Hotel</a>
            <a href="destination.html">Destination</a>
              <a href="travelling_mode.html">Rideshare</a>
    </div>

    <div class="container">
        <h1><span style="color: #4CAF50;">&#10004;</span> Booking Successful</h1>

        <!-- Display dynamic order details from the database -->
        <%
            // Get the userId from the session
            Integer userId = (Integer) session.getAttribute("userId");

            // If userId is null, redirect to login page
            if (userId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Retrieve order details from the database for the logged-in user
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/users_db", "root", "jnec@cse");
                
                // Fetch the latest booking for the current logged-in user
                ps = con.prepareStatement("SELECT * FROM bookings WHERE userId = ? ORDER BY id DESC LIMIT 1");
                ps.setInt(1, userId);
                rs = ps.executeQuery();

                if (rs.next()) {
                    String travelMode = rs.getString("travelMode");
                    String hotel = rs.getString("hotel");
                    int noOfDays = rs.getInt("noOfDays");
                    double totalExpense = rs.getDouble("totalExpense");
                    String source = rs.getString("source");
                    String destination = rs.getString("destination");
                    Date bookingDate = rs.getDate("bookingDate"); // Fetch the booking date

                    // Display booking details
        %>
                    <h3>Booking Details</h3>
                    <p><strong>Source:</strong> <%= source %></p>
                    <p><strong>Destination:</strong> <%= destination %></p>
                    <p><strong>Number of Days:</strong> <%= noOfDays %></p>
                    <p><strong>Travel Mode:</strong> <%= travelMode %></p>
                    <p><strong>Hotel:</strong> <%= hotel %></p>
                    <p><strong>Total Expense:</strong> ₹<%= totalExpense %></p>
                    <p><strong>Booking Date:</strong> <%= bookingDate != null ? bookingDate.toString() : "Not available" %></p> <!-- Display booking date -->
        <%
                } else {
                    out.println("<p>No booking found for the current user.</p>");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            }
        %>
    </div>

    <!-- Sound effect -->
    <audio id="successSound">
        <source src="resources/audio/orderconfirmed.mp3" type="audio/mpeg">
        Your browser does not support the audio element.
    </audio>

    <script>
        // Play success sound
        var successSound = document.getElementById("successSound");
        successSound.play();
    </script>
</body>
</html>
