package org.mkh.frm.common.core;

public class PagingRequest {
    private String searchFilter;
    private String sort;
    private int page = 0;
    private int size = 40;

    public PagingRequest() {
        super();
    }


    public PagingRequest(String searchFilter, String sort, int page, int size) {
        super();
        this.searchFilter = searchFilter;
        this.sort = sort;
        this.page = page;
        this.size = size;
    }

    public PagingRequest(String searchFilter) {
        super();
        this.searchFilter = searchFilter;
    }


    public String getSearchFilter() {
        return searchFilter;
    }

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

}
