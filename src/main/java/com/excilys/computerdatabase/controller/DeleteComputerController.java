package com.excilys.computerdatabase.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Controller managing HttpServletRequests on /deletecomputer URL
* Used to remove computers from the database through the webapp
*
* @author Jeremy SCARELLA
*/
@WebServlet("/deletecomputer")
public class DeleteComputerController extends HttpServlet {

  private static final long serialVersionUID = 1L;

  /**
   * Remove computer(s) from the database using HttpServletRequest params (rows checked by the user)
   */
  @Override
  protected void doPost(final HttpServletRequest httpReq, final HttpServletResponse httpResp)
      throws ServletException, IOException {

    UtilControllerHttp.deleteComputers(httpReq);
    httpResp.sendRedirect("dashboard");
  }

}