package com.devfuncionar.jirametrics.business;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.devfuncionar.jirametrics.domain.Issue;
import com.devfuncionar.jirametrics.domain.Status;
import com.devfuncionar.jirametrics.domain.StatusEn;

public class CSVWriter {

	private static String[] columns = { "Id", "Name" };

	public void write(Collection<Issue> issues) throws IOException {

		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		/*
		 * CreationHelper helps us create instances for various things like DataFormat,
		 * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
		 */
		CreationHelper createHelper = workbook.getCreationHelper();

		// Create a Sheet
		Sheet sheet = workbook.createSheet("Report");
		sheet.setDisplayGridlines(false);

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.WHITE.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(HSSFColorPredefined.ROYAL_BLUE.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		headerCellStyle.setBorderBottom(BorderStyle.THIN);
		headerCellStyle.setBorderLeft(BorderStyle.THIN);
		headerCellStyle.setBorderTop(BorderStyle.THIN);
		headerCellStyle.setBorderRight(BorderStyle.THIN);

		// Create a Row
		Row headerRow = sheet.createRow(0);

		// Creating cells
		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}
		
		int col = columns.length;
		
		for (StatusEn _statusEn : StatusEn.values()) {
			Cell cell = headerRow.createCell(col++);
			cell.setCellValue(_statusEn.getTranslation());
			cell.setCellStyle(headerCellStyle);
		}
		
		{
			Cell cell = headerRow.createCell(col++);
			cell.setCellValue("Time");
			cell.setCellStyle(headerCellStyle);
		}

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);

		// Create Cell Style for formatting Date
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
		dateCellStyle.setBorderBottom(BorderStyle.THIN);
		dateCellStyle.setBorderLeft(BorderStyle.THIN);
		dateCellStyle.setBorderTop(BorderStyle.THIN);
		dateCellStyle.setBorderRight(BorderStyle.THIN);

		CellStyle currencyCellStyle = workbook.createCellStyle();
		currencyCellStyle.setDataFormat(createHelper.createDataFormat()
				.getFormat("_(R$* #,##0.00_);_(R$* (#,##0.00);_(R$* \\\"-\\\"??_);_(@_)"));
		currencyCellStyle.setBorderBottom(BorderStyle.THIN);
		currencyCellStyle.setBorderLeft(BorderStyle.THIN);
		currencyCellStyle.setBorderTop(BorderStyle.THIN);
		currencyCellStyle.setBorderRight(BorderStyle.THIN);

		// Create Other rows and cells with employees data
		int rowNum = 1;
		 
		for (Issue issue : issues) {
			Row row = sheet.createRow(rowNum++);
			int collumnNum = 0;

			Cell cell = row.createCell(collumnNum++);
			cell.setCellValue(issue.getId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(collumnNum++);
			cell.setCellValue(issue.getDescription());
			cell.setCellStyle(cellStyle);
			
			for(Status status : issue.getStatus()) {
				if(status.getStatus() != null) {
					Cell dateCell = row.createCell(collumnNum + status.getStatus().getStep());
					dateCell.setCellValue(status.getMovedIn());
					dateCell.setCellStyle(dateCellStyle);
				}
			}
			
			cell = row.createCell(StatusEn.values().length + collumnNum);
			cell.setCellValue(issue.getTeam());
			cell.setCellStyle(cellStyle);
			collumnNum++;
			
			
//			Cell dateCell = row.createCell(0);
//			dateCell.setCellValue(issue.getCreated());
//			dateCell.setCellStyle(dateCellStyle);
		}

		// Resize all columns to fit the content size
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream("output.xlsx");
		workbook.write(fileOut);
		fileOut.close();

		// Closing the workbook
		workbook.close();
	}
}
