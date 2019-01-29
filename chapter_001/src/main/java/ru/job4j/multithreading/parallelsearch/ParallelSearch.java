package ru.job4j.multithreading.parallelsearch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.job4j.multithreading.copr.SimpleBlockingQueue;

public class ParallelSearch {
	private static final Logger LOG = LogManager.getLogger("customLog");
	
    public static void main(String[] args) {
    	SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        final Thread consumer = new Thread(
                () -> {
                    while (!Thread.currentThread().isInterrupted()) {
                    	try {
                    		Thread.sleep(500);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
							LOG.warn(e.getMessage());
						}
                    	System.out.println(queue.poll());
                    }
                }
        );
        consumer.start();
        new Thread(
                () -> {
                    for (int index = 0; index != 3; index++) {
                        queue.offer(index);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        	consumer.interrupt();
            				LOG.warn(e.getMessage());
                        }
                    }
                    consumer.interrupt();
                }
        ).start();
    }
}