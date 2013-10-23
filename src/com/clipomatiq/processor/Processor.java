package com.clipomatiq.processor;

import java.util.HashMap;



public interface Processor {
	
	public ProcessorData process();
	public String url="";
	public String getURL();
	public HashMap getParam();
	public void init(HashMap params);
}
