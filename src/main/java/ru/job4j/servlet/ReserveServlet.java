package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.ArrayList;
import java.util.List;

public class ReserveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        TicketBuyService service = new TicketBuyService(SeatStore.instOf());
        List<Seat> seats = service.showHall();
        List<Seat> result = new ArrayList<>();
        for (Seat seat : seats) {
            String param = "seat" + seat.getId();
            String reqParameter = req.getParameter(param);
            if ("on".equals(reqParameter)) {
                result.add(seat);
            }
        }
        String name = req.getParameter("account");
        String tel = req.getParameter("tel");
        Account account = new Account(name, tel);
        service.saleTicket(result, account);
        Gson gson = new GsonBuilder().create();
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.println(gson.toJson(result));
        System.out.println(gson.toJson(result));
        writer.flush();
    }
}
