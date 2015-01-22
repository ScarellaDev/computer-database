package com.excilys.computerdatabase.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    long id = 0;
    final String idString = req.getParameter("id");
    if (StringValidation.isPositiveLong(idString)) {
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

  @Override
  protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {

    final Map<String, String> errorMsgMap = new HashMap<String, String>();

    //Get the id
    final String idS = req.getParameter("id");

    //Check if the idString is a valid id
    if (!StringValidation.isPositiveLong(idS)) {
      errorMsgMap.put("eId", "Incorrect id : an id should be a long");
      req.setAttribute("error", errorMsgMap);
    }
    final Long id = Long.valueOf(idS);

    //Check if a computer with this id exist in the database
    if (computerDBService.getById(id) == null) {
      errorMsgMap.put("eId", "No computer with this id was found");
      req.setAttribute("error", errorMsgMap);
    }

    //////////////
    Computer computer = new Computer();

    final Computer.Builder builder = Computer.builder();
    final String name = req.getParameter("name");
    final String introducedS = req.getParameter("introduced");
    final String discontinuedS = req.getParameter("discontinued");
    final String companyIdS = req.getParameter("companyId");

    //Check if the name is a valid Name
    if (!StringValidation.isEmpty(name)) {
      builder.name(name);
    } else {
      errorMsgMap.put("eName", "Incorrect name : a name can't be empty or only spaces");
    }

    //Check if the introduced date is valid
    if (introducedS != null && !introducedS.trim().isEmpty()) {
      if (StringValidation.isDate(introducedS)) {
        StringBuffer introducedSB = new StringBuffer(introducedS);
        introducedSB.append(" 00:00:00");
        builder.introduced(LocalDateTime.parse(introducedSB, DATE_TIME_FORMATTER));
      } else {
        errorMsgMap.put("eDateI",
            "Incorrect date : the field must be at the yyyy-MM-dd format or left empty");
      }
    }

    //Check if the discontinued date is valid
    if (discontinuedS != null && !discontinuedS.trim().isEmpty()) {
      if (StringValidation.isDate(discontinuedS)) {
        StringBuffer discontinuedSB = new StringBuffer(discontinuedS);
        discontinuedSB.append(" 00:00:00");
        builder.discontinued(LocalDateTime.parse(discontinuedSB, DATE_TIME_FORMATTER));
      } else {
        errorMsgMap.put("eDateD",
            "Incorrect date : the field must be at the yyyy-MM-dd format or left empty");
      }
    }

    //Check if the company id is valid
    if (companyIdS != null && !companyIdS.trim().isEmpty()) {
      if (StringValidation.isPositiveLong(companyIdS)) {
        final Company company = companyDBService.getById(Long.valueOf(companyIdS));

        //Check if a company with this id exist in the database 
        if (company != null) {
          builder.company(company);
        }

      } else {
        errorMsgMap.put("eCompanyId", "Incorrect Company identifier");
      }
    }

    //Return a computer if there was no error
    if (errorMsgMap.isEmpty()) {
      computer = builder.build();
    } else {
      computer = null;
    }
    ///////////////////////////

    if (computer != null) {
      computer.setId(id);
      computerDBService.updateByComputer(computer);
      LOGGER.info(computer + " was successfully updated in the database");
      resp.sendRedirect("dashboard");
    } else {
      doGet(req, resp);
    }
  }

}
