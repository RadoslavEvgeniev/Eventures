package eventures.service.services;

import eventures.domain.entities.Order;
import eventures.domain.models.service.OrderServiceModel;
import eventures.service.repositories.EventRepository;
import eventures.service.repositories.OrderRepository;
import eventures.utils.MappingUtil;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository, EventRepository eventRepository, EventService eventService, UserService userService) {
        this.orderRepository = orderRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.userService = userService;
    }

    @Override
    public void importOrder(OrderServiceModel orderServiceModel) {
        if (orderServiceModel.getEvent().getTotalTickets() - orderServiceModel.getTicketsCount() < 0) {
            throw new IllegalArgumentException("Not enough tickets.");
        }

        orderServiceModel.getEvent().setTotalTickets(orderServiceModel.getEvent().getTotalTickets() - orderServiceModel.getTicketsCount());

        Order order = (Order) MappingUtil.map(orderServiceModel, Order.class);

        this.eventRepository.save(order.getEvent());
        this.orderRepository.save(order);
    }

    @Override
    public List<OrderServiceModel> extractAllOrdersByUsername(String username) {
        List<Order> ordersFromDb = this.orderRepository.findAllByCustomerUsername(username);
        List<OrderServiceModel> orderServiceModels = new ArrayList<>();

        for (Order order : ordersFromDb) {
            orderServiceModels.add((OrderServiceModel) MappingUtil.map(order, OrderServiceModel.class));
        }

        return orderServiceModels;
    }

    @JmsListener(destination = "message-queue")
    private void readOrderMessage(Message<String> message) {
        String[] orderMessageParams = message.getPayload().split(" ");
        OrderServiceModel orderServiceModel = this.mapToOrderServiceModel(orderMessageParams[0], orderMessageParams[1], Integer.parseInt(orderMessageParams[2]));

        this.importOrder(orderServiceModel);
    }

    private OrderServiceModel mapToOrderServiceModel(String eventId, String customerName, int ticketsCount) {
        OrderServiceModel orderServiceModel = new OrderServiceModel();
        orderServiceModel.setEvent(this.eventService.extractEventById(eventId));
        orderServiceModel.setCustomer(this.userService.extractUserByUsername(customerName));
        orderServiceModel.setOrderedOn(LocalDateTime.now());
        orderServiceModel.setTicketsCount(ticketsCount);

        return orderServiceModel;
    }
}
