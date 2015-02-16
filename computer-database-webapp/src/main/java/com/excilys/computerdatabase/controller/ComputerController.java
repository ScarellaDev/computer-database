package com.excilys.computerdatabase.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.OrderBy;
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

  private String           message      = null;
  private String           errormessage = null;

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
   * Displays pages of computer lists from database
   */
  @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
  protected String dashboard(final ModelMap map, @PageableDefault(page = 0, size = 20)
  final Pageable pageable, @RequestParam(value = "search", required = false, defaultValue = "")
  final String search) {
    final Page<Computer> page;
    if (pageable.getPageSize() > 100) {
      final Pageable limitedPageable = new PageRequest(0, 100);
      page = computerService.getPagedList(search, limitedPageable);
    } else {
      page = computerService.getPagedList(search, pageable);
    }

    if (page.hasContent()) {
      map.addAttribute("search", search);
      map.addAttribute("page", page);
      if (page.getSort() != null && OrderBy.getOrderByFromSort(page.getSort()) != null) {
        map.addAttribute("sort", OrderBy.getOrderByFromSort(page.getSort()).getColName());
        map.addAttribute("direction", OrderBy.getOrderByFromSort(page.getSort()).getDir());
      }
    } else {
      errormessage = messageSourceAccessor.getMessage("error-no-computer-found");
    }

    map.addAttribute("message", message);
    message = null;
    map.addAttribute("errormessage", errormessage);
    errormessage = null;
    return "dashboard";
  }

  /**
   * Displays the addcomputer form 
   */
  @RequestMapping(value = "/addcomputer", method = RequestMethod.GET)
  protected String getAddForm(final ModelMap map) {
    map.addAttribute("companies", companyService.getAll());
    map.addAttribute("computerDto", new ComputerDto());

    map.addAttribute("errormessage", errormessage);
    errormessage = null;
    return "addcomputer";
  }

  /**
   * Add computer to database
   */
  @RequestMapping(value = "/addcomputer", method = RequestMethod.POST)
  protected String addComputer(final ModelMap map, @Valid
  final ComputerDto computerDto, final BindingResult result) {
    if (!result.hasErrors()) {
      if (computerDto.getCompanyId() == 0
          || (computerDto.getCompanyId() > 0 && companyService.getById(computerDto.getCompanyId()) != null)) {
        computerService.addByComputer(ComputerDtoConverter.toComputer(computerDto,
            messageSourceAccessor.getMessage("date-format")));
        message = messageSourceAccessor.getMessage("success-add") + computerDto.toString();
        return "redirect:/dashboard";
      } else {
        errormessage = messageSourceAccessor.getMessage("error-company-selection");
        map.addAttribute("companies", companyService.getAll());
        return "addcomputer";
      }
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
      final ComputerDto computerDto = computerService.getById(id);
      if (computerDto != null) {
        map.addAttribute("companies", companyService.getAll());
        map.addAttribute("computerDto", computerDto);

        map.addAttribute("errormessage", errormessage);
        errormessage = null;
        return "editcomputer";
      } else {
        errormessage = messageSourceAccessor.getMessage("error-edit-selection");
        return "redirect:/dashboard";
      }
    } else {
      errormessage = messageSourceAccessor.getMessage("error-edit-false-selection");
      return "redirect:/dashboard";
    }
  }

  /**
   * Update computer in database
   */
  @RequestMapping(value = "/editcomputer", method = RequestMethod.POST)
  protected String editComputer(final ModelMap map, @Valid
  final ComputerDto computerDto, final BindingResult result) {
    final ComputerDto newComputerDto = computerService.getById(computerDto.getId());
    if (newComputerDto != null) {
      if (!result.hasErrors()) {
        if (computerDto.getCompanyId() == 0
            || (computerDto.getCompanyId() > 0 && companyService
                .getById(computerDto.getCompanyId()) != null)) {
          computerService.updateByComputer(ComputerDtoConverter.toComputer(computerDto,
              messageSourceAccessor.getMessage("date-format")));
          message = messageSourceAccessor.getMessage("success-edit") + computerDto.toString();
          return "redirect:/dashboard";
        } else {
          errormessage = messageSourceAccessor.getMessage("error-company-selection");
          map.addAttribute("companies", companyService.getAll());
          return "editcomputer";
        }
      } else {
        map.addAttribute("companies", companyService.getAll());
        return "editcomputer";
      }
    } else {
      errormessage = messageSourceAccessor.getMessage("error-edit-selection")
          + computerDto.toString();
      return "redirect:/dashboard";
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
      errormessage = messageSourceAccessor.getMessage("error-empty-selection");
      return "redirect:/dashboard";
    }
    List<ComputerDto> computers = computerService.removeByIdList(idList);
    if (computers != null) {
      message = messageSourceAccessor.getMessage("success-delete");
    } else {
      errormessage = messageSourceAccessor.getMessage("error-remove-selection");
    }
    return "redirect:/dashboard";
  }
}
