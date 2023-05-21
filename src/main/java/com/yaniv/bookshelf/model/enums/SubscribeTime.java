package com.yaniv.bookshelf.model.enums;

public enum SubscribeTime {
    MONTH(1), THREE_MONTH(3), SIX_MONTH(6), YEAR(12);
    private final int count;

    SubscribeTime(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
