package com.sunshine.monitor.system.sign.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.SuspApprovalFlag;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.sign.bean.BKSign;
import com.sunshine.monitor.system.sign.bean.Duty;
import com.sunshine.monitor.system.sign.dao.BKSignDao;
import com.sunshine.monitor.system.sign.service.BKSignManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

@Service("BKSignManager")
public class BKSignManagerImpl implements BKSignManager{
	
	@Autowired
	private BKSignDao bKSignDao;
	
	@Autowired
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;
	
	@Transactional
	public String addSign(BKSign bs) throws Exception {
		return this.bKSignDao.addSign(bs);
	}

	@Transactional
	public BKSign querySign(BKSign bs) throws Exception {
		bs.setQdkssj(this.getSysteDate());
		return this.bKSignDao.querySign(bs);
	}

	@Transactional
	public String getSysteDate() throws Exception {
		return this.bKSignDao.getSysteDate();
	}

	@Transactional
	public int dosignout(BKSign bs) throws Exception {
		// 签 出
		bs.setBcksps(this.getBCKSPS(bs));
		bs.setYjqss(this.getYJQSS(bs));
		bs.setZlxds(this.getZLXDS(bs));
		bs.setZlfks(this.getZLFKS(bs));
		this.signout(bs);
		// 签 到(交接)
		/*BKSign b = new BKSign();
		b.setQdr(bs.getJjr());
		b.setQdkssj(bs.getQdjssj());
		b.setDwdm(this.systemDao.getUser(bs.getJjr()).getGlbm());
		b.setBz(bs.getBz());
		this.addSign(b);*/
		return 1;
	}
	
	@Transactional
	public int signout(BKSign bs) throws Exception {
		return this.bKSignDao.signout(bs);
	}
	
	public List<BKSign> queryNotSignout(String yhdh) throws Exception{
		List<BKSign> list = this.bKSignDao.queryNotSignout(yhdh);
		for(BKSign bks:list){
			String dwdm = bks.getDwdm();
			bks.setDwdmmc(this.systemDao.getDepartmentName(dwdm));
			String qdr = bks.getQdr();
			bks.setQdrmc(this.systemDao.getUserName(qdr));
		}
		return list;
	}
	
	public BKSign queryBKSignById(String id) throws Exception {
		
		return this.bKSignDao.queryBKSignById(id);
	}

	public Map<String, Object> getSignList(Map<String, Object> conditions)
			throws Exception {
		Map<String, Object> map = this.bKSignDao.getSignList(conditions);
		List<Map<String,Object>> list = (List<Map<String,Object>>)map.get("rows");
		for(Map<String,Object> md:list){
			String dwdm = (String)md.get("DWDM");
			String dwdmmc = this.systemDao.getDepartmentName(dwdm);
			md.put("DWDMMC", dwdmmc);
			String qdr = (String)md.get("QDR");
			String qdrmc = this.systemDao.getUserName(qdr);
			md.put("QDRMC", qdrmc);
		}
		return map;
	}
	
	@Transactional
	public long getBCKSPS(BKSign bs) throws Exception {
		return this.bKSignDao.getBCKSPS(bs);
	}
	
	@Transactional
	public long getYJQSS(BKSign bs) throws Exception {
		return this.bKSignDao.getYJQSS(bs);
	}
	
	@Transactional
	public long getZLXDS(BKSign bs) throws Exception {
		return this.bKSignDao.getZLXDS(bs);
	}
	
	@Transactional
	public long getZLFKS(BKSign bs) throws Exception {
		return this.bKSignDao.getZLFKS(bs);
	}

	@Transactional
	public List<Object> queryBCKSPSList(BKSign bs) throws Exception {
		List<Map<String,Object>> list = this.bKSignDao.queryBCKSPSList(bs);
		for(Map<String,Object> map:list){
			String bkdlmc = "";
			String bklbmc = "" ;
			String hpzlmc = "" ;
			// 操作人
			String yhdh = (String)map.get("CZR");
			map.put("CZRMC", this.systemDao.getUserName(yhdh));
			// 号牌种类
			String hpzl = (String)map.get("HPZL");
			Code code = this.systemDao.getCode("030107", hpzl);
			if(code != null)
				hpzlmc = code.getDmsm1();
			map.put("HPZLMC", hpzlmc);
			// 布控大类
			String bkdl = (String)map.get("BKDL");
			code = this.systemDao.getCode("120019", bkdl);
			if(code != null)
				bkdlmc = code.getDmsm1();
			map.put("BKDLMC", bkdlmc);
			// 布控类型
			String bklb = (String)map.get("BKLB");
			code = this.systemDao.getCode("120005", bklb);
			if(code != null)
				bklbmc = code.getDmsm1();
			map.put("BKLBMC", bklbmc);
			// 标志位
			String bzw = (String)map.get("BZW");
			if(SuspApprovalFlag.DISPATCHED_APPROVAL.getCode().equals(bzw)){
				map.put("BZWMC", SuspApprovalFlag.DISPATCHED_APPROVAL.getDesc());
			}else if(SuspApprovalFlag.WITHDRAW_APPROVAL.getCode().equals(bzw)){
				map.put("BZWMC", SuspApprovalFlag.WITHDRAW_APPROVAL.getDesc());
			}else{
				map.put("BZWMC", "");
			}
		}
		return (List)list;
	}

	public List<Object> queryYJQSSList(BKSign bs) throws Exception {
		List<Map<String,Object>> list = this.bKSignDao.queryYJQSSList(bs);
		for(Map<String,Object> map:list){
			String bkdlmc = "" ;
			String bklbmc = "" ;
			String hpzlmc = "" ;
			String qrztmc = "" ;
			// 操作人
			String yhdh = (String)map.get("QRR");
			map.put("QRRMC", this.systemDao.getUserName(yhdh));
			// 号牌种类
			String hpzl = (String)map.get("HPZL");
			Code code = this.systemDao.getCode("030107", hpzl);
			if(code != null)
				hpzlmc = code.getDmsm1();
			map.put("HPZLMC", hpzlmc);
			// 大类
			String bkdl = (String)map.get("BJDL");
			code = this.systemDao.getCode("120019", bkdl);
			if(code != null)
				bkdlmc = code.getDmsm1();
			map.put("BJDLMC", bkdlmc);
			// 类型
			String bklb = (String)map.get("BJLX");
			code = this.systemDao.getCode("120005", bklb);
			if(code != null)
				bklbmc = code.getDmsm1();
			map.put("BJLBMC", bklbmc);
			// 确认状态
			String qrzt = (String)map.get("QRZT");
			code = this.systemDao.getCode("120014", qrzt);
			if(code != null)
				qrztmc = code.getDmsm1();
			map.put("QRZTMC", qrztmc);
		}
		return (List)list;
	}

	public List<Object> queryXDZLSList(BKSign bs) throws Exception {
		List<Map<String,Object>> list = this.bKSignDao.queryXDZLSList(bs);
		for(Map<String,Object> map:list){
			String bjdlmc = "" ;
			String bjlbmc = "" ;
			String hpzlmc = "" ;
			// 操作人
			String yhdh = (String)map.get("ZLR");
			map.put("ZLRMC", this.systemDao.getUserName(yhdh));
			// 号牌种类
			String hpzl = (String)map.get("HPZL");
			Code code = this.systemDao.getCode("030107", hpzl);
			if(code != null)
				hpzlmc = code.getDmsm1();
			map.put("HPZLMC", hpzlmc);
			// 报警大类
			String bjdl = (String)map.get("BJDL");
			code = this.systemDao.getCode("130033", bjdl);
			if(code != null)
				bjdlmc = code.getDmsm1();
			map.put("BJDLMC", bjdlmc);
			// 报警类型
			String bjlb = (String)map.get("BKLX");
			code = this.systemDao.getCode("130034", bjlb);
			if(code != null)
				bjlbmc = code.getDmsm1();
			map.put("BJLBMC", bjlbmc);
			map.put("ZLZTMC", ("1".equals((String)map.get("SFXDZL")))?"已下达":"未下达");
		}
		return (List)list;
	}

	public List<Object> queryZLFKSList(BKSign bs) throws Exception {
		List<Map<String,Object>> list = this.bKSignDao.queryZLFKSList(bs);
		for(Map<String,Object> map:list){
			String bkdlmc = "" ;
			String bklbmc = "" ;
			String hpzlmc = "" ;
			// 操作人
			String yhdh = (String)map.get("BY2");
			map.put("FKRMC", this.systemDao.getUserName(yhdh));
			// 号牌种类
			String hpzl = (String)map.get("HPZL");
			Code code = this.systemDao.getCode("030107", hpzl);
			if(code != null)
				hpzlmc = code.getDmsm1();
			map.put("HPZLMC", hpzlmc);
			// 布控大类
			String bkdl = (String)map.get("BJDL");
			code = this.systemDao.getCode("120019", bkdl);
			if(code != null)
				bkdlmc = code.getDmsm1();
			map.put("BJDLMC", bkdlmc);
			// 布控类型
			String bklb = (String)map.get("BJLX");
			code = this.systemDao.getCode("120005", bklb);
			if(code != null)
				bklbmc = code.getDmsm1();
			map.put("BJLBMC", bklbmc);
			String tt = (String)map.get("BY1");
			if(tt !=null && !"".equals(tt)){
				if("1".equals(tt)){
					map.put("FKZTMC", "有效");
				}else if("0".equals(tt)){
					map.put("FKZTMC", "无效");
				}
			}
		}
		return (List)list;
	}

	public BKSign queryBkSignListBysubsql(BKSign bs) throws Exception {
		//String citybh = bs.getDwdm().substring(0, 4);
		StringBuffer subSql = new StringBuffer(20);
		/*
		subSql.append(" where instr(dwdm,'");
		subSql.append(citybh);
		subSql.append("')>0 and rownum=1 ");
		*/
		subSql.append(" where qdr ='"+bs.getQdr()+"' and qdkssj = (select max(qdkssj) from jm_bk_sign where qdr='"+bs.getQdr()+"')");
		return this.bKSignDao.queryBkSignListBysubsql(subSql.toString());
	}
	
	public int checkUser(SysUser sysuser) throws Exception{
		int result = 0;
	    result = this.bKSignDao.checkUser(sysuser);
	    if (result == 1)
	      throw new Exception("系统里面没有该用户");
	    if (result == 6)
	      throw new Exception("密码错误!");
	    return result;
	}

	public List<VehSuspinfo> queryNoBCKSPSList(BKSign bs) throws Exception {
		List<VehSuspinfo> list = this.bKSignDao.queryNoBCKSPSList(bs);
		for(VehSuspinfo v : list){
			Code code = this.systemDao.getCode("030107", v.getHpzl());
			String hpzlmc = "";
			String bklbmc = "";
			if(code != null)
				hpzlmc = code.getDmsm1();
			v.setHpzlmc(hpzlmc);
			code = this.systemDao.getCode("120005", v.getBklb());
			if(code != null)
				bklbmc = code.getDmsm1();
			v.setBklbmc(bklbmc);
			v.setBkjgmc(this.systemDao.getDepartmentName(v.getBkjg()));
			v.setBkrmc(this.systemDao.getUserName(v.getBkr()));
		}
		return list;
	}

	public List<VehAlarmrec> queryNoYJQSSList(BKSign bs) throws Exception {
		List<VehAlarmrec> list = this.bKSignDao.queryNoYJQSSList(bs);
		for(VehAlarmrec v : list){
			String hpzlmc = "";
			String bjlxmc = "";
			String bjdlmc = "";
			Code code = this.systemDao.getCode("030107", v.getHpzl());
			if(code != null){
				hpzlmc = code.getDmsm1();
			}
			v.setHpzlmc(hpzlmc);
			
			code = this.systemDao.getCode("120005", v.getBjlx());
			if(code != null){
				bjlxmc = code.getDmsm1();
			}
			v.setBjlxmc(bjlxmc);
			
			code = this.systemDao.getCode("120019", v.getBjdl());
			if(code != null){
				bjdlmc = code.getDmsm1();
			}
			v.setBjdlmc(bjdlmc);
			v.setBjdwmc(this.systemDao.getDepartmentName(v.getBjdwdm()));
			v.setKdmc(this.gateManager.getGateName(v.getKdbh()));
			v.setFxmc(this.gateManager.getDirectName(v.getFxbh()));
		}
		return list;
	}

	public List<VehAlarmrec> queryNoXDZLSList(BKSign bs) throws Exception {
		List<VehAlarmrec> list = this.bKSignDao.queryNoXDZLSList(bs);
		for(VehAlarmrec v : list){
			String hpzlmc = "";
			String bjlxmc = "";
			String bjdlmc = "";
			Code code = this.systemDao.getCode("030107", v.getHpzl());
			if(code != null){
				hpzlmc = code.getDmsm1();
			}
			v.setHpzlmc(hpzlmc);
			
			code = this.systemDao.getCode("120005", v.getBjlx());
			if(code != null){
				bjlxmc = code.getDmsm1();
			}
			v.setBjlxmc(bjlxmc);
			
			code = this.systemDao.getCode("120019", v.getBjdl());
			if(code != null){
				bjdlmc = code.getDmsm1();
			}
			v.setBjdlmc(bjdlmc);
			v.setBjdwmc(this.systemDao.getDepartmentName(v.getBjdwdm()));
			v.setKdmc(this.gateManager.getGateName(v.getKdbh()));
			v.setFxmc(this.gateManager.getDirectName(v.getFxbh()));
		}
		return list;	
	}

	public List<Object> queryNoZLFKSList(BKSign bs)
			throws Exception {
		List<Map<String,Object>> list = this.bKSignDao.queryNoZLFKSList(bs);
		for(Map<String,Object> map:list){
			String bkdlmc = "" ;
			String bklbmc = "" ;
			String hpzlmc = "" ;
			// 号牌种类
			String hpzl = (String)map.get("HPZL");
			Code code = this.systemDao.getCode("030107", hpzl);
			if(code != null)
				hpzlmc = code.getDmsm1();
			map.put("HPZLMC", hpzlmc);
			// 布控大类
			String bkdl = (String)map.get("BJDL");
			code = this.systemDao.getCode("120019", bkdl);
			if(code != null)
				bkdlmc = code.getDmsm1();
			map.put("BJDLMC", bkdlmc);
			// 布控类型
			String bklb = (String)map.get("BJLX");
			code = this.systemDao.getCode("120005", bklb);
			if(code != null)
				bklbmc = code.getDmsm1();
			map.put("BJLBMC", bklbmc);
			map.put("FKZTMC", "未反馈");
		}
		return (List)list;
	}
	public SysUser getJjrdh(String jjr)throws Exception{
	 return this.bKSignDao.getJjrdh(jjr);
		
	}
	/**
	 * 查询签到表中当前值班人数据
	 */
	public BKSign getDqzbrsj() throws Exception{
		return this.bKSignDao.getDqzbrsj();		
	}
	public List<Duty> queryNextZbr(String yhmc) throws Exception{
		List<Duty> list = this.bKSignDao.queryNextZbr(yhmc);		
		return list;
	}
	public int saveZbrlist(Map<String, Object> conditions) throws Exception{
		return this.bKSignDao.saveZbrlist(conditions);	
	}
	public Map<String, Object> getZblist(Map<String, Object> conditions) throws Exception{
		return this.bKSignDao.getZblist(conditions);	
	}
	
	public Map<String, Object> getZblistView(Map<String, Object> conditions) throws Exception{
		return this.bKSignDao.getZblistView(conditions);	
	}
	
	public int addZxzb(UserSession userSession) throws Exception{
		return this.bKSignDao.addZxzb(userSession);	
	}
	
	
	public Map<String, Object> getPeople(Map<String, Object> conditions){
		Map<String, Object> map = this.bKSignDao.getPeople(conditions);
		return map;
	}
	
	
	public Map changePeople(Map<String, Object> conditions) {
		// TODO Auto-generated method stub
		return this.bKSignDao.changePeople(conditions);
	}
	public String getTime() {
		return this.bKSignDao.getTime();
	}
	public List<Duty> editZblist(String glbm) throws Exception{
		return this.bKSignDao.editZblist(glbm);	
	}
	
	

	@Transactional
	public void countWork(String id,int y_bcksp, int y_yjqs, int y_xdzl, int y_zlfk) {
		// TODO Auto-generated method stub
		this.bKSignDao.updateCountWork( id, y_bcksp,  y_yjqs,  y_xdzl,  y_zlfk);
		
	}
}
