package com.clipomatiq.processor;

import java.util.HashMap;

public class ProcessorData {
	
	String procName;
	HashMap data;

	public ProcessorData() {
		// TODO Auto-generated constructor stub
		data=new HashMap();
	}

	public String getProcName() {
		return procName;
	}

	public void setProcName(String procName) {
		this.procName = procName;
	}

	public Object getData(String key) {
		return data.get(key);
	}

	public void setData(HashMap params) {
		this.data = params;
	}

	public void addData(String key, Object dat) {
		data.put(key, dat);
		
	}

}
