package com.excilys.computerdatabase.cli;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.validator.StringValidator;
import com.excilys.computerdatabase.webservice.ICompanyWebService;
import com.excilys.computerdatabase.webservice.IComputerWebService;

/**
* Class managing the inputs of the CLI.
*
* @author Jeremy SCARELLA
*/
public class InputManagerCLIClient {

  /*
   * Scanner sc : get the user input
   * String userInput : save the user input
   */
  private static Scanner                 sc;
  private static String                  userInput;

  /*
   * Date FORMATTER : yyyy-MM-dd
   */
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
  * Instance of ComputerService for the access to the database
  */
  private IComputerWebService            computerWebService;

  /**
   * Instance of CompanyService for the access to the database
   */
  private ICompanyWebService             companyWebService;

  /**
   * Instance of OuputManagerCLI
   */
  private OutputManagerCLIClient               outputManagerCLIClient;

  public InputManagerCLIClient(final ClassPathXmlApplicationContext context) {
    sc = new Scanner(System.in);
    outputManagerCLIClient = new OutputManagerCLIClient(context);
    computerWebService = (IComputerWebService) context.getBean(IComputerWebService.class);
    companyWebService = (ICompanyWebService) context.getBean(ICompanyWebService.class);
  }

  /**
   * Step by step input for the "ls" command.
   */
  public void askParamsLs() {
    userInput = null;
    System.out
        .println("-> You entered the ls command, please enter the option you want to display:\r\n- 'computers'\r\n- 'companies'");

    userInput = sc.nextLine().trim().toLowerCase();
    switch (userInput) {
      case "computers":
        outputManagerCLIClient.showComputerList();
        break;
      case "ls computers":
        outputManagerCLIClient.showComputerList();
        break;
      case "1":
        outputManagerCLIClient.showComputerList();
        break;
      case "companies":
        outputManagerCLIClient.showCompanyList();
        break;
      case "ls companies":
        outputManagerCLIClient.showCompanyList();
        break;
      case "2":
        outputManagerCLIClient.showCompanyList();
        break;
      default:
        System.out.println("Non valid command.\r\n-> ls command aborted");
        break;
    }
  }

  /**
   * Step by step input for the "show" command.
   */
  public void askParamsShow() {
    System.out
        .println("-> You entered the show command, please enter the id of the computer you want to display (or press enter to quit command):");
    while (true) {
      userInput = null;

      userInput = sc.nextLine().trim().toLowerCase();
      if (StringValidator.isEmpty(userInput)) {
        System.out.println("-> show command aborted");
        return;
      } else {
        if (StringValidator.isPositiveLong(userInput)) {

          outputManagerCLIClient.showComputer(userInput);
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
  public void askParamsAdd() {
    final Computer.Builder builder = Computer.builder();

    //Get name
    System.out
        .println("-> You entered the add command:\r\n- please enter the name of the computer you want to add (or press enter to quit command):");
    while (true) {
      userInput = null;

      userInput = sc.nextLine().trim().toLowerCase();
      if (StringValidator.isEmpty(userInput)) {
        System.out.println("-> add command aborted");
        return;
      } else {
        if (!StringValidator.isValidName(userInput)) {
          System.out
              .println("Non valid name (cannot be empty or set to 'null' or have its length > 255 characters)");
          continue;
        } else {
          builder.name(userInput);
          break;
        }
      }
    }

    //Get introduced date
    System.out
        .println("- please enter the introduced date (format: yyyy-MM-dd) of the computer you want to add (press enter or type 'null' to skip this value):");
    while (true) {
      userInput = null;

      userInput = sc.nextLine().trim().toLowerCase();
      if (StringValidator.isEmpty(userInput)) {
        break;
      } else {
        if (StringValidator.isDate(userInput, "yyyy-MM-dd")) {
          final StringBuffer introducedS = new StringBuffer(userInput);
          builder.introduced(LocalDate.parse(introducedS, FORMATTER));
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

      userInput = sc.nextLine().trim().toLowerCase();
      if (StringValidator.isEmpty(userInput)) {
        break;
      } else {
        if (StringValidator.isDate(userInput, "yyyy-MM-dd")) {
          final StringBuffer discontinuedS = new StringBuffer(userInput);
          builder.discontinued(LocalDate.parse(discontinuedS, FORMATTER));
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

      userInput = sc.nextLine().trim().toLowerCase();
      if (StringValidator.isEmpty(userInput)) {
        break;
      } else {
        if (StringValidator.isPositiveLong(userInput)) {
          if (companyWebService.getById(new Long(userInput)) != null) {
            builder.company(companyWebService.getById(new Long(userInput)));
            break;
          } else {
            System.out.println("Company not found in DB, please enter a new valid id:");
            continue;
          }
        } else {
          System.out.println("Please, enter a new valid id:");
          continue;
        }
      }
    }
    outputManagerCLIClient.showAddResult(builder.build());
  }

  /**
   * Step by step input for the "update" command.
   */
  public void askParamsUpdate() {
    final Computer.Builder builder = Computer.builder();

    //Get id
    System.out
        .println("-> You entered the update command:\r\n- please enter the id of the computer you want to update (or press enter to quit command):");
    while (true) {
      userInput = null;

      userInput = sc.nextLine().trim().toLowerCase();
      if (StringValidator.isEmpty(userInput)) {
        System.out.println("-> update command aborted");
        return;
      } else {
        if (StringValidator.isPositiveLong(userInput)) {
          if (computerWebService.getById(new Long(userInput)) != null) {
            builder.id(new Long(userInput));
            break;
          } else {
            System.out.println("Computer not found in DB, please enter a new valid id:");
            continue;
          }
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

      userInput = sc.nextLine().trim().toLowerCase();
      if (!StringValidator.isValidName(userInput)) {
        System.out
            .println("Non valid name (cannot be empty or set to 'null' or have its length > 255 characters)");
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

      userInput = sc.nextLine().trim().toLowerCase();
      if (StringValidator.isEmpty(userInput)) {
        break;
      } else {
        if (StringValidator.isDate(userInput, "yyyy-MM-dd")) {
          final StringBuffer introducedS = new StringBuffer(userInput);
          builder.introduced(LocalDate.parse(introducedS, FORMATTER));
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

      userInput = sc.nextLine().trim().toLowerCase();
      if (StringValidator.isEmpty(userInput)) {
        break;
      } else {
        if (StringValidator.isDate(userInput, "yyyy-MM-dd")) {
          final StringBuffer discontinuedS = new StringBuffer(userInput);
          builder.discontinued(LocalDate.parse(discontinuedS, FORMATTER));
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

      userInput = sc.nextLine().trim().toLowerCase();
      if (StringValidator.isEmpty(userInput)) {
        break;
      } else {
        if (StringValidator.isPositiveLong(userInput)) {
          if (companyWebService.getById(new Long(userInput)) != null) {
            builder.company(companyWebService.getById(new Long(userInput)));
            break;
          } else {
            System.out.println("Company not found in DB, please enter a new valid id:");
            continue;
          }
        } else {
          System.out.println("Please, enter a new valid id:");
          continue;
        }
      }
    }
    outputManagerCLIClient.showUpdateResult(builder.build());
  }

  /**
   * Step by step input for the "remove" command.
   */
  public void askParamsRemove() {
    userInput = null;
    System.out
        .println("-> You entered the remove command, please enter the option you want to display:\r\n- 'computer'\r\n- 'company'");

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
  public void askParamsRemoveComputer() {
    System.out
        .println("-> You entered the remove computer command, please enter the id of the computer you want to remove from the DB (or press enter to quit command):");
    while (true) {
      userInput = null;

      userInput = sc.nextLine().trim().toLowerCase();
      if (StringValidator.isEmpty(userInput)) {
        System.out.println("-> remove computer command aborted");
        return;
      } else {
        if (StringValidator.isPositiveLong(userInput)) {
          outputManagerCLIClient.showRemoveComputerResult(userInput);
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
  public void askParamsRemoveCompany() {
    System.out
        .println("-> You entered the remove company command, please enter the id of the company you want to remove from the DB (or press enter to quit command):");
    while (true) {
      userInput = null;

      userInput = sc.nextLine().trim().toLowerCase();
      if (StringValidator.isEmpty(userInput)) {
        System.out.println("-> remove company command aborted");
        return;
      } else {
        if (StringValidator.isPositiveLong(userInput)) {
          outputManagerCLIClient.showRemoveCompanyResult(userInput);
          break;
        } else {
          System.out.println("Please, enter a new valid id:");
          continue;
        }
      }
    }
  }
}
