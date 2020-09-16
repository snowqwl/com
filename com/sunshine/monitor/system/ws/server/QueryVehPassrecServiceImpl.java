package com.sunshine.monitor.system.ws.server;

import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.dao.UrlDao;
import com.sunshine.monitor.system.veh.dao.PassrecDao;
import com.sunshine.monitor.system.ws.QueryVehPassrecConditions;
import com.sunshine.monitor.system.ws.VehPassrecEntity;

@WebService(endpointInterface = "com.sunshine.monitor.system.ws.server.QueryVehPassrecService", 
		serviceName = "QueryVehPassrecService")
@Component("queryVehPassrecServiceImpl")
public class QueryVehPassrecServiceImpl implements QueryVehPassrecService {

	@Autowired
	private PassrecDao passrecDao;
	
	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;
	
	@Autowired
	private UrlDao urlDao;
	@Autowired
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public List<VehPassrecEntity> queryVehPassrec(
			QueryVehPassrecConditions queryConditions) {
		List<VehPassrecEntity> list = null;
		String sql = getSql(queryConditions);
		
			try {
				list = passrecDao.queryPassrecLimitedList(sql);
			} catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
			if(list != null && list.size()>0) {
				for(VehPassrecEntity vp:list){
					try {
						vp.setCity(urlDao.getUrlName(vp.getGcxh().substring(0,4)+"00000000"));
						vp.setKdmc(gateManager.getGateName(vp.getKdbh()));
						vp.setFxmc(gateManager.getDirectName(vp.getFxbh()));
						Code code = systemDao.getCode("030107", vp.getHpzl());
						vp.setHpzlmc(code==null?"-":code.getDmsm1());
						vp.setSbmc("");
						vp.setTp1(vp.getTp1()==null?"":vp.getTp1().replaceAll("\\\\", "/"));
						vp.setTp2(vp.getTp2()==null?"":vp.getTp2().replaceAll("\\\\", "/"));
						vp.setTp3(vp.getTp3()==null?"":vp.getTp3().replaceAll("\\\\", "/"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		
		return list;
	}

	private String getSql(QueryVehPassrecConditions queryConditions) {
		String _queryCount = "1000";
		String _psql = "select /*+FIRST_ROWS(1)*/ csz from frm_syspara where gjz='gatherlimit'";
		_queryCount = (String) jdbcTemplate.queryForObject(_psql, String.class);
		
		StringBuffer sql = new StringBuffer(30);
		StringBuffer sql2 = new StringBuffer(30);
		String _kdbh = queryConditions.getKdbh();
		String _fxbh = queryConditions.getFxbh();
		String _hphm = queryConditions.getHphm();
		String _hpzl = queryConditions.getHpzl();
		String _kssj = queryConditions.getKssj();
		String _jssj = queryConditions.getJssj();
		String hint = "" ;
		if(_kdbh != null && !"".equals(_kdbh)) {
			sql.append(" and kdbh ='").append(_kdbh).append("'");
			hint = " /*+INDEX(veh_passrec IDX_VEH_PASSREC_HPHM,LI_PASSREC_GCSJ)*/ ";
		} else {
			hint = " /*+FIRST_ROWS*/ ";
		}
		if(_fxbh != null && !"".equals(_fxbh)) {
			sql.append(" and fxbh ='").append(_fxbh).append("'");
		}
		if (_hphm != null && !"".equals(_hphm)) {
			sql.append(" and hphm like '").append(_hphm).append("'");
		}
		if (_hpzl != null && !"".equals(_hpzl)) {
			sql.append(" and hpzl ='").append(_hpzl).append("'");
		}
		if (_jssj != null && !"".equals(_jssj)) {
			sql.append(" and gcsj <=").append("to_date('").append(_jssj)
					.append("','yyyy-mm-dd hh24:mi:ss')");
		}
		if (_kssj != null && !"".equals(_kssj)) {
			sql.append(" and gcsj >=").append("to_date('").append(_kssj)
					.append("','yyyy-mm-dd hh24:mi:ss')");
		}
		sql2.append("select * from (select "+hint+" * from veh_passrec where 1 = 1 "); 
		sql2.append(sql.toString());
		sql2.append(" order by gcsj desc)");
		sql2.append(" where rownum <= ");
		sql2.append(_queryCount);
		return sql2.toString();
	}
}
