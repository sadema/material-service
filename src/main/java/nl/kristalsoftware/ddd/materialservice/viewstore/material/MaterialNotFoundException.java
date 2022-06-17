package nl.kristalsoftware.ddd.materialservice.viewstore.material;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@NoArgsConstructor
public class MaterialNotFoundException extends RuntimeException {

    public MaterialNotFoundException(String message) {
        super(message);
    }

}
