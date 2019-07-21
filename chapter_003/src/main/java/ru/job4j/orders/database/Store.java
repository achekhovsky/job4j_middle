package ru.job4j.orders.database;

import ru.job4j.orders.Order;

public interface Store<T> {
	boolean add(T plc);
	boolean deleteOrder(int id);
	boolean updateStatus(int id, boolean status);
	T[] getOrders();
	T[] getNotRdyOrders();
}

