package com.sunshine.monitor.comm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.bean.MaintainHandle;
import com.sunshine.monitor.comm.bean.RoleMenu;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.maintain.service.MaintainManager;
import com.sunshine.monitor.comm.service.MainPageManager;
import com.sunshine.monitor.comm.service.RoleMenuManager;
import com.sunshine.monitor.comm.util.FusionChartsXMLGenerator;
import com.sunshine.monitor.system.alarm.service.VehAlarmHandleManager;
import com.sunshine.monitor.system.alarm.service.VehAlarmManager;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.dao.LogDao;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.monitor.bean.GateProject;
import com.sunshine.monitor.system.monitor.dao.KkjrjcProjectDao;
import com.sunshine.monitor.system.monitor.dao.StatMonitorDao;
import com.sunshine.monitor.system.monitor.service.GateProjectManager;
import com.sunshine.monitor.system.monitor.service.KkjrjcProjectManager;
import com.sunshine.monitor.system.monitor.service.SjcsjcProjectManager;
import com.sunshine.monitor.system.susp.service.SuspinfoAuditApproveManager;
import com.sunshine.monitor.system.susp.service.SuspinfoEditManager;

@Transactional(propagation=Propagation.REQUIRED)
@Service("MainPageManager")
public class MainPageManagerImpl implements MainPageManager{
	@Autowired
	private SystemManager systemManager;
	
	@Autowired
	private RoleMenuManager roleMenuManager;
	
	@Autowired
	@Qualifier("suspinfoAuditApproveManager")
	private SuspinfoAuditApproveManager suspinfoAuditApproveManager;
	
	@Autowired
	@Qualifier("vehAlarmHandleManager")
	private VehAlarmHandleManager vehAlarmHandleManager;
	
	@Autowired
	@Qualifier("suspinfoEditManager")
	private SuspinfoEditManager suspinfoEditManager;
	
	@Autowired
	private MaintainManager maintainManager;
	
	@Autowired
	@Qualifier("kkjrjcProjectManager")
	private KkjrjcProjectManager kkjrjcProjectManager;
	
	@Autowired
	@Qualifier("sjcsjcProjectManager")
	private SjcsjcProjectManager sjcsjcProjectManager;
	
	@Autowired
	private VehAlarmManager vehAlarmManager;
	
	@Autowired
	private GateProjectManager gateProjectManager;
	
	@Autowired
	@Qualifier("statMonitorDao")
	private StatMonitorDao statMonitorDao;
	
	@Autowired
	@Qualifier("logDao")
	private LogDao logDao;
	
	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;
	
	@Autowired
	@Qualifier("kkjrjcProjectDao")
	private KkjrjcProjectDao kkjrjcProjectDao;
	
	//private static List<String> list = null;
		
	//private static boolean backoutSurveillanceApplication = false;
		
	public SystemManager getSystemManager() {
		return systemManager;
	}

	public void setSystemManager(SystemManager systemManager) {
		this.systemManager = systemManager;
	}

	public SuspinfoAuditApproveManager getSuspinfoAuditApproveManager() {
		return suspinfoAuditApproveManager;
	}

	public void setSuspinfoAuditApproveManager(
			SuspinfoAuditApproveManager suspinfoAuditApproveManager) {
		this.suspinfoAuditApproveManager = suspinfoAuditApproveManager;
	}

	public VehAlarmHandleManager getVehAlarmHandleManager() {
		return vehAlarmHandleManager;
	}

	public void setVehAlarmHandleManager(VehAlarmHandleManager vehAlarmHandleManager) {
		this.vehAlarmHandleManager = vehAlarmHandleManager;
	}

	public SuspinfoEditManager getSuspinfoEditManager() {
		return suspinfoEditManager;
	}

	public void setSuspinfoEditManager(SuspinfoEditManager suspinfoEditManager) {
		this.suspinfoEditManager = suspinfoEditManager;
	}

	public MaintainManager getMaintainManager() {
		return maintainManager;
	}

	public void setMaintainManager(MaintainManager maintainManager) {
		this.maintainManager = maintainManager;
	}

	public KkjrjcProjectManager getKkjrjcProjectManager() {
		return kkjrjcProjectManager;
	}

	public void setKkjrjcProjectManager(KkjrjcProjectManager kkjrjcProjectManager) {
		this.kkjrjcProjectManager = kkjrjcProjectManager;
	}

	public SjcsjcProjectManager getSjcsjcProjectManager() {
		return sjcsjcProjectManager;
	}

	public void setSjcsjcProjectManager(SjcsjcProjectManager sjcsjcProjectManager) {
		this.sjcsjcProjectManager = sjcsjcProjectManager;
	}

	public VehAlarmManager getVehAlarmManager() {
		return vehAlarmManager;
	}

	public void setVehAlarmManager(VehAlarmManager vehAlarmManager) {
		this.vehAlarmManager = vehAlarmManager;
	}
	
	public GateProjectManager getGateProjectManager() {
		return gateProjectManager;
	}
	
	public void setGateProjectManager(GateProjectManager gateProjectManager) {
		this.gateProjectManager = gateProjectManager;
	}
	
	public Map getDataCountInfo(HttpServletRequest request,HttpServletResponse response, UserSession userSession,SysUser user){
		response.setContentType("text/html;charset=utf-8");
		Map<String,Object> map = new HashMap<String,Object>();
		boolean localSurveillanceApplication = false;
		boolean transRegionalSurveillanceApplication = false;
		boolean surveillanceAudit = false;
		boolean surveillanceApproval = false;
		boolean earlyWarningConfirm = false;
		boolean interceptConfirmation = false;
		boolean backoutSurveillanceAudit = false;
		boolean backoutSurveillanceApproval = false;
		boolean giveOrder = false;
		boolean policeFeedbackEntering = false;
		boolean failureProcessFeedback = false;
		boolean alarmInformationQuery = false;
	    boolean backoutSurveillanceApplication = false;
	    boolean gateQuery = false;
		
		String yhdh = user.getYhdh();
		Map<String,String> filter = new HashMap<String,String>();
		filter.put("curPage", "1");	//页数
		filter.put("pageSize", "1");	//每页显示行数
		try {
			/**
			 for(String role : roles){
				List<RoleMenu> list = roleMenuManager.getRoleMenuList(role);
				for(RoleMenu rm : list){
					if(rm.getCxdh().equals("100040")) localSurveillanceApplication = true;
					if(rm.getCxdh().equals("100041")) transRegionalSurveillanceApplication = true;
					if(rm.getCxdh().equals("100042")) surveillanceAudit = true;
					if(rm.getCxdh().equals("100054")) surveillanceApproval = true;
					if(rm.getCxdh().equals("100050")) earlyWarningConfirm = true;
					if(rm.getCxdh().equals("100065")) interceptConfirmation = true;
					if(rm.getCxdh().equals("100037")) backoutSurveillanceApplication = true;
					if(rm.getCxdh().equals("100045")) backoutSurveillanceAudit = true;
					if(rm.getCxdh().equals("100057")) backoutSurveillanceApproval = true;
					if(rm.getCxdh().equals("100051")) giveOrder = true;
					if(rm.getCxdh().equals("100053")) policeFeedbackEntering = true;
					if(rm.getCxdh().equals("100010")) failureProcessFeedback = true;
					if(rm.getCxdh().equals("100075")) alarmInformationQuery = true;
				}
			}
			*/

			List<String> list = roleMenuManager.queryMenusByRole(yhdh);

			for(int i=0; i<list.size(); i++){
				//布控申请
				if("100254".equals(list.get(i))){
					localSurveillanceApplication = true;
					transRegionalSurveillanceApplication = true;
					continue;
				}
				//布控审核
				if("100042".equals(list.get(i))) {
					surveillanceAudit = true;
					continue;
				}
				//布控审批
				if("100054".equals(list.get(i))) {
					surveillanceApproval = true;
					continue;
				}
				//预警签收
				if("100050".equals(list.get(i))) {
					earlyWarningConfirm = true;
					continue;
				}
				//拦截确认
				if("100065".equals(list.get(i))) {
					interceptConfirmation = true;
					continue;
				}
				//撤控申请
				if("100037".equals(list.get(i))) {
					backoutSurveillanceApplication = true;
					continue;
				}
				//撤控审核
				if("100045".equals(list.get(i))) {
					backoutSurveillanceAudit = true;
					continue;
				}
				//撤控审批
				if("100057".equals(list.get(i))) {
					backoutSurveillanceApproval = true;
					continue;
				}
				//下达指令
				if("100051".equals(list.get(i))) {
					giveOrder = true;
					continue;
				}
				//出警反馈录入
				if("100053".equals(list.get(i))) {
					policeFeedbackEntering = true;
					continue;
				}
				//故障处理反馈
				if("100010".equals(list.get(i))) {
					failureProcessFeedback = true;
					continue;
				}
				//报警信息查询
				if("100075".equals(list.get(i))) {
					alarmInformationQuery = true;
					continue;
				}
				//卡口信息查询
				if("100066".equals(list.get(i))) {
					gateQuery = true;
					continue;
				}
			}
			
			String end = this.systemManager.getSysDate(null, false);
			String begin = this.systemManager.getSysDate("-180", false);
			long _1= System.currentTimeMillis();
			int suspinfoBkfwlxCount = this.suspinfoAuditApproveManager.getSuspinfoBkfwlxCount(begin, end, userSession.getSysuser().getGlbm());
			long _2 = System.currentTimeMillis();
			System.out.println("=======(首页)联动布控(已报警)C======"+(_2-_1) + "ms");
			
			long _3= System.currentTimeMillis();
			int suspinfoNoBkfwlxCount = this.suspinfoAuditApproveManager.getSuspinfoNoBkfwlxCount(begin, end, userSession.getSysuser().getGlbm());
			long _4 = System.currentTimeMillis();
			System.out.println("=======(首页)联动布控(未报警)C======"+(_4-_3) + "ms");
			
			int passrecCount = 0;

			map.put("suspinfoBkfwlxCount", suspinfoBkfwlxCount);
			map.put("suspinfoNoBkfwlxCount", suspinfoNoBkfwlxCount);
			map.put("passrecCount", passrecCount);
			
			if (localSurveillanceApplication) {
				long _13= System.currentTimeMillis();
				int editCount = this.suspinfoEditManager.getSuspinfoEditCount(begin, end, user.getYhdh(), "1");
				long _14 = System.currentTimeMillis();
				System.out.println("=======(首页)本地布控待修改C======"+(_14-_13) + "ms");
				map.put("editCount", editCount);
			}
			
			if ( transRegionalSurveillanceApplication ) {
				long _15= System.currentTimeMillis();
				int editBkfwlxCount = this.suspinfoEditManager.getSuspinfoEditCount(begin, end, user.getYhdh(), "2");
				long _16 = System.currentTimeMillis();
				System.out.println("=======(首页)联动布控待修改C======"+(_16-_15) + "ms");
				map.put("editBkfwlxCount", editBkfwlxCount);
			}
			
			if( surveillanceAudit ) {
				long _17= System.currentTimeMillis();
				int suspinfoAuditCount = this.suspinfoAuditApproveManager.getSuspinfoAuditCount(begin, end, userSession.getDepartment().getGlbm());
				long _18 = System.currentTimeMillis();
				System.out.println("=======(首页)布控未审核C======"+(_18-_17) + "ms");
				
				long _19= System.currentTimeMillis();
				int suspinfoAuditOverTimeCount = this.suspinfoAuditApproveManager.getSuspinfoAuditOverTimeCount(begin, end, userSession.getDepartment().getGlbm());
				long _20 = System.currentTimeMillis();
				System.out.println("=======(首页)布控超时未审核C======"+(_20-_19) + "ms");
				
				map.put("suspinfoAuditCount", suspinfoAuditCount);
				map.put("suspinfoAuditOverTimeCount", suspinfoAuditOverTimeCount);
			}
			
			if ( surveillanceApproval ) {
				
				long _19= System.currentTimeMillis();
				int suspinfoApproveCount = this.suspinfoAuditApproveManager.getSuspinfoApproveCount(begin, end, userSession.getDepartment().getGlbm());
				long _20 = System.currentTimeMillis();
				System.out.println("=======(首页)布控未审批C======"+(_20-_19) + "ms");
				
				long _21= System.currentTimeMillis();
				int suspinfoApproveOverTimeCount = this.suspinfoAuditApproveManager.getSuspinfoApproveOverTimeCount(begin, end, userSession.getDepartment().getGlbm());
				long _22 = System.currentTimeMillis();
				System.out.println("=======(首页)布控超时未审批C======"+(_22-_21) + "ms");
				
				map.put("suspinfoApproveCount", suspinfoApproveCount);
				map.put("suspinfoApproveOverTimeCount", suspinfoApproveOverTimeCount);
			}
			if ( earlyWarningConfirm ) {
				long _23= System.currentTimeMillis();
				int alarmNoHandleCount = this.vehAlarmHandleManager.getAlaramNoHandleCount(begin,end,userSession.getDepartment().getGlbm());
				long _24= System.currentTimeMillis();
				System.out.println("=======(首页)报警未确认(bjdl(1,2))C======"+(_24-_23) + "ms");
				
				long _25= System.currentTimeMillis();
				int overTimeAlarmCount = this.vehAlarmManager.getOverTimeAlarmCountInThisMonth(userSession.getDepartment().getGlbm());
				long _26= System.currentTimeMillis();
				System.out.println("=======(首页)报警未确认C======"+(_26-_25) + "ms");
				
				map.put("alarmNoHandleCount", alarmNoHandleCount);
				map.put("overTimeAlarmCount", overTimeAlarmCount);
			}
			if ( interceptConfirmation ) {
				long _27= System.currentTimeMillis();
				int alarmLjSuspinfoCount = this.vehAlarmHandleManager.getAlarmNoLjSuspinfoCount(begin, end, userSession.getDepartment().getGlbm(),userSession.getSysuser().getYhdh(),userSession.getDepartment().getJb(),userSession.getDepartment().getBmlx());
				long _28= System.currentTimeMillis();
				System.out.println("=======(首页)报警未拦截C======"+(_28-_27) + "ms");
				
				map.put("alarmLjSuspinfoCount", alarmLjSuspinfoCount);
			}
			if ( backoutSurveillanceApplication ) {
				long _25= System.currentTimeMillis();
				int alarmCancelSuspinfoCount = this.vehAlarmHandleManager.getAlarmNoCancleSuspinfoCount(begin, end, userSession.getDepartment().getGlbm(),userSession.getSysuser().getYhdh());
				long _26= System.currentTimeMillis();
				System.out.println("=======(首页)报警未撤控C======"+(_26-_25) + "ms");
				
				int suspinfoExpireCount = this.suspinfoAuditApproveManager.getExpireCancelSuspinfoCount(begin, end, userSession.getDepartment().getGlbm(),userSession.getSysuser().getYhdh());
				long _27= System.currentTimeMillis();
				System.out.println("=======(首页)布控未撤控C======"+(_27-_26) + "ms");
				
    			int suspinfoOuttimeCount = this.suspinfoAuditApproveManager.getSuspinfoOuttimeCountCount(begin, end,userSession.getDepartment().getGlbm(),userSession.getSysuser().getYhdh());
				long _28= System.currentTimeMillis();
				System.out.println("=======(首页监测)超时未撤控C======"+(_28-_27) + "ms");
				map.put("suspinfoOuttimeCount", suspinfoOuttimeCount);
				
				//int suspinfoOuttimeCount = this.suspinfoAuditApproveManager.getSuspinfoOuttimeCountCount(begin, end,userSession.getDepartment().getGlbm(),userSession.getSysuser().getYhdh());
				//long _28= System.currentTimeMillis();
				//System.out.println("=======(首页)超时未撤控审批C======"+(_28-_27) + "ms");
				
				map.put("alarmCancelSuspinfoCount", alarmCancelSuspinfoCount);
				map.put("suspinfoExpireCount", suspinfoExpireCount);
			}

			if ( backoutSurveillanceAudit ) {
				long _29= System.currentTimeMillis();
				int suspinfoCancelAuditCount = this.suspinfoAuditApproveManager.getSuspinfoCancelAuditCount(begin, end, userSession.getDepartment().getGlbm());
				long _30= System.currentTimeMillis();
				System.out.println("=======(首页)撤控审核C======"+(_30-_29) + "ms");
				
				int suspinfoCancelAuditOverTimeCount = this.suspinfoAuditApproveManager.getSuspinfoCancelAuditOverTimeCount(begin, end, userSession.getDepartment().getGlbm());
				long _31= System.currentTimeMillis();
				System.out.println("=======(首页)撤控超时未审核C======"+(_31-_30) + "ms");
				
				map.put("suspinfoCancelAuditCount", suspinfoCancelAuditCount);
				map.put("suspinfoCancelAuditOverTimeCount", suspinfoCancelAuditOverTimeCount);
			}
			if ( backoutSurveillanceApproval ) {
				long _32= System.currentTimeMillis();
				int suspinfoCancelApproveCount = this.suspinfoAuditApproveManager.getSuspinfoCancelApproveCount(begin, end, userSession.getDepartment().getGlbm());
				long _33= System.currentTimeMillis();
				System.out.println("=======(首页)撤控审批数C======"+(_33-_32) + "ms");
				
				int suspinfoCancelApproveOverTimeCount = this.suspinfoAuditApproveManager.getSuspinfoCancelApproveOverTimeCount(begin, end, userSession.getDepartment().getGlbm());
				long _34= System.currentTimeMillis();
				System.out.println("=======(首页)撤控超时未审批数C======"+(_34-_33) + "ms");
				
				map.put("suspinfoCancelApproveCount", suspinfoCancelApproveCount);
				map.put("suspinfoCancelApproveOverTimeCount", suspinfoCancelApproveOverTimeCount);
			}
			if ( giveOrder ) {
				long _35= System.currentTimeMillis();
				int alarmcmdCount = this.vehAlarmHandleManager.getAlaramCmdCount(this.systemManager.getSysDate("-1/24",true), this.systemManager.getSysDate("+1/24", true),userSession.getDepartment().getGlbm());
				long _36= System.currentTimeMillis();
				System.out.println("=======(首页)未下达指令数C======"+(_36-_35) + "ms");
				
				map.put("alarmcmdCount", alarmcmdCount);
				//userXml.append("<dataset seriesName='报警未下达指令'><set value='").append(alarmcmdCount).append("' link='JavaScript:showData(14)").append("' /></dataset>");
			}
			if ( policeFeedbackEntering ) {
				long _37= System.currentTimeMillis();
				int alarmNoHandleBackCount = this.vehAlarmHandleManager.getAlaramNoHandledBackCount(begin, end, userSession.getDepartment().getGlbm());
				long _38= System.currentTimeMillis();
				System.out.println("=======(首页)报警未反馈C======"+(_38-_37) + "ms");
				
				map.put("alarmNoHandleBackCount", alarmNoHandleBackCount);
				//userXml.append("<dataset seriesName='报警未出警反馈'><set value='").append(alarmNoHandleBackCount).append("' link='JavaScript:showData(15)").append("' /></dataset>");
			}
			if ( failureProcessFeedback ) {
				long _39= System.currentTimeMillis();
				MaintainHandle handle = new MaintainHandle();
				handle.setSfcl("0");
				Map maintainMap = this.maintainManager.findMaintainHandleForMap(filter,handle,userSession.getDepartment());
				long _40= System.currentTimeMillis();
				System.out.println("=======(首页)故障C======"+(_40-_39) + "ms");
				
				int maintainCount = (Integer) maintainMap.get("total");
				map.put("maintainCount", maintainCount);
				//userXml.append("<dataset seriesName='未处理故障数'><set value='").append(maintainCount).append("' link='JavaScript:showData(17)").append("' /></dataset>");
			}
			if ( alarmInformationQuery ) {
				long _11= System.currentTimeMillis();
				int confirmOut = this.vehAlarmManager.getConfirmOut();
				long _12 = System.currentTimeMillis();
				System.out.println("=======(首页)预警签收签收确认超时C======"+(_12-_11) + "ms");
				map.put("confirmOut", confirmOut);
				//userXml.append("<dataset seriesName='本月确认超时数'><set value='").append(confirmOut).append("' link='JavaScript:showData(23)").append("' /></dataset>");
			}
			if ( gateQuery ){
				CodeGate d = new CodeGate();
				d.setKkzt("1");
				int kkzys = this.gateDao.getGateCount(d,null);
				d.setKkzt("2");
				int kkbfs = this.gateDao.getGateCount(d,null);
				d.setKkzt("3");
				int kkwxs = this.gateDao.getGateCount(d,null);
				d.setKkzt("4");
				int kkgzs = this.gateDao.getGateCount(d,null);
				map.put("kkzys", kkzys);
				map.put("kkbfs", kkbfs);
				map.put("kkwxs", kkwxs);
				map.put("kkgzs", kkgzs);
				
			}
			//userXml.append("</graph>");
			//System.out.println(userXml.toString());
			//map.put("userXml", userXml.toString());
	    }catch(Exception e) {
			e.printStackTrace();
			
		}
		return map;
	}
		
    public Map getDataCountDefereInfo(HttpServletRequest request,HttpServletResponse response, UserSession userSession,SysUser user){
    	Map map = new HashMap();
    	boolean kkjrInfo = false;
    	boolean kkzxInfo = false;
    	boolean sjcsInfo = false;
    	
    	try {
        	List<String> list = roleMenuManager.queryMenusByRole(user.getYhdh());

    		for(int i=0; i<list.size(); i++){
    			//卡口接入监测
    			if("100091".equals(list.get(i))){
    				kkjrInfo = true;
    				continue;
    			}
    			//卡口在线监测
    			if("100087".equals(list.get(i))) {
    				kkzxInfo = true;
    				continue;
    			}
    			//数据传输情况监测
    			if("100088".equals(list.get(i))) {
    				sjcsInfo = true;
    				continue;
    			}
    		}
    		
			if(kkjrInfo){
				long _5= System.currentTimeMillis();
				int kkjrsinfoCount = this.kkjrjcProjectManager.getKkjrjcQueryCount();
				long _6 = System.currentTimeMillis();
				System.out.println("=======(首页监测)卡口接入数C======"+(_6-_5) + "ms");
	    		map.put("kkjrsinfoCount", kkjrsinfoCount);
			}
    		
			if(kkzxInfo){
				long _7= System.currentTimeMillis();
				int kkzxinfoCount = this.kkjrjcProjectManager.getKkzxQueryCount();
				long _8 = System.currentTimeMillis();
				System.out.println("=======(首页监测)卡口在线情况C======"+(_8-_7) + "ms");
				map.put("kkzxinfoCount",kkzxinfoCount);
			}
    		
			if(sjcsInfo){
				long _9= System.currentTimeMillis();
				int sjcsinfoCount = this.sjcsjcProjectManager.getSjcsQueryCount();
				long _10 = System.currentTimeMillis();
				System.out.println("=======(首页监测)数据传输情况监测C======"+(_10-_9) + "ms");
				map.put("sjcsinfoCount", sjcsinfoCount);
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    	} 
			return map;
		}
	
	

	public Map getGateProjectData(HttpServletRequest request,
			HttpServletResponse response, UserSession userSession, SysUser user) {
		//List<String> years = new ArrayList<String>();
		//List<Long> quantities = new ArrayList<Long>();
		Map map = new HashMap();
		boolean gateProjectRegister = false;
		try {
			String[]roles = user.getRoles().split(",");
			for(String role : roles){
				List<RoleMenu> list = roleMenuManager.getRoleMenuList(role);
				for(RoleMenu rm : list){
					if(rm.getCxdh().equals("100074")) gateProjectRegister = true;
				}
			}
			if ( gateProjectRegister ) {
				StringBuffer gateProjectXml = 
					new StringBuffer("<graph  baseFont='SumSim' baseFontSize='12' " +
							"caption='卡口规划登记线状图' xAxisName='年份' yAxisName='数量' " +
							"lineColor='3087E8' anchorBgColor='3087E8' anchorBorderColor='E6F1F8' " +
							" bgColor='E6F1F8' showBorder='0' canvasBorderColor='E6F1F8' canvasBgColor='CDD1C8' " +
							"hovercapbg='FFECAA' hovercapborder='F47E00' formatNumberScale='0' " +
							"showValues = '1' numdivlines='5' numVdivlines='0' showNames='1' rotateNames='1' " +
							"slantLabels='1' rotateYAxisName='0' showAlternateHGridColor='0'>");
			
					List<GateProject> list = this.gateProjectManager.getGateProjects();
					for(GateProject gateProject : list) {
						String year = gateProject.getRq();
						Long quantity = gateProject.getGhjrs();
						//years.add(year);
						//quantities.add(quantity);
						gateProjectXml.append("<set name='").append(year).append("' value='").append(quantity).append("' link='JavaScript:openGateProject()").append("' />");
					}
					 gateProjectXml.append("</graph>");
				 //map.put("years",years);
				 //map.put("quantities",quantities);
				 map.put("gateProjectXml",gateProjectXml.toString());
			} else {
				map = null ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public Map getGateInCount() throws Exception {
		StringBuffer userXml = new StringBuffer("<graph  baseFont='SumSim' baseFontSize='12' unescapeLinks='0' caption='全省卡口备案情况' xAxisName='地市' yAxisName='数量' bgColor='E6F1F8' hovercapbg='FFECAA' hovercapborder='F47E00' formatNumberScale='0' showValues = '1' numdivlines='5' numVdivlines='0' showNames='1' rotateNames='1' slantLabels='1' rotateYAxisName='0' showAlternateHGridColor='1'>");
		Map resultMap=new HashMap();
		List gate_List=this.gateProjectManager.getGateInfo();
		for(int i=0;i<gate_List.size();i++){
			Map gate_Map=(Map)gate_List.get(i);
	    	userXml.append("<set name='"+gate_Map.get("JDMC").toString()+"' value='").append(gate_Map.get("TOTAL").toString()).append("' ").append(" />");
		}
		 userXml.append("</graph>");
		    resultMap.put("st", "st");
		    resultMap.put("gateXml", userXml.toString());
			return resultMap;
		/* Map map=this.gateProjectManager.getGateConnectCount();
	    Map resultMap=new HashMap();
	   // resultMap.put("gateConnectCount", map);
	    Set key=map.keySet();
	    Iterator it=  key.iterator();
	    for(int i=0;it.hasNext();i++){
	    	String cityname=it.next().toString();
	    	Map count=(Map) map.get(cityname);
	    	userXml.append("<set name='"+cityname+"卡口接入总数' value='").append(count.get("TOTAL")).append("' ").append(" />");
	    	//userXml.append("<set name='"+cityname+"卡口接入总数' value='").append(count.get("TOTAL")).append("' link='JavaScript:showkkConnect("+i+")").append("' />");
	    	//userXml.append("<set name='"+cityname+"卡口在线数' value='").append(count.get("ON_LINE")).append("' link='JavaScript:showkkConnect("+i+")").append("' />");
	    }
	   userXml.append("</graph>");
	    resultMap.put("userXml", userXml.toString());
		return resultMap;*/
		
	}
	
	public Map getGateInCountByCity(String glbm) throws Exception {
		Map resultMap=new HashMap();
		List gate_List=this.gateProjectManager.getGateInfoByCity(glbm);
		StringBuffer userXml = new StringBuffer();
		for(int i=0;i<gate_List.size();i++){
			Map gate_Map=(Map)gate_List.get(i);
			userXml.append("<graph  baseFont='SumSim' baseFontSize='12' unescapeLinks='0' caption='地市卡口接入情况(卡口备案数:"+gate_Map.get("TOTAL").toString()+")' xAxisName='地市' yAxisName='数量' bgColor='E6F1F8' hovercapbg='FFECAA' hovercapborder='F47E00' formatNumberScale='0' showValues = '1' numdivlines='5' numVdivlines='0' showNames='1' rotateNames='1' slantLabels='1' rotateYAxisName='0' showAlternateHGridColor='1'>");
	    	//userXml.append("<set name='卡口备案数' value='").append(gate_Map.get("TOTAL").toString()).append("' ").append(" />");
	    	userXml.append("<set name='卡口接入数' value='").append(gate_Map.get("ON_LINE").toString()).append("' ").append(" />");
	    	userXml.append("<set name='卡口报废数' value='").append(gate_Map.get("BREAK").toString()).append("' ").append(" />");
	    	userXml.append("<set name='卡口维修数' value='").append(gate_Map.get("FIX").toString()).append("' ").append(" />");
		}
		 userXml.append("</graph>");
		 	resultMap.put("city", "city");
		    resultMap.put("gateXml", userXml.toString());
			return resultMap;
	}
	
	public Map getFlow(UserSession userSession, SysUser user) {
		String glbm = userSession.getDepartment().getGlbm();
		Map map = new HashMap();
		String flowXml = null;
		try {
			if (StringUtils.isNotBlank(glbm) && glbm.indexOf("440000") == 0) {
				List cityList = this.systemManager.getCode("105000");
				List<Map<String,Object>> flowList = this.statMonitorDao.getStFlow(cityList);
				String tjrq = "";
				for(Map m : flowList) {
					if(m.get("TJRQ")!=null) {
						tjrq=m.get("TJRQ").toString();
						break;
					}
				}
				String[] key = {"CITYNAME","GCS"};
				List datalist = FusionChartsXMLGenerator.getInstance().getSingleDSXMLData(flowList, key);
				flowXml = FusionChartsXMLGenerator.getInstance().getSingleDSXML(datalist, "地市过车流量统计", "最新统计日期:"+tjrq+"月", "1", "", "地市","流量",1, 1, 0, 1);
				System.out.println(flowXml);
			} else {
				Map dataMap = this.statMonitorDao.getFlow();
				flowXml = "<graph  baseFont='SumSim' baseFontSize='12' unescapeLinks='0' caption='过车流量统计' xAxisName='说明' yAxisName='流量' bgColor='E6F1F8' hovercapbg='FFECAA' hovercapborder='F47E00' formatNumberScale='0' showValues = '1' numdivlines='5' numVdivlines='0' showNames='1' rotateNames='1' slantLabels='1' rotateYAxisName='0' showAlternateHGridColor='1'>";
				flowXml += "<set name='过车总数' value = '"+dataMap.get("GCS")+"' />";
				flowXml += "<set name='小车数' value = '"+dataMap.get("XCS")+"' />";
				flowXml += "<set name='大车数' value = '"+dataMap.get("DCS")+"' />";
				flowXml += "<set name='违法车数' value = '"+dataMap.get("WFCS")+"' />";
				flowXml += "<set name='正常车数' value = '"+dataMap.get("ZCCS")+"' />";
				flowXml += "<set name='小车违法数' value = '"+dataMap.get("XCWFS")+"' />";
				flowXml += "<set name='大车违法数' value = '"+dataMap.get("DCWFS")+"' />";
				flowXml += "<set name='省内车数' value = '"+dataMap.get("SNCS")+"' />";
				flowXml += "<set name='省外车数' value = '"+dataMap.get("SWCS")+"' />";
				flowXml += "<set name='其他颜色车' value = '"+dataMap.get("QTYSC")+"' />";
				flowXml += "<set name='粤港车数' value = '"+dataMap.get("YGCS")+"' />";
				flowXml += "<set name='军警车数' value = '"+dataMap.get("JJCS")+"' />";
				flowXml += "<set name='无牌车数' value = '"+dataMap.get("WPCS")+"' />";
				flowXml += "<set name='蓝牌车数' value = '"+dataMap.get("LPCS")+"' />";
				flowXml += "<set name='黄牌车数' value = '"+dataMap.get("HPCS")+"' />";
				flowXml += "<set name='其他车数' value = '"+dataMap.get("QTCS")+"' />";
				flowXml += "</graph>";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("flowXml", flowXml);
		return map;
	}
	
	public Map getPoliceRatio(Map condition) {
		Map map = null;
		try {
			map = this.logDao.getPoliceRatio(condition);
			int lgate = this.kkjrjcProjectDao.getKkzxCount(condition.get("lastMonth_start").toString(), condition.get("lastMonth_end").toString());
			int tgate = this.kkjrjcProjectDao.getKkzxCount(condition.get("thisMonth_start").toString(), condition.get("thisMonth_end").toString());
			map.put("LGATE", lgate);
			map.put("TGATE", tgate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
}
