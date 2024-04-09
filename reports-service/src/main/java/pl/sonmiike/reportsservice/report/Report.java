package pl.sonmiike.reportsservice.report;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    private DateInterval dateInterval;
    private BigDecimal totalExpenses;
    private BigDecimal totalIncome;
    private BigDecimal totalProfit;
    private BigDecimal totalProfitPercentage;
    private BigDecimal averageDailyExpenses;

}
