package com.excilys.computerdatabase.domain;

import java.util.List;

/**
* Page class.
* 
* @author Jeremy SCARELLA
*/
public class Page<T> {
  /*
  * Index of the current page (1 by default)
  */
  private Integer pageIndex;

  /*
  * List of elements of the current page
  */
  private List<T> list;

  /*
  * Max number of elements the page can have (20 by default)
  */
  private Integer nbElementsPerPage;

  /*
  * Total number of elements in the database
  */
  private Integer totalNbElements;

  /*
   * Total number of pages
   */
  private Integer totalNbPages;

  /*
   * Search parameter
   */
  private String  search;

  /*
   * Constructors
   */
  public Page() {
    this.pageIndex = 1;
    this.nbElementsPerPage = 20;
  }

  /**
   * @param pageIndex
   * @param list
   * @param nbElementsPerPage
   * @param totalNbElements
   * @param totalNbPages
   */
  public Page(final Integer pageIndex, final List<T> list, final Integer nbElementsPerPage,
      final Integer totalNbElements, final Integer totalNbPages, final String search) {
    this.pageIndex = pageIndex;
    this.list = list;
    this.nbElementsPerPage = nbElementsPerPage;
    this.totalNbElements = totalNbElements;
    this.totalNbPages = totalNbPages;
    this.search = search;
  }

  /*
   * toString/hashCode/equals overrides
   */
  @Override
  public String toString() {
    return "Page [pageIndex=" + pageIndex + ", list=" + list + ", nbElementsPerPage="
        + nbElementsPerPage + ", totalNbElements=" + totalNbElements + ", totalNbPages="
        + totalNbPages + "]";
  }

  @Override
  public int hashCode() {
    final Integer prime = 31;
    Integer result = 1;
    result = prime * result + ((list == null) ? 0 : list.hashCode());
    result = prime * result + totalNbPages;
    result = prime * result + totalNbElements;
    result = prime * result + nbElementsPerPage;
    result = prime * result + pageIndex;
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
    if (list == null) {
      if (other.list != null) {
        return false;
      }
    } else if (!list.equals(other.list)) {
      return false;
    }

    if (totalNbPages != other.totalNbPages) {
      return false;
    }

    if (totalNbElements != other.totalNbElements) {
      return false;
    }

    if (nbElementsPerPage != other.nbElementsPerPage) {
      return false;
    }

    if (pageIndex != other.pageIndex) {
      return false;
    }

    return true;
  }

  /*
   * Getter/Setter
   */
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