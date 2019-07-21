package ru.job4j.orders.database;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.job4j.orders.Order;

public class JPAStore implements Store<Order> {
	private static final Logger LOG = LogManager.getLogger("customLog");
	private static final JPAStore STORE = new JPAStore();
	private final EntityManagerFactory emf;
	
	private JPAStore() {
		emf = Persistence.createEntityManagerFactory("order_unit_sqlite");
	}
	
	public static JPAStore getInstance() {
		return STORE;
	}

	@Override
	public boolean add(Order ord) {
		// TODO Auto-generated method stub
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(ord);
		if (ord.getImage() != null) {
			em.persist(ord.getImage());
		}
		tx.commit();
		em.close();
		return true;
	}

	@Override
	public boolean deleteOrder(int id) {
		Order forDel = null;
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
/*		Query query = em.createNamedQuery("deleteOrder").setParameter("oId", id);
		query.executeUpdate();*/
		forDel = em.find(Order.class, id);
		em.remove(forDel);
		tx.commit();
		em.close();
		return true;
	}

	@Override
	public boolean updateStatus(int id, boolean status) {
//		Order forDel = null;
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Query query = em.createNamedQuery("updateStatus")
				.setParameter("oRdy", status)
				.setParameter("oId", id);
		query.executeUpdate();
//		forDel = em.find(Order.class, id);
//		forDel.setDone(status);
		tx.commit();
		em.close();
		return true;
	}

	@Override
	public Order[] getOrders() {
		EntityManager em = emf.createEntityManager();
		Query query = em.createNamedQuery("selectOrders");
		@SuppressWarnings("unchecked")
		List<Order> orders = (List<Order>) query.getResultList();
		em.close();
		return orders == null ? null : orders.toArray(new Order[orders.size()]);
	}

	@Override
	public Order[] getNotRdyOrders() {
		List<Order> orders = null;
		EntityManager em = emf.createEntityManager();
		
		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<Order> criteria = builder.createQuery(Order.class);
		Root<Order> root = criteria.from(Order.class);
		criteria.select(root);
		criteria.where(builder.equal(root.get("done"), false));
		orders = em.createQuery(criteria).getResultList();
		em.close();
		return orders == null ? null : orders.toArray(new Order[orders.size()]);
	}

}
