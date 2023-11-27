package com.thegeekylad.marcusbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Device {
    @Id
    @Getter @Setter
    public String id;

    @Getter @Setter
    public String board;

    @Getter @Setter
    public String device;

    @Getter @Setter
    public int deviceId;

    @Getter @Setter
    public String boardTopic;

    @Getter @Setter
    public String description;

    @Getter @Setter
    public String type;

    @Getter @Setter
    public List<String> controls;

    public Device(@JsonProperty(required = true) String board,
                  @JsonProperty(required = true) String device,
                  @JsonProperty(required = true) int deviceId,
                  @JsonProperty(required = false) String boardTopic,
                  @JsonProperty(required = true) String description,
                  @JsonProperty(required = true) String type,
                  @JsonProperty(required = true) List<String> controls) {
        this.board = board;
        this.device = device;
        this.deviceId = deviceId;
        this.boardTopic = boardTopic;
        this.description = description;
        this.type = type;
        this.controls = controls;
    }
}
