package com.library.servlet;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.library.util.DBConnection;

@WebServlet("/updateBook")
public class UpdateBookServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "UPDATE books SET bname=?, author=?, btype=?, price=? WHERE id=?")) {
            ps.setString(1, req.getParameter("bname"));
            ps.setString(2, req.getParameter("author"));
            ps.setString(3, req.getParameter("btype"));
            ps.setFloat(4, Float.parseFloat(req.getParameter("price")));
            ps.setInt(5, Integer.parseInt(req.getParameter("id")));
            ps.executeUpdate();
            res.sendRedirect("viewBooks");
        } catch (Exception e) {
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}