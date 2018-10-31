//Adelayde Rome (7675884868)
//accesses friend info from database for their separate pages

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/friendInfo")
public class friendInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public friendInfo() {
        super();

    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PrintWriter pw = response.getWriter();
		
		String goal = request.getParameter("lookingfor");
		String email = request.getParameter("friendEmail");
		String currUser = request.getParameter("userEmail");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CalendarUsers?user=root&password=root&useSSL=false");
			
			st = conn.createStatement();
			//email will be sent every time
			rs = st.executeQuery("SELECT * FROM Users WHERE email='" + email + "';");
			rs.next();
			
			if(goal.equals("name")) {
				pw.println(rs.getString("username"));
			}
			else if(goal.equals("img")) {
				pw.println(rs.getString("imgURL"));
			}
			
			else if(goal.equals("followStatus")) {
				Statement st1 = null;
				ResultSet rs1 = null;
				st1 = conn.createStatement();
				rs1 = st1.executeQuery("SELECT * FROM UserFollowing WHERE follower='" 
						+ currUser + "' AND followingUser='" + email + "';");
				//if they are following the user
				if(rs1.next()) {
					pw.println("Unfollow");
				}
				else {
					pw.println("Follow");
				}
				
				if(rs1 != null) rs1.close();
				if(st1 != null) st1.close();
			}
			else if(goal.equals("fEvents")) {
				Statement st1 = null;
				ResultSet rs1 = null;
				st1 = conn.createStatement();
				response.setContentType("text/html");
				rs1 = st1.executeQuery("SELECT * FROM EventList WHERE useremail='" 
						+ email + "';");
				
				String eventHTML = "";
				while(rs1.next()) {
					//send html to front end for the events table 
					//also pass event data for adding to google calendar + database
					eventHTML += "<tr onclick=\"followEvent('" + rs1.getString("eventID") + "', '" + rs1.getString("edate") + "', '" + rs1.getString("etime") + "', '" 
							+ rs1.getString("ename") + "', '" + rs1.getString("sDateTime") + "', '" 
							+ rs1.getString("eDateTime") + "')\"><td>" + rs1.getString("edate") + "</td><td>" 
							+ rs1.getString("etime") + "</td><td>" + rs1.getString("ename") + "</td></tr>";
				}
				pw.println(eventHTML);
				if(rs1 != null) rs1.close();
				if(st1 != null) st1.close();
			}
			
			else if(goal.equals("deleteFollow")) {
				Statement st1 = null;
				st1 = conn.createStatement();
				st1.executeUpdate("DELETE FROM UserFollowing WHERE follower='"
						+ currUser + "' AND followingUser='" + email + "';");
				if(st1 != null) st1.close();
			}
			else if(goal.equals("addFollow")) {
				Statement st1 = null;
				st1 = conn.createStatement();
				st1.executeUpdate("INSERT INTO UserFollowing(follower, followingUser) "
						+ "VALUES('" + currUser + "', '" + email + "');");
				if(st1 != null) st1.close();
			}
			else if(goal.equals("followEvent")) {
				Statement st1 = null;
				st1 = conn.createStatement();
				String eventDate = request.getParameter("edate");
				String eventTime = request.getParameter("etime");
				String eventName = request.getParameter("ename");
				String startDT = request.getParameter("startDateTime");
				String endDT = request.getParameter("endDateTime");
				String eventID = request.getParameter("eid");
				
				st1.executeUpdate("INSERT INTO EventList(eventID, userEmail, ename, etime, edate, sDateTime, eDateTime)"
						+ "VALUES('" + eventID + "', '" + currUser + "', '" + eventName + "', '" + eventTime
						+ "', '" + eventDate + "', '" + startDT + "', '" + endDT + "');" );
				if(st1 != null) st1.close();
			}
			
		} catch(SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch(ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(st != null) {
					st.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch(SQLException sqle) {
				System.out.println("sqle closing conn: " + sqle.getMessage());
			}
		}
    	
    }
    
}