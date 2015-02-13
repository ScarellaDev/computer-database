package com.excilys.computerdatabase.ui.cli;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.service.ICompanyService;
import com.excilys.computerdatabase.service.IComputerService;
import com.excilys.computerdatabase.validator.StringValidator;

/**
* Class managing the inputs of the CLI.
*
* @author Jeremy SCARELLA
*/
public class InputManagerCLI {

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
  private IComputerService               computerService;

  /**
   * Instance of CompanyService for the access to the database
   */
  private ICompanyService                companyService;

  /**
   * Instance of OuputManagerCLI
   */
  private OutputManagerCLI               outputManagerCLI;

  public InputManagerCLI(final ClassPathXmlApplicationContext context) {
    sc = new Scanner(System.in);
    outputManagerCLI = new OutputManagerCLI(context);
    computerService = (IComputerService) context.getBean("computerService");
    companyService = (ICompanyService) context.getBean("companyService");
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
        outputManagerCLI.showComputerPage();
        break;
      case "ls computers":
        outputManagerCLI.showComputerPage();
        break;
      case "1":
        outputManagerCLI.showComputerPage();
        break;
      case "companies":
        outputManagerCLI.showCompanyPage();
        break;
      case "ls companies":
        outputManagerCLI.showCompanyPage();
        break;
      case "2":
        outputManagerCLI.showCompanyPage();
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
      if (userInput.isEmpty() || userInput == null || userInput.equals("")) {
        System.out.println("-> show command aborted");
        return;
      } else {
        if (StringValidator.isPositiveLong(userInput)) {

          outputManagerCLI.showComputer(userInput);
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
        builder.name(userInput);
        break;
      }
    }

    //Get introduced date
    System.out
        .println("- please enter the introduced date (format: yyyy-MM-dd) of the computer you want to add (press enter or type 'null' to skip this value):");
    while (true) {
      userInput = null;

      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || "".equals(userInput)
          || "null".equals(userInput)) {
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
      if (userInput.isEmpty() || userInput == null || "".equals(userInput)
          || "null".equals(userInput)) {
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
      if (userInput.isEmpty() || userInput == null || "".equals(userInput)
          || "null".equals(userInput)) {
        break;
      } else {
        if (StringValidator.isPositiveLong(userInput)) {
          if (companyService.getById(new Long(userInput)) != null) {
            builder.company(companyService.getById(new Long(userInput)));
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
    outputManagerCLI.showAddResult(builder.build());
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
      if (userInput.isEmpty() || userInput == null || "".equals(userInput)
          || "null".equals(userInput)) {
        System.out.println("-> update command aborted");
        return;
      } else {
        if (StringValidator.isPositiveLong(userInput)) {
          if (computerService.getById(new Long(userInput)) != null) {
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
      if (StringValidator.isEmpty(userInput) || "null".equals(userInput)) {
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

      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || "".equals(userInput)
          || "null".equals(userInput)) {
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
      if (userInput.isEmpty() || userInput == null || "".equals(userInput)
          || "null".equals(userInput)) {
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
      if (userInput.isEmpty() || userInput == null || "".equals(userInput)
          || "null".equals(userInput)) {
        break;
      } else {
        if (StringValidator.isPositiveLong(userInput)) {
          if (companyService.getById(new Long(userInput)) != null) {
            builder.company(companyService.getById(new Long(userInput)));
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
    outputManagerCLI.showUpdateResult(builder.build());
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
      if (userInput.isEmpty() || userInput == null || userInput.equals("")) {
        System.out.println("-> remove computer command aborted");
        return;
      } else {
        if (StringValidator.isPositiveLong(userInput)) {
          outputManagerCLI.showRemoveComputerResult(userInput);
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
      if (userInput.isEmpty() || userInput == null || userInput.equals("")) {
        System.out.println("-> remove company command aborted");
        return;
      } else {
        if (StringValidator.isPositiveLong(userInput)) {
          outputManagerCLI.showRemoveCompanyResult(userInput);
          break;
        } else {
          System.out.println("Please, enter a new valid id:");
          continue;
        }
      }
    }
  }
}
