package pl.sonmiike.reportsservice.income;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sonmiike.reportsservice.user.UserReport;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "income")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate incomeDate;

    private String name;

    private String description;
    private BigDecimal amount;

    @ManyToOne
    private UserReport user;


    public Income(BigDecimal amount) {
        this.amount = amount;
    }

    public Income(BigDecimal amount, LocalDate incomeDate) {
        this.amount = amount;
        this.incomeDate = incomeDate;
    }
}