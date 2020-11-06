package com.foursoft.xml.io.write;

import javax.xml.bind.Marshaller.Listener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarshallerListener extends Listener {

    private final List<Listener> listeners;

    public MarshallerListener() {
        listeners = new ArrayList<>();
    }

    public MarshallerListener(final Listener... listeners) {
        this.listeners = Arrays.asList(listeners);
    }

    public void addListener(final Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void beforeMarshal(final Object source) {
        listeners.forEach(c -> c.beforeMarshal(source));
    }
}
