//Adelayde Rome (7675884868)

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


@WebServlet("/Following")
public class Following extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Following() {
        super();

    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PrintWriter pw = response.getWriter();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CalendarUsers?user=root&password=root&useSSL=false");
			
			st = conn.createStatement();
			String email = request.getParameter("userEmail");
			rs = st.executeQuery("SELECT u.email, u.imgURL, u.username  FROM Users u, UserFollowing f WHERE "
					+ "f.follower = '" + email + "' AND u.email=f.followingUser");
			// FROM UserFollowing WHERE follower='" + email + "';");
			
			String followingHTML = "<h2>Following</h2>";
			int startRow = 0;
			int endRow = 1;
			while(rs.next()) {
				if(startRow%4 == 0) followingHTML += "<div class=\"row\">";
				followingHTML += "<div class=\"column\"><a href=\"friendProfile.jsp?userid=" + rs.getString("email") + 
						"\"><figure><img id=\"friendPic\" src=\"" + rs.getString("imgURL") + "\"/>"
						+ "<figcaption>" + rs.getString("username") + "</figcaption></figure></a></div>";
				if(endRow%4 == 0) followingHTML += "</div>";
				startRow++;
				endRow++;
			}
			pw.println(followingHTML); 
			
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
