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
}
