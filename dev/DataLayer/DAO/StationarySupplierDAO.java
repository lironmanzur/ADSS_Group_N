package dev.DataLayer.DAO;

import dev.BusinessLayer.SupplierBL.StationarySupplier;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class StationarySupplierDAO {

    private final Connection connection;
    private final DayOfWeekDAO dayOfWeekDAO;

    public StationarySupplierDAO(Connection connection) {
        this.connection = connection;
        this.dayOfWeekDAO = new DayOfWeekDAO(connection);
    }

    public void saveSupplier(StationarySupplier supplier) throws SQLException {
        String insertSupplierQuery = "INSERT INTO stationary_supplier (name, address) VALUES (?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertSupplierQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, supplier.getName());
            insertStmt.setString(2, supplier.getAddress());
            insertStmt.executeUpdate();

            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int supplierId = generatedKeys.getInt(1);
                supplier.setId(supplierId);
                dayOfWeekDAO.saveDeliveryDays(supplierId, supplier.getDeliveryDays());
            }
        }
    }

    public StationarySupplier getSupplier(int id) throws SQLException {
        String query = "SELECT * FROM stationary_supplier WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                StationarySupplier supplier = new StationarySupplier(rs.getString("name"), rs.getString("address"));
                supplier.setId(rs.getInt("id"));
                List<DayOfWeek> deliveryDays = dayOfWeekDAO.getDeliveryDays(id);
                supplier.setDeliveryDays(deliveryDays);
                return supplier;
            }
        }
        return null;
    }

    public class DayOfWeekDAO {

        private final Connection connection;

        public DayOfWeekDAO(Connection connection) {
            this.connection = connection;
        }

        public void saveDeliveryDays(int supplierId, List<DayOfWeek> deliveryDays) throws SQLException {
            String deleteQuery = "DELETE FROM supplier_delivery_days WHERE supplier_id = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, supplierId);
                deleteStmt.executeUpdate();
            }

            String insertQuery = "INSERT INTO supplier_delivery_days (supplier_id, delivery_day) VALUES (?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                for (DayOfWeek day : deliveryDays) {
                    insertStmt.setInt(1, supplierId);
                    insertStmt.setString(2, day.name());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
        }

        public List<DayOfWeek> getDeliveryDays(int supplierId) throws SQLException {
            List<DayOfWeek> deliveryDays = new ArrayList<>();
            String query = "SELECT delivery_day FROM supplier_delivery_days WHERE supplier_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, supplierId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    deliveryDays.add(DayOfWeek.valueOf(rs.getString("delivery_day")));
                }
            }
            return deliveryDays;
        }
    }

}
