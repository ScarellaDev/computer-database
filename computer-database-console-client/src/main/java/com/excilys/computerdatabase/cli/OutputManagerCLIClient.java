package com.excilys.computerdatabase.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.ws.rs.core.Response;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.dto.ComputerDtoValidator;
import com.excilys.computerdatabase.validator.StringValidator;
import com.excilys.computerdatabase.webservice.ICompanyWebService;
import com.excilys.computerdatabase.webservice.IComputerWebService;

/**
* Class managing the outputs of the CLI.
*
* @author Jeremy SCARELLA
*/
public class OutputManagerCLIClient {

  /*
   * Scanner sc : get the user input
   * String userInput : save the user input
   */
  private static Scanner      sc;
  private static String       userInput;

  /**
  * Instance of ComputerService for the access to the database
  */
  private IComputerWebService computerWebService;

  /**
  * Instance of CompanyService for the access to the database
  */
  private ICompanyWebService  companyWebService;

  public OutputManagerCLIClient(final ClassPathXmlApplicationContext context) {
    sc = new Scanner(System.in);
    computerWebService = (IComputerWebService) context.getBean(IComputerWebService.class);
    companyWebService = (ICompanyWebService) context.getBean(ICompanyWebService.class);
  }

  /**
   * Display the main menu
   */
  public void showMenu() {
    final StringBuffer menu = new StringBuffer("* * * * * MENU * * * * *\r\n");
    menu.append("0. Show menu (cmd: 'menu')\r\n");
    menu.append("1. Show list of all computers (cmd: 'ls computers')\r\n");
    menu.append("2. Show list of all companies (cmd: 'ls companies')\r\n");
    menu.append("3. Show the details of one computer (cmd: 'show id')\r\n");
    menu.append("4. Add a computer (cmd: 'add name introduced discontinued companyId')\r\n");
    menu.append("5. Update a computer (cmd: 'update id name introduced discontinued companyId')\r\n");
    menu.append("6. Remove a computer (cmd: 'remove computer id')\r\n");
    menu.append("7. Remove a company (cmd: 'remove company id')\r\n");
    menu.append("\r\nINFO: to display the help concerning a command, just type 'help commandNumber' or 'help commandName'\r\n");
    menu.append("INFO2: in order to quit the program, just type 'q' or 'quit' or 'exit'\r\n");
    System.out.println(menu);
  }

  /**
   * Display a page of twenty computers and allow navigation through pages.
   */
  //  public void showComputerPage() {
  //    System.out.println("-> You entered the computer list navigation:");
  //    //Create a Pageable instance
  //    Pageable pageable = new PageRequest(0, 20);
  //    //Create a Page instance and get the first Page of computers from the database
  //    Page<Computer> page = computerWebService.getPagedList("", pageable);
  //
  //    if (page.hasContent()) {
  //      //Show the content of the page
  //      System.out.println("Page " + (page.getNumber() + 1) + " out of " + (page.getTotalPages())
  //          + " / Total number of computers : " + page.getTotalElements());
  //      page.getContent().forEach(System.out::println);
  //
  //      while (true) {
  //        if (page.isFirst()) {
  //          System.out
  //              .println("\r\nType 'return' or 'r' to exit\r\nor 'next' or 'n' or press enter to show the next page\r\nor 'last' or 'l' to show the last page\r\nor the number of the page you want to display");
  //        } else if (page.isLast()) {
  //          System.out
  //              .println("\r\nType 'return' or 'r' to exit\r\nor 'previous' or 'p' to show the previous page\r\nor 'first' or 'f' to show the first page\r\nor the number of the page you want to display");
  //        } else {
  //          System.out
  //              .println("\r\nType 'return' or 'r' to exit\r\nor 'previous' or 'p' to show the previous page, 'next' or 'n' or press enter to show the next page\r\nor 'first' or 'f' to show the first page or 'last' or 'l' to show the last page\r\nor the number of the page you want to display");
  //        }
  //
  //        userInput = null;
  //
  //        userInput = sc.nextLine().trim().toLowerCase();
  //        if (StringValidator.isEmpty(userInput)) {
  //          if (page.hasNext()) {
  //            page = computerWebService.getPagedList("", page.nextPageable());
  //            System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                + (page.getTotalPages()) + " / Total number of computers : "
  //                + page.getTotalElements());
  //            page.getContent().forEach(System.out::println);
  //          } else {
  //            System.out.println("Warning: last page reached!");
  //          }
  //        } else if (StringValidator.isPositiveInt(userInput)) {
  //          Integer number = new Integer(userInput);
  //          if (number < 1 || number > page.getTotalPages()) {
  //            System.out.println("Non valid page number.");
  //          } else {
  //            number--;
  //            pageable = new PageRequest(0, 20);
  //            page = computerWebService.getPagedList("", pageable);
  //            while (page.getNumber() != number) {
  //              page = computerWebService.getPagedList("", page.nextPageable());
  //            }
  //            System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                + (page.getTotalPages()) + " / Total number of computers : "
  //                + page.getTotalElements());
  //            page.getContent().forEach(System.out::println);
  //          }
  //        } else {
  //          switch (userInput) {
  //            case "r":
  //              System.out.println("-> back to main menu");
  //              return;
  //            case "return":
  //              System.out.println("-> back to main menu");
  //              return;
  //            case "n":
  //              if (page.hasNext()) {
  //                page = computerWebService.getPagedList("", page.nextPageable());
  //                System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                    + (page.getTotalPages()) + " / Total number of computers : "
  //                    + page.getTotalElements());
  //                page.getContent().forEach(System.out::println);
  //              } else {
  //                System.out.println("Warning: last page reached!");
  //              }
  //              break;
  //            case "next":
  //              if (page.hasNext()) {
  //                page = computerWebService.getPagedList("", page.nextPageable());
  //                System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                    + (page.getTotalPages()) + " / Total number of computers : "
  //                    + page.getTotalElements());
  //                page.getContent().forEach(System.out::println);
  //              } else {
  //                System.out.println("Warning: last page reached!");
  //              }
  //              break;
  //            case "p":
  //              if (page.hasPrevious()) {
  //                page = computerWebService.getPagedList("", page.previousPageable());
  //                System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                    + (page.getTotalPages()) + " / Total number of computers : "
  //                    + page.getTotalElements());
  //                page.getContent().forEach(System.out::println);
  //              } else {
  //                System.out.println("Warning: first page reached!");
  //              }
  //              break;
  //            case "previous":
  //              if (page.hasPrevious()) {
  //                page = computerWebService.getPagedList("", page.previousPageable());
  //                System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                    + (page.getTotalPages()) + " / Total number of computers : "
  //                    + page.getTotalElements());
  //                page.getContent().forEach(System.out::println);
  //              } else {
  //                System.out.println("Warning: first page reached!");
  //              }
  //              break;
  //            case "f":
  //              pageable = new PageRequest(0, 20);
  //              page = computerWebService.getPagedList("", pageable);
  //              System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                  + (page.getTotalPages()) + " / Total number of computers : "
  //                  + page.getTotalElements());
  //              page.getContent().forEach(System.out::println);
  //              break;
  //            case "first":
  //              pageable = new PageRequest(0, 20);
  //              page = computerWebService.getPagedList("", pageable);
  //              System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                  + (page.getTotalPages()) + " / Total number of computers : "
  //                  + page.getTotalElements());
  //              page.getContent().forEach(System.out::println);
  //              break;
  //            case "l":
  //              pageable = new PageRequest(0, 20);
  //              page = computerWebService.getPagedList("", pageable);
  //              while (page.hasNext()) {
  //                page = computerWebService.getPagedList("", page.nextPageable());
  //              }
  //              System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                  + (page.getTotalPages()) + " / Total number of computers : "
  //                  + page.getTotalElements());
  //              page.getContent().forEach(System.out::println);
  //              break;
  //            case "last":
  //              pageable = new PageRequest(0, 20);
  //              page = computerWebService.getPagedList("", pageable);
  //              while (page.hasNext()) {
  //                page = computerWebService.getPagedList("", page.nextPageable());
  //              }
  //              System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                  + (page.getTotalPages()) + " / Total number of computers : "
  //                  + page.getTotalElements());
  //              page.getContent().forEach(System.out::println);
  //              break;
  //            default:
  //              System.out.println("Non valid command, please try again.");
  //              break;
  //          }
  //        }
  //      }
  //    } else {
  //      System.out.println("No computer found in database.");
  //    }
  //  }

  /**
   * Display a list of all the computers in database.
   */
  public void showComputerList() {
    //Print computer list from DB
    List<ComputerDto> computersDto = new ArrayList<ComputerDto>();
    computersDto = computerWebService.getAll();
    if (computersDto != null) {
      System.out.println("Here is a list of all the computers in the DB:\r\n");
      for (ComputerDto computerDto : computersDto) {
        System.out.println(computerDto.toString());
      }
    } else {
      System.out.println("No computer found in database.");
    }
  }

  /**
   * Display a page of twenty computers and allow navigation through pages.
   */
  //  public void showCompanyPage() {
  //    System.out.println("-> You entered the company list navigation:");
  //
  //    //Create a Pageable instance
  //    Pageable pageable = new PageRequest(0, 20);
  //    //Create a Page instance and get the first Page of computers from the database
  //    Page<Company> page = companyWebService.getPagedList(pageable);
  //
  //    if (page.hasContent()) {
  //      //Show the content of the page
  //      System.out.println("Page " + (page.getNumber() + 1) + " out of " + (page.getTotalPages())
  //          + " / Total number of companies : " + page.getTotalElements());
  //      page.getContent().forEach(System.out::println);
  //      while (true) {
  //        if (page.isFirst()) {
  //          System.out
  //              .println("\r\nType 'return' or 'r' to exit\r\nor 'next' or 'n' or press enter to show the next page\r\nor 'last' or 'l' to show the last page\r\nor the number of the page you want to display");
  //        } else if (page.isLast()) {
  //          System.out
  //              .println("\r\nType 'return' or 'r' to exit\r\nor 'previous' or 'p' to show the previous page\r\nor 'first' or 'f' to show the first page\r\nor the number of the page you want to display");
  //        } else {
  //          System.out
  //              .println("\r\nType 'return' or 'r' to exit\r\nor 'previous' or 'p' to show the previous page, 'next' or 'n' or press enter to show the next page\r\nor 'first' or 'f' to show the first page or 'last' or 'l' to show the last page\r\nor the number of the page you want to display");
  //        }
  //
  //        userInput = null;
  //
  //        userInput = sc.nextLine().trim().toLowerCase();
  //        if (StringValidator.isEmpty(userInput)) {
  //          if (page.hasNext()) {
  //            page = companyWebService.getPagedList(page.nextPageable());
  //            System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                + (page.getTotalPages()) + " / Total number of companies : "
  //                + page.getTotalElements());
  //            page.getContent().forEach(System.out::println);
  //          } else {
  //            System.out.println("Warning: last page reached!");
  //          }
  //        } else if (StringValidator.isPositiveLong(userInput)) {
  //          Integer number = new Integer(userInput);
  //          if (number < 1 || number > page.getTotalPages()) {
  //            System.out.println("Non valid page number.");
  //          } else {
  //            number--;
  //            pageable = new PageRequest(0, 20);
  //            page = companyWebService.getPagedList(pageable);
  //            while (page.getNumber() != number) {
  //              page = companyWebService.getPagedList(page.nextPageable());
  //            }
  //            System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                + (page.getTotalPages()) + " / Total number of companies : "
  //                + page.getTotalElements());
  //            page.getContent().forEach(System.out::println);
  //          }
  //        } else {
  //          switch (userInput) {
  //            case "r":
  //              System.out.println("-> back to main menu");
  //              return;
  //            case "return":
  //              System.out.println("-> back to main menu");
  //              return;
  //            case "n":
  //              if (page.hasNext()) {
  //                page = companyWebService.getPagedList(page.nextPageable());
  //                System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                    + (page.getTotalPages()) + " / Total number of companies : "
  //                    + page.getTotalElements());
  //                page.getContent().forEach(System.out::println);
  //              } else {
  //                System.out.println("Warning: last page reached!");
  //              }
  //              break;
  //            case "next":
  //              if (page.hasNext()) {
  //                page = companyWebService.getPagedList(page.nextPageable());
  //                System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                    + (page.getTotalPages()) + " / Total number of companies : "
  //                    + page.getTotalElements());
  //                page.getContent().forEach(System.out::println);
  //              } else {
  //                System.out.println("Warning: last page reached!");
  //              }
  //              break;
  //            case "p":
  //              if (page.hasPrevious()) {
  //                page = companyWebService.getPagedList(page.previousPageable());
  //                System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                    + (page.getTotalPages()) + " / Total number of companies : "
  //                    + page.getTotalElements());
  //                page.getContent().forEach(System.out::println);
  //              } else {
  //                System.out.println("Warning: first page reached!");
  //              }
  //              break;
  //            case "previous":
  //              if (page.hasPrevious()) {
  //                page = companyWebService.getPagedList(page.previousPageable());
  //                System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                    + (page.getTotalPages()) + " / Total number of companies : "
  //                    + page.getTotalElements());
  //                page.getContent().forEach(System.out::println);
  //              } else {
  //                System.out.println("Warning: first page reached!");
  //              }
  //              break;
  //            case "f":
  //              pageable = new PageRequest(0, 20);
  //              page = companyWebService.getPagedList(pageable);
  //              System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                  + (page.getTotalPages()) + " / Total number of companies : "
  //                  + page.getTotalElements());
  //              page.getContent().forEach(System.out::println);
  //              break;
  //            case "first":
  //              pageable = new PageRequest(0, 20);
  //              page = companyWebService.getPagedList(pageable);
  //              System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                  + (page.getTotalPages()) + " / Total number of companies : "
  //                  + page.getTotalElements());
  //              page.getContent().forEach(System.out::println);
  //              break;
  //            case "l":
  //              pageable = new PageRequest(0, 20);
  //              page = companyWebService.getPagedList(pageable);
  //              while (page.hasNext()) {
  //                page = companyWebService.getPagedList(page.nextPageable());
  //              }
  //              System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                  + (page.getTotalPages()) + " / Total number of companies : "
  //                  + page.getTotalElements());
  //              page.getContent().forEach(System.out::println);
  //              break;
  //            case "last":
  //              pageable = new PageRequest(0, 20);
  //              page = companyWebService.getPagedList(pageable);
  //              while (page.hasNext()) {
  //                page = companyWebService.getPagedList(page.nextPageable());
  //              }
  //              System.out.println("Page " + (page.getNumber() + 1) + " out of "
  //                  + (page.getTotalPages()) + " / Total number of companies : "
  //                  + page.getTotalElements());
  //              page.getContent().forEach(System.out::println);
  //              break;
  //            default:
  //              System.out.println("Non valid command, please try again.");
  //              break;
  //          }
  //        }
  //      }
  //    } else {
  //      System.out.println("No company found in database.");
  //    }
  //  }

  /**
   * Display a list of all the companies in database.
   */
  public void showCompanyList() {
    //Print company list from DB
    List<Company> companies = new ArrayList<Company>();
    companies = companyWebService.getAll();
    if (companies != null) {
      System.out.println("Here is a list of all the companies in the DB:\r\n");
      for (Company company : companies) {
        System.out.println(company.toString());
      }
    } else {
      System.out.println("No company found in database.");
    }
  }

  /**
   * Display the details of a precise computer in the database.
   * @param idS : String representing the Long id of the selected computer.
   */
  public void showComputer(final String idS) {
    //Print the details of the computer with id=idS

    if (StringValidator.isPositiveLong(idS)) {
      final ComputerDto computerDto = computerWebService.getById(new Long(idS));
      if (computerDto == null) {
        System.out.println("Computer not found in database.\r\n");
      } else {
        System.out.println("Here are the details of the computer you requested:");
        System.out.println(computerDto.toString());
      }
    } else {
      System.out.println("The id you entered is incorrect.\r\n");
    }
  }

  /**
   * Display the result of the "add" command.
   * @param params : String table composed of "name" (mandatory), "introduced" (date format: yyyy-MM-dd), discontinued (date format: yyyy-MM-dd), "companyId".
   * Use the String "null" to skip a value.
   */
  public void showAddResult(final String[] params) {
    final Map<String, String> errorMap = new HashMap<String, String>();
    final ComputerDto.Builder builder = ComputerDto.builder();
    if (params.length > 0) {
      if (!"null".equals(params[0].toLowerCase())) {
        builder.name(params[0]);
      }
    }
    if (params.length > 1) {
      if (!"null".equals(params[1].toLowerCase())) {
        builder.introduced(params[1]);
      }
    }
    if (params.length > 2) {
      if (!"null".equals(params[2].toLowerCase())) {
        builder.discontinued(params[2]);
      }
    }
    if (params.length > 3) {
      if (!"null".equals(params[3].toLowerCase())) {
        if (StringValidator.isPositiveLong(params[3])) {
          final Company company = companyWebService.getById(new Long(params[3]));
          if (company != null) {
            builder.companyId(company.getId());
            builder.companyName(company.getName());
          } else {
            errorMap.put("eCompanyId",
                "Incorrect CompanyId: No Company found in database with given id");
          }
        }
      }
    }

    final ComputerDto computerDto = builder.build();

    //Check computerDto
    if (ComputerDtoValidator.isValid(computerDto, errorMap)) {
      Response response = null;
      response = computerWebService.addByComputer(computerDto);
      if (response == null) {
        System.out.println("Error: Computer could not be added to the DB.\r\n");
      } else {
        System.out.println(response.getLocation().toString());
      }
    } else {
      System.out.println(errorMap.toString());
    }
  }

  /**
   * Display the result of the "add" command.
   * @param computer : instance of the computer that needs to be added to the database. Must have a name at least. 
   */
  public void showAddResult(final Computer computer) {
    //Check computerDto
    final Map<String, String> errorMap = new HashMap<String, String>();
    if (ComputerDtoValidator.isValid(ComputerDtoConverter.toDto(computer), errorMap)) {
      Response response = null;
      response = computerWebService.addByComputer(ComputerDtoConverter.toDto(computer));
      if (response == null) {
        System.out.println("Error: Computer could not be added to the DB.\r\n");
      } else {
        System.out.println("Your computer was successfully added to the DB :");
        System.out.println(response.getLocation().toString());
      }
    } else {
      System.out.println(errorMap.toString());
    }
  }

  /**
   * Display the result of the "update" command.
   * @param params : String table composed of "id" (mandatory), "name" (mandatory), "introduced" (date format: yyyy-MM-dd), discontinued (date format: yyyy-MM-dd), "companyId".
   * All the attributes of the updated computer gets changed.
   */
  public void showUpdateResult(final String[] params) {
    final Map<String, String> errorMap = new HashMap<String, String>();
    final ComputerDto.Builder builder = ComputerDto.builder();
    if (params.length > 0) {
      if (StringValidator.isPositiveLong(params[0])) {
        if (computerWebService.getById(new Long(params[0])) != null) {
          errorMap.put("eId", "Incorrect Id: No Computer found in database with given id");
        } else {
          builder.id(new Long(params[0]));
        }
      } else {
        errorMap.put("eId", "Incorrect Id: an id should be a positive integer");
      }
    }
    if (params.length > 1) {
      if (!"null".equals(params[1].toLowerCase())) {
        builder.name(params[1]);
      } else {
        errorMap.put("eName", "Incorrect name");
      }
    }
    if (params.length > 2) {
      if (!"null".equals(params[2].toLowerCase())) {
        builder.introduced(params[2]);
      }
    }
    if (params.length > 3) {
      if (!"null".equals(params[3].toLowerCase())) {
        builder.discontinued(params[3]);
      }
    }
    if (params.length > 4) {
      if (!"null".equals(params[4].toLowerCase())) {
        if (StringValidator.isPositiveLong(params[4])) {
          final Company company = companyWebService.getById(new Long(params[4]));
          if (company != null) {
            builder.companyId(company.getId());
            builder.companyName(company.getName());
          } else {
            errorMap.put("eCompanyId",
                "Incorrect CompanyId: No Company found in database with given id");
          }
        }
      }
    }

    final ComputerDto computerDto = builder.build();

    //Check computerDto
    if (ComputerDtoValidator.isValid(computerDto, errorMap)) {
      Response response = null;
      response = computerWebService.updateByComputer(computerDto);
      if (response == null) {
        System.out.println("Error: Computer could not be updated in the DB.\r\n");
      } else {
        System.out.println("Your computer was updated successfully in the DB :");
        System.out.println(response.getLocation().toString());
      }
    } else {
      System.out.println(errorMap.toString());
    }
  }

  /**
   * Display the result of the "update" command.
   * @param computer : instance of the computer that needs to be added to the database. Must have an id at least. 
   */
  public void showUpdateResult(final Computer computer) {
    //Check computerDto
    final Map<String, String> errorMap = new HashMap<String, String>();
    if (ComputerDtoValidator.isValid(ComputerDtoConverter.toDto(computer), errorMap)) {
      Response response = null;
      response = computerWebService.updateByComputer(ComputerDtoConverter.toDto(computer));
      if (response == null) {
        System.out.println("Error: Computer could not be updated in the DB.\r\n");
      } else {
        System.out.println("Your computer was updated successfully in the DB :");
        System.out.println(response.getLocation().toString());
      }
    } else {
      System.out.println(errorMap.toString());
    }
  }

  /**
   * Display the result of the "remove computer" command.
   * @param idS : String representing the Long id of the computer to remove.
   */
  public void showRemoveComputerResult(final String idS) {
    //Print the details of the computer with id=idS
    if (StringValidator.isPositiveLong(idS)) {
      if (computerWebService.getById(new Long(idS)) != null) {
        Response response = computerWebService.removeById(new Long(idS));
        if (response != null) {
          System.out.println("Your computer was successfully removed from the DB");
        } else {
          System.out.println("Error: Computer could not be removed from DB.\r\n");
        }
      } else {
        System.out.println("No computer found in database with given id.\r\n");
      }
    } else {
      System.out.println("The id you entered is incorrect.\r\n");
    }
  }

  /**
   * Display the result of the "remove company" command.
   * @param idS : String representing the Long id of the company to remove.
   */
  public void showRemoveCompanyResult(final String idS) {
    //Print the details of the computer with id=idS
    if (StringValidator.isPositiveLong(idS)) {
      if (companyWebService.getById(new Long(idS)) != null) {
        Response response = companyWebService.removeById(new Long(idS));
        if (response != null) {
          System.out.println("Company successfully deleted ");
        } else {
          System.out.println("No company found with this id in the database");
        }
      } else {
        System.out.println("No company found in database with given id.\r\n");
      }
    } else {
      System.out.println("Incorrect id: must be a strictly positive integer");
    }
  }

  /**
   * Display the help for specific commands.
   * @param choice : the Integer corresponding to the selected command.
   */
  public void showHelp(final int choice) {
    switch (choice) {
      case 0:
        System.out
            .println("HELP command '0': type '0' or 'menu' in order to display the menu.\r\n");
        break;
      case 1:
        System.out
            .println("HELP command '1': type '1' or 'ls computers' in order to display the list of all the computers in DB.\r\n");
        break;
      case 2:
        System.out
            .println("HELP command '2': type '2' or 'ls companies' in order to display the list of all the computers in DB.\r\n");
        break;
      case 3:
        System.out
            .println("HELP command '3': type '3 id' or 'show id' in order to display the details of a specific computer.");
        System.out.println("- id is a positive integer\r\n");
        break;
      case 4:
        System.out
            .println("HELP command '4': type '4 name introduced discontinued companyId' or 'add name introduced discontinued companyId' in order to add a new computer to the DB.");
        System.out
            .println("- name is mandatory whereas introduced and discontinued and companyId are optional");
        System.out
            .println("- introduced and discontinued are dates with the following format: 'yyyy-MM-dd'");
        System.out.println("- companyId is an integer between [1, 43]");
        System.out.println("- if you do not want to set a value, refer to it as null\r\n");
        break;
      case 5:
        System.out
            .println("HELP command '5': type '5 id name introduced discontinued companyId' or 'update id name introduced discontinued companyId' in order to update an existing computer in the DB.");
        System.out
            .println("- id is mandatory whereas name and introduced and discontinued and companyId are optional");
        System.out
            .println("- introduced and discontinued are dates with the following format: 'yyyy-MM-dd'");
        System.out.println("- id is a positive integer");
        System.out.println("- companyId is a positive integer");
        System.out.println("- if you do not want to set a value, refer to it as null\r\n");
        break;
      case 6:
        System.out
            .println("HELP command '6': type '6 id' or 'remove computer id' in order to remove a specific computer from the DB.\r\n");
        break;
      case 7:
        System.out
            .println("HELP command '7': type '7 id' or 'remove company id' in order to remove a specific company and the related computers from the DB.\r\n");
        break;
      case 8:
        System.out.println("HELP command 'menu':");
        System.out.println("- type '0' or 'menu' in order to display the menu.\r\n");
        break;
      case 9:
        System.out.println("HELP command 'ls':");
        System.out
            .println("- type '1' or 'ls computers' in order to display the list of all the computers in DB.");
        System.out
            .println("- type '2' or 'ls companies' in order to display the list of all the computers in DB.\r\n");
        break;
      case 10:
        System.out.println("HELP command 'show':");
        System.out
            .println("- type '3 id' or 'show id' in order to display the details of a specific computer.");
        System.out.println("- id is a positive integer\r\n");
        break;
      case 11:
        System.out.println("HELP command 'add':");
        System.out
            .println("- type '4 name introduced discontinued companyId' or 'add name introduced discontinued companyId' in order to add a new computer to the DB.");
        System.out
            .println("- name is mandatory whereas introduced and discontinued and companyId are optional");
        System.out
            .println("- introduced and discontinued are dates with the following format: 'yyyy-MM-dd'");
        System.out.println("- companyId is an integer between [1, 43]");
        System.out.println("- if you do not want to set a value, refer to it as null\r\n");
        break;
      case 12:
        System.out.println("HELP command 'update':");
        System.out
            .println("- type '5 id name introduced discontinued companyId' or 'update id name introduced discontinued companyId' in order to update an existing computer in the DB.");
        System.out
            .println("- id is mandatory whereas name and introduced and discontinued and companyId are optional");
        System.out
            .println("- introduced and discontinued are dates with the following format: 'yyyy-MM-dd'");
        System.out.println("- id is a positive integer");
        System.out.println("- companyId is an integer between [1, 43]");
        System.out.println("- if you do not want to set a value, refer to it as null\r\n");
        break;
      case 13:
        System.out.println("HELP command 'remove':");
        System.out
            .println("- type '6 id' or 'remove id' in order to remove a specific computer from the DB.\r\n");
        break;
      default:
        System.out.println("Non valid command. Please, try again.\r\n");
        break;
    }
  }
}
