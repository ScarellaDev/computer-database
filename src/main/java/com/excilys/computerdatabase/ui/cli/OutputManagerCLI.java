package com.excilys.computerdatabase.ui.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.service.ICompanyDBService;
import com.excilys.computerdatabase.service.IComputerDBService;
import com.excilys.computerdatabase.service.impl.CompanyDBServiceImpl;
import com.excilys.computerdatabase.service.impl.ComputerDBServiceImpl;
import com.excilys.computerdatabase.validator.StringValidation;

/**
* Class managing the outputs of the CLI.
*
* @author Jeremy SCARELLA
*/
public class OutputManagerCLI {
  /*
   * Instance of computerDBService
   */
  private static IComputerDBService computerDBService = ComputerDBServiceImpl.INSTANCE;

  /*
   * Instance of companyDBService
   */
  private static ICompanyDBService  companyDBService  = CompanyDBServiceImpl.INSTANCE;

  /*
   * Scanner sc : get the user input
   * String userInput : save the user input
   */
  private static Scanner            sc;
  private static String             userInput;

  /**
   * Display the main menu
   */
  public static void showMenu() {
    StringBuffer menu = new StringBuffer("* * * * * MENU * * * * *\r\n");
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
  public static void showComputerPage() {
    System.out.println("-> You entered the computer list navigation:");
    //Create a Page
    Page<ComputerDto> page = new Page<ComputerDto>();
    //Get the first Page of computers from the database
    page = computerDBService.getPagedList(page);
    //Show the content of the page
    System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
        + " / Total number of computers : " + page.getTotalNbElements());
    page.getList().forEach(System.out::println);

    while (true) {
      if (page.getPageIndex() == 1) {
        System.out
            .println("\r\nType 'return' or 'r' to exit, 'next' or 'n' or press enter to show the next page\r\nor the number of the page you want to display");
      } else if (page.getPageIndex() == page.getTotalNbPages()) {
        System.out
            .println("\r\nType 'return' or 'r' to exit, 'previous' or 'p' to show the previous page\r\nor the number of the page you want to display");
      } else {
        System.out
            .println("\r\nType 'return' or 'r' to exit, 'previous' or 'p' to show the previous page, 'next' or 'n' or press enter to show the next page\r\nor the number of the page you want to display");
      }

      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput == null || userInput.isEmpty() || "".equals(userInput)) {
        if (page.next()) {
          page = computerDBService.getPagedList(page);
          System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
              + " / Total number of computers : " + page.getTotalNbElements());
          page.getList().forEach(System.out::println);
        } else {
          System.out.println("Warning: last page reached!");
        }
      } else if (StringValidation.isPositiveLong(userInput)) {
        Integer index = new Integer(userInput);
        if (index < 1 || index > page.getTotalNbPages()) {
          System.out.println("Non valid page number.");
        } else {
          page.setPageIndex(index);
          page = computerDBService.getPagedList(page);
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
              page = computerDBService.getPagedList(page);
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
              page = computerDBService.getPagedList(page);
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
              page = computerDBService.getPagedList(page);
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
              page = computerDBService.getPagedList(page);
              System.out.println("Page " + page.getPageIndex() + " out of "
                  + page.getTotalNbPages() + " / Total number of computers : "
                  + page.getTotalNbElements());
              page.getList().forEach(System.out::println);
            } else {
              System.out.println("Warning: first page reached!");
            }
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
  public static void showComputerList() {
    //Print computer list from DB
    List<ComputerDto> computersDto = new ArrayList<ComputerDto>();
    computersDto = computerDBService.getAll();
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
  public static void showCompanyPage() {
    System.out.println("-> You entered the company list navigation:");

    //Create a Page
    Page<Company> page = new Page<Company>();
    //Get the first Page of computers from the database
    page = companyDBService.getPagedList(page);
    //Show the content of the page
    System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
        + " / Total number of companies : " + page.getTotalNbElements());
    page.getList().forEach(System.out::println);
    while (true) {
      if (page.getPageIndex() == 1) {
        System.out
            .println("\r\nType 'return' or 'r' to exit, 'next' or 'n' or press enter to show the next page\r\nor the number of the page you want to display");
      } else if (page.getPageIndex() == page.getTotalNbPages()) {
        System.out
            .println("\r\nType 'return' or 'r' to exit, 'previous' or 'p' to show the previous page\r\nor the number of the page you want to display");
      } else {
        System.out
            .println("\r\nType 'return' or 'r' to exit, 'previous' or 'p' to show the previous page, 'next' or 'n' or press enter to show the next page\r\nor the number of the page you want to display");
      }

      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || "".equals(userInput)) {
        if (page.next()) {
          page = companyDBService.getPagedList(page);
          System.out.println("Page " + page.getPageIndex() + " out of " + page.getTotalNbPages()
              + " / Total number of companies : " + page.getTotalNbElements());
          page.getList().forEach(System.out::println);
        } else {
          System.out.println("Warning: last page reached!");
        }
      } else if (StringValidation.isPositiveLong(userInput)) {
        Integer index = new Integer(userInput);
        if (index < 1 || index > page.getTotalNbPages()) {
          System.out.println("Non valid page number.");
        } else {
          page.setPageIndex(index);
          page = companyDBService.getPagedList(page);
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
              page = companyDBService.getPagedList(page);
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
              page = companyDBService.getPagedList(page);
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
              page = companyDBService.getPagedList(page);
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
              page = companyDBService.getPagedList(page);
              System.out.println("Page " + page.getPageIndex() + " out of "
                  + page.getTotalNbPages() + " / Total number of companies : "
                  + page.getTotalNbElements());
              page.getList().forEach(System.out::println);
            } else {
              System.out.println("Warning: first page reached!");
            }
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
  public static void showCompanyList() {
    //Print company list from DB
    List<Company> companies = new ArrayList<Company>();
    companies = companyDBService.getAll();
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
  public static void showComputer(String idS) {
    //Print the details of the computer with id=idS

    if (StringValidation.isPositiveLong(idS)) {
      ComputerDto computerDto = computerDBService.getById(new Long(idS));
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
  public static void showAddResult(String[] params) {
    ComputerDto computerDto = null;
    computerDto = computerDBService.addByString(params);
    if (computerDto == null) {
      System.out.println("MySQL Error: your computer could not be added to the DB.\r\n");
    } else {
      System.out.println("Your computer was successfully added to the DB :");
      System.out.println(computerDto.toString());
    }
  }

  /**
   * Display the result of the "add" command.
   * @param computer : instance of the computer that needs to be added to the database. Must have a name at least. 
   */
  public static void showAddResult(Computer computer) {
    ComputerDto computerDto = computerDBService.addByComputer(computer);
    if (computerDto == null) {
      System.out.println("MySQL Error: your computer could not be added to the DB.\r\n");
    } else {
      System.out.println("Your computer was successfully added to the DB :");
      System.out.println(computerDto.toString());
    }
  }

  /**
   * Display the result of the "update" command.
   * @param params : String table composed of "id" (mandatory), "name" (mandatory), "introduced" (date format: yyyy-MM-dd), discontinued (date format: yyyy-MM-dd), "companyId".
   * All the attributes of the updated computer gets changed.
   */
  public static void showUpdateResult(String[] params) {
    ComputerDto computerDto = computerDBService.updateByString(params);
    if (computerDto == null) {
      System.out.println("MySQL Error: your computer could not be updated in the DB.\r\n");
    } else {
      System.out.println("Your computer was updated successfully in the DB :");
      System.out.println(computerDto.toString());
    }
  }

  /**
   * Display the result of the "update" command.
   * @param computer : instance of the computer that needs to be added to the database. Must have an id at least. 
   */
  public static void showUpdateResult(Computer computer) {
    ComputerDto computerDto = computerDBService.updateByComputer(computer);
    if (computerDto == null) {
      System.out.println("MySQL Error: your computer could not be updated in the DB.\r\n");
    } else {
      System.out.println("Your computer was updated successfully in the DB :");
      System.out.println(computerDto.toString());
    }
  }

  /**
   * Display the result of the "remove computer" command.
   * @param idS : String representing the Long id of the computer to remove.
   */
  public static void showRemoveComputerResult(String idS) {
    //Print the details of the computer with id=idS
    if (StringValidation.isPositiveLong(idS)) {
      ComputerDto computerDto = computerDBService.removeById(new Long(idS));
      if (computerDto == null) {
        System.out.println("MySQL Error: computer could not be removed from DB.\r\n");
      } else {
        System.out.println("Your computer was successfully removed from the DB :");
        System.out.println(computerDto.toString());
      }
    } else {
      System.out.println("The id you entered is incorrect.\r\n");
    }
  }

  /**
   * Display the result of the "remove company" command.
   * @param idS : String representing the Long id of the company to remove.
   */
  public static void showRemoveCompanyResult(String idS) {
    //Print the details of the computer with id=idS
    if (StringValidation.isPositiveLong(idS)) {
      if (companyDBService.removeById(new Long(idS))) {
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
  public static void showHelp(int choice) {
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
