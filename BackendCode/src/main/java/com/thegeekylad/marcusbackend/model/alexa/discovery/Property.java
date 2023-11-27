package com.thegeekylad.marcusbackend.model.alexa.discovery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Property {

    @Getter @Setter
    public Supported[] supported;

    @Getter @Setter
    public boolean retrievable;
}
