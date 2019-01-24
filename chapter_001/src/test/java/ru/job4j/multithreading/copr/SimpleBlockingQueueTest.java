package ru.job4j.multithreading.copr;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

import org.junit.Before;
import org.junit.Test;

public class SimpleBlockingQueueTest {
	private Producer p;
	private Consumer c;
	private	SimpleBlockingQueue<Integer> sbk;  
	
	@Before
	public void setUp() throws Exception {
		sbk = new SimpleBlockingQueue<>(5);
		
		p = new Producer(sbk, 10);
		c = new Consumer(sbk, 10);
	}

	@Test
	public void whenThreadsFinishWorkGetTheLastLoopValue() {
		Thread pt = new Thread(p);
		Thread ct = new Thread(c);
		
		pt.start();
		ct.start();
		
		try {
			ct.join();
			pt.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertThat(9, is(c.getLastValue()));
	}

}
