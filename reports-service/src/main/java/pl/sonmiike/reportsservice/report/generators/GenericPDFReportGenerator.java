package pl.sonmiike.reportsservice.report.generators;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.expense.ExpenseEntityService;
import pl.sonmiike.reportsservice.income.IncomeService;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.user.UserEntityReport;
import pl.sonmiike.reportsservice.user.refrshtoken.UserEntityService;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GenericPDFReportGenerator<T extends Report> implements ReportPDFGenerator<T> {

    private final List<T> reports;

    private final ExpenseEntityService expenseService;
    private final IncomeService incomeService;
    private final UserEntityService userEntityService;


    @Override
    public Set<T> generatePDF() {
        return generatePdf("123");
    }


    public void generatePdf(String baseOutputPath) {
        for (T report : reports) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                    contentStream.setLeading(14.5f);
                    contentStream.newLineAtOffset(50, 750);

                    Map<String, String> reportData = report.getReportData();
                    for (Map.Entry<String, String> entry : reportData.entrySet()) {
                        contentStream.showText(entry.getKey() + ": " + entry.getValue());
                        contentStream.newLine();
                    }
                    contentStream.endText();
                }

                String outputPath = baseOutputPath + "_" + report.getUser().getName() + ".pdf";
                document.save(outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
