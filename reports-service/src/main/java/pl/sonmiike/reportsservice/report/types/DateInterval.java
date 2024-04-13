package pl.sonmiike.reportsservice.report.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateInterval {
    private LocalDate startDate;
    private LocalDate endDate;
}
