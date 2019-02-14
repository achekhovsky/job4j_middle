package ru.job4j.multithreading.copr;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

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
	
    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        Thread producer = new Thread(
                () -> {
                    IntStream.range(0, 5).forEach(
                            sbk::offer
                    );
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (true) {
                    	Integer polled = sbk.poll();
                    	if (polled != null) {
                    		buffer.add(polled);
                    	}
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join(2000);
        assertThat(buffer, is(Arrays.asList(0, 1, 2, 3, 4)));
    }

}
