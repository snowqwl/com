package com.sunshine.monitor.comm.video.dao.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.video.dao.VideoDao;
@Repository
public class VideoDaoImpl extends BaseDaoImpl implements VideoDao {
	
	/*
	 * 取摄像机部门结构树
	 */
	public List<Map<String,Object>> getCameraTree()throws Exception{
		StringBuffer sql = new StringBuffer("select xzqhdm as id,xzqhmc as idname,nvl(pid,0) as pid from frm_xzqh where 1=1 and xzqhdm <> '430000870000'  ");		
		sql.append(" order by xzqhqc ");
		return this.jdbcTemplate.queryForList(sql.toString()); 
	}
	
	/*
	 * 取摄像机部门异步结构树
	 */
	public List<Map<String,Object>> getCameraTreeAsync(String id)throws Exception{
		String sql = "";
		if("0".equals(id)){
			sql = " select xzqhdm as id,xzqhmc as idname,nvl(pid,0) as pid from frm_xzqh where 1=1 and xzqhdm <> '430000870000' and jb in ('2','3') ";
			return this.jdbcTemplate.queryForList(sql);
		}else{
			sql = " select rescode as id,resname as idname,substr(rescode,0,4)||'00' as pid from " +
					"JM_CAMERA where rescode like ?";

			return this.jdbcTemplate.queryForList(sql,new String[]{"%"+id.substring(0,4)+"%"});
		}

	}
	
	/*
	 * 判断节点是否为父节点
	 */
	public boolean getCountFromCamera(String id)throws Exception{
		String sql = " select count(1) from jm_camera where rescode like ?";
		int i = this.jdbcTemplate.queryForInt(sql,new String[]{"%"+id.substring(0,4)+"%"});
		if(i>0){
			return true;
		}else{
			return false;
		}
	}
}
