package com.excilys.computerdatabase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

  /*
   * MessageSourceAccessor
   */
  private MessageSourceAccessor messageSourceAccessor;

  @Autowired
  public void setMessageSource(final MessageSource messageSource) {
    this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
  }

  @RequestMapping(method = RequestMethod.GET)
  protected String doGet(final ModelMap map) {
    map.addAttribute("companies", companyService.getAll());
    map.addAttribute("computerDto", new ComputerDto());
    return "addcomputer";
  }

  /**
   * Add computer to database
   */
  @RequestMapping(method = RequestMethod.POST)
  protected String doPost(final ModelMap map, @Validated
  final ComputerDto computerDto, final BindingResult result) {
    if (!result.hasErrors()) {
      computerService.addByComputer(ComputerDtoConverter.toComputer(computerDto));

      map.addAttribute("message",
          messageSourceAccessor.getMessage("success-add") + computerDto.toString());

      return "redirect:/dashboard";
    } else {
      map.addAttribute("companies", companyService.getAll());
      return "addcomputer";
    }
  }
}
