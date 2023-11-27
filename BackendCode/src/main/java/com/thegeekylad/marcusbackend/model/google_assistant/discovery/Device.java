package com.thegeekylad.marcusbackend.model.google_assistant.discovery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Device {

    @Getter @Setter
    public String id;

    @Getter @Setter
    public String type;

    @Getter @Setter
    public String[] traits;

    @Getter @Setter
    public Name name;

    @Getter @Setter
    public boolean willReportState;

    @Getter @Setter
    public Attributes attributes;
}
