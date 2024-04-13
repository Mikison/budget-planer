package pl.sonmiike.reportsservice.report.generators;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.report.types.Report;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GenericPDFReportGenerator<T extends Report> implements ReportPDFGenerator<T> {

    private final Set<T> reports;



    @Override
    public void generatePDF() {
        generatePdf("123");
    }


    private void generatePdf(String baseOutputPath) {
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
