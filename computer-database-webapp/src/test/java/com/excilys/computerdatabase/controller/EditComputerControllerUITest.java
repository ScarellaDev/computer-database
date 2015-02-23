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
public class EditComputerControllerUITest {

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
  public void testEditLegitComputer() {

    Locale locale = new Locale("en");
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(locale);

    String computerName = "SeleniumTestingComputer";
    String dateIntroduced = "1985-12-14";
    String dateDiscontinued = "2000-05-03";

    driver.get("http://localhost:8080/computer-database/editcomputer?id=1");

    // Fill in the computer name
    WebElement computerNameElement = driver.findElement(By.id("name"));
    computerNameElement.clear();
    computerNameElement.sendKeys(computerName);

    WebElement computerIntroducedElement = driver.findElement(By.id("introduced"));
    computerIntroducedElement.clear();
    computerIntroducedElement.sendKeys(dateIntroduced);

    WebElement computerDiscontinuedElement = driver.findElement(By.id("discontinued"));
    computerDiscontinuedElement.clear();
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

    // Click on "Edit"
    driver.findElement(By.id("submit")).click();

    List<WebElement> successElement = driver.findElements(By.id("message"));

    assertEquals(1, successElement.size());

    // All Done!
  }

  @Test
  public void testEditEmptyComputer() {

    Locale locale = new Locale("en");
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(locale);

    driver.get("http://localhost:8080/computer-database/editcomputer?id=1");

    // Fill in the computer name
    WebElement computerNameElement = driver.findElement(By.id("name"));
    computerNameElement.clear();

    WebElement computerIntroducedElement = driver.findElement(By.id("introduced"));
    computerIntroducedElement.clear();

    WebElement computerDiscontinuedElement = driver.findElement(By.id("discontinued"));
    computerDiscontinuedElement.clear();

    WebElement computerCompanyElement = driver.findElement(By.id("companyId"));
    List<WebElement> companies = computerCompanyElement.findElements(By.tagName("option"));

    WebElement companyOption = companies.get(0);
    companyOption.click();

    // Click on "Edit"
    driver.findElement(By.id("submit")).click();

    List<WebElement> errorElement = driver.findElements(By.id("name-error"));

    assertEquals(1, errorElement.size());
  }

  @Test
  public void testEditWrongDateComputer() {

    Locale locale = new Locale("en");
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(locale);

    driver.get("http://localhost:8080/computer-database/editcomputer?id=1");;

    String computerName = "SeleniumTestingComputer";
    String dateIntroduced = "2050-12-14";

    // Fill in the computer name
    WebElement computerNameElement = driver.findElement(By.id("name"));
    computerNameElement.clear();
    computerNameElement.sendKeys(computerName);

    WebElement computerIntroducedElement = driver.findElement(By.id("introduced"));
    computerIntroducedElement.clear();
    computerIntroducedElement.sendKeys(dateIntroduced);

    WebElement computerDiscontinuedElement = driver.findElement(By.id("discontinued"));
    computerDiscontinuedElement.clear();

    WebElement computerCompanyElement = driver.findElement(By.id("companyId"));
    List<WebElement> companies = computerCompanyElement.findElements(By.tagName("option"));

    WebElement companyOption = companies.get(0);
    companyOption.click();

    // Click on "Edit"
    driver.findElement(By.id("submit")).click();

    List<WebElement> errorElement = driver.findElements(By.id("introduced.errors"));

    assertEquals(1, errorElement.size());
  }

  @Test
  public void testEditWrongDateFormatComputer() {

    Locale locale = new Locale("en");
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(locale);

    driver.get("http://localhost:8080/computer-database/editcomputer?id=1");

    String computerName = "SeleniumTestingComputer";
    String dateIntroduced = "01-01-2001";

    // Fill in the computer name
    WebElement computerNameElement = driver.findElement(By.id("name"));
    computerNameElement.clear();
    computerNameElement.sendKeys(computerName);

    WebElement computerIntroducedElement = driver.findElement(By.id("introduced"));
    computerIntroducedElement.clear();
    computerIntroducedElement.sendKeys(dateIntroduced);

    WebElement computerDiscontinuedElement = driver.findElement(By.id("discontinued"));
    computerDiscontinuedElement.clear();

    WebElement computerCompanyElement = driver.findElement(By.id("companyId"));
    List<WebElement> companies = computerCompanyElement.findElements(By.tagName("option"));

    WebElement companyOption = companies.get(0);
    companyOption.click();

    // Click on "Edit"
    driver.findElement(By.id("submit")).click();

    List<WebElement> errorElement = driver.findElements(By.id("introduced-error"));

    assertEquals(1, errorElement.size());
  }

}
