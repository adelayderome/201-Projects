//Adelayde ROme (7675884868)

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/Searching")
public class Searching extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Searching() {
        super();
 
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PrintWriter pw = response.getWriter();
		
		boolean emptySearch = false;
		String currUser = request.getParameter("Email");
		String searchInput = request.getParameter("searchInput");
		if(searchInput == null) {
			emptySearch = true;
		}
		//make not crash if space is entered
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CalendarUsers?user=root&password=root&useSSL=false");

			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM Users WHERE email NOT IN ('" + currUser + "');");
			//System.out.println("here");
			String searchList = "";
			response.setContentType("text/html");
			int startRow =0, endRow =1;
			
			while(rs.next()) {
				String[] searchName = searchInput.split(" ");
				String name1 = searchName[0].toLowerCase(), name2 = null;
		
				if(searchName.length > 1) {
					name2 = searchName[1].toLowerCase();
				}
				
				String userName = rs.getString("username");
				String[] userNames = userName.split(" ");
				String fname = userNames[0], lname = null;
				if(userNames.length > 1) lname = userNames[1];
				
				
				
				if(fname.toLowerCase().contains(name1) || (name2 != null && fname.toLowerCase().contains(name2)) 
						|| (lname != null && lname.toLowerCase().contains(name1)) || emptySearch ||
						(lname != null && name2 != null && lname.toLowerCase().contains(name2))) {
					if(startRow%3 == 0) searchList += "<div class=\"row\">";
					
					searchList += "<div class=\"column\"><a href=\"friendProfile.jsp?userid=" + rs.getString("email") + 
							"\"> <img id= \"friendPic\" src=\"" +  rs.getString("imgURL") + "\"><p>" + userName + "</p></a></div>";
					
					if(endRow%3 == 0) searchList += "</div>";
					startRow++;
					endRow++;
				}
			}
			if(searchList.equals("")) {
				searchList += "No Users Found."; 
			}
			pw.println(searchList);
			
			
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
