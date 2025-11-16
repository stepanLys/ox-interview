package com.interview.crm.client.export;

import com.interview.crm.client.dto.ClientResponse;
import com.interview.crm.common.export.ExportStrategy;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component("clientPdf")
public class ClientPdfExportStrategy implements ExportStrategy<ClientResponse> {

    @Override
    public String getFormat() {
        return "pdf";
    }

    @Override
    public String getMimeType() {
        return "application/pdf";
    }
    
    @Override
    public String getFileExtension() {
        return ".pdf";
    }

    @Override
    public ByteArrayInputStream export(List<ClientResponse> clients) throws IOException {
        try (Document document = new Document(PageSize.A4);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            PdfWriter.getInstance(document, out);
            document.open();

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            
            table.addCell(new PdfPCell(new Phrase("ID")));
            table.addCell(new PdfPCell(new Phrase("Company Name")));
            table.addCell(new PdfPCell(new Phrase("Industry")));
            table.addCell(new PdfPCell(new Phrase("Address")));

            for (ClientResponse client : clients) {
                table.addCell(String.valueOf(client.id()));
                table.addCell(client.companyName());
                table.addCell(client.industry());
                table.addCell(client.address());
            }

            document.add(table);
            document.close();

            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}