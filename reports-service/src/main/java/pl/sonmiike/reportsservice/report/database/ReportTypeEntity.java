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
public class ReportTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportType name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate reportDate;

    @ManyToOne
    private UserEntityReport user;
}
