package pl.sonmiike.reportsservice.income;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "income")
public class IncomeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate incomeDate;

    private String name;

    private String description;
    private BigDecimal amount;

    @ManyToOne
    private UserEntityReport user;


}