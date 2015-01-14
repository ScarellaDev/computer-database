package com.excilys.computerdatabase.ui.cli;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.service.CompanyService;
import com.excilys.computerdatabase.service.ComputerService;
import com.excilys.computerdatabase.service.ManagerService;

public class CommandLineInterface {
  private static String          userInput;
  private static ComputerService computerService = ManagerService.getInstance()
                                                     .getComputerService();
  private static CompanyService  companyService  = ManagerService.getInstance().getCompanyService();
  private static Scanner         sc;
  
    //  REGEX_DATE_EN : yyyy-MM-dd (separator = . || - || /)
    //  REGEX_DATE_FR : dd-MM-yyyy (separator = . || - || /)
    private static final String REGEX_DELIMITER = "(\\.|-|\\/)";
    private static final String REGEX_DATE_EN = "("
        + "((\\d{4})" + REGEX_DELIMITER + "(0[13578]|10|12)" + REGEX_DELIMITER + "(0[1-9]|[12][0-9]|3[01]))"
        + "|((\\d{4})" + REGEX_DELIMITER + "(0[469]|11)" + REGEX_DELIMITER + "([0][1-9]|[12][0-9]|30))"
        + "|((\\d{4})" + REGEX_DELIMITER + "(02)" + REGEX_DELIMITER  + "(0[1-9]|1[0-9]|2[0-8]))"
        + "|(([02468][048]00)" + REGEX_DELIMITER + "(02)" + REGEX_DELIMITER + "(29))"
        + "|(([13579][26]00)" + REGEX_DELIMITER + "(02)" + REGEX_DELIMITER + "(29))"
        + "|(([0-9][0-9][0][48])" + REGEX_DELIMITER + "(02)" + REGEX_DELIMITER + "(29))"
        + "|(([0-9][0-9][2468][048])" + REGEX_DELIMITER + "(02)" + REGEX_DELIMITER + "(29))"
        + "|(([0-9][0-9][13579][26])" + REGEX_DELIMITER + "(02)" + REGEX_DELIMITER + "(29))"
        + ")";
    private static final String REGEX_DATE_FR = "("
        + "((0[1-9]|[12][0-9]|3[01])" + REGEX_DELIMITER + "(0[13578]|10|12)" + REGEX_DELIMITER + "(\\d{4}))"
        + "|(([0][1-9]|[12][0-9]|30)" + REGEX_DELIMITER + "(0[469]|11)" + REGEX_DELIMITER + "(\\d{4}))"
        + "|((0[1-9]|1[0-9]|2[0-8])" + REGEX_DELIMITER + "(02)" + REGEX_DELIMITER + "(\\d{4}))"
        + "|((29)" + REGEX_DELIMITER + "(02)" + REGEX_DELIMITER + "([02468][048]00))"
        + "|((29)" + REGEX_DELIMITER + "(02)" + REGEX_DELIMITER + "([13579][26]00))"
        + "|((29)" + REGEX_DELIMITER + "(02)" + REGEX_DELIMITER + "([0-9][0-9][0][48]))"
        + "|((29)" + REGEX_DELIMITER + "(02)" + REGEX_DELIMITER + "([0-9][0-9][2468][048]))"
        + "|((29)" + REGEX_DELIMITER + "(02)" + REGEX_DELIMITER + "([0-9][0-9][13579][26]))"
        + ")";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

  public void start() {
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();

      if (userInput.toLowerCase().startsWith("ls ")) {
        userInput = userInput.substring(3);
        if (userInput.equals("computers")) {
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
            if (help) {
              showHelp(0);
            } else {
              displayMenu();
            }
            break;
          case "1":
            if (help) {
              showHelp(1);
            } else {
              showComputerList();
            }
            break;
          case "2":
            if (help) {
              showHelp(2);
            } else {
              showCompanyList();
            }
            break;
          case "3":
            if (help) {
              showHelp(3);
            } else {
              askParamsShow();
            }
            break;
          case "4":
            if (help) {
              showHelp(4);
            } else {
              askParamsAdd();
            }
            break;
          case "5":
            if (help) {
              showHelp(5);
            } else {
              askParamsUpdate();
            }
            break;
          case "6":
            if (help) {
              showHelp(6);
            } else {
              askParamsRemove();
            }
            break;
          case "menu":
            showHelp(7);
            break;
          case "ls":
            if (help) {
              showHelp(8);
            } else {
              askParamsLs();
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
              showHelp(9);
            } else {
              askParamsShow();
            }
            break;
          case "add":
            if (help) {
              showHelp(10);
            } else {
              askParamsAdd();
            }
            break;
          case "update":
            if (help) {
              showHelp(11);
            } else {
              askParamsUpdate();
            }
            break;
          case "remove":
            if (help) {
              showHelp(12);
            } else {
              askParamsRemove();
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

  public Boolean isComputerId(String idS) {
    Long id;
    Long max = computerService.getLastId();
    if (idS.matches("[0-9]+")) {
      id = new Long(idS);
      if (id < 1 || id > max) {
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }
  
  public Boolean isCompanyId(String idS) {
    Long id;
    if (idS.matches("[0-9]+")) {
      id = new Long(idS);
      if (id < 1 || id > 43) {
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }
  
  public Boolean isDate(String dateS) {
    if (dateS.matches(REGEX_DATE_EN)) {
      return true;
    } else {
      return false;
    }
  }

  public void askParamsLs() {
    userInput = null;
    System.out
        .println("-> You entered the ls command, please enter the option you want to display:\r\n- 'computers'\r\n- 'companies'");
    sc = new Scanner(System.in);
    userInput = sc.nextLine().trim().toLowerCase();
    switch (userInput) {
      case "computers":
        showComputerList();
        break;
      case "ls computers":
        showComputerList();
        break;
      case "1":
        showComputerList();
        break;
      case "companies":
        showCompanyList();
        break;
      case "ls companies":
        showCompanyList();
        break;
      case "2":
        showCompanyList();
        break;
      default:
        System.out.println("Non valid command.\r\n-> ls command aborted");
        break;
    }
  }

  public void askParamsShow() {
    System.out
        .println("-> You entered the show command, please enter the id of the computer you want to display (or press enter to quit command):");
    ;
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || userInput.equals("")) {
        System.out.println("-> show command aborted");
        return;
      } else {
        if (isComputerId(userInput)) {
          showComputer(userInput);
          break;
        } else {
          System.out.println("Please, enter a new valid id:");
          continue;
        }
      }
    }
  }

  public void askParamsAdd() {
    Computer.Builder builder = Computer.builder();
    
  //Get name
    System.out
    .println("-> You entered the add command:\r\n- please enter the name of the computer you want to add (or press enter to quit command):");
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || "".equals(userInput) || "null".equals(userInput)) {
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
      if (userInput.isEmpty() || userInput == null || "".equals(userInput) || "null".equals(userInput)) {
        break;
      } else {
        if(isDate(userInput)) {
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
      if (userInput.isEmpty() || userInput == null || "".equals(userInput) || "null".equals(userInput)) {
        break;
      } else {
        if(isDate(userInput)) {
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
    .println("- please enter the id (between [1, 43]) of the company of the computer you want to add (press enter or type 'null' to skip this value):");
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || "".equals(userInput) || "null".equals(userInput)) {
        break;
      } else {
        if(isCompanyId(userInput)) {
          builder.company(companyService.getById(new Long(userInput)));
          break;
        } else {
          System.out.println("Please, enter a new valid id (between [1, 43]):");
          continue;
        }
      }
    }
    showAddResult(builder.build());
  }
  
  public void askParamsUpdate() {
    Computer.Builder builder = Computer.builder();
  
  //Get id
    System.out
    .println("-> You entered the update command:\r\n- please enter the id of the computer you want to update (or press enter to quit command):");
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || "".equals(userInput) || "null".equals(userInput)) {
        System.out.println("-> update command aborted");
        return;
      } else {
        if(isComputerId(userInput)) {
          builder.id(new Long(userInput));
          break;
        } else {
          System.out.println("Please, enter a new valid id (between [1, " + computerService.getLastId() + "]):");
          continue;
        }
      }
    }  
    
  //Get name
    System.out
    .println("- please enter the name of the computer if you want to update it (press enter or type 'null' to skip value):");
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || "".equals(userInput) || "null".equals(userInput)) {
        break;
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
      if (userInput.isEmpty() || userInput == null || "".equals(userInput) || "null".equals(userInput)) {
        break;
      } else {
        if(isDate(userInput)) {
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
      if (userInput.isEmpty() || userInput == null || "".equals(userInput) || "null".equals(userInput)) {
        break;
      } else {
        if(isDate(userInput)) {
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
    .println("- please enter the id (between [1, 43]) of the company of the computer if you want to update it (press enter or type 'null' to skip this value):");
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || "".equals(userInput) || "null".equals(userInput)) {
        break;
      } else {
        if(isCompanyId(userInput)) {
          builder.company(companyService.getById(new Long(userInput)));
          break;
        } else {
          System.out.println("Please, enter a new valid id (between [1, 43]):");
          continue;
        }
      }
    }
    showUpdateResult(builder.build());
  }
  
  public void askParamsRemove() {
    System.out
        .println("-> You entered the remove command, please enter the id of the computer you want to remove from the DB (or press enter to quit command):");
    ;
    while (true) {
      userInput = null;
      sc = new Scanner(System.in);
      userInput = sc.nextLine().trim().toLowerCase();
      if (userInput.isEmpty() || userInput == null || userInput.equals("")) {
        System.out.println("-> remove command aborted");
        return;
      } else {
        if (isComputerId(userInput)) {
          showRemoveResult(userInput);
          break;
        } else {
          System.out.println("Please, enter a new valid id:");
          continue;
        }
      }
    }
  }

  public void showComputerList() {
    //Print computer list from DB
    List<Computer> computers = new ArrayList<Computer>();
    computers = computerService.getAll();
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

  public void showComputer(String idS) {
    //Print the details of the computer with id=idS
    Long max = computerService.getLastId();

    if (idS.matches("[0-9]+")) {
      Long id = new Long(idS);

      if (id < 1 || id > max) {
        System.out.println("The id you entered is incorrect, it must be within [1, "
            + max.toString() + "].\r\n");
      } else {
        Computer computer = computerService.getById(new Long(idS));
        if (computer == null) {
          System.out.println("MySQL Error: computer not found.\r\n");
        } else {
          System.out.println("Here are the details of the computer you requested:");
          System.out.println(computer.toString());
        }
      }
    } else {
      System.out.println("The id you entered is incorrect, it must be within [1, " + max.toString()
          + "].\r\n");
    }
  }

  public void showAddResult(String[] params) {
    Computer computer = null;
    computer = computerService.addByString(params);
    if (computer == null) {
      System.out.println("MySQL Error: your computer could not be added to the DB.\r\n");
    } else {
      System.out.println("Your computer was successfully added to the DB :");
      System.out.println(computer.toString());
    }
  }
  
  public void showAddResult(Computer computer) {
    computer = computerService.addByComputer(computer);
    if (computer == null) {
      System.out.println("MySQL Error: your computer could not be added to the DB.\r\n");
    } else {
      System.out.println("Your computer was successfully added to the DB :");
      System.out.println(computer.toString());
    }
  }

  public void showUpdateResult(String[] params) {
    Computer computer = null;
    computer = computerService.updateByString(params);
    if (computer == null) {
      System.out.println("MySQL Error: your computer could not be updated in the DB.\r\n");
    } else {
      System.out.println("Your computer was updated successfully in the DB :");
      System.out.println(computer.toString());
    }
  }
  
  public void showUpdateResult(Computer computer) {
    computer = computerService.updateByComputer(computer);
    if (computer == null) {
      System.out.println("MySQL Error: your computer could not be updated in the DB.\r\n");
    } else {
      System.out.println("Your computer was updated successfully in the DB :");
      System.out.println(computer.toString());
    }
  }

  public void showRemoveResult(String idS) {
    //Print the details of the computer with id=idS
    Long max = computerService.getLastId();

    if (idS.matches("[0-9]+")) {
      Long id = new Long(idS);

      if (id < 1 || id > max) {
        System.out.println("The id you entered is incorrect, it must be within [1, "
            + max.toString() + "].\r\n");
      } else {
        Computer computer = null;
        computer = computerService.removeById(new Long(idS));
        if (computer == null) {
          System.out.println("MySQL Error: computer could not be removed from DB.\r\n");
        } else {
          System.out.println("Your computer was successfully removed from the DB :");
          System.out.println(computer.toString());
        }
      }
    } else {
      System.out.println("The id you entered is incorrect, it must be within [1, " + max.toString()
          + "].\r\n");
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
}