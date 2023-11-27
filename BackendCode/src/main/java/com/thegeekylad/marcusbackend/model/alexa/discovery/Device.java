package com.thegeekylad.marcusbackend.model.alexa.discovery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Device {

    @Getter @Setter
    public String endpointId;

    @Getter @Setter
    public String manufacturerName;

    @Getter @Setter
    public String friendlyName;

    @Getter @Setter
    public String description;

    @Getter @Setter
    public String[] displayCategories;

    @Getter @Setter
    public Capability[] capabilities;
}
