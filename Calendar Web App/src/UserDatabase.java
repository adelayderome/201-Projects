//Adelayde Rome (7675884868)
//takes info from profile page and adds it into database

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UserDatabase")
public class UserDatabase extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public UserDatabase() {
        super();
        
    }



	@SuppressWarnings("resource")
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("username");
		String imgURL = request.getParameter("imageURL");
		String email = request.getParameter("useremail");
		String eventName = request.getParameter("eventName");
		String eventDate = request.getParameter("eventDate");
		String eventTime = request.getParameter("eventTime");
		String startDateTime = request.getParameter("eventStart");
		String endDateTime = request.getParameter("eventEnd");
		String eventID = request.getParameter("eid");
		String addEvent = request.getParameter("addevent");
		String email2 = request.getParameter("Email");
		
		
		PrintWriter pw = response.getWriter();
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		//PreparedStatement ps = null;
		
		//inserting user and user events into database
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CalendarUsers?user=root&password=root&useSSL=false");
			
			st = conn.createStatement();
			//check if user has already been inserted, if not insert 
			//is this okay or causing problems? 
			if(email != null) {
				
				int update = st.executeUpdate(
						"INSERT IGNORE INTO Users(email, username, imgURL)" + "VALUES ('"+ email + "', '"
						+ name + "', '" + imgURL + "'); " );
				
				//delete this 
				if(update == 1) pw.println("added to database:" + update);

				else {

					String userlist = "did not add to database ";
					pw.println(userlist);
				}
			}
			
			if(addEvent != null) {
				rs = st.executeQuery("SELECT * FROM Users WHERE email='" + email2 + "';");
				Statement st1 = null;
				st1 = conn.createStatement();
				
				Statement st2 = null;
				ResultSet rs1 = null;
				st2 = conn.createStatement();
				//where eventid is equal and user is equal..each user can only have event id once
				rs1 = st2.executeQuery("SELECT * FROM EventList WHERE userEmail='" + email2 + "' AND "
						+ "eventID='"+ eventID + "';");
				if(!rs1.next()){
					while(rs.next()) {
						//only runs once 
						st1.executeUpdate("INSERT INTO EventList(userEmail, eventID, ename, etime, edate, sDateTime, eDateTime)"
								+ " VALUES ('"  + email2 + "', '" + eventID + "', '" +eventName + "', '"
								+ eventTime + "', '" + eventDate + "', '" + startDateTime + "', '"+ endDateTime + "'); ");
					}
				}
				if(st1 != null) st1.close();
				if(st2 != null) st2.close();
			}
			
		} catch(SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch(ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
		 	//do we need to close database connection here or later??
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
		//send data back to jsp for search reasons to every page?
		//only if looking for this info..?
		
	}
}
