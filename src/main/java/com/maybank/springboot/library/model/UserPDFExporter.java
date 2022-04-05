package com.maybank.springboot.library.model;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class UserPDFExporter {
    private List<Approve> listApprove;
    
    public UserPDFExporter(List<Approve> listApprove) {
        this.listApprove = listApprove;
    }

	private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.YELLOW);
        cell.setPadding(5);
         
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.BLACK);
         
        cell.setPhrase(new Phrase("Approval ID", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Book Title", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Book Author", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Book Publisher", font));
        table.addCell(cell);    
        
        cell.setPhrase(new Phrase("Rent Date", font));
        table.addCell(cell);  
        
        cell.setPhrase(new Phrase("Return Date", font));
        table.addCell(cell);  
        
        cell.setPhrase(new Phrase("Status", font));
        table.addCell(cell);  
        
        cell.setPhrase(new Phrase("Approved By", font));
        table.addCell(cell);  
    }
     
    private void writeTableData(PdfPTable table) {
        for (Approve approve : listApprove) {
            table.addCell(String.valueOf(approve.getApprove_id()));
            table.addCell(approve.getBook().getBook_title());
            table.addCell(approve.getBook().getBook_author());
            table.addCell(approve.getBook().getBook_publisher());
            table.addCell(approve.getRent_date());
            table.addCell(approve.getReturn_date());
            table.addCell(approve.getStatus());
            table.addCell(approve.getEmployee());
        }
    }
     
    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
         
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLACK);
         
        Paragraph p = new Paragraph("My Checkout", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
         
        document.add(p);
         
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 2.0f, 2.2f, 1.3f, 1.5f, 1.2f, 2.0f});
        table.setSpacingBefore(10);
         
        writeTableHeader(table);
        writeTableData(table);
         
        document.add(table);
         
        document.close();
         
    }
}
