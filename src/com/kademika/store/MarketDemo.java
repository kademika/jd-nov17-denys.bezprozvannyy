package com.kademika.store;

import com.kademika.store.Market.Market;
import com.kademika.store.Market.MarketUI;

public class MarketDemo {

    public static void main(String[] args) {

        Market m = new Market();
        m.init();
        new MarketUI(m);

    }

}
