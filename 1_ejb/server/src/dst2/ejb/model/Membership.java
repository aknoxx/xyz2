package dst2.ejb.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="Membership")
@IdClass(MembershipId.class)
public class Membership implements Serializable {

	private static final long serialVersionUID = -2770212991140224310L;

	@Id
	private long userId;
	@Id
	private long gridId;
	
	@ManyToOne
	@PrimaryKeyJoinColumn(name="user_id", referencedColumnName="ID")
	private User user;
	
	@ManyToOne
	@PrimaryKeyJoinColumn(name="grid_id", referencedColumnName="ID")
	private Grid grid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date registration;
	@Column(nullable=false)
	private Double discount;
	
	public Membership() {
		super();
	}

	public Membership(Date registration, Double discount) {
		super();
		this.registration = registration;
		this.discount = discount;
	}

	public Date getRegistration() {
		return registration;
	}

	public void setRegistration(Date registration) {
		this.registration = registration;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getGridId() {
		return gridId;
	}

	public void setGridId(long gridId) {
		this.gridId = gridId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}
}
