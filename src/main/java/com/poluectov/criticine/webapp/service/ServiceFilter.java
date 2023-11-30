package com.poluectov.criticine.webapp.service;

import java.util.Map;

public class ServiceFilter implements Filter {
        private Map<String, Object> filters;
        private String sortBy;

        public ServiceFilter(Map<String, Object> filters, String sortBy) {
            this.filters = filters;
            this.sortBy = sortBy;
        }

        public Map<String, Object> getFilters() {
            return filters;
        }

        public void setFilters(Map<String, Object> filters) {
            this.filters = filters;
        }

        public String getSortBy() {
            return sortBy;
        }

        public void setSortBy(String sortBy) {
            this.sortBy = sortBy;
        }
        // Constructors, getters, and setters

        // Other methods as needed
}
