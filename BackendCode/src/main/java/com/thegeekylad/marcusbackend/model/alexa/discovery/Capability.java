package com.thegeekylad.marcusbackend.model.alexa.discovery;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Capability {

    @Getter @Setter
    public String type;

    @JsonProperty(value = "interface")
    @Getter @Setter
    public String _interface;

    @Getter @Setter
    public String version;

    @Getter @Setter
    public Property properties;
}
