package com.thegeekylad.marcusbackend.model.alexa.StateReport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class StateReport {

    @Getter @Setter
    public Context context;

    @Getter @Setter
    public Event event;
}
