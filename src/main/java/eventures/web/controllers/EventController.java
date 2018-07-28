package eventures.web.controllers;

import eventures.domain.models.service.EventServiceModel;
import eventures.domain.models.view.EventViewModel;
import eventures.service.services.EventService;
import eventures.utils.MappingUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController extends BaseController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/all")
    public ModelAndView allEvents() {
        return super.view("events/all", "events", this.mapToEventViewModel());
    }

    private List<EventViewModel> mapToEventViewModel() {
        List<EventViewModel> eventViewModels = new ArrayList<>();

        for (EventServiceModel eventServiceModel : this.eventService.extractAllEvents()) {
            eventViewModels.add((EventViewModel) MappingUtil.map(eventServiceModel, EventViewModel.class));
        }

        return eventViewModels;
    }
}
