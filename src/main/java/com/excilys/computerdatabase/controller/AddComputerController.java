package com.excilys.computerdatabase.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.service.ICompanyService;
import com.excilys.computerdatabase.service.IComputerService;
import com.excilys.computerdatabase.service.impl.CompanyServiceJDBC;
import com.excilys.computerdatabase.service.impl.ComputerServiceJDBC;

/**
* Controller managing HttpServletRequests on /addcomputer URL
* Used to add computers to the database through the webapp
*
* @author Jeremy SCARELLA
*/
@WebServlet("/addcomputer")
public class AddComputerController extends HttpServlet {

  private static final long       serialVersionUID = 1L;

  /*
   * Instance of computerService
   */
  private static IComputerService computerService  = ComputerServiceJDBC.INSTANCE;

  /*
   * Instance of companyService
   */
  private static ICompanyService  companyService   = CompanyServiceJDBC.INSTANCE;

  /**
   * Prints error messages
   */
  @Override
  protected void doGet(final HttpServletRequest httpReq, final HttpServletResponse httpResp)
      throws ServletException, IOException {

    final List<Company> companies = companyService.getAll();
    httpReq.setAttribute("companies", companies);

    // Get the JSP dispatcher
    final RequestDispatcher dispatcher = httpReq
        .getRequestDispatcher("WEB-INF/views/addcomputer.jsp");

    // Forward the httpRequest
    dispatcher.forward(httpReq, httpResp);
  }

  /**
   * Add computer to database using HttpServletRequest params {name (required), introduced, discontinued, companyId}
   */
  @Override
  protected void doPost(final HttpServletRequest httpReq, final HttpServletResponse httpResp)
      throws ServletException, IOException {

    final Computer computer = UtilControllerHttp.buildComputer(httpReq);

    if (computer != null) {
      if (computerService.addByComputer(computer) != null) {
        httpResp.sendRedirect("dashboard");
      } else {
        doGet(httpReq, httpResp);
      }
    } else {
      doGet(httpReq, httpResp);
    }
  }
}
