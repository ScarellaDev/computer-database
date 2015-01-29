package com.excilys.computerdatabase;

import com.excilys.computerdatabase.ui.cli.CommandLineInterface;

/**
* Program's entry point
*
* @author Jeremy SCARELLA
*/
public class Main {
  public static void main(final String[] args) {
    final CommandLineInterface cli = new CommandLineInterface();
    cli.start();
  }
}