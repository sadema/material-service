package nl.kristalsoftware.ddd.materialservice.rest.material.command;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.MaterialCommandService;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.AggregateNotFoundException;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;
import nl.kristalsoftware.ddd.materialservice.rest.material.command.requestbody.AddMaterialToStockRequestBody;
import nl.kristalsoftware.ddd.materialservice.rest.material.command.requestbody.RegisterMaterialRequestBody;
import nl.kristalsoftware.ddd.materialservice.rest.material.command.requestbody.ReserveMaterialForTicketRequestBody;
import nl.kristalsoftware.ddd.materialservice.rest.material.command.requestbody.SendMaterialRetourForTicketRequestBody;
import nl.kristalsoftware.ddd.materialservice.rest.material.command.requestbody.UseMaterialForTicketRequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/material", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Material commands", description = "Endpoints for managing material")
public class MaterialCommandController {

    private final MaterialCommandService materialCommandService;

    @PostMapping
    @Operation(
            summary = "Register material",
            description = "Register new material type",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
            }
    )
    public void registerMaterial(@Valid @RequestBody RegisterMaterialRequestBody registerMaterialRequestBody, HttpServletResponse response) {
        try {
            MaterialReference materialReference = materialCommandService.registerNewMaterialType(
                    MaterialDescription.of(registerMaterialRequestBody.getDescription()),
                    MaterialQuantity.of(registerMaterialRequestBody.getInStock())
            );
            response.setHeader("Location", ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/material/" + materialReference.getValue()).toUriString());
            response.setStatus(HttpStatus.CREATED.value());
        } catch (ConstraintViolationException e) {
            log.info(e.getLocalizedMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
    }

    @PutMapping(value = "/{materialReference}")
    @Operation(
            summary = "Add material to stock",
            description = "Add material to stock",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Not Found", responseCode = "404")
            }
    )
    public ResponseEntity<Void> addMaterialToStock(
            @PathVariable String materialReference,
            @Valid @RequestBody AddMaterialToStockRequestBody addMaterialToStockRequestBody
    ) {
        try {
            materialCommandService.addMaterialToStock(
                    MaterialReference.of(UUID.fromString(materialReference)),
                    MaterialQuantity.of(addMaterialToStockRequestBody.getMaterialQuantity())
            );
        } catch (AggregateNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{materialReference}/reserve-for-ticket")
    @Operation(
            summary = "Reserve material",
            description = "Reserve material for ticket",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            }
    )
    public ResponseEntity<Void> reserveMaterialForTicket(
            @PathVariable String materialReference,
            @Valid @RequestBody ReserveMaterialForTicketRequestBody reserveMaterialForTicketRequestBody
    ) {
        try {
            if (!materialCommandService.reserveMaterialForTicket(
                    MaterialReference.of(UUID.fromString(materialReference)),
                    MaterialQuantity.of(reserveMaterialForTicketRequestBody.getMaterialQuantity()),
                    TicketReference.of(reserveMaterialForTicketRequestBody.getTicketReference())
            )) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (AggregateNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{materialReference}/use-for-ticket")
    @Operation(
            summary = "Use material",
            description = "Use material for ticket",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            }
    )
    public ResponseEntity<Void> useMaterialForTicket(
            @PathVariable String materialReference,
            @Valid @RequestBody UseMaterialForTicketRequestBody useMaterialForTicketRequestBody
    ) {
        try {
            if (!materialCommandService.useMaterialForTicket(
                    MaterialReference.of(UUID.fromString(materialReference)),
                    MaterialQuantity.of(useMaterialForTicketRequestBody.getMaterialQuantity()),
                    TicketReference.of(useMaterialForTicketRequestBody.getTicketReference())
            )) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (AggregateNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{materialReference}/send-retour-for-ticket")
    @Operation(
            summary = "Send material retour",
            description = "Send material retour for ticket",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            }
    )
    public ResponseEntity<Void> sendMaterialRetourForTicket(
            @PathVariable String materialReference,
            @Valid @RequestBody SendMaterialRetourForTicketRequestBody sendMaterialRetourForTicketRequestBody
    ) {
        try {
            materialCommandService.sendMaterialRetourForTicket(
                    MaterialReference.of(UUID.fromString(materialReference)),
                    MaterialQuantity.of(sendMaterialRetourForTicketRequestBody.getMaterialQuantity()),
                    TicketReference.of(sendMaterialRetourForTicketRequestBody.getTicketReference())
            );
        } catch (AggregateNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
