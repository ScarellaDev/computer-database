package com.excilys.computerdatabase.ui.cli;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.service.ICompanyDBService;
import com.excilys.computerdatabase.service.IComputerDBService;
import com.excilys.computerdatabase.service.impl.CompanyDBServiceImpl;
import com.excilys.computerdatabase.service.impl.ComputerDBServiceImpl;
import com.excilys.computerdatabase.validator.StringValidation;

/**
* Class managing the inputs of the CLI.
*
* @author Jeremy SCARELLA
*/
public class InputManagerCLI {
  /*
   * Instance of computerDBService
   */
  private static IComputerDBService      computerDBService = ComputerDBServiceImpl.INSTANCE;

  /*
   * Instance of companyDBService
   */
  private static ICompanyDBService       companyDBService  = CompanyDBServiceImpl.INSTANCE;

  /*
   * Scanner sc : get the user input
   * String userInput : save the user input
   */
  private static Scanner                 sc;
  private static String                  userInput;

  /*
   * Date FORMATTER : yyyy-MM-dd HH:mm:ss
   */
  private static final DateTimeFormatter FORMATTER         = DateTimeFormatter
                                                               .ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * Step by step input for the "ls" command.
   */
  public static void askParamsLs() {
    userInput = null;
    System.out
        .println("-> You entered the ls command, please enter the option you want to display:\r\n- 'computers'\r\n- 'companies'");
    sc = new Scanner(System.in);
    userInput = sc.nextLine().trim().toLowerCase();
    switch (userInput) {
      case "computers":
        OutputManagerCLI.showComputerPage();
        break;
      case "ls computers":
        OutputManagerCLI.showComputerPage();
        break;
      case "1":
        OutputManagerCLI.showComputerPage();
        break;
      case "companies":
        OutputManagerCLI.showCompanyPage();
        break;
      case "ls companies":
        OutputManagerCLI.showCompanyPage();
        break;
      case "2":
        OutputManagerCLI.showCompanyPage();
        break;
      default:
        System.out.println("Non valid command.\r\n-> ls command aborted");
        break;
    }
  }

  /**
   * Step by step input for the "show" command.
   */
  public static void askParamsShow() {
    System.out
        .println("-> You entered the show command, please enter the id of the computer you want to display (or press enter to quit command):");;
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || userInput.equals("")) {
        System.out.println("-> show command aborted");
        return;
      } else {
        if (StringValidation.isPositiveLong(userInput)) {
          OutputManagerCLI.showComputer(userInput);
          break;
        } else {
          System.out.println("Please, enter a new valid id:");
          continue;
        }
      }
    }
  }

  /**
   * Step by step input for the "add" command.
   */
  public static void askParamsAdd() {
    Computer.Builder builder = Computer.builder();

    //Get name
    System.out
        .println("-> You entered the add command:\r\n- please enter the name of the computer you want to add (or press enter to quit command):");
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (StringValidation.isEmpty(userInput)) {
        System.out.println("-> add command aborted");
        return;
      } else {
        builder.name(userInput);
        break;
      }
    }

    //Get introduced date
    System.out
        .println("- please enter the introduced date (format: yyyy-MM-dd) of the computer you want to add (press enter or type 'null' to skip this value):");
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || "".equals(userInput)
          || "null".equals(userInput)) {
        break;
      } else {
        if (StringValidation.isDate(userInput)) {
          StringBuffer introducedS = new StringBuffer(userInput);
          introducedS.append(" 00:00:00");
          builder.introduced(LocalDateTime.parse(introducedS, FORMATTER));
          break;
        } else {
          System.out.println("Please, enter a new valid date (format: yyyy-MM-dd):");
          continue;
        }
      }
    }

    //Get discontinued date
    System.out
        .println("- please enter the discontinued date (format: yyyy-MM-dd) of the computer you want to add (press enter or type 'null' to skip this value):");
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || "".equals(userInput)
          || "null".equals(userInput)) {
        break;
      } else {
        if (StringValidation.isDate(userInput)) {
          StringBuffer discontinuedS = new StringBuffer(userInput);
          discontinuedS.append(" 00:00:00");
          builder.discontinued(LocalDateTime.parse(discontinuedS, FORMATTER));
          break;
        } else {
          System.out.println("Please, enter a new valid date (format: yyyy-MM-dd):");
          continue;
        }
      }
    }

    //Get company_id
    System.out
        .println("- please enter the id of the company of the computer you want to add (press enter or type 'null' to skip this value):");
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || "".equals(userInput)
          || "null".equals(userInput)) {
        break;
      } else {
        if (StringValidation.isPositiveLong(userInput)) {
          builder.company(companyDBService.getById(new Long(userInput)));
          break;
        } else {
          System.out.println("Please, enter a new valid id:");
          continue;
        }
      }
    }
    OutputManagerCLI.showAddResult(builder.build());
  }

  /**
   * Step by step input for the "update" command.
   */
  public static void askParamsUpdate() {
    Computer.Builder builder = Computer.builder();

    //Get id
    System.out
        .println("-> You entered the update command:\r\n- please enter the id of the computer you want to update (or press enter to quit command):");
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || "".equals(userInput)
          || "null".equals(userInput)) {
        System.out.println("-> update command aborted");
        return;
      } else {
        if (StringValidation.isPositiveLong(userInput)) {
          builder.id(new Long(userInput));
          break;
        } else {
          System.out.println("Please, enter a new valid id:");
          continue;
        }
      }
    }

    //Get name
    System.out.println("- please enter the name of the computer:");
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (StringValidation.isEmpty(userInput) || "null".equals(userInput)) {
        System.out.println("Non valid name (cannot be empty or set to 'null')");
        continue;
      } else {
        builder.name(userInput);
        break;
      }
    }

    //Get introduced date
    System.out
        .println("- please enter the introduced date (format: yyyy-MM-dd) of the computer if you want to update it (press enter or type 'null' to skip this value):");
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || "".equals(userInput)
          || "null".equals(userInput)) {
        break;
      } else {
        if (StringValidation.isDate(userInput)) {
          StringBuffer introducedS = new StringBuffer(userInput);
          introducedS.append(" 00:00:00");
          builder.introduced(LocalDateTime.parse(introducedS, FORMATTER));
          break;
        } else {
          System.out.println("Please, enter a new valid date (format: yyyy-MM-dd):");
          continue;
        }
      }
    }

    //Get discontinued date
    System.out
        .println("- please enter the discontinued date (format: yyyy-MM-dd) of the computer if you want to update (press enter or type 'null' to skip this value):");
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || "".equals(userInput)
          || "null".equals(userInput)) {
        break;
      } else {
        if (StringValidation.isDate(userInput)) {
          StringBuffer discontinuedS = new StringBuffer(userInput);
          discontinuedS.append(" 00:00:00");
          builder.discontinued(LocalDateTime.parse(discontinuedS, FORMATTER));
          break;
        } else {
          System.out.println("Please, enter a new valid date (format: yyyy-MM-dd):");
          continue;
        }
      }
    }

    //Get company_id
    System.out
        .println("- please enter the id of the company of the computer if you want to update it (press enter or type 'null' to skip this value):");
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || "".equals(userInput)
          || "null".equals(userInput)) {
        break;
      } else {
        if (StringValidation.isPositiveLong(userInput)) {
          builder.company(companyDBService.getById(new Long(userInput)));
          break;
        } else {
          System.out.println("Please, enter a new valid id:");
          continue;
        }
      }
    }
    OutputManagerCLI.showUpdateResult(builder.build());
  }

  /**
   * Step by step input for the "remove" command.
   */
  public static void askParamsRemove() {
    userInput = null;
    System.out
        .println("-> You entered the remove command, please enter the option you want to display:\r\n- 'computer'\r\n- 'company'");
    sc = new Scanner(System.in);
    userInput = sc.nextLine().trim().toLowerCase();
    switch (userInput) {
      case "computer":
        askParamsRemoveComputer();
        break;
      case "remove computer":
        askParamsRemoveComputer();
        break;
      case "6":
        askParamsRemoveComputer();
        break;
      case "company":
        askParamsRemoveCompany();
        break;
      case "remove company":
        askParamsRemoveCompany();
        break;
      case "7":
        askParamsRemoveCompany();
        break;
      default:
        System.out.println("Non valid command.\r\n-> remove command aborted");
        break;
    }
  }

  /**
   * Step by step input for the "remove" command.
   */
  public static void askParamsRemoveComputer() {
    System.out
        .println("-> You entered the remove computer command, please enter the id of the computer you want to remove from the DB (or press enter to quit command):");;
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || userInput.equals("")) {
        System.out.println("-> remove computer command aborted");
        return;
      } else {
        if (StringValidation.isPositiveLong(userInput)) {
          OutputManagerCLI.showRemoveComputerResult(userInput);
          break;
        } else {
          System.out.println("Please, enter a new valid id:");
          continue;
        }
      }
    }
  }

  /**
   * Step by step input for the "remove" command.
   */
  public static void askParamsRemoveCompany() {
    System.out
        .println("-> You entered the remove company command, please enter the id of the company you want to remove from the DB (or press enter to quit command):");;
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || userInput.equals("")) {
        System.out.println("-> remove company command aborted");
        return;
      } else {
        if (StringValidation.isPositiveLong(userInput)) {
          OutputManagerCLI.showRemoveCompanyResult(userInput);
          break;
        } else {
          System.out.println("Please, enter a new valid id:");
          continue;
        }
      }
    }
  }
}
