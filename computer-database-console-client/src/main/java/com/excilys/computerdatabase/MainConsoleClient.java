package com.excilys.computerdatabase;

import com.excilys.computerdatabase.cli.CommandLineInterface;

/**
* Program's entry point
*
* @author Jeremy SCARELLA
*/
public class MainConsoleClient {
  public static void main(final String[] args) {
    final CommandLineInterface cli = new CommandLineInterface();
    cli.start();
  }
}