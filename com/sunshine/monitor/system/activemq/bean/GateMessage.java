package com.sunshine.monitor.system.activemq.bean;

import java.util.List;

import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateCd;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;

/**
 * 边界卡口信息
 * @author OUYANG 2014/4/17
 *
 */
public class GateMessage extends TransBean {

	private static final long serialVersionUID = 1L;
	
	/** 卡口基本信息 */
	private CodeGate codeGate;
	
	/** 卡口方向车道信息列表 */
	private List<CodeGateCd> codeGateCd;
	
	/** 卡口扩展表信息  */
	private List<CodeGateExtend> codeGateExtend;
	
	/** 操作人*/
	private String operator;

	public CodeGate getCodeGate() {
		return codeGate;
	}

	public void setCodeGate(CodeGate codeGate) {
		this.codeGate = codeGate;
	}

	public List<CodeGateCd> getCodeGateCd() {
		return codeGateCd;
	}

	public void setCodeGateCd(List<CodeGateCd> codeGateCd) {
		this.codeGateCd = codeGateCd;
	}

	public List<CodeGateExtend> getCodeGateExtend() {
		return codeGateExtend;
	}

	public void setCodeGateExtend(List<CodeGateExtend> codeGateExtend) {
		this.codeGateExtend = codeGateExtend;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}
