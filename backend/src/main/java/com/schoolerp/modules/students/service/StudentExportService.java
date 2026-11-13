package com.schoolerp.modules.students.service;

import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentExportService {

    private final StudentRepository studentRepository;

    /**
     * Retrieve students filtered by class and optional section.
     */
    public List<Student> getStudentsForExport(String tenantId, String className, String sectionName) {
        if (className != null && !className.isBlank() && !"ALL".equalsIgnoreCase(className)) {
            if (sectionName != null && !sectionName.isBlank() && !"ALL".equalsIgnoreCase(sectionName)) {
                return studentRepository.findAllByTenantIdAndClassNameAndSectionNameOrderByRollNumberAsc(tenantId, className, sectionName);
            }
            return studentRepository.findAllByTenantIdAndClassNameOrderByRollNumberAsc(tenantId, className);
        }
        return studentRepository.findAllByTenantId(tenantId);
    }

    /**
     * Generate structured Microsoft Excel (.xlsx) spreadsheet with Apache POI.
     */
    public byte[] generateExcelExport(String tenantId, String className, String sectionName) throws IOException {
        List<Student> students = getStudentsForExport(tenantId, className, sectionName);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Class Roster");
            sheet.setDisplayGridlines(true);

            // Cell Styles
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            Font metaFont = workbook.createFont();
            metaFont.setItalic(true);
            metaFont.setFontHeightInPoints((short) 10);
            metaFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());

            CellStyle metaStyle = workbook.createCellStyle();
            metaStyle.setFont(metaFont);
            metaStyle.setAlignment(HorizontalAlignment.CENTER);

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setFontHeightInPoints((short) 11);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.MEDIUM);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            CellStyle zebraStyle = workbook.createCellStyle();
            zebraStyle.cloneStyleFrom(dataStyle);
            zebraStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            zebraStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle centerDataStyle = workbook.createCellStyle();
            centerDataStyle.cloneStyleFrom(dataStyle);
            centerDataStyle.setAlignment(HorizontalAlignment.CENTER);

            // Title Banner
            Row titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(24);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("SCHOOL ERP - OFFICIAL CLASS ENROLLMENT & ACADEMIC ROSTER");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));

            // Subtitle & Scope
            String classLabel = (className != null && !className.isBlank()) ? className : "All Classes";
            String sectionLabel = (sectionName != null && !sectionName.isBlank()) ? sectionName : "All Sections";
            Row subRow = sheet.createRow(1);
            Cell subCell = subRow.createCell(0);
            subCell.setCellValue("Cohort: " + classLabel + " (" + sectionLabel + ") | Academic Session: 2026-2027 | Total Students: " + students.size());
            subCell.setCellStyle(metaStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 14));

            // Generation Date
            Row dateRow = sheet.createRow(2);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")) + " | Single Source of Truth DB");
            dateCell.setCellStyle(metaStyle);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 14));

            // Spacing Row (Row index 3)
            sheet.createRow(3);

            // Header Columns (Row index 4)
            String[] headers = {
                    "S.No", "Admission No", "Roll No", "Student Full Name", "Gender",
                    "Date of Birth", "Class", "Section", "Parent / Guardian", "Guardian Phone",
                    "Guardian Email", "Attendance %", "Fee Status", "Balance Dues (INR)", "Status"
            };

            Row headerRow = sheet.createRow(4);
            headerRow.setHeightInPoints(26);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Populate Student Data Rows
            int rowIndex = 5;
            int serialNo = 1;
            for (Student s : students) {
                Row row = sheet.createRow(rowIndex);
                CellStyle currentStyle = (serialNo % 2 == 0) ? zebraStyle : dataStyle;

                Cell c0 = row.createCell(0); c0.setCellValue(serialNo++); c0.setCellStyle(centerDataStyle);
                Cell c1 = row.createCell(1); c1.setCellValue(s.getAdmissionNumber() != null ? s.getAdmissionNumber() : "-"); c1.setCellStyle(centerDataStyle);
                Cell c2 = row.createCell(2); c2.setCellValue(s.getRollNumber()); c2.setCellStyle(centerDataStyle);
                Cell c3 = row.createCell(3); c3.setCellValue(s.getFullName() != null ? s.getFullName() : "-"); c3.setCellStyle(currentStyle);
                Cell c4 = row.createCell(4); c4.setCellValue(s.getGender() != null ? s.getGender() : "-"); c4.setCellStyle(centerDataStyle);
                Cell c5 = row.createCell(5); c5.setCellValue(s.getDateOfBirth() != null ? s.getDateOfBirth().toString() : "-"); c5.setCellStyle(centerDataStyle);
                Cell c6 = row.createCell(6); c6.setCellValue(s.getClassName() != null ? s.getClassName() : "-"); c6.setCellStyle(centerDataStyle);
                Cell c7 = row.createCell(7); c7.setCellValue(s.getSectionName() != null ? s.getSectionName() : "-"); c7.setCellStyle(centerDataStyle);
                Cell c8 = row.createCell(8); c8.setCellValue(s.getGuardianName() != null ? s.getGuardianName() : "-"); c8.setCellStyle(currentStyle);
                Cell c9 = row.createCell(9); c9.setCellValue(s.getGuardianPhone() != null ? s.getGuardianPhone() : "-"); c9.setCellStyle(centerDataStyle);
                Cell c10 = row.createCell(10); c10.setCellValue(s.getGuardianEmail() != null ? s.getGuardianEmail() : "-"); c10.setCellStyle(currentStyle);
                Cell c11 = row.createCell(11); c11.setCellValue(s.getAttendancePercentage() + "%"); c11.setCellStyle(centerDataStyle);
                Cell c12 = row.createCell(12); c12.setCellValue(s.getFeeStatus() != null ? s.getFeeStatus() : "Paid"); c12.setCellStyle(centerDataStyle);
                Cell c13 = row.createCell(13); c13.setCellValue("INR " + String.format("%.2f", s.getBalanceAmount())); c13.setCellStyle(centerDataStyle);
                Cell c14 = row.createCell(14); c14.setCellValue(s.getStatus() != null ? s.getStatus() : "Active"); c14.setCellStyle(centerDataStyle);

                rowIndex++;
            }

            // Auto-size columns with minimum widths
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                int currentWidth = sheet.getColumnWidth(i);
                if (currentWidth < 3000) {
                    sheet.setColumnWidth(i, 3500);
                }
            }

            // Freeze header rows
            sheet.createFreezePane(0, 5);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Generate RFC 4180 compliant CSV stream with UTF-8 BOM for Microsoft Excel.
     */
    public String generateCsvExport(String tenantId, String className, String sectionName) {
        List<Student> students = getStudentsForExport(tenantId, className, sectionName);
        StringBuilder sb = new StringBuilder();

        // Prepend UTF-8 BOM so Excel opens regional / special characters flawlessly
        sb.append('\uFEFF');

        // CSV Header
        sb.append("S.No,Admission No,Roll No,Full Name,Gender,DOB,Class,Section,Guardian Name,Guardian Phone,Guardian Email,Attendance %,Fee Status,Balance INR,Status\n");

        int sNo = 1;
        for (Student s : students) {
            sb.append(sNo++).append(",");
            sb.append(escapeCsv(s.getAdmissionNumber())).append(",");
            sb.append(s.getRollNumber()).append(",");
            sb.append(escapeCsv(s.getFullName())).append(",");
            sb.append(escapeCsv(s.getGender())).append(",");
            sb.append(s.getDateOfBirth() != null ? s.getDateOfBirth().toString() : "").append(",");
            sb.append(escapeCsv(s.getClassName())).append(",");
            sb.append(escapeCsv(s.getSectionName())).append(",");
            sb.append(escapeCsv(s.getGuardianName())).append(",");
            sb.append(escapeCsv(s.getGuardianPhone())).append(",");
            sb.append(escapeCsv(s.getGuardianEmail())).append(",");
            sb.append(s.getAttendancePercentage()).append("%,");
            sb.append(escapeCsv(s.getFeeStatus())).append(",");
            sb.append(String.format("%.2f", s.getBalanceAmount())).append(",");
            sb.append(escapeCsv(s.getStatus())).append("\n");
        }

        return sb.toString();
    }

    /**
     * Generate a printer-ready HTML document with native @media print styling.
     */
    public String generatePrintableRosterHtml(String tenantId, String className, String sectionName) {
        List<Student> students = getStudentsForExport(tenantId, className, sectionName);
        String classTitle = (className != null && !className.isBlank()) ? className : "All Classes";
        String sectionTitle = (sectionName != null && !sectionName.isBlank()) ? sectionName : "All Sections";
        String genTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss"));

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
            .append("<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n")
            .append("<title>Class Roster - ").append(classTitle).append(" ").append(sectionTitle).append("</title>\n")
            .append("<style>\n")
            .append("  body { font-family: 'Segoe UI', Arial, sans-serif; margin: 20px; color: #1e293b; }\n")
            .append("  .header { border-bottom: 2px solid #0f172a; padding-bottom: 12px; margin-bottom: 20px; text-align: center; }\n")
            .append("  .header h1 { margin: 0; font-size: 22px; text-transform: uppercase; letter-spacing: 0.5px; color: #0f172a; }\n")
            .append("  .header p { margin: 4px 0 0; font-size: 13px; color: #64748b; }\n")
            .append("  .meta-bar { display: flex; justify-content: space-between; margin-bottom: 16px; font-size: 13px; font-weight: 600; }\n")
            .append("  .action-bar { margin-bottom: 20px; display: flex; justify-content: flex-end; }\n")
            .append("  .btn-print { background: #2563eb; color: #fff; border: none; padding: 8px 16px; border-radius: 6px; font-weight: 600; cursor: pointer; }\n")
            .append("  .btn-print:hover { background: #1d4ed8; }\n")
            .append("  table { width: 100%; border-collapse: collapse; font-size: 12px; }\n")
            .append("  th, td { border: 1px solid #cbd5e1; padding: 6px 8px; text-align: left; }\n")
            .append("  th { background-color: #f1f5f9; font-weight: 700; color: #334155; text-align: center; }\n")
            .append("  .text-center { text-align: center; }\n")
            .append("  .check-box { width: 45px; text-align: center; }\n")
            .append("  .remarks-col { width: 140px; }\n")
            .append("  .signatures { display: flex; justify-content: space-between; margin-top: 50px; padding-top: 20px; font-size: 12px; }\n")
            .append("  .sig-line { border-top: 1px solid #475569; width: 180px; text-align: center; padding-top: 5px; }\n")
            .append("  @media print {\n")
            .append("    .action-bar { display: none !important; }\n")
            .append("    body { margin: 0; }\n")
            .append("    th { background-color: #f8fafc !important; -webkit-print-color-adjust: exact; }\n")
            .append("    tr { page-break-inside: avoid; }\n")
            .append("  }\n")
            .append("</style>\n</head>\n<body>\n")
            .append("<div class=\"action-bar\">\n")
            .append("  <button class=\"btn-print\" onclick=\"window.print()\">Print Document / Save PDF</button>\n")
            .append("</div>\n")
            .append("<div class=\"header\">\n")
            .append("  <h1>Official Classroom Attendance & Verification Roster</h1>\n")
            .append("  <p>Affiliated with Central Board of Secondary Education (CBSE) | Institutional ERP Ledger</p>\n")
            .append("</div>\n")
            .append("<div class=\"meta-bar\">\n")
            .append("  <div><strong>Class & Section:</strong> ").append(classTitle).append(" - ").append(sectionTitle).append("</div>\n")
            .append("  <div><strong>Session:</strong> 2026-2027 | <strong>Total Enrolled:</strong> ").append(students.size()).append("</div>\n")
            .append("  <div><strong>Date:</strong> ").append(genTime).append("</div>\n")
            .append("</div>\n")
            .append("<table>\n<thead>\n<tr>\n")
            .append("  <th>S.No</th>\n<th>Roll</th>\n<th>Adm No</th>\n<th>Student Name</th>\n<th>Gender</th>\n")
            .append("  <th>Guardian Contact</th>\n<th>Attn %</th>\n<th>Fee Status</th>\n")
            .append("  <th class=\"check-box\">Present [ / ]</th>\n<th class=\"remarks-col\">Notes / Remarks</th>\n")
            .append("</tr>\n</thead>\n<tbody>\n");

        int count = 1;
        for (Student s : students) {
            html.append("<tr>\n")
                .append("  <td class=\"text-center\">").append(count++).append("</td>\n")
                .append("  <td class=\"text-center\"><strong>").append(s.getRollNumber()).append("</strong></td>\n")
                .append("  <td class=\"text-center\">").append(s.getAdmissionNumber() != null ? s.getAdmissionNumber() : "-").append("</td>\n")
                .append("  <td><strong>").append(s.getFullName() != null ? s.getFullName() : "-").append("</strong></td>\n")
                .append("  <td class=\"text-center\">").append(s.getGender() != null ? s.getGender() : "-").append("</td>\n")
                .append("  <td>").append(s.getGuardianPhone() != null ? s.getGuardianPhone() : "-").append("</td>\n")
                .append("  <td class=\"text-center\">").append(s.getAttendancePercentage()).append("%</td>\n")
                .append("  <td class=\"text-center\">").append(s.getFeeStatus() != null ? s.getFeeStatus() : "Paid").append("</td>\n")
                .append("  <td class=\"check-box\">[ &nbsp; ]</td>\n")
                .append("  <td></td>\n")
                .append("</tr>\n");
        }

        html.append("</tbody>\n</table>\n")
            .append("<div class=\"signatures\">\n")
            .append("  <div class=\"sig-line\">Class Teacher Signature</div>\n")
            .append("  <div class=\"sig-line\">Exam / Activity Coordinator</div>\n")
            .append("  <div class=\"sig-line\">Principal Seal & Signature</div>\n")
            .append("</div>\n")
            .append("</body>\n</html>");

        return html.toString();
    }

    private String escapeCsv(String val) {
        if (val == null) return "";
        if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }
}

