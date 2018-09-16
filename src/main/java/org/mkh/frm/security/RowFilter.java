package org.mkh.frm.security;

import java.util.Map;

public class RowFilter {

    private String filterName;
    private Map<String, Object> filterValues;
    private boolean enabled = true;

    public RowFilter(String filterName, Map<String, Object> filterValues, Boolean enabled) {
        super();
        this.filterName = filterName;
        this.filterValues = filterValues;
        this.enabled = enabled;
    }

    public RowFilter(String filterName, Map<String, Object> filterValues) {
        super();
        this.filterName = filterName;
        this.filterValues = filterValues;
        this.enabled = true;
    }

    public RowFilter() {
        super();
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public Map<String, Object> getFilterValues() {
        return filterValues;
    }

    public void setFilterValues(Map<String, Object> filterValues) {
        this.filterValues = filterValues;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
