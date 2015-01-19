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
  private int     pageIndex;

  /*
  * List of elements of the current page
  */
  private List<T> list;

  /*
  * Max number of elements the page can have (20 by default)
  */
  private int     nbElementsPerPage;

  /*
  * Total number of elements in the database
  */
  private int     totalNbElements;

  /*
   * Total number of pages
   */
  private int     totalNbPages;

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
  public Page(final int pageIndex, final List<T> list, final int nbElementsPerPage,
      final int totalNbElements, final int totalNbPages) {
    this.pageIndex = pageIndex;
    this.list = list;
    this.nbElementsPerPage = nbElementsPerPage;
    this.totalNbElements = totalNbElements;
    this.totalNbPages = totalNbPages;
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
    final int prime = 31;
    int result = 1;
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
  public int getPageIndex() {
    return pageIndex;
  }

  public void setPageIndex(int pageIndex) {
    this.pageIndex = pageIndex;
  }

  public List<T> getList() {
    return list;
  }

  public void setList(List<T> list) {
    this.list = list;
  }

  public int getNbElementsPerPage() {
    return nbElementsPerPage;
  }

  public void setNbElementsPerPage(int nbElementsPerPage) {
    this.nbElementsPerPage = nbElementsPerPage;
  }

  public int getTotalNbElements() {
    return totalNbElements;
  }

  public void setTotalNbElements(int totalNbElements) {
    this.totalNbElements = totalNbElements;
  }

  public int getTotalNbPages() {
    return totalNbPages;
  }

  public void setTotalNbPages(int totalNbPages) {
    this.totalNbPages = totalNbPages;
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

    if (pageIndex == totalNbPages) {
      if (totalNbElements % nbElementsPerPage == 0) {
        nbElementsPerPage = 0;
      } else {
        nbElementsPerPage = totalNbElements % nbElementsPerPage;
      }
    } else {
      nbElementsPerPage = 20;
    }
  }
}