package eventures.service.services;

import eventures.domain.entities.Event;
import eventures.domain.models.service.EventServiceModel;
import eventures.service.repositories.EventRepository;
import eventures.utils.MappingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    @Override
    public List<EventServiceModel> extractAllEvents() {
        List<Event> eventsFromDb = this.eventRepository.findAll();
        List<EventServiceModel> eventServiceModels = new ArrayList<>();

        for (Event event : eventsFromDb) {
            eventServiceModels.add((EventServiceModel) MappingUtil.map(event, EventServiceModel.class));
        }

        return eventServiceModels;
    }

    @Override
    public EventServiceModel extractEventById(String id) {
        Event eventFromDb = this.eventRepository.findById(id).orElse(null);

        if (eventFromDb == null) {
            throw new IllegalArgumentException("Invalid event id.");
        }

        return (EventServiceModel) MappingUtil.map(eventFromDb, EventServiceModel.class);
    }

    @Override
    public void importEvent(EventServiceModel eventServiceModel) {
        this.eventRepository.save((Event) MappingUtil.map(eventServiceModel, Event.class));
    }
}
