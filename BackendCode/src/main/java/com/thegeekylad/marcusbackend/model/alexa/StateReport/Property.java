package com.thegeekylad.marcusbackend.model.alexa.StateReport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Property {

    @Getter @Setter
    public String namespace;

    @Getter @Setter
    public String name;

    @Getter @Setter
    public String value;

    @Getter @Setter
    public String timeOfSample;

    @Getter @Setter
    public int uncertaintyInMilliseconds;
}
