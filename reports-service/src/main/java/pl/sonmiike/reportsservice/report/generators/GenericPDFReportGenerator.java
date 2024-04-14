package pl.sonmiike.reportsservice.report.generators;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.income.IncomeEntity;
import pl.sonmiike.reportsservice.report.types.MonthlyReport;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenericPDFReportGenerator<T extends Report> implements ReportPDFGenerator<T> {

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
        generatePdf(path.toString());
    }


    private void generatePdf(String baseOutputPath) {
        for (Report report : reports) {
            String outputPath = Paths.get(baseOutputPath, report.getUser().getUsername() + ".pdf").toString();
            try (PdfWriter writer = new PdfWriter(outputPath);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {

                // Title
                document.add(new Paragraph("Report for " + report.getUser().getUsername())
                        .setBold()
                        .setFontSize(14));

                // Income List Table
                if (report instanceof MonthlyReport || report instanceof WeeklyReport) {
                    Table incomeTable = new Table(UnitValue.createPercentArray(new float[]{3, 3, 3}));
                    incomeTable.setWidth(UnitValue.createPercentValue(100));
                    incomeTable.addHeaderCell("Date").addHeaderCell("Description").addHeaderCell("Amount");

//                    for (IncomeEntity income : ) {
//                        incomeTable.addCell(income.getDate().toString());
//                        incomeTable.addCell(income.getDescription());
//                        incomeTable.addCell(income.getAmount().toString());
//                    }
                    document.add(new Paragraph("Incomes:"));
                    document.add(incomeTable);
                }

                // Expense List Table
                Table expenseTable = new Table(UnitValue.createPercentArray(new float[]{3, 3, 3}));
                expenseTable.setWidth(UnitValue.createPercentValue(100));
                expenseTable.addHeaderCell("Date").addHeaderCell("Description").addHeaderCell("Amount");
//
//                for (ExpenseEntity expense : report.getExpensesList()) {
//                    expenseTable.addCell(expense.getDate().toString());
//                    expenseTable.addCell(expense.getDescription());
//                    expenseTable.addCell(expense.getAmount().toString());
//                }
                document.add(new Paragraph("Expenses:"));
                document.add(expenseTable);

                // Footer and metadata
                document.add(new Paragraph("Report Generated on: " + new java.util.Date().toString()));
                System.out.println("Report saved to: " + outputPath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
