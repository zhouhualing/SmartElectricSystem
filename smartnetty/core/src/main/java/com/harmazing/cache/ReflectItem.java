package com.harmazing.cache;

import java.io.Serializable;

public class ReflectItem implements Serializable {
	public int func_type = 0; // 0: none-static 1: static
	public String cls_name = "";
	public String func_name = "";
	public Class<?>[] formal_params = null;
	public Object[]   actual_params = null;
	
	public String channel= ""; // for result sync up between netty and spms
}
