package com.excilys.computerdatabase.cli;

import java.util.Scanner;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* Command Line Interface that allows the user to consult and modify his computer database in the Console.
*
* @author Jeremy SCARELLA
*/
public class CommandLineInterface {
  /*
   * Scanner mainSc : get the user input
   * String mainUserInput : save the user input
   */
  private static Scanner mainSc;
  private static String  mainUserInput;

  /**
   * Start the CLI : infinite loop waiting for mainUserInput and executing the command matching the mainUserInput when received.
   */
  public void start() {
    final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
        "service-context.xml");
    final InputManagerCLI inputManagerCLI = new InputManagerCLI(context);
    final OutputManagerCLI outputManagerCLI = new OutputManagerCLI(context);
    outputManagerCLI.showMenu();
    while (true) {
      mainUserInput = null;
      mainSc = new Scanner(System.in);
      mainUserInput = mainSc.nextLine().trim().toLowerCase();

      if (mainUserInput.toLowerCase().startsWith("ls ")) {
        mainUserInput = mainUserInput.substring(3);
        if (mainUserInput.equals("computers")) {
          outputManagerCLI.showComputerPage();
        } else if (mainUserInput.equals("companies")) {
          outputManagerCLI.showCompanyPage();
        } else {
          System.out.println("Non valid command. Please, try again.\r\n");
        }
      } else if (mainUserInput.toLowerCase().startsWith("3 ")) {
        mainUserInput = mainUserInput.substring(2);
        outputManagerCLI.showComputer(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("show ")) {
        mainUserInput = mainUserInput.substring(5);
        outputManagerCLI.showComputer(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("4 ")) {
        mainUserInput = mainUserInput.substring(2);
        outputManagerCLI.showAddResult(mainUserInput.split("\\s+"));
      } else if (mainUserInput.toLowerCase().startsWith("add ")) {
        mainUserInput = mainUserInput.substring(4);
        outputManagerCLI.showAddResult(mainUserInput.split("\\s+"));
      } else if (mainUserInput.toLowerCase().startsWith("5 ")) {
        mainUserInput = mainUserInput.substring(2);
        outputManagerCLI.showUpdateResult(mainUserInput.split("\\s+"));
      } else if (mainUserInput.toLowerCase().startsWith("update ")) {
        mainUserInput = mainUserInput.substring(7);
        outputManagerCLI.showUpdateResult(mainUserInput.split("\\s+"));
      } else if (mainUserInput.toLowerCase().startsWith("6 ")) {
        mainUserInput = mainUserInput.substring(2);
        outputManagerCLI.showRemoveComputerResult(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("remove computer ")) {
        mainUserInput = mainUserInput.substring(16);
        outputManagerCLI.showRemoveComputerResult(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("7 ")) {
        mainUserInput = mainUserInput.substring(2);
        outputManagerCLI.showRemoveCompanyResult(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("remove company ")) {
        mainUserInput = mainUserInput.substring(15);
        outputManagerCLI.showRemoveCompanyResult(mainUserInput);
      } else {
        Boolean help = false;
        if (mainUserInput.toLowerCase().startsWith("help ")) {
          help = true;
          mainUserInput = mainUserInput.substring(5);
        }
        switch (mainUserInput) {
          case "0":
            if (help) {
              outputManagerCLI.showHelp(0);
            } else {
              outputManagerCLI.showMenu();
            }
            break;
          case "1":
            if (help) {
              outputManagerCLI.showHelp(1);
            } else {
              outputManagerCLI.showComputerPage();
            }
            break;
          case "2":
            if (help) {
              outputManagerCLI.showHelp(2);
            } else {
              outputManagerCLI.showCompanyPage();
            }
            break;
          case "3":
            if (help) {
              outputManagerCLI.showHelp(3);
            } else {
              inputManagerCLI.askParamsShow();
            }
            break;
          case "4":
            if (help) {
              outputManagerCLI.showHelp(4);
            } else {
              inputManagerCLI.askParamsAdd();
            }
            break;
          case "5":
            if (help) {
              outputManagerCLI.showHelp(5);
            } else {
              inputManagerCLI.askParamsUpdate();
            }
            break;
          case "6":
            if (help) {
              outputManagerCLI.showHelp(6);
            } else {
              inputManagerCLI.askParamsRemoveComputer();
            }
            break;
          case "7":
            if (help) {
              outputManagerCLI.showHelp(7);
            } else {
              inputManagerCLI.askParamsRemoveCompany();
            }
            break;
          case "menu":
            outputManagerCLI.showHelp(8);
            break;
          case "ls":
            if (help) {
              outputManagerCLI.showHelp(9);
            } else {
              inputManagerCLI.askParamsLs();
            }
            break;
          case "computers":
            System.out.println("Did you mean 'ls computers'? See 'help ls' for more info.");
            break;
          case "companies":
            System.out.println("Did you mean 'ls companies'? See 'help ls' for more info.");
            break;
          case "show":
            if (help) {
              outputManagerCLI.showHelp(10);
            } else {
              inputManagerCLI.askParamsShow();
            }
            break;
          case "add":
            if (help) {
              outputManagerCLI.showHelp(11);
            } else {
              inputManagerCLI.askParamsAdd();
            }
            break;
          case "update":
            if (help) {
              outputManagerCLI.showHelp(12);
            } else {
              inputManagerCLI.askParamsUpdate();
            }
            break;
          case "remove":
            if (help) {
              outputManagerCLI.showHelp(13);
            } else {
              inputManagerCLI.askParamsRemove();
            }
            break;
          case "q":
            System.out.println("Thank you for using our CLI. Goodbye!");
            mainSc.close();
            context.close();
            return;
          case "quit":
            System.out.println("Thank you for using our CLI. Goodbye!");
            mainSc.close();
            context.close();
            return;
          case "exit":
            System.out.println("Thank you for using our CLI. Goodbye!");
            mainSc.close();
            context.close();
            return;
          default:
            System.out.println("Non valid command. Please, try again.\r\n");
            break;
        }
      }
    }
  }
}