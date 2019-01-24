package ru.job4j.multithreading.copr;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class SimpleBlockingQueue<T> {
	private static final Logger LOG = LogManager.getLogger("customLog");
    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;
    
    public SimpleBlockingQueue(int capacity) {
    	this.capacity = capacity;
	}
    
    public synchronized void offer(T value) {
    	while (capacity < this.queue.size()) {
    		try {
				wait();
			} catch (InterruptedException e) {
				LOG.warn(e.getMessage());
			}
    	}
   		this.queue.offer(value);
   		notify();
    }

    public synchronized T poll() {
    	T result = null;
    	int count = 0;
    	while (this.queue.size() == 0 && count < 21) {
    		try {
    			count++;
				wait(100);
			} catch (InterruptedException e) {
				LOG.warn(e.getMessage());
			}
    	} 
    	if (count != 20) {
    		result = this.queue.poll();
    	}
   		notify();
        return result;
    }
}
