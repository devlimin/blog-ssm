package com.limin.blog.util;

import java.util.Map;

public class SysCategory {
	
	public static String MOBILE_DEVELOPEMENT="MOBILE_DEVELOPEMENT";
	public static String WEB_FRONT_END="WEB_FRONT_END";
	public static String ARCHITECTURE_DESIGN="ARCHITECTURE_DESIGN";
	public static String PROGRAMMING_LANGUAGE="PROGRAMMING_LANGUAGE";
	public static String INTERNET="INTERNET";
	
	public static String DATABASE="DATABASE";
	public static String SYSTEM_OPERATIONAL="SYSTEM_OPERATIONAL";
	public static String CLOUD_COMPUTING="CLOUD_COMPUTING";
	public static String RESEARCH_DEVELOPMENT_MANAGEMENT="RESEARCH_DEVELOPMENT_MANAGEMENT";
	public static String COMPREHENSIVE="COMPREHENSIVE";
	
	public static Map<String, String> sysCategory;
	static {
		sysCategory.put(MOBILE_DEVELOPEMENT, "移动开发");
		sysCategory.put(WEB_FRONT_END, "Web前端");
		sysCategory.put(ARCHITECTURE_DESIGN, "架构设计");
		sysCategory.put(PROGRAMMING_LANGUAGE, "编程语言");
		sysCategory.put(INTERNET, "互联网");
		
		sysCategory.put(DATABASE, "数据库");
		sysCategory.put(SYSTEM_OPERATIONAL, "系统运维");
		sysCategory.put(CLOUD_COMPUTING, "云计算");
		sysCategory.put(RESEARCH_DEVELOPMENT_MANAGEMENT, "研发管理");
		sysCategory.put(COMPREHENSIVE, "综合");
	}
}
