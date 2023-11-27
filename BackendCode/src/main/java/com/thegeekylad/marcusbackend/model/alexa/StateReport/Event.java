package com.thegeekylad.marcusbackend.model.alexa.StateReport;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event {

    @Getter @Setter
    public Endpoint endpoint;
}
