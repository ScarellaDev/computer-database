package com.excilys.computerdatabase.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.service.IComputerService;

/**
* Controller managing the /deletecomputer URL
* Used to remove computers from the database through the webapp
*
* @author Jeremy SCARELLA
*/
@Controller
@RequestMapping("/deletecomputer")
public class DeleteComputerController {

  /*
   * Instance of ComputerServiceJDBC
   */
  @Autowired
  private IComputerService     computerService;

  /*
   * POSITIVE LONG PATTERN
   */
  private static final Pattern POSITIVE_LONG_PATTERN = Pattern.compile("\\d{1,19}");

  /**
   * Remove computer(s) from the database
   */
  @RequestMapping(method = RequestMethod.POST)
  protected String doPost(@RequestParam("selection")
  final String selection) {

    //Create a matcher to find the positives longs in the String
    final Matcher m = POSITIVE_LONG_PATTERN.matcher(selection);

    final List<Long> idList = new ArrayList<Long>();

    //For each long found, delete the computer
    while (m.find()) {
      idList.add(new Long(m.group()));
    }

    if (idList.isEmpty()) {
      return "redirect:/dashboard";
    }

    computerService.removeByIdList(idList);

    return "redirect:/dashboard";
  }

}