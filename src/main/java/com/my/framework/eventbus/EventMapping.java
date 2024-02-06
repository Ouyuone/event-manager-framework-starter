package com.my.framework.eventbus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@AllArgsConstructor
public class EventMapping<T extends Event> implements Iterable<EventHandler<T>> {

    @Getter
    private final Class<T> clazz;

    final List<EventHandler<T>> handlers = new ArrayList<>();

    @Override
    public Iterator<EventHandler<T>> iterator() {
        return handlers.iterator();
    }

}