package pl.sonmiike.reportsservice.report.repository;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {

    private Long id;
    private ReportType type;
    private String fileName;
    private String startDate;
    private String endDate;
    private String generatedDate;
    private Long userId;
}
