package com.library.servlet;

import java.io.*;
import java.sql.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.library.util.DBConnection;

@WebServlet("/editBook")
public class EditBookServlet extends HttpServlet {

    private static final String HEAD =
        "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'>"
      + "<meta name='viewport' content='width=device-width, initial-scale=1'>"
      + "<title>Update Book</title>"
      + "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>"
      + "<link href='https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css' rel='stylesheet'>"
      + "<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap' rel='stylesheet'>"
      + "<link href='css/style.css' rel='stylesheet'>"
      + "</head><body>";

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("'", "&#39;").replace("\"", "&quot;");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        int id = Integer.parseInt(req.getParameter("id"));

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM books WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                out.println(HEAD);
                out.println("<div class='glass'>");
                out.println("<h2 class='title mb-4'><i class='bi bi-pencil-square me-2'></i>Update Book</h2>");
                out.println("<form action='updateBook' method='post'>");
                out.println("<input type='hidden' name='id' value='" + id + "'>");

                out.println("<div class='input-group mb-3'><span class='input-group-text'><i class='bi bi-book'></i></span>"
                    + "<input type='text' name='bname' class='form-control' value='" + esc(rs.getString("bname")) + "' required></div>");
                out.println("<div class='input-group mb-3'><span class='input-group-text'><i class='bi bi-person'></i></span>"
                    + "<input type='text' name='author' class='form-control' value='" + esc(rs.getString("author")) + "' required></div>");
                out.println("<div class='input-group mb-3'><span class='input-group-text'><i class='bi bi-tags'></i></span>"
                    + "<input type='text' name='btype' class='form-control' value='" + esc(rs.getString("btype")) + "' required></div>");
                out.println("<div class='input-group mb-4'><span class='input-group-text'><i class='bi bi-currency-rupee'></i></span>"
                    + "<input type='number' step='0.01' name='price' class='form-control' value='" + rs.getFloat("price") + "' required></div>");

                out.println("<div class='d-grid'><button type='submit' class='btn btn-grad b3'>"
                    + "<i class='bi bi-check-circle me-2'></i>Update Book</button></div></form>");
                out.println("<div class='text-center mt-3'><a href='viewBooks' class='text-decoration-none fw-semibold'>"
                    + "<i class='bi bi-arrow-left'></i> Back to Books</a></div>");
                out.println("</div></body></html>");
            } else {
                out.println("Book not found.");
            }
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}