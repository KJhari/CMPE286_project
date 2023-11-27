package com.thegeekylad.marcusbackend.model.google_assistant.discovery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attributes {

    @Getter @Setter
    public boolean pausable;
}
