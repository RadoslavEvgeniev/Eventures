package eventures.web.controllers;

import eventures.domain.models.binding.EventCreateBindingModel;
import eventures.domain.models.service.EventServiceModel;
import eventures.domain.models.service.OrderServiceModel;
import eventures.domain.models.view.EventViewModel;
import eventures.service.services.EventService;
import eventures.service.services.OrderService;
import eventures.utils.MappingUtil;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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

    @GetMapping("/create")
    public ModelAndView createEvent(@ModelAttribute(name = "eventCreateBindingModel") EventCreateBindingModel eventCreateBindingModel) {
        return super.view("events/create", "eventCreateBindingModel", eventCreateBindingModel);
    }

    @PostMapping("/create")
    public ModelAndView createEventConfirm(@Valid @ModelAttribute(name = "eventCreateBindingModel") EventCreateBindingModel eventCreateBindingModel, BindingResult bindingResult) {
        if (eventCreateBindingModel.getStart().isAfter(eventCreateBindingModel.getEnd())) {
            bindingResult
                    .addError(new FieldError("eventCreateBindingModel"
                            , "start"
                            , "Start date cannot be after end date.")
                    );

        } else if (eventCreateBindingModel.getEnd().isBefore(eventCreateBindingModel.getStart())) {
            bindingResult
                    .addError(new FieldError("eventCreateBindingModel"
                            , "end"
                            , "End date cannot be before start date.")
                    );
        }

        if (bindingResult.hasErrors()) {
            return super.view("events/create", "eventCreateBindingModel", eventCreateBindingModel);
        }

        this.eventService.importEvent((EventServiceModel) MappingUtil.map(eventCreateBindingModel, EventServiceModel.class));

        return super.redirect("/events/all");
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
