package com.thegeekylad.marcusbackend.store;

/**
 * This class must mirror what's on the ESP8266 as it
 * contains task names that must match those on the switchboard.
 */
public class Tasks {

    public static final int TASK_STATE_CHANGE = 0;
    public static final int TASK_STATE_REPORT = 1;
    public static final int TASK_TIME_REPORT = 2;
    public static final int TASK_AUTO_LIGHTING = 3;
}
