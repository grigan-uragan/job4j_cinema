package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.job4j.model.Account;
import ru.job4j.model.Seat;
import ru.job4j.service.TicketBuyService;
import ru.job4j.store.SeatStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReserveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        TicketBuyService service = new TicketBuyService(SeatStore.instOf());
        Gson gson = new GsonBuilder().create();
        String name = req.getParameter("name");
        String tel = req.getParameter("tel");
        String seat = req.getParameter("seats");
        Type type = new TypeToken<List<Seat>>() { }.getType();
        List<Seat> res = gson.fromJson(seat, type);
        Account account = new Account(name, tel);
        service.saleTicket(res, account);
    }
}
