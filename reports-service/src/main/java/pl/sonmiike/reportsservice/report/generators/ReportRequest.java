package pl.sonmiike.reportsservice.report.generators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.sonmiike.reportsservice.report.repository.ReportType;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
    private Long userId;
    private ReportType reportType;
    private Map<String, Object> parameters;

}
