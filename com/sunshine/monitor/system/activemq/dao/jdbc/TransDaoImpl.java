package com.sunshine.monitor.system.activemq.dao.jdbc;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.dao.PicDao;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.activemq.bean.TransAlarm;
import com.sunshine.monitor.system.activemq.bean.TransAlarmHandled;
import com.sunshine.monitor.system.activemq.bean.TransAuditApprove;
import com.sunshine.monitor.system.activemq.bean.TransCmd;
import com.sunshine.monitor.system.activemq.bean.TransLivetrace;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.activemq.bean.TransSusp;
import com.sunshine.monitor.system.activemq.dao.TransDao;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;

@Repository("transDao")
public class TransDaoImpl extends BaseDaoImpl implements TransDao {
	
	@Autowired
	private PicDao picDao;
	
	public List getTransList(String tablename, String type, String maxrow) {
		/**String sql = "select CSXH, CSDW, JSDW, CSBJ, CSSJ, YWXH, TYPE, DDBJ, DDSJ from "
				+ tablename + " where DDBJ=0 and type='" + type + "' and rownum<" + maxrow;*/
		String sql = "select CSXH, CSDW, JSDW, CSBJ, CSSJ, YWXH, TYPE, DDBJ, DDSJ from "
				+ tablename + " where DDBJ=0 and type=? and rownum<?";
		List list = null;
		//list = this.queryForList(sql, TransObj.class);
		list = this.queryForList(sql, new Object[]{type,maxrow}, TransObj.class);
		return list;
	}
	
	@Transactional(readOnly=true)
	public TransAuditApprove getTransSuspApproval(String ywxh, String type)
			throws Exception {
		StringBuffer sqlbuf = new StringBuffer(20);
		sqlbuf.append("SELECT XH,BKXH,CZR,CZRJH,CZRMC,CZRDWMC,CZRDW,CZSJ,CZJG,MS,BZW,BY1,BY2 FROM AUDIT_APPROVE WHERE BKXH=? AND BZW =?");
		List<TransAuditApprove> list = this.queryForList(sqlbuf.toString(),
				new Object[]{ywxh, type}, TransAuditApprove.class);
		if(list != null && list.size()>0)
			return list.get(0);
		return null;
	}

	public TransSusp getTransSuspDetail(String ywxh, int type)throws Exception {
		StringBuffer sqlbuf = new StringBuffer("SELECT * FROM VEH_SUSPINFO WHERE BKXH = ?");
		TransSusp bean = null;
		List<TransSusp> list = this.queryForList(sqlbuf.toString(), new Object[]{ywxh}, TransSusp.class);
		if(list != null && list.size() > 0){
			bean = list.get(0);
		}
		if(bean == null)
			return bean;
		//布控如果有图片，则设置图片信息
		byte[] picByte = (byte[]) null;
		picByte = picDao.getPic2(ywxh);
		if(picByte != null){
			bean.setPic(picByte);
		}
		sqlbuf.setLength(0);
		sqlbuf.append("select zjlx,bkxh from susp_picrec where bkxh=?");
		List<VehSuspinfopic> vPic = this.queryForList(sqlbuf.toString(),
				new Object[]{ywxh}, VehSuspinfopic.class);
		if(vPic != null && vPic.size() > 0){
			VehSuspinfopic vehSuspic = vPic.get(0);
			//bean.setPic(vehSuspic.getZjwj());  //这样取值有问题
			bean.setPicname(vehSuspic.getZjlx());
		}
		
		//保存布控附件信息
		savefj(ywxh,bean);
		
		//设置审核审批信息
		sqlbuf.setLength(0);
		String bzw = "";
		if (11 == type)
			bzw = "'1','2'";
		else if (13 == type)
			bzw = "'3','4'";
		else {
			bzw = "'1','2','3','4'";
		}

		sqlbuf.append("select * from AUDIT_APPROVE where bkxh='").append(ywxh)
				.append("' and bzw in (").append(bzw).append(")");
		//List list1 = this.jdbcTemplate.queryForList(sqlbuf.toString(),TransAuditApprove.class);
		List list1 = this.queryForList(sqlbuf.toString(),TransAuditApprove.class);
		if(list1.size() > 0 )
			bean.setAuditList(list1);
		return bean;
	}
	
	private void savefj(String ywxh,TransSusp bean) throws SQLException, IOException {
		//布控如果有车辆布控申请表图片，则设置车辆布控申请表图片信息
		byte[] picByte = (byte[]) null;
		picByte = picDao.getClbksqb(ywxh);
		if(picByte != null){
			bean.setClbksqb(picByte);
		}
		StringBuffer sqlbuf = new StringBuffer("select clbksqblj,bkxh from susp_picrec where bkxh=?");
		List<VehSuspinfopic> vPic = this.queryForList(sqlbuf.toString(), new Object[]{ywxh},VehSuspinfopic.class);
		if(vPic != null && vPic.size() > 0){	
			VehSuspinfopic vehSuspic = vPic.get(0);
			//bean.setPic(vehSuspic.getZjwj());  //这样取值有问题
			bean.setClbksqblj(vehSuspic.getClbksqblj());
		}
		//布控如果有立案决定书表图片，则设置立案决定书图片信息
		byte[] picByte2 = (byte[]) null;
		picByte2 = picDao.getLajds(ywxh);
		if(picByte != null){
			bean.setClbksqb(picByte2);
		}
		StringBuffer sqlbuf2 = new StringBuffer("select lajdslj,bkxh from susp_picrec where bkxh=?");
		List<VehSuspinfopic> vPic2 = this.queryForList(sqlbuf2.toString(), new Object[]{ywxh},VehSuspinfopic.class);
		if(vPic2 != null && vPic2.size() > 0){	
			VehSuspinfopic vehSuspic = vPic2.get(0);
			//bean.setPic(vehSuspic.getZjwj());  //这样取值有问题
			bean.setLajdslj(vehSuspic.getLajdslj());
		}
		//布控如果有移交承诺书表图片，则设置移交承诺书图片信息
		byte[] picByte3 = (byte[]) null;
		picByte3 = picDao.getLajds(ywxh);
		if(picByte != null){
			bean.setYjcns(picByte3);
		}
		StringBuffer sqlbuf3 = new StringBuffer("select yjcnslj,bkxh from susp_picrec where bkxh=?");
		List<VehSuspinfopic> vPic3 = this.queryForList(sqlbuf3.toString(), new Object[]{ywxh},VehSuspinfopic.class);
		if(vPic3 != null && vPic3.size() > 0){	
			VehSuspinfopic vehSuspic = vPic3.get(0);
			//bean.setPic(vehSuspic.getZjwj());  //这样取值有问题
			bean.setYjcnslj(vehSuspic.getYjcnslj());
		}
		
	}

	public TransAlarm getTransAlarmDetail(String ywxh)throws Exception {
		Field[] field = TransAlarm.class.getDeclaredFields();
		StringBuffer sqlbuf = new StringBuffer("SELECT ");
		boolean isFirst = true;
		for (int i = 1; i < field.length; i++) {
			if (field[i].getModifiers() == 2) {
				if (isFirst) {
					sqlbuf.append(field[i].getName());
					isFirst = false;
				} else {
					sqlbuf.append(",").append(field[i].getName());
				}
			}
		}
		sqlbuf.append(" FROM VEH_ALARMREC WHERE BJXH = ?");
		List<TransAlarm> list = this.queryForList(sqlbuf.toString(),
				new Object[]{ywxh}, TransAlarm.class);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public TransAlarmHandled getTransAlarmHandledDetail(String ywxh) throws Exception {
		Field[] field = TransAlarmHandled.class.getDeclaredFields();
		StringBuffer sqlbuf = new StringBuffer("select ");
		boolean isFirst = true;
		for (int i = 0; i < field.length; i++) {
			if (field[i].getModifiers() == 2) {
				if (isFirst) {
					sqlbuf.append(field[i].getName());
					isFirst = false;
				} else {
					sqlbuf.append(",").append(field[i].getName());
				}
			}
		}
		sqlbuf.append(" FROM VEH_ALARM_HANDLED WHERE FKBH=?");
		List<TransAlarmHandled> list = this.queryForList(sqlbuf.toString(),
				new Object[]{ywxh}, TransAlarmHandled.class);
		TransAlarmHandled bean = null;
		if(list !=null && list.size() > 0){
			bean = list.get(0);
			List<TransCmd> cmdList = this.queryForList(
					"select * from veh_alarm_cmd where bjxh=?",
					new Object[]{bean.getBjxh()}, TransCmd.class);
			bean.setCmdList(cmdList);
			List<TransLivetrace> livetraceList = this.queryForList(
					"select * from veh_alarm_livetrace where bjxh=?",
					new Object[]{bean.getBjxh()}, TransLivetrace.class);
			bean.setLivetraceList(livetraceList);
		}
		return bean;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public int updateTransStatus(String tableName, String csxh) throws Exception {
		String sql = "update "+tableName+" set ddbj=1, ddsj=sysdate where csxh=?";
		return this.jdbcTemplate.update(sql,new Object[]{csxh});
	}
	
}