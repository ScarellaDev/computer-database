package com.excilys.computerdatabase;

import com.excilys.computerdatabase.ui.cli.CommandLineInterface;

/**
* Program's entry point.
*
* @author Jeremy SCARELLA
*/
public class Main {
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    CommandLineInterface cli = new CommandLineInterface();

    cli.displayMenu();
    cli.start();
  }
}