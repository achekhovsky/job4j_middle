package ru.job4j.orders.servlets;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.job4j.orders.Order;
import ru.job4j.orders.database.SQLStore;
import ru.job4j.orders.json.Parser;

/**
 * Servlet implementation class HallController
 */
public class OrdersController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ru.job4j.orders.database.Store store = SQLStore.getInstance();
	private boolean showRdyFlag = true;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		if (request.getParameter("getAction") != null) {
			switch (request.getParameter("getAction")) {
			case "getOrders":
				break;
			case "getNotRdy": 
				showRdyFlag = !showRdyFlag;
				break;
			case "addOrder":
				Order ord = new Order(
						Integer.parseInt(request.getParameter("id").toString()), 
						request.getParameter("ordername").toString(), 
						request.getParameter("orderdescription").toString());
				store.add(ord);
	 			break;
			case "updateStatus":
				store.updateStatus(Integer.parseInt(request.getParameter("id").toString()), 
						Boolean.parseBoolean(request.getParameter("done").toString()));
	 			break;
	 		case "deleteOrder":
	 			store.deleteOrder(Integer.parseInt(request.getParameter("id").toString()));
	 			break;
	 		default:
	 			break;
			}
			if (showRdyFlag) {
				this.sendData(response, Parser.parseToJson(store.getOrders())); 
			} else {
				this.sendData(response, Parser.parseToJson(store.getNotRdyOrders()));
			}
		}
		doGet(request, response);
	}
	
	private void sendData(HttpServletResponse response, String data) throws ServletException, IOException {
		response.reset();
		response.setContentType("text/plain");
		response.getWriter().append(data);
		response.getWriter().flush();
	}
}
