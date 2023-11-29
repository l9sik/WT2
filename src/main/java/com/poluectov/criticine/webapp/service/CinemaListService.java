package com.poluectov.criticine.webapp.service;

import com.poluectov.criticine.webapp.model.data.Cinema;
import com.poluectov.criticine.webapp.model.data.CinemaType;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CinemaListService {
    public class Filter {
        private Map<String, Object> filters;
        private String sortBy;

        public Filter(Map<String, Object> filters, String sortBy) {
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

    List<Cinema> getCinemaList(Filter filter, int page) throws SQLException;
    List<Cinema> getCinemaList() throws SQLException;

    int getCinemaListCount(Filter filter) throws SQLException;
    int getCinemaListCount() throws SQLException;

}
