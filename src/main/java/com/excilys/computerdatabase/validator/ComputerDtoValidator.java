package com.excilys.computerdatabase.validator;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.excilys.computerdatabase.dto.ComputerDto;

@Component
public class ComputerDtoValidator implements Validator {

  @Override
  public boolean supports(final Class<?> clazz) {
    return ComputerDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    if (target == null) {
      errors.reject("null-object");
      return;
    }
    if (!(target instanceof ComputerDto)) {
      errors.reject("wrong-type");
      return;
    }

    final ComputerDto computerDto = (ComputerDto) target;

    if (computerDto.getId() < 0) {
      errors.rejectValue("id", "computer-id");
    }

    if (StringValidator.isEmpty(computerDto.getName())) {
      errors.rejectValue("name", "computer-name");
    }

    if (!StringValidator.isEmpty(computerDto.getIntroduced())) {
      if (!StringValidator.isDate(computerDto.getIntroduced())) {
        errors.rejectValue("introduced", "computer-date");
      }
    }

    if (!StringValidator.isEmpty(computerDto.getDiscontinued())) {
      if (!StringValidator.isDate(computerDto.getDiscontinued())) {
        errors.rejectValue("discontinued", "computer-date");
      }
    }

    if (computerDto.getCompanyId() < 0) {
      errors.rejectValue("companyId", "company-id");
    }
  }

  public static boolean isValid(final ComputerDto computerDto, final Map<String, String> errorMap) {
    if (computerDto == null) {
      return false;
    }

    if (computerDto.getId() < 0) {
      errorMap.put("eId", "Incorrect id: must be a strictly positive integer");
    }

    if (StringValidator.isEmpty(computerDto.getName())) {
      errorMap.put("eName", "Incorrect name: can't be empty or only spaces");
    }

    if (!StringValidator.isEmpty(computerDto.getIntroduced())) {
      if (!StringValidator.isDate(computerDto.getIntroduced())) {
        errorMap
            .put(
                "eDateI",
                "Incorrect introduced date: the field must be at the yyyy-MM-dd format (within a range of '1970-01-01' UTC to '2038-01-19' UTC) or shoulb be left empty");
      }
    }

    if (!StringValidator.isEmpty(computerDto.getDiscontinued())) {
      if (!StringValidator.isDate(computerDto.getDiscontinued())) {
        errorMap
            .put(
                "eDateD",
                "Incorrect discontinued date: the field must be at the yyyy-MM-dd format (within a range of '1970-01-01' UTC to '2038-01-19' UTC) or should be left empty");
      }
    }

    if (computerDto.getCompanyId() < 0) {
      errorMap.put("eCompanyId", "Incorrect companyId: must be a strictly positive integer");
    }

    //Return the computer instance if there was no error
    if (!errorMap.isEmpty()) {
      return false;
    }
    return true;
  }
}
