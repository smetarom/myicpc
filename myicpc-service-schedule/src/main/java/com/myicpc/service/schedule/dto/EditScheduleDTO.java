package com.myicpc.service.schedule.dto;

import com.myicpc.model.schedule.Event;

import java.io.Serializable;
import java.util.List;

/**
 * Schedule data holder for bulk edit
 *
 * @author Roman Smetana
 */
public class EditScheduleDTO implements Serializable {
    private static final long serialVersionUID = -6056773973438047322L;

    /**
     * Events which are part of the bulk update
     */
    private List<Event> events;

    /**
     * Constructor
     *
     * @param events events in the bulk update
     */
    public EditScheduleDTO(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }
}
