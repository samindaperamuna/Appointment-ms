package ms.asp.appointment.model.validators.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ms.asp.appointment.model.validators.PeriodValidator;

@Target({ ElementType.PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = PeriodValidator.class)
@Documented
public @interface ValidatePeriod {

    String message() default "{period.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
