package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class Validator {
    public <T> void isValid(@Valid T commandData) {
    }
}
