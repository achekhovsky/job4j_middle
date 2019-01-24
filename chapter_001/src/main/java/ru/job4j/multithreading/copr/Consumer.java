package ru.job4j.multithreading.copr;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class Consumer implements Runnable {
	private final SimpleBlockingQueue<Integer> sbk;
	private int lastValue;
	private final int cycleSize;
	
	public Consumer(SimpleBlockingQueue<Integer> sbk, int cycleSize) {
		this.sbk = sbk;
		this.cycleSize = cycleSize;
	}
	
	public int getLastValue() {
		return lastValue;
	}

	@Override
	public void run() {
		for (int i = 0; i < this.cycleSize; i++) {
			this.lastValue = this.sbk.poll();
		}
	}

}
