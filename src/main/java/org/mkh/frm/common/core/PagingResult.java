package org.mkh.frm.common.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PagingResult<T> {

    private int totalElements;
    private int page;
    private int size;

    @JsonProperty("rows")
    private List<T> items;

    public PagingResult() {

    }

    public PagingResult(int page, int totalElements, int size, List<T> items) {
        super();
        this.page = page;
        this.totalElements = totalElements;
        this.size = size;
        this.items = items;
    }


    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
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

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getTotalPages() {
        if (this.getSize() > 0)
            return (int) Math.ceil(this.getTotalElements() / (this.getSize() * 1.0));

        return 0;
    }

}
