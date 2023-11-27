package com.thegeekylad.marcusbackend.controller;

import com.thegeekylad.marcusbackend.model.AttentionGrabberBody;
import com.thegeekylad.marcusbackend.model.SimpleResponse;
import com.thegeekylad.marcusbackend.store.Actions;
import com.thegeekylad.marcusbackend.util.Helpers;
import org.springframework.web.bind.annotation.*;

import static com.thegeekylad.marcusbackend.util.Helpers.getTimestamp;

@RestController
public class SimpleController {

    @GetMapping("/")
    public String greeting() {
        Helpers.log("Served a welcome request.", true);
        return "Welcome to Marcus Smart Home! Drop an email to rahul.pillai03@gmail.com to know more. :)";
    }

    @PostMapping("/attentionGrabberLogin")
    @CrossOrigin("*")
    public SimpleResponse attentionGrabberLogin(@RequestBody AttentionGrabberBody body) {
        if (body.getPin() == 110798)
            return new SimpleResponse(Actions.ATTENTION_GRAB, null, null);
        else {
            return new SimpleResponse(Actions.ATTENTION_GRAB, null, "Incorrect password.");
        }
    }
}
