package com.thegeekylad.marcusbackend.model.power;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
public class States {

    @Getter @Setter
    public List<State> states;
}
