package com.thegeekylad.marcusbackend.model.alexa.StateReport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Value {

    @Getter @Setter
    public String scale;

    @Getter @Setter
    public String value;
}
