package ru.job4j.multithreading.pools;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;

public class EmailNotificationTest {
	private final ExecutorService pool = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors());
	private final int notifyAttempts = 15;
	private volatile boolean fl;
	private EmailNotification en; 
	private BlockingQueue<User> forSend;
	private int sended;
	

	@Before
	public void setUp() throws Exception {
		en	= new EmailNotification();
		forSend = new ArrayBlockingQueue<>(10);
		fl = true;
		sended = 0;
	}

	@Test
	public void ifNotifyAttemptsIsFifteenThenSendedIsFifteen() throws InterruptedException {
		final Thread offer = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < notifyAttempts; i++) {
					forSend.offer(new User("username_" + i, "email_" + i));
					try {
						sleepOrNotSleep(forSend.size() >= 9);
					} catch (InterruptedException e) {
						fl = false;
						Thread.currentThread().interrupt();
					}
				}
				fl = false;
			}
		});

		final Thread poll = new Thread(new Runnable() {
			@Override
			public void run() {
				while (fl || forSend.size() > 0) {
					if (forSend.size() > 0) {
						en.emailTo(forSend.poll());
						sended++;
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				en.close();
			}
		});
		
		pool.submit(offer);
		pool.submit(poll);
		while (true) {
			if (en.isClosed()) {
				pool.shutdownNow();
				break;
			}
		}
		assertThat(sended, is(notifyAttempts));
	}
	
	private void sleepOrNotSleep(boolean sleep) throws InterruptedException {
		Random r = new Random();
		if (r.nextBoolean()) {
			Thread.sleep(200);
		}
	}

}
