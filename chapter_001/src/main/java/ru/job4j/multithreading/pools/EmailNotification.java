package ru.job4j.multithreading.pools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EmailNotification {
	private final ExecutorService pool = Executors.newFixedThreadPool(
				Runtime.getRuntime().availableProcessors());
	
	public void emailTo(User user) {
		String subject = String.format("Notification %s to email %s.", user.username, user.email);
		String body = String.format("Add a new event to %s.", user.username);
		pool.execute(new Runnable() {
			@Override
			public void run() {
				send(subject, body, user.email);
			}
		});
	}
	
	private boolean send(String subject, String body, String email) {
		System.out.printf("Sended: %s \n", subject);
		return true;
	}
	
	public void close() {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(2, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(2, TimeUnit.SECONDS)) {
					System.err.println("Pool did not terminate");
				}
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
		System.out.println("NOTIFICATOR IS CLOSED");
	}
	
	public boolean isClosed() {
		return this.pool.isTerminated();
	}
}
