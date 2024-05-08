package pl.sonmiike.reportsservice.report.util;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.category.Category;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.income.Income;
import pl.sonmiike.reportsservice.report.repository.ReportEntity;
import pl.sonmiike.reportsservice.report.repository.ReportEntityRepository;
import pl.sonmiike.reportsservice.report.repository.ReportType;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.report.types.ReportDataKey;
import pl.sonmiike.reportsservice.user.UserReport;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class PDFCreationHelper {

    // TODO Check the pdf after replacing strings with enums, probably it will have enum name so u need to make string for enums and constructor to return name

    public static final DeviceRgb DARK_GREEN_COLOR = new DeviceRgb(50, 102, 71);
    public static final DeviceRgb DARK_RED_COLOR = new DeviceRgb(220, 20, 60);


    public void createDirectories(String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException("Could not create report output directory", e);
        }
    }

    public String generatePdf(Report report, String basePath, ReportEntityRepository repository) {
        if (report == null || report.getUser() == null) {
            System.out.println("Report or user is null, aborting PDF generation.");
            return "";
        }
        String username = report.getUser().getUsername();
        String dateInterval = report.getReportData().get(ReportDataKey.DATE_INTERVAL).toString();
        String fileName = report.getReportType() + "_" + dateInterval + "_" + username + ".pdf";
        String outputPath = Paths.get(basePath, fileName).toString();
        if (Files.exists(Paths.get(outputPath))) {
            System.out.println("Report already exists, aborting PDF generation.");
            return "";
        }
        try (PdfWriter writer = new PdfWriter(outputPath);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph(report.getReportType().toString())
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Report for " + report.getUser().getEmail())
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER));

            Map<ReportDataKey, Object> reportData = report.getReportData();
            ReportDataKey[] order = getOrderForReportType(report.getReportType());

            for (ReportDataKey key : order) {
                Object value = reportData.get(key);
                if (value == null) continue;

                if (key.equals(ReportDataKey.BUDGET_SUMMARY) || key.equals(ReportDataKey.BIGGEST_EXPENSE) || key.equals(ReportDataKey.SMALLEST_EXPENSE)) {

                    handleSpecialEntries(document, key, value);
                    continue;
                }
                if (key.equals(ReportDataKey.CATEGORY_EXPENSES)) {
                    createCategoryExpensesTable(document, (Map<Category, BigDecimal>) value);
                    continue;
                }
                if (value instanceof List) {
                    createCenteredTable(document, key, value);
                } else {
                    document.add(createKeyValueParagraph(key, value));
                }
            }

            document.add(new Paragraph("Report Generated on: " + new Date()));
            System.out.println("Report saved to: " + outputPath);
            DateInterval dateIntervalObj = getDateIntervalFromString(dateInterval);
            if (!repository.existsByStartDateAndEndDate(dateIntervalObj.getStartDate(), dateIntervalObj.getEndDate())) {
                repository.save(getReportEntity(report.getUser(), report.getReportType(), dateIntervalObj, fileName));
            }
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private DateInterval getDateIntervalFromString(String dateInterval) {
        String[] dates = dateInterval.split("-");
        String startDate = dates[0] + "-" + dates[1] + "-" + dates[2];
        String endDate = dates[3] + "-" + dates[4] + "-" + dates[5];
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return new DateInterval(start, end);
    }


    private void handleSpecialEntries(Document document, ReportDataKey key, Object value) {
        if (key.equals(ReportDataKey.BUDGET_SUMMARY)) {
            BigDecimal summary = new BigDecimal(value.toString());
            Paragraph summaryParagraph = new Paragraph(key + ": ").setBold();
            String summaryText = summary.compareTo(BigDecimal.ZERO) > 0 ? "+" + summary.toPlainString() : summary.toPlainString();
            summaryParagraph.add(new Text(summaryText)
                    .setFontColor(summary.compareTo(BigDecimal.ZERO) > 0 ? DARK_GREEN_COLOR : DARK_RED_COLOR));
            document.add(summaryParagraph);
        } else {
            Expense expense = (Expense) value;
            String expenseText = String.format("%s on %s spent %s",
                    expense.getDescription(),
                    expense.getDate().toString(),
                    expense.getAmount().toPlainString());
            document.add(createKeyValueParagraph(key, expenseText));
        }
    }

    private void createCenteredTable(Document document, ReportDataKey key, Object value) {
        List<?> list = (List<?>) value;
        if (!list.isEmpty() && (list.get(0) instanceof Income || list.get(0) instanceof Expense)) {
            Table table = new Table(UnitValue.createPercentArray(new float[]{0.8f, 2.5f, 0.8f, 1f}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell(new Cell().add(new Paragraph("Date").setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(new Paragraph("Description").setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(new Paragraph("Amount").setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(new Paragraph("Category").setTextAlignment(TextAlignment.CENTER)));

            for (Object item : list) {
                if (item instanceof Expense expense) {
                    table.addCell(new Cell().add(new Paragraph(expense.getDate().toString()).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(expense.getDescription()).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph("-" + expense.getAmount().toPlainString()).setFontColor(DARK_RED_COLOR).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(expense.getCategory().getName()).setTextAlignment(TextAlignment.CENTER)));
                } else if (item instanceof Income income) {
                    table.addCell(new Cell().add(new Paragraph(income.getIncomeDate().toString()).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(income.getDescription()).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph("+" + income.getAmount().toPlainString()).setFontColor(DARK_GREEN_COLOR).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph("N/A")));
                }
            }
            document.add(new Paragraph(key + ":").setBold());
            document.add(table);
        }
    }

    private Paragraph createKeyValueParagraph(ReportDataKey key, Object value) {
        Paragraph p = new Paragraph();
        p.add(new Text(key + ": ").setBold());
        p.add(new Text(value.toString()));

        return p;
    }

    private void createCategoryExpensesTable(Document document, Map<Category, BigDecimal> categoryExpenses) {
        if (categoryExpenses != null && !categoryExpenses.isEmpty()) {
            Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
            table.addHeaderCell("Category Name").setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell("Amount Spent").setTextAlignment(TextAlignment.CENTER);

            for (Map.Entry<Category, BigDecimal> entry : categoryExpenses.entrySet()) {
                table.addCell(new Cell().add(new Paragraph(entry.getKey().getName())).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(entry.getValue().toPlainString()).setTextAlignment(TextAlignment.CENTER)));
            }

            document.add(new Paragraph("Category Expenses:").setBold().setFontSize(14));
            document.add(table);
        }

    }

    private ReportDataKey[] getOrderForReportType(ReportType reportType) {
        return switch (reportType) {
            case WEEKLY_REPORT, CUSTOM_DATE_REPORT -> new ReportDataKey[]{
                    ReportDataKey.USER,
                    ReportDataKey.DATE_INTERVAL,
                    ReportDataKey.TOTAL_EXPENSES,
                    ReportDataKey.LARGEST_EXPENSE,
                    ReportDataKey.AVERAGE_DAILY_EXPENSE,
                    ReportDataKey.TOTAL_INCOMES,
                    ReportDataKey.BUDGET_SUMMARY,
                    ReportDataKey.BIGGEST_EXPENSE,
                    ReportDataKey.SMALLEST_EXPENSE,
                    ReportDataKey.EXPENSES_LIST,
                    ReportDataKey.INCOME_LIST,
                    ReportDataKey.CATEGORY_EXPENSES
            };
            case MONTHLY_REPORT -> new ReportDataKey[]{
                    ReportDataKey.USER,
                    ReportDataKey.DATE_INTERVAL,
                    ReportDataKey.TOTAL_EXPENSES,
                    ReportDataKey.LARGEST_EXPENSE,
                    ReportDataKey.AVERAGE_WEEKLY_EXPENSE,
                    ReportDataKey.WEEK_WITH_HIGHEST_EXPENSES,
                    ReportDataKey.DAY_WITH_HIGHEST_AVERAGE_EXPENSE,
                    ReportDataKey.TOTAL_INCOMES,
                    ReportDataKey.BUDGET_SUMMARY,
                    ReportDataKey.EXPENSES_LIST,
                    ReportDataKey.INCOME_LIST,
                    ReportDataKey.CATEGORY_EXPENSES
            };
        };
    }

    private ReportEntity getReportEntity(UserReport user, ReportType reportType, DateInterval dateInterval, String outputPath) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setStartDate(dateInterval.getStartDate());
        reportEntity.setEndDate(dateInterval.getEndDate());
        reportEntity.setGeneratedDate(LocalDate.now());
        reportEntity.setType(reportType);
        reportEntity.setUser(user);
        reportEntity.setFileName(outputPath);
        return reportEntity;
    }
}
