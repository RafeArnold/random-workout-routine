package uk.co.rafearnold.randomworkoutroutine.web.model

data class Filter(val searchTerm: String = "", val excludedTerms: Set<String> = setOf())
