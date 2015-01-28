package com.excilys.computerdatabase.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.validator.StringValidation;

/**
* Converter class between Computer objects and ComputerDto objects
* 
* @author Jeremy SCARELLA
*/
public class ComputerDtoConverter {

  /*
   * DATE TIME FORMATTER
   */
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
                                                                 .ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
  * Converts a ComputerDto to a Computer
  * @param computerDto : ComputerDto to convert to Computer
  * @return a Computer
  */
  public static Computer toComputer(final ComputerDto computerDto) {
    if (computerDto == null) {
      return null;
    }

    final Computer.Builder builder = Computer.builder();
    builder.id(computerDto.getId()).name(computerDto.getName());

    if (computerDto.getIntroduced() != null && !computerDto.getIntroduced().trim().isEmpty()) {
      builder.introduced(LocalDateTime.parse(computerDto.getIntroduced() + " 00:00:00",
          DATE_TIME_FORMATTER));
    }
    if (computerDto.getDiscontinued() != null && !computerDto.getDiscontinued().trim().isEmpty()) {
      builder.discontinued(LocalDateTime.parse(computerDto.getDiscontinued() + " 00:00:00",
          DATE_TIME_FORMATTER));
    }
    if (computerDto.getCompanyId() != 0) {
      builder.company(new Company(computerDto.getCompanyId(), computerDto.getCompanyName()));
    }
    return builder.build();
  }

  /**
  * Converts a ComputerDto List to a Computer List
  * @param computerDtos : ComputerDto List to convert to Computer List
  * @return a Computer List
  */
  public static List<Computer> toComputer(final List<ComputerDto> computerDtos) {
    if (computerDtos == null) {
      return null;
    }

    return computerDtos.stream().map(c -> toComputer(c)).collect(Collectors.toList());
  }

  /**
  * Converts a Computer to a ComputerDto
  * @param computer : Computer to convert to ComputerDto
  * @return a ComputerDto
  */
  public static ComputerDto toDto(final Computer computer) {
    if (computer == null) {
      return null;
    }

    final ComputerDto.Builder builder = ComputerDto.builder();
    builder.id(computer.getId()).name(computer.getName());

    if (computer.getIntroduced() != null && !computer.getIntroduced().toString().trim().isEmpty()) {
      builder.introduced(computer.getIntroduced().toString().substring(0, 10));
    }
    if (computer.getDiscontinued() != null
        && !computer.getDiscontinued().toString().trim().isEmpty()) {
      builder.discontinued(computer.getDiscontinued().toString().substring(0, 10));
    }
    if (computer.getCompany() != null) {
      builder.companyId(computer.getCompany().getId());
      builder.companyName(computer.getCompany().getName());
    } else {
      builder.companyId(0);
    }

    return builder.build();
  }

  /**
  * Converts a Computer List to a ComputerDto List
  * @param computers : Computer List to convert to ComputerDto List
  * @return a ComputerDto List
  */
  public static List<ComputerDto> toDto(final List<Computer> computers) {
    if (computers == null) {
      return null;
    }

    return computers.stream().map(c -> toDto(c)).collect(Collectors.toList());
  }

  /**
   * Validates a ComputerDto and populates an errorMap if some variables are not okay
   * @param computerDto : the ComputerDto to check
   * @param errorMap : the errorMap to populate
   * @return true if the ComputerDto is valid, false otherwise
   */
  public static boolean validate(final ComputerDto computerDto, final Map<String, String> errorMap) {
    if (computerDto == null) {
      return false;
    }

    if (computerDto.getId() < 0) {
      errorMap.put("eId", "Incorrect id : an id should be a positive integer");
    }

    if (StringValidation.isEmpty(computerDto.getName())) {
      errorMap.put("eName",
          "Incorrect name : a name can't be empty or only spaces or set to 'null'");
    }

    if (computerDto.getIntroduced() != null && !computerDto.getIntroduced().isEmpty()) {
      if (!StringValidation.isDate(computerDto.getIntroduced())) {
        errorMap
            .put(
                "eDateI",
                "Incorrect introduced date : the field must be at the yyyy-MM-dd format (within a range of '1970-01-01' UTC to '2038-01-19' UTC) or shoulb be left empty");
      }
    }

    if (computerDto.getDiscontinued() != null && !computerDto.getDiscontinued().isEmpty()) {
      if (!StringValidation.isDate(computerDto.getDiscontinued())) {
        errorMap
            .put(
                "eDateD",
                "Incorrect discontinued date : the field must be at the yyyy-MM-dd format (within a range of '1970-01-01' UTC to '2038-01-19' UTC) or should be left empty");
      }
    }

    if (computerDto.getCompanyId() < 0) {
      errorMap.put("eCompanyId", "Incorrect CompanyId: an id should be a positive integer");
    }

    //Return the computer instance if there was no error
    if (!errorMap.isEmpty()) {
      return false;
    }
    return true;
  }
}