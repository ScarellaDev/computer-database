package com.excilys.computerdatabase.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.service.ICompanyService;
import com.excilys.computerdatabase.service.IComputerService;

/**
* Controller managing the /addcomputer URL
* Used to add computers to the database through the webapp
*
* @author Jeremy SCARELLA
*/
@Controller
@RequestMapping("/addcomputer")
public class AddComputerController {

  /*
   * Instance of ComputerService
   */
  @Autowired
  private IComputerService computerService;

  /*
   * Instance of CompanyService
   */
  @Autowired
  private ICompanyService  companyService;

  @RequestMapping(method = RequestMethod.GET)
  protected String doGet(final ModelMap map) {
    // The view requires the list of all companies
    map.addAttribute("companies", companyService.getAll());
    return "addcomputer";
  }

  /**
   * Add computer to database
   */
  @RequestMapping(method = RequestMethod.POST)
  protected String doPost(final ComputerDto computerDto, final ModelMap map) {

    final Map<String, String> errorMap = new HashMap<String, String>();

    // Storing given values in case something goes wrong and we need to display it back to the user
    map.addAttribute("newName", computerDto.getName());
    map.addAttribute("newIntroduced", computerDto.getIntroduced());
    map.addAttribute("newDiscontinued", computerDto.getDiscontinued());
    map.addAttribute("newCompanyId", computerDto.getCompanyId());

    Company company = null;
    final Long companyId = computerDto.getCompanyId();
    if (companyId > 0) {
      company = companyService.getById(companyId);
      if (company == null) {
        errorMap.put("eCompanyId", "Incorrect companyId : an id should be a positive integer");
      }
    }

    if (ComputerDtoConverter.validate(computerDto, errorMap)) {
      computerService.addByComputer(ComputerDtoConverter.toComputer(computerDto));
      return "redirect:/dashboard";
    } else {
      map.addAttribute("error", errorMap);
      return doGet(map);
    }
  }
}
