package com.excilys.computerdatabase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.service.IComputerService;

/**
* Controller managing the /dashboard URL
* Main interface
* Displays computer list from database and enable page navigation, name search, creation, update and deletion of computers
*
* @author Jeremy SCARELLA
*/
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

  /*
   * Instance of ComputerService
   */
  @Autowired
  private IComputerService computerService;

  /**
   * Displays pages of computer lists from database
   */
  @RequestMapping(method = RequestMethod.GET)
  protected String doGet(final ModelMap map,
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
}