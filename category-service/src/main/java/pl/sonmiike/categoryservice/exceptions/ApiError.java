package pl.sonmiike.categoryservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private String path;
    private List<String> message;
    private int statusCode;
    private LocalDateTime localDateTime;


    public ApiError(String path, List<String> message, int statusCode) {
        this.path = path;
        this.message = message;
        this.statusCode = statusCode;
    }
}
