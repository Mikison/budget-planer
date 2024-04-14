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
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.income.IncomeEntity;
import pl.sonmiike.reportsservice.report.types.Report;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenericPDFReportGenerator<T extends Report> implements ReportPDFGenerator<T> {

    public static final DeviceRgb DARK_GREEN_COLOR = new DeviceRgb(50, 102, 71);
    public static final DeviceRgb DARK_RED_COLOR = new DeviceRgb(165, 0, 0);
    private final Set<T> reports;

    //    @Value("${reports.path}")
    private String basePath = "./reports/";


    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(basePath));
        } catch (IOException e) {
            throw new RuntimeException("Could not create report output directory", e);
        }
    }

    @Override
    public void generatePDF(Path path) {
        generatePdf(basePath);
    }

    private void generatePdf(String baseOutputPath) {
        for (Report report : reports) {
            String outputPath = Paths.get(baseOutputPath, report.getReportType() + "_" + report.getReportData().get("Date Interval") + "_" + report.getUser().getUsername() + ".pdf").toString();
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
                String[] order = {"User", "Date Interval", "Total Expenses", "Average Daily Expense",
                        "Total Incomes", "Budget Summary", "Biggest Expense", "Smallest Expense",
                        "Expenses List", "Income List"};

                for (String key : order) {
                    Object value = reportData.get(key);

                    if (key.equals("Budget Summary") || key.equals("Biggest Expense") || key.equals("Smallest Expense")) {

                        handleSpecialEntries(document, key, value);
                        continue;
                    }

                    if (value instanceof List) {
                        createCenteredTable(document, key, value);
                    } else {
                        document.add(createKeyValueParagraph(key, value));
                    }
                }

                document.add(new Paragraph("Report Generated on: " + new java.util.Date().toString()));
                System.out.println("Report saved to: " + outputPath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            Table table = new Table(UnitValue.createPercentArray(new float[]{0.8f, 2.5f , 0.8f, 1f}))
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
                    table.addCell(new Cell().add(new Paragraph("-"+ expense.getAmount().toPlainString()).setFontColor(DARK_RED_COLOR).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(expense.getCategory().getName()).setTextAlignment(TextAlignment.CENTER)));
                } else if (item instanceof IncomeEntity income) {
                    table.addCell(new Cell().add(new Paragraph(income.getIncomeDate().toString()).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph(income.getDescription()).setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph("+"+ income.getAmount().toPlainString()).setFontColor(DARK_GREEN_COLOR).setTextAlignment(TextAlignment.CENTER)));
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
}
