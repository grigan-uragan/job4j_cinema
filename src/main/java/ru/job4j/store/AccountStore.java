package ru.job4j.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.model.Account;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AccountStore implements Store<Account> {
    private final BasicDataSource pool = new BasicDataSource();

    private AccountStore() {
        Properties properties = new Properties();
        try (BufferedReader reader =
                     new BufferedReader(new FileReader("app.properties"))) {
            properties.load(reader);
            Class.forName(properties.getProperty("jdbc.driver"));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        pool.setDriverClassName(properties.getProperty("jdbc.driver"));
        pool.setUrl(properties.getProperty("jdbc.url"));
        pool.setUsername(properties.getProperty("jdbc.username"));
        pool.setPassword(properties.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    @Override
    public int save(Account element) {
        if (findByTel(element.getTel()) != null) {
            return findByTel(element.getTel()).getId();
        } else {
            int id = 0;
            try (Connection connection = pool.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "insert into accounts (account_name, account_tel) values ((?), (?))",
                         PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, element.getName());
                statement.setString(2, element.getTel());
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    id = resultSet.getInt("account_id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return id;
        }
    }

    @Override
    public List<Account> findAll() {
        List<Account> result = new ArrayList<>();
        try (Connection connection = pool.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select * from accounts")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(new Account(
                        resultSet.getInt("account_id"),
                        resultSet.getString("account_name"),
                        resultSet.getString("account_tel")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Account findById(int id) {
        Account account = null;
        try (Connection connection = pool.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select * from accounts where account_id = (?)")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                account = new Account(
                        resultSet.getInt("account_id"),
                        resultSet.getString("account_name"),
                        resultSet.getString("account_tel")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public Account findByTel(String tel) {
        Account account = null;
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from accounts where account_tel = (?)")) {
            statement.setString(1, tel);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                account = new Account(
                        resultSet.getInt("account_id"),
                        resultSet.getString("account_name"),
                        resultSet.getString("account_tel")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public static AccountStore instOf() {
        return Lazy.INST;
    }

    private static final class Lazy {
        private static final AccountStore INST = new AccountStore();
    }
}
