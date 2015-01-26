package com.excilys.computerdatabase.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.validator.DtoValidation;

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
    if (!DtoValidation.isValidComputerDto(computerDto)) {
      return null;
    }

    final Computer.Builder builder = Computer.builder();
    builder.id(computerDto.getId()).name(computerDto.getName());

    if (computerDto.getIntroduced() != null) {
      builder.introduced(LocalDateTime.parse(computerDto.getIntroduced() + " 00:00:00",
          DATE_TIME_FORMATTER));
    }
    if (computerDto.getDiscontinued() != null) {
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
    final List<Computer> computers = computerDtos.stream().map(computerDto -> {
      final Computer computer = ComputerDtoConverter.toComputer(computerDto);
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
    final ComputerDto.Builder builder = ComputerDto.builder();
    builder.id(computer.getId()).name(computer.getName());

    if (computer.getIntroduced() != null) {
      builder.introduced(computer.getIntroduced().toString().substring(0, 10));
    }
    if (computer.getDiscontinued() != null) {
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