package com.harmazing.spms.jszc.dto;

import java.util.ArrayList;
import java.util.List;

import com.harmazing.spms.base.dto.WorkFlowCommonDTO;

public class JSZCMainDTO  extends WorkFlowCommonDTO{
	private List<JSZCDTO> jszcdtos = new ArrayList<JSZCDTO>();

	public List<JSZCDTO> getJszcdtos() {
		return jszcdtos;
	}

	public void setJszcdtos(List<JSZCDTO> jszcdtos) {
		this.jszcdtos = jszcdtos;
	}
	
}
