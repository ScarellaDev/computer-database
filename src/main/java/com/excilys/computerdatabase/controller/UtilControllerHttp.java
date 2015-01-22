package com.excilys.computerdatabase.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.service.ICompanyDBService;
import com.excilys.computerdatabase.service.IComputerDBService;
import com.excilys.computerdatabase.service.impl.CompanyDBServiceImpl;
import com.excilys.computerdatabase.service.impl.ComputerDBServiceImpl;
import com.excilys.computerdatabase.validator.StringValidation;

public class UtilControllerHttp {

  /*
   * Instance of computerDBService
   */
  private static IComputerDBService      computerDBService   = ComputerDBServiceImpl.INSTANCE;

  /*
   * Instance of companyDBService
   */
  private static ICompanyDBService       companyDBService    = CompanyDBServiceImpl.INSTANCE;

  /*
   * Instance of companyDBService
   */
  private static final Logger            LOGGER              = LoggerFactory
                                                                 .getLogger(UtilControllerHttp.class);

  /*
   * DATE TIME FORMATTER
   */
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
                                                                 .ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * Builds a Computer instance based on the information contained in the given HttpServletRequest from an Add operation
   * If there was an input error, it sets a Map "errorMsgMap" who contain error messages in the HttpServletRequest 
   * @param httpReq : HttpServletRequest containing the information of the computer { name (required), introduced, discontinued, companyId }
   * @return a Computer instance based on the request info if there was no errors or null if there was an error
   */
  public static Computer buildComputer(HttpServletRequest httpReq) {
    Computer.Builder builder = Computer.builder();
    String name = httpReq.getParameter("name");
    String introducedS = httpReq.getParameter("introduced");
    String discontinuedS = httpReq.getParameter("discontinued");
    String companyIdS = httpReq.getParameter("companyId");

    Map<String, String> errorMsgMap = new HashMap<String, String>();

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

    //Return the computer instance if there was no error
    if (errorMsgMap.isEmpty()) {
      return builder.build();
    }

    //Return null if there was an error and set a Map of errors as an Attribute in the httpRequest
    httpReq.setAttribute("error", errorMsgMap);
    return null;
  }

  /**
   * Builds a Computer instance based on the information contained in the given HttpServletRequest from an Update operation
   * If there was an input error, it sets a Map "errorMsgMap" who contain error messages in the HttpServletRequest 
   * @param httpReq : HttpServletRequest containing the information of the computer { id (required), name (required), introduced, discontinued, companyId }
   * @return a Computer instance based on the request info if there was no errors or null if there was an error
   */
  public static Computer buildComputerWithId(HttpServletRequest httpReq) {
    Map<String, String> errorMsgMap = new HashMap<String, String>();

    //Get the id
    String idS = httpReq.getParameter("id");

    //Check if the idS is a valid id
    if (!StringValidation.isPositiveLong(idS)) {
      errorMsgMap.put("eId", "Incorrect id : an id should be a positive integer");
      httpReq.setAttribute("error", errorMsgMap);
      return null;
    }

    Long id = Long.valueOf(idS);

    //Check if a computer with this id exist in the database
    if (computerDBService.getById(id) == null) {
      errorMsgMap.put("eId", "No computer with this id was found");
      httpReq.setAttribute("error", errorMsgMap);
      return null;
    }

    //Create a computer with the informations in the httpRequest
    Computer computer = buildComputer(httpReq);

    //Check if the computer was created
    if (computer == null) {
      return null;
    }

    computer.setId(id);
    return computer;
  }
}
