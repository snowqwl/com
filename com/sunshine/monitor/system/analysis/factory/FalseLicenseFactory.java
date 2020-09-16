package com.sunshine.monitor.system.analysis.factory;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.analysis.dao.FalseLicenseDao;
import com.sunshine.monitor.system.analysis.dao.jdbc.FalseLicenseSCSDaoImpl;

@Repository("falseLicenseFactory")
public class FalseLicenseFactory {
	
	/** SCSDB */
	public static final String SCS_DB_SOURCENAME="SCS";
	
	/** ORACLE */
	public static final String ORACL_DB_SOURCENAME="ORACLE";
	
	/** NOW USE SOURCE NAME (oracle与天云星之间切换只需要改这里)*/
	public static final String SOURCENAME= ORACL_DB_SOURCENAME;
	
	public FalseLicenseDao UseModel(String desc){
		if(ORACL_DB_SOURCENAME.equals(desc)){//oracle数据源
			return (FalseLicenseDao) SpringApplicationContext.getStaticApplicationContext().getBean("falseSCSDao");
			//return (FalseLicenseDao) SpringApplicationContext.getStaticApplicationContext().getBean("falseDao");
			
		}else if(SCS_DB_SOURCENAME.equals(desc)){//天云星数据源
			return (FalseLicenseDao) SpringApplicationContext.getStaticApplicationContext().getBean("falseDao");
			//return (FalseLicenseDao) SpringApplicationContext.getStaticApplicationContext().getBean("falseSCSDao");
		}
		return null;
	}
}
