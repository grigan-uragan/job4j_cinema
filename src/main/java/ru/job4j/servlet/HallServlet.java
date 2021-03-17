package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

public class HallServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        TicketBuyService service = new TicketBuyService(SeatStore.instOf());
        List<Seat> seats = service.showHall();
        Gson gson = new GsonBuilder().create();
        String result = gson.toJson(seats);
        resp.setContentType("json");
        resp.getWriter().write(result);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String form = req.getParameter("norm");
        String[] strings = form.split("&");
        List<Integer> ids = new ArrayList<>();
        for (String str : strings) {
            String s = str.substring(0, str.indexOf('='));
            int id = Integer.parseInt(s);
            ids.add(id);
        }
        TicketBuyService service = new TicketBuyService(SeatStore.instOf());
        List<Seat> seats = new ArrayList<>();
        ids.forEach(integer -> seats.add(service.getSeat(integer)));
        Gson gson = new GsonBuilder().create();
        resp.getWriter().write(gson.toJson(seats));
    }
}
