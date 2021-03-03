package ru.job4j.model;

import java.util.Objects;

public class Seat {
    int id;
    int row;
    int column;

    public Seat(int id, int row, int column) {
        this.id = id;
        this.row = row;
        this.column = column;
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
}
