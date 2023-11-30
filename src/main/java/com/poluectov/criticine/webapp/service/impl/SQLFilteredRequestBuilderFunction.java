package com.poluectov.criticine.webapp.service.impl;

import com.poluectov.criticine.webapp.service.Filter;

@FunctionalInterface
public interface SQLFilteredRequestBuilderFunction {

    String apply(Filter filter, int page);

}
