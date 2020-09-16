package com.sunshine.monitor.system.susp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.Menu;
import com.sunshine.monitor.comm.dao.MenuDao;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.AbstractLobCreatingPreparedStatementCallbackImpl;
import com.sunshine.monitor.comm.util.orm.SqlUtils;
import com.sunshine.monitor.comm.util.orm.bean.PreSqlEntry;
import com.sunshine.monitor.system.activemq.bean.TransSusp;
import com.sunshine.monitor.system.activemq.bean.TransSuspmonitor;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.GkSuspinfoApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;
import com.sunshine.monitor.system.susp.dao.SuspinfoDao;

@Repository
public class SuspinfoDaoImpl extends BaseDaoImpl implements SuspinfoDao {
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private SystemDao systemDao;

    public SuspinfoDaoImpl() {
        setTableName("VEH_SUSPINFO");
    }

    public Map<String, Object> querySuspinfoByPage(Map<String, Object> condition)
            throws Exception {
        List param = new ArrayList<>();
        StringBuffer sql = new StringBuffer(50);
        sql.append("select bkxh, bklb, hpzl, hphm, bksj, bkqssj, bkjzsj, jlzt, ywzt,lar,ladw from VEH_SUSPINFO where 1=1 ");
        Set<Entry<String, Object>> set = condition.entrySet();
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
                if ("bkkssj".equals(key)) {
                    sql.append(" and BKSJ >= ").append(
                            "to_date(?,'yyyy-mm-dd hh24:mi:ss')");
                    param.add(value);
                } else if ("bkjssj".equals(key)) {
                    sql.append(" and BKSJ <= ").append(
                            "to_date(?,'yyyy-mm-dd hh24:mi:ss')");
                    param.add(value);
                } else {
                    sql.append(" and ?=?");
                    param.add(key);
                    param.add(value);
                }
            } else {
                continue;
            }
        }
        sql.append(" order by ");
        sql.append(condition.get("sort"));
        sql.append(" ");
        sql.append(condition.get("order"));
        Map<String, Object> map = this.getSelf().findPageForMap(sql.toString(), param.toArray(),
                Integer.parseInt(condition.get("page").toString()),
                Integer.parseInt(condition.get("rows").toString()));
        return map;
    }

    public Map<String, Object> querySuspinfoByPage(
            Map<String, Object> condition, Class<?> clazz) throws Exception {
        List param = new ArrayList<>();
        StringBuffer sql = new StringBuffer(50);
        int curPage = 1;
        int pageSize = 10;
        sql
                .append("select * from VEH_SUSPINFO where 1=1 ");
        Set<Entry<String, Object>> set = condition.entrySet();
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
                if ("bkkssj".equals(key)) {
                    sql.append(" and BKSJ >= ").append(
                            "to_date(?,'yyyy-mm-dd hh24:mi:ss')");
                    param.add(value);
                } else if ("bkjssj".equals(key)) {
                    sql.append(" and BKSJ <= ").append(
                            "to_date(?,'yyyy-mm-dd hh24:mi:ss')");
                    param.add(value);
                } else {
                    sql.append(" and ?=?");
                    param.add(key);
                    param.add(value);
                }
            } else {
                continue;
            }
        }
        sql.append(" order by ");
        sql.append(condition.get("sort"));
        sql.append(" ");
        sql.append(condition.get("order"));

        if (condition.containsKey("page")) {
            curPage = Integer.parseInt(condition.get("page").toString());
        }
        if (condition.containsKey("rows")) {
            pageSize = Integer.parseInt(condition.get("rows").toString());
        }

        Map<String, Object> map = this.findPageForMap(sql.toString(), param.toArray(), curPage,
                pageSize, clazz);
        return map;
    }

    public Map<String, Object> getSuspinfoByHphm(String hphm, String hpzl,
                                                 boolean flag) throws Exception {
        StringBuffer sql = new StringBuffer(30);
        sql.append("SELECT * FROM VEH_SUSPINFO WHERE HPHM = ? and HPZL= ? ");
        if (!flag) {
            sql.append(" and bkdl not in('3') and ywzt not in('99')");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", this.queryForList(sql.toString(), new Object[]{hphm, hpzl}, VehSuspinfo.class));
        map.put("sql", sql);
        return map;
    }

    public List checkSuspinfo(String hphm) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from veh_suspinfo where hphm =? and bkdl='3' ");
        return this.queryList(sql.toString(), new Object[]{hphm}, VehSuspinfo.class);
    }

    /**
     * Save and Update suspinfo object 新增或更新布控 更新未实现完...
     * 建议使用saveSuspinfo2
     */
    @Deprecated
    public Object saveSuspinfo(VehSuspinfo bean) throws Exception {
        String _bkxh = "";
        // 返回结果
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String s_ssjz = "SELECT ssjz from frm_department where glbm = ?";
        String _ssjz = (String) this.jdbcTemplate.queryForObject(s_ssjz, new Object[]{bean.getBkjg()},
                String.class);
        if (_ssjz.length() < 1) {
            resultMap.put("code", "-999");
            resultMap.put("msg", "您操作的用户所在的部门缺少警种");
            return resultMap;
        }
        // 更新操作的
        int _susp = 0;
        if (bean.getBkxh() != null && !"".equals(bean.getBkxh())) {
            String s_susp = "SELECT count(1) v_count from veh_suspinfo where bkxh = ?";
            _susp = this.jdbcTemplate.queryForInt(s_susp, new Object[]{bean.getBkxh()});
            if (_susp > 0) {
                // 更新与布控不是同一人
                String s_bkr = "SELECT bkr from veh_suspinfo where bkxh = ?";
                String _bkr = this.jdbcTemplate.queryForObject(s_bkr, new Object[]{bean.getBkxh()},
                        String.class);
                if (_bkr.equals(bean.getBkr())) {
                    resultMap.put("code", "0");
                    resultMap.put("msg", "非法错误:布控与更新不是同一人操作");
                    return resultMap;
                }
            }
            // ...更新....

            // 插入操作(添加新布控数据)
        } else {
            // 获取布控序号
            String s_bkxh = "select SEQ_SUSPINFO_XH.NEXTVAL from dual";
            _bkxh = this.jdbcTemplate.queryForObject(s_bkxh, String.class);
            // 从参数表获取行政区划
            String s_bkpt = "SELECT csz from frm_syspara where gjz = 'xzqh'";
            String _bkpt = this.jdbcTemplate.queryForObject(s_bkpt,
                    String.class);
            if ("1".equals(bean.getMhbkbj()) && "2".equals(bean.getBkfwlx())) {
                resultMap.put("code", "0");
                resultMap.put("msg", "模糊布控不能联动其它地市,布控范围只能在本地");
                return resultMap;
            }
            List param = new ArrayList<>();
            String sql = "INSERT into veh_suspinfo(bkxh, bkdl, bklb, hphm, hpzl, clxh, fdjh, bkjg, "
                    + "bkjgmc, clsyr, cllx, cltz, clsbdh, syrlxdh, syrxxdz, bkqssj, bkjzsj, bkr, bkrmc, "
                    + "bkrjh, bkjglxdh, bkfw, bkfwlx, jyaq, mhbkbj, bksj, gxsj, clpp, csys, bjfs, bkjb, dxjshm,"
                    + "bkxz, bjya, hpys, bkpt, sqsb, ladw, ladwlxdh, lar, ywzt, xxly) "
                    + "Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'YYYY-MM-DD " +
                    "HH24:MI:SS'), to_date(?,'YYYY-MM-DD HH24:MI:SS'),?,?,?,?,?,?,?,?," +
                    " sysdate,sysdate, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,'0')";

            param.add(bean.getBkjg() + _bkxh);
            param.add(bean.getBkdl());
            param.add(bean.getBklb());
            param.add(bean.getHphm());
            param.add(bean.getHpzl());
            if (bean.getClxh() != null) {
                param.add(bean.getClxh());
            } else {
                param.add("''");
            }
            if (bean.getFdjh() != null) {
                param.add(bean.getFdjh());
            } else {
                param.add("''");
            }
            param.add(bean.getBkjg());
            param.add(bean.getBkjgmc());
            if (bean.getClsyr() != null) {
                param.add(bean.getClsyr());
            } else {
                param.add("''");
            }
            if (bean.getCllx() != null) {
                param.add(bean.getCllx());
            } else {
                param.add("''");
            }
            if (bean.getCltz() != null) {
                param.add(bean.getCltz());
            } else {
                param.add("''");
            }
            if (bean.getClsbdh() != null) {
                param.add(bean.getClsbdh());
            } else {
                param.add("''");
            }
            if (bean.getSyrlxdh() != null) {
                param.add(bean.getSyrlxdh());
            } else {
                param.add("''");
            }
            if (bean.getSyrxxdz() != null) {
                param.add(bean.getSyrxxdz());
            } else {
                param.add("''");
            }
            param.add(bean.getBkqssj());
            param.add(bean.getBkjzsj());
            param.add(bean.getBkr());
            if (bean.getBkrmc() != null) {
                param.add(bean.getBkrmc());
            } else {
                param.add("''");
            }
            param.add(bean.getBkrjh());
            param.add(bean.getBkjglxdh());
            if (bean.getBkfw() != null) {
                param.add(bean.getBkfw());
            } else {
                param.add("''");
            }
            param.add(bean.getBkfwlx());
            param.add(bean.getJyaq());
            param.add(bean.getMhbkbj());
            if (bean.getClpp() != null) {
                param.add(bean.getClpp());
            } else {
                param.add("''");
            }
            if (bean.getCsys() != null) {
                param.add(bean.getCsys());
            } else {
                param.add("''");
            }
            param.add(bean.getBjfs());
            param.add(bean.getBkjb());
            if (bean.getDxjshm() != null) {
                param.add(bean.getDxjshm());
            } else {
                param.add("''");
            }
            param.add(bean.getBkxz());
            param.add(bean.getBjya());
            if (bean.getHpys() != null) {
                param.add(bean.getHpys());
            } else {
                param.add("''");
            }
            param.add(_bkpt);
            param.add(bean.getSqsb());
            param.add(bean.getLadw());
            param.add(bean.getLadwlxdh());
            param.add(bean.getLar());
            param.add(bean.getYwzt());

            int c = this.jdbcTemplate.update(sql, param.toArray());

            if (c > 0) {
                resultMap.put("code", "1");
                resultMap.put("msg", "新增布控执行成功!");
                String temp = bean.getBkjg() + _bkxh;
                bean.setBkxh(temp);
                bean.setBkrdwjz(_ssjz);
                resultMap.put("suspinfo", bean);
            }
        }
        return resultMap;
    }


    public int saveSuspinfo2(VehSuspinfo bean) throws Exception {
        List param = new ArrayList<>();
        String sql = "INSERT into veh_suspinfo(bkxh, bkdl, bklb, hphm, hpzl, clxh, fdjh, bkjg, "
                + "bkjgmc, clsyr, cllx, cltz, clsbdh, syrlxdh, syrxxdz, bkqssj, bkjzsj, bkr, bkrmc, "
                + "bkrjh, bkjglxdh, bkfw, bkfwlx, jyaq, mhbkbj, bksj, gxsj, clpp, csys, bjfs, bkjb, dxjshm,"
                + "bkxz, bjya, hpys, bkpt, sqsb, ladw, ladwlxdh, lar, ywzt, xxly) "
                + "Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'YYYY-MM-DD " +
                "HH24:MI:SS'), to_date(?,'YYYY-MM-DD HH24:MI:SS'),?,?,?,?,?,?,?,?," +
                " sysdate,sysdate, ?,?,?,?,?,?,?,?,?,?,?,?,?,'0')";

        param.add(bean.getBkxh());
        param.add(bean.getBkdl());
        param.add(bean.getBklb());
        param.add(bean.getHphm());
        param.add(bean.getHpzl());
        if (bean.getClxh() != null) {
            param.add(bean.getClxh());
        } else {
            param.add("''");
        }
        if (bean.getFdjh() != null) {
            param.add(bean.getFdjh());
        } else {
            param.add("''");
        }
        param.add(bean.getBkjg());
        param.add(bean.getBkjgmc());
        if (bean.getClsyr() != null) {
            param.add(bean.getClsyr());
        } else {
            param.add("''");
        }
        if (bean.getCllx() != null) {
            param.add(bean.getCllx());
        } else {
            param.add("''");
        }
        if (bean.getCltz() != null) {
            param.add(bean.getCltz());
        } else {
            param.add("''");
        }
        if (bean.getClsbdh() != null) {
            param.add(bean.getClsbdh());
        } else {
            param.add("''");
        }
        if (bean.getSyrlxdh() != null) {
            param.add(bean.getSyrlxdh());
        } else {
            param.add("''");
        }
        if (bean.getSyrxxdz() != null) {
            param.add(bean.getSyrxxdz());
        } else {
            param.add("''");
        }
        param.add(bean.getBkqssj());
        param.add(bean.getBkjzsj());
        param.add(bean.getBkr());
        if (bean.getBkrmc() != null) {
            param.add(bean.getBkrmc());
        } else {
            param.add("''");
        }
        param.add(bean.getBkrjh());
        param.add(bean.getBkjglxdh());
        if (bean.getBkfw() != null) {
            param.add(bean.getBkfw());
        } else {
            param.add("''");
        }
        param.add(bean.getBkfwlx());
        param.add(bean.getJyaq());
        param.add(bean.getMhbkbj());
        if (bean.getClpp() != null) {
            param.add(bean.getClpp());
        } else {
            param.add("''");
        }
        if (bean.getCsys() != null) {
            param.add(bean.getCsys());
        } else {
            param.add("''");
        }
        param.add(bean.getBjfs());
        param.add(bean.getBkjb());
        if (bean.getDxjshm() != null) {
            param.add(bean.getDxjshm());
        } else {
            param.add("''");
        }
        param.add(bean.getBkxz());
        param.add(bean.getBjya());
        if (bean.getHpys() != null) {
            param.add(bean.getHpys());
        } else {
            param.add("''");
        }
        param.add(bean.getSqsb());
        param.add(bean.getLadw());
        param.add(bean.getLadwlxdh());
        param.add(bean.getLar());
        param.add(bean.getYwzt());

        int c = 0;
        try {
            c = this.jdbcTemplate.update(sql, param.toArray());
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        return c;
    }

    public Object updateSuspinfo(VehSuspinfo bean) throws Exception {
        // 请继续添加
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List param = new ArrayList<>();
        String sql = "update VEH_SUSPINFO set ";
        if (bean.getBjzt() != null) {
            sql = sql + " bjzt=?,";
            param.add(bean.getBjzt());
        }

        sql = sql + " bjsj=to_date(?,'YYYY-MM-DD HH24:MI:SS')" + " WHERE bkxh=?";
        param.add(bean.getBjsj());
        param.add(bean.getBkxh());
        int c = this.jdbcTemplate.update(sql, param.toArray());
        if (c > 0) {
            resultMap.put("code", "1");
            resultMap.put("msg", "更新布控执行成功!");

        }
        return resultMap;

    }

    /**
     * Save suspinfo object 新增布控(联动)
     */
    public int saveSuspinfoLink(TransSusp bean) throws Exception {
        String sql = "";
        List param = new ArrayList<>();
        String s_susp = "SELECT count(1) v_count from veh_suspinfo where bkxh = ?";
        int _susp = this.jdbcTemplate.queryForInt(s_susp, bean.getBkxh());
        if (_susp > 0) {
            if (bean.getJlzt().equals("1")) {// 记录状态为"布控"
                sql = "UPDATE VEH_SUSPINFO SET BKSJ=to_date(?,'YYYY-MM-DD HH24:MI:SS')," + " " +
                        "BKJZSJ=to_date(?,'YYYY-MM-DD HH24:MI:SS'),"
                        + " gxsj=sysdate" + " where bkxh=?";
                param.add(bean.getBksj());
                param.add(bean.getBkjzsj());
                param.add(bean.getBkxh());
            }

        } else {
            sql = "INSERT into veh_suspinfo(bkxh, bkdl, bklb, hphm, hpzl, clxh, fdjh, bkjg, "
                    + "bkjgmc, clsyr, cllx, cltz, clsbdh, syrlxdh, syrxxdz, bkqssj, bkjzsj, bkr, bkrmc, "
                    + "bkrjh, bkjglxdh, bkfw, bkfwlx, jyaq, mhbkbj, bksj, gxsj, clpp, csys, bjfs, bkjb, dxjshm,"
                    + "bkxz, bjya, hpys, bkpt, sqsb, ladw, ladwlxdh, lar, ywzt, xxly, jlzt, ysbh) "
                    + "Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'YYYY-MM-DD " +
                    "HH24:MI:SS'), to_date(?,'YYYY-MM-DD HH24:MI:SS'),?,?,?,?,?,?,?,?, " +
                    " sysdate,sysdate, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            param.add(bean.getBkxh());
            if (bean.getBkdl() != null) {
                param.add(bean.getBkdl());
            } else {
                param.add("''");
            }
            if (bean.getBklb() != null) {
                param.add(bean.getBklb());
            } else {
                param.add("''");
            }
            if (bean.getHphm() != null) {
                param.add(bean.getHphm());
            } else {
                param.add("''");
            }
            if (bean.getHpzl() != null) {
                param.add(bean.getHpzl());
            } else {
                param.add("''");
            }
            if (bean.getClxh() != null) {
                param.add(bean.getClxh());
            } else {
                param.add("''");
            }
            if (bean.getFdjh() != null) {
                param.add(bean.getFdjh());
            } else {
                param.add("''");
            }
            if (bean.getBkjg() != null) {
                param.add(bean.getBkjg());
            } else {
                param.add("''");
            }
            if (bean.getBkjgmc() != null) {
                param.add(bean.getBkjgmc());
            } else {
                param.add("''");
            }
            if (bean.getClsyr() != null) {
                param.add(bean.getClsyr());
            } else {
                param.add("''");
            }
            if (bean.getCllx() != null) {
                param.add(bean.getCllx());
            } else {
                param.add("''");
            }
            if (bean.getCltz() != null) {
                param.add(bean.getCltz());
            } else {
                param.add("''");
            }
            if (bean.getClsbdh() != null) {
                param.add(bean.getClsbdh());
            } else {
                param.add("''");
            }
            if (bean.getSyrlxdh() != null) {
                param.add(bean.getSyrlxdh());
            } else {
                param.add("''");
            }
            if (bean.getSyrxxdz() != null) {
                param.add(bean.getSyrxxdz());
            } else {
                param.add("''");
            }
            param.add(bean.getBkqssj());
            param.add(bean.getBkjzsj());
            if (bean.getBkr() != null) {
                param.add(bean.getBkr());
            } else {
                param.add("''");
            }
            if (bean.getBkrmc() != null) {
                param.add(bean.getBkrmc());
            } else {
                param.add("''");
            }
            if (bean.getBkrjh() != null) {
                param.add(bean.getBkrjh());
            } else {
                param.add("''");
            }
            if (bean.getBkjglxdh() != null) {
                param.add(bean.getBkjglxdh());
            } else {
                param.add("''");
            }
            if (bean.getBkfw() != null) {
                param.add(bean.getBkfw());
            } else {
                param.add("''");
            }
            if (bean.getBkfwlx() != null) {
                param.add(bean.getBkfwlx());
            } else {
                param.add("''");
            }
            if (bean.getJyaq() != null) {
                param.add(bean.getJyaq());
            } else {
                param.add("''");
            }
            if (bean.getMhbkbj() != null) {
                param.add(bean.getMhbkbj());
            } else {
                param.add("''");
            }
            if (bean.getClpp() != null) {
                param.add(bean.getClpp());
            } else {
                param.add("''");
            }
            if (bean.getCsys() != null) {
                param.add(bean.getCsys());
            } else {
                param.add("''");
            }
            if (bean.getBjfs() != null) {
                param.add(bean.getBjfs());
            } else {
                param.add("''");
            }
            if (bean.getBkjb() != null) {
                param.add(bean.getBkjb());
            } else {
                param.add("''");
            }
            if (bean.getDxjshm() != null) {
                param.add(bean.getDxjshm());
            } else {
                param.add("''");
            }
            if (bean.getBkxz() != null) {
                param.add(bean.getBkxz());
            } else {
                param.add("''");
            }
            if (bean.getBjya() != null) {
                param.add(bean.getBjya());
            } else {
                param.add("''");
            }
            if (bean.getHpys() != null) {
                param.add(bean.getHpys());
            } else {
                param.add("''");
            }
            if (bean.getBkpt() != null) {
                param.add(bean.getBkpt());
            } else {
                param.add("''");
            }
            if (bean.getSqsb() != null) {
                param.add(bean.getSqsb());
            } else {
                param.add("''");
            }
            if (bean.getLadw() != null) {
                param.add(bean.getLadw());
            } else {
                param.add("''");
            }
            if (bean.getLadwlxdh() != null) {
                param.add(bean.getLadwlxdh());
            } else {
                param.add("''");
            }
            if (bean.getLar() != null) {
                param.add(bean.getLar());
            } else {
                param.add("''");
            }
            if (bean.getYwzt() != null) {
                param.add(bean.getYwzt());
            } else {
                param.add("''");
            }
            if (bean.getJlzt() != null) {
                param.add(bean.getJlzt());
            } else {
                param.add("''");
            }
            if (bean.getYsbh() != null) {
                param.add(bean.getYsbh());
            } else {
                param.add("''");
            }
        }
        return this.jdbcTemplate.update(sql, param.toArray());

    }

    /**
     * Save suspinfo object 新增布控(联动)—省厅
     */
    public int saveSuspinfoLinkST(TransSusp bean) throws Exception {
        String sql = "";
        int returnVal = 0;
        List param = new ArrayList<>();
        String s_susp = "SELECT count(1) v_count from veh_suspinfo where bkxh = ?";
        int _susp = this.jdbcTemplate.queryForInt(s_susp,bean.getBkxh());
        if (_susp > 0) {
            if (bean.getJlzt().equals("1")) {// 记录状态为"布控"
                sql = "UPDATE VEH_SUSPINFO SET BKSJ=to_date(?,'YYYY-MM-DD HH24:MI:SS')," + " " +
                        "BKJZSJ=to_date(?,'YYYY-MM-DD HH24:MI:SS'),"
                        + " gxsj=sysdate" + " where bkxh=?";
                param.add(bean.getBksj());
                param.add(bean.getBkjzsj());
                param.add(bean.getBkxh());
                returnVal = 1;
            } else {
                log.info("布控序号：" + bean.getBkxh() + ",该布控已被撤控");
                return -1;
            }

        } else {
            sql = "INSERT into veh_suspinfo(bkxh, bkdl, bklb, hphm, hpzl, clxh, fdjh, bkjg, "
                    + "bkjgmc, clsyr, cllx, cltz, clsbdh, syrlxdh, syrxxdz, bkqssj, bkjzsj, bkr, bkrmc, "
                    + "bkrjh, bkjglxdh, bkfw, bkfwlx, jyaq, mhbkbj, bksj, gxsj, clpp, csys, bjfs, bkjb, dxjshm,"
                    + "bkxz, bjya, hpys, bkpt, sqsb, ladw, ladwlxdh, lar, ywzt, xxly, jlzt, ysbh) "
                    + "Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'YYYY-MM-DD " +
                    "HH24:MI:SS'), to_date(?,'YYYY-MM-DD HH24:MI:SS'),?,?,?,?,?,?,?,?, " +
                    " sysdate,sysdate, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            param.add(bean.getBkxh());
            if (bean.getBkdl() != null) {
                param.add(bean.getBkdl());
            } else {
                param.add("''");
            }
            if (bean.getBklb() != null) {
                param.add(bean.getBklb());
            } else {
                param.add("''");
            }
            if (bean.getHphm() != null) {
                param.add(bean.getHphm());
            } else {
                param.add("''");
            }
            if (bean.getHpzl() != null) {
                param.add(bean.getHpzl());
            } else {
                param.add("''");
            }
            if (bean.getClxh() != null) {
                param.add(bean.getClxh());
            } else {
                param.add("''");
            }
            if (bean.getFdjh() != null) {
                param.add(bean.getFdjh());
            } else {
                param.add("''");
            }
            if (bean.getBkjg() != null) {
                param.add(bean.getBkjg());
            } else {
                param.add("''");
            }
            if (bean.getBkjgmc() != null) {
                param.add(bean.getBkjgmc());
            } else {
                param.add("''");
            }
            if (bean.getClsyr() != null) {
                param.add(bean.getClsyr());
            } else {
                param.add("''");
            }
            if (bean.getCllx() != null) {
                param.add(bean.getCllx());
            } else {
                param.add("''");
            }
            if (bean.getCltz() != null) {
                param.add(bean.getCltz());
            } else {
                param.add("''");
            }
            if (bean.getClsbdh() != null) {
                param.add(bean.getClsbdh());
            } else {
                param.add("''");
            }
            if (bean.getSyrlxdh() != null) {
                param.add(bean.getSyrlxdh());
            } else {
                param.add("''");
            }
            if (bean.getSyrxxdz() != null) {
                param.add(bean.getSyrxxdz());
            } else {
                param.add("''");
            }
            param.add(bean.getBkqssj());
            param.add(bean.getBkjzsj());
            if (bean.getBkr() != null) {
                param.add(bean.getBkr());
            } else {
                param.add("''");
            }
            if (bean.getBkrmc() != null) {
                param.add(bean.getBkrmc());
            } else {
                param.add("''");
            }
            if (bean.getBkrjh() != null) {
                param.add(bean.getBkrjh());
            } else {
                param.add("''");
            }
            if (bean.getBkjglxdh() != null) {
                param.add(bean.getBkjglxdh());
            } else {
                param.add("''");
            }
            if (bean.getBkfw() != null) {
                param.add(bean.getBkfw());
            } else {
                param.add("''");
            }
            if (bean.getBkfwlx() != null) {
                param.add(bean.getBkfwlx());
            } else {
                param.add("''");
            }
            if (bean.getJyaq() != null) {
                param.add(bean.getJyaq());
            } else {
                param.add("''");
            }
            if (bean.getMhbkbj() != null) {
                param.add(bean.getMhbkbj());
            } else {
                param.add("''");
            }
            if (bean.getClpp() != null) {
                param.add(bean.getClpp());
            } else {
                param.add("''");
            }
            if (bean.getCsys() != null) {
                param.add(bean.getCsys());
            } else {
                param.add("''");
            }
            if (bean.getBjfs() != null) {
                param.add(bean.getBjfs());
            } else {
                param.add("''");
            }
            if (bean.getBkjb() != null) {
                param.add(bean.getBkjb());
            } else {
                param.add("''");
            }
            if (bean.getDxjshm() != null) {
                param.add(bean.getDxjshm());
            } else {
                param.add("''");
            }
            if (bean.getBkxz() != null) {
                param.add(bean.getBkxz());
            } else {
                param.add("''");
            }
            if (bean.getBjya() != null) {
                param.add(bean.getBjya());
            } else {
                param.add("''");
            }
            if (bean.getHpys() != null) {
                param.add(bean.getHpys());
            } else {
                param.add("''");
            }
            if (bean.getBkpt() != null) {
                param.add(bean.getBkpt());
            } else {
                param.add("''");
            }
            if (bean.getSqsb() != null) {
                param.add(bean.getSqsb());
            } else {
                param.add("''");
            }
            if (bean.getLadw() != null) {
                param.add(bean.getLadw());
            } else {
                param.add("''");
            }
            if (bean.getLadwlxdh() != null) {
                param.add(bean.getLadwlxdh());
            } else {
                param.add("''");
            }
            if (bean.getLar() != null) {
                param.add(bean.getLar());
            } else {
                param.add("''");
            }
            if (bean.getYwzt() != null) {
                param.add(bean.getYwzt());
            } else {
                param.add("''");
            }
            if (bean.getJlzt() != null) {
                param.add(bean.getJlzt());
            } else {
                param.add("''");
            }
            if (bean.getYsbh() != null) {
                param.add(bean.getYsbh());
            } else {
                param.add("''");
            }

            returnVal = 2;
        }
        this.jdbcTemplate.update(sql,param.toArray());
        return returnVal;

    }

    /**
     * 联动布控状态监测
     */
    public int saveSuspinfomonitorLink(TransSuspmonitor bean) throws Exception {
        String sql = "";
        List param = new ArrayList();
        sql = "INSERT into jm_suspinfo_monitor(bkxh,dwdm,bkjg,lrsj) "
                + "Values(?, ?,?, sysdate)";
        param.add(bean.getBkxh());
        param.add(bean.getDwdm());
        param.add(bean.getBkjg());

        return this.jdbcTemplate.update(sql,param.toArray());

    }

    /**
     * 各地市撤控(联动)
     */
    public int saveCsuspinfoLink(TransSusp bean) throws Exception {
		/*
		String sql = "select * from veh_suspinfo where bkxh = '"
				+ bean.getBkxh() + "'";
		List<VehSuspinfo> l = this.queryForList(sql, VehSuspinfo.class);
		*/
        String sql = "select * from veh_suspinfo where bkxh = ?";
        List<VehSuspinfo> l = this.queryForList(sql, new Object[]{bean.getBkxh()}, VehSuspinfo.class);
        if (l.size() > 0) {
            VehSuspinfo suspinfo = l.get(0);
            // VehSuspinfo suspinfo = this.queryForObject(sql,
            // VehSuspinfo.class);
            if (suspinfo != null) {
                List param = new ArrayList<>();
                if (!(suspinfo.getJlzt().equals("2") && suspinfo.getYwzt()
                        .equals("99"))) {
                    sql =
                            " UPDATE VEH_SUSPINFO SET CXSQR=?," + " CXSQRJH=?,CXSQRMC=?,"
                            + " CXSQDW=?,CXSQDWMC=?," + " CXSQSJ=to_date(?,'yyyy-mm-dd " +
                                    "hh24:mi:ss'),CKYYDM=?," + " CKYYMS=?,YWZT=?," + " JLZT=?," +
                                    "GXSJ=sysdate " + " where bkxh=?";
                    param.add(bean.getCxsqr());
                    param.add(bean.getCxsqrjh());
                    param.add(bean.getCxsqrmc());
                    param.add(bean.getCxsqdw());
                    param.add(bean.getCxsqdwmc());
                    param.add(bean.getCxsqsj());
                    param.add(bean.getCkyydm());
                    param.add(bean.getYwzt());
                    param.add(bean.getCkyyms());
                    param.add(bean.getJlzt());
                    param.add(bean.getBkxh());


                    return this.jdbcTemplate.update(sql, param.toArray());
                }
            }
        }

        return -1;
        /*
         * String sql = ""; String s_susp =
         * "SELECT count(1) v_count from veh_suspinfo where bkxh = '" +
         * bean.getBkxh() + "'"; int _susp =
         * this.jdbcTemplate.queryForInt(s_susp); if (_susp > 0) {
         * if(!(bean.getJlzt().equals("2") && bean.getYwzt().equals("99"))){ sql
         * = " UPDATE VEH_SUSPINFO SET CXSQR='"+bean.getCxsqr()+"',"
         * +" CXSQRJH='"+bean.getCxsqrjh()+"',CXSQRMC='"+bean.getCxsqrmc()+"',"
         * +" CXSQDW='"+bean.getCxsqdw()+"',CXSQDWMC='"+bean.getCxsqdwmc()+"',"
         * +" CXSQSJ=to_date('"+bean.getCxsqsj()+
         * "','yyyy-mm-dd hh24:mi:ss'),CKYYDM='"+bean.getCkyydm()+"',"
         * +" CKYYMS='"+bean.getCkyyms()+"',YWZT='"+bean.getYwzt()+"',"
         * +" JLZT='"+bean.getJlzt()+"',GXSJ=sysdate "
         * +" where bkxh='"+bean.getBkxh()+"'"; } } return
         * this.jdbcTemplate.update(sql);
         */

    }

    /**
     * Save suspinfo picture 保存布控图片
     */
    public boolean saveSuspinfopictrue(final VehSuspinfopic vspic)
            throws Exception {
        String _suspic = "SELECT count(1) from susp_picrec where bkxh = ?";
        int cc = this.jdbcTemplate.queryForInt(_suspic,vspic.getBkxh());
        boolean flag = false;
        // 更新操作
        if (cc > 0) {
            String u_sql = "UPDATE susp_picrec set zjlx = ?, zjwj = ? where bkxh = ?";
            AbstractLobCreatingPreparedStatementCallbackImpl callBack = new AbstractLobCreatingPreparedStatementCallbackImpl(
                    this.lobHandler) {
                public void setValues(PreparedStatement pstmt,
                                      LobCreator lobCreator) throws SQLException,
                        DataAccessException {
                    VehSuspinfopic picture = (VehSuspinfopic) this
                            .getParameterObject();
                    lobCreator.setBlobAsBytes(pstmt, 2, picture.getZjwj());
                    pstmt.setString(1, picture.getZjlx());
                    pstmt.setString(3, picture.getBkxh());
                }
            };
            callBack.setParameterObject(vspic);
            int c = this.jdbcTemplate.execute(u_sql, callBack);
            flag = (c < 1) ? false : true;
        } else {
            // 添加新记录
            String c_sql = "insert into SUSP_PICREC(xh, bkxh, zjwj, zjlx)values(SEQ_PICREC_XH.nextval,?,?,?)";
            int c = this.jdbcTemplate.execute(c_sql,
                    new AbstractLobCreatingPreparedStatementCallbackImpl(
                            lobHandler) {
                        public void setValues(PreparedStatement pstmt,
                                              LobCreator lobCreator) throws SQLException,
                                DataAccessException {
                            pstmt.setString(1, vspic.getBkxh());
                            lobCreator
                                    .setBlobAsBytes(pstmt, 2, vspic.getZjwj());
                            pstmt.setString(3, vspic.getZjlx());
                        }
                    });
            flag = (c < 1) ? false : true;
            // throw new IllegalDataException("数据出错!");
        }
        return flag;
    }

    public VehSuspinfo getSuspinfoDetail(String bkxh) throws Exception {
        String sql = "select BKXH, YSBH, HPHM, HPZL, BKDL, BKLB, BKQSSJ, BKJZSJ, JYAQ, BKFWLX, BKFW, BKJB, BKXZ, SQSB, BJYA, BJFS, DXJSHM, LAR, LADW, LADWLXDH, CLPP, HPYS, CLXH, CLLX, CSYS, CLSBDH, FDJH, CLTZ, CLSYR, SYRLXDH, SYRXXDZ, BKR, BKJG, BKJGMC, BKJGLXDH, BKSJ, CXSQR, CXSQDW, CXSQDWMC, CXSQSJ, CKYYDM, CKYYMS, YWZT, JLZT, GXSJ, XXLY, BKPT, BY1, BY2, BY3, BY4, BY5, BJZT, MHBKBJ, CXSQRJH, BKRJH, BJSJ, CXSQRMC, BKRMC from veh_suspinfo where  bkxh = ?";
        List<VehSuspinfo> list = this.queryForList(sql, new Object[]{bkxh}, VehSuspinfo.class);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public VehSuspinfo getCitySuspDetail(String bkxh, String cityname) throws Exception {
        String sql = "select * from veh_suspinfo";
        if (cityname != null) {
            sql += "@" + cityname;
        }
		/*
		sql+=" where bkxh = '" + bkxh + "'";
		List<VehSuspinfo> list = this.queryForList(sql, VehSuspinfo.class);
		*/
        sql += " where bkxh = ?";
        List<VehSuspinfo> list = this.queryForList(sql, new Object[]{bkxh}, VehSuspinfo.class);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public TransSusp getTransSuspDetail(String bkxh) throws Exception {

        String sql = "select * from veh_suspinfo where bkxh = ?";
        TransSusp transSusp = this.queryForObject(sql, new Object[]{bkxh},TransSusp.class);
        String sql2 = "select * from audit_approve where bkxh= ?";
        List<AuditApprove> list = this.queryForList(sql2, new Object[]{bkxh},AuditApprove.class);
        if (list.size() > 0) {
            transSusp.setAuditList(list);
        }
        return transSusp;
    }

    public VehSuspinfo getVehsuspinfo(String bkxh) throws Exception {
        String sql = "select * from veh_suspinfo where bkxh = ?";
        List<VehSuspinfo> list = this.queryForList(sql, new Object[]{bkxh}, VehSuspinfo.class);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    // --------------------------------------撤控确认-------------------------------------------

    public Map<String, Object> querySuspinfoCancelList(
            Map<String, Object> conditions) throws Exception {
        StringBuffer sql = new StringBuffer(50);
        List param = new ArrayList<>();
        // 445300010300
        sql
                .append("select t.fkbh, t.bkxh, t.bjxh, t.zlxh ,t.bjsj, t.sflj, t.lrsj, t.by1, t.by4, s.hphm, s.bkdl, s.bklb, s.hpzl,s.bkjgmc, s.bkr,s.bkrmc, s.bksj, s.bkjzsj, s.by3, s.jlzt, s.ywzt from veh_alarm_handled t, veh_alarmrec a, "
                        + " veh_suspinfo s where t.bjxh = a.bjxh(+) "
                        + "and t.bkxh = s.bkxh and t.sflj = '1' and t.by1 = '1' and t.by4 is not " +
                        "null and s.BJZT = '1' and s.ywzt = '14' and s.jlzt = '1' and s.bkjg = ?");

        param.add((String) conditions.get("BMDWDM"));
        // sql
        // .append("select h.fkbh, h.bjxh, h.zlxh, s.bkxh, s.hphm, s.hpzl, s.bkdl, s.bklb, s.bkqssj, s.bkjzsj, s.bkr, s.bkjg, s.bkjgmc, s.bksj, s.by3, s.ywzt, s.jlzt, s.bjzt, h.bjdwdm, h.bjsj, h.by1, h.by4 from (select distinct fkbh, bkxh, bjxh, zlxh, bjdwdm, bjsj, by1, by4 from veh_alarm_handled where sflj = '1' and by1 = '1' and by4 is not null) h, veh_suspinfo s where h.bkxh = s.bkxh and s.ywzt = '14' and s.xxly = '0' and s.BJZT = '1' and s.jlzt = '1' and (s.by3 = '0' or s.by3 is null) ");
        // sql.append(" and h.BJDWDM = '" + (String) conditions.get("BJDWDM")
        // + "'");
        Set<Entry<String, Object>> set = conditions.entrySet();
        Iterator<Entry<String, Object>> it = set.iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            // 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
            String key = entry.getKey();
            String value = (String) entry.getValue();
            boolean isFilter = "sort".equalsIgnoreCase(key)
                    || "order".equalsIgnoreCase(key)
                    || "page".equalsIgnoreCase(key)
                    || "rows".equalsIgnoreCase(key);
            if (!isFilter) {
                if (key.indexOf("qrzt") != -1) {
                    if ("0".equals(value)) {
                        sql.append(" and (s.by3 = '0' or s.by3 is null)");
                    } else {
                        sql.append(" and s.by3 = '1' ");
                    }
                } else if (key.indexOf("kssj") != -1) {
                    String[] temps = key.split("_");
                    if (temps.length != 2)
                        continue;
                    sql.append(" and ? >= ").append(
                            "to_date(?,'yyyy-mm-dd hh24:mi:ss')");
                    param.add("s."+temps[1]);
                    param.add(value);
                } else if (key.indexOf("jssj") != -1) {
                    String[] temps = key.split("_");
                    if (temps.length != 2)
                        continue;
                    sql.append(" and ? <= ").append(
                            "to_date(?,'yyyy-mm-dd hh24:mi:ss')");
                    param.add("s."+temps[1]);
                    param.add(value);
                } else {
                    if ("BMDWDM".equalsIgnoreCase(key))
                        continue;
                    sql.append(" and ? = ?");
                    param.add("s."+key);
                    param.add(value);
                }
            }
        }
        sql.append(" order by ? ?");
        param.add("t."+conditions.get("sort"));
        param.add(conditions.get("order"));

        //System.out.println("布控审批人进入拦截确认sql:"+sql.toString());
        Map<String, Object> map = this.getSelf().findPageForMap(sql.toString(),param.toArray(),
                Integer.parseInt(conditions.get("page").toString()),
                Integer.parseInt(conditions.get("rows").toString()));
        return map;
    }

    public Map<String, Object> querySuspinfoCancelListForZx(
            Map<String, Object> conditions) {
        List param = new ArrayList<>();
        StringBuffer sql = new StringBuffer(50);
        // 445300010300
        sql
                .append("select t.fkbh, t.bkxh, t.bjxh, t.zlxh ,t.bjsj, t.sflj, t.lrsj, t.by1, t.by4, s.hphm, s.bkdl, s.bklb,s.bkjgmc, s.hpzl, s.bkr,s.bkrmc, s.bksj, s.bkjzsj, s.by3, s.jlzt, s.ywzt from veh_alarm_handled t, veh_alarmrec a, "
                        + " veh_suspinfo s where t.bjxh = a.bjxh(+)  "
                        + "and t.bkxh = s.bkxh and t.sflj = '1' and t.by1 = '1' and t.by4 is not null and s.BJZT = '1' and s.ywzt = '14' and s.jlzt = '1'  ");

        Set<Entry<String, Object>> set = conditions.entrySet();
        Iterator<Entry<String, Object>> it = set.iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            // 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
            String key = entry.getKey();
            String value = (String) entry.getValue();
            boolean isFilter = "sort".equalsIgnoreCase(key)
                    || "order".equalsIgnoreCase(key)
                    || "page".equalsIgnoreCase(key)
                    || "rows".equalsIgnoreCase(key);
            if (!isFilter) {
                if (key.indexOf("qrzt") != -1) {
                    if ("0".equals(value)) {
                        sql.append(" and (s.by3 = '0' or s.by3 is null)");
                    } else {
                        sql.append(" and s.by3 = '1' ");
                    }
                } else if (key.indexOf("kssj") != -1) {
                    String[] temps = key.split("_");
                    if (temps.length != 2)
                        continue;
                    sql.append(" and ? >= ").append(
                            "to_date(?,'yyyy-mm-dd hh24:mi:ss')");
                    param.add("s."+temps[1]);
                    param.add(value);
                } else if (key.indexOf("jssj") != -1) {
                    String[] temps = key.split("_");
                    if (temps.length != 2)
                        continue;
                    sql.append(" and ? <= ").append(
                            "to_date(?,'yyyy-mm-dd hh24:mi:ss')");
                    param.add("s."+temps[1]);
                    param.add(value);
                } else {
                    if ("BMDWDM".equalsIgnoreCase(key))
                        continue;
                    sql.append(" and ? = ?");
                    param.add("s." + key);
                    param.add(value);
                }
            }
        }
        sql.append(" order by ? ?");
        param.add("t."+conditions.get("sort"));
        param.add(conditions.get("order"));

        System.out.println("指挥中心进入拦截确认sql:" + sql.toString());
        Map<String, Object> map = this.getSelf().findPageForMap(sql.toString(),param.toArray(),
                Integer.parseInt(conditions.get("page").toString()),
                Integer.parseInt(conditions.get("rows").toString()));
        return map;
    }

    public int saveSuspinfoCancelsign(VehSuspinfo vehSuspinfo) throws Exception {
        String sql = null;
//		if(vehSuspinfo.getBy3().equals("1")){
//			sql = "update veh_suspinfo set by1=to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),ywzt= '99' ,jlzt = '2',by3 = '"
//				+ vehSuspinfo.getBy3()
//				+ "',by4 = '"
//				+ vehSuspinfo.getBy4()
//				+ "',by5 = '"
//				+ vehSuspinfo.getBy5()
//				+ "' where bkxh = '"
//				+ vehSuspinfo.getBkxh().trim() + "'";
//			System.out.println("拦截确认ok，不走撤控申请等流程："+sql);
//		}else{
        List param = new ArrayList<>();
        sql = "update veh_suspinfo set by1=to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),by3 = ?,by4 =" +
                " ?,by5 = ? where bkxh = ?";
        param.add(vehSuspinfo.getBy3());
        param.add(vehSuspinfo.getBy4());
        param.add(vehSuspinfo.getBy5());
        param.add(vehSuspinfo.getBkxh().trim());
        //}
        return this.jdbcTemplate.update(sql,param.toArray());
    }

    public List<VehSuspinfo> querySuspinfo(String subSql) throws Exception {
        String sql = "select * from veh_suspinfo where 1 = 1 ";
        if (subSql != null && !"".equals(subSql)) {
            sql = sql + subSql;
        }
        List<VehSuspinfo> list = this.queryForList(sql, VehSuspinfo.class);
        return list;
    }

    public VehSuspinfo save(VehSuspinfo info) throws Exception {
        // 获取布控序号
        String s_bkxh = "select SEQ_SUSPINFO_XH.NEXTVAL from dual";
        String _bkxh = this.jdbcTemplate.queryForObject(s_bkxh, String.class);
        // 从参数表获取行政区划
        String s_bkpt = "SELECT csz from frm_syspara where gjz = 'xzqh'";
        String _bkpt = this.jdbcTemplate.queryForObject(s_bkpt, String.class);
        info.setBkxh(info.getBkjg() + _bkxh);
        info.setBkpt(_bkpt);
        Map<String, String> defaultFieldSqlMap = new HashMap<String, String>();
        defaultFieldSqlMap.put("gxsj", "sysdate");
        defaultFieldSqlMap.put("gxsj", "sysdate");
        PreSqlEntry preEntry = SqlUtils.getPreInsertSqlByObject(getTableName(),
                info, defaultFieldSqlMap);
        String outStr = "执行SQL:" + preEntry.getSql() + "/value:";
        for (Object value : preEntry.getValues()) {
            outStr += value.toString() + ",";
        }
        log.debug(outStr);
        this.jdbcTemplate.update(preEntry.getSql(), preEntry.getValues()
                .toArray());
        return info;
    }

    public VehSuspinfo update(VehSuspinfo info) throws Exception {
        PreSqlEntry entry = SqlUtils.getPreUpdateSqlByObject(getTableName(),
                info, new HashMap<String, String>(), "bkxh");
        List param = new ArrayList<>();
        StringBuffer strSql = new StringBuffer("Update VEH_SUSPINFO set dxjshm=");
        if (info != null && !info.equals("")) {
            strSql.append("?,cxsqsj=to_date(?,'yyyy" +
                    "-MM-dd HH24:mi:ss'),cxsqrjh=?,ckyyms=?,ywzt=?,cxsqdw" +
                    "=?,ckyydm=?,cxsqdwmc=?,cxsqr=?,jlzt=? Where bkxh=?");
            param.add(info.getDxjshm());
            param.add(info.getCxsqsj());
            param.add(info.getCxsqrjh());
            param.add(info.getCkyyms());
            param.add(info.getYwzt());
            param.add(info.getCxsqdw());
            param.add(info.getCkyydm());
            param.add(info.getCxsqdwmc());
            param.add(info.getCxsqr());
            param.add(info.getJlzt());
            param.add(info.getBkxh());
        }
        this.jdbcTemplate.update(strSql.toString(),param.toArray());
        return info;
    }

    public VehSuspinfo findSuspInfo(String hphm, String ysbh) {
        List param = new ArrayList<>();
        String sql = "Select * from veh_suspinfo";
        VehSuspinfo veh = null;
        if (hphm != null && !"".equals(hphm)) {
            sql += " Where hphm = ?";
            param.add(hphm);
            if (ysbh != null && !"".equals(ysbh)) {
                sql += " and ysbh = ?";
                param.add(ysbh);
            }
        } else {
            sql += "Where ysbh = ?";
            param.add(ysbh);
        }
        List<VehSuspinfo> list = this.queryForList(sql, param.toArray(),VehSuspinfo.class);
        if (list != null && list.size() > 0) {
            veh = (VehSuspinfo) list.get(0);
        }
        return veh;
    }

    public List<VehSuspinfo> getDxhmByHpdm(String hphm) throws Exception {

        String sql = "select bkjglxdh,bkr from veh_suspinfo ";
        sql += "where hphm =? and ywzt<41";

        List<VehSuspinfo> list = this.queryForList(sql, new Object[]{hphm},VehSuspinfo.class);
        return list;
    }

    public String getBkxh(String bkjg) throws Exception {
        String xh = this.jdbcTemplate.queryForObject("select SEQ_SUSPINFO_XH.NEXTVAL from dual", String.class);
        String bkxh = bkjg + xh;
        return bkxh;
    }

    public Map<String, Object> queryHCSuspinfo(Map<String, Object> page, String con)
            throws Exception {
        String sql = "SELECT A.BKXH, A.BJXH, A.HPHM, A.HPZL, A.KDMC, A.FXMC, A.BJSJ, S.BKR, S.BKJG, S.BKSJ, S.BKDL, S.BKLB, G.HCJG FROM VEH_ALARMREC A JOIN VEH_SUSPINFO S ON A.BKXH=S.BKXH LEFT JOIN VEH_GK_SUSPINFO_APPROVE G ON A.BJXH=G.BJXH WHERE A.BJDL='3' AND A.QRZT='1'";
        if (StringUtils.isNotBlank(con))
            sql += con;
        return this.getSelf().findPageForMap(sql,
                Integer.parseInt(page.get("page").toString()),
                Integer.parseInt(page.get("rows").toString()));
    }


    public void insertHCsuspinfo(GkSuspinfoApprove gsf) throws Exception {
        List param = new ArrayList<>();
        String sql = "INSERT INTO VEH_GK_SUSPINFO_APPROVE(BJXH,BKXH,HCR,HCSJ,HCJG,HCNR,HCDW,BY1," +
                "BY2)values(?,?,?,?,?,?,?,?,?)";
        param.add(gsf.getBjxh());
        param.add(gsf.getBkxh());
        param.add(gsf.getHcr());
        param.add(gsf.getHcjg());
        if(gsf.getHcnr()!=null){
            param.add(gsf.getHcnr());
        }else {
            param.add("''");
        }
        param.add(gsf.getBy1());
        if(gsf.getHcnr()!=null){
            param.add(gsf.getBy1());
        }else {
            param.add("''");
        }
        param.add(gsf.getBy2());
        if(gsf.getHcnr()!=null){
            param.add(gsf.getBy2());
        }else {
            param.add("''");
        }

        this.jdbcTemplate.update(sql,param.toArray());
    }

    @Override
    public List<Map<String, Object>> getSuspinfoCountByBkr(String bkjg, String curDate) throws Exception {//
        List param = new ArrayList<>();
        String sql = "select * from ("
                + " select '0' as bzw,count(ywzt) as total  from veh_suspinfo where bkjg=?"
                + " and bksj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')"
                + " and ywzt in ('11','13')"
                + " union all"
                + " select bzw,count(bzw) as total from veh_suspinfo v"
                + " inner join audit_approve a on v.bkxh=a.bkxh"
                + " where v.bkjg=? and czsj >= to_date(?,'yyyy-mm-dd " +
                "hh24:mi:ss') and bzw in('1','2')"
                + " group by a.bzw) x order by x.bzw";
        param.add(bkjg);
        param.add(curDate+"-01 00:00:00");
        param.add(bkjg);
        param.add(curDate+"-01 00:00:00");

        List<Map<String, Object>> datas = this.jdbcTemplate.queryForList(sql,param.toArray());
        return datas;
    }

    @Override
    public List<Map<String, Object>> getSuspinfoCountByBkjg(String roles, String yhdh, String glbm) throws Exception {
        List<Menu> menuList = this.menuDao.findMenuListByRoles(roles);
        List<Map<String, Object>> list = menuDao.getIndexMenuPower("");
        String ywzt = "";
        String ywzt2 = "";
        for (Map<String, Object> map : list) {
            String id = (String) map.get("menuid");
            String status = (String) map.get("status");
            for (Menu menu : menuList) {
                if (id.equals(menu.getId())) {
                    int statusInt = Integer.parseInt(status);
                    if (statusInt == 12) {
                        ywzt2 += status + ",";
                    } else if (statusInt == 42) {
                        ywzt2 += status + ",";
                    } else {
                        ywzt += status + ",";
                    }
                }
            }
        }
        //String tempSql = "";
        if (!"".equals(ywzt)) {
            ywzt = ywzt.substring(0, ywzt.length() - 1);
        } else ywzt = "''";
        if (!"".equals(ywzt2)) {
            ywzt2 = ywzt2.substring(0, ywzt2.length() - 1);
        } else ywzt2 = "''";
        String end = this.systemDao.getSysDate(null, false) + " 23:59:59";
        String begin = this.systemDao.getSysDate("-180", false)
                + " 00:00:00";
        List param = new ArrayList<>();
        String sql = "select jlzt,ywzt,total from (" +
                " select jlzt,ywzt,count(ywzt) as total" +
                " from veh_suspinfo" +
                " where ywzt in (?)" +
                //" and (bkjg = '" + glbm + "'       or cxsqdw= '" + glbm + "')" +
                " and (bkjg in (Select xjjg from frm_prefecture Where dwdm=?)" +
                " or cxsqdw in (Select xjjg from frm_prefecture Where dwdm=?))" +
                " and BKSJ >= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
                " and BKSJ <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
                " group by jlzt, ywzt" +
                " union all" +
                " select jlzt,ywzt,count(ywzt) as total" +
                " from veh_suspinfo" +
                " where ywzt in (?)" +
                " and (bkjg in (Select xjjg from frm_prefecture Where dwdm = ?)" +
                " or cxsqdw in   (Select xjjg from frm_prefecture Where dwdm = ?))" +
                " and BKSJ >= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
                " and BKSJ <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
                " group by jlzt, ywzt) t";
        param.add(ywzt);
        param.add(glbm);
        param.add(glbm);
        param.add(begin);
        param.add(end);
        param.add(ywzt2);
        param.add(glbm);
        param.add(glbm);
        param.add(begin);
        param.add(end);

        List<Map<String, Object>> datas = this.jdbcTemplate.queryForList(sql,param.toArray());
        return datas;
    }

    public boolean isSusp(final VehSuspinfopic vspic)
            throws Exception {
        String _suspic = "SELECT count(1) from susp_picrec where bkxh = ?";

        int cc = this.jdbcTemplate.queryForInt(_suspic,vspic.getBkxh());
        boolean flag = false;
        if (cc > 0) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public boolean updateSuspPic(String sql, AbstractLobCreatingPreparedStatementCallbackImpl callBack)
            throws Exception {
        boolean flag = false;
        int c = this.jdbcTemplate.execute(sql, callBack);
        flag = (c < 1) ? false : true;
        return flag;
    }

    public boolean savePic(final VehSuspinfopic vspic)
            throws Exception {
        boolean flag = false;
        String c_sql = "insert into SUSP_PICREC(xh, bkxh, zjwj, zjlx,clbksqb,clbksqblj,lajds,lajdslj,yjcns,yjcnslj)values(SEQ_PICREC_XH.nextval,?,?,?,?,?,?,?,?,?)";
        int c = this.jdbcTemplate.execute(c_sql,
                new AbstractLobCreatingPreparedStatementCallbackImpl(
                        lobHandler) {
                    public void setValues(PreparedStatement pstmt,
                                          LobCreator lobCreator) throws SQLException,
                            DataAccessException {
                        pstmt.setString(1, vspic.getBkxh());
                        lobCreator.setBlobAsBytes(pstmt, 2, vspic.getZjwj());
                        pstmt.setString(3, vspic.getZjlx());
                        lobCreator.setBlobAsBytes(pstmt, 4, vspic.getClbksqb());
                        pstmt.setString(5, vspic.getClbksqblj());
                        lobCreator.setBlobAsBytes(pstmt, 6, vspic.getLajds());
                        pstmt.setString(7, vspic.getLajdslj());
                        lobCreator.setBlobAsBytes(pstmt, 8, vspic.getYjcns());
                        pstmt.setString(9, vspic.getYjcnslj());
                    }
                });
        flag = (c < 1) ? false : true;
        return flag;
    }

}
