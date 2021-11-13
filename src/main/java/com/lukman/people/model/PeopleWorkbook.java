package com.lukman.people.model;

import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PeopleWorkbook {
    private final XSSFWorkbook workbook = new XSSFWorkbook();
    private final XSSFSheet sheet = workbook.createSheet("Customers");
    private static final int PREFERRED_COLUMN_WIDTH = 20 * 256; //1/256th of char width i.e. 20 * 256 is 20 character width
    private int rowIndex = 0;

    public PeopleWorkbook(List<String> columnNames){
        addColumnHeaders(sheet, columnNames);
        addHeaderAndFooter(sheet);
    }

    public void addTableData(Person person) {
        XSSFRow currentRow = sheet.createRow(++rowIndex);
        int columnIndex = 0;

        XSSFCell idCell = currentRow.createCell(columnIndex);
        idCell.setCellStyle(defaultCellStyle());
        idCell.setCellValue(person.getId());

        XSSFCell firstNameCell = currentRow.createCell(++columnIndex);
        firstNameCell.setCellStyle(defaultCellStyle());
        firstNameCell.setCellValue(person.getFirstName());

        XSSFCell lastNameCell = currentRow.createCell(++columnIndex);
        lastNameCell.setCellStyle(defaultCellStyle());
        lastNameCell.setCellValue(person.getLastName());

        XSSFCell emailCell = currentRow.createCell(++columnIndex);
        emailCell.setCellStyle(defaultCellStyle());
        emailCell.setCellValue(person.getEmail());

        XSSFCell ageCell = currentRow.createCell(++columnIndex);
        ageCell.setCellStyle(defaultCellStyle());
        ageCell.setCellValue(person.getAge());

        XSSFCell itemsBoughtCell = currentRow.createCell(++columnIndex);
        itemsBoughtCell.setCellStyle(defaultCellStyle());
        itemsBoughtCell.setCellValue(String.format("%,d", person.getNumberOfItemsBought()));

        XSSFCell totalSpentCell = currentRow.createCell(++columnIndex);
        totalSpentCell.setCellStyle(defaultCellStyle());
        totalSpentCell.setCellValue(String.format("$ %,.2f", person.getTotalAmountSpent()));

        XSSFCell verifiedCell = currentRow.createCell(++columnIndex);
        verifiedCell.setCellStyle(defaultCellStyle());
        verifiedCell.setCellValue(person.isVerified());
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        workbook.write(outputStream);
    }

    private void addColumnHeaders(XSSFSheet sheet, List<String> columnNames) {
        XSSFRow columnRow = sheet.createRow(rowIndex++);

        for (int i = 0; i < columnNames.size(); i++) {
            XSSFCell cell = columnRow.createCell(i);
            sheet.setColumnWidth(cell.getColumnIndex(), PREFERRED_COLUMN_WIDTH);
            cell.setCellStyle(defaultCellStyle());
            cell.setCellValue(columnNames.get(i));
        }
    }

    private XSSFCellStyle defaultCellStyle() {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private void addHeaderAndFooter(XSSFSheet sheet) {
        Header header = sheet.getHeader();
        header.setLeft("Demo Organisation");
        header.setRight("Customers");

        Footer footer = sheet.getFooter();
        footer.setCenter("List of loyal customers for the month");
        footer.setRight("Page " + HeaderFooter.page() + " of " + HeaderFooter.numPages());
    }
}
