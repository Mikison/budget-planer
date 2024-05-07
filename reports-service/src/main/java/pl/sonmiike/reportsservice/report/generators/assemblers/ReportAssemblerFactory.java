package pl.sonmiike.reportsservice.report.generators.assemblers;

import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.report.database.ReportType;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReportAssemblerFactory {

    private final Map<ReportType, ReportAssembler> assemblers;

    public ReportAssemblerFactory(WeeklyReportAssembler weeklyReportAssembler,
                                  MonthlyReportAssembler monthlyReportAssembler,
                                  CustomDateReportAssembler customDateReportAssembler) {
        assemblers = new HashMap<>();
        assemblers.put(ReportType.WEEKLY_REPORT, weeklyReportAssembler);
        assemblers.put(ReportType.MONTHLY_REPORT, monthlyReportAssembler);
        assemblers.put(ReportType.CUSTOM_DATE_REPORT, customDateReportAssembler);
    }

    public ReportAssembler getAssembler(ReportType reportType) {
        ReportAssembler assembler = assemblers.get(reportType);
        if (assembler == null) {
            throw new IllegalArgumentException("No assembler available for type: " + reportType);
        }
        return assembler;
    }
}
