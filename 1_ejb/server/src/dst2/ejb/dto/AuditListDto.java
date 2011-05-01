package dst2.ejb.dto;

import java.io.Serializable;
import java.util.List;

public class AuditListDto implements Serializable {

	private static final long serialVersionUID = 7948622264104169414L;

	public List<AuditDto> audits;

	public AuditListDto(List<AuditDto> audits) {
		super();
		this.audits = audits;
	}
}
