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
* Controller managing HttpServletRequests on /editcomputer URL
* Updates a computer in database with the parameters given through URL
*
* @author Jeremy SCARELLA
*/
@WebServlet("/editcomputer")
public class EditComputerController extends HttpServlet {

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
  * Prints error message
  */
  @Override
  protected void doGet(final HttpServletRequest httpReq, final HttpServletResponse httpResp)
      throws ServletException, IOException {
    Long id = 0L;
    final String idS = httpReq.getParameter("id");
    if (StringValidation.isPositiveLong(idS)) {
      id = Long.valueOf(httpReq.getParameter("id"));

      final ComputerDto computerDto = computerServiceJDBC.getById(id);
      httpReq.setAttribute("computer", computerDto);
    }

    final List<Company> companies = companyServiceJDBC.getAll();
    httpReq.setAttribute("companies", companies);

    // Get the JSP dispatcher
    final RequestDispatcher dispatcher = httpReq
        .getRequestDispatcher("WEB-INF/views/editcomputer.jsp");
    // Forward the httpRequest
    dispatcher.forward(httpReq, httpResp);
  }

  /**
   * Update computer in database using HttpServletRequest params {id (required), name (required), introduced, discontinued, companyId}
   */
  @Override
  protected void doPost(final HttpServletRequest httpReq, final HttpServletResponse httpResp)
      throws ServletException, IOException {

    final Map<String, String> errorMap = new HashMap<String, String>();

    final ComputerDto.Builder builder = ComputerDto.builder()
        .name(httpReq.getParameter("name").trim())
        .introduced(httpReq.getParameter("introduced").trim())
        .discontinued(httpReq.getParameter("discontinued").trim());

    if (StringValidation.isPositiveLong(httpReq.getParameter("id").trim())) {
      builder.id((Long.valueOf(httpReq.getParameter("id").trim())));
    } else {
      errorMap.put("eId", "Incorrect id : an id should be a positive integer");
    }

    if (StringValidation.isPositiveLong(httpReq.getParameter("companyId").trim())) {
      builder.companyId(Long.valueOf(httpReq.getParameter("companyId").trim()));
    }

    final ComputerDto computerDto = builder.build();

    if (ComputerDtoConverter.validate(computerDto, errorMap)) {
      computerServiceJDBC.updateByComputer(ComputerDtoConverter.toComputer(computerDto));
      httpResp.sendRedirect("dashboard");
    } else {
      httpReq.setAttribute("error", errorMap);
      doGet(httpReq, httpResp);
    }
  }
}
