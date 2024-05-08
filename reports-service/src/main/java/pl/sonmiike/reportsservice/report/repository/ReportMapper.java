package pl.sonmiike.reportsservice.report.repository;

import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public ReportDTO toDTO(ReportEntity entity) {
        return ReportDTO.builder()
                .id(entity.getId())
                .type(entity.getType())
                .fileName(entity.getFileName())
                .startDate(entity.getStartDate().toString())
                .endDate(entity.getEndDate().toString())
                .generatedDate(entity.getGeneratedDate().toString())
                .userId(entity.getUser().getUserId())
                .build();
    }
}
