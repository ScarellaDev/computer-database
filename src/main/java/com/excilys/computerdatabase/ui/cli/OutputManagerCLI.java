package com.excilys.computerdatabase.ui.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.service.ICompanyService;
import com.excilys.computerdatabase.service.IComputerService;
import com.excilys.computerdatabase.validator.ComputerDtoValidator;
import com.excilys.computerdatabase.validator.StringValidator;

/**
* Class managing the outputs of the CLI.
*
* @author Jeremy SCARELLA
*/
public class OutputManagerCLI {

  /*
   * Scanner sc : get the user input
   * String userInput : save the user input
   */
  private static Scanner   sc;
  private static String    userInput;

  /**
  * Instance of ComputerServiceJDBC for the access to the database
  */
  private IComputerService computerService;

  /**
  * Instance of CompanyServiceJDBC for the access to the database
  */
  private ICompanyService  companyService;

  public OutputManagerCLI() {
    final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
        "applicationContext.xml");
    computerService = (IComputerService) context.getBean("computerServiceJDBC");
    companyService = (ICompanyService) context.getBean("companyServiceJDBC");
    context.close();
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
  public void showComputerPage() {
    System.out.println("-> You entered the computer list navigation:");
    //Create a Page
    Page<ComputerDto> page = new Page<ComputerDto>();
    //Get the first Page of computers from the database
    page = computerService.getPagedList(page);
    //Show the content of the page
    System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
        + " / Total number of computers : " + page.getTotalNbElements());
    page.getList().forEach(System.out::println);

    while (true) {
      if (page.getPageIndex() == 1) {
        System.out
            .println("\r\nType 'return' or 'r' to exit\r\nor 'next' or 'n' or press enter to show the next page\r\nor 'last' or 'l' to show the last page\r\nor the number of the page you want to display");
      } else if (page.getPageIndex() == page.getTotalNbPages()) {
        System.out
            .println("\r\nType 'return' or 'r' to exit\r\nor 'previous' or 'p' to show the previous page\r\nor 'first' or 'f' to show the first page\r\nor the number of the page you want to display");
      } else {
        System.out
            .println("\r\nType 'return' or 'r' to exit\r\nor 'previous' or 'p' to show the previous page, 'next' or 'n' or press enter to show the next page\r\nor 'first' or 'f' to show the first page or 'last' or 'l' to show the last page\r\nor the number of the page you want to display");
      }

      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput == null || userInput.isEmpty() || "".equals(userInput)) {
        if (page.next()) {
          page = computerService.getPagedList(page);
          System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
              + " / Total number of computers : " + page.getTotalNbElements());
          page.getList().forEach(System.out::println);
        } else {
          System.out.println("Warning: last page reached!");
        }
      } else if (StringValidator.isPositiveLong(userInput)) {
        final Integer index = new Integer(userInput);
        if (index < 1 || index > page.getTotalNbPages()) {
          System.out.println("Non valid page number.");
        } else {
          page.setPageIndex(index);
          page = computerService.getPagedList(page);
          System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
              + " / Total number of computers : " + page.getTotalNbElements());
          page.getList().forEach(System.out::println);
        }
      } else {
        switch (userInput) {
          case "r":
            System.out.println("-> back to main menu");
            return;
          case "return":
            System.out.println("-> back to main menu");
            return;
          case "n":
            if (page.next()) {
              page = computerService.getPagedList(page);
              System.out.println("Page " + page.getPageIndex() + " out of "
                  + page.getTotalNbPages() + " / Total number of computers : "
                  + page.getTotalNbElements());
              page.getList().forEach(System.out::println);
            } else {
              System.out.println("Warning: last page reached!");
            }
            break;
          case "next":
            if (page.next()) {
              page = computerService.getPagedList(page);
              System.out.println("Page " + page.getPageIndex() + " out of "
                  + page.getTotalNbPages() + " / Total number of computers : "
                  + page.getTotalNbElements());
              page.getList().forEach(System.out::println);
            } else {
              System.out.println("Warning: last page reached!");
            }
            break;
          case "p":
            if (page.previous()) {
              page = computerService.getPagedList(page);
              System.out.println("Page " + page.getPageIndex() + " out of "
                  + page.getTotalNbPages() + " / Total number of computers : "
                  + page.getTotalNbElements());
              page.getList().forEach(System.out::println);
            } else {
              System.out.println("Warning: first page reached!");
            }
            break;
          case "previous":
            if (page.previous()) {
              page = computerService.getPagedList(page);
              System.out.println("Page " + page.getPageIndex() + " out of "
                  + page.getTotalNbPages() + " / Total number of computers : "
                  + page.getTotalNbElements());
              page.getList().forEach(System.out::println);
            } else {
              System.out.println("Warning: first page reached!");
            }
            break;
          case "f":
            page.setPageIndex(1);
            page = computerService.getPagedList(page);
            System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
                + " / Total number of computers : " + page.getTotalNbElements());
            page.getList().forEach(System.out::println);
            break;
          case "first":
            page.setPageIndex(1);
            page = computerService.getPagedList(page);
            System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
                + " / Total number of computers : " + page.getTotalNbElements());
            page.getList().forEach(System.out::println);
            break;
          case "l":
            page.setPageIndex(page.getTotalNbPages());
            page = computerService.getPagedList(page);
            System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
                + " / Total number of computers : " + page.getTotalNbElements());
            page.getList().forEach(System.out::println);
            break;
          case "last":
            page.setPageIndex(page.getTotalNbPages());
            page = computerService.getPagedList(page);
            System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
                + " / Total number of computers : " + page.getTotalNbElements());
            page.getList().forEach(System.out::println);
            break;
          default:
            System.out.println("Non valid command, please try again.");
            break;
        }
      }
    }
  }

  /**
   * Display a list of all the computers in database.
   */
  public void showComputerList() {
    //Print computer list from DB
    List<ComputerDto> computersDto = new ArrayList<ComputerDto>();
    computersDto = computerService.getAll();
    if (computersDto != null) {
      System.out.println("Here is a list of all the computers in the DB:\r\n");
      for (ComputerDto computerDto : computersDto) {
        System.out.println(computerDto.toString());
      }
    } else {
      System.out.println("No computers found.");
    }
  }

  /**
   * Display a page of twenty computers and allow navigation through pages.
   */
  public void showCompanyPage() {
    System.out.println("-> You entered the company list navigation:");

    //Create a Page
    Page<Company> page = new Page<Company>();
    //Get the first Page of computers from the database
    page = companyService.getPagedList(page);
    //Show the content of the page
    System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
        + " / Total number of companies : " + page.getTotalNbElements());
    page.getList().forEach(System.out::println);
    while (true) {
      if (page.getPageIndex() == 1) {
        System.out
            .println("\r\nType 'return' or 'r' to exit\r\nor 'next' or 'n' or press enter to show the next page\r\nor 'last' or 'l' to show the last page\r\nor the number of the page you want to display");
      } else if (page.getPageIndex() == page.getTotalNbPages()) {
        System.out
            .println("\r\nType 'return' or 'r' to exit\r\nor 'previous' or 'p' to show the previous page\r\nor 'first' or 'f' to show the first page\r\nor the number of the page you want to display");
      } else {
        System.out
            .println("\r\nType 'return' or 'r' to exit\r\nor 'previous' or 'p' to show the previous page, 'next' or 'n' or press enter to show the next page\r\nor 'first' or 'f' to show the first page or 'last' or 'l' to show the last page\r\nor the number of the page you want to display");
      }

      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || "".equals(userInput)) {
        if (page.next()) {
          page = companyService.getPagedList(page);
          System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
              + " / Total number of companies : " + page.getTotalNbElements());
          page.getList().forEach(System.out::println);
        } else {
          System.out.println("Warning: last page reached!");
        }
      } else if (StringValidator.isPositiveLong(userInput)) {
        final Integer index = new Integer(userInput);
        if (index < 1 || index > page.getTotalNbPages()) {
          System.out.println("Non valid page number.");
        } else {
          page.setPageIndex(index);
          page = companyService.getPagedList(page);
          System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
              + " / Total number of companies : " + page.getTotalNbElements());
          page.getList().forEach(System.out::println);
        }
      } else {
        switch (userInput) {
          case "r":
            System.out.println("-> back to main menu");
            return;
          case "return":
            System.out.println("-> back to main menu");
            return;
          case "n":
            if (page.next()) {
              page = companyService.getPagedList(page);
              System.out.println("Page " + page.getPageIndex() + " out of "
                  + page.getTotalNbPages() + " / Total number of companies : "
                  + page.getTotalNbElements());
              page.getList().forEach(System.out::println);
            } else {
              System.out.println("Warning: last page reached!");
            }
            break;
          case "next":
            if (page.next()) {
              page = companyService.getPagedList(page);
              System.out.println("Page " + page.getPageIndex() + " out of "
                  + page.getTotalNbPages() + " / Total number of companies : "
                  + page.getTotalNbElements());
              page.getList().forEach(System.out::println);
            } else {
              System.out.println("Warning: last page reached!");
            }
            break;
          case "p":
            if (page.previous()) {
              page = companyService.getPagedList(page);
              System.out.println("Page " + page.getPageIndex() + " out of "
                  + page.getTotalNbPages() + " / Total number of companies : "
                  + page.getTotalNbElements());
              page.getList().forEach(System.out::println);
            } else {
              System.out.println("Warning: first page reached!");
            }
            break;
          case "previous":
            if (page.previous()) {
              page = companyService.getPagedList(page);
              System.out.println("Page " + page.getPageIndex() + " out of "
                  + page.getTotalNbPages() + " / Total number of companies : "
                  + page.getTotalNbElements());
              page.getList().forEach(System.out::println);
            } else {
              System.out.println("Warning: first page reached!");
            }
            break;
          case "f":
            page.setPageIndex(1);
            page = companyService.getPagedList(page);
            System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
                + " / Total number of computers : " + page.getTotalNbElements());
            page.getList().forEach(System.out::println);
            break;
          case "first":
            page.setPageIndex(1);
            page = companyService.getPagedList(page);
            System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
                + " / Total number of computers : " + page.getTotalNbElements());
            page.getList().forEach(System.out::println);
            break;
          case "l":
            page.setPageIndex(page.getTotalNbPages());
            page = companyService.getPagedList(page);
            System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
                + " / Total number of computers : " + page.getTotalNbElements());
            page.getList().forEach(System.out::println);
            break;
          case "last":
            page.setPageIndex(page.getTotalNbPages());
            page = companyService.getPagedList(page);
            System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
                + " / Total number of computers : " + page.getTotalNbElements());
            page.getList().forEach(System.out::println);
            break;
          default:
            System.out.println("Non valid command, please try again.");
            break;
        }
      }
    }
  }

  /**
   * Display a list of all the companies in database.
   */
  public void showCompanyList() {
    //Print company list from DB
    List<Company> companies = new ArrayList<Company>();
    companies = companyService.getAll();
    if (companies != null) {
      System.out.println("Here is a list of all the companies in the DB:\r\n");
      for (Company company : companies) {
        System.out.println(company.toString());
      }
    } else {
      System.out.println("No company found");
    }
  }

  /**
   * Display the details of a precise computer in the database.
   * @param idS : String representing the Long id of the selected computer.
   */
  public void showComputer(final String idS) {
    //Print the details of the computer with id=idS

    if (StringValidator.isPositiveLong(idS)) {
      final ComputerDto computerDto = computerService.getById(new Long(idS));
      if (computerDto == null) {
        System.out.println("MySQL Error: computer not found.\r\n");
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
          final Company company = companyService.getById(new Long(params[3]));
          if (company != null) {
            builder.companyId(company.getId());
            builder.companyName(company.getName());
          } else {
            errorMap.put("eCompanyId", "Incorrect CompanyId: no existing Company with given Id");
          }
        }
      }
    }

    final ComputerDto computerDto = builder.build();

    //Check computerDto
    if (ComputerDtoValidator.isValid(computerDto, errorMap)) {
      ComputerDto newComputerDto = null;
      newComputerDto = computerService.addByComputer(ComputerDtoConverter.toComputer(computerDto));
      if (newComputerDto == null) {
        System.out.println("MySQL Error: your computer could not be added to the DB.\r\n");
      } else {
        System.out.println("Your computer was successfully added to the DB :");
        System.out.println(newComputerDto.toString());
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
      ComputerDto newComputerDto = null;
      newComputerDto = computerService.addByComputer(computer);
      if (newComputerDto == null) {
        System.out.println("MySQL Error: your computer could not be added to the DB.\r\n");
      } else {
        System.out.println("Your computer was successfully added to the DB :");
        System.out.println(newComputerDto.toString());
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
        builder.id(new Long(params[0]));
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
          final Company company = companyService.getById(new Long(params[4]));
          if (company != null) {
            builder.companyId(company.getId());
            builder.companyName(company.getName());
          } else {
            errorMap.put("eCompanyId", "Incorrect CompanyId: no existing Company with given Id");
          }
        }
      }
    }

    final ComputerDto computerDto = builder.build();

    //Check computerDto
    if (ComputerDtoValidator.isValid(computerDto, errorMap)) {
      ComputerDto newComputerDto = null;
      newComputerDto = computerService.updateByComputer(ComputerDtoConverter
          .toComputer(computerDto));
      if (newComputerDto == null) {
        System.out.println("MySQL Error: your computer could not be updated in the DB.\r\n");
      } else {
        System.out.println("Your computer was updated successfully in the DB :");
        System.out.println(newComputerDto.toString());
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
      ComputerDto newComputerDto = null;
      newComputerDto = computerService.updateByComputer(computer);
      if (newComputerDto == null) {
        System.out.println("MySQL Error: your computer could not be updated in the DB.\r\n");
      } else {
        System.out.println("Your computer was updated successfully in the DB :");
        System.out.println(newComputerDto.toString());
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
      if (computerService.removeById(new Long(idS))) {
        System.out.println("Your computer was successfully removed from the DB :");
      } else {
        System.out.println("MySQL Error: computer could not be removed from DB.\r\n");
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
      if (companyService.removeById(new Long(idS))) {
        System.out
            .println("Your company and the attached computers were successfully removed from the DB.");
      } else {
        System.out.println("MySQL Error: company could not be removed from DB.\r\n");
      }
    } else {
      System.out.println("The id you entered is incorrect.\r\n");
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
