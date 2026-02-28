package com.quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quiz.util.DBConnection;

@WebServlet("/result")
public class ResultServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
                          throws ServletException, IOException {

        int score = 0;
        String name = request.getParameter("username");

        if (name == null || name.trim().isEmpty()) {
            name = "Guest";
        }

        try {

            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM questions");

            int total = 0;

            while (rs.next()) {
                total++;
                int id = rs.getInt("id");
                int correct = rs.getInt("correct_option");

                String userAnswer = request.getParameter("q" + id);

                if (userAnswer != null &&
                        Integer.parseInt(userAnswer) == correct) {
                    score++;
                }
            }

            double percentage = 0;
            if (total > 0) {
                percentage = ((double) score / total) * 100;
            }

            double passPercentage = 60.0;

            // Save result in database
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO results(username, score, total, exam_date) VALUES (?, ?, ?, ?)"
            );

            ps.setString(1, name);
            ps.setInt(2, score);
            ps.setInt(3, total);
            ps.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            ps.executeUpdate();

            con.close();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("<html><head><title>Result</title>");
            out.println("<style>");
            out.println("body{font-family:Arial;background:linear-gradient(to right,#4facfe,#00f2fe);text-align:center;padding-top:100px;}");
            out.println(".box{background:white;width:40%;margin:auto;padding:30px;border-radius:15px;box-shadow:0 8px 25px rgba(0,0,0,0.3);}");
            out.println("button{background:#28a745;color:white;padding:12px 25px;border:none;border-radius:8px;cursor:pointer;font-size:16px;margin-top:20px;}");
            out.println("button:hover{background:#218838;}");
            out.println("</style></head><body>");
            out.println("<div class='box'>");

            out.println("<h2>Quiz Result</h2>");
            out.println("<p><strong>Name:</strong> " + name + "</p>");
            out.println("<p><strong>Score:</strong> " + score + " / " + total + "</p>");
            out.println("<p><strong>Percentage:</strong> " + String.format("%.2f", percentage) + "%</p>");

            if (percentage >= passPercentage) {

                out.println("<h3 style='color:green;'>Congratulations! You Passed 🎉</h3>");
                out.println("<form action='certificate' method='get'>");
                out.println("<input type='hidden' name='username' value='" + name + "'>");
                out.println("<input type='hidden' name='score' value='" + score + "'>");
                out.println("<input type='hidden' name='total' value='" + total + "'>");
                out.println("<button type='submit'>Download Certificate</button>");
                out.println("</form>");

            } else {

                out.println("<h3 style='color:red;'>Sorry! You did not pass.</h3>");
                out.println("<p>Minimum Required: 60%</p>");

            }

            out.println("</div></body></html>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}