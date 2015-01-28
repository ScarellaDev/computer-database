package com.excilys.computerdatabase.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.service.ICompanyService;
import com.excilys.computerdatabase.service.IComputerService;
import com.excilys.computerdatabase.validator.StringValidation;

/**
* Controller managing HttpServletRequests on /addcomputer URL
* Used to add computers to the database through the webapp
*
* @author Jeremy SCARELLA
*/
@WebServlet("/addcomputer")
public class AddComputerController extends HttpServlet {

  private static final long serialVersionUID = 1L;

  /*
   * Instance of ComputerServiceJDBC
   */
  @Autowired
  private IComputerService  computerServiceJDBC;

  /*
   * Instance of CompanyServiceJDBC
   */
  @Autowired
  private ICompanyService   companyServiceJDBC;

  /**
   * Override of the init() method of GenericServlet in order to link the Servlet context to the Spring one
   */
  @Override
  public void init() throws ServletException {
    super.init();
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
  }

  /**
   * Prints error messages
   */
  @Override
  protected void doGet(final HttpServletRequest httpReq, final HttpServletResponse httpResp)
      throws ServletException, IOException {

    final List<Company> companies = companyServiceJDBC.getAll();
    httpReq.setAttribute("companies", companies);

    // Get the JSP dispatcher
    final RequestDispatcher dispatcher = httpReq
        .getRequestDispatcher("WEB-INF/views/addcomputer.jsp");

    // Forward the httpRequest
    dispatcher.forward(httpReq, httpResp);
  }

  /**
   * Add computer to database using HttpServletRequest params {name (httpRequired), introduced, discontinued, companyId}
   */
  @Override
  protected void doPost(final HttpServletRequest httpReq, final HttpServletResponse httpResp)
      throws ServletException, IOException {

    final Map<String, String> errorMap = new HashMap<String, String>();

    final ComputerDto.Builder builder = ComputerDto.builder()
        .name(httpReq.getParameter("name").trim())
        .introduced(httpReq.getParameter("introduced").trim())
        .discontinued(httpReq.getParameter("discontinued").trim());

    if (StringValidation.isPositiveLong(httpReq.getParameter("companyId").trim())) {
      builder.companyId(Long.valueOf(httpReq.getParameter("companyId").trim()));
    }

    final ComputerDto computerDto = builder.build();

    if (ComputerDtoConverter.validate(computerDto, errorMap)) {
      computerServiceJDBC.addByComputer(ComputerDtoConverter.toComputer(computerDto));
      httpResp.sendRedirect("dashboard");
    } else {
      httpReq.setAttribute("error", errorMap);
      doGet(httpReq, httpResp);
    }
  }
}
