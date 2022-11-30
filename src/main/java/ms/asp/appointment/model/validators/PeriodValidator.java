package ms.asp.appointment.model.validators;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ms.asp.appointment.model.PeriodModel;
import ms.asp.appointment.model.validators.annotations.ValidatePeriod;

public class PeriodValidator implements ConstraintValidator<ValidatePeriod, PeriodModel> {

    @Override
    public void initialize(ValidatePeriod constraintAnnotation) {
    }

    @Override
    public boolean isValid(PeriodModel value, ConstraintValidatorContext context) {
	if (Objects.nonNull(value) && Objects.nonNull(value.getStart()) == Objects.nonNull(value.getEnd())
		&& Objects.nonNull(value.getStart())) {

	    return value.getStart().isBefore(value.getEnd());
	}

	return false;
    }
}
