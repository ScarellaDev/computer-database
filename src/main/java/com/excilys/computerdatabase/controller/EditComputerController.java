package com.excilys.computerdatabase.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.service.ICompanyService;
import com.excilys.computerdatabase.service.IComputerService;

/**
* Controller managing the /editcomputer URL
* Updates a computer in database with the parameters given through URL
*
* @author Jeremy SCARELLA
*/
@Controller
@RequestMapping("/editcomputer")
public class EditComputerController {

  /*
   * Instance of ComputerServiceJDBC
   */
  @Autowired
  private IComputerService computerService;

  /*
   * Instance of CompanyServiceJDBC
   */
  @Autowired
  private ICompanyService  companyService;

  @RequestMapping(method = RequestMethod.GET)
  protected String doGet(final ModelMap map, @RequestParam("id")
  final Long id) {
    if (id != null) {
      map.addAttribute("computer", computerService.getById(id));
      map.addAttribute("companies", companyService.getAll());
    }
    return "editcomputer";
  }

  /**
   * Update computer in database
   */
  @RequestMapping(method = RequestMethod.POST)
  protected String doPost(final ComputerDto computerDto, final ModelMap map) {

    final Map<String, String> errorMap = new HashMap<String, String>();

    ComputerDto newComputerDto = computerService.getById(computerDto.getId());
    if (newComputerDto == null) {
      //ERROR computer doesn't exist
      return "redirect:/dashboard";
    }
    // Storing given value in case something goes wrong and we need to display it back to the user
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
      computerService.updateByComputer(ComputerDtoConverter.toComputer(computerDto));
      return "redirect:/dashboard";
    } else {
      map.addAttribute("error", errorMap);
      return doGet(map, computerDto.getId());
    }
  }
}
