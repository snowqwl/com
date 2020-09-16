package com.sunshine.monitor.comm.log;

/**
 * 操作类型
 * @author OUYANG 2013/7/19
 *
 */
public enum OperationType {
	
	LOGIN_OK("1001","登录成功"),
	LOGIN_FAIL("1002","登录失败"),
	LOGIN_PKI_OK("1003","PKI登录成功"),
	LOGIN_PKI_FAIL("1004","PKI登录失败"),
	LOGIN_SSO_OK("1005","单点登录成功"),
	LOGIN_SSO_FAIL("1006","单点登录失败"),
	SUSP_LOCAL_QUERY("2010","本地布控申请查询"),
	SUSP_LOCAL_ADD("2011","本地布控申请新增"),
	SUSP_LD_QUERY("2020","联动布控申请查询"),
	SUSP_LD_ADD("2021","联动布控申请新增"),
	SUSP_MH_QUERY("2030","本地模糊布控申请查询"),
	SUSP_MH_ADD("2031","本地模糊布控申请新增"),
	SUSP_LOCAL_CHANGE_QUERY("2110","本地布控变更查询"),
	SUSP_LOCAL_CHANGE_SAVE("2112","本地布控变更保存"),
	SUSP_LOCAL_CHANGE_DELETE("2113","本地布控变更删除"),
	SUSP_LD_CHANGE_QUERY("2120","联动布控变更查询"),
	SUSP_LD_CHANGE_SAVE("2122","联动布控变更保存"),
	SUSP_LD_CHANGE_DELETE("2123","联动布控变更删除"),
	SUSP_CHECK_QUERY("2210","布控领导审核查询"),
	SUSP_CHECK_ADD("2211","布控领导审核新增"),
	SUSP_APPROVAL_QUERY("2220","布控指挥中心审批查询"),
	SUSP_APPROVAL_ADD("2221","布控指挥中心审批新增"),
	SUSP_APPROVAL_SA_QUERY("2240","布控指挥中心涉案类审批查询"),
	SUSP_APPROVAL_SA_ADD("2241","布控指挥中心涉案类审批新增"),
	SUSP_APPROVAL_JT_QUERY("2260","布控指挥中心交通类审批查询"),
	SUSP_APPROVAL_JT_ADD("2261","布控指挥中心交通类审批新增"),
	HCSUSP_APPROVAL_LD_QUERY("2280","联动布控管控核查查询"),
	HCSUSP_APPROVAL_LD_SAVE("2281","联动布控管控核查确认"),
	INTEGRATION_RESUSP_QUERY("2310","续控信息查询"),
	EXPIRE_SUSP_QUERY("2500","过期布控申请查询"),
	CSUSP_APPLY_SURE_QUERY("2410","拦截确认查询"),
	CSUSP_APPLY_SURE_SAVE("2411","拦截确认保存"),
	CSUSP_APPLY_QUERY("2510","撤控申请查询"),
	CSUSP_APPLY_ADD("2511","撤控申请新增"),
	CSUSP_CHECK_QUERY("2610","撤控领导审核查询"),
	CSUSP_CHECK_ADD("2611","撤控领导审核新增"),
	CSUSP_APPROVAL_QUERY("2620","撤控指挥中心审批查询"),
	CSUSP_APPROVAL_ADD("2621","撤控指挥中心审批新增"),
	CSUSP_APPROVAL_SA_QUERY("2640","撤控指挥中心涉案类审批查询"),
	CSUSP_APPROVAL_SA_ADD("2641","撤控指挥中心涉案类审批新增"),
	CSUSP_APPROVAL_JT_QUERY("2660","撤控指挥中心交通类审批查询"),
	CSUSP_APPROVAL_JT_ADD("2661","撤控指挥中心交通类审批新增"),
	ALARM_SIGN_QUERY("3010","预警签收查询"),
	ALARM_SIGN("3011","预警签收"),
	ALARM_CMD_QUERY("3020","下达指令查询"),
	ALARM_CMD("3021","下达指令"),
	CALARM_LIVE_QUERY("3030","出警跟踪查询"),
	CALARM_LIVE("3031","出警跟踪"),
	CALARM_HANDLE_QUERY("3080","出警反馈查询"),
	CALARM_HANDLE("3081","出警反馈"),
	HANDLE_SIGN_QUERY("3090","反馈签收查询"),
	HANDLE_SIGN("3091","反馈签收"),
	ALARM_REGISTER("3041","出警登记"),
	CMD_VIES("3051","指令查看"),
	ALARM_LIVE("3061","预警跟踪"),
	NOTICE_PUB("4011","公告发布"),
	NOTICE_MOD("4012","公告修改"),
	NOTICE_DEL("4013","公告删除"),
	NOTICE_QUE("4050","公告查询"),
	INTEGRATION_PROVINCE_QUERY("5040","全省漫游查询"),
	INTEGRATION_PASSRC_QUERY("5510","过车信息查询"),
	INTEGRATION_PASSRC_EXPORT("5515","过车信息导出"),
	INTEGRATION_SUSP_QUERY("5520","布控信息查询"),
	INTEGRATION_GK_QUERY("5530","模糊布控信息查询"),
	INTEGRATION_ALARM_QUERY("5540","报警信息查询"),
	TRAFFIC_QUERY("5550","交通违法查询"),
	HPHM_PASS_QUERY("5560","多号牌过车查询"),
	INTEGRATION_PASSJJ_QUERY("5570","过车信息查询(地市)"),
	COLLECTVEH_QUERY("5580","关注车辆查询"),
	INTEGRATION_PREFECTURE_QUERY("5590","下级机关查询"),
	BATCH_DATA_INPUT("7011","批量数据导入"),
	BATCH_DATA_OUPUT("7020","批量数据导出"),
	DIFFERENT_PLACE_ECHANGE_STATICS("7075","异地交换统计"),
	SYNC_PROGREAM_MONITOR("7120","同步程序监控"),
	KD_DISTRIBUTION("7510","卡口分布"),
	TASK_START_STOP("7772","任务启动停止"),
	MONITOR_DIFFERENT_PLACE_TRANS("8510","异地传输监测"),
	MONITOR_DEVICE_STATU("8520","设备状态监测"),
	DEVICE_QUERY("8530","设备信息查询"),
	DEVICE_ADD("8531","设备信息新增"),
	DEVICE_MODIFY("8532","设备信息修改"),
	DEVICE_DELETE("8533","设备信息删除"),
	KD_QUERY("8540","卡口信息查询"),
	KD_ADD("8541","卡口信息新增"),
	KD_MODIFY("8542","卡口信息修改"),
	KD_DELTE("8543","卡口信息删除"),
	DIRECTION_QUERY("8550","方向信息查询"),
	DIRECTION_ADD("8551","方向信息新增"),
	DIRECTION_MODIFY("8552","方向信息修改"),
	DIRECTION_DELETE("8553","方向信息删除"),
	PARAMETER_SAVE("9011","参数保存"),
	ROLE_ADD("9021","角色新增"),
	ROLE_MODIFY("9022","角色修改"),
	ROLE_DELETE("9023","角色删除"),
	DEPARTMENT_ADD("9031","部门查询"),
	DEPARTMENT_MODIFY("9032","部门变更"),
	DEPARTMENT_DELETE("9033","部门删除"),
	POLICE_DEPARTMENT_SYNC("9036","警综部门同步"),
	USER_ADD("9041","用户查询"),
	USER_MODIFY("9042","用户变更"),
	USER_DELETE("9043","用户删除"),
	POLICE_USER_SYNC("9046","警综用户同步"),
	USER_RESET_PASSWORD("9049","用户重置密码"),
	CODE_TYPE_ADD("9051","代码类别新增"),
	CODE_TYPE_MOD("9052","代码类型修改"),
	CODE_TYPE_DELETE("9053","代码类型删除"),
	CODE_ADD("9055","代码新增"),
	CODE_MODIFY("9056","代码修改"),
	CODE_DELETE("9057","代码删除"),
	CATALOG_SAVE("9081","目录管理保存"),
	CATALOG_DELETE("9083","目录管理删除"),
	MENU_SAVE("9085","程序管理保存"),
	MENU_DELETE("9087","程序管理删除"),
	PREFECTURE_ADD("9091","辖区增加"),
	PREFECTURE_DELETE("9093","辖区删除"),
	PASSWORD_MODIFY("9901","密码修改"),
	// 新增类别
	RED_LIST_ADD("9100","新增红名单"),
	RED_LIST_BATCH_ADD("9101","批量导入红名单"),
	RED_LIST_AUDIT("9102","红名单审批"),
	RED_LIST_QUERY("9103","查询未审批红名单"),
	RED_LIST_AUIDTED_QUERY("9104","查询审批通过红名单"),
	RED_LIST_DEBLOCK("9105","解封红名单"),
	RED_LIST_REMOVE("9106","移除红名单库"),
	
	// 边界卡口
	BOUNDARY_GATE_UPDATE("9110","更新边界卡"),
	BOUNDARY_GATE_ADD("9111","添加边界卡"),
	
	//下载审计
	DOWNLOAD_RESULT("10000","下载或导出内容"),
	//角色授权
	ROLE_MENU_BATCH_ADD("9902","添加角色授权"),
	ROLE_MENU_BATCH_CANCEL("9903","取消角色授权"),
	//用户授权
	ROLE_USER_BATCH_ADD("9904","添加角色");
	//ROLE_USER_BATCH_CANCEL("9905","取消角色");

	private String type;
	
	private String desc;
	
	private OperationType(String type,String desc){
		this.type = type ;
		this.desc = desc ;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}