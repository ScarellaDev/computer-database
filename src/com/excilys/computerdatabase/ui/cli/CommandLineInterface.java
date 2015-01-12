package com.excilys.computerdatabase.ui.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.excilys.computerdatabase.exception.InvalidArgsNumberException;
import com.excilys.computerdatabase.exception.InvalidCompanyIdException;
import com.excilys.computerdatabase.model.Company;
import com.excilys.computerdatabase.model.Computer;
import com.excilys.computerdatabase.service.CompanyService;
import com.excilys.computerdatabase.service.ComputerService;

public class CommandLineInterface {
  private static String          userInput;
  private static ComputerService computerService = new ComputerService();
  private static CompanyService  companyService  = new CompanyService();

  public void displayMenu() {
    StringBuffer menu = new StringBuffer("* * * * * MENU * * * * *\r\n");
    menu.append("0. Show menu (cmd: 'menu')\r\n");
    menu.append("1. Show list of all computers (cmd: 'ls computers')\r\n");
    menu.append("2. Show list of all companies (cmd: 'ls companies')\r\n");
    menu.append("3. Show the details of one computer (cmd: 'show id')\r\n");
    menu.append("4. Add a computer (cmd: 'add name introduced discontinued companyId')\r\n");
    menu.append("5. Update a computer (cmd: 'update id name introduced discontinued companyId')\r\n");
    menu.append("6. Remove a computer (cmd: 'remove id')\r\n");
    menu.append("\r\nINFO: to display the help concerning a command, just type 'help commandNumber' or 'help commandName'\r\n");
    menu.append("INFO2: in order to quit the program, just type 'q' or 'quit' or 'exit'\r\n");
    System.out.println(menu);
  }

  public void newCommand() {
    while (true) {
      userInput = null;
      Scanner in = new Scanner(System.in);
      userInput = in.nextLine().trim();

      if (userInput.toLowerCase().startsWith("ls ")) {
        userInput = userInput.substring(3);
        if(userInput.equals("computers")) {
          showComputerList();
        } else if (userInput.equals("companies")) {
          showCompanyList();
        } else {
          System.out.println("Non valid command. Please, try again.\r\n");
        }
      } else if (userInput.toLowerCase().startsWith("3 ")) {
        userInput = userInput.substring(2);
        showComputer(userInput);
      } else if (userInput.toLowerCase().startsWith("show ")) {
        userInput = userInput.substring(5);
        showComputer(userInput);
      } else if (userInput.toLowerCase().startsWith("4 ")) {
        userInput = userInput.substring(2);
        showAddResult(userInput.split("\\s+"));
      } else if (userInput.toLowerCase().startsWith("add ")) {
        userInput = userInput.substring(4);
        showAddResult(userInput.split("\\s+"));
      } else if (userInput.toLowerCase().startsWith("5 ")) {
        userInput = userInput.substring(2);
        showUpdateResult(userInput.split("\\s+"));
      } else if (userInput.toLowerCase().startsWith("update ")) {
        userInput = userInput.substring(7);
        showUpdateResult(userInput.split("\\s+"));
      } else if (userInput.toLowerCase().startsWith("6 ")) {
        userInput = userInput.substring(2);
        showRemoveResult(userInput);
      } else if (userInput.toLowerCase().startsWith("remove ")) {
        userInput = userInput.substring(7);
        showRemoveResult(userInput);
      } else {
        Boolean help = false;
        if (userInput.toLowerCase().startsWith("help ")) {
          help = true;
          userInput = userInput.substring(5);
        }
        switch (userInput) {
          case "0":
            if(help) {
              showHelp(0);
            } else {
              displayMenu();
            }
            break;
          case "1":
            if(help) {
              showHelp(1);
            } else {
              showComputerList();
            }
            break;
          case "2":
            if(help) {
              showHelp(2);
            } else {
              showCompanyList();
            }
            break;
          case "3":
            showHelp(3);
            break;
          case "4":
            showHelp(4);
            break;
          case "5":
            showHelp(5);
            break;
          case "6":
            showHelp(6);
            break;
          case "menu":
            showHelp(7);
            break;
          case "ls":
            showHelp(8);
            break;
          case "show":
            showHelp(9);
            break;
          case "add":
            showHelp(10);
            break;
          case "update":
            showHelp(11);
            break;
          case "remove":
            showHelp(12);
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

  public void showComputerList() {
    //Print computer list from DB
    List<Computer> computers = new ArrayList<Computer>();
    computers = computerService.getAllComputers();
    if (computers != null) {
      System.out.println("Here is a list of all the computers in the DB:\r\n");
      for (Computer computer : computers) {
        System.out.println(computer.toString());
      }
    } else {
      System.out.println("No computers found.");
    }
  }

  public void showCompanyList() {
    //Print company list from DB
    List<Company> companies = new ArrayList<Company>();
    companies = companyService.getAllCompanies();
    if (companies != null) {
      System.out.println("Here is a list of all the companies in the DB:\r\n");
      for (Company company : companies) {
        System.out.println(company.toString());
      }
    } else {
      System.out.println("No company found");
    }
  }
  
  public void showComputer(String idS) {
    //Print the details of the computer with id=idS
    Long max = computerService.getLastId();
    
    if (idS.matches("[0-9]+")) {
      Long id = new Long(idS);
      
      if (id < 1 || id > max) {
        System.out.println("The id you entered is incorrect, it must be within [1, " + max.toString() + "].\r\n");
      } else {
        Computer computer = computerService.getComputer(new Long(idS));
        if(computer == null) {
          System.out.println("MySQL Error: computer not found.\r\n");
        } else {
          System.out.println("Here are the details of the computer you requested:\r\n");
          System.out.println(computer.toString());
        }
      }
    } else {
      System.out.println("The id you entered is incorrect, it must be within [1, " + max.toString() + "].\r\n");
    }
  }
  
  public void showAddResult(String[] params) {
    try {
      if(computerService.addComputer(params)) {
        System.out.println("Your computer was added to the DB :\r\n");
        System.out.println(computerService.getComputer(computerService.getLastId()).toString());
      } else {
        System.out.println("MySQL Error: your computer could not be added to the DB.\r\n");
      }
    } catch (InvalidArgsNumberException e) {
      // TODO Auto-generated catch block
      System.out.println(e.getMessage());
      return;
    } catch (InvalidCompanyIdException e) {
      // TODO Auto-generated catch block
      System.out.println(e.getMessage());
      return;
    }
  }
  
  public void showUpdateResult(String[] params) {
    try {
      if(computerService.setComputer(params)) {
        System.out.println("Your computer was updated in the DB :\r\n");
        System.out.println(computerService.getComputer(new Long(params[0])).toString());
      } else {
        System.out.println("MySQL Error: your computer could not be updated in the DB.\r\n");
      }
    } catch (InvalidArgsNumberException e) {
      // TODO Auto-generated catch block
      System.out.println(e.getMessage());
      return;
    } catch (InvalidCompanyIdException e) {
      // TODO Auto-generated catch block
      System.out.println(e.getMessage());
      return;
    }
  }
  
  public void showRemoveResult(String idS) {
  //Print the details of the computer with id=idS
    Long max = computerService.getLastId();
    
    if (idS.matches("[0-9]+")) {
      Long id = new Long(idS);
      
      if (id < 1 || id > max) {
        System.out.println("The id you entered is incorrect, it must be within [1, " + max.toString() + "].\r\n");
      } else {
        if (computerService.removeComputer(new Long(idS))) {
          System.out.println("The specified computer was removed from the DB");
        } else {
          System.out.println("MySQL Error: computer coul not be removed from DB.\r\n");
        }
      }
    } else {
      System.out.println("The id you entered is incorrect, it must be within [1, " + max.toString() + "].\r\n");
    }
  }

  public void showHelp(int choice) {
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
        System.out.println("- id is an integer between [1, "
            + computerService.getLastId().toString() + "]\r\n");
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
        System.out.println("- id is an integer between [1, "
            + computerService.getLastId().toString() + "]");
        System.out.println("- companyId is an integer between [1, 43]");
        System.out.println("- if you do not want to set a value, refer to it as null\r\n");
        break;
      case 6:
        System.out
            .println("HELP command '6': type '6 id' or 'remove id' in order to remove a specific computer from the DB.\r\n");
        break;
      case 7:
        System.out.println("HELP command 'menu':");
        System.out.println("- type '0' or 'menu' in order to display the menu.\r\n");
        break;
      case 8:
        System.out.println("HELP command 'ls':");
        System.out
            .println("- type '1' or 'ls computers' in order to display the list of all the computers in DB.");
        System.out
            .println("- type '2' or 'ls companies' in order to display the list of all the computers in DB.\r\n");
        break;
      case 9:
        System.out.println("HELP command 'show':");
        System.out
            .println("- type '3 id' or 'show id' in order to display the details of a specific computer.");
        System.out.println("- id is an integer between [1, "
            + computerService.getLastId().toString() + "]\r\n");
        break;
      case 10:
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
      case 11:
        System.out.println("HELP command 'update':");
        System.out
            .println("- type '5 id name introduced discontinued companyId' or 'update id name introduced discontinued companyId' in order to update an existing computer in the DB.");
        System.out
            .println("- id is mandatory whereas name and introduced and discontinued and companyId are optional");
        System.out
            .println("- introduced and discontinued are dates with the following format: 'yyyy-MM-dd'");
        System.out.println("- id is an integer between [1, "
            + computerService.getLastId().toString() + "]");
        System.out.println("- companyId is an integer between [1, 43]");
        System.out.println("- if you do not want to set a value, refer to it as null\r\n");
        break;
      case 12:
        System.out.println("HELP command 'remove':");
        System.out
            .println("- type '6 id' or 'remove id' in order to remove a specific computer from the DB.\r\n");
        break;
      default:
        System.out.println("Non valid command. Please, try again.\r\n");
        break;
    }
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    CommandLineInterface cli = new CommandLineInterface();

    cli.displayMenu();
    cli.newCommand();
  }
}