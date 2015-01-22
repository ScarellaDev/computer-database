package com.excilys.computerdatabase.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.service.ICompanyDBService;
import com.excilys.computerdatabase.service.IComputerDBService;
import com.excilys.computerdatabase.service.impl.CompanyDBServiceImpl;
import com.excilys.computerdatabase.service.impl.ComputerDBServiceImpl;

/**
* Controller managing HttpServletRequests on /addcomputer URL
* Used to add computers to the database through the webapp
*
* @author Jeremy SCARELLA
*/
@WebServlet("/addcomputer")
public class AddComputerController extends HttpServlet {

  private static final long         serialVersionUID  = 1L;

  /*
   * Instance of computerDBService
   */
  private static IComputerDBService computerDBService = ComputerDBServiceImpl.INSTANCE;

  /*
   * Instance of companyDBService
   */
  private static ICompanyDBService  companyDBService  = CompanyDBServiceImpl.INSTANCE;

  /*
   * LOGGER
   */
  private static final Logger       LOGGER            = LoggerFactory
                                                          .getLogger(AddComputerController.class);

  /**
   * Prints error messages
   */
  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {

    final List<Company> companies = companyDBService.getAll();
    req.setAttribute("companies", companies);

    // Get the JSP dispatcher
    final RequestDispatcher dispatcher = req.getRequestDispatcher("WEB-INF/views/addcomputer.jsp");

    // Forward the request
    dispatcher.forward(req, resp);
  }

  /**
   * Add computer to database using HttpServletRequest params {name (required), introduced, discontinued, companyId}
   */
  @Override
  protected void doPost(final HttpServletRequest httpReq, final HttpServletResponse httpResp)
      throws ServletException, IOException {

    final Computer computer = UtilControllerHttp.buildComputer(httpReq);

    if (computer != null) {
      if (computerDBService.addByComputer(computer) != null) {
        LOGGER.info("MySQL Info: computer INSERT SUCCESS: " + computer);
        httpResp.sendRedirect("dashboard");
      } else {
        LOGGER.warn("MySQL Error: computer INSERT FAIL");
        httpResp.sendRedirect("dashboard");
      }
    } else {
      doGet(httpReq, httpResp);
    }
  }
}
