package com.quiz;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@WebServlet("/certificate")
public class CertificateServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String score = request.getParameter("score");
        String total = request.getParameter("total");

        if (username == null || username.trim().isEmpty()) {
            username = "Guest";
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Certificate.pdf");

        try {

            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // ================= WATERMARK BADGE =================
            Image watermark = Image.getInstance(
                    getServletContext().getRealPath("/images/badge.png"));

            watermark.scaleAbsolute(400, 400);
            watermark.setAbsolutePosition(
                    (PageSize.A4.getWidth() - 400) / 2,
                    (PageSize.A4.getHeight() - 400) / 2);

            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.15f); // 8% visibility (very light)

            PdfContentByte canvas = writer.getDirectContentUnder();
            canvas.saveState();
            canvas.setGState(gs);
            canvas.addImage(watermark);
            canvas.restoreState();

            // ================= TOP LOGO =================
            Image logo = Image.getInstance(
                    getServletContext().getRealPath("/images/Symbol.png"));

            logo.scaleAbsolute(80, 80);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            document.add(new Paragraph("\n"));

            // ================= TITLE =================
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 28, Font.BOLD, BaseColor.BLUE);
            Paragraph title = new Paragraph("CERTIFICATE OF ACHIEVEMENT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n\n"));

            // ================= CONTENT =================
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 16);

            Paragraph content1 = new Paragraph(
                    "This is to certify that", normalFont);
            content1.setAlignment(Element.ALIGN_CENTER);
            document.add(content1);

            document.add(new Paragraph("\n"));

            Font nameFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
            Paragraph namePara = new Paragraph(username.toUpperCase(), nameFont);
            namePara.setAlignment(Element.ALIGN_CENTER);
            document.add(namePara);

            document.add(new Paragraph("\n"));

            Paragraph content2 = new Paragraph(
                    "has successfully completed the Online Quiz Examination",
                    normalFont);
            content2.setAlignment(Element.ALIGN_CENTER);
            document.add(content2);

            document.add(new Paragraph("\n"));

            Paragraph scorePara = new Paragraph(
                    "Score: " + score + " / " + total,
                    new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY));
            scorePara.setAlignment(Element.ALIGN_CENTER);
            document.add(scorePara);

            document.add(new Paragraph("\n\n"));

            Paragraph datePara = new Paragraph(
                    "Date: " + LocalDate.now(),
                    new Font(Font.FontFamily.HELVETICA, 14));
            datePara.setAlignment(Element.ALIGN_RIGHT);
            document.add(datePara);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}