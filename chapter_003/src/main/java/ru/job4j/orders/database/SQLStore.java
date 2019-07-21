package ru.job4j.orders.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import ru.job4j.orders.Order;

public class SQLStore implements Store<Order> {
	private static final Logger LOG = Logger.getLogger(SQLStore.class.getName());
	private static final SQLStore STORE = new SQLStore();
	private final DataSource source;
	
	private SQLStore() {
		this.source = this.getSource();
		this.initTable();
		//TODO Change double quotes to single quotes, insert done and version values 
		this.add(new Order(1, "order_1", "description_1"));
		this.add(new Order(2, "order_2", "description_2"));
		this.add(new Order(3, "order_3", "description_3"));
	}
	
	public static SQLStore getInstance() {
		return STORE;
	}

	@Override
	public boolean add(Order ord) {
		try (Connection conn = this.source.getConnection();
				PreparedStatement stmtU = conn
						.prepareStatement("INSERT INTO orders (id, createDate, name, description, done) VALUES (?, ?, ?, ?, ?)")) {
			stmtU.setInt(1, ord.getId());
			stmtU.setString(2, ord.getCreateDate().toString());
			stmtU.setString(3, ord.getName());
			stmtU.setString(4, ord.getDescription());
			stmtU.setBoolean(5, ord.isDone());
			stmtU.executeUpdate();
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "SQLStore::add", e);
		}
		return true;
	}

	@Override
	public boolean deleteOrder(int id) {
		try (Connection conn = this.source.getConnection();
				PreparedStatement stmtU = conn.prepareStatement("DELETE FROM orders WHERE id = ?")) {
			stmtU.setInt(1, id);
			stmtU.executeUpdate();
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "SQLStore::deleteOrder", e);
		}
		return true;
	}

	@Override
	public boolean updateStatus(int id, boolean status) {
		try (Connection conn = this.source.getConnection();
				PreparedStatement stmtU = conn.prepareStatement("UPDATE orders SET done = ? WHERE id = ?")) {
			stmtU.setInt(2, id);
			stmtU.setBoolean(1, status);
			stmtU.executeUpdate();
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "SQLStore::updateStatus", e);
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Order[] getOrders() {
		ArrayList<Order> ords = new ArrayList<>();
		try (Connection conn = this.source.getConnection()) {
				Statement stmt = conn.createStatement(); 
			try (ResultSet rs = stmt.executeQuery("SELECT * FROM orders")) {
				while (rs.next()) {
					ords.add(new Order(rs.getInt("id"), rs.getString("createDate"), rs.getString("name"), rs.getString("description"), rs.getBoolean("done")));
				}
			}
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "SQLStore::getOrders", e);
		}
		return ords.toArray(new Order[ords.size()]);
	}

	@Override
	public Order[] getNotRdyOrders() {
		ArrayList<Order> ords = new ArrayList<>();
		try (Connection conn = this.source.getConnection()) {
				Statement stmt = conn.createStatement(); 
			try (ResultSet rs = stmt.executeQuery("SELECT * FROM orders WHERE done = 0")) {
				while (rs.next()) {
					ords.add(new Order(rs.getInt("id"), rs.getString("createDate"), rs.getString("name"), rs.getString("description"), false));
				}
			}
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "SQLStore::getOccupiedPlaces", e);
		}
		return ords.toArray(new Order[ords.size()]);
	}

	private DataSource getSource() {
		DataSource ds = null;
		try {
			InitialContext cxt = new InitialContext();
			ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/SQLite/Orders");
		} catch (NamingException e) {
			LOG.log(Level.SEVERE, "SQLStore::getSource", e);
		}
		return ds;
	}

	private void initTable() {
		try (Connection conn = this.source.getConnection(); Statement stmt = conn.createStatement()) {
			stmt.execute(
					"CREATE TABLE IF NOT EXISTS orders (id integer PRIMARY KEY, createDate text NOT NULL, name varchar(25) NOT NULL, description text, done boolean NOT NULL)");
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "SQLStore::initTable", e);
		}
	}
}
