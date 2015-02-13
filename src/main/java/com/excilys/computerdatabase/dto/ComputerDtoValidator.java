package com.excilys.computerdatabase.dto;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.excilys.computerdatabase.validator.StringValidator;

@Component
public class ComputerDtoValidator implements Validator {

  @Override
  public boolean supports(final Class<?> clazz) {
    return ComputerDto.class.isAssignableFrom(clazz);
  }

  /*
   * MessageSourceAccessor
   */
  private MessageSourceAccessor messageSourceAccessor;

  @Autowired
  public void setMessageSource(final MessageSource messageSource) {
    this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    if (target == null) {
      errors.reject("error-null-object");
      return;
    }
    if (!(target instanceof ComputerDto)) {
      errors.reject("error-wrong-type");
      return;
    }

    final ComputerDto computerDto = (ComputerDto) target;
    final String dateFormat = messageSourceAccessor.getMessage("date-format");

    if (computerDto.getId() < 0) {
      errors.rejectValue("id", "error-computer-id");
    }

    if (StringValidator.isEmpty(computerDto.getName())) {
      errors.rejectValue("name", "error-computer-name");
    }

    if (!StringValidator.isEmpty(computerDto.getIntroduced())) {
      if (!StringValidator.isDate(computerDto.getIntroduced(), dateFormat)) {
        errors.rejectValue("introduced", "error-computer-date");
      }
    }

    if (!StringValidator.isEmpty(computerDto.getDiscontinued())) {
      if (!StringValidator.isDate(computerDto.getDiscontinued(), dateFormat)) {
        errors.rejectValue("discontinued", "error-computer-date");
      }
    }

    if (computerDto.getCompanyId() < 0) {
      errors.rejectValue("companyId", "error-company-id");
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
      if (!StringValidator.isDate(computerDto.getIntroduced(), "yyyy-MM-dd")) {
        errorMap
            .put(
                "eDateI",
                "Incorrect introduced date: the field must be at the yyyy-MM-dd format (within a range of '1970-01-01' UTC to '2038-01-19' UTC) or shoulb be left empty");
      }
    }

    if (!StringValidator.isEmpty(computerDto.getDiscontinued())) {
      if (!StringValidator.isDate(computerDto.getDiscontinued(), "yyyy-MM-dd")) {
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
