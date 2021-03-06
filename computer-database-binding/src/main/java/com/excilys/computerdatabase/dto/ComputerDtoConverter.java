package com.excilys.computerdatabase.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.validator.StringValidator;

/**
* Converter class between Computer objects and ComputerDto objects
* 
* @author Jeremy SCARELLA
*/
public class ComputerDtoConverter {

  /**
  * Converts a ComputerDto to a Computer
  * @param computerDto : ComputerDto to convert to Computer
  * @return a Computer
  */
  public static Computer toComputer(final ComputerDto computerDto, final String dateFormat) {
    if (computerDto == null) {
      return null;
    }

    final Computer.Builder builder = Computer.builder();
    if (computerDto.getId() >= 0) {
      builder.id(computerDto.getId());
    }
    if (StringValidator.isValidName(computerDto.getName())) {
      builder.name(computerDto.getName());
    }
    if (StringValidator.isDate(computerDto.getIntroduced(), dateFormat)) {
      builder.introduced(LocalDate.parse(computerDto.getIntroduced(),
          DateTimeFormatter.ofPattern(dateFormat)));
    }
    if (StringValidator.isDate(computerDto.getDiscontinued(), dateFormat)) {
      builder.discontinued(LocalDate.parse(computerDto.getDiscontinued(),
          DateTimeFormatter.ofPattern(dateFormat)));
    }
    if (computerDto.getCompanyId() > 0) {
      builder.company(new Company(computerDto.getCompanyId(), computerDto.getCompanyName()));
    }
    return builder.build();
  }

  /**
  * Converts a ComputerDto List to a Computer List
  * @param computerDtos : ComputerDto List to convert to Computer List
  * @return a Computer List
  */
  public static List<Computer> toComputer(final List<ComputerDto> computerDtos,
      final String dateFormat) {
    if (computerDtos == null) {
      return null;
    }

    final List<Computer> computers = computerDtos.stream().map(computerDto -> {
      final Computer computer = ComputerDtoConverter.toComputer(computerDto, dateFormat);
      if (computer != null) {
        return computer;
      }
      return null;
    }).collect(Collectors.toList());
    return computers;
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
    if (computer.getId() != null && computer.getId() >= 0) {
      builder.id(computer.getId());
    }
    if (computer.getName() != null && StringValidator.isValidName(computer.getName())) {
      builder.name(computer.getName());
    }
    if (computer.getIntroduced() != null
        && !StringValidator.isEmpty(computer.getIntroduced().toString())) {
      builder.introduced(computer.getIntroduced().toString());
    }
    if (computer.getDiscontinued() != null
        && !StringValidator.isEmpty(computer.getDiscontinued().toString())) {
      builder.discontinued(computer.getDiscontinued().toString());
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

    final List<ComputerDto> computerDtos = computers.stream().map(computer -> {
      final ComputerDto computerDto = ComputerDtoConverter.toDto(computer);
      if (computerDto != null) {
        return computerDto;
      }
      return null;
    }).collect(Collectors.toList());
    return computerDtos;
  }
}