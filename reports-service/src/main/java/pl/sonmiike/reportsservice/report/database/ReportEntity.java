package pl.sonmiike.reportsservice.report.database;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.time.LocalDate;

@Table(name = "reports")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportType type;

    private String fileName;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate generatedDate;

    @ManyToOne
    private UserEntityReport user;
}
