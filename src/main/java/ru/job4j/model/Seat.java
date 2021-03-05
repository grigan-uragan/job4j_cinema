package ru.job4j.model;

import java.util.Objects;

public class Seat {
    private int id;
    private int row;
    private int column;
    private int price;
    private int accountId;

    public Seat(int row, int column, int price) {
        this.row = row;
        this.column = column;
        accountId = 0;
        this.price = price;
    }

    public Seat(int id, int row, int column, int price, int accountId) {
        this.id = id;
        this.row = row;
        this.column = column;
        this.price = price;
        this.accountId = accountId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Seat seat = (Seat) o;
        return id == seat.id && row == seat.row && column == seat.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, row, column);
    }

    @Override
    public String toString() {
        return "Seat{" + "id=" + id + ", row=" + row
                + ", column=" + column + ", price=" + price
                + ", accountId=" + accountId + '}';
    }
}
