package pl.sonmiike.reportsservice.report.generators;

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
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.cateogry.CategoryEntity;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.income.IncomeEntity;
import pl.sonmiike.reportsservice.report.database.ReportEntity;
import pl.sonmiike.reportsservice.report.database.ReportEntityRepository;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Setter
@Service
@RequiredArgsConstructor
public class ReportGenerator<T extends Report> implements ReportPDFGenerator<T> {

    public static final DeviceRgb DARK_GREEN_COLOR = new DeviceRgb(50, 102, 71);
    public static final DeviceRgb DARK_RED_COLOR = new DeviceRgb(220, 20, 60);
    private final ReportEntityRepository reportEntityRepository;



    @Value("${reports.folder.root}")
    private String basePath;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(basePath));
        } catch (IOException e) {
            throw new RuntimeException("Could not create report output directory", e);
        }
    }

    @Override
    public void generatePDF(T report) {
        generatePdf(report,basePath);
    }

    private void generatePdf(T report, String baseOutputPath) {
        if (report == null || report.getUser() == null) {
            System.out.println("Report or user is null, aborting PDF generation.");
            return;
        }
        String username = report.getUser().getUsername();
        String dateInterval = report.getReportData().get("Date Interval").toString();
        String fileName = report.getReportType() + "_" + dateInterval + "_" + username + ".pdf";
        String outputPath = Paths.get(baseOutputPath, fileName).toString();
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

            Map<String, Object> reportData = report.getReportData();
            String[] order = getOrderForReportType(report.getReportType());

            for (String key : order) {
                Object value = reportData.get(key);
                if (value == null) continue;

                if (key.equals("Budget Summary") || key.equals("Biggest Expense") || key.equals("Smallest Expense")) {

                    handleSpecialEntries(document, key, value);
                    continue;
                }
                if (key.equals("Category Expenses")) {
                    createCategoryExpensesTable(document, (Map<CategoryEntity, BigDecimal>) value);
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
            DateInterval dateIntervalObj = getDateIntervalfromString(dateInterval);
            if (!reportEntityRepository.existsByStartDateAndEndDate(dateIntervalObj.getStartDate(), dateIntervalObj.getEndDate())) {
                reportEntityRepository.save(getReportEntity(report.getUser(), report.getReportType(), dateIntervalObj , fileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private DateInterval getDateIntervalfromString(String dateInterval) {
        String[] dates = dateInterval.split("-");
        String startDate = dates[0] + "-" + dates[1] + "-" + dates[2];
        String endDate = dates[3] + "-" + dates[4] + "-" + dates[5];
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return new DateInterval(start, end);
    }


    private void handleSpecialEntries(Document document, String key, Object value) {
        if (key.equals("Budget Summary")) {
            BigDecimal summary = new BigDecimal(value.toString());
            Paragraph summaryParagraph = new Paragraph(key + ": ").setBold();
            String summaryText = summary.compareTo(BigDecimal.ZERO) > 0 ? "+" + summary.toPlainString() : summary.toPlainString();
            summaryParagraph.add(new Text(summaryText)
                    .setFontColor(summary.compareTo(BigDecimal.ZERO) > 0 ? DARK_GREEN_COLOR : DARK_RED_COLOR));
            document.add(summaryParagraph);
        } else {
            ExpenseEntity expense = (ExpenseEntity) value;
            String expenseText = String.format("%s on %s spent %s",
                    expense.getDescription(),
                    expense.getDate().toString(),
                    expense.getAmount().toPlainString());
            document.add(createKeyValueParagraph(key, expenseText));
        }
    }

    private void createCenteredTable(Document document, String key, Object value) {
        List<?> list = (List<?>) value;
        if (!list.isEmpty() && (list.get(0) instanceof IncomeEntity || list.get(0) instanceof ExpenseEntity)) {
            Table table = new Table(UnitValue.createPercentArray(new float[]{0.8f, 2.5f, 0.8f, 1f}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell(new Cell().add(new Paragraph("Date").setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(new Paragraph("Description").setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(new Paragraph("Amount").setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(new Paragraph("Category").setTextAlignment(TextAlignment.CENTER)));

            for (Object item : list) {
                if (item instanceof ExpenseEntity expense) {
                    table.addCell(new Cell().add(new Paragraph(expense.getDate().toString()).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(expense.getDescription()).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph("-" + expense.getAmount().toPlainString()).setFontColor(DARK_RED_COLOR).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(expense.getCategory().getName()).setTextAlignment(TextAlignment.CENTER)));
                } else if (item instanceof IncomeEntity income) {
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

    private Paragraph createKeyValueParagraph(String key, Object value) {
        Paragraph p = new Paragraph();
        p.add(new Text(key + ": ").setBold());
        p.add(new Text(value.toString()));

        return p;
    }

    private void createCategoryExpensesTable(Document document, Map<CategoryEntity, BigDecimal> categoryExpenses) {
        if (categoryExpenses != null && !categoryExpenses.isEmpty()) {
            Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
            table.addHeaderCell("Category Name").setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell("Amount Spent").setTextAlignment(TextAlignment.CENTER);

            for (Map.Entry<CategoryEntity, BigDecimal> entry : categoryExpenses.entrySet()) {
                table.addCell(new Cell().add(new Paragraph(entry.getKey().getName())).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(entry.getValue().toPlainString()).setTextAlignment(TextAlignment.CENTER)));
            }

            document.add(new Paragraph("Category Expenses:").setBold().setFontSize(14));
            document.add(table);
        }

    }

    private String[] getOrderForReportType(ReportType reportType) {
        return switch (reportType) {
            case WEEKLY_REPORT -> new String[]{"User", "Date Interval", "Total Expenses", "Average Daily Expense",
                    "Total Incomes", "Budget Summary", "Biggest Expense", "Smallest Expense",
                    "Expenses List", "Income List", "Category Expenses"};
            case MONTHLY_REPORT ->
                    new String[]{"User", "Date Interval", "Total Expenses", "Largest Expense", "Average Weekly Expense", "Week With Highest Expenses", "Day With Highest Average Expense", "Total Incomes", "Budget Summary", "Expenses List", "Income List", "Category Expenses"};
            default -> new String[]{};
        };
    }

    private ReportEntity getReportEntity(UserEntityReport user, ReportType reportType, DateInterval dateInterval, String outputPath) {
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
