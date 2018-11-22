package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import entity.Result;
import entity.User;


@WebServlet("/demo")
public class DemoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DemoServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int page = 1, limit = 10;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * from test limit " + (page - 1) * limit + ", " + page * limit);
			List<User> data = new ArrayList<>();
			while(rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				data.add(user);
			}
			rs.close();
			rs = st.executeQuery("select count(*) from test");
			rs.next();
			int count = rs.getInt(1);
			Result r = new Result();
			r.setCode(0);
			r.setMsg("success");
			r.setCount(count);
			r.setData(data);
			Gson gson = new Gson();
			String json = gson.toJson(r);
			response.getWriter().write(json);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
