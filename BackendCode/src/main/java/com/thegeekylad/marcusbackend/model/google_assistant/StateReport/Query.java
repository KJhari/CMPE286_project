package com.thegeekylad.marcusbackend.model.google_assistant.StateReport;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Query {

    @Getter @Setter
    public boolean on;
}
