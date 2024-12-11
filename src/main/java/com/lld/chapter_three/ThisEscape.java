package com.lld.chapter_three;

import jdk.jfr.Event;

import java.util.List;
import java.util.ArrayList;
import java.util.EventListener;

class EventSource {
    private List<EventListener> listeners = new ArrayList<>();

    public void registerListener(EventListener l) {
        listeners.add(l);
        // What if another thread is iterating over listeners right now?
        // It might see our partially constructed object!
    }
}

public class ThisEscape {
    private void doSmtg(Event e) {}

    public ThisEscape(EventSource source) {
        source.registerListener(new EventListener(){
            public void onEvent(Event e) {
                doSmtg(e);
            }
        });
    }
}
