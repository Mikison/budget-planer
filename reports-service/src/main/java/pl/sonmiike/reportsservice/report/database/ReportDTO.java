package pl.sonmiike.reportsservice.report.database;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportDTO {

    private Long id;
    private ReportType type;
    private String fileName;
    private String startDate;
    private String endDate;
    private String generatedDate;
    private Long userId;
}
