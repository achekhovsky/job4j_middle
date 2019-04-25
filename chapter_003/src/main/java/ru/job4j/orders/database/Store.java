package ru.job4j.orders.database;

import ru.job4j.orders.Order;

public interface Store {
	boolean add(Order plc);
	boolean deleteOrder(int id);
	boolean updateStatus(int id, boolean status);
	Order[] getOrders();
	Order[] getNotRdyOrders();
}

