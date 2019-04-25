package ru.job4j.orders;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Order {
	private final int id;
	private final LocalDate createDate;
	private String name;
	private String description;
	private boolean done;
	
	public Order(int id, String name, String description) {
		this.id = id;
		this.createDate = LocalDate.now();
		this.name = name;
		this.description = description;
		this.done = false;
	}
	
	public Order(int id, String createDate, String name, String description, boolean done) {
		this.id = id;
		this.createDate = LocalDate.parse(createDate);
		this.name = name;
		this.description = description;
		this.done = done;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the done
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * @param done the done to set
	 */
	public void setDone(boolean done) {
		this.done = done;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the createDate
	 */
	public LocalDate getCreateDate() {
		return createDate;
	}

	/* 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + id;
		return result;
	}

	/* 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Order other = (Order) obj;
		if (createDate == null) {
			if (other.createDate != null) {
				return false;
			}
		} else if (!createDate.equals(other.createDate)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		return true;
	}


}
