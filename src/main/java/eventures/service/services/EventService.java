package eventures.service.services;

import eventures.domain.models.service.EventServiceModel;

import java.util.List;

public interface EventService {

    List<EventServiceModel> extractAllEvents();
}
