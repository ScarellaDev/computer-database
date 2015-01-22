package com.excilys.computerdatabase.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.IComputerDBService;
import com.excilys.computerdatabase.service.impl.ComputerDBServiceImpl;
import com.excilys.computerdatabase.validator.StringValidation;

/**
* Controller managing HttpServletRequests on /dashboard URL
* Main interface
* Displays computer list from database and enable page navigation, name search, creation, update and deletion of computers
*
* @author Jeremy SCARELLA
*/
@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {
  private static final long         serialVersionUID  = 1L;

  /*
   * Instance of computerDBService
   */
  private static IComputerDBService computerDBService = ComputerDBServiceImpl.INSTANCE;

  /**
   * Displays pages of computer lists from database using HttpServletRequest params {pageIndex, nbElementsPerPage}
   */
  @Override
  protected void doGet(final HttpServletRequest httpReq, final HttpServletResponse httpResp)
      throws ServletException, IOException {

    Page<Computer> page = new Page<Computer>();

    String search = null;
    search = httpReq.getParameter("search");
    page.setSearch(search);

    //Get pageIndex and set it
    final String intString = httpReq.getParameter("pageIndex");
    int pageIndex = 0;
    if (StringValidation.isPositiveInt(intString)) {
      pageIndex = Integer.valueOf(intString);
    }
    if (pageIndex < 1) {
      page.setPageIndex(1);
    } else {
      page.setPageIndex(pageIndex);
    }

    //Get nbElementsPerPage and set it
    final String nbElementsPerPageString = httpReq.getParameter("nbElementsPerPage");
    int nbElementsPerPage = 0;
    if (StringValidation.isPositiveInt(nbElementsPerPageString)) {
      nbElementsPerPage = Integer.valueOf(nbElementsPerPageString);
    }
    if (nbElementsPerPage < 10) {
      page.setNbElementsPerPage(10);
    } else {
      page.setNbElementsPerPage(nbElementsPerPage);
    }

    //Retrieve the list of computers to display
    page = computerDBService.getPagedList(page);
    httpReq.setAttribute("page", page);

    // Get the JSP dispatcher
    final RequestDispatcher dispatcher = httpReq
        .getRequestDispatcher("WEB-INF/views/dashboard.jsp");

    // Forward the httpRequest
    dispatcher.forward(httpReq, httpResp);
  }
}