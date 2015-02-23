package com.excilys.computerdatabase.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

@RunWith(MockitoJUnitRunner.class)
public class AddComputerControllerUITest {

  WebDriver driver;

  @Before
  public void setUp() {
    driver = new FirefoxDriver();
  }

  @After
  public void tearDown() {
    driver.close();
  }

  @Test
  public void testAddLegitComputer() {

    Locale locale = new Locale("en");
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(locale);

    String computerName = "SeleniumTestingComputer";
    String dateIntroduced = "1985-12-14";
    String dateDiscontinued = "2000-05-03";

    driver.get("http://localhost:8080/computer-database/addcomputer");

    // Fill in the computer name
    WebElement computerNameElement = driver.findElement(By.id("name"));
    computerNameElement.sendKeys(computerName);

    WebElement computerIntroducedElement = driver.findElement(By.id("introduced"));
    computerIntroducedElement.sendKeys(dateIntroduced);

    WebElement computerDiscontinuedElement = driver.findElement(By.id("discontinued"));
    computerDiscontinuedElement.sendKeys(dateDiscontinued);

    WebElement computerCompanyElement = driver.findElement(By.id("companyId"));
    List<WebElement> companies = computerCompanyElement.findElements(By.tagName("option"));

    // Generating a random int between 0 and companies.size() - 1
    Random rand = new Random();
    int randomIndex = rand.nextInt(companies.size());

    WebElement companyOption = companies.get(randomIndex);
    String companyId = companyOption.getAttribute("value");
    // Selecting the company option
    companyOption.click();

    // Click on "Add"
    driver.findElement(By.id("submit")).click();

    List<WebElement> successElement = driver.findElements(By.id("message"));

    assertEquals(1, successElement.size());

    // All Done!
  }

  @Test
  public void testAddEmptyComputer() {

    Locale locale = new Locale("en");
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(locale);

    driver.get("http://localhost:8080/computer-database/addcomputer");

    // Click on "Add"
    driver.findElement(By.id("submit")).click();

    List<WebElement> errorElement = driver.findElements(By.id("name-error"));

    assertEquals(1, errorElement.size());
  }

  @Test
  public void testAddWrongDateComputer() {

    Locale locale = new Locale("en");
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(locale);

    driver.get("http://localhost:8080/computer-database/addcomputer");

    String computerName = "SeleniumTestingComputer";
    String dateIntroduced = "2050-12-14";

    // Fill in the computer name
    WebElement computerNameElement = driver.findElement(By.id("name"));
    computerNameElement.sendKeys(computerName);

    WebElement computerIntroducedElement = driver.findElement(By.id("introduced"));
    computerIntroducedElement.sendKeys(dateIntroduced);

    // Click on "Add"
    driver.findElement(By.id("submit")).click();

    List<WebElement> errorElement = driver.findElements(By.id("eIntroduced"));

    assertEquals(1, errorElement.size());
  }

  @Test
  public void testAddWrongDateFormatComputer() {

    Locale locale = new Locale("en");
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(locale);

    driver.get("http://localhost:8080/computer-database/addcomputer");

    String computerName = "SeleniumTestingComputer";
    String dateIntroduced = "01-01-2001";

    // Fill in the computer name
    WebElement computerNameElement = driver.findElement(By.id("name"));
    computerNameElement.sendKeys(computerName);

    WebElement computerIntroducedElement = driver.findElement(By.id("introduced"));
    computerIntroducedElement.sendKeys(dateIntroduced);

    // Click on "Add"
    driver.findElement(By.id("submit")).click();

    List<WebElement> errorElement = driver.findElements(By.id("introduced-error"));

    assertEquals(1, errorElement.size());
  }

}
