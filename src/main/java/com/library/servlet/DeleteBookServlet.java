package com.library.servlet;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.library.util.DBConnection;

@WebServlet("/deleteBook")
public class DeleteBookServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM books WHERE id=?")) {
            ps.setInt(1, Integer.parseInt(req.getParameter("id")));
            ps.executeUpdate();
            res.sendRedirect("viewBooks");
        } catch (Exception e) {
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}