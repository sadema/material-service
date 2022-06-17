package nl.kristalsoftware.ddd.materialservice.rest.material.view;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialView;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialViewService;
import nl.kristalsoftware.ddd.materialservice.rest.material.MaterialResponseBody;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/material", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Material view", description = "Endpoints for retrieving material")
public class MaterialViewController {

    private final MaterialViewService materialViewService;

    @GetMapping
    @Operation(
            summary = "Get all material",
            description = "Get all material",
            tags = "Material view",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
            }
    )
    public List<MaterialResponseBody> getAllMaterial() {
        List<MaterialView> materialList = materialViewService.getAllMaterial();
        return materialList.stream()
                .map(it -> MaterialResponseBody.of(it))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{materialReference}")
    @Operation(
            summary = "Get material by reference",
            description = "Get material by reference",
            tags = "Material view",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
            }
    )
    public MaterialResponseBody getMaterialByReference(@PathVariable String materialReference) {
          return MaterialResponseBody.of(
                  materialViewService.getMaterialByReference(MaterialReference.of(UUID.fromString(materialReference)))
          );
    }
}
