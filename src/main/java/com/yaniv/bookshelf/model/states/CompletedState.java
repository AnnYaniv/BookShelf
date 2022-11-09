package com.yaniv.bookshelf.model.states;

import com.yaniv.bookshelf.model.Invoice;
import com.yaniv.bookshelf.model.enums.OrderStatus;

public class CompletedState extends State {
    public CompletedState(Invoice invoice) {
        super(invoice);
    }

    @Override
    public OrderStatus onCreating() {
        return invoice.getStatus();
    }

    @Override
    public OrderStatus onDeclined() {
        return invoice.getStatus();
    }

    @Override
    public OrderStatus onShipment() {
        return invoice.getStatus();
    }

    @Override
    public OrderStatus onCompleted() {
        return invoice.getStatus();
    }
}
