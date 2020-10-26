package uk.co.rafearnold.randomworkoutroutine.web.model;

import lombok.Data;

import java.util.Set;

@Data
public class Filter {
    private String searchTerm;
    private Set<String> excludedTerms;
}
