package com.yaniv.bookshelf.model.states;

import com.yaniv.bookshelf.model.Invoice;
import com.yaniv.bookshelf.model.enums.OrderStatus;

public class CreatingState extends State {

    public CreatingState(Invoice invoice) {
        super(invoice);
    }

    @Override
    public OrderStatus onCreating() {
        return invoice.getStatus();
    }

    @Override
    public OrderStatus onDeclined() {
        invoice.setState(new DeclinedState(invoice));
        invoice.setStatus(OrderStatus.DECLINED);
        return invoice.getStatus();
    }

    @Override
    public OrderStatus onShipment() {
        invoice.setState(new ShipmentState(invoice));
        invoice.setStatus(OrderStatus.SHIPMENT);
        return invoice.getStatus();
    }

    @Override
    public OrderStatus onCompleted() {
        return invoice.getStatus();
    }
}
