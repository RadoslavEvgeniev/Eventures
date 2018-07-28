package eventures.web.controllers;

import eventures.domain.models.service.EventServiceModel;
import eventures.domain.models.service.OrderServiceModel;
import eventures.domain.models.view.EventViewModel;
import eventures.service.services.EventService;
import eventures.service.services.OrderService;
import eventures.utils.MappingUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController extends BaseController {

    private final EventService eventService;
    private final OrderService orderService;

    public EventController(EventService eventService, OrderService orderService) {
        this.eventService = eventService;
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public ModelAndView allEvents() {
        return super.view("events/all", "events", this.mapToEventViewModel());
    }

    @GetMapping("/my")
    public ModelAndView myEvents(Principal principal) {
        return super.view("events/my", "events", this.mapToEventViewModel(principal.getName()));
    }

    private List<EventViewModel> mapToEventViewModel() {
        List<EventViewModel> eventViewModels = new ArrayList<>();

        for (EventServiceModel eventServiceModel : this.eventService.extractAllEvents()) {
            eventViewModels.add((EventViewModel) MappingUtil.map(eventServiceModel, EventViewModel.class));
        }

        return eventViewModels;
    }

    private List<EventViewModel> mapToEventViewModel(String username) {
        List<EventViewModel> eventViewModels = new ArrayList<>();

        for (OrderServiceModel orderServiceModel : this.orderService.extractAllOrdersByUsername(username)) {
            EventViewModel eventViewModel = (EventViewModel) MappingUtil.map(orderServiceModel.getEvent(), EventViewModel.class);
            eventViewModel.setNumberOfTickets(orderServiceModel.getTicketsCount());

            eventViewModels.add(eventViewModel);
        }

        return eventViewModels;
    }
}
