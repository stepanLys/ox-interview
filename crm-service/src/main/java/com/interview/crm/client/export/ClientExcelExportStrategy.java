package com.interview.crm.client.export;

import com.interview.crm.client.dto.ClientResponse;
import com.interview.crm.common.export.ExportStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component("clientExcel")
public class ClientExcelExportStrategy implements ExportStrategy<ClientResponse> {

    @Override
    public String getFormat() {
        return "excel";
    }
    
    @Override
    public String getMimeType() {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }
    
    @Override
    public String getFileExtension() {
        return ".xlsx";
    }

    @Override
    public ByteArrayInputStream export(List<ClientResponse> clients) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Clients");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Company Name", "Industry", "Address"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIdx = 1;
            for (ClientResponse client : clients) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(client.id());
                row.createCell(1).setCellValue(client.companyName());
                row.createCell(2).setCellValue(client.industry());
                row.createCell(3).setCellValue(client.address());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}