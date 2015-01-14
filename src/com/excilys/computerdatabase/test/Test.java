package com.excilys.computerdatabase.test;

public class Test {
  //  public static void main(String[] args) {
  //    // TODO Auto-generated method stub
  //
  //    //Open new console
  //    //new Console();
  //    
  ////    String s;
  ////    Scanner in = new Scanner(System.in);
  ////    System.out.println("Enter a string: ");
  ////    s = in.nextLine();
  ////    System.out.println("You entered string "+s);
  //    
  //
  //    //Print computer list from DB
  //    ComputerService computerService = new ComputerService();
  //    List<Computer> computers = new ArrayList<Computer>();
  //    computers = computerService.getAllComputers();
  //    if (computers != null) {
  //      //      for (Computer computer : computers) {
  //      //         System.out.println(computer.toString());
  //      //      }
  //      System.out.println(computers.get(computers.size() - 1).toString());
  //    } else {
  //      System.out.println("computers = null");
  //    }
  //
  //    //    //Print company list from DB
  //    //    CompanyService companyService = new CompanyService();
  //    //    List<Company> companies = new ArrayList<Company>();
  //    //    companies = companyService.getAllCompanies();
  //    //    if (companies != null) {
  //    //      for (Company company : companies) {
  //    //        System.out.println(company.toString());
  //    //      }
  //    //    } else {
  //    //      System.out.println("companies = null");
  //    //    }
  //
  //    //Add a computer to DB and print the computer list from DB
  //    String params[] = { "Mon PC", "2015-01-12", "null", "21" };
  //    try {
  //      computerService.addComputer(params);
  //    } catch (InvalidArgsNumberException e) {
  //      // TODO Auto-generated catch block
  //      e.printStackTrace();
  //    } catch (InvalidCompanyIdException e) {
  //      // TODO Auto-generated catch block
  //      e.printStackTrace();
  //    }
  //    computers.clear();
  //    computers = computerService.getAllComputers();
  //    if (computers != null) {
  //      //      for (Computer computer : computers) {
  //      //        System.out.println(computer.toString());
  //      //      }
  //      System.out.println(computers.get(computers.size() - 1).toString());
  //    } else {
  //      System.out.println("computers = null");
  //    }
  //
  //    //Update the last computer in DB and print the computer list from DB
  //    String params2[] = { computerService.getLastId().toString(), "null", "2015-01-13", "2015-01-14" };
  //    try {
  //      computerService.setComputer(params2);
  //    } catch (InvalidArgsNumberException e) {
  //      // TODO Auto-generated catch block
  //      e.printStackTrace();
  //    } catch (InvalidCompanyIdException e) {
  //      // TODO Auto-generated catch block
  //      e.printStackTrace();
  //    }
  //    computers.clear();
  //    computers = computerService.getAllComputers();
  //    if (computers != null) {
  //      //      for (Computer computer : computers) {
  //      //        System.out.println(computer.toString());
  //      //      }
  //      System.out.println(computers.get(computers.size() - 1).toString());
  //    } else {
  //      System.out.println("computers = null");
  //    }
  //
  //    //Remove the last computer from DB and print the computer list from DB
  //    computerService.removeLastComputer();
  //    computers.clear();
  //    computers = computerService.getAllComputers();
  //    if (computers != null) {
  //      //      for (Computer computer : computers) {
  //      //        System.out.println(computer.toString());
  //      //      }
  //      System.out.println(computers.get(computers.size() - 1).toString());
  //    } else {
  //      System.out.println("computers = null");
  //    }
  //  }
}