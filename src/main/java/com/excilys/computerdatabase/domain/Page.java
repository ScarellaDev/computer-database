package com.excilys.computerdatabase.domain;

import java.util.List;

/**
* Page class.
* 
* @author Jeremy SCARELLA
*/
public class Page<T> {

  /*
   * COLUMN NAMES
   */
  private static final String[] COLUMN_NAMES = { "c.id", "c.name", "c.introduced",
      "c.discontinued", "company.name"      };

  /*
  * Index of the current page (1 by default)
  */
  private Integer               pageIndex;

  /*
  * List of elements of the current page
  */
  private List<T>               list;

  /*
  * Max number of elements the page can have (20 by default)
  */
  private Integer               nbElementsPerPage;

  /*
  * Total number of elements in the database
  */
  private Integer               totalNbElements;

  /*
   * Total number of pages
   */
  private Integer               totalNbPages;

  /*
   * Search parameter
   */
  private String                search;

  /*
   * Sort parameter
   */
  private Integer               sort;

  /*
   * Order parameter (works with sort)
   */
  private String                order;

  /*
   * Constructors
   */
  public Page() {
    this.pageIndex = 1;
    this.nbElementsPerPage = 20;
    this.search = "";
    this.sort = 0;
    this.order = "ASC";
  }

  /**
   * @param pageIndex
   * @param list
   * @param nbElementsPerPage
   * @param totalNbElements
   * @param totalNbPages
   */
  public Page(final Integer pageIndex, final List<T> list, final Integer nbElementsPerPage,
      final Integer totalNbElements, final Integer totalNbPages, final String search,
      final Integer sort, final String order) {
    this.pageIndex = pageIndex;
    this.list = list;
    this.nbElementsPerPage = nbElementsPerPage;
    this.totalNbElements = totalNbElements;
    this.totalNbPages = totalNbPages;
    this.search = search;
    this.sort = sort;
    this.order = order;
  }

  /*
   * toString/hashCode/equals overrides
   */
  @Override
  public String toString() {
    return "Page [pageIndex=" + pageIndex + ", list=" + list + ", nbElementsPerPage="
        + nbElementsPerPage + ", totalNbElements=" + totalNbElements + ", totalNbPages="
        + totalNbPages + ", search=" + search + ", sort=" + sort + ", order=" + order + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((list == null) ? 0 : list.hashCode());
    result = prime * result + ((nbElementsPerPage == null) ? 0 : nbElementsPerPage.hashCode());
    result = prime * result + ((pageIndex == null) ? 0 : pageIndex.hashCode());
    result = prime * result + ((search == null) ? 0 : search.hashCode());
    result = prime * result + ((sort == null) ? 0 : sort.hashCode());
    result = prime * result + ((order == null) ? 0 : order.hashCode());
    result = prime * result + ((totalNbElements == null) ? 0 : totalNbElements.hashCode());
    result = prime * result + ((totalNbPages == null) ? 0 : totalNbPages.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }

    @SuppressWarnings("unchecked")
    final Page<T> other = (Page<T>) obj;
    if (list == null && other.list != null) {
      return false;
    } else if (!list.equals(other.list)) {
      return false;
    }

    if (order == null && other.order != null) {
      return false;
    } else if (!order.equals(other.order)) {
      return false;
    }

    if (sort == null && other.sort != null) {
      return false;
    } else if (!sort.equals(other.sort)) {
      return false;
    }

    if (search == null && other.search != null) {
      return false;
    } else if (!search.equals(other.search)) {
      return false;
    }

    if (totalNbPages == null && other.totalNbPages != null) {
      return false;
    } else if (!totalNbPages.equals(other.totalNbPages)) {
      return false;
    }

    if (totalNbElements == null && other.totalNbElements != null) {
      return false;
    } else if (!totalNbElements.equals(other.totalNbElements)) {
      return false;
    }

    if (nbElementsPerPage == null && other.nbElementsPerPage != null) {
      return false;
    } else if (!nbElementsPerPage.equals(other.nbElementsPerPage)) {
      return false;
    }

    if (pageIndex == null && other.pageIndex != null) {
      return false;
    } else if (!pageIndex.equals(other.pageIndex)) {
      return false;
    }

    return true;
  }

  /*
   * Getter/Setter
   */
  public static String[] getColumnNames() {
    return COLUMN_NAMES;
  }

  public Integer getPageIndex() {
    return pageIndex;
  }

  public void setPageIndex(Integer pageIndex) {
    this.pageIndex = pageIndex;
  }

  public List<T> getList() {
    return list;
  }

  public void setList(List<T> list) {
    this.list = list;
  }

  public Integer getNbElementsPerPage() {
    return nbElementsPerPage;
  }

  public void setNbElementsPerPage(Integer nbElementsPerPage) {
    this.nbElementsPerPage = nbElementsPerPage;
  }

  public Integer getTotalNbElements() {
    return totalNbElements;
  }

  public void setTotalNbElements(Integer totalNbElements) {
    this.totalNbElements = totalNbElements;
  }

  public Integer getTotalNbPages() {
    return totalNbPages;
  }

  public void setTotalNbPages(Integer totalNbPages) {
    this.totalNbPages = totalNbPages;
  }

  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public Integer getSort() {
    return sort;
  }

  public void setSort(Integer sort) {
    this.sort = sort;
  }

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  /**
  * Increment the pageIndex if there is more pages.
  * @return true if there is a next page
  */
  public boolean next() {
    //Check if there is a next page before incrementing
    if (pageIndex < totalNbPages) {
      pageIndex++;
      return true;
    }
    return false;
  }

  /**
  * Decrement the pageIndex if there is a previous page.
  * @return true if there is a previous page.
  */
  public boolean previous() {
    //Check if there is a previous page before decrementing
    if (pageIndex > 1) {
      pageIndex--;
      return true;
    }
    return false;
  }

  /**
  * Sets the totalNbPages to the correct value.
  */
  public void refreshNbPages() {
    if (nbElementsPerPage != 0) {
      totalNbPages = totalNbElements / nbElementsPerPage;
      if (totalNbElements % nbElementsPerPage != 0) {
        totalNbPages++;
      }
    } else {
      totalNbPages = 0;
    }
  }
}