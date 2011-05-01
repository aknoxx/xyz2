package dst2.ejb.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AuditDto implements Serializable {

	private static final long serialVersionUID = 6300964045410835688L;

	public Date invocationTime;
	public String methodName;
	public List<ParamDto> parameters;
	public String resultValue;
	
	public AuditDto(Date invocationTime, String methodName,
			List<ParamDto> parameters, String resultValue) {
		super();
		this.invocationTime = invocationTime;
		this.methodName = methodName;
		this.parameters = parameters;
		this.resultValue = resultValue;
	}
}
