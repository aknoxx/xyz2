package dst2.ejb.model;

import java.io.Serializable;

public class MembershipId implements Serializable {
	 
	private static final long serialVersionUID = -1761437052404363205L;

	private long userId;
	 
	private long gridId;
	 
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

	public int hashCode() {
		return (int)(userId + gridId);
	}
	 
	public boolean equals(Object object) {
		if (object instanceof MembershipId) {
			MembershipId otherId = (MembershipId) object;
			return (otherId.userId == this.userId) && (otherId.gridId == this.gridId);
	    }
	    return false;
	}
}
