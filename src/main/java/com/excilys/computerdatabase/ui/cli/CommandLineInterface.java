package com.excilys.computerdatabase.ui.cli;

import java.util.Scanner;

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
    OutputManagerCLI.showMenu();
    while (true) {
      mainUserInput = null;
      mainSc = new Scanner(System.in);
      mainUserInput = mainSc.nextLine().trim().toLowerCase();

      if (mainUserInput.toLowerCase().startsWith("ls ")) {
        mainUserInput = mainUserInput.substring(3);
        if (mainUserInput.equals("computers")) {
          OutputManagerCLI.showComputerPage();
        } else if (mainUserInput.equals("companies")) {
          OutputManagerCLI.showCompanyPage();
        } else {
          System.out.println("Non valid command. Please, try again.\r\n");
        }
      } else if (mainUserInput.toLowerCase().startsWith("3 ")) {
        mainUserInput = mainUserInput.substring(2);
        OutputManagerCLI.showComputer(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("show ")) {
        mainUserInput = mainUserInput.substring(5);
        OutputManagerCLI.showComputer(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("4 ")) {
        mainUserInput = mainUserInput.substring(2);
        OutputManagerCLI.showAddResult(mainUserInput.split("\\s+"));
      } else if (mainUserInput.toLowerCase().startsWith("add ")) {
        mainUserInput = mainUserInput.substring(4);
        OutputManagerCLI.showAddResult(mainUserInput.split("\\s+"));
      } else if (mainUserInput.toLowerCase().startsWith("5 ")) {
        mainUserInput = mainUserInput.substring(2);
        OutputManagerCLI.showUpdateResult(mainUserInput.split("\\s+"));
      } else if (mainUserInput.toLowerCase().startsWith("update ")) {
        mainUserInput = mainUserInput.substring(7);
        OutputManagerCLI.showUpdateResult(mainUserInput.split("\\s+"));
      } else if (mainUserInput.toLowerCase().startsWith("6 ")) {
        mainUserInput = mainUserInput.substring(2);
        OutputManagerCLI.showRemoveComputerResult(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("remove computer ")) {
        mainUserInput = mainUserInput.substring(16);
        OutputManagerCLI.showRemoveComputerResult(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("7 ")) {
        mainUserInput = mainUserInput.substring(2);
        OutputManagerCLI.showRemoveCompanyResult(mainUserInput);
      } else if (mainUserInput.toLowerCase().startsWith("remove company ")) {
        mainUserInput = mainUserInput.substring(15);
        OutputManagerCLI.showRemoveCompanyResult(mainUserInput);
      } else {
        Boolean help = false;
        if (mainUserInput.toLowerCase().startsWith("help ")) {
          help = true;
          mainUserInput = mainUserInput.substring(5);
        }
        switch (mainUserInput) {
          case "0":
            if (help) {
              OutputManagerCLI.showHelp(0);
            } else {
              OutputManagerCLI.showMenu();
            }
            break;
          case "1":
            if (help) {
              OutputManagerCLI.showHelp(1);
            } else {
              OutputManagerCLI.showComputerPage();
            }
            break;
          case "2":
            if (help) {
              OutputManagerCLI.showHelp(2);
            } else {
              OutputManagerCLI.showCompanyPage();
            }
            break;
          case "3":
            if (help) {
              OutputManagerCLI.showHelp(3);
            } else {
              InputManagerCLI.askParamsShow();
            }
            break;
          case "4":
            if (help) {
              OutputManagerCLI.showHelp(4);
            } else {
              InputManagerCLI.askParamsAdd();
            }
            break;
          case "5":
            if (help) {
              OutputManagerCLI.showHelp(5);
            } else {
              InputManagerCLI.askParamsUpdate();
            }
            break;
          case "6":
            if (help) {
              OutputManagerCLI.showHelp(6);
            } else {
              InputManagerCLI.askParamsRemoveComputer();
            }
            break;
          case "7":
            if (help) {
              OutputManagerCLI.showHelp(7);
            } else {
              InputManagerCLI.askParamsRemoveCompany();
            }
            break;
          case "menu":
            OutputManagerCLI.showHelp(8);
            break;
          case "ls":
            if (help) {
              OutputManagerCLI.showHelp(9);
            } else {
              InputManagerCLI.askParamsLs();
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
              OutputManagerCLI.showHelp(10);
            } else {
              InputManagerCLI.askParamsShow();
            }
            break;
          case "add":
            if (help) {
              OutputManagerCLI.showHelp(11);
            } else {
              InputManagerCLI.askParamsAdd();
            }
            break;
          case "update":
            if (help) {
              OutputManagerCLI.showHelp(12);
            } else {
              InputManagerCLI.askParamsUpdate();
            }
            break;
          case "remove":
            if (help) {
              OutputManagerCLI.showHelp(13);
            } else {
              InputManagerCLI.askParamsRemove();
            }
            break;
          case "q":
            System.out.println("Thank you for using our CLI. Goodbye!");
            return;
          case "quit":
            System.out.println("Thank you for using our CLI. Goodbye!");
            return;
          case "exit":
            System.out.println("Thank you for using our CLI. Goodbye!");
            return;
          default:
            System.out.println("Non valid command. Please, try again.\r\n");
            break;
        }
      }
    }
  }
}