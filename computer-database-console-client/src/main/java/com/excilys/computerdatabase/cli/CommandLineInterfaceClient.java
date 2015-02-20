package com.excilys.computerdatabase.cli;

import java.util.Scanner;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* Command Line Interface that allows the user to consult and modify his computer database in the Console.
*
* @author Jeremy SCARELLA
*/
public class CommandLineInterfaceClient {
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
        "console-client-context.xml");
    final InputManagerCLIClient inputManagerCLIClient = new InputManagerCLIClient(context);
    final OutputManagerCLIClient outputManagerCLIClient = new OutputManagerCLIClient(context);
    outputManagerCLIClient.showMenu();
    while (true) {
      mainUserInput = null;
      mainSc = new Scanner(System.in);
      mainUserInput = mainSc.nextLine().trim().toLowerCase();

      if (mainUserInput.toLowerCase().startsWith("ls ")) {
        mainUserInput = mainUserInput.substring(3);
        if (mainUserInput.equals("computers")) {
          outputManagerCLIClient.showComputerList();
        } else if (mainUserInput.equals("companies")) {
          outputManagerCLIClient.showCompanyList();
        } else {
          System.out.println("Non valid command. Please, try again.\r\n");
        }
      } else if (mainUserInput.toLowerCase().startsWith("3 ")) {
        mainUserInput = mainUserInput.substring(2);
        outputManagerCLIClient.showComputer(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("show ")) {
        mainUserInput = mainUserInput.substring(5);
        outputManagerCLIClient.showComputer(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("4 ")) {
        mainUserInput = mainUserInput.substring(2);
        outputManagerCLIClient.showAddResult(mainUserInput.split("\\s+"));
      } else if (mainUserInput.toLowerCase().startsWith("add ")) {
        mainUserInput = mainUserInput.substring(4);
        outputManagerCLIClient.showAddResult(mainUserInput.split("\\s+"));
      } else if (mainUserInput.toLowerCase().startsWith("5 ")) {
        mainUserInput = mainUserInput.substring(2);
        outputManagerCLIClient.showUpdateResult(mainUserInput.split("\\s+"));
      } else if (mainUserInput.toLowerCase().startsWith("update ")) {
        mainUserInput = mainUserInput.substring(7);
        outputManagerCLIClient.showUpdateResult(mainUserInput.split("\\s+"));
      } else if (mainUserInput.toLowerCase().startsWith("6 ")) {
        mainUserInput = mainUserInput.substring(2);
        outputManagerCLIClient.showRemoveComputerResult(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("remove computer ")) {
        mainUserInput = mainUserInput.substring(16);
        outputManagerCLIClient.showRemoveComputerResult(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("7 ")) {
        mainUserInput = mainUserInput.substring(2);
        outputManagerCLIClient.showRemoveCompanyResult(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("remove company ")) {
        mainUserInput = mainUserInput.substring(15);
        outputManagerCLIClient.showRemoveCompanyResult(mainUserInput);
      } else {
        Boolean help = false;
        if (mainUserInput.toLowerCase().startsWith("help ")) {
          help = true;
          mainUserInput = mainUserInput.substring(5);
        }
        switch (mainUserInput) {
          case "0":
            if (help) {
              outputManagerCLIClient.showHelp(0);
            } else {
              outputManagerCLIClient.showMenu();
            }
            break;
          case "1":
            if (help) {
              outputManagerCLIClient.showHelp(1);
            } else {
              outputManagerCLIClient.showComputerList();
            }
            break;
          case "2":
            if (help) {
              outputManagerCLIClient.showHelp(2);
            } else {
              outputManagerCLIClient.showCompanyList();
            }
            break;
          case "3":
            if (help) {
              outputManagerCLIClient.showHelp(3);
            } else {
              inputManagerCLIClient.askParamsShow();
            }
            break;
          case "4":
            if (help) {
              outputManagerCLIClient.showHelp(4);
            } else {
              inputManagerCLIClient.askParamsAdd();
            }
            break;
          case "5":
            if (help) {
              outputManagerCLIClient.showHelp(5);
            } else {
              inputManagerCLIClient.askParamsUpdate();
            }
            break;
          case "6":
            if (help) {
              outputManagerCLIClient.showHelp(6);
            } else {
              inputManagerCLIClient.askParamsRemoveComputer();
            }
            break;
          case "7":
            if (help) {
              outputManagerCLIClient.showHelp(7);
            } else {
              inputManagerCLIClient.askParamsRemoveCompany();
            }
            break;
          case "menu":
            outputManagerCLIClient.showHelp(8);
            break;
          case "ls":
            if (help) {
              outputManagerCLIClient.showHelp(9);
            } else {
              inputManagerCLIClient.askParamsLs();
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
              outputManagerCLIClient.showHelp(10);
            } else {
              inputManagerCLIClient.askParamsShow();
            }
            break;
          case "add":
            if (help) {
              outputManagerCLIClient.showHelp(11);
            } else {
              inputManagerCLIClient.askParamsAdd();
            }
            break;
          case "update":
            if (help) {
              outputManagerCLIClient.showHelp(12);
            } else {
              inputManagerCLIClient.askParamsUpdate();
            }
            break;
          case "remove":
            if (help) {
              outputManagerCLIClient.showHelp(13);
            } else {
              inputManagerCLIClient.askParamsRemove();
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