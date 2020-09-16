package com.sunshine.monitor.comm.util.extract;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sunshine.monitor.comm.bean.Policess;

public class DataTransfer {
	@Autowired
	@Qualifier("jdbcTemplate")
	public JdbcTemplate jdbcTemplate;
	private static  PropertiesConfiguration config;
	private Logger log = Logger.getLogger(DataTransfer.class);
	//抽取对方数据库
	 public List<Policess> DataExtract(List lists) throws ClassNotFoundException, SQLException {  
		 List<Policess> list=new ArrayList<Policess>();
		 String policeurl=null;
		 String password=null;
		 String username=null;
		 String tableName=null;
		 String time=null;
		 try {
					config = new PropertiesConfiguration("ipport.properties");
					policeurl= config.getString("policeurl");
					username=config.getString("username");
					password=config.getString("password");
					tableName=config.getString("tableName");
				} catch (ConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection(policeurl,username,password);
			Statement stmt = conn.createStatement();
			StringBuffer sqlsz = new StringBuffer("");
			sqlsz.append("select * from  ");
			sqlsz.append(tableName);
			if(lists.size()!=0){
				sqlsz.append(" "+" where LASTUPDATETIME >  to_date(");
				for (int i = 0; i < lists.size(); i++) {
					time=lists.get(i).toString().substring(0,19);
					sqlsz.append("'"+time+"'");
					if(i< lists.size()-1){
						sqlsz.append(",");
					}
				}	
				sqlsz.append(",'YYYY/MM/DD HH24:MI:SS')");
			}
		ResultSet rs = stmt.executeQuery(sqlsz.toString());
		while (rs.next()) {
			Policess po=new  Policess();
			po.setID(rs.getString("ID"));
			po.setUSER_ID(rs.getLong("USER_ID"));
			po.setUSER_NAME(rs.getString("USER_NAME"));
			po.setUSER_REALNAME(rs.getString("USER_REALNAME"));
			po.setUSER_SEX(rs.getString("USER_SEX"));
			po.setUSER_WORKTEL(rs.getString("USER_WORKTEL"));
			po.setUSER_MOBILETEL1(rs.getString("USER_MOBILETEL1"));
			po.setUSER_IDCARD(rs.getString("USER_IDCARD"));
			po.setUSER_REGDATE(rs.getString("USER_REGDATE"));
			po.setOFFICALRANK(rs.getString("OFFICALRANK"));
			po.setPOST(rs.getString("POST"));
			po.setSTATUS(rs.getString("STATUS"));
			po.setCANCELTIME(rs.getString("CANCELTIME"));
			po.setLASTUPDATETIME(rs.getString("LASTUPDATETIME"));
			po.setORGNAME(rs.getString("ORGNAME"));
			po.setORGCODE(rs.getString("ORGCODE"));
			po.setLASTUPDATACOLUMN(rs.getString("LASTUPDATACOLUMN"));
			po.setGXSJ(rs.getString("GXSJ"));
			po.setJLRKSJ(rs.getString("JLRKSJ"));
			po.setRHCKSJ(rs.getString("RHCKSJ"));
			po.setRZHKSJ(rs.getString("RZHKSJ"));
			po.setV_CANCELTIME(rs.getString("V_CANCELTIME"));
			po.setV_JLRKSJ(rs.getString("V_JLRKSJ"));
			po.setV_LASTUPDATETIME(rs.getString("V_LASTUPDATETIME"));
			po.setV_USER_REGDATE(rs.getString("V_USER_REGDATE"));
			list.add(po);
		}
		if (rs != null) {
		rs.close();
		}
		if (stmt != null) {
		stmt.close();
		}
		if (conn != null) {
		conn.close();
		}
		return list;
	}

 	//将数据存入我放数据库
	 public int DataStorage(List<Policess> list) throws ClassNotFoundException, SQLException {
				 String ygneturl=null;
				 String	 ygnetname=null;
				 String ygnetpassword=null;
				try {
					config = new PropertiesConfiguration("ipport.properties");
					ygneturl= config.getString("ygneturl");
					ygnetname=config.getString("ygnetname");
					ygnetpassword=config.getString("ygnetpassword");
				} catch (ConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 
		 	Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = DriverManager.getConnection(ygneturl,ygnetname,ygnetpassword);
			Statement stmt = conn.createStatement();
			PreparedStatement pstmt=null;
			// 更新用户表
		 	int count=0; 
		 	String id=null;
			StringBuffer sbInsert = new StringBuffer("");
			for (int i = 0; i < list.size(); i++) {
				sbInsert.setLength(0);
				Policess  pol=new Policess();
				pol=list.get(i);
				if(pol.getUSER_REGDATE() != null){
					pol.setUSER_REGDATE("'"+pol.getUSER_REGDATE().substring(0,19)+"'");
				}
				if(pol.getCANCELTIME() != null){
					pol.setCANCELTIME("'"+pol.getCANCELTIME().substring(0,19)+"'");
				}
				if(pol.getLASTUPDATETIME() != null){
					pol.setLASTUPDATETIME("'"+pol.getLASTUPDATETIME().substring(0,19)+"'");
				}
				if(pol.getJLRKSJ() != null){
					pol.setJLRKSJ("'"+pol.getJLRKSJ().substring(0,19)+"'");
				}
				if(pol.getRHCKSJ() != null){
					pol.setRHCKSJ("'"+pol.getRHCKSJ().substring(0,19)+"'");
				}
				if(pol.getRZHKSJ() != null){
					pol.setRZHKSJ("'"+pol.getRZHKSJ().substring(0,19)+"'");
				}
				/*
				 * 判断是否修改
				 * */
				ResultSet rs = stmt.executeQuery("select id from qbzx_user where id ='"+pol.getID()+"'");
				while (rs.next()) {
					 id=rs.getString("id");
				}
				if(id!=null){
					sbInsert.append("update qbzx_user set USER_ID ='"+pol.getID()+"',")
					.append("USER_NAME='"+pol.getUSER_NAME()+"',")
					.append(" USER_REALNAME='"+pol.getUSER_REALNAME()+"',")
					.append(" USER_SEX ='"+pol.getUSER_SEX()+"',")
					.append(" USER_WORKTEL='"+pol.getUSER_WORKTEL()+"',")
					.append(" USER_MOBILETEL1='"+pol.getUSER_MOBILETEL1()+"',")
					.append(" USER_IDCARD='"+pol.getUSER_IDCARD()+"',")
					.append(" USER_REGDATE=to_date("+pol.getUSER_REGDATE()+",'yyyy-mm-dd hh24:mi:ss'),")
					.append(" OFFICALRANK='"+pol.getOFFICALRANK()+"',")
					.append(" POST='"+pol.getPOST()+"',")
					.append(" STATUS='"+pol.getSTATUS()+"',")
					.append(" CANCELTIME=to_date("+pol.getCANCELTIME()+",'yyyy-mm-dd hh24:mi:ss'),")
					.append("LASTUPDATETIME=to_date("+pol.getLASTUPDATETIME()+",'yyyy-mm-dd hh24:mi:ss'),")
					.append("  ORGNAME ='"+pol.getORGNAME()+"',")
					.append(" ORGCODE='"+pol.getORGCODE()+"',")
					.append(" LASTUPDATACOLUMN='"+pol.getLASTUPDATACOLUMN()+"',")
					.append(" GXSJ='"+pol.getGXSJ()+"',")
					.append(" JLRKSJ=to_date("+pol.getJLRKSJ()+",'yyyy-mm-dd hh24:mi:ss'),")
					.append(" RHCKSJ=to_date("+pol.getRHCKSJ()+",'yyyy-mm-dd hh24:mi:ss'),")
					.append(" RZHKSJ=to_date("+pol.getRZHKSJ()+",'yyyy-mm-dd hh24:mi:ss'),")
					.append(" V_CANCELTIME='"+pol.getV_CANCELTIME()+"',")
					.append(" V_JLRKSJ='"+pol.getV_JLRKSJ()+"',")
					.append(" V_LASTUPDATETIME='"+pol.getV_LASTUPDATETIME()+"',")
					.append(" V_USER_REGDATE='"+pol.getV_USER_REGDATE()+"'")
					.append("where id ='"+pol.getID()+"'");					
				}else{
					sbInsert.append(
					"insert into qbzx_user(ID,USER_ID,user_name,user_realname,user_sex,user_worktel,user_mobiletel1,user_idcard,user_regdate,officalrank,post,status,canceltime,lastupdatetime,orgname,orgcode,lastupdatacolumn,gxsj,jlrksj,rhcksj,rzhksj,v_canceltime,v_jlrksj,v_lastupdatetime,v_user_regdate)values('")
					.append(pol.getID()).append("',")
					.append(pol.getUSER_ID()).append(",'")
					.append(pol.getUSER_NAME()).append("','")
					.append(pol.getUSER_REALNAME()).append("','")
					.append(pol.getUSER_SEX()).append("','")
					.append(pol.getUSER_WORKTEL()).append("','")
					.append(pol.getUSER_MOBILETEL1()).append("','")
					.append(pol.getUSER_IDCARD().toString()).append("',to_date(")
					.append(pol.getUSER_REGDATE()).append(",'yyyy-mm-dd hh24:mi:ss'),'")
					.append(pol.getOFFICALRANK()).append("','")
					.append(pol.getPOST()).append("','")
					.append(pol.getSTATUS()).append("',to_date(")
					.append(pol.getCANCELTIME()).append(",'yyyy-mm-dd hh24:mi:ss'),to_date(")
					.append(pol.getLASTUPDATETIME()).append(",'yyyy-mm-dd hh24:mi:ss'),'")
					.append(pol.getORGNAME()).append("','")
					.append(pol.getORGCODE()).append("','")
					.append(pol.getLASTUPDATACOLUMN()).append("','")
					.append(pol.getGXSJ()).append("',to_date(")
					.append(pol.getJLRKSJ()).append(",'yyyy-mm-dd hh24:mi:ss'),to_date(")
					.append(pol.getRHCKSJ()).append(",'yyyy-mm-dd hh24:mi:ss'),to_date(")
					.append(pol.getRZHKSJ()).append(",'yyyy-mm-dd hh24:mi:ss'),'")
					.append(pol.getV_CANCELTIME()).append("','")
					.append(pol.getV_JLRKSJ()).append("','")
					.append(pol.getV_LASTUPDATETIME()).append("','")
					.append(pol.getV_USER_REGDATE()).append("')");
				}
				try{
					pstmt = (PreparedStatement) conn.prepareStatement(sbInsert.toString());
					 count =pstmt.executeUpdate();
					}catch(SQLException e){
						log.info(e);
					}
			}

			if(stmt !=null){
				stmt.close();
			}
			if (pstmt != null) {
				pstmt.close();
				}
				if (conn != null) {
				conn.close();
				}
				list.removeAll(list);
			return count;
	 }
	 public List selctID() throws ClassNotFoundException, SQLException {
		 List list=new ArrayList();
		 String ygneturl=null;
		 String	 ygnetname=null;
		 String ygnetpassword=null;
				try {
					config = new PropertiesConfiguration("ipport.properties");
					ygneturl= config.getString("ygneturl");
					ygnetname=config.getString("ygnetname");
					ygnetpassword=config.getString("ygnetpassword");
				} catch (ConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection(ygneturl,ygnetname,ygnetpassword);
			Statement stmt = conn.createStatement();
	//	      PreparedStatement ps = conn.prepareStatement("select * from t_user");
		 
		//4.执行数据库命令
		ResultSet rs = stmt.executeQuery("select LASTUPDATETIME from ( select LASTUPDATETIME from qbzx_user  group by LASTUPDATETIME order by LASTUPDATETIME desc ) where rownum=1");
	//	      ResultSet rs = ps.executeQuery();
		 
		//5.处理执行结果
		while (rs.next()) {
			list.add(rs.getString("LASTUPDATETIME"));
		}
		//6.释放数据库资源
		if (rs != null) {
			rs.close();
		}
	//	      if (ps != null) {
	//	          ps.close();
	//	      }
		if (stmt != null) {
		stmt.close();
		}
		if (conn != null) {
		conn.close();
		}
		return list;
	}
	 
	 public void doDataTransfer() {
		 	log.info("开始抽取数据");
			try {
				List lists=this.selctID();
				List<Policess> list=this.DataExtract(lists);
				try{
				   int i=this.DataStorage(list);
				}catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 System.out.println("抽取数据结束");
		}
	 
	 
}
