package nl.kristalsoftware.ddd.materialservice.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;

@Slf4j
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
        log.debug("BadRequestException: {}", message);
    }

    public BadRequestException(String message, Object... args) {
        super(format(message, args));
        log.debug(this.getMessage());
    }

}
