package ru.job4j.store;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import ru.job4j.model.Seat;
import org.apache.commons.dbcp2.BasicDataSource;

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

public class SeatStore implements Store<Seat> {
    private static final Logger LOG = LogManager.getLogger(SeatStore.class);
    private final BasicDataSource pool = new BasicDataSource();

    private SeatStore() {
        Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(
                new FileReader("app.properties"))) {
            properties.load(reader);
            Class.forName(properties.getProperty("jdbc.driver"));
        } catch (IOException | ClassNotFoundException e) {
            LOG.error("some trouble with initialization", e);
        }
        pool.setDriverClassName(properties.getProperty("jdbc.driver"));
        pool.setUrl(properties.getProperty("jdbc.url"));
        pool.setUsername(properties.getProperty("jdbc.username"));
        pool.setPassword(properties.getProperty("jdbc.password"));
        pool.setMaxIdle(10);
        pool.setMinIdle(5);
        pool.setMaxOpenPreparedStatements(100);
    }

    @Override
    public int save(Seat element) {
        if (element.getId() != 0) {
            update(element);
        } else {
            int id = create(element);
            element.setId(id);
        }
        return element.getId();
    }

    @Override
    public List<Seat> findAll() {
        List<Seat> result = new ArrayList<>();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from seats")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(new Seat(
                        resultSet.getInt("seat_id"),
                        resultSet.getInt("seat_row"),
                        resultSet.getInt("seat_col"),
                        resultSet.getInt("price"),
                        resultSet.getInt("account_id")));
            }
        } catch (SQLException e) {
            LOG.error("some trouble with sql query or database connection", e);
        }
        return result;
    }

    @Override
    public Seat findById(int id) {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from seats where seat_id = (?)")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Seat(
                        resultSet.getInt("seat_id"),
                        resultSet.getInt("seat_row"),
                        resultSet.getInt("seat_col"),
                        resultSet.getInt("price"),
                        resultSet.getInt("account_id"));
            }
        } catch (SQLException e) {
            LOG.error("some trouble with sql query or database connection", e);
        }
        return null;
    }

    public static SeatStore instOf() {
        return Lazy.INST;
    }

    private int create(Seat seat) {
        int id = 0;
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "insert  into seats (seat_row, seat_col, price) values ((?), (?), (?))",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, seat.getRow());
            statement.setInt(2, seat.getColumn());
            statement.setInt(3, seat.getPrice());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt("seat_id");
            }
        } catch (SQLException e) {
            LOG.error("some trouble with sql query or database connection", e);
        }
        return id;
    }

    private void update(Seat seat) {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "update seats set account_id = (?) where seat_id = (?)")) {
            statement.setInt(1, seat.getAccountId());
            statement.setInt(2, seat.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("some trouble with sql query or database connection", e);
        }
    }

    private static final class Lazy {
        private static final SeatStore INST = new SeatStore();
    }
}
