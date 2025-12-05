package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Enum.EventType;
import edu.dosw.rideci.domain.model.NotificationEvent;

import java.util.List;

public interface NotificationSubscriber {

    void handleEvent(NotificationEvent event);

    List<EventType> getSubscribedEvents();

    String getName();
}
