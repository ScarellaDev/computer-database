package com.excilys.computerdatabase.domain;

import org.springframework.data.domain.Sort;

public enum OrderBy {

  COMPUTER_NAME_ASC(new Sort(Sort.Direction.ASC, "name"), "name", "ASC"), COMPUTER_NAME_DESC(
      new Sort(Sort.Direction.DESC, "name"), "name", "DESC"), INTRODUCED_DATE_ASC(new Sort(
      Sort.Direction.ASC, "introduced"), "introduced", "ASC"), INTRODUCED_DATE_DESC(new Sort(
      Sort.Direction.DESC, "introduced"), "introduced", "DESC"), DISCONTINUED_DATE_ASC(new Sort(
      Sort.Direction.ASC, "discontinued"), "discontinued", "ASC"), DISCONTINUED_DATE_DESC(new Sort(
      Sort.Direction.DESC, "discontinued"), "discontinued", "DESC"), COMPANY_NAME_ASC(new Sort(
      Sort.Direction.ASC, "company.name"), "company.name", "ASC"), COMPANY_NAME_DESC(new Sort(
      Sort.Direction.DESC, "company.name"), "company.name", "DESC");

  private Sort   sort;
  private String colName = "name";
  private String dir     = "ASC";

  private OrderBy(final Sort sort, final String colName, final String dir) {
    this.setSort(sort);
    this.setColName(colName);
    this.setDir(dir);
  }

  public Sort getSort() {
    return sort;
  }

  private void setSort(final Sort sort) {
    this.sort = sort;
  }

  public String getColName() {
    return colName;
  }

  public void setColName(final String colName) {
    this.colName = colName;
  }

  public String getDir() {
    return dir;
  }

  private void setDir(final String dir) {
    this.dir = dir;
  }

  public static OrderBy get(final String colName, String dir) {
    if (colName == null) {
      return null;
    }
    if (dir == null) {
      dir = "asc";
    }
    final OrderBy[] list = OrderBy.values();
    for (OrderBy col : list) {
      if (col.getColName().equals(colName) && col.getDir().equals(dir)) {
        return col;
      }
    }
    return null;
  }

  public String getDirForCol(final String col) {
    if (this.getColName().equals(col) && this.getDir().equals("asc")) {
      return "desc";
    }
    return "asc";
  }

  public static OrderBy getOrderByFromSort(final Sort sort) {
    if (sort == null) {
      return null;
    }
    final OrderBy[] list = OrderBy.values();
    for (OrderBy ob : list) {
      if (ob.getSort().equals(sort)) {
        return ob;
      }
    }
    return null;
  }
}
