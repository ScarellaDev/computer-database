package com.excilys.computerdatabase.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.computerdatabase.service.IComputerService;

/**
* Controller managing HttpServletRequests on /deletecomputer URL
* Used to remove computers from the database through the webapp
*
* @author Jeremy SCARELLA
*/
@WebServlet("/deletecomputer")
public class DeleteComputerController extends HttpServlet {

  private static final long    serialVersionUID      = 1L;

  /*
   * Instance of ComputerServiceJDBC
   */
  @Autowired
  private IComputerService     computerServiceJDBC;

  /*
   * POSITIVE LONG PATTERN
   */
  private static final Pattern POSITIVE_LONG_PATTERN = Pattern.compile("\\d{1,19}");

  /**
   * Override of the init() method of GenericServlet in order to link the Servlet context to the Spring one
   */
  @Override
  public void init() throws ServletException {
    super.init();
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
  }

  /**
   * Remove computer(s) from the database using HttpServletRequest params (rows checked by the user)
   */
  @Override
  protected void doPost(final HttpServletRequest httpReq, final HttpServletResponse httpResp)
      throws ServletException, IOException {

    //Get the String containing the Ids of the computers to delete
    String selection = httpReq.getParameter("selection");

    //Create a matcher to find the positives longs in the String
    Matcher m = POSITIVE_LONG_PATTERN.matcher(selection);

    List<Long> idList = new ArrayList<Long>();

    //For each long found, delete the computer
    while (m.find()) {
      idList.add(new Long(m.group()));
    }

    if (idList.isEmpty()) {
      return;
    }
    computerServiceJDBC.removeByIdList(idList);

    httpResp.sendRedirect("dashboard");
  }

}