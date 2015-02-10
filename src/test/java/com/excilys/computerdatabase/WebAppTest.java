package com.excilys.computerdatabase;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class WebAppTest {

  private WebDriver driver;

  @Before
  public void init() {
    driver = new FirefoxDriver();
  }

  public void test() {
    driver.get("http://localhost:8080/computer-database/dashboard");

    driver.findElement(By.id("addcomputer"));
  }

  public void pagination() {
    final int x = 20;
    driver.get("http://localhost:8080/computer-database/dashboard?totalNbElements=" + x);
    assertEquals(x, driver.findElements(By.cssSelector("#results tr")).size());
  }

  public void addComputer() {
    driver.get("http://localhost:8080/computer-database/addcomputer");

    final WebElement element = driver.findElement(By.name("name"));
    element.sendKeys("Selenium");

    driver.findElement(By.name("introduced")).sendKeys("2000-02-10");
    driver.findElement(By.name("discontinued")).sendKeys("2012-02-10");
    final Select select = new Select(driver.findElement(By.name("companyId")));
    select.selectByIndex(1);

    element.submit();
  }

  public void editComputer() {
    driver.get("http://localhost:8080/computer-database/editcomputer?id=1");

    final WebElement element = driver.findElement(By.name("name"));
    element.sendKeys("Selenium Edited");

    driver.findElement(By.name("introduced")).sendKeys("2003-02-10");
    driver.findElement(By.name("discontinued")).sendKeys("2015-02-10");
    final Select select = new Select(driver.findElement(By.name("companyId")));
    select.selectByIndex(2);

    element.submit();
  }

  public void search() {
    driver.get("http://localhost:8080/computer-database/dashboard");
    final WebElement element = driver.findElement(By.id("search"));
    element.sendKeys("mac");
    element.submit();
  }

  @After
  public void after() {
    //Close the browser
    driver.quit();
  }

}
