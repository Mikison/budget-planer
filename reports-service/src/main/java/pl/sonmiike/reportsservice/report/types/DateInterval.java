package pl.sonmiike.reportsservice.report.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DateInterval {
    private LocalDate startDate;
    private LocalDate endDate;
}
