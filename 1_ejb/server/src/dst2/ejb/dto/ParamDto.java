package dst2.ejb.dto;

import java.io.Serializable;

public class ParamDto implements Serializable {

	private static final long serialVersionUID = 8323307052394976508L;

	public int paramIndex;
	public String className;
	public String value;
	
	public ParamDto(int paramIndex, String className, String value) {
		super();
		this.paramIndex = paramIndex;
		this.className = className;
		this.value = value;
	}
}
