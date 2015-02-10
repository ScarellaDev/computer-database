package com.excilys.computerdatabase.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.service.ICompanyService;
import com.excilys.computerdatabase.service.IComputerService;

/**
* Controller managing the /computer URL
*
* @author Jeremy SCARELLA
*/
@Controller
public class ComputerController {

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

  @InitBinder("computerDto")
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

  /**
   * Redirect root to /dashboard
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  protected String getRoot() {

    return "redirect:/dashboard";
  }

  /**
   * Displays pages of computer lists from database
   */
  @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
  protected String getDashboard(final ModelMap map,
      @RequestParam(value = "pageIndex", required = false, defaultValue = "1")
      final Integer pageIndex,
      @RequestParam(value = "nbElementsPerPage", required = false, defaultValue = "10")
      final Integer nbElementsPerPage,
      @RequestParam(value = "search", required = false, defaultValue = "")
      final String search, @RequestParam(value = "sort", required = false, defaultValue = "0")
      final Integer sort, @RequestParam(value = "order", required = false, defaultValue = "ASC")
      final String order, @ModelAttribute("message")
      final String message, @ModelAttribute("errormessage")
      final String errormessage) {

    map.addAttribute("message", message);
    map.addAttribute("errormessage", errormessage);

    Page<ComputerDto> page = new Page<ComputerDto>();

    //Get pageIndex and set it
    if (pageIndex < 1) {
      page.setPageIndex(1);
    } else {
      page.setPageIndex(pageIndex);
    }

    //Get nbElementsPerPage and set it
    if (nbElementsPerPage < 10) {
      page.setNbElementsPerPage(10);
    } else {
      page.setNbElementsPerPage(nbElementsPerPage);
    }

    //Get search parameter
    if (search != null) {
      page.setSearch(search.trim());
    }

    //Get sort & order parameters
    if (sort != null) {
      page.setSort(sort);
    }

    if (order != null
        && (order.compareToIgnoreCase("ASC") == 0 || order.compareToIgnoreCase("DESC") == 0)) {
      page.setOrder(order.toUpperCase());
    }

    //Retrieve the list of computers to display
    page = computerService.getPagedList(page);

    map.addAttribute("page", page);

    return "dashboard";
  }

  /**
   * Displays the addcomputer form 
   */
  @RequestMapping(value = "/addcomputer", method = RequestMethod.GET)
  protected String getAddForm(final ModelMap map) {
    map.addAttribute("companies", companyService.getAll());
    map.addAttribute("computerDto", new ComputerDto());
    return "addcomputer";
  }

  /**
   * Add computer to database
   */
  @RequestMapping(value = "/addcomputer", method = RequestMethod.POST)
  protected String addComputer(final ModelMap map, @Validated
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

  /**
   * Displays the editcomputer form
   */
  @RequestMapping(value = "/editcomputer", method = RequestMethod.GET)
  protected String getEditForm(final ModelMap map, @RequestParam("id")
  final Long id) {
    if (id != null) {
      ComputerDto computerDto = computerService.getById(id);
      if (computerDto != null) {
        map.addAttribute("companies", companyService.getAll());
        map.addAttribute("computerDto", computerDto);
        return "editcomputer";
      } else {
        map.addAttribute("errormessage", messageSourceAccessor.getMessage("error-edit-selection"));
        return "redirect:/dashboard";
      }
    }
    map.addAttribute("errormessage", messageSourceAccessor.getMessage("error-edit-false-selection"));
    return "redirect:/dashboard";
  }

  /**
   * Update computer in database
   */
  @RequestMapping(value = "/editcomputer", method = RequestMethod.POST)
  protected String editComputer(final ModelMap map, @Validated
  final ComputerDto computerDto, final BindingResult result) {
    final ComputerDto newComputerDto = computerService.getById(computerDto.getId());
    if (newComputerDto == null) {
      map.addAttribute("errormessage",
          computerDto + messageSourceAccessor.getMessage("error-edit-selection"));
      return "redirect:/dashboard";
    }

    if (!result.hasErrors()) {
      computerService.updateByComputer(ComputerDtoConverter.toComputer(computerDto));

      map.addAttribute("message",
          messageSourceAccessor.getMessage("success-edit") + computerDto.toString());

      return "redirect:/dashboard";
    } else {
      map.addAttribute("companies", companyService.getAll());
      return "editcomputer";
    }
  }

  /**
   * Remove computer(s) from the database
   */
  @RequestMapping(value = "/deletecomputer", method = RequestMethod.POST)
  protected String removeComputer(final ModelMap map, @RequestParam("selection")
  final String selection) {

    //Create a matcher to find the positives longs in the String
    final Matcher m = Pattern.compile("\\d{1,19}").matcher(selection);

    final List<Long> idList = new ArrayList<Long>();

    //For each long found, delete the computer
    while (m.find()) {
      idList.add(new Long(m.group()));
    }

    if (idList.isEmpty()) {
      map.addAttribute("errormessage", messageSourceAccessor.getMessage("error-empty-selection"));
      return "redirect:/dashboard";
    }
    computerService.removeByIdList(idList);

    map.addAttribute("message", messageSourceAccessor.getMessage("success-delete"));

    return "redirect:/dashboard";
  }
}
