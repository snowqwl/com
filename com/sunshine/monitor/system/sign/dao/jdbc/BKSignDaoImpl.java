package com.sunshine.monitor.system.sign.dao.jdbc;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.sunshine.monitor.comm.bean.User;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.CallableStatementCallbackImpl;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.notice.bean.SysInformation;
import com.sunshine.monitor.system.sign.bean.BKSign;
import com.sunshine.monitor.system.sign.bean.Duty;
import com.sunshine.monitor.system.sign.dao.BKSignDao;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

@Repository("BKSignDao")
public class BKSignDaoImpl extends BaseDaoImpl implements BKSignDao {

    public String addSign(BKSign bs) throws Exception {
        String id = this.jdbcTemplate.queryForObject(
                "select SEQ_BK_SIGN.NEXTVAL from dual", String.class);
		/*
		String sql = "insert into JM_BK_SIGN(ID, QDKSSJ, QDR, DWDM)values('"
				+ id + "',to_date('" + bs.getQdkssj()
				+ "','yyyy-mm-dd hh24:mi:ss'),'" + bs.getQdr() + "','"
				+ bs.getDwdm() + "')";
		*/
        List param = new ArrayList<>();
        StringBuffer sql = new StringBuffer("insert into JM_BK_SIGN(ID, QDKSSJ, QDR, DWDM, BZ," +
                "ZBRMC,ZBRBH,ZBRDH)values(?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,");
        param.add(id);
        param.add(bs.getQdkssj());
        param.add(bs.getQdr());
        param.add(bs.getDwdm());
        if (bs.getBz() == null) {
            sql.append("null ,");
        } else {
            sql.append("?,");
            param.add(bs.getBz());
        }
        sql.append("?,?,?)");
        param.add(bs.getZbrmc());
        param.add(bs.getZbrbh());
        param.add(bs.getZbrdh());
        this.jdbcTemplate.update(sql.toString(), param.toArray());
        return id;
    }

    public BKSign queryBKSignById(String id) throws Exception {
        String sql = "select id, qdkssj, qdjssj, qdr, dwdm, jjr, bz from JM_BK_SIGN where id=?";
        List<BKSign> list = this.queryList(sql, new Object[]{id}, BKSign.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public BKSign querySign(BKSign bs) throws Exception {
        String sql = "select id, qdkssj, qdjssj, qdr, dwdm, jjr from JM_BK_SIGN where to_char" +
                "(qdkssj,'yyyy-mm-dd')=? and qdr = ?";
        List<BKSign> list = this.queryList(sql, new Object[]{bs.getQdkssj(), bs.getQdr()}, BKSign.class);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public String getSysteDate() throws Exception {
        String sql = "select to_char(sysdate,'YYYY-MM-dd') from dual";
        String nowDate = this.jdbcTemplate.queryForObject(sql, String.class);
        return nowDate;
    }

    public int signout(BKSign bs) throws Exception {
        List param = new ArrayList<>();
        String sql = "update JM_BK_SIGN set jjr=?, qdjssj=to_date(?,'yyyy-mm-dd hh24:mi:ss')," +
                "bcksps=?, yjqss=?,zlxds=?,zlfks=? where id=?";
        param.add(bs.getJjr());
        param.add(bs.getQdjssj());
        if (bs.getBcksps() == null) {
            param.add(0);
        } else {
            param.add(bs.getBcksps());
        }
        if (bs.getYjqss() == null) {
            param.add(0);
        } else {
            param.add(bs.getYjqss());
        }
        if (bs.getZlxds() == null) {
            param.add(0);
        } else {
            param.add(bs.getZlxds());
        }
        if (bs.getZlfks() == null) {
            param.add(0);
        } else {
            param.add(bs.getZlfks());
        }
        param.add(bs.getId());
        return this.jdbcTemplate.update(sql, param.toArray());
    }

    public List<BKSign> queryNotSignout(String yhdh) throws Exception {
        String sql = "select id, qdkssj, qdjssj, qdr, dwdm, jjr, bz,zbrmc from JM_BK_SIGN t where" +
                " qdr=? and qdjssj is null and jjr is null";
        List<BKSign> list = this.queryList(sql, new Object[]{yhdh}, BKSign.class);
        return list;
    }

    public Map<String, Object> getSignList(Map<String, Object> conditions)
            throws Exception {
        List param = new ArrayList<>();
        StringBuffer sql = new StringBuffer(50);
        sql.append("select /*+RULE*/ id, to_char(qdkssj,'yyyy-mm-dd hh24:mi:ss') qdkssj, to_char(qdjssj,'yyyy-mm-dd hh24:mi:ss') qdjssj, qdr, dwdm, bcksps, yjqss, zlxds, zlfks, bz,zbrmc,zbrbh from  JM_BK_SIGN a where 1 = 1 ");
        Set<Entry<String, Object>> set = conditions.entrySet();
        Iterator<Entry<String, Object>> it = set.iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            String value = (String) entry.getValue();
            boolean isFilter = "sort".equalsIgnoreCase(key)
                    || "order".equalsIgnoreCase(key)
                    || "page".equalsIgnoreCase(key)
                    || "rows".equalsIgnoreCase(key);
            if (!isFilter) {
                if (key.indexOf("kssj") != -1) {
                    sql.append(" and qdkssj >= ").append(
                            "to_date(?,'yyyy-mm-dd hh24:mi:ss')");
                    param.add(value);
                } else if (key.indexOf("jssj") != -1) {
                    sql.append(" and qdkssj <= ").append(
                            "to_date(?,'yyyy-mm-dd hh24:mi:ss')");
                    param.add(value);
                } else if (key.indexOf("zbrmc") != -1) {
                    sql.append(" and zbrmc  like ?");
                    param.add("%" + value + "%");
                } else {
                    sql.append(" and ? = ?");
                    param.add(key);
                    param.add(value);
                }
            }
        }
        sql.append(" order by ? ?");
        param.add(conditions.get("sort"));
        param.add(conditions.get("order"));
        Map<String, Object> map = findPageForMap(sql.toString(), param.toArray(),
                Integer.parseInt(conditions.get("page").toString()),
                Integer.parseInt(conditions.get("rows").toString()));
        return map;
    }

    // -----------------------------------------------
    public long getBCKSPS(BKSign bs) throws Exception {
        List param = new ArrayList<>();
        String sql = "select count(1) from AUDIT_APPROVE t where czr=? and bzw in(2,4) and czsj " +
                ">= to_date(?,'yyyy-mm-dd hh24:mi:ss') and czsj <= to_date(?,'yyyy-mm-dd " +
                "hh24:mi:ss')";
        param.add(bs.getQdr());
        param.add(bs.getQdkssj());
        param.add(bs.getQdjssj());
        return this.jdbcTemplate.queryForLong(sql, param.toArray());
    }

    public long getYJQSS(BKSign bs) throws Exception {
        List param = new ArrayList<>();
        String sql =
                "select count(1) from VEH_ALARMREC t where qrr=? and qrsj >= to_date(?," +
                        "'yyyy-mm-dd hh24:mi:ss') and qrsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
        param.add(bs.getQdr());
        param.add(bs.getQdkssj());
        param.add(bs.getQdjssj());
        return this.jdbcTemplate.queryForLong(sql, param.toArray());
    }

    public long getZLXDS(BKSign bs) throws Exception {
        List param = new ArrayList<>();
        String sql = "select count(1) from VEH_ALARM_CMD t where zlr=? and zlsj >= to_date(?," +
                "'yyyy-mm-dd hh24:mi:ss') and zlsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
        param.add(bs.getQdr());
        param.add(bs.getQdkssj());
        param.add(bs.getQdjssj());
        return this.jdbcTemplate.queryForLong(sql, param.toArray());
    }

    public long getZLFKS(BKSign bs) throws Exception {
        List param = new ArrayList<>();
        String sql = "select count(*) from VEH_ALARM_HANDLED t where BY2=? and to_date(by4," +
                "'yyyy-mm-dd hh24:mi:ss') >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and to_date(by4," +
                "'yyyy-mm-dd hh24:mi:ss') <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
        param.add(bs.getQdr());
        param.add(bs.getQdkssj());
        param.add(bs.getQdjssj());
        return this.jdbcTemplate.queryForLong(sql, param.toArray());
    }

    public List<Object> queryBCKSPSList(BKSign bs) throws Exception {
        List param = new ArrayList<>();
        String sql = "select b.bkxh, b.hphm, b.hpzl, b.bkdl, b.bklb, b.lar, b.ladw, b.bkr, b" +
                ".bkjg,b.bkjgmc, to_char(b.bkqssj,'yyyy-mm-dd hh24:mi:ss') bkqssj, to_char(b" +
                ".bkjzsj,'yyyy-mm-dd hh24:mi:ss') bkjzsj, to_char(czsj,'yyyy-mm-dd hh24:mi:ss') " +
                "czsj, a.czr, a.bzw  from AUDIT_APPROVE a join veh_suspinfo b on a.bkxh = b.bkxh where a.bzw in(2,4) and a.czr=? and czsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and czsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
        param.add(bs.getQdr());
        param.add(bs.getQdkssj());
        param.add(bs.getQdjssj());
        return (List) this.jdbcTemplate.queryForList(sql, param.toArray());
    }

    public List<Object> queryYJQSSList(BKSign bs) throws Exception {
        List param = new ArrayList<>();
        String sql = "select t.bjxh, t.bkxh, t.hphm, t.hpzl, t.bjdl, t.bjlx, t.kdbh, c.kdmc, t" +
                ".fxbh, f.fxmc, to_char(t.bjsj, 'yyyy-mm-dd hh24:mi:ss') bjsj, t.qrr, to_char(t" +
                ".qrsj, 'yyyy-mm-dd hh24:mi:ss') qrsj, t.qrzt  from VEH_ALARMREC t left join code_gate c on t.kdbh = c.kdbh  left join code_direct f  on t.fxbh = f.fxbh  where t.qrr = ? and t.qrsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and t.qrsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
        param.add(bs.getQdr());
        param.add(bs.getQdkssj());
        param.add(bs.getQdjssj());
        return (List) this.jdbcTemplate.queryForList(sql, param.toArray());
    }

    public List<Object> queryXDZLSList(BKSign bs) throws Exception {
        List param = new ArrayList<>();
        String sql = "select t.zlxh, t.bjxh, a.hphm, a.hpzl, a.bjdl, a.bjlx, to_char(a.bjsj, 'yyyy-mm-dd hh24:mi:ss') bjsj, a.kdmc,  a.fxmc, a.sfxdzl, t.zlr,t.zlrmc, t.zldwmc,to_char(t.zlsj, 'yyyy-mm-dd hh24:mi:ss') zlsj from VEH_ALARM_CMD t left join veh_alarmrec a on t.bjxh = a.bjxh left join code_gate c  on a.kdbh = c.kdbh left join code_direct f on a.fxbh = f.fxbh where where sfxdzl = '1' and zlr = ? and zlsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and zlsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
        param.add(bs.getQdr());
        param.add(bs.getQdkssj());
        param.add(bs.getQdjssj());
        return (List) this.jdbcTemplate.queryForList(sql, param.toArray());
    }

    public List<Object> queryZLFKSList(BKSign bs) throws Exception {
        List param = new ArrayList<>();
        String sql = "select t.fkbh,t.bkxh,t.bjxh,t.zlxh,t.by1,t.by2, t.by3,t.by4,a.hphm,a.bjdl,a" +
                ".bjlx,to_char(a.bjsj,'yyyy-mm-dd hh24:mi:ss') bjsj,a.hpzl,a.kdbh,a.kdmc,a.fxmc " +
                "from VEH_ALARM_HANDLED t join veh_alarmrec a  on t.bjxh = a.bjxh  where t.by1 in" +
                "('0','1') and t.BY2 = ? and to_date(t.by4,'yyyy-mm-dd hh24:mi:ss') >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and to_date(t.by4,'yyyy-mm-dd hh24:mi:ss') <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
        param.add(bs.getQdr());
        param.add(bs.getQdkssj());
        param.add(bs.getQdjssj());
        return (List) this.jdbcTemplate.queryForList(sql, param.toArray());
    }

    public BKSign queryBkSignListBysubsql(String subSql) throws Exception {
        String sql = "select * from JM_BK_SIGN ";
        if (subSql != null && !"".equals(subSql)) {
            sql = sql + subSql;
        }
        List<BKSign> list = this.queryForList(sql, BKSign.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public int checkUser(SysUser sysuser) throws Exception {
        String callString = "{call JM_SYS_PKG.check_user(?,?,?,?,?)}";
        CallableStatementCallbackImpl callBack = new CallableStatementCallbackImpl() {
            public Object doInCallableStatement(CallableStatement cstmt)
                    throws SQLException, DataAccessException {
                SysUser frmSysuser = (SysUser) getParameterObject();
                if (frmSysuser.getYhdh() == null) {
                    cstmt.setNull(1, 12);
                } else {
                    cstmt.setString(1, frmSysuser.getYhdh());
                }
                if (frmSysuser.getMm() == null) {
                    cstmt.setNull(2, 12);
                } else {
                    cstmt.setString(2, frmSysuser.getMm());
                }
                if (frmSysuser.getYzm() == null) {
                    cstmt.setNull(3, 12);
                } else {
                    cstmt.setString(3, frmSysuser.getYzm());
                }
                if (frmSysuser.getBz() == null) {
                    cstmt.setNull(4, 12);
                } else {
                    cstmt.setString(4, frmSysuser.getBz());
                }
                cstmt.registerOutParameter(5, 2);
                cstmt.execute();
                int iResult = cstmt.getInt(5);
                return new Integer(iResult);
            }
        };
        callBack.setParameterObject(sysuser);
        Integer i = (Integer) this.jdbcTemplate.execute(callString, callBack);
        return i.intValue();
    }

    // ------------------------------------------
    public List<VehSuspinfo> queryNoBCKSPSList(BKSign bs) throws Exception {
        List param = new ArrayList<>();
        String sql = "Select bkxh, hphm, hpzl, bklb, bkjg, bkr, to_char(bkqssj,'yyyy-mm-dd " +
                "hh24:mi:ss') bkqssj, to_char(bkjzsj,'yyyy-mm-dd hh24:mi:ss') bkjzsj, bjzt from " +
                "VEH_SUSPINFO Where YWZT in('12','42') and XXLY = '0' and bksj >= to_date(?, " +
                "'yyyy-mm-dd hh24:mi:ss') and bksj <= to_date(?, 'yyyy-mm-dd hh24:mi:ss') and bkjg in (Select xjjg from frm_prefecture Where dwdm = ?) order by bksj desc";
        param.add(bs.getQdkssj());
        param.add(bs.getQdjssj());
        param.add(bs.getDwdm());
        return this.queryList(sql, param.toArray(), VehSuspinfo.class);
    }

    public List<VehAlarmrec> queryNoYJQSSList(BKSign bs) throws Exception {
        List param = new ArrayList<>();
        String sql = "select /*+RULE*/ bjxh, hphm, hpzl, bjdl, bjlx, bjdwdm, to_char(bjsj," +
                "'yyyy-mm-dd hh24:mi:ss') bjsj, kdbh, fxbh, qrzt from VEH_ALARMREC where QRZT = " +
                "'0' and BJDWDM = ? and bjsj >= to_date(?, 'yyyy-mm-dd hh24:mi:ss') and bjsj <= " +
                "to_date(?, 'yyyy-mm-dd hh24:mi:ss') order by BJSJ desc";
        param.add(bs.getDwdm());
        param.add(bs.getQdkssj());
        param.add(bs.getQdjssj());
        return this.queryList(sql, param.toArray(), VehAlarmrec.class);
    }

    public List<VehAlarmrec> queryNoXDZLSList(BKSign bs) throws Exception {
        List param = new ArrayList<>();
        String sql = "select /*+RULE*/ bjxh, hphm, hpzl, bjdl, bjlx, bjdwdm, to_char(bjsj," +
                "'yyyy-mm-dd hh24:mi:ss') bjsj, kdbh, fxbh, qrzt, sfxdzl from VEH_ALARMREC where " +
                " QRZT = '1' and SFXDZL = '0'  and BJDWDM = ? and qrsj >= to_date(?, 'yyyy-mm-dd " +
                "hh24:mi:ss') and qrsj <= to_date(?, 'yyyy-mm-dd hh24:mi:ss') order by BJSJ desc";
        param.add(bs.getDwdm());
        param.add(bs.getQdkssj());
        param.add(bs.getQdjssj());

        return this.queryList(sql, param.toArray(), VehAlarmrec.class);
    }

    public List<Object> queryNoZLFKSList(BKSign bs) throws Exception {
        List param = new ArrayList<>();
        String sql = "select t.fkbh, t.bkxh, t.bjxh, t.zlxh, a.hphm,a.bjdl, a.bjlx,a.hpzl, a" +
                ".kdbh, a.kdmc,a.fxmc, to_char(t.bjsj,'yyyy-mm-dd hh24:mi:ss') bjsj, t.sflj, " +
                "to_char(t.lrsj,'yyyy-mm-dd hh24:mi:ss') lrsj, t.by1 from veh_alarm_handled t, " +
                "veh_alarmrec a where t.bjxh = a.bjxh and t.sflj = '1' and t.BJDWDM = ? and t.lrsj >= to_date(?, 'yyyy-mm-dd hh24:mi:ss') and t.lrsj <= to_date(?, 'yyyy-mm-dd hh24:mi:ss') and t.by1 is null order by t.LRSJ desc";
        param.add(bs.getDwdm());
        param.add(bs.getQdkssj());
        param.add(bs.getQdjssj());

        return (List) this.jdbcTemplate.queryForList(sql, param.toArray());
    }

    public SysUser getJjrdh(String jjr) throws Exception {
        String sql = "select t.lxdh1,t.lxdh3 from frm_sysuser t where t.yhdh=?";
        List<SysUser> list = this.queryList(sql, new Object[]{jjr}, SysUser.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public BKSign getDqzbrsj() throws Exception {
        String sql = "select * from jm_bk_sign where qdkssj in (select max(qdkssj)from jm_bk_sign)";
        List<BKSign> list = this.queryList(sql, BKSign.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<Duty> queryNextZbr(String yhmc) throws Exception {
        String sql = "select zbrmc,zbrbh from jm_duty where zbrbh not in (?)";
        List<Duty> list = this.queryList(sql, new Object[]{yhmc}, Duty.class);
        return list;
    }

    public int saveZbrlist(Map<String, Object> conditions) throws Exception {
        int result = 0;
        String zt = "";
        String lxdh = "";
        List param = new ArrayList<>();

        if (conditions.get("dqzbrtext").equals(conditions.get("zbrmc"))) {
            zt = "1";
        } else {
            zt = "0";
        }

        String dhsql = " select fs.lxdh1 from frm_sysuser fs where fs.yhdh=?";
        param.add(conditions.get("zbrbh"));

        List<String> list = this.jdbcTemplate.queryForList(dhsql, param.toArray(), String.class);
        //lxdh = this.jdbcTemplate(dhsql,String.class);
        if (!list.isEmpty()) {
            lxdh = list.get(0);
        }
        List v1 = new ArrayList<>();
        String sql = "insert into jm_duty(zbrbh,zbrmc,xgsj,zt,zbrdh,zbrbm,zbrbmbh,fpkssj,fpjssj) " +
                "values(?, ?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?, ?, ?, ?,to_date(?,'yyyy-mm-dd hh24:mi:ss')"
                + ",to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
        param.add(conditions.get("zbrbh"));
        param.add(conditions.get("zbrmc"));
        param.add(conditions.get("time"));
        param.add(zt);
        if ((lxdh == null)) {
            param.add("''");
        } else {
            param.add(lxdh);
        }
        param.add(conditions.get("bm"));
        param.add(conditions.get("AllDepart2"));
        param.add(conditions.get("kssj") + " 00:00:00");
        param.add(conditions.get("jssj") + " 00:00:00");


        result = this.jdbcTemplate.update(sql,param.toArray());
        return result;
    }

    public Map<String, Object> getZblist(Map<String, Object> condition)
            throws Exception {
        String sql = "";
        List param = new ArrayList<>();
        if (condition.containsKey("zbrmc")) {
            sql = " and jd.zbrmc like ?";
            param.add("%" + condition.get("zbrmc") + "%");
        }
        if (condition.containsKey("zt")) {
            sql = " and jd.zt =?";
            param.add(condition.get("zt"));
        }
		/*if (condition.containsKey("fpkssj") && condition.containsKey("fpjssj")) {
			sql = " and (fpkssj>=to_date('" + condition.get("fpkssj")
					+ "','yyyy-mm-dd hh24:mi:ss') and fpjssj<=to_date('"
					+ condition.get("fpjssj") + "','yyyy-mm-dd hh24:mi:ss'))";		
		}*/
        List param2 = new ArrayList<>();
        sql = "select zbr.*,zbb.* from (select count(zbrmc) as zbrs from jm_duty where xgsj in " +
                "(select max(xgsj) from jm_duty) and zbrbmbh = ? "
                + sql
                + " ) zbr,"
                + " (select DISTINCT zbrbm,xgsj,fpkssj,fpjssj from jm_duty where xgsj in (select " +
                "max(xgsj) from jm_duty) and zbrbmbh = ? "
                + sql
                + " ) zbb";
        param2.add(condition.get("glbm"));
        for (Object o : param) {
            param2.add(o);
        }
        param2.add(condition.get("glbm"));
        for (Object o : param) {
            param2.add(o);
        }


        return this.findPageForMap(sql, param2.toArray(),Integer.parseInt(condition.get(
                "page").toString()), Integer.parseInt(condition.get("rows")
                .toString()));

    }

    public Map<String, Object> getZblistView(Map<String, Object> condition)
            throws Exception {
        String sql = "";
        if (condition.containsKey("zbrmc")) {
            sql = " and jd.zbrmc like'%" + condition.get("zbrmc") + "%'";
        }
        if (condition.containsKey("zt")) {
            sql = " and jd.zt ='" + condition.get("zt") + "'";
        }


        sql = "select  fs.jh,jd.* from frm_sysuser fs,jm_duty jd where jd.zbrbh=fs.yhdh and xgsj in (select max(xgsj) from jm_duty) and zbrbmbh ='"
                + condition.get("glbm")
                + "' "
                + sql;


        return this.findPageForMap(sql, Integer.parseInt(condition.get(
                "page").toString()), Integer.parseInt(condition.get("rows")
                .toString()));
    }

    public int addZxzb(UserSession userSession) throws Exception {
        String id = this.jdbcTemplate.queryForObject(
                "select SEQ_BK_SIGN.NEXTVAL from dual", String.class);
        String sql = "select jd.* from jm_duty jd where jd.zt='1' and jd.xgsj in (select max(xgsj) from jm_duty)";

        List<Duty> list = this.queryList(sql, Duty.class);

        for (int i = 0; i < list.size(); i++) {
            Duty duty = list.get(i);
            String lxdh = "";
            if (duty.getZbrdh() != "") {
                lxdh = duty.getZbrdh();
            }
            String qdr = userSession.getSysuser().getYhdh();
            String dwdm = userSession.getSysuser().getGlbm();
            List param = new ArrayList<>();
            String insertSql = " insert into jm_bk_sign(id,zbrbh,zbrmc,qdkssj,qdr,dwdm,zbrdh) " +
                    "values(?,?,?,to_date(?,'yyyy" +
                    "-mm-dd hh24:mi:ss'), ? ,?,?)";
            param.add(id);
            param.add(duty.getZbrbh());
            param.add(duty.getZbrmc());
            param.add(duty.getXgsj());
            param.add(qdr);
            param.add(dwdm);
            param.add(lxdh);
            this.jdbcTemplate.update(insertSql,param.toArray());
        }
        return 0;
    }


    public Map<String, Object> getPeople(Map<String, Object> conditions) {
        // TODO Auto-generated method stub
        String dwdm = conditions.get("dwdm").toString();
        StringBuffer sql = new StringBuffer(50);
        sql.append("select * from  JM_DUTY a where 1 = 1 and xgsj in (select max(xgsj) from jm_duty where zbrbmbh='" + dwdm + "')");
//		Set<Entry<String, Object>> set = conditions.entrySet();
//		Iterator<Entry<String, Object>> it = set.iterator();
//			Entry<String, Object> entry = it.next();
//			String key = entry.getKey();
//			String value = (String) entry.getValue();
        Map<String, Object> map = findPageForMap(sql.toString(),
                Integer.parseInt(conditions.get("page").toString()),
                Integer.parseInt(conditions.get("rows").toString()));
        //Map<String, Object> map = findPageForMap(sql.toString(),1,20);
        return map;
    }


    public Map changePeople(Map<String, Object> conditions) {

        String dwdm = conditions.get("DWDM").toString();
        //值班人结束，更新结束时间
        List param = new ArrayList<>();
        StringBuffer sql1 = new StringBuffer("update JM_BK_SIGN set QDJSSJ=sysdate where ZBRBH in" +
                " (select ZBRBH from jm_duty where zt='1' and xgsj in (select max(xgsj) from " +
                "jm_duty where zbrbmbh=?) ) and QDKSSJ in (select max(QDKSSJ) from JM_BK_SIGN where dwdm=?) ");
        param.add(dwdm);
        param.add(dwdm);
        this.jdbcTemplate.update(sql1.toString(),param.toArray());


        Map sign = jdbcTemplate.queryForMap("select * from JM_BK_SIGN where ZBRBH  in (select " +
                "ZBRBH from jm_duty where zt='1' and xgsj in (select max(xgsj) from jm_duty where" +
                " zbrbmbh=?) ) and QDKSSJ in (select max(QDKSSJ) from JM_BK_SIGN where" +
                " dwdm=?) ",new Object[]{dwdm,dwdm});

        //查询出该id值
        //String signId = this.jdbcTemplate.queryForObject("select id from JM_BK_SIGN where ZBRBH in (select ZBRBH from jm_duty where zt='1' and xgsj in (select max(xgsj) from jm_duty)) and QDKSSJ in (select max(QDKSSJ) from JM_BK_SIGN) ", String.class);


        //更改结束了值班的值班人的状态
        StringBuffer sql2 = new StringBuffer("update jm_duty set zt='0' where zt='1' and xgsj in " +
                "(select max(xgsj) from jm_duty  where zbrbmbh=? ) ");
        this.jdbcTemplate.update(sql2.toString(),dwdm);

        String zbrbh = conditions.get("ZBRBH").toString();
        //下一个值班人的状态
        StringBuffer sql3 = new StringBuffer("update jm_duty set zt='1' where 1=1 and ZBRBH=?  " +
                "and xgsj in (select max(xgsj) from jm_duty where zbrbmbh=? )");

        this.jdbcTemplate.update(sql3.toString(),zbrbh,dwdm);

        String ZBRMC = "";
        String zbrdh = "";
        try {
            ZBRMC = URLDecoder.decode(conditions.get("ZBRMC").toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (conditions.get("ZBRDH") != "") {
            zbrdh = conditions.get("ZBRDH").toString();
        }
        String id = this.jdbcTemplate.queryForObject("select SEQ_BK_SIGN.NEXTVAL from dual", String.class);
        List v1 = new ArrayList<>();
        String insertSql = " insert into jm_bk_sign(id,zbrbh,zbrmc,qdkssj,qdr,dwdm,zbrdh) values(?,?,?,sysdate,?,?,?)";
        v1.add(id);
        v1.add(zbrbh);
        v1.add(ZBRMC);
        v1.add(conditions.get("QDR"));
        v1.add(conditions.get("DWDM"));
        v1.add(zbrdh);
        //System.out.println("================="+insertSql);
        this.jdbcTemplate.update(insertSql,param.toArray());
        return sign;
    }

    public String getTime() {
        String timeSql = "select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual";
        return this.jdbcTemplate.queryForObject(timeSql, String.class);

    }

    public List<Duty> editZblist(String glbm) throws Exception {
        String sql = "select zbrmc,zbrbh,zt,fpkssj,fpjssj from jm_duty where xgsj in (select max" +
                "(xgsj) from jm_duty) and zbrbmbh=?";
        return this.queryForList(sql, new Object[]{glbm},Duty.class);
    }

    public void updateCountWork(String id, int y_bcksp, int y_yjqs, int y_xdzl,
                                int y_zlfk) {
        List param = new ArrayList<>();
        String insertSql =
                " update jm_bk_sign set bcksps=?, yjqss=?, zlxds=?, zlfks=? where " +
                        "id=?";
        param.add(y_bcksp);
        param.add(y_yjqs);
        param.add(y_xdzl);
        param.add(y_zlfk);
        param.add(id);

        this.jdbcTemplate.update(insertSql,param.toArray());
    }


    //获取值班人信息
    public List dutyInfo() {
        String sql = " select * from jm_duty where zt='1' ";
        List list = this.jdbcTemplate.queryForList(sql);

        return list;
    }
}
