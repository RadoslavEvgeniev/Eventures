package eventures.service.services;

import eventures.domain.models.service.OrderServiceModel;

import java.util.List;

public interface OrderService {

    void importOrder(OrderServiceModel orderServiceModel);

    List<OrderServiceModel> extractAllOrdersByUsername(String username);
}
