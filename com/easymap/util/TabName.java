package com.easymap.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TabName {
	/** read fileName*/
	private static final String CONFIGNAME = "database.properties";
	
	/** read encode*/
	private static final String ENCODE="utf-8";
	
	private static final String COLUMKEY = "jdbc.taball";
	
	private static String alias =  "";//default
	private static Logger log = LoggerFactory.getLogger(TabName.class);

	/*
	static{
		Properties prop = null;
		InputStream finput = null;
		//Reader rdr = null;
		try{
			prop = new Properties();
			finput = TabName.class.getClassLoader().getResourceAsStream(CONFIGNAME);
			//rdr = new InputStreamReader(finput,ENCODE);
			prop.load(finput);
			Set<Entry<Object,Object>> set= prop.entrySet();
			for(Entry<Object,Object> entry : set){
				if(!entry.getKey().toString().startsWith("#")){
					if(entry.getKey().equals(COLUMKEY)){
						alias = entry.getValue().toString() + ".";
						break;
					}
				}
			}
			//rdr.close();
			finput.close();
		
		}catch(Exception e){
			log.warn("TabName Init()",e);
		}
	}
	*/
	/**
	 * 布控表
	 */
	public static final String SUSPTAB= alias + "VEH_SUSPINFO";
	
	/**
	 * 布控表
	 */
	public static final String SUSPTAB_KK= alias + "VEH_SUSPINFO";
	
	/**
	 * 布控序列
	 */
	public static final String SUSPSEQ= alias + "SEQ_SUSPINFO_XH.NEXTVAL";
	
	/**
	 * 布控图片表
	 */
	public static final String SUSPPICTAB= alias + "SUSP_PICREC";
	
	/**
	 * 布控图片序列
	 */
	public static final String SUSPPICSEQ= alias + "SEQ_PICREC_XH.NEXTVAL";
	
	/**
	 * 审核审批表
	 */
	public static final String AUDITTAB= alias + "AUDIT_APPROVE";
	
	/**
	 * 审核审批序列
	 */
	public static final String AUDITSEQ= alias + "seq_audit_xh.nextval";
	
	/**
	 * 报警表
	 */
	public static final String ALARMTAB =  alias + "VEH_ALARMREC";
	
	/**
	 * 报警表
	 */
	public static final String ALARMTAB_KK =  alias + "VEH_ALARMREC";
	
	/**
	 * 出警反馈表
	 */
	public static final String ALARMHANDLETAB =  alias + "veh_alarm_handled";
	
	/**
	 * 出警反馈表序列
	 */
	public static final String ALARMHANDLESEQ =  alias + "SEQ_ALARM_HANDLED_FKBH.NEXTVAL";
	
	/**
	 * 下达指令表
	 */
	public static final String ALARMCMDTAB =  alias + "veh_alarm_cmd";
	
	public static final String VEHPASSREC =  alias + "veh_passrec";
	
	/**
	 * 下达指令范围
	 */
	public static final String ALARMCMDSCOPETAB =  alias + "veh_alarm_cmdscope";
	
	/**
	 * 出警跟踪表
	 */
	public static final String ALARMLIVETRACETAB =  alias + "veh_alarm_livetrace";
	
	/**
	 * 出警反馈图片表
	 */
	public static final String AlARMHANDLEDPICTAB=  alias + "veh_alarm_handled_picrec";
	
	/**
	 *  ip地址表
	 */
	public static final String URL_CODE_kk =  alias + "code_url";
	
	
	/**
	 *  卡口表
	 */
	public static final String CODE_Gate =  alias + "code_gate";
	
	/**
	 * 卡口方向表
	 */
	public static final String CODE_Gate_extend =  alias + "code_gate_extend";
	
	/**
	 * 字典表
	 */
	public static final String FRM_CODE =  alias + "frm_code";
	
	public static final String FRM_CODE_DEF = "frm_code";
	//TODO
	/**
	 * ---fenghu
	 */
	public static final String T_SUSP_VEHPASS =  alias + "t_susp_vehpass";
	
	public static final String T_SUSP_VEHPASS_DEF = "t_susp_vehpass";
	
	public static final String T_PHONE_ALARM = "t_phone_alarm";
	
	/**
	 * 卡口表
	 */
	public static final String GATETAB = alias + "code_gate";
	
	/**
	 * 部门表
	 */
	public static final String DEPARTMENT = alias + "frm_department";
	
	/**
	 * 方向表
	 */
	//public static final String DIRECTTAB = "kk.code_direct";
	public static final String DIRECTTAB = alias + "code_gate_extend";
	
	/**
	 * 预案表
	 */
	public static final String RECODETAB = alias + "code_gate_record";
	
	/**
	 * 流量表
	 */
	public static final String FLOWTAB = alias + "stat_flow";
	
	/**
	 * pdt定位表
	 */
	public static final String PDTLOCATIONMTAB = "t_pdt_ssgjxx";
	
	/**
	 * 卡口车道表
	 */
	public static final String CODE_Gate_Cd =  alias + "code_gate_cd";
}
