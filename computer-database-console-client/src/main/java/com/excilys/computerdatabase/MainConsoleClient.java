package com.excilys.computerdatabase;

import com.excilys.computerdatabase.cli.CommandLineInterfaceClient;

/**
* Program's entry point
*
* @author Jeremy SCARELLA
*/
public class MainConsoleClient {
  public static void main(final String[] args) {
    final CommandLineInterfaceClient cli = new CommandLineInterfaceClient();
    cli.start();
  }
}