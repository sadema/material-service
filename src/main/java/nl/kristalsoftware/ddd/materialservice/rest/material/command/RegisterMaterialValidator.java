package nl.kristalsoftware.ddd.materialservice.rest.material.command;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.RegisterMaterial;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RegisterMaterialValidator implements ConstraintValidator<RegisterMaterialConstraint, RegisterMaterial> {

    @Override
    public boolean isValid(RegisterMaterial registerMaterial, ConstraintValidatorContext constraintValidatorContext) {
        return isValidDescription(registerMaterial.getMaterialDescription(), constraintValidatorContext) &&
                isValidInStock(registerMaterial.getMaterialQuantity(), constraintValidatorContext);
    }

    private boolean isValidDescription(MaterialDescription materialDescription, ConstraintValidatorContext constraintValidatorContext) {
        if (materialDescription.getValue().length() < 3) {
            addErrorMessage(constraintValidatorContext, "Description lenght smaller then 3");
            return false;
        }
        return true;
    }

    private boolean isValidInStock(MaterialQuantity materialInStock, ConstraintValidatorContext constraintValidatorContext) {
        if (materialInStock.getValue() < 5) {
            addErrorMessage(constraintValidatorContext, "Minimal amount is 5");
            return false;
        }
        return true;
    }

    private void addErrorMessage(ConstraintValidatorContext constraintValidatorContext, String error) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext
                .buildConstraintViolationWithTemplate(error).addConstraintViolation();
    }

}
