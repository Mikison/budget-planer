package pl.sonmiike.reportsservice.report.generators;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.report.types.Report;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GenericPDFReportGenerator<T extends Report> implements ReportPDFGenerator<T> {

    private final Set<T> reports;



    @Override
    public void generatePDF(Path path) {
        generatePdf(path.toString());
    }


    private void generatePdf(String baseOutputPath) {
        for (T report : reports) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
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
                System.out.println("Report saved to: " + outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
