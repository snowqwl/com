package com.sunshine.monitor.comm.dao.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.PicDao;
import com.sunshine.monitor.system.alarm.bean.VehAlarmHandled;

@Repository("picDao")
public class PicDaoImpl extends BaseDaoImpl implements PicDao{

	
	/*public byte[] getPic(String xh) throws Exception {
		
		Object object = queryDetail("SUSP_PICREC", "BKXH", xh, VehSuspinfopic.class);
		byte[] bytes = (byte[]) null;
		if (object != null) {
			VehSuspinfopic sp = (VehSuspinfopic) object;
			Blob blob = sp.getZjwj();
			if (blob != null) {
				bytes = new byte[(int) blob.length()];
				InputStream pi = blob.getBinaryStream();
				int size = pi.read(bytes);
				if (size == -1) {
					bytes = (byte[]) null;
				}
				pi.close();
			}
		}
		return bytes;
	}*/

	public byte[] getPic2(String xh) throws SQLException, IOException {
		Connection conn = null;
		DataSource ds = this.jdbcTemplate.getDataSource();
		conn = DataSourceUtils.getConnection(ds);
		PreparedStatement ps2 = conn
				.prepareStatement("select zjwj from SUSP_PICREC where BKXH=?");
		ps2.setString(1, xh);
		ResultSet rs = ps2.executeQuery();
		byte[] bytes = (byte[]) null;
		if (rs.next()) {
			Blob blob = rs.getBlob(1);
			if (blob != null) {
				bytes = new byte[(int) blob.length()];
				InputStream pi = blob.getBinaryStream();
				int size = pi.read(bytes);
				if (size == -1) {
					bytes = (byte[]) null;
				}
				pi.close();
			}
		}
		DataSourceUtils.releaseConnection(conn, ds);
		return bytes;
	}

	public byte[] getLjPicForBkxh(String xh) throws SQLException, IOException {
		List<VehAlarmHandled> list = this.queryForList("select *  from veh_alarm_handled  where  sflj = '1'  and  bkxh = '"+xh+"'", VehAlarmHandled.class);
		byte[] bytes = (byte[]) null;
		
		Connection conn = null;
		DataSource ds = this.jdbcTemplate.getDataSource();
		conn = DataSourceUtils.getConnection(ds);
		PreparedStatement ps2 = conn
				.prepareStatement("select LJTP  from veh_alarm_handled_picrec  where fkbh = ?");
		
		for(VehAlarmHandled handle : list){
			ps2.setString(1, handle.getFkbh());
			ResultSet rs = ps2.executeQuery();
			if (rs.next()) {
				Blob blob = rs.getBlob(1);
				if (blob != null) {
					bytes = new byte[(int) blob.length()];
					InputStream pi = blob.getBinaryStream();
					int size = pi.read(bytes);
					if (size == -1) {
						bytes = (byte[]) null;
					}
					pi.close();
					
					if (size != -1) {
						break;
					}
				}
			}	
		}	
		DataSourceUtils.releaseConnection(conn, ds);
		
		return bytes;
	}

	public byte[] getKktpForKdbh(String xh) throws SQLException, IOException {
		Connection conn = null;
		DataSource ds = this.jdbcTemplate.getDataSource();
		conn = DataSourceUtils.getConnection(ds);
		PreparedStatement ps2 = conn.prepareStatement("SELECT KKTP FROM CODE_GATE_PIC WHERE KDBH=?");
		ps2.setString(1, xh);
		ResultSet rs = ps2.executeQuery();
		byte[] bytes = (byte[]) null;
		if (rs.next()) {
			Blob blob = rs.getBlob(1);
			if (blob != null) {
				bytes = new byte[(int) blob.length()];
				InputStream pi = blob.getBinaryStream();
				int size = pi.read(bytes);
				if (size == -1) {
					bytes = (byte[]) null;
				}
				pi.close();
			}
		}
		DataSourceUtils.releaseConnection(conn, ds);
		return bytes;
	}
	
	

	/**
	 * @author yudam
	 * @return Boolean
	 * @param String bkxh
	 * @since 2014-10-30
	 * 
	 * */
	//public XH BKXH ZJWJ ZJLX
	public Boolean existPic(String bkxh){
		Connection conn = null;
		String ZJLX="";
		String clbksqbName =null;
		String lajdsName = null;
		String yjcnsName = null;
		DataSource ds = this.jdbcTemplate.getDataSource();
		conn = DataSourceUtils.getConnection(ds);
		PreparedStatement ps2;
		try {
			ps2 = conn.prepareStatement("select zjlx,clbksqblj,lajdslj,yjcnslj from SUSP_PICREC where BKXH=?");
		
		ps2.setString(1, bkxh);
		ResultSet rs = ps2.executeQuery();
		if (rs.next()) {
			clbksqbName = rs.getString("clbksqblj");
			lajdsName = rs.getString("lajdslj");
			yjcnsName = rs.getString("yjcnslj");
			ZJLX = rs.getString(1);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DataSourceUtils.releaseConnection(conn, ds); 
		if((ZJLX!=null&&ZJLX!="") || clbksqbName!=null || lajdsName!=null || yjcnsName!=null){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 布控申请附件
	 */
	public byte[] getClbksqb(String xh) throws SQLException, IOException {
		Connection conn = null;
		DataSource ds = this.jdbcTemplate.getDataSource();
		conn = DataSourceUtils.getConnection(ds);
		PreparedStatement ps2 = conn
				.prepareStatement("select clbksqb from SUSP_PICREC where BKXH=?");
		ps2.setString(1, xh);
		ResultSet rs = ps2.executeQuery();
		byte[] bytes = (byte[]) null;
		if (rs.next()) {
			Blob blob = rs.getBlob(1);
			if (blob != null) {
				bytes = new byte[(int) blob.length()];
				InputStream pi = blob.getBinaryStream();
				int size = pi.read(bytes);
				if (size == -1) {
					bytes = (byte[]) null;
				}
				pi.close();
			}
		}
		DataSourceUtils.releaseConnection(conn, ds);
		return bytes;
	}
	public byte[] getLajds(String xh) throws SQLException, IOException {
		Connection conn = null;
		DataSource ds = this.jdbcTemplate.getDataSource();
		conn = DataSourceUtils.getConnection(ds);
		PreparedStatement ps2 = conn
				.prepareStatement("select lajds from SUSP_PICREC where BKXH=?");
		ps2.setString(1, xh);
		ResultSet rs = ps2.executeQuery();
		byte[] bytes = (byte[]) null;
		if (rs.next()) {
			Blob blob = rs.getBlob(1);
			if (blob != null) {
				bytes = new byte[(int) blob.length()];
				InputStream pi = blob.getBinaryStream();
				int size = pi.read(bytes);
				if (size == -1) {
					bytes = (byte[]) null;
				}
				pi.close();
			}
		}
		DataSourceUtils.releaseConnection(conn, ds);
		return bytes;
	}
	public byte[] getYjcns(String xh) throws SQLException, IOException {
		Connection conn = null;
		DataSource ds = this.jdbcTemplate.getDataSource();
		conn = DataSourceUtils.getConnection(ds);
		PreparedStatement ps2 = conn
				.prepareStatement("select yjcns from SUSP_PICREC where BKXH=?");
		ps2.setString(1, xh);
		ResultSet rs = ps2.executeQuery();
		byte[] bytes = (byte[]) null;
		if (rs.next()) {
			Blob blob = rs.getBlob(1);
			if (blob != null) {
				bytes = new byte[(int) blob.length()];
				InputStream pi = blob.getBinaryStream();
				int size = pi.read(bytes);
				if (size == -1) {
					bytes = (byte[]) null;
				}
				pi.close();
			}
		}
		DataSourceUtils.releaseConnection(conn, ds);
		return bytes;
	}
}
