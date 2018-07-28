package eventures.web.controllers;

import eventures.domain.models.service.OrderServiceModel;
import eventures.service.services.EventService;
import eventures.service.services.OrderService;
import eventures.service.services.UserService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController {

    private final OrderService orderService;
    private final UserService userService;
    private final EventService eventService;
    private final JmsTemplate jmsTemplate;


    public OrderController(OrderService orderService, UserService userService, EventService eventService, JmsTemplate jmsTemplate) {
        this.orderService = orderService;
        this.userService = userService;
        this.eventService = eventService;
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping("/{id}")
    public ModelAndView order(@PathVariable(name = "id") String id, @RequestParam(name = "numberOfTickets") Integer numberOfTickets, Principal principal) {
//        this.orderService.importOrder(this.mapToOrderServiceModel(id, principal.getName(), numberOfTickets));
        this.sendOrderMessage(id, principal.getName(), numberOfTickets);

        return super.redirect("/home");
    }

//    private OrderServiceModel mapToOrderServiceModel(String eventId, String customerName, int ticketsCount) {
//        OrderServiceModel orderServiceModel = new OrderServiceModel();
//        orderServiceModel.setEvent(this.eventService.extractEventById(eventId));
//        orderServiceModel.setCustomer(this.userService.extractUserByUsername(customerName));
//        orderServiceModel.setOrderedOn(LocalDateTime.now());
//        orderServiceModel.setTicketsCount(ticketsCount);
//
//        return orderServiceModel;
//    }

    private void sendOrderMessage(String id, String name, Integer numberOfTickets) {
        StringBuilder orderMessageSb = new StringBuilder();
        orderMessageSb.append(id).append(" ").append(name).append(" ").append(numberOfTickets);

        this.jmsTemplate.convertAndSend(orderMessageSb.toString());
    }
}
