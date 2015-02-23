package com.excilys.computerdatabase.controller;

import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

public class DashboardComputerControllerUITest {

  WebDriver driver;

  @Before
  public void setUp() {
    driver = new FirefoxDriver();
  }

  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void test() {

    Locale locale = new Locale("en");
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(locale);

    driver.get("http://localhost:8080/computer-database/");

    WebElement element = driver.findElement(By.id("homeTitle"));
    String homeTitle = element.getText();
    if (homeTitle == null || !homeTitle.matches("\\d+ Computers found")) {
      fail("Computer list page is broken");
    }
  }
}