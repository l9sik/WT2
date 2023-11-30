package com.poluectov.criticine.webapp.service;

import java.util.Map;

public interface Filter {
    Map<String, Object> getFilters();

    void setFilters(Map<String, Object> filters);

    String getSortBy();

    void setSortBy(String sortBy);
    // Constructors, getters, and setters

}
