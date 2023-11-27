package com.thegeekylad.marcusbackend.model.google_assistant.discovery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Name {

    @Getter @Setter
    public String[] defaultNames;

    @Getter @Setter
    public String name;

    @Getter @Setter
    public String[] nicknames;
}
