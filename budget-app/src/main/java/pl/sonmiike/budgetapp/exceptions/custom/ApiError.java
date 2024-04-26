package pl.sonmiike.budgetapp.exceptions.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private String path;
    private String message;
    private int statusCode;
    private LocalDateTime localDateTime;

    public ApiError(String path, String message, int statusCode) {
        this.path = path;
        this.message = message;
        this.statusCode = statusCode;
    }
}
