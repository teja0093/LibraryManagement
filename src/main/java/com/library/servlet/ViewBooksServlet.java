package com.library.servlet;

import java.io.*;
import java.sql.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.library.util.DBConnection;

@WebServlet("/viewBooks")
public class ViewBooksServlet extends HttpServlet {

    private static final String HEAD =
        "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'>"
      + "<meta name='viewport' content='width=device-width, initial-scale=1'>"
      + "<title>View Books</title>"
      + "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>"
      + "<link href='https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css' rel='stylesheet'>"
      + "<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap' rel='stylesheet'>"
      + "<link href='css/style.css' rel='stylesheet'>"
      + "</head><body>";

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        out.println(HEAD);
        out.println("<div class='glass wide'>");
        out.println("<h2 class='title mb-4'><i class='bi bi-collection me-2'></i>All Books</h2>");
        out.println("<div class='table-responsive'>");
        out.println("<table class='table table-striped text-center mb-0'><thead><tr>"
                  + "<th>ID</th><th>Name</th><th>Author</th><th>Type</th><th>Price</th><th>Actions</th>"
                  + "</tr></thead><tbody>");

        boolean hasRows = false;
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM books")) {
            while (rs.next()) {
                hasRows = true;
                int id = rs.getInt("id");
                out.println("<tr><td><span class='badge bg-secondary'>" + id + "</span></td>"
                    + "<td class='fw-semibold'>" + esc(rs.getString("bname")) + "</td>"
                    + "<td>" + esc(rs.getString("author")) + "</td>"
                    + "<td><span class='badge rounded-pill text-bg-primary'>" + esc(rs.getString("btype")) + "</span></td>"
                    + "<td class='price'>&#8377; " + String.format("%.2f", rs.getFloat("price")) + "</td>"
                    + "<td><a class='btn btn-edit me-1' href='editBook?id=" + id + "'><i class='bi bi-pencil-square'></i> Edit</a>"
                    + "<a class='btn btn-del' href='deleteBook?id=" + id + "' onclick=\"return confirm('Delete this book?')\">"
                    + "<i class='bi bi-trash'></i> Delete</a></td></tr>");
            }
            if (!hasRows) {
                out.println("<tr><td colspan='6' class='text-muted py-4'>No books yet. Click Add Book to create one.</td></tr>");
            }
        } catch (Exception e) {
            out.println("<tr><td colspan='6' class='text-danger'>Error: " + e.getMessage() + "</td></tr>");
        }

        out.println("</tbody></table></div>");
        out.println("<div class='text-center mt-4'>"
                  + "<a href='index.html' class='btn btn-grad b1 me-2'><i class='bi bi-house'></i> Home</a>"
                  + "<a href='addBook.html' class='btn btn-grad b2'><i class='bi bi-plus-circle'></i> Add Book</a></div>");
        out.println("</div></body></html>");
    }
}