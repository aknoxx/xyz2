package dst2.ejb;

import javax.ejb.Remote;

@Remote
public interface Testing {

	public void insertData();
	public void remove();
}
