package ru.job4j.model;

import java.util.Objects;

public class Account {
    private int id;
    private String name;
    private String tel;

    public Account(String name, String tel) {
        this.name = name;
        this.tel = tel;
    }

    public Account(int id, String name, String tel) {
        this.id = id;
        this.name = name;
        this.tel = tel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return id == account.id && Objects.equals(name, account.name)
                && Objects.equals(tel, account.tel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tel);
    }
}
