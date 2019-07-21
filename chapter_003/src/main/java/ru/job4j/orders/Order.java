package ru.job4j.orders;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonInclude;

import ru.job4j.orders.database.LDConverter;
import ru.job4j.orders.database.jpa.listeners.OrderListener;

/*@EntityListeners({OrderListener.class}) //jpa
@Entity //jpa
@Table(name = "j4jorders", 
	   uniqueConstraints =  @UniqueConstraint(
			   name = "uk_order",
			   columnNames = {
					   "id",
			   		   "name"}),     
	   indexes =  @Index(
            name = "idx_order_name",
            columnList = "name",
            unique = false)
) //jpa
@NamedQuery(
		name = "deleteOrder",
		query = "DELETE FROM Order o WHERE o.id = :oId",
		hints = {@QueryHint(name = "javax.persistence.query.timeout", value = "3000")})
@NamedQuery(
		name = "updateStatus",
		query = "UPDATE Order o SET o.done = :oRdy WHERE o.id = :oId",
		hints = {@QueryHint(name = "javax.persistence.query.timeout", value = "3000")})
@NamedQuery(
		name = "selectOrders",
		query = "SELECT o FROM Order o")
@NamedQuery(
		name = "selectNotRdyOrders",
		query = "SELECT o FROM Order o WHERE o.done IS false")*/
public class Order implements Serializable { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
/*	@Version //jpa
	@ColumnDefault("0")*/
	private long version; 
/*	@Id //jpa
*/	private int id;
/*	@Column(name = "createDate", columnDefinition = "text", nullable = false, insertable = true, updatable = true, unique = false, length = 10)
	@Convert(converter = LDConverter.class)*/
	private LocalDate createDate;
/*	@OrderBy("name DESC") //jpa
*/	private String name;
	private String description;
/*	@ColumnDefault("false") //hibernate 
*/	private boolean done;
/*	@OneToOne(
			orphanRemoval = true,  
			cascade = CascadeType.ALL)
	@JoinColumn(name = "oi_fk",
			foreignKey = @ForeignKey(
					name = "oi_fk", 
					value = ConstraintMode.CONSTRAINT), 
			referencedColumnName = "id")*/
	private OrderImage image;
	
	public Order() {
	}
	
	public Order(int id, String name, String description) {
		this.id = id;
		this.createDate = LocalDate.now();
		this.name = name;
		this.description = description;
		this.done = false;
		this.image = null;
	}
	
	public Order(int id, String createDate, String name, String description, boolean done) {
		this.id = id;
		this.createDate = LocalDate.parse(createDate);
		this.name = name;
		this.description = description;
		this.done = done;
		this.image = null;
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
	
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the createDate
	 */
	public LocalDate getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(LocalDate ldate) {
		this.createDate = ldate;
	}
	
	/**
	 * @return the image
	 */
	public OrderImage getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(OrderImage image) {
		this.image = image;
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

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Order [version=" + version + ", id=" + id + ", createDate=" + createDate + ", name=" + name
				+ ", description=" + description + ", done=" + done + "]";
	}
}
