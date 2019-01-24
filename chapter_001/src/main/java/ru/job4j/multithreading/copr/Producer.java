package ru.job4j.multithreading.copr;

public class Producer implements Runnable {
	private final SimpleBlockingQueue<Integer> sbk;
	private final int cycleSize;
	
	public Producer(SimpleBlockingQueue<Integer> sbk, int cycleSize) {
		this.sbk = sbk;
		this.cycleSize = cycleSize;
	}

	@Override
	public void run() {
		for (int i = 0; i < this.cycleSize; i++) {
			this.sbk.offer(i);
		}
	}

}
