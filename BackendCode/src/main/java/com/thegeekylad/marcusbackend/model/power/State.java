package com.thegeekylad.marcusbackend.model.power;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class State {

    @Getter @Setter
    public int deviceId;

    @Getter @Setter
    public String boardTopic;

    @Getter @Setter
    public int state;

    @Getter @Setter
    public boolean reloadForStateRefresh;

    public State(int deviceId, String boardTopic) {
        this.deviceId = deviceId;
        this.boardTopic = boardTopic;
    }

    // deep copy
    public State(State anotherState) {
        this.setDeviceId(anotherState.getDeviceId());
        this.setBoardTopic(anotherState.getBoardTopic());
        this.setState(anotherState.getState());
        this.setReloadForStateRefresh(anotherState.isReloadForStateRefresh());
    }

    public static boolean isSameDevice(State state1, State state2) {
        return state1.getDeviceId() == state2.getDeviceId()
                && state1.getBoardTopic().equals(state2.getBoardTopic());
    }
}
