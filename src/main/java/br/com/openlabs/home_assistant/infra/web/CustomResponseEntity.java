package br.com.openlabs.home_assistant.infra.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CustomResponseEntity<T> {

    private HttpStatus status;
    private String message;
    private int code;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private T body;

    public CustomResponseEntity(HttpStatus status, String message, int code, T body) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.timestamp = LocalDateTime.now();
        this.body = body;
    }
}