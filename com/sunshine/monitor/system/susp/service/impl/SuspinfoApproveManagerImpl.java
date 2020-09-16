

package com.sunshine.monitor.system.susp.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.comm.maintain.SmsFacade;
import com.sunshine.monitor.comm.maintain.dao.MaintainDao;
import com.sunshine.monitor.comm.maintain.sms.DefaultSmsTransmitterCallBack;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.activemq.bean.SuspinfoMessage;
import com.sunshine.monitor.system.activemq.bean.TransSusp;
import com.sunshine.monitor.system.activemq.dao.TransDao;
import com.sunshine.monitor.system.communication.dao.CommunicationDao;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.SysparaDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.dao.UrlDao;
import com.sunshine.monitor.system.manager.service.PrefectureManager;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;
import com.sunshine.monitor.system.susp.dao.SuspInfoAuditDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoApproveDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoEditDao;
import com.sunshine.monitor.system.susp.service.SuspinfoApproveManager;

@Transactional
@Service("suspinfoApproveManager")
public class SuspinfoApproveManagerImpl implements SuspinfoApproveManager {

	@Autowired
	@Qualifier("suspinfoApproveDao")
	private SuspinfoApproveDao suspinfoApproveDao;
	
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("sysparaDao")
	private SysparaDao sysparaDao;
	
	@Autowired
	private UrlDao urlDao;
	
	@Autowired
	@Qualifier("suspinfoAuditDao")
	private SuspInfoAuditDao suspinfoAuditDao;
	
	@Autowired
	@Qualifier("suspinfoEditDao")
	private SuspinfoEditDao suspinfoEditDao;
	
	@Autowired
	@Qualifier("transDao")
	private TransDao transDao;
	
	@Autowired
	@Qualifier("CommunicationDao")
	private CommunicationDao communicationDao;
	
	@Autowired
	@Qualifier("maintainDao")
	private MaintainDao maintainDaoImpl;
	
	@Autowired
	@Qualifier("prefectureManager")
	private PrefectureManager prefectureManager;
	/**
	 * 布控指挥中心审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	private Logger logger = LoggerFactory.getLogger(SuspinfoApproveManagerImpl.class);

	@OperationAnnotation(type=OperationType.SUSP_APPROVAL_QUERY,description="布控指挥中心审批查询")
	public Map<String, Object> getSuspinfoApproves(Map filter,
			VehSuspinfo info, String glbm) throws Exception {
		Map<String, Object> map =  this.suspinfoApproveDao.getSusinfoApproves(filter, info, glbm);
		List queryList = (List) map.get("rows");
		for (int i=0;i<queryList.size();i++) {
			Map  susp = (Map) queryList.get(i);
			susp.put("SHRMC",this.suspinfoApproveDao.getBkshrmc(susp.get("bkxh").toString()));
		}
		map.put("rows", queryList);
		return map;
	}
	
	/**
	 * 布控指挥中心审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	@OperationAnnotation(type=OperationType.SUSP_APPROVAL_QUERY,description="布控指挥中心审批查询")
	public Map<String, Object> getSuspinfoApprovesOverTime(Map filter,
			VehSuspinfo info, String glbm) throws Exception {
		Map<String, Object> map =  this.suspinfoApproveDao.getSusinfoApprovesOverTime(filter, info, glbm);
		return map;
	}
	/**
	 * 布控指挥中心涉案类审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	@OperationAnnotation(type=OperationType.SUSP_APPROVAL_SA_QUERY,description="布控指挥中心涉案类审批查询")
	public Map<String, Object> getSuspinfoClassApproves(Map filter,
			VehSuspinfo info, String glbm) throws Exception {
		Map<String, Object> map =  this.suspinfoApproveDao.getSusinfoClassApproves(filter, info, glbm);
		/*
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> vehSupsinfo = (Map<String, Object>) i.next();
			vehSupsinfo.put("BKJGMC",this.systemDao.getDepartmentName(glbm));
			list.add(vehSupsinfo);
		}
		map.put("rows", list);
		*/
		return map;
	}
	/**
	 * 布控指挥中心交通类审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	@OperationAnnotation(type=OperationType.SUSP_APPROVAL_JT_QUERY,description="布控指挥中心交通类审批查询")
	public Map<String, Object> getSuspinfoTrafficApproves(Map filter,
			VehSuspinfo info, String glbm) throws Exception {
		Map<String, Object> map =  this.suspinfoApproveDao.getSusinfoTrafficApproves(filter, info, glbm);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> vehSupsinfo = (Map<String, Object>) i.next();
			vehSupsinfo.put("BKJGMC",this.systemDao.getDepartmentName(glbm));
			list.add(vehSupsinfo);
		}
		map.put("rows", list);
		return map;
	}
	
	
	

	public SuspinfoApproveDao getSuspinfoApproveDao() {
		return suspinfoApproveDao;
	}

	public void setSuspinfoApproveDao(SuspinfoApproveDao suspinfoApproveDao) {
		this.suspinfoApproveDao = suspinfoApproveDao;
	}

	public SystemDao getSystemDao() {
		return systemDao;
	}

	public void setSystemDao(SystemDao systemDao) {
		this.systemDao = systemDao;
	}
	
	public SysparaDao getSysparaDao() {
		return sysparaDao;
	}

	public void setSysparaDao(SysparaDao sysparaDao) {
		this.sysparaDao = sysparaDao;
	}
	
	/**
	 * 
	 * @param bkxh
	 * @param glbm
	 * @return
	 * @throws Exception 
	 */
	public Object getApproveDetailForBkxh111(String bkxh) throws Exception {
		VehSuspinfo vehInfo = (VehSuspinfo) this.suspinfoApproveDao.getApprpvesDetailForBkxh111(bkxh);
		List<Code> hpzlList = this.systemDao.getCode("030107");
		
		for (Code code : hpzlList) {
			if (code.getDmz().equals(vehInfo.getHpzl())) {
				vehInfo.setHpzlmc(code.getDmsm1());
			}
		}
		
		List<Code> hpysList = this.systemDao.getCode("031001");
		for (Code code : hpysList) {
			if (code.getDmz().equals(vehInfo.getHpys())) {
				vehInfo.setHpysmc(code.getDmsm1());
			}
		}
		
		List<Code> bkdlList = this.systemDao.getCode("120019");
		for (Code code : bkdlList) {
			if (code.getDmz().equals(vehInfo.getBkdl())) {
				vehInfo.setBkdlmc(code.getDmsm1());
			}
		}
		
        List<Code> cllxList = this.systemDao.getCode("030104");
        for (Code code : cllxList) {
        	if (code.getDmz().equals(vehInfo.getCllx())) {
        		vehInfo.setCllx(code.getDmsm1());
        	}
        }
        
        List<Code> bklbList = this.systemDao.getCode("120005");
        for (Code code : bklbList) {
        	if (code.getDmz().equals(vehInfo.getBklb())) {
        		vehInfo.setBklbmc(code.getDmsm1());
        	}
        }
        
        List<Code> ywztList = this.systemDao.getCode("120008");
        for (Code code : ywztList) {
        	if (code.getDmz().equals(vehInfo.getYwzt())) {
        		vehInfo.setYwztmc(code.getDmsm1());
        	}
        }
        
        List<Code> bkjbList = this.systemDao.getCode("120020");
        for (Code code : bkjbList) {
        	if (code.getDmz().equals(vehInfo.getBkjb())) {
        		vehInfo.setBkjbmc(code.getDmsm1());
        	}
        }
        
        if(StringUtils.isNotBlank(vehInfo.getBkfw())){
			if("3".equals(vehInfo.getBkfwlx())){
				vehInfo.setBkfwmc(getksBkfw(vehInfo.getBkfw()));
			}else{
				//System.out.println("111");
				vehInfo.setBkfwmc(getBkfw(vehInfo.getBkfw()));
			}
		}
        
        
        List<Code> bkfwlxList = this.systemDao.getCode("120004");
        for (Code code : bkfwlxList) {
        	if (code.getDmz().equals(vehInfo.getBkfwlx())) {
        		vehInfo.setBkfwlx(code.getDmsm1());
        	}
        }
        
        List<Code> bkxzList = this.systemDao.getCode("120021");
        for (Code code : bkxzList) {
        	if (code.getDmz().equals(vehInfo.getBkxz())) {
        		vehInfo.setBkxzmc(code.getDmsm1());
        	}
        }
        
        List<Code> sqsbList = this.systemDao.getCode("190001");
        for (Code code : sqsbList) {
        	if (code.getDmz().equals(vehInfo.getSqsb())) {
        		vehInfo.setSqsbmc(code.getDmsm1());
        	}
        }
        
        if (vehInfo.getCsys() != null && vehInfo.getCsys().length() > 0) {
        	vehInfo.setCsysmc(getCsys(vehInfo.getCsys()));
        }
        
       
        
        if (vehInfo.getBjfs() != null && vehInfo.getBjfs().length() > 0) {
        	vehInfo.setBjfsmc(getBjfs(vehInfo.getBjfs()));
        }
        
        if (vehInfo.getJyaq() != null && vehInfo.getJyaq().length() > 0) {
        	vehInfo.setJyaq(TextToHtml(vehInfo.getJyaq()));
        }
        
        
        try{
            Map map=communicationDao.getcommunicateByXh(bkxh);
    		if(map.get("DXNR")!=null)
    			vehInfo.setXjdx(map.get("DXNR").toString());
    		else
    			vehInfo.setXjdx("");
            }
            catch(Exception e){
            	logger.info("communication表不存在");
            }
        
        return vehInfo;
	}
	
	/**
	 * 
	 * @param bkxh
	 * @param glbm
	 * @return
	 */
	public Object getApproveDetailForBkxh(String bkxh, String glbm)
			throws Exception {
		VehSuspinfo vehInfo = (VehSuspinfo) this.suspinfoApproveDao.getApprpvesDetailForBkxh(bkxh, glbm);
		List<Code> hpzlList = this.systemDao.getCode("030107");
		
		for (Code code : hpzlList) {
			if (code.getDmz().equals(vehInfo.getHpzl())) {
				vehInfo.setHpzlmc(code.getDmsm1());
			}
		}
		
		List<Code> hpysList = this.systemDao.getCode("031001");
		for (Code code : hpysList) {
			if (code.getDmz().equals(vehInfo.getHpys())) {
				vehInfo.setHpysmc(code.getDmsm1());
			}
		}
		
		List<Code> bkdlList = this.systemDao.getCode("120019");
		for (Code code : bkdlList) {
			if (code.getDmz().equals(vehInfo.getBkdl())) {
				vehInfo.setBkdlmc(code.getDmsm1());
			}
		}
		
        List<Code> cllxList = this.systemDao.getCode("030104");
        for (Code code : cllxList) {
        	if (code.getDmz().equals(vehInfo.getCllx())) {
        		vehInfo.setCllx(code.getDmsm1());
        	}
        }
        
        List<Code> bklbList = this.systemDao.getCode("120005");
        for (Code code : bklbList) {
        	if (code.getDmz().equals(vehInfo.getBklb())) {
        		vehInfo.setBklbmc(code.getDmsm1());
        	}
        }
        
        List<Code> ywztList = this.systemDao.getCode("120008");
        for (Code code : ywztList) {
        	if (code.getDmz().equals(vehInfo.getYwzt())) {
        		vehInfo.setYwztmc(code.getDmsm1());
        	}
        }
        
        List<Code> bkjbList = this.systemDao.getCode("120020");
        for (Code code : bkjbList) {
        	if (code.getDmz().equals(vehInfo.getBkjb())) {
        		vehInfo.setBkjbmc(code.getDmsm1());
        	}
        }
        
        if(StringUtils.isNotBlank(vehInfo.getBkfw())){
			if("3".equals(vehInfo.getBkfwlx())){
				vehInfo.setBkfwmc(getksBkfw(vehInfo.getBkfw()));
			}else{
				//System.out.println("111");
				String bkfwmc = getBkfw(vehInfo.getBkfw());
				String bkfwmc1 = bkfwmc.replaceAll("（\\*）", "");
				vehInfo.setBkfwmc(bkfwmc1);
			}
		}
        
        
        List<Code> bkfwlxList = this.systemDao.getCode("120004");
        for (Code code : bkfwlxList) {
        	if (code.getDmz().equals(vehInfo.getBkfwlx())) {
        		vehInfo.setBkfwlx(code.getDmsm1());
        	}
        }
        
        List<Code> bkxzList = this.systemDao.getCode("120021");
        for (Code code : bkxzList) {
        	if (code.getDmz().equals(vehInfo.getBkxz())) {
        		vehInfo.setBkxzmc(code.getDmsm1());
        	}
        }
        
        List<Code> sqsbList = this.systemDao.getCode("190001");
        for (Code code : sqsbList) {
        	if (code.getDmz().equals(vehInfo.getSqsb())) {
        		vehInfo.setSqsbmc(code.getDmsm1());
        	}
        }
        
        if (vehInfo.getCsys() != null && vehInfo.getCsys().length() > 0) {
        	vehInfo.setCsysmc(getCsys(vehInfo.getCsys()));
        }
        
       
        
        if (vehInfo.getBjfs() != null && vehInfo.getBjfs().length() > 0) {
        	vehInfo.setBjfsmc(getBjfs(vehInfo.getBjfs()));
        }
        
        if (vehInfo.getJyaq() != null && vehInfo.getJyaq().length() > 0) {
        	vehInfo.setJyaq(TextToHtml(vehInfo.getJyaq()));
        }
        
        
        try{
            Map map=communicationDao.getcommunicateByXh(bkxh);
    		if(map.get("DXNR")!=null)
    			vehInfo.setXjdx(map.get("DXNR").toString());
    		else
    			vehInfo.setXjdx("");
            }
            catch(Exception e){
            	logger.info("communication表不存在");
            }
        
        return vehInfo;
	}
	
	/**
	 * 获得车身颜色
	 * @param csys
	 * @return
	 */
	public String getCsys(String csys) throws Exception {
		try {
			if ((csys == null) || (csys.length() < 1))
				return csys;
			String t = csys;
			if (csys.indexOf(",") != -1)
				t = csys.replaceAll(",", "");
			String ccsys = "";
			for (int i = 0; i < t.length(); i++) {
				ccsys = ccsys
						+ this.systemDao.getCodeValue("030108", String.valueOf(t.charAt(i)))
						+ ",";
			}
			if (ccsys.endsWith(","))
				ccsys = ccsys.substring(0, ccsys.length() - 1);
			return ccsys;
		} catch (Exception e) {
		}
		return csys;
	}
	
	/**
	 * 获得布控范围
	 * @param bkfw
	 * @return
	 */
	public String getBkfw(String bkfw) throws Exception {
		try {
			if ((bkfw == null) || (bkfw.length() < 1))
				return bkfw;
			String[] temp = bkfw.split(",");
			String bkfws = "";
			List list = this.getBkfwListTreenew();
			for (int i = 0; i < temp.length; i++) {
				for (Iterator it = list.iterator(); it.hasNext();) {
					CodeUrl cu = (CodeUrl) it.next();
					if (cu.getDwdm().equals(temp[i])) {
						bkfws = bkfws + cu.getJdmc() + ",";
					}
				}
			}
			if (bkfws.endsWith(","))
				bkfws = bkfws.substring(0, bkfws.length() - 1);
			return bkfws;
		} catch (Exception e) {
		}
		return bkfw;
	}
	
	
	/**
	 * by huyaj 2017/7/4s
	 *得到跨省的布控范围
	 *@param ksbkfw
	 * @return
	 * @throws Exception
	 */
	public String getksBkfw(String bkfw) throws Exception {
		try {
			if ((bkfw == null) || (bkfw.length() < 1))
				return bkfw;
			String[] temp = bkfw.split(",");
			String bkfws = "";
//			PropertyUtil.load("ipport");
//			String ipgd=PropertyUtil.get("ipgd").trim();
//			String portgd=PropertyUtil.get("portgd").trim();
//			//POST的URL
//			String url = "http://" + ipgd + ":" +portgd +"/api/gdservice/department/query";
//			//String url = "http://127.0.0.1:8899/api/service/department/query";
//			//建立HttpPost对象
//			HttpPost httppost = new HttpPost(url);
			try {
//				HttpResponse responseSend = new  DefaultHttpClient().execute(httppost);
//				String sendPostResult = EntityUtils.toString(responseSend.getEntity());
//				List<Map<String, Object>> list = (List<Map<String, Object>>)JSON.parse(sendPostResult);
				List<Map<String, Object>> list = prefectureManager.getGdCityTree();
				for(int i=0;i<temp.length;i++){
					for (Iterator it = list.iterator(); it.hasNext();) {
						Map m = (Map)it.next();
						if (m.get("DWDM").toString().equals(temp[i])) {
							bkfws = bkfws + m.get("JDMC").toString().replaceAll(" ","") + ",";
						}
					}
					
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			if (bkfws.endsWith(","))
				bkfws = bkfws.substring(0, bkfws.length() - 1);
			return bkfws;
		} catch (Exception e) {
		}
		return bkfw;
	}
	/**
	 * 查询code_url表信息
	 * @return
	 * @throws Exception
	 */
	public List getBkfwListTree() throws Exception {
		return this.suspinfoApproveDao.getBkfwListTree();
	}
	
	public List getBkfwListTreenew() throws Exception {
		return this.suspinfoApproveDao.getBkfwListTreenew();
	}
	
	/**
	 * 得到报警方式
	 * @param bjfs
	 * @return
	 * @throws Exception
	 */
	public String getBjfs(String bjfs) throws Exception {
		try {
			if ((bjfs == null) || (bjfs.length() < 1))
				return bjfs;
			String bjfss = "";
			if (bjfs.charAt(0) == '1')
				bjfss = bjfss + this.systemDao.getCodeValue("130000", "4")
						+ ",";
			if (bjfs.charAt(1) == '1')
				bjfss = bjfss + this.systemDao.getCodeValue("130000", "3")
						+ ",";
			if (bjfs.charAt(2) == '1')
				bjfss = bjfss + this.systemDao.getCodeValue("130000", "2")
						+ ",";
			if (bjfs.charAt(3) == '1')
				bjfss = bjfss + this.systemDao.getCodeValue("130000", "1")
						+ ",";
			if (bjfss.endsWith(","))
				bjfss = bjfss.substring(0, bjfss.length() - 1);
			return bjfss;
		} catch (Exception e) {
		}
		return bjfs;
	}
	/**
	 * 转义字符
	 * @return
	 * @throws Exception
	 */
	public String TextToHtml(String s) {
		char[] c = s.toCharArray();

		StringBuffer buf = new StringBuffer();
		int i = 0;
		for (int size = c.length; i < size; i++) {
			char ch = c[i];
			if (ch == '"')
				buf.append("&quot;");
			else if (ch == '&')
				buf.append("&amp;");
			else if (ch == '<')
				buf.append("&lt;");
			else if (ch == '>')
				buf.append("&gt;");
			else if (ch == '\r')
				buf.append("");
			else if (ch == '\n')
				buf.append("<br>");
			else if (ch == ' ')
				buf.append("&nbsp;");
			else {
				buf.append(ch);
			}
		}
		c = (char[]) null;
		return buf.toString();
	}
	/**
	 * (根据参数)查询布控信息查询审批/核
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public List getSuspinfoAuditHistory(String bkxh) throws Exception {
		if (bkxh == null || bkxh.length() < 1) {
			return null;
		}
		AuditApprove aa = new AuditApprove();
		aa.setBkxh(bkxh);
		aa.setBzw("12");
		List list = this.suspinfoApproveDao.getAuditApproves(aa);
		for (Iterator it = list.iterator(); it.hasNext();) {
			AuditApprove t = (AuditApprove) it.next();
			t.setCzjg(this.systemDao.getCodeValue("190002", t.getCzjg()));
			if (t.getBzw().equals("1")) {
				t.setBzw("审核");
			} else if (t.getBzw().equals("2")) {
				t.setBzw("审批");
			}
		}
		return list;
	}
	
	/**
	 * 布控指挥中心审批新增
	 * @param info
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@OperationAnnotation(type=OperationType.SUSP_APPROVAL_ADD,description="布控指挥中心审批新增")
	public Map<String,String> saveApprove(final AuditApprove info) throws Exception {
		Map<String,String> result = null;
		final VehSuspinfo vehInfo = (VehSuspinfo) this.suspinfoApproveDao.getApprpvesDetailForBkxh(info.getBkxh(), info.getCzrdw());
		if (vehInfo == null) {
			//省厅布控申请图片不是必须的，不需要进行判断
			//return Common.messageBox("未找到相应的图片信息！", "0");
		}
		Department dp = this.systemDao.getDepartment(info.getCzrdw());
		if (dp == null) {
			//return Common.messageBox("操作人单位为空！", "0");
		} else {
			if (dp.getSsjz() == null || dp.getSsjz().equals("")) {
				return Common.messageBox("本部门缺少警钟信息！", "0");
			}
		}
		try {
			String t_jlzt = "0";
			String t_ywzt = null;
			if (info.getCzjg().equals("0")) {//审批不同意
				t_ywzt = "99";
				//审批不同意，短信通知布控人
				sendSmsToBkrByApproveNoPass(info, vehInfo);
				new Thread(){
					@Override
					public void run() {
						sendSmsToBkrByApproveNoPass(info, vehInfo);
					}
				}.start();
			} else if (info.getCzjg().equals("2")) {
				t_ywzt = "13";
				//sendSmsToBkrByApproveNeedEdit(info, vehInfo);
				new Thread(){
					@Override
					public void run() {
						sendSmsToBkrByApproveNeedEdit(info, vehInfo);
					}
				}.start();
			} else if (info.getCzjg().equals("1")) {
				t_ywzt = "14";
				t_jlzt = "1";
			}
			//保存到审核审批表中
			info.setBzw("2");
			int count  = this.insertAuditApprove(info);
			//更新布控表信息
			this.updateSuspInfo(t_ywzt, t_jlzt, info.getBkxh());
			
			String csz = vehInfo.getBkpt();
			Syspara sysparam = this.sysparaDao.getSyspara("xzqh", "1", "");
			if(csz==null||"".equals(csz)){
				logger.info("联动布控信息中布控平台为空！");
				csz = sysparam.getCsz();
			}
			String bkfw = vehInfo.getBkfw();
			String czjg = info.getCzjg();
			//保存到传输表
			if (t_jlzt.equals("1") && vehInfo.getBkfwlx().equals("2")) {
				//地市版布控数据 省厅平台审批传输单位为地市布控平台
				/*Syspara sysparam = this.sysparaDao.getSyspara("xzqh", "1", "");
				String csz = sysparam.getCsz();*/
				this.insertTransSusp(csz, bkfw, info.getBkxh(), "11");//下发审批结果
			}else if(t_jlzt.equals("0") && vehInfo.getBkfwlx().equals("2")){
				if(!sysparam.getCsz().equals(vehInfo.getBkpt()))//当前平台不是布控平台(省厅)
					this.insertTransSusp(csz, bkfw, info.getBkxh(), "30");//反馈审批结果
			}
			
			TransSusp transSusp = transDao.getTransSuspDetail(info.getBkxh(), 11);
			VehSuspinfo vehSuspinfo = suspinfoEditDao.getSuspinfoDetailForBkxh(info.getBkxh());
			AuditApprove auditApprove = suspinfoEditDao.getAuditApprove(info.getBkxh());
			VehSuspinfopic vspic = new VehSuspinfopic();
			vspic.setBkxh(transSusp.getBkxh());
			vspic.setZjlx(transSusp.getPicname());
			vspic.setZjwj(transSusp.getPic());
			
			if(t_jlzt.equals("1") && vehInfo.getBkfwlx().equals("3") && "14".equals(vehSuspinfo.getYwzt())){
				
				PropertiesConfiguration config;
				config = new PropertiesConfiguration("ipport.properties");
				String ipGd = config.getString("ipgd");
				String portGd = config.getString("portgd");
				
				//POST的URL
				//String url = "http://" + ipGd + ":" + portGd + "/api/gdservice/vehicle/suspService/saveSuspInfo";
				String url = "http://" + ipGd + ":" + portGd + "/api/service/vehicle/SuspRecService/suspks";
				//建立HttpPost对象
				HttpPost httppost = new HttpPost(url);
				//建立一个NameValuePair数组，用于存储欲传送的参数
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				
//				JSONArray json1 = JSONArray.fromObject(vehSuspinfo); 
//				JSONArray json2 = JSONArray.fromObject(auditApprove); 
//				JSONArray json3 = JSONArray.fromObject(vspic);
//				
//				//添加参数
//				params.add(new BasicNameValuePair("vehSuspinfoBean", json1.toString()));
//				params.add(new BasicNameValuePair("auditApproveBean", json2.toString()));
//				params.add(new BasicNameValuePair("vspicBean", json3.toString()));
				
				//添加参数
				params.add(new BasicNameValuePair("sfzmhm","360124199703185195"));
				params.add(new BasicNameValuePair("xh",vehSuspinfo.getBkxh()));
				params.add(new BasicNameValuePair("bkxh",vehSuspinfo.getBkxh()));
				params.add(new BasicNameValuePair("hpzl",vehSuspinfo.getHpzl()));
				params.add(new BasicNameValuePair("hphm",vehSuspinfo.getHphm()));
				params.add(new BasicNameValuePair("bkdl",vehSuspinfo.getBkdl()));
				params.add(new BasicNameValuePair("bklb",vehSuspinfo.getBklb()));
				params.add(new BasicNameValuePair("bkqssj",vehSuspinfo.getBkqssj()));
				params.add(new BasicNameValuePair("bkjzsj",vehSuspinfo.getBkjzsj()));
				params.add(new BasicNameValuePair("jyaq",vehSuspinfo.getJyaq()));
				
				String str = vehSuspinfo.getBkfw();
				String sarr[]=str.split(",");
				String st ="";
				for (int i = 0; i < sarr.length; i++) {
					String ss = sarr[i].substring(0, 4);
					if(i == sarr.length-1 ) {
						st= st + ss;
					}else {
						st= st + ss + ",";
					}	
				}
				params.add(new BasicNameValuePair("bkfw",st));
				params.add(new BasicNameValuePair("bkjb",vehSuspinfo.getBkjb()));
				params.add(new BasicNameValuePair("sqsb",vehSuspinfo.getSqsb()));
				params.add(new BasicNameValuePair("dxjshm",vehSuspinfo.getDxjshm()));
				params.add(new BasicNameValuePair("bkr",vehSuspinfo.getBkr()));
				params.add(new BasicNameValuePair("bkrjh",vehSuspinfo.getBkrjh()));
				params.add(new BasicNameValuePair("bkrmc",vehSuspinfo.getBkrmc()));
				params.add(new BasicNameValuePair("bkjg",vehSuspinfo.getBkjg()));
				params.add(new BasicNameValuePair("bkjgmc",vehSuspinfo.getBkjgmc()));
				params.add(new BasicNameValuePair("bkjglxdh",vehSuspinfo.getBkjglxdh()));
				params.add(new BasicNameValuePair("bksj",vehSuspinfo.getBksj()));
				
				String bkpt = vehSuspinfo.getBkpt();
				String bkpt1 = bkpt.substring(0, 2);
				params.add(new BasicNameValuePair("bkpt",bkpt1));
				params.add(new BasicNameValuePair("lar",vehSuspinfo.getLar()));
				params.add(new BasicNameValuePair("ladw",vehSuspinfo.getLadw()));
				params.add(new BasicNameValuePair("ladwlxdh",vehSuspinfo.getLadwlxdh()));
				params.add(new BasicNameValuePair("clpp", vehSuspinfo.getClpp()));
				params.add(new BasicNameValuePair("hpys", vehSuspinfo.getHpys()));
				params.add(new BasicNameValuePair("clxh", vehSuspinfo.getClxh()));
				params.add(new BasicNameValuePair("cllx", vehSuspinfo.getCllx()));
				params.add(new BasicNameValuePair("csys", vehSuspinfo.getCsys()));
				params.add(new BasicNameValuePair("clsbdh", vehSuspinfo.getClsbdh()));
				params.add(new BasicNameValuePair("fdjh", vehSuspinfo.getFdjh()));
				params.add(new BasicNameValuePair("cltz", vehSuspinfo.getCltz()));
				params.add(new BasicNameValuePair("clsyr", vehSuspinfo.getClsyr()));
				params.add(new BasicNameValuePair("SYRLXDH", vehSuspinfo.getSyrlxdh()));
				params.add(new BasicNameValuePair("syrxxdz", vehSuspinfo.getSyrxxdz()));
				params.add(new BasicNameValuePair("bkfwlx", vehSuspinfo.getBkfwlx()));
				params.add(new BasicNameValuePair("bkxz", vehSuspinfo.getBkxz()));
				params.add(new BasicNameValuePair("bjya", vehSuspinfo.getBjya()));
				params.add(new BasicNameValuePair("bjfs", vehSuspinfo.getBjfs()));
				params.add(new BasicNameValuePair("ywzt", vehSuspinfo.getYwzt()));
				params.add(new BasicNameValuePair("jlzt", vehSuspinfo.getJlzt()));
				params.add(new BasicNameValuePair("gxsj", vehSuspinfo.getGxsj()));
				params.add(new BasicNameValuePair("xxly", vehSuspinfo.getXxly()));
				params.add(new BasicNameValuePair("bjzt", vehSuspinfo.getBjzt()));
				params.add(new BasicNameValuePair("mhbkbj", vehSuspinfo.getMhbkbj()));
				
				/*
				 *另一种方法    利用获取每个参数
				Class clz = vehSuspinfo.getClass();
				Field[] field = clz.getDeclaredFields();
				for(Field f: field) {
					if("serialVersionUID".equals(f.getName()) || "picxh".equals(f.getName()))
						continue;
					if("bkfw".equals(f.getName())) {
						String _bkfw = vehSuspinfo.getBkfw();
						String bkfws[]=_bkfw.split(",");
						String val ="";
						for (int i = 0; i < bkfws.length; i++) {
							String ss = bkfws[i].substring(0, 4);
							if(i == bkfws.length-1 ) {
								val = val + ss;
							}else {
								val = val + ss + ",";
							}	
						}
						params.add(new BasicNameValuePair(f.getName(), val));
						continue;
					}
					if("bkpt".equals(f.getName())) {
						String _bkpt = vehSuspinfo.getBkpt();
						String bkptVal = _bkpt.substring(0, 2);
						params.add(new BasicNameValuePair(f.getName(), bkptVal));
						continue;
					}
					String getMehodName = "get"+StringUtils.capitalize(f.getName());
					Method md = clz.getMethod(getMehodName);
					String objValue = (String) md.invoke(vehSuspinfo);
					params.add(new BasicNameValuePair(f.getName(), objValue));
					System.out.println(objValue);
				}
				params.add(new BasicNameValuePair("sfzmhm","360124199703185195"));
				params.add(new BasicNameValuePair("xh",vehSuspinfo.getBkxh()));*/
				
				
				HttpResponse responseSend;
				httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				responseSend = new DefaultHttpClient().execute(httppost);
//				String sendPostResult = EntityUtils.toString(responseSend.getEntity());
//				Map<String, Object> map = (Map<String, Object>)JSON.parse(sendPostResult);
				
				this.insertTransSusp(csz, bkfw, info.getBkxh(), "11");//下发审批结果
			}
			
			
			//未安装新系统地市按原传输机制传输
			/*String[] bkfws = vehInfo.getBkfw().split(",");
				for(String jsdw : bkfws){
					CodeUrl url = urlDao.getUrl(jsdw);
					if(StringUtils.isNotBlank(url.getBz() ) && "0".equals(url.getBz().substring(0, 1))){
						suspinfoAuditDao.insertTransSusp_test(csz, jsdw, info.getBkxh(),"11");
					}
				}*/
			
			//保存日志表
			this.insertBusinessLog(vehInfo, info, dp.getSsjz(), "12");
			if(count == 1)
				result = Common.messageBox("保存成功！", "1");
			else
				result = Common.messageBox("保存失败！", "0");
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("出现异常！", "0");
		}
		return result;
	}
	
	
	

	@Transactional(propagation = Propagation.REQUIRED)
	public int insertAuditApprove(AuditApprove info) throws Exception {
		return this.suspinfoApproveDao.saveSuspinfoApprove(info);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int insertBusinessLog(VehSuspinfo suspInfo, AuditApprove info,
			String ssjz, String ywlb) throws Exception {
		return this.suspinfoApproveDao.saveSuspinfoApproveLog(info, ywlb, suspInfo, ssjz);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int insertTransSusp(String xzqh, String jsdw, String bkxh,
			String type) throws Exception {
		return this.suspinfoApproveDao.saveTranSusp(xzqh, jsdw, bkxh, type);
	}

	public void sendMsg(SuspinfoMessage message) throws Exception {
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int updateSuspInfo(String T_YWZT, String T_JLZT, String bkxh)
			throws Exception {
		return this.suspinfoApproveDao.updateSuspinfoApprove(T_YWZT, T_JLZT, bkxh);
	}
	
	private void sendSMS(String phone, String content, Map<String, String> dxcondition){
		SmsFacade smsFacade = new SmsFacade();
		if(phone == null || "".equals(phone)){
			dxcondition.put("zt", "2");
			dxcondition.put("sysdate", "''");
			dxcondition.put("reason", "手机号码为空，短信自动作废");
		} else {
			try {
				DefaultSmsTransmitterCallBack callBack = new DefaultSmsTransmitterCallBack();
				boolean flag=smsFacade.sendAndPostSms(phone, content, callBack);
				if(flag){
					dxcondition.put("zt", "1");
					dxcondition.put("sysdate", "sysdate");
				} else {
					dxcondition.put("zt", "2");
					dxcondition.put("sysdate","''");
					dxcondition.put("reason", callBack.getErrorMsg());
				}
			} catch (Exception e) {
				dxcondition.put("zt", "2");
				dxcondition.put("sysdate","''");
				dxcondition.put("reason","短信发送失败" );
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 发送短信给布控人,审批不通过的时候
	 * @param info
	 * @param vehInfo
	 */
	private void sendSmsToBkrByApproveNoPass(AuditApprove info,
			VehSuspinfo vehInfo) {
		//短信立刻发送 ---1
		try{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String content = "您布控的【车辆号牌号码：" + vehInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",vehInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",vehInfo.getBkdl())+"、布控子类："+this.systemDao.getCodeValue("120005",vehInfo.getBklb())+"】在"+sdf.format(new Date())+"审批不通过，原因："+info.getMs()+" ！";
		Map<String, String> dxcondition  = new HashMap<String,String>();
		dxcondition.put("xh", info.getBkxh());
		dxcondition.put("ywlx","43");
		dxcondition.put("dxjsr",vehInfo.getBkr()==null?"":vehInfo.getBkr() );
		dxcondition.put("dxjshm", vehInfo.getBkjglxdh()==null?"":vehInfo.getBkjglxdh());
		dxcondition.put("dxnr", content);
		dxcondition.put("reason", "");
		dxcondition.put("fsfs", "1");
		dxcondition.put("sysdate","''");
		this.sendSMS(vehInfo.getBkjglxdh(), content, dxcondition);
			String value="id,xh,ywlx,dxjsr,dxjshm,dxnr,DXFSSJ,zt,fsfs,reason";
		String values=""
			+"SEQ_FRM_COMMUNICATION.nextval,'"
			+dxcondition.get("xh")
			+"','"
			+dxcondition.get("ywlx")
			+"','"
			+dxcondition.get("dxjsr")
			+"','"
			+dxcondition.get("dxjshm")
			+"','"
			+dxcondition.get("dxnr")
			+"',"
			+dxcondition.get("sysdate")
			+",'"
			+dxcondition.get("zt")
			+"','"
			+dxcondition.get("fsfs")
			+"','"
			+dxcondition.get("reason")
			+"'"
			;
		this.maintainDaoImpl.saveDxsj(value,values);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 发短信给布控人,审批需修改时候
	 * @param info
	 * @param vehInfo
	 */
	private void sendSmsToBkrByApproveNeedEdit(AuditApprove info,
			VehSuspinfo vehInfo) {
		try{
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String content = "您布控的【车辆号牌号码：" + vehInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",vehInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",vehInfo.getBkdl())+"、布控子类："+this.systemDao.getCodeValue("120005",vehInfo.getBklb())+"】在"+sdf.format(new Date())+"待修改，原因："+info.getMs()+" ！";
		Map<String, String> dxcondition  = new HashMap<String,String>();
		dxcondition.put("xh", info.getBkxh());
		dxcondition.put("ywlx","17");
		dxcondition.put("dxjsr",vehInfo.getBkr()==null?"":vehInfo.getLar() );
		dxcondition.put("dxjshm", vehInfo.getBkjglxdh()==null?"":vehInfo.getBkjglxdh());
		dxcondition.put("dxnr", content);
		dxcondition.put("fsfs","1");
		dxcondition.put("sysdate","''");
		dxcondition.put("reason","");
		this.sendSMS(vehInfo.getBkjglxdh(), content, dxcondition);
			String value="id,xh,ywlx,dxjsr,dxjshm,dxnr,DXFSSJ,zt,fsfs,reason";
		String values=""
			+"SEQ_FRM_COMMUNICATION.nextval,'"
			+dxcondition.get("xh")
			+"','"
			+dxcondition.get("ywlx")
			+"','"
			+dxcondition.get("dxjsr")
			+"','"
			+dxcondition.get("dxjshm")
			+"','"
			+dxcondition.get("dxnr")
			+"',"
			+dxcondition.get("sysdate")
			+",'"
			+dxcondition.get("zt")
			+"','"
			+dxcondition.get("fsfs")
			+"','"
			+dxcondition.get("reason")
			+"'"
			;
		
		this.maintainDaoImpl.saveDxsj(value,values);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public List<Map<String, Object>> getBatchApproveList() throws Exception {
		List<Map<String, Object>> list = suspinfoApproveDao.queryBatchApproveList();
		return list;
	}
}
