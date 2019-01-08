package ru.job4j.pingpong;

import java.awt.Point;
import java.util.Random;

import javafx.scene.shape.Rectangle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RectangleMove implements Runnable {
	private static Logger log = LogManager.getLogger("customLog");
    private final Rectangle rect;
    private final Point[] allowedDirections = {
    		new Point(-1, -1), 
    		new Point(-1, 0), 
    		new Point(0, -1), 
    		new Point(1, 0), 
    		new Point(1, 1), 
    		new Point(0, 1), 
    		new Point(-1, 1), 
    		new Point(1, -1)};
    private Point direction = null;

    public RectangleMove(Rectangle rect) {
        this.rect = rect;
    }
    
    /**
     * Checks the position of the rectangle relative to the specified limits.
     * @param limitX - width limit
     * @param limitY - height limit
     * @return true if rectangle ran into the borders, otherwise false
     */
    private boolean checkBoundaries(double limitX, double limitY) {
    	boolean isDeadEnd = false;
    	if (direction == null
    			|| this.rect.getX() + this.rect.getWidth() + direction.getX() >= limitX 
    			|| this.rect.getY() + this.rect.getHeight() + direction.getY() >= limitY 
    			|| this.rect.getX() + direction.getX() <= 0 
    			|| this.rect.getY() + direction.getY() <= 0) {
    		isDeadEnd = true;
    	}
    	return isDeadEnd;
    }
    
    /**
     * Changes the direction of movement of the rectangle
     */
    private void changeDirect() {
		Random rndStep = new Random();
		Point checked = this.allowedDirections[rndStep.nextInt(allowedDirections.length - 1)];
    	this.direction = checked; 
    }
    
    private void doStep() {
    	this.rect.setX(this.rect.getX() + this.direction.getX());
    	this.rect.setY(this.rect.getY() + this.direction.getY());
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (this.rect.getParent().getScene() != null) {
            	if (this.checkBoundaries(
            			this.rect.getParent().getScene().getHeight(), 
            			this.rect.getParent().getScene().getWidth())) {
            		this.changeDirect();
            	} else {
            		this.doStep();
            	}
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                log.warn(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}
