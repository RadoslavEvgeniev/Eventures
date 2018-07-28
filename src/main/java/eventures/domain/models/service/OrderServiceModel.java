package eventures.domain.models.service;

import java.time.LocalDateTime;

public class OrderServiceModel {

    private String id;
    private LocalDateTime orderedOn;
    private EventServiceModel event;
    private UserServiceModel customer;
    private Integer ticketsCount;

    public OrderServiceModel() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getOrderedOn() {
        return this.orderedOn;
    }

    public void setOrderedOn(LocalDateTime orderedOn) {
        this.orderedOn = orderedOn;
    }

    public EventServiceModel getEvent() {
        return this.event;
    }

    public void setEvent(EventServiceModel event) {
        this.event = event;
    }

    public UserServiceModel getCustomer() {
        return this.customer;
    }

    public void setCustomer(UserServiceModel customer) {
        this.customer = customer;
    }

    public Integer getTicketsCount() {
        return this.ticketsCount;
    }

    public void setTicketsCount(Integer ticketsCount) {
        this.ticketsCount = ticketsCount;
    }
}
