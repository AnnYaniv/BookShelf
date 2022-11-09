package com.yaniv.bookshelf.model.states;

import com.yaniv.bookshelf.model.Invoice;
import com.yaniv.bookshelf.model.enums.OrderStatus;

public abstract class State {
    protected Invoice invoice;

    protected State(Invoice invoice) {
        this.invoice = invoice;
    }

    public abstract OrderStatus onCreating();
    public abstract OrderStatus onDeclined();
    public abstract OrderStatus onShipment();
    public abstract OrderStatus onCompleted();
}
