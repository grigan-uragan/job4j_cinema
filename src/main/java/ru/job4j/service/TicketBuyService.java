package ru.job4j.service;

import ru.job4j.model.Account;
import ru.job4j.model.Seat;
import ru.job4j.store.AccountStore;
import ru.job4j.store.Store;

import java.util.ArrayList;
import java.util.List;

public class TicketBuyService {
    private Store<Seat> hall;

    public TicketBuyService(Store<Seat> hall) {
        this.hall = hall;
    }

    public List<Seat> showHall() {
        List<Seat> result = hall.findAll();
        if (result.isEmpty()) {
            openHall(init());
            result = hall.findAll();
        }
        return result;
    }

    private List<Seat> init() {
        List<Seat> result = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            for (int j = 1; j <= 7; j++) {
                int price = 100;
                if (i >= 2 && j >= 2 && i < 5 && j < 5) {
                    price += 50;
                }
                result.add(new Seat(i, j, price));
            }
        }
        return result;
    }

    public void saleTicket(List<Seat> seats, Account account) {
        int id = AccountStore.instOf().save(account);
        account.setId(id);
        for (Seat seat : seats) {
            seat.setAccountId(account.getId());
            hall.save(seat);
        }
    }

    public Seat getSeat(int id) {
        return hall.findById(id);
    }

    public void openHall(List<Seat> emptySeat) {
        emptySeat.forEach(hall::save);
    }
}
