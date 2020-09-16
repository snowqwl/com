package com.sunshine.monitor.system.gate.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.gate.bean.CodeDevice;
import com.sunshine.monitor.system.gate.dao.DeviceDao;

@Repository("deviceDao")
public class DeviceDaoImpl extends BaseDaoImpl implements DeviceDao {

	public CodeDevice getDevice(String sbbh) throws Exception {
		CodeDevice codeDevice = null;
		String sql = "select * from code_device where sbbh= ?";
		// + "' and qyrq<=sysdate and tyrq>sysdate and bfrq>sysdate";
		List list = this.queryForList(sql, new Object[] { sbbh }, CodeDevice.class);
		if (list != null && list.size() > 0) {
			codeDevice = (CodeDevice) list.get(0);
		}
		return codeDevice;
	}

	public Map<String, Object> getDevices(Map filter, CodeDevice d, String subsql) throws Exception {
		String tmpSql = "";
		if ((d.getSbbh() != null) && (d.getSbbh().length() > 0)) {
			tmpSql = tmpSql + " and sbbh='" + d.getSbbh() + "'";
		}
		if ((d.getSbmc() != null) && (d.getSbmc().length() > 0)) {
			tmpSql = tmpSql + " and sbmc like '%" + d.getSbmc() + "%'" + tmpSql;
		}
		if ((d.getDwdm() != null) && (d.getDwdm().length() > 0)) {
			tmpSql = tmpSql + " and dwdm='" + d.getDwdm() + "'" + tmpSql;
		}
		if ((d.getSblx() != null) && (d.getSblx().length() > 0)) {
			tmpSql = tmpSql + " and sblx='" + d.getSblx() + "'" + tmpSql;
		}
		if ((d.getSbip() != null) && (d.getSbip().length() > 0)) {
			tmpSql = tmpSql + " and sbip='" + d.getSbip() + "'" + tmpSql;
		}
		if ((d.getKdbh() != null) && (d.getKdbh().length() > 0)) {
			tmpSql = tmpSql + " and kdbh='" + d.getKdbh() + "'" + tmpSql;
		}
		if ((subsql != null) && (subsql.length() > 1)) {
			tmpSql = tmpSql + " and dwdm in (" + subsql + ")";
		}
		if (tmpSql.length() > 1)
			tmpSql = " where " + tmpSql.substring(5, tmpSql.length()) + " ";
		tmpSql = "select * from code_device " + tmpSql + " order by gxsj desc";
		Map map = this.findPageForMap(tmpSql, Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
		return map;
	}

	public List getAllDevices() throws Exception {
		List list = null;
		String sql = "select * from code_device order by kdbh,sbbh";
		list = this.queryList(sql, CodeDevice.class);
		return list;
	}

	/**
	 * 获取设备信息
	 *
	 */

	public List getDevices() throws Exception {
		List list = null;
		String sql = "Select * from code_device order by kdbh,sbbh";
		list = this.queryForList(sql, CodeDevice.class);
		return list;
	}

	public List<CodeDevice> getDevicesByKdbh(String kdbh) throws Exception {
		String sql = "select * from code_device where kdbh=? order by kdbh,sbbh";
		return this.queryForList(sql, new Object[] { kdbh }, CodeDevice.class);
	}

	// 已经不使用
	@Deprecated
	public String getDeviceName(String sbbh)
			throws Exception {/*
								 * String sql =
								 * "select * from code_device where sbbh=? and qyrq<=sysdate and tyrq>sysdate and bfrq>sysdate and sbzt=0"
								 * ; List<CodeDevice> list =
								 * this.queryForList(sql,new Object[]{sbbh},
								 * CodeDevice.class); if(list.size() == 0){
								 * return sbbh ; } CodeDevice d = list.get(0);
								 * if ((d == null) || (d.getSbmc() == null)) {
								 * return sbbh; } return d.getSbmc();
								 */
		return "";
	}

	/**
	 * 旧版设备编号
	 * 
	 * @param sbbh
	 * @return
	 * @throws Exception
	 */
	public String getOldDeviceName(String sbbh) throws Exception {
		String sql = "select * from dc_code_device where sbbh=? and qyrq<=sysdate and tyrq>sysdate and bfrq>sysdate and sbzt=0";
		List<CodeDevice> list = this.queryForList(sql, new Object[] { sbbh }, CodeDevice.class);
		if (list.size() == 0) {
			return sbbh;
		}
		CodeDevice d = list.get(0);
		if ((d == null) || (d.getSbmc() == null)) {
			return sbbh;
		}
		return d.getSbmc();
	}

	public Map<String, Object> getDevices(Map filter, CodeDevice d) throws Exception {
		String tmpSql = "";
		if (d.getSbbh() != null && d.getSbbh().length() > 1) {
			tmpSql = tmpSql + " and sbbh = '" + d.getSbbh() + "'";
		}
		if (d.getSbmc() != null && d.getDwdm().length() > 1) {
			tmpSql = tmpSql + " and sbmc like '%" + d.getSbmc() + "%'";
		}
		if (d.getDwdm() != null && d.getDwdm().length() > 1) {
			tmpSql = tmpSql + " and dwdm like '" + d.getDwdm() + "%'";
		}
		if (d.getDwdmmc() != null && d.getDwdmmc().length() > 1) {
			tmpSql = tmpSql + " and dwdmmc like '" + d.getDwdmmc() + "%'";
		}
		if (tmpSql.length() > 1) {
			tmpSql = "Where " + tmpSql.substring(5, tmpSql.length()) + " ";
		}
		tmpSql = "Select * from code_device " + tmpSql + " order by sbmc";
		Map<String, Object> queryMap = this.findPageForMap(tmpSql, Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}

	public int removeDevice(CodeDevice codeDevice) throws Exception {
		String tmpSql = "Delete from code_device Where sbbh=?";
		return this.jdbcTemplate.update(tmpSql, new Object[] { codeDevice.getSbbh() });
	}

	public int saveDevice(CodeDevice codeDevice) throws Exception {
		List vl = new ArrayList();
		String sql = "Select count(*) from code_device Where sbbh=?";
		String tmpSql = null;
		int count = this.jdbcTemplate.queryForInt(sql, new Object[] { codeDevice.getSbbh() });
		if (count > 0) {
			vl.add(codeDevice.getSbmc());
			vl.add(codeDevice.getSblx());
			vl.add(codeDevice.getDwdm());
			vl.add(codeDevice.getSbip());
			vl.add(codeDevice.getFtpdk());
			vl.add(codeDevice.getFtpyh());
			vl.add(codeDevice.getFtpkl());
			vl.add(codeDevice.getWebdk());
			vl.add(codeDevice.getSbxh());
			vl.add(codeDevice.getZms());
			vl.add(codeDevice.getDdsm());
			vl.add(codeDevice.getDcxs());
			vl.add(codeDevice.getXcxs());
			vl.add(codeDevice.getZdxs());
			vl.add(codeDevice.getSbzt());
			vl.add(codeDevice.getQyrq());
			vl.add(codeDevice.getBfrq());
			vl.add(codeDevice.getTyrq());
			vl.add(codeDevice.getKdbh());
			vl.add(codeDevice.getZszt());
			vl.add(codeDevice.getLjzt());
			vl.add(codeDevice.getZrdw());
			vl.add(codeDevice.getLjdw());
			vl.add(codeDevice.getBz());
			vl.add(codeDevice.getSbbh());

			tmpSql = "Update code_Device set sbmc=?,sblx=?,dwdm=?,sbip=?,"
					+ "ftpdk=?,ftpyh=?,ftpkl=?,webdk=?,sbxh=?,zms=?,ddsm=?," + "dcxs=?,xcxs=?,zdxs?,sbzt=?,"
					+ "qyrq=to_date(?,'yyyy-MM-dd HH24:mi:ss')," + "bfrq=to_date(?,'yyyy-MM-dd HH24:mi:ss'),"
					+ "tyrq=to_date(?,'yyyy-MM-dd HH24:mi:ss'),kdbh=?,zszt=?,ljzt=?,zrdw=?,"
					+ "ljdw=?,gxsj=sysdate,bz=? Where sbbh=?";
		} else {
			vl.add(codeDevice.getSbbh());
			vl.add(codeDevice.getSbmc());
			vl.add(codeDevice.getSblx());
			vl.add(codeDevice.getDwdm());
			vl.add(codeDevice.getSbip());
			vl.add(codeDevice.getFtpdk());
			vl.add(codeDevice.getFtpyh());
			vl.add(codeDevice.getFtpkl());
			vl.add(codeDevice.getWebdk());
			vl.add(codeDevice.getSbxh());
			vl.add(codeDevice.getZms());
			vl.add(codeDevice.getDdsm());
			vl.add(codeDevice.getDcxs());
			vl.add(codeDevice.getXcxs());
			vl.add(codeDevice.getZdxs());
			vl.add(codeDevice.getSbzt());
			vl.add(codeDevice.getQyrq());
			vl.add(codeDevice.getBfrq());
			vl.add(codeDevice.getTyrq());
			vl.add(codeDevice.getKdbh());
			vl.add(codeDevice.getZszt());
			vl.add(codeDevice.getLjzt());
			vl.add(codeDevice.getZrdw());
			vl.add(codeDevice.getLjdw());
			vl.add(codeDevice.getBz());
			tmpSql = "Insert into Code_Device(sbbh,sbmc,sblx,dwdm,sbip,ftpdk,ftpyh,ftpkl,webdk,sbxh,zms,ddsm,dcxs,xcxs,zdxs,sbzt,qyrq,bfrq,tyrq,kdbh,zszt,ljzt,"
					+ "zrdw,ljdw,gxsj,bz) values(" + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "to_date(?,'yyyy-MM-dd HH24:mi:ss')," + "to_date(?,'yyyy-MM-dd HH24:mi:ss'),"
					+ "to_date(?,'yyyy-MM-dd HH24:mi:ss')," + "?,?,?,?,?,sysdate,?" + ")";
		}
		return this.jdbcTemplate.update(tmpSql, vl.toArray());
	}

}
