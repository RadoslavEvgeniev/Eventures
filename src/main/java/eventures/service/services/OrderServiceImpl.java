package eventures.service.services;

import eventures.domain.entities.Order;
import eventures.domain.models.service.OrderServiceModel;
import eventures.service.repositories.EventRepository;
import eventures.service.repositories.OrderRepository;
import eventures.utils.MappingUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final EventRepository eventRepository;

    public OrderServiceImpl(OrderRepository orderRepository, EventRepository eventRepository) {
        this.orderRepository = orderRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public void importOrder(OrderServiceModel orderServiceModel) {
        if (orderServiceModel.getEvent().getTotalTickets() - orderServiceModel.getTicketsCount() > 0) {
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
}
