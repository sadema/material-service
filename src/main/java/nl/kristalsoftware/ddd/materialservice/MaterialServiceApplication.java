package nl.kristalsoftware.ddd.materialservice;

import lombok.extern.slf4j.Slf4j;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.MaterialCommandService;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.AggregateNotFoundException;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

@Slf4j
@SpringBootApplication
public class MaterialServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaterialServiceApplication.class, args);
    }

    @Profile("!Test")
    @Bean
    CommandLineRunner runMaterialCommands(MaterialCommandService materialCommandService) {
        return args -> {
            TicketReference ticketReference = TicketReference.of(UUID.randomUUID());
            TicketReference ticketReference_2 = TicketReference.of(UUID.randomUUID());
            try {
                MaterialReference materialReference = materialCommandService.registerNewMaterialType(
                        MaterialDescription.of("Schroefje"),
                        MaterialQuantity.of(15)
                );
                materialCommandService.addMaterialToStock(
                        materialReference,
                        MaterialQuantity.of(8));
                materialCommandService.reserveMaterialForTicket(
                        materialReference,
                        MaterialQuantity.of(3),
                        ticketReference
                );
                materialCommandService.useMaterialForTicket(
                        materialReference,
                        MaterialQuantity.of(2),
                        ticketReference
                );
                materialCommandService.reserveMaterialForTicket(
                        materialReference,
                        MaterialQuantity.of(4),
                        ticketReference
                );
                materialCommandService.useMaterialForTicket(
                        materialReference,
                        MaterialQuantity.of(8),
                        ticketReference_2
                );
            } catch (AggregateNotFoundException anf) {
                log.info(anf.getMessage());
            } catch (ConstraintViolationException cve) {
                cve.getConstraintViolations().stream().forEach(it -> log.info(it.getMessage()));
            }
        };
    }
}
