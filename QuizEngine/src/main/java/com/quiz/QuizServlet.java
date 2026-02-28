package com.quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.quiz.util.DBConnection;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
                         throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {

            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM questions");

            out.println("<html><head><title>Online Quiz</title>");

            // ================= CSS =================
            out.println("<style>");
            out.println("body{font-family:Arial;background:linear-gradient(to right,#4facfe,#00f2fe);margin:0;}");
            out.println(".container{width:60%;margin:50px auto;background:white;padding:30px;border-radius:15px;box-shadow:0 8px 25px rgba(0,0,0,0.3);}");
            out.println("#timer{background:#ff4d4d;color:white;padding:10px;border-radius:8px;text-align:center;margin-bottom:20px;font-weight:bold;}");
            out.println(".hidden{display:none;}");
            out.println(".question{margin-top:20px;font-weight:bold;}");
            out.println(".options{margin-left:20px;margin-top:10px;}");
            out.println("input[type=text]{padding:10px;width:60%;border-radius:6px;border:1px solid #ccc;}");
            out.println("button,input[type=submit]{background:#28a745;color:white;padding:12px 25px;border:none;border-radius:8px;cursor:pointer;margin-top:20px;font-size:16px;}");
            out.println("button:hover,input[type=submit]:hover{background:#218838;}");
            out.println("</style>");

            // ================= JAVASCRIPT =================
            out.println("<script>");
            out.println("let timeLeft = 60;");
            out.println("function startQuiz(){");
            out.println("  let name = document.getElementById('username').value;");
            out.println("  if(name.trim() === ''){");
            out.println("     alert('Please enter your name');");
            out.println("     return;");
            out.println("  }");

            // IMPORTANT LINE - Pass name to hidden field
            out.println("  document.getElementById('hiddenName').value = name;");

            out.println("  document.getElementById('nameSection').style.display = 'none';");
            out.println("  document.getElementById('quizSection').style.display = 'block';");
            out.println("  startTimer();");
            out.println("}");

            out.println("function startTimer(){");
            out.println("  let timer = setInterval(function(){");
            out.println("     if(timeLeft <= 0){");
            out.println("        clearInterval(timer);");
            out.println("        alert('Time is up! Submitting quiz.');");
            out.println("        document.getElementById('quizForm').submit();");
            out.println("     }");
            out.println("     document.getElementById('timer').innerHTML = 'Time Left: ' + timeLeft + ' seconds';");
            out.println("     timeLeft--; ");
            out.println("  },1000);");
            out.println("}");
            out.println("</script>");

            out.println("</head><body>");
            out.println("<div class='container'>");
            out.println("<h2>Online Quiz</h2>");

            // ================= NAME SECTION =================
            out.println("<div id='nameSection'>");
            out.println("<label><strong>Enter Your Name:</strong></label><br><br>");
            out.println("<input type='text' id='username'><br>");
            out.println("<button type='button' onclick='startQuiz()'>Continue to Quiz</button>");
            out.println("</div>");

            // ================= QUIZ SECTION =================
            out.println("<div id='quizSection' style='display:none;'>");
            out.println("<div id='timer'></div>");
            out.println("<form id='quizForm' action='result' method='post'>");

            // Hidden field (VERY IMPORTANT)
            out.println("<input type='hidden' name='username' id='hiddenName'>");

            while (rs.next()) {

                int id = rs.getInt("id");

                out.println("<div class='question'>" + rs.getString("question") + "</div>");
                out.println("<div class='options'>");

                out.println("<label><input type='radio' name='q" + id + "' value='1'> "
                        + rs.getString("option1") + "</label><br>");

                out.println("<label><input type='radio' name='q" + id + "' value='2'> "
                        + rs.getString("option2") + "</label><br>");

                out.println("<label><input type='radio' name='q" + id + "' value='3'> "
                        + rs.getString("option3") + "</label><br>");

                out.println("<label><input type='radio' name='q" + id + "' value='4'> "
                        + rs.getString("option4") + "</label><br>");

                out.println("</div>");
            }

            out.println("<input type='submit' value='Submit Quiz'>");
            out.println("</form>");
            out.println("</div>");

            out.println("</div></body></html>");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}