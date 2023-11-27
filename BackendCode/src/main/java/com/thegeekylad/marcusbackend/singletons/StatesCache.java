package com.thegeekylad.marcusbackend.singletons;

import java.util.HashMap;
import java.util.Map;

public class StatesCache {
    
    // state refresh
    public Map<String, Integer> states;
    
    private static StatesCache statesCache;

    public StatesCache() {
        this.states = new HashMap<>();
    }

    public static StatesCache getInstance() {
        if (statesCache == null) statesCache = new StatesCache();
        return statesCache;
    }
}
