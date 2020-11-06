package com.foursoft.xml.io.write.processinginstructions;

import java.util.*;

public class ProcessingInstructions {

    private final Map<Object, List<ProcessingInstruction>> map = new HashMap<>();

    public boolean containsKey(final Object key) {
        return map.containsKey(key);
    }

    public List<ProcessingInstruction> get(final Object key) {
        return map.getOrDefault(key, Collections.emptyList());
    }

    public void put(final Object key, final List<ProcessingInstruction> processingInstruction) {
        Objects.requireNonNull(key);
        map.put(key, processingInstruction);
    }

    public void put(final Object key, final ProcessingInstruction... processingInstruction) {
        Objects.requireNonNull(key);
        map.put(key, Arrays.asList(processingInstruction));
    }

}
