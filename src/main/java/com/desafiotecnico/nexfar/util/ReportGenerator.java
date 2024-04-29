package com.desafiotecnico.nexfar.util;

import com.desafiotecnico.nexfar.model.Order;
import com.desafiotecnico.nexfar.model.Item;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.csv.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReportGenerator {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static byte[] generateReport(List<Order> orders, String key, String format) throws IOException {
        if (format.equalsIgnoreCase("XLS")) {
            return createExcelReport(orders, key);
        } else {
            return createCsvReport(orders, key);
        }
    }

    private static byte[] createExcelReport(List<Order> orders, String key) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Report");
            createHeaders(sheet, key);

            int rowNum = 1;
            for (Order order : orders) {
                if (key.equals("ORDER_SIMPLE")) {
                    Row row = sheet.createRow(rowNum++);
                    fillOrderSimpleRow(row, order);
                } else if (key.equals("ORDER_DETAILED")) {
                    for (Item item : order.getItems()) {
                        Row row = sheet.createRow(rowNum++);
                        fillOrderDetailedRow(row, order, item);
                    }
                }
            }

            workbook.write(bos);
            return bos.toByteArray();
        }
    }

    private static byte[] createCsvReport(List<Order> orders, String key) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(bos), CSVFormat.DEFAULT.withHeader(getCsvHeaders(key)))) {
            for (Order order : orders) {
                if (key.equals("ORDER_SIMPLE")) {
                    printer.printRecord(getCsvSimpleRow(order));
                } else if (key.equals("ORDER_DETAILED")) {
                    for (Item item : order.getItems()) {
                        printer.printRecord(getCsvDetailedRow(order, item));
                    }
                }
            }
            printer.flush();
            return bos.toByteArray();
        }
    }

    private static void createHeaders(Sheet sheet, String key) {
        Row header = sheet.createRow(0);
        String[] headers = getCsvHeaders(key);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }
    }

    private static void fillOrderSimpleRow(Row row, Order order) {
        row.createCell(0).setCellValue(order.getId());
        row.createCell(1).setCellValue(order.getClient().getCnpj());
        row.createCell(2).setCellValue(order.getClient().getName());
        row.createCell(3).setCellValue(sdf.format(order.getCreatedAt()));
        row.createCell(4).setCellValue(order.getStatus());
        row.createCell(5).setCellValue(order.getNetTotal());
        row.createCell(6).setCellValue(order.getTotalWithTaxes());
    }

    private static void fillOrderDetailedRow(Row row, Order order, Item item) {
        row.createCell(0).setCellValue(order.getId());
        row.createCell(1).setCellValue(order.getClient().getCnpj());
        row.createCell(2).setCellValue(order.getClient().getName());
        row.createCell(3).setCellValue(sdf.format(order.getCreatedAt()));
        row.createCell(4).setCellValue(order.getStatus());
        row.createCell(5).setCellValue(item.getProduct().getSku());
        row.createCell(6).setCellValue(item.getProduct().getName());
        row.createCell(7).setCellValue(item.getQuantity());
        row.createCell(8).setCellValue(item.getFinalPrice());
        row.createCell(9).setCellValue(item.getFinalPrice());
    }

    private static Object[] getCsvSimpleRow(Order order) {
        return new Object[]{
                order.getId(),
                order.getClient().getCnpj(),
                order.getClient().getName(),
                sdf.format(order.getCreatedAt()),
                order.getStatus(),
                order.getNetTotal(),
                order.getTotalWithTaxes()
        };
    }

    private static Object[] getCsvDetailedRow(Order order, Item item) {
        return new Object[]{
                order.getId(),
                order.getClient().getCnpj(),
                order.getClient().getName(),
                sdf.format(order.getCreatedAt()),
                order.getStatus(),
                item.getProduct().getSku(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getFinalPrice(),
                item.getFinalPrice()
        };
    }

    private static String[] getCsvHeaders(String key) {
        if (key.equals("ORDER_SIMPLE")) {
            return new String[]{"ID", "CNPJ", "Name", "CreatedAt", "Status", "NetTotal", "TotalWithTaxes"};
        } else if (key.equals("ORDER_DETAILED")) {
            return new String[]{"Order ID", "CNPJ", "Client Name", "Created At", "Status", "SKU", "Product Name", "Quantity", "Price", "Final Price"};
        }
        return new String[0];
    }
}
