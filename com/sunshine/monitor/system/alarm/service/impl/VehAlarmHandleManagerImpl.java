package com.sunshine.monitor.system.alarm.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.activemq.bean.TransAlarmHandled;
import com.sunshine.monitor.system.alarm.bean.VehAlarmCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmHandled;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.dao.VehAlarmDao;
import com.sunshine.monitor.system.alarm.service.VehAlarmHandleManager;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.dao.SysUserDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.dao.UrlDao;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspinfoEditDao;


@Transactional
@Service("vehAlarmHandleManager")
public class VehAlarmHandleManagerImpl implements VehAlarmHandleManager {

	@Autowired
	@Qualifier("vehAlarmDao")
	private VehAlarmDao vehAlarmDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("sysUserDao")
	private SysUserDao sysUserDao;
	
	@Autowired
	@Qualifier("suspinfoEditDao")
	private SuspinfoEditDao suspinfoEditDao;
	
	@Autowired
	private UrlDao urlDao;
	/**
	 * 出警反馈信息
	 * @param fillter
	 * @param info
	 * @return
	 * @throws Exception
	 */
	
	@OperationAnnotation(type=OperationType.CALARM_HANDLE_QUERY,description="出警反馈查询")
	public Map getAlarmHandleForMap(Map fillter, VehAlarmCmd info)
			throws Exception {
		return vehAlarmDao.getAlarmHandleForMap(fillter, info);
	}
	/**
	 * （根据报警序号）查询veh_alarm_cmd表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public VehAlarmrec getVehAlarmrecForBjxh(String bjxh) throws Exception {
		VehAlarmrec bean = vehAlarmDao.getVehAlarmForBjxh(bjxh);
		if(bean != null){

			Code code = null;
			code = this.systemDao.getCode("030107", bean.getHpzl());
			if (code != null) {
				bean.setHpzlmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("120019", bean.getBjdl());
			if (code != null) {
				bean.setBjdlmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("120005", bean.getBjlx());
			if (code != null) {
				bean.setBjlxmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("120014", bean.getQrzt());
			if (code != null) {
				bean.setQrztmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("130004", bean.getJyljtj());
			if (code != null) {
				bean.setLjtjmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("120012", bean.getXxly());
			if (code != null) {
				bean.setXxly(code.getDmsm1());
			}
			code = this.systemDao.getCode("031001", bean.getHpys());
			if (code != null) {
				bean.setHpys(code.getDmsm1());
			}
			bean.setTp1(bean.getTp1().replaceAll("\\\\", "/"));
			bean.setTp2(bean.getTp2().replaceAll("\\\\", "/"));
			bean.setTp3(bean.getTp3().replaceAll("\\\\", "/"));
		
		}
		return bean;
	}
	/**
	 * （根据指令序号）查询预警命令信息
	 * @param zlxh 指令序号
	 * @return
	 * @throws Exception
	 */
	public VehAlarmCmd getVehAlarmCmdForZlxh(String zlxh) throws Exception {
		VehAlarmCmd cmd = this.vehAlarmDao.getVehAlarmCmdForZlxh(zlxh);
		if (cmd != null) {
			cmd.setZlfsmc(this.systemDao.getCodeValue("130021", cmd.getZlfs()));
		}
		
		return cmd;
	}
	/**
	 * （根据报警序号）查询veh_alarm_livetrace表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public List getVehAlarmLivetraceForBjxh(String bjxh)
			throws Exception {
		
		return this.vehAlarmDao.getVehAlarmLivetraceListForBjxh(bjxh);
	}
	/**
	 * 插入成功拦截图片
	 * @param fkbh
	 * @param handle
	 * @return
	 * @throws Exception
	 */
	public int insertLjTp(String fkbh,VehAlarmHandled handle) throws Exception{
		
		return vehAlarmDao.insertLjTp( fkbh, handle);
	}
	/**
	 * 
	 * 函数功能说明:保存出警反馈
	 * 修改日期 	2013-6-24
	 * @param handle
	 * @param xzqh
	 * @param ssjz
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	@OperationAnnotation(type=OperationType.CALARM_HANDLE,description="出警反馈")
	public Map setHandle(VehAlarmHandled handle,String xzqh,String ssjz) throws Exception {
		
		try {
			this.insertAlarmHandle(handle, xzqh);
			this.updateAlarmForHandle(handle.getSflj(), handle.getBjxh());
			
			VehSuspinfo susp = suspinfoEditDao.getSuspinfoDetailForBkxh(handle.getBkxh());
			VehAlarmCmd cmd = vehAlarmDao.getVehAlarmCmdForZlxh(handle.getZlxh());
			String fkbh = vehAlarmDao.getVehAlarmHandleFkxhForBean(handle);
			
			if(handle.getTp() != null){
				//插入图片
				insertLjTp(fkbh,handle);
			}
			
			String ddr = handle.getDdr();
			String[]ddrs = ddr.split(",");
			for(String yhdh : ddrs){
				SysUser user = sysUserDao.getSysUserByYHDH(yhdh);
				this.insertBusinessForHandle(fkbh, "44", susp.getBkjb(), cmd.getZlsj(), user, ssjz, "1");
				
				if("1".equals(handle.getSflj())){
					this.insertBusinessForHandle(fkbh, "45", susp.getBkjb(), cmd.getZlsj(), user, ssjz, "1");
				}
			}
			
			String []xbrs = handle.getXbr().split(",");
			for(String yhdh : xbrs){
				SysUser user = sysUserDao.getSysUserByYHDH(yhdh);
				this.insertBusinessForHandle(fkbh, "44", susp.getBkjb(), cmd.getZlsj(), user, ssjz, "0");
				if("1".equals(handle.getSflj())){
					this.insertBusinessForHandle(fkbh, "45", susp.getBkjb(), cmd.getZlsj(), user, ssjz, "0");
				}
			}
			this.updateAlarmScopeForHandle(handle.getZlxh());
			
			CodeUrl url = urlDao.getUrl(susp.getBkpt());
			boolean tip = false;
			if(url != null && StringUtils.isNotBlank(url.getBz()) && "0".equals(url.getBz().substring(0, 1))){
				tip = true;
			}
			//大情报平台
			if(susp.getXxly().equals("5")){
				//省厅
				if(xzqh.equals(susp.getBkpt())){
					this.insertTransTabForHandle(xzqh, null, fkbh, "25", "trans_intelligence");
				}else{
					
					//this.insertTransTabForHandle(xzqh, susp.getBkpt(), fkbh, "25", "jm_trans_alarm");
					
					//if(tip){
				    //this.insertTransTabForHandle(xzqh, susp.getBkpt(), fkbh, "25", "trans_alarm");
					this.insertTransTabForHandle(xzqh, susp.getBkfw(), fkbh, "25", "JM_trans_alarm");
					
					//}
				}
			}
			//联动
			else if(susp.getXxly().equals("2")){
				this.insertTransTabForHandle(xzqh, susp.getBkfw(), fkbh, "25", "jm_trans_alarm");
				

				if(tip){
					this.insertTransTabForHandle(xzqh, susp.getBkpt(), fkbh, "25", "trans_alarm");
				}
				
			}
			//6in1
			else if(susp.getXxly().equals("9")){
				
				if(xzqh.equals(susp.getBkpt())){
					this.insertTransTabForHandle(xzqh, null, fkbh, "25", "trans_trff");
				}else{
					//this.insertTransTabForHandle(xzqh, susp.getBkpt(), fkbh, "25", "jm_trans_alarm");
				

					//if(tip){
					//this.insertTransTabForHandle(xzqh, susp.getBkpt(), fkbh, "25", "trans_alarm");
					this.insertTransTabForHandle(xzqh, susp.getBkfw(), fkbh, "25", "JM_trans_alarm");
					
					//}
				}
			}
			//警综
			else if(susp.getXxly().equals("0") && susp.getBkfwlx().equals("2")){
				this.insertTransTabForHandle(xzqh, susp.getBkfw(), fkbh, "25", "jm_trans_alarm");
			

				if(tip){
					this.insertTransTabForHandle(xzqh, susp.getBkpt(), fkbh, "25", "trans_alarm");
				}
			}
			
			return Common.messageBox("保存成功!", "1");
		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("程序异常!", "0");
		}
		
	}
	/**
	 * 
	 * 函数功能说明:保存出警反馈
	 * 修改日期 	2013-6-24
	 * @param handle
	 * @param xzqh
	 * @param ssjz
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public int insertAlarmHandle(VehAlarmHandled handle, String xzqh)
			throws Exception {
		
		return vehAlarmDao.insertAlarmHandle(handle, xzqh);
	}
	/**
	 * 查询传输出警反馈信息(联动)
	 * @param ywxh  反馈编号
	 * @return
	 * @throws Exception
	 */
	public TransAlarmHandled getAlarmHandledLink(String ywxh) throws Exception{
		return vehAlarmDao.getAlarmHandledLink(ywxh);
	}
	
	/**
	 * 保存出警反馈(联动)
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int saveAlarmHandleLink(TransAlarmHandled bean) throws Exception {
		bean.setXxly("2");
		return vehAlarmDao.saveAlarmHandleLink(bean);
	}
	/**
	 * （根据参数）保存business_log表
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public int insertBusinessForHandle(String fkbh, String ywlb, String bkjb,
			String bzsj, SysUser user, String ssjz, String sfwfzr)
			throws Exception {
		
		return vehAlarmDao.insertBusinessForHandle(fkbh, ywlb, bkjb, bzsj, user, ssjz, sfwfzr);
	}
	/**
	 * （根据各参数）保存表各个字段
	 * @param csdw 
	 * @param jsdw
	 * @param ywxh
	 * @param type
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public int insertTransTabForHandle(String csdw, String jsdw, String ywxh,
			String type, String tableName) throws Exception {
		
		return vehAlarmDao.insertTransTabForHandle(csdw, jsdw, ywxh, type, tableName);
	}
	/**
	 * （根据参数）更新veh_alarmrec表
	 * @param sflj
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public int updateAlarmForHandle(String sflj, String bjxh) throws Exception {
		
		return vehAlarmDao.updateAlarmForHandle(sflj, bjxh);
	}
	/**
	 * （根据参数）更新veh_alarm_cmdscope表
	 * @param zlxh 
	 * @return
	 * @throws Exception
	 */
	public int updateAlarmScopeForHandle(String zlxh) throws Exception {
		
		return vehAlarmDao.updateAlarmScopeForHandle(zlxh);
	}
	/**
	 * （根据报警序号）查询veh_alarm_cmd表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public List getAlarmCmdListForBjxh(String bjxh) throws Exception {
		
		return vehAlarmDao.getVehAlarmCmdListForBjxh(bjxh);
	}
	/**
	 * （根据报警序号）查询veh_alarm_handled表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public List getVehAlarmHandleForBjxh(String bjxh) throws Exception {
		List list = vehAlarmDao.getAlarmHandleForBjxh(bjxh);
		
		for(Object o : list){
			VehAlarmHandled handle = (VehAlarmHandled)o;
			
			String sflj = handle.getSflj();
			if("0".equals(sflj))handle.setSfljmc("未拦截到");
			else handle.setSfljmc("拦截到");
			
			if(StringUtils.isNotBlank(handle.getWljdyy())){
				handle.setWljdyymc(systemDao.getCodeValue("130003", handle.getWljdyy()));
			}
		}
		return list;
	}
	/**
	 * （根据布控序号）查询veh_alarm_handled表信息
	 * @param bkxh 布控序号
	 * @return
	 * @throws Exception
	 */
	public List getVehAlarmHandleForBkxh(String bkxh)throws Exception{
		List list = vehAlarmDao.getAlarmHandleForBkxh(bkxh);
		
		for(Object o : list){
			VehAlarmHandled handle = (VehAlarmHandled)o;
			
			String sflj = handle.getSflj();
			if("0".equals(sflj))handle.setSfljmc("未拦截到");
			else handle.setSfljmc("拦截到");
			
			if(StringUtils.isNotBlank(handle.getWljdyy())){
				handle.setWljdyymc(systemDao.getCodeValue("130003", handle.getWljdyy()));
			}
		}
		return list;
		
	}
	/**
	 * 查询报警未确认数量
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception
	 */
	public int getAlaramNoHandleCount(String begin, String end, String glbm) throws Exception {
		return this.vehAlarmDao.getAlarmNoHandleCount(begin, end, glbm);
	}
	/**
	 * 查询报警指未下达指令数量
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 */
	public int getAlaramCmdCount(String begin, String end, String glbm) throws Exception {
		return this.vehAlarmDao.getAlarmCmdCount(begin, end, glbm);
	}
	/**
	 * 查询报警未反馈数量
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 */
	public int getAlaramNoHandledBackCount(String begin, String end, String glbm) throws Exception {
		return this.vehAlarmDao.getAlarmNoHandledBackCount(begin, end, glbm);
	}
	
	/**
	 * 查询报警未拦截数量
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception
	 */
	public int getAlarmNoLjSuspinfoCount(String begin, String end,
			String glbm,String yhmc,String jb,String bmlx) throws Exception {
		if("3".equals(jb) && "1".equals(bmlx))
		return this.vehAlarmDao.getAlarmNoLjSuspinfoCountForZx(begin, end, glbm, yhmc,jb,bmlx);
		else
		return this.vehAlarmDao.getAlarmNoLjSuspinfoCount(begin, end, glbm, yhmc,jb,bmlx);
		
	}
	/**
	 * 查询报警未撤控数量
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception
	 */
	public int getAlarmNoCancleSuspinfoCount(String begin, String end,
			String glbm,String yhmc) throws Exception {
		return this.vehAlarmDao.getAlarmNoCancelSuspinfoCount(begin, end, glbm, yhmc);
	}
	/**
	 * 查询已报警布控数量
	 * @param begin
	 * @param end
	 * @param yhdh
	 * @return
	 * @throws Exception
	 */
	public int getAlarmHasBeenSuspinfoCount(String begin, String end,
			String yhdh) throws Exception {
		return 0;
	}
	/**
	 * （根据反馈编号）查询veh_alarm_handled表信息
	 * @param fkbh 反馈编号
	 * @return
	 * @throws Exception
	 */
	public VehAlarmHandled getAlarmHandleForFkbh(String fkbh) throws Exception {
		
		return vehAlarmDao.getAlarmHandleForFkbh(fkbh);
	}
	

}
