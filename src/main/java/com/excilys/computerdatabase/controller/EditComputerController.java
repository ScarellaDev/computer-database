package com.excilys.computerdatabase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

  /*
   * Validator settings
   */
  @Autowired
  @Qualifier("computerDtoValidator")
  private Validator        computerDtoValidator;

  @InitBinder
  private void initBinder(final WebDataBinder binder) {
    binder.setValidator(computerDtoValidator);
  }

  @RequestMapping(method = RequestMethod.GET)
  protected String doGet(final ModelMap map, @RequestParam("id")
  final Long id) {
    if (id != null) {
      map.addAttribute("computer", computerService.getById(id));
      map.addAttribute("companies", companyService.getAll());
      map.addAttribute("computerDto", new ComputerDto());
    }
    return "editcomputer";
  }

  /**
   * Update computer in database
   */
  @RequestMapping(method = RequestMethod.POST)
  protected String doPost(final ModelMap map, @Validated
  final ComputerDto computerDto, final BindingResult result) {
    final ComputerDto newComputerDto = computerService.getById(computerDto.getId());
    if (newComputerDto == null) {
      map.addAttribute("message", computerDto + "does not exist in database anymore.");
      return "redirect:/dashboard";
    }

    if (!result.hasErrors()) {
      computerService.updateByComputer(ComputerDtoConverter.toComputer(computerDto));
      map.addAttribute("message", "Successfully edited " + computerDto.toString());
      return "redirect:/dashboard";
    } else {
      map.addAttribute("companies", companyService.getAll());
      return "editcomputer";
    }
  }
}
