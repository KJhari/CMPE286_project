package com.thegeekylad.marcusbackend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleResponse {

    @Getter @Setter @JsonProperty(required = true)
    public String action;

    @Getter @Setter
    public Object result;

    @Getter @Setter
    public String error;
}
