
package com.sunshine.monitor.system.query.dao.impl;

import java.net.URLDecoder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Repository;

import com.ibm.icu.text.SimpleDateFormat;
import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.bean.MapEntry;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.AbstractLobCreatingPreparedStatementCallbackImpl;
import com.sunshine.monitor.system.query.bean.Surveil;
import com.sunshine.monitor.system.query.bean.SuspMonitor;
import com.sunshine.monitor.system.query.bean.VehAlarmrecIntegrated;
import com.sunshine.monitor.system.query.bean.VehPassrecIntegrated;
import com.sunshine.monitor.system.query.dao.QueryListDao;
import com.sunshine.monitor.system.query.dao.ReQueryListDao;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Repository("reQueryListDao")
public class ReQueryListDaoImpl extends BaseDaoImpl implements ReQueryListDao {

    public int updates(List<Map<String, Object>> listrows) {
        for (Map<String, Object> m : listrows) {
            List param = new ArrayList<>();
            String sql =
                    " update VEH_KS_SPQK set fqsj=? , fksj=? ,fkjg=? where bkxh=? and sf=? and ds =? ";
            param.add(getCurtime());
            param.add(getCurtime());
            param.add(m.get("fkjg").toString());
            param.add(m.get("bkxh").toString());
            param.add(m.get("sf").toString());
            param.add(m.get("ds").toString());
            this.jdbcTemplate.update(sql, param.toArray());
        }
        return 0;
    }

    public String getCurtime() {
        long l = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(l));
    }

    public int insert(List<Map<String, Object>> listrows) {
        StringBuffer sb = new StringBuffer();
        List param = new ArrayList<>();
        if (listrows.size() == 1) {
            Map<String, Object> m = listrows.get(0);
            sb.append("insert into VEH_KS_SPQK (bkxh,fqsj,sf,ds,fksj,fkjg) values"
                    + "(?,?,?,?,?,?)");
            param.add(m.get("bkxh").toString());
            param.add(m.get("fqsj").toString());
            param.add(m.get("sf").toString());
            param.add(m.get("ds").toString());
            param.add(m.get("fksj").toString());
            param.add(m.get("fkjg").toString());

        } else {
            sb.append("insert into VEH_KS_SPQK (bkxh,fqsj,sf,ds,fksj,fkjg) ");
            for (int i = 0; i < listrows.size(); i++) {
                if (i != 0) {
                    sb.append(" UNION ALL ");
                }
                sb.append(" SELECT ?,?,?,?,?,? FROM DUAL ");
                param.add(listrows.get(i).get("bkxh").toString());
                param.add(listrows.get(i).get("fqsj").toString());
                param.add(listrows.get(i).get("sf").toString());
                param.add(listrows.get(i).get("ds").toString());
                param.add(listrows.get(i).get("fksj").toString());
                param.add(listrows.get(i).get("fkjg").toString());
            }
        }
        return this.jdbcTemplate.update(sb.toString(),param.toArray());
    }

    public List<Map<String, Object>> query(String bkxh) {
        String sql = "select  bkxh,nvl(fqsj,' ') ,sf,nvl(fksj,' '),fkjg,ds from VEH_KS_SPQK where" +
                " bkxh= ?";
        return this.jdbcTemplate.queryForList(sql,bkxh);
    }


    public Map getMapForSuspinfoFilter(Map map, StringBuffer condition)
            throws Exception {
        String idxSql = map.get("idx_sql").toString();
        List param = new ArrayList<>();
        StringBuffer sb = new StringBuffer("select ? BKXH,HPHM,HPZL,BKLB,BKJGMC,BKSJ,YWZT,BJZT," +
                "XXLY,BKRMC,CXSQRMC,BY5  from ");
        param.add(idxSql);
        sb.append(" veh_suspinfo ");
        sb.append("  where ?");
        param.add(condition);
        System.out.println("续控管理查询：" + sb);
        return this.getSelf().findPageForMap(sb.toString(),param.toArray(),
                Integer.parseInt(map.get("curPage").toString()),
                Integer.parseInt(map.get("pageSize").toString()));
    }

    /**
     * Save suspinfo picture 保存布控文书
     */
    public boolean saveSuspinfopictrue(final VehSuspinfopic vspic)
            throws Exception {
        boolean flag = false;
        String c_sql = "insert into SUSP_DOCUMENTS(bkxh, WSWJ, WJMC,GXSJ) values(?,?,?,sysdate)";
        int c = this.jdbcTemplate.execute(c_sql,
                new AbstractLobCreatingPreparedStatementCallbackImpl(
                        lobHandler) {
                    public void setValues(PreparedStatement pstmt,
                                          LobCreator lobCreator) throws SQLException,
                            DataAccessException {
                        pstmt.setString(1, vspic.getBkxh());
                        lobCreator.setBlobAsBytes(pstmt, 2, vspic.getZjwj());
                        pstmt.setString(3, vspic.getZjlx());
                    }
                });
        flag = (c < 1) ? false : true;
        // throw new IllegalDataException("数据出错!");

        return flag;
    }

}