package com.excilys.computerdatabase.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
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
import com.excilys.computerdatabase.validator.StringValidation;

/**
* Controller managing HttpServletRequests on /editcomputer URL
* Updates a computer in database with the parameters given through URL
*
* @author Jeremy SCARELLA
*/
@WebServlet("/editcomputer")
public class EditComputerController extends HttpServlet {

  private static final long              serialVersionUID    = 1L;

  /*
   * Instance of computerDBService
   */
  private static IComputerDBService      computerDBService   = ComputerDBServiceImpl.INSTANCE;

  /*
   * Instance of companyDBService
   */
  private static ICompanyDBService       companyDBService    = CompanyDBServiceImpl.INSTANCE;

  /*
   * LOGGER
   */
  private static final Logger            LOGGER              = LoggerFactory
                                                                 .getLogger(EditComputerController.class);

  /*
   * DATE TIME FORMATTER
   */
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
                                                                 .ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
  * Prints error message
  */
  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    Long id = 0L;
    final String idS = req.getParameter("id");
    if (StringValidation.isPositiveLong(idS)) {
      id = Long.valueOf(req.getParameter("id"));

      final Computer computer = computerDBService.getById(id);
      req.setAttribute("computer", computer);
    }

    final List<Company> companies = companyDBService.getAll();
    req.setAttribute("companies", companies);

    // Get the JSP dispatcher
    final RequestDispatcher dispatcher = req.getRequestDispatcher("WEB-INF/views/editcomputer.jsp");
    // Forward the request
    dispatcher.forward(req, resp);
  }

  /**
   * Update computer in database using HttpServletRequest params {id (required), name, introduced, discontinued, companyId}
   */
  @Override
  protected void doPost(final HttpServletRequest httpReq, final HttpServletResponse httpResp)
      throws ServletException, IOException {

    Computer computer = UtilControllerHttp.buildComputerWithId(httpReq);
    if (computer != null) {
      if (computerDBService.updateByComputer(computer) != null) {
        LOGGER.info("MySQL Info: computer UPDATE SUCCESS: " + computer);
        httpResp.sendRedirect("dashboard");
      } else {
        LOGGER.warn("MySQL Error: computer UPDATE FAIL");
        httpResp.sendRedirect("dashboard");
      }
    } else {
      doGet(httpReq, httpResp);
    }
  }
}
