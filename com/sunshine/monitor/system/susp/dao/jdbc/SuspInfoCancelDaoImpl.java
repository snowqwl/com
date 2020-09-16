package com.sunshine.monitor.system.susp.dao.jdbc;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.orm.SqlUtils;
import com.sunshine.monitor.comm.util.orm.bean.PreSqlEntry;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspInfoCancelDao;

@Repository("suspInfoCancelDao")
public class SuspInfoCancelDaoImpl extends BaseDaoImpl implements SuspInfoCancelDao {

    public Map findSuspinfoCancelForMap(Map filter, VehSuspinfo info,
                                        String glbm, String modul) throws Exception {
        List param = new ArrayList<>();
        StringBuffer sb = new StringBuffer(" select BKXH, HPZL,hphm,BKDL, BKLB ,bkrmc,to_char(BKQSSJ,'yyyy-mm-dd') as BKQSSJ ")
                .append(",to_char(BKJZSJ,'yyyy-mm-dd') as BKJZSJ,bjzt, bjsj  from  ");

        if ("new".equals(modul)) {
            sb.append(" (select * from VEH_SUSPINFO  where YWZT = '14'   and ( XXLY = '0' or xxly = '2' ) ");
            sb.append(" and ((bkdl = '1' or bkdl = '2' or bkdl = '3') and BKJG  in (SELECT xjjg " +
                    "FROM frm_prefecture WHERE dwdm=? OR xjjg=?)) ");
            param.add(glbm);
            param.add(glbm);
        } else {
            sb.append(" (select * from VEH_SUSPINFO  where YWZT = '41'   and XXLY = '0' ");
            sb.append(" and bkjg in (Select xjjg from frm_prefecture Where dwdm=?)");
//			sb.append(" and BKJG  in (SELECT glbm FROM frm_department WHERE glbm='").append(glbm).append("' OR sjbm='").append(glbm).append("')");
//			sb.append(" and BKJG = '").append(glbm).append("'");
            param.add(glbm);
        }

        if (StringUtils.isNotBlank(info.getBkdl())) {
            sb.append(" and bkdl =? ");
            param.add(info.getBkdl());
        }

        if (StringUtils.isNotBlank(info.getBklb())) {
            sb.append(" and bklb = ? ");
            param.add(info.getBklb());
        }

        if (StringUtils.isNotBlank(info.getHpzl())) {
            sb.append("  and hpzl = ? ");
            param.add(info.getHpzl());
        }

        if (StringUtils.isNotBlank(info.getHphm())) {
            sb.append(" and hphm like ? ");
            param.add("%" + info.getHphm() + "%");
        }

        if (StringUtils.isNotBlank(info.getKssj())) {
            sb.append(" and bksj >= to_date(?,'yyyy-mm-dd HH24:mi:ss')  ");
            param.add(info.getKssj());
        }

        if (StringUtils.isNotBlank(info.getJssj())) {
            sb.append(" and bksj <= to_date(?,'yyyy-mm-dd HH24:mi:ss') ");
            param.add(info.getJssj());
        }
		/*
		if(StringUtils.isBlank(info.getKssj()) && StringUtils.isBlank(info.getJssj())){
			sb.append(" and bksj >= sysdate - 365  ");
		}
		*/
        if (StringUtils.isNotBlank(info.getBjzt())) {
            sb.append(" and bjzt = ? ");
            param.add(info.getBjzt());
        }

        sb.append(")v ");
        sb.append("order by bksj desc ");

//		System.out.println("new审核:"+sb.toString());
        return this.getSelf().findPageForMap(sb.toString(), param.toArray(),
                Integer.parseInt(filter.get("curPage").toString()),
                Integer.parseInt(filter.get("pageSize").toString()));
    }

    public Map findSuspinfoCancelOverTimeForMap(Map filter, VehSuspinfo info,
                                                String glbm, String modul) throws Exception {
        List param = new ArrayList<>();
        StringBuffer sb = new StringBuffer(" select BKXH, HPZL,hphm,BKDL, BKLB ,bkrmc,to_char(BKQSSJ,'yyyy-mm-dd') as BKQSSJ ")
                .append(",to_char(BKJZSJ,'yyyy-mm-dd') as BKJZSJ,bjzt, bjsj  from  ");

        if ("new".equals(modul)) {

            sb.append(" VEH_SUSPINFO  where YWZT = '14'   and XXLY = '0' ");
            sb.append(" and (((bkdl = '1' or bkdl = '2') and BKJG = ?) or  (bkdl = '3' and bkr = ? )) ");
            param.add(glbm);
            param.add(info.getBkr());
        } else {
            sb.append(" VEH_SUSPINFO  where YWZT = '41'   and XXLY = '0' ");
            sb.append(" and BKJG = ? ");
            param.add(glbm);
        }

        sb.append(" and bkdl='3'");

        if (StringUtils.isNotBlank(info.getBklb())) {
            sb.append(" and bklb = ? ");
            param.add(info.getBklb());
        }

        if (StringUtils.isNotBlank(info.getHpzl())) {
            sb.append("  and hpzl = ? ");
            param.add(info.getHpzl());
        }

        if (StringUtils.isNotBlank(info.getHphm())) {
            sb.append(" and hphm like ? ");
            param.add("%"+info.getHphm()+"%");
        }

        if (StringUtils.isNotBlank(info.getKssj())) {
            sb.append(" and bksj >= to_date(?,'yyyy-mm-dd HH24:mi:ss')  ");
            param.add(info.getKssj());
        }

        if (StringUtils.isNotBlank(info.getJssj())) {
            sb.append(" and bksj <= to_date(?,'yyyy-mm-dd HH24:mi:ss') ");
            param.add(info.getJssj());
        }

        if (StringUtils.isBlank(info.getKssj()) && StringUtils.isBlank(info.getJssj())) {
            sb.append(" and bksj >= sysdate - 365  ");
        }

        if (StringUtils.isNotBlank(info.getBjzt())) {
            sb.append(" and bjzt = ? ");
            param.add(info.getBjzt());
        }
        sb.append(" and (sysdate-bksj)*24*3600>7200 ");
        //sb.append(")v ");
        System.out.println("timeout:" + sb.toString());
        return this.getSelf().findPageForMap(sb.toString(),param.toArray(),
                Integer.parseInt(filter.get("curPage").toString()),
                Integer.parseInt(filter.get("pageSize").toString()));
    }

    //更新布控表(针对撤控申请)
    public int updateSuspInfoForCancel(VehSuspinfo info) throws Exception {
        String userdw = info.getCxsqdw();
        String bkjg = info.getBkjg();
        StringBuffer sqlCancel = null;
        String bkr = info.getBkrjh();
        String ckr = info.getCxsqr();
        List param = new ArrayList<>();
        if (!userdw.equals(bkjg)) {//上级的人撤控申请，不走审核审批流程
            sqlCancel = new StringBuffer("UPDATE veh_suspinfo  set ywzt = '99' ,gxsj = sysdate ");
        } else {
            if (!bkr.equals(ckr)) {
                sqlCancel = new StringBuffer("UPDATE veh_suspinfo  set ywzt = '99' ,gxsj = sysdate ");
            } else {
                sqlCancel = new StringBuffer("UPDATE veh_suspinfo  set ywzt = '41' ,gxsj = sysdate ");
            }
        }
        if (StringUtils.isNotBlank(info.getCkyydm())){
            sqlCancel.append(",ckyydm = ? ");
            param.add(info.getCkyydm());
        }
        if (StringUtils.isNotBlank(info.getCkyyms())){
            sqlCancel.append(",ckyyms = ? ");
            param.add(info.getCkyyms());
        }


        if (StringUtils.isNotBlank(info.getCxsqr())){
            sqlCancel.append(", cxsqr = ? ");
            param.add(info.getCxsqr());
        }

        if (StringUtils.isNotBlank(info.getCxsqrjh())){
            sqlCancel.append(",cxsqrjh = ? ");
            param.add(info.getCxsqrjh());
        }

        if (StringUtils.isNotBlank(info.getCxsqrmc())){
            sqlCancel.append(",cxsqrmc = ? ");
            param.add(info.getCxsqrmc());
        }

        if (StringUtils.isNotBlank(info.getCxsqdw())){
            sqlCancel.append(", cxsqdw = ? ");
            param.add(info.getCxsqdw());
        }


        if (StringUtils.isNotBlank(info.getCxsqdwmc())){
            sqlCancel.append(", cxsqdwmc = ? ");
            param.add(info.getCxsqdwmc());
        }


        if (StringUtils.isBlank(info.getBy1())) {
            sqlCancel.append(", by1 = to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')");
        }

        /**一键撤控的申请人的yhdh**/
        if (StringUtils.isNotBlank(info.getBy5())) {
            sqlCancel.append(", by5 = ?");
            param.add(info.getBy5());
        }

        sqlCancel.append(", cxsqsj = sysdate  where bkxh = ? ");
        param.add(info.getBkxh());


        int i = this.jdbcTemplate.update(sqlCancel.toString(),param.toArray());

        return i;
    }

    //写入操作日志
    public int insertBusinessLog(VehSuspinfo info, String jz) throws Exception {
        List param = new ArrayList<>();
        StringBuffer sqlLog = new StringBuffer("INSERT into BUSINESS_LOG (xh, ywxh, ywlb, ywjb, czrdh, czrjh, czrdwdm, czrdwjz) ");
        sqlLog.append(" values (seq_business_log_xh.nextval,?,'20',?,?,?,?,?");

        param.add(info.getBkxh());
        param.add(info.getBkjb());
        param.add(info.getCxsqr());
        param.add(info.getCxsqrjh());
        param.add(info.getCxsqdw());
        param.add(jz);


        int i = this.jdbcTemplate.update(sqlLog.toString(),param.toArray());

        return i;
    }

    public List getAlarmList(String bkxh) throws Exception {
        String sql = "select * from veh_alarmrec where bkxh = ?";

        List list = this.queryForList(sql, new Object[]{bkxh}, VehAlarmrec.class);
        return list;
    }

    public List getAuditApproves(AuditApprove aa) throws Exception {
        String sql = "";
        List<String> list = new ArrayList<String>();
        if ((aa.getBkxh() != null) && (aa.getBkxh().length() > 0)) {
            //sql = sql + " and bkxh='" + aa.getBkxh() + "'";
            sql = sql + " and bkxh = ?";
            list.add(aa.getBkxh());
        }
        if ((aa.getBzw() != null) && (aa.getBzw().length() > 0)) {
            if (aa.getBzw().length() == 1) {
                //sql = sql + " and bzw='" + aa.getBzw() + "'";
                sql = sql + " and bzw = ?";
                list.add(aa.getBzw());
            } else if (aa.getBzw().equals("12")) {
                sql = sql + " and (bzw='1' or bzw='2')";
            } else if (aa.getBzw().equals("34")) {
                sql = sql + " and (bzw='3' or bzw='4')";
            }
        }
        if (sql.length() > 1)
            sql = " where " + sql.substring(5, sql.length()) + " ";
        sql = "select XH, BKXH, CZR, CZRDW, CZRDWMC, CZSJ, CZJG, MS, BZW, BY1, BY2, CZRJH, CZRMC from audit_approve " + sql + " order by czsj desc";
        return this.queryForList(sql, list.toArray(), AuditApprove.class);
    }

    //获取自动撤控的信息
    public List<VehSuspinfo> getAutoCsuspinfoList() throws Exception {
        String sql = "select * from veh_suspinfo where bkjzsj < sysdate "
                + "and ywzt <> '99' and jlzt <> '2' and rownum <= 1000";
        List<VehSuspinfo> list = this.queryList(sql, VehSuspinfo.class);
        return list;
    }

    public Map findSuspinfoCancelTimeoutForMap(Map filter, VehSuspinfo info,
                                               String glbm) throws Exception {
        List param  =new ArrayList<>();
        StringBuffer sb = new StringBuffer(" select BKXH, HPZL,hphm, BKLB ,bkrmc,to_char(BKQSSJ,'yyyy-mm-dd') as BKQSSJ ")
                .append(",to_char(BKJZSJ,'yyyy-mm-dd') as BKJZSJ,bjzt, bjsj  from  ");

        sb.append(" (select * from VEH_SUSPINFO  where  XXLY = '0' ");
        sb.append(" and (((bkdl = '1' or bkdl = '2') and bkr = ?) or  (bkdl = '3' and bkr = ? )) ");
        param.add(info.getBkr());
        param.add(info.getBkr());
        sb.append(" and YWZT='14' and cxsqsj is null and by2 is not null and ROUND(TO_NUMBER(sysdate - by2) * 24 * 60 * 60) >= 24 * 60 * 60 ");

        if (StringUtils.isNotBlank(info.getBkdl())) {
            sb.append(" and bkdl = ?  ");
            param.add(info.getBkdl());
        }

        if (StringUtils.isNotBlank(info.getBklb())) {
            sb.append(" and bklb = ? ");
            param.add(info.getBklb());
        }

        if (StringUtils.isNotBlank(info.getHpzl())) {
            sb.append("  and hpzl = ? ");
            param.add(info.getHpzl());
        }

        if (StringUtils.isNotBlank(info.getHphm())) {
            sb.append(" and hphm like ? ");
            param.add("%"+info.getHphm()+"%");
        }

        if (StringUtils.isNotBlank(info.getKssj())) {
            sb.append(" and bksj >= to_date(?,'yyyy-mm-dd HH24:mi:ss')  ");
            param.add(info.getKssj());
        }

        if (StringUtils.isNotBlank(info.getJssj())) {
            sb.append(" and bksj <= to_date(?,'yyyy-mm-dd HH24:mi:ss') ");
            param.add(info.getJssj());
        }

        if (StringUtils.isBlank(info.getKssj()) && StringUtils.isBlank(info.getJssj())) {
            sb.append(" and bksj >= sysdate - 365  ");
        }

        if (StringUtils.isNotBlank(info.getBjzt())) {
            sb.append(" and bjzt = ? ");
            param.add(info.getBjzt());
        }

        sb.append(")v ");

        //System.out.println(sb.toString());
        return this.getSelf().findPageForMap(sb.toString(),param.toArray(),
                Integer.parseInt(filter.get("curPage").toString()),
                Integer.parseInt(filter.get("pageSize").toString()));
    }

    /**
     * 二次同步警综布控撤控
     */
    public void synchronizationJz() throws Exception {
        //1-同步开始时间
        //String x_sql = "select bz from trans_schedule where rwbh='A4010'";    //警综被盗被抢同步
        String x_sql = "select bz from JM_trans_schedule where rwbh='A4010'";   //警综被盗被抢同步
        String t_kssj = (String) this.jdbcTemplate.queryForObject(x_sql, String.class);
        if (t_kssj.equals("") || t_kssj == null) {
            t_kssj = "20130101000001";
        }

        String t_jyaq = "";
        String t_clsbdh = "";
        String t_fdjh = "";
        String y_sql = "select csz from frm_syspara where gjz='xzqh'";
        String t_bkfw = this.jdbcTemplate.queryForObject(y_sql, String.class);

        List param = new ArrayList<>();
        //2-需要同步的数据(查询一个月内)
        String v_sql = " SELECT bkxh,ysbh,hphm,hpzl,bkdl,bklb,to_char(bkqssj,'yyyy-mm-dd hh24:mi:ss') bkqssj," +
                "to_char(bkjzsj,'yyyy-mm-dd hh24:mi:ss') bkjzsj,jyaq,bkfwlx,bkfw,bkjb," +
                "bkxz,sqsb,bjya,mhbkbj,bjfs,dxjshm,lar,ladw,ladwlxdh,clpp,hpys,clxh,cllx,csys,clsbdh," +
                "fdjh,cltz,clsyr,syrlxdh,syrxxdz,bkr,bkrjh,bkrmc,bkjg,bkjgmc,bkjglxdh,to_char(bksj,'yyyy-mm-dd hh24:mi:ss') bksj,czr,czrjh," +
                "czrmc,czrdw,czrdwmc,to_char(czsj,'yyyy-mm-dd hh24:mi:ss') czsj,czjg,ms,cxsqr,cxsqrjh,cxsqrmc,cxsqdw,cxsqdwmc,to_char(cxsqsj,'yyyy-mm-dd hh24:mi:ss') cxsqsj,ckyydm," +
                "ckyyms,ckczr,ckczrjh,ckczrmc,ckczrdw,ckczrdwmc,to_char(ckczsj,'yyyy-mm-dd hh24:mi:ss') ckczsj,ckczjg,ckms,ywzt,jlzt,gxsj,xxly," +
                "bkpt,bjzt,bjsj,by1,by2,by3,by4,by5 " +
                " FROM jz_veh_suspinfo " +
                " WHERE gxsj>to_date(?,'yyyymmddhh24miss')-30 and " +
                " gxsj<to_date(?,'yyyymmddhh24miss') order by gxsj";
        param.add( t_kssj);
        param.add(t_kssj);
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(v_sql,param.toArray());
        if (list.size() > 0) {
            for (Iterator<Map<String, Object>> it = list.iterator(); it.hasNext(); ) {
                Map<String, Object> obj = (Map<String, Object>) it.next();
                if (obj.get("jyaq").toString().length() > 512) {
                    t_jyaq = obj.get("jyaq").toString().substring(0, 512) + "...";
                } else {
                    t_jyaq = obj.get("jyaq").toString();
                }
                if (obj.get("hpzl").toString().length() != 2) {
                    continue;
                }
                if (obj.get("hphm").toString().length() < 6 || obj.get("hphm").toString().length() > 10) {
                    continue;
                }
                if (obj.get("clsbdh").toString().length() > 28) {
                    t_clsbdh = obj.get("clsbdh").toString().substring(0, 28) + "...";
                } else {
                    t_clsbdh = obj.get("clsbdh").toString();
                }
                if (obj.get("fdjh").toString().length() > 28) {
                    t_fdjh = obj.get("fdjh").toString().substring(0, 28) + "...";
                } else {
                    t_fdjh = obj.get("fdjh").toString();
                }
                //判断是否需要在系统布控(漏掉的布控记录)
                if (obj.get("jlzt").toString().equals("1")) {
                    List params = new ArrayList<>();
                    String c_sql = "select count(1) from veh_suspinfo " +
                            " where ysbh=? and hphm=?";
                    params.add(obj.get("ysbh").toString());
                    params.add(obj.get("hphm").toString());
                    int count = this.jdbcTemplate.queryForInt(c_sql,params.toArray());
                    if (count == 0) {

                        String seq_sql = "select ? from dual";

                        String t_bkxh = (String) this.jdbcTemplate.queryForObject(seq_sql,
                                new Object[]{t_bkfw+"||seq_suspinfo_xh.nextval"},String.class);
                        List list1 = new ArrayList<>();
                        StringBuffer i_sql1 = new StringBuffer("Insert Into veh_suspinfo (bkxh," +
                                "ysbh,hphm,hpzl,bkdl,bklb,bkqssj,bkjzsj,jyaq,bkfwlx,bkfw,bkjb," +
                                "bkxz,sqsb,bjya,bjfs,dxjshm,lar,ladw,ladwlxdh,clpp,hpys,clxh,cllx,csys,clsbdh,fdjh,cltz,clsyr,syrlxdh," +
                                "syrxxdz,bkr,bkjg,bkjgmc,bkjglxdh,bksj,cxsqr,cxsqdw,cxsqdwmc,cxsqsj,ckyydm,ckyyms,ywzt,jlzt,gxsj,xxly," +
                                "bkpt,by1,by2,by3,by4,by5,bjzt,mhbkbj,cxsqrjh,bkrjh,bjsj,cxsqrmc,bkrmc) Values " +
                                "(?,?,?,?,'1','06',to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?," +
                                "'yyyy-mm-dd hh24:mi:ss'),?,'1',?," +
                                "'2','1','0','1','0111','',?,?,?,?,");
                        list1.add(t_bkxh);
                        list1.add(obj.get("ysbh").toString());
                        list1.add(obj.get("hphm").toString());
                        list1.add(obj.get("hpzl").toString());
                        list1.add(obj.get("bkqssj").toString());
                        list1.add(obj.get("bkjzsj"));
                        list1.add(t_jyaq);
                        list1.add(t_bkfw);
                        list1.add(obj.get("lar").toString());
                        list1.add(obj.get("ladw").toString());
                        list1.add(obj.get("ladwlxdh").toString());
                        list1.add(obj.get("clpp").toString());
                        if(obj.get("hpys") != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("hpys"));
                        }
                        i_sql1.append("?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd" +
                                " hh24:mi:ss'),");
                        list1.add(obj.get("clxh").toString());
                        list1.add(obj.get("cllx").toString());
                        list1.add(obj.get("csys").toString());
                        list1.add(t_clsbdh);
                        list1.add(t_fdjh);
                        list1.add(obj.get("cltz").toString());
                        list1.add(obj.get("clsyr").toString());
                        list1.add(obj.get("syrlxdh").toString());
                        list1.add(obj.get("syrxxdz").toString());
                        list1.add(obj.get("bkr").toString());
                        list1.add(obj.get("bkjg").toString());
                        list1.add(obj.get("bkjgmc").toString());
                        list1.add(obj.get("bkjglxdh").toString());
                        list1.add(obj.get("bksj").toString());
                        if(obj.get("cxsqr") != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("cxsqr"));
                        }
                        if(obj.get("cxsqdw") != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("cxsqdw"));
                        }
                        if(obj.get("cxsqdwmc")  != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("cxsqdwmc") );
                        }
                        if(obj.get("cxsqsj")  != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("cxsqsj"));
                        }
                        if(obj.get("ckyydm")  != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("ckyydm"));
                        }
                        if(obj.get("ckyyms")  != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("ckyyms"));
                        }
                        i_sql1.append("'14','1',sysdate,'6',?,");
                        list1.add(t_bkfw);
                        if(obj.get("by1")  != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("by1"));
                        }
                        if(obj.get("by2")  != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("by2"));
                        }
                        if(obj.get("by3")  != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("by3"));
                        }
                        if(obj.get("by4")  != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("by4"));
                        }
                        if(obj.get("by5")  != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("by5"));
                        }
                        i_sql1.append("'0','0',");
                        if(obj.get("cxsqrjh")  != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("cxsqrjh"));
                        }
                        i_sql1.append("?,");
                        list1.add(obj.get("bkrjh").toString());
                        if(obj.get("bjsj")  != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("bjsj"));
                        }
                        if(obj.get("cxsqrmc")  != null){
                            i_sql1.append("?,");
                            list1.add(obj.get("cxsqrmc"));
                        }
                        i_sql1.append("?,");
                        list1.add(obj.get("bkrmc").toString());
                        i_sql1.append(")");

                        this.jdbcTemplate.update(i_sql1.toString(),list1.toArray());

                        List list2 = new ArrayList<>();
                        StringBuffer i_sql2 = new StringBuffer("Insert Into audit_approve (xh," +
                                "bkxh,czr,czrdw,czrdwmc,czsj,czjg,ms,bzw,by1,by2,czrjh,czrmc) " +
                                "Values " );

                        i_sql2.append("(?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),'1'," +
                                        "?,'2',");
                        list2.add(t_bkfw+"||seq_audit_xh.nextval");
                        list2.add(t_bkxh);
                        list2.add(obj.get("czr").toString());
                        list2.add(obj.get("czrdw").toString());
                        list2.add(obj.get("czrdwmc").toString());
                        list2.add(obj.get("czsj").toString());
                        list2.add(obj.get("ms").toString());
                        if(obj.get("by1")  != null){
                            i_sql2.append("?,");
                            list2.add(obj.get("by1"));
                        }
                        if(obj.get("by2")  != null){
                            i_sql2.append("?,");
                            list2.add(obj.get("by2"));
                        }
                        i_sql2.append("?,?)");
                        list2.add(obj.get("czrjh").toString());
                        list2.add(obj.get("czrmc").toString());

                        this.jdbcTemplate.update(i_sql2.toString(),list2.toArray());
                    }
                }
                //判断是否需要在系统撤控(漏掉的撤控记录)
                if (obj.get("jlzt").toString().equals("2")) {
                    List list1 = new ArrayList<>();
                    String c_sql = "select count(1) from veh_suspinfo " +
                            " where ysbh=? and hphm=? and " +
                            "jlzt!='2'";
                    list1.add(obj.get("ysbh").toString());
                    list1.add(obj.get("hphm").toString());
                    int count = this.jdbcTemplate.queryForInt(c_sql,list1.toArray());

                    if (count == 1) {
                        List list2 = new ArrayList<>();
                        StringBuffer i_sql1 =new StringBuffer("update veh_suspinfo set cxsqr=");
                        if(obj.get("cxsqr")  != null){
                            i_sql1.append("?,");
                            list2.add(obj.get("cxsqr"));
                        }
                        i_sql1.append("cxsqrjh=");

                        if(obj.get("cxsqrjh")  != null){
                            i_sql1.append("?,");
                            list2.add(obj.get("cxsqrjh"));
                        }else{
                            i_sql1.append("'',");
                        }
                        i_sql1.append("cxsqrmc=?,cxsqdw=?,cxsqdwmc=");
                        list2.add(obj.get("cxsqrmc").toString());
                        list2.add(obj.get("cxsqdw").toString());
                        if(obj.get("cxsqdwmc")  != null){
                            i_sql1.append("?,");
                            list2.add(obj.get("cxsqdwmc"));
                        }else{
                            i_sql1.append("'',");
                        }
                        i_sql1.append("cxsqsj=to_date(?,'yyyy-mm-dd hh24:mi:ss'),ckyydm='99'," +
                                "ywzt='99',jlzt='2',gxsj=sysdate where ysbh=? and hphm=?");
                        list2.add(obj.get("cxsqsj").toString());
                        list2.add(obj.get("ysbh").toString());
                        list2.add(obj.get("hphm").toString());

                        this.jdbcTemplate.update(i_sql1.toString(),list2.toArray());

                        List list3 = new ArrayList<>();
                        String b_sql =
                                "select bkxh  from veh_suspinfo where ysbh=? and hphm=?";
                        list3.add(obj.get("ysbh").toString());
                        list3.add(obj.get("hphm").toString());
                        String t_bkxh = this.jdbcTemplate.queryForObject(b_sql,list3.toArray(),
                                String.class);

                        List list4 = new ArrayList<>();
                        StringBuffer i_sql2 = new StringBuffer("Insert Into audit_approve (xh,bkxh,czr,czrdw,czrdwmc,czsj,czjg,ms,bzw,by1,by2,czrjh,czrmc) Values ");
                        i_sql2.append("(?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),'1',?,'4',?," +
                                "?," +
                                "?,?)");
                        list4.add(t_bkfw+"||seq_audit_xh.nextval");
                        list4.add(t_bkxh);
                        list4.add(obj.get("ckczr").toString());
                        if(obj.get("ckczrdw") != null){
                            list4.add(obj.get("ckczrdw"));
                        }else {
                            list4.add(obj.get("''"));
                        }
                        list4.add(obj.get("ckczrdwmc").toString());
                        list4.add(obj.get("ckczsj").toString());
                        list4.add(obj.get("ckms").toString());
                        if(obj.get("by1") != null){
                            list4.add(obj.get("by1"));
                        }else {
                            list4.add(obj.get("''"));
                        }
                        if(obj.get("by2") != null){
                            list4.add(obj.get("by2"));
                        }else {
                            list4.add(obj.get("''"));
                        }
                        list4.add(obj.get("ckczrjh").toString());
                        list4.add(obj.get("ckczrmc").toString());

                        this.jdbcTemplate.update(i_sql2.toString(),list4.toArray());
                    }

                }
            }
        }

        String i_sql3 = "update JM_trans_schedule set bz=to_char(sysdate,'yyyymmddhh24miss') where rwbh='A4010'";
        this.jdbcTemplate.update(i_sql3);

    }

    public void updateSuspInfo(VehSuspinfo info) throws Exception {
        Map<String, String> defaultMap = new HashMap<String, String>();
        defaultMap.put("gxsj", "sysdate");
        PreSqlEntry preEntity = SqlUtils.getPreUpdateSqlByObject("veh_suspinfo", info, new HashMap<String, String>(), "bkxh");
        this.jdbcTemplate.update(preEntity.getSql(), preEntity.getValues().toArray());
    }

    /*
     * yaowang
     *
     */
    //更新布控表(针对撤控申请)
    public int updateSuspInfoForCancelcksq(VehSuspinfo info) throws Exception {
        String userdw = info.getCxsqdw();
        String bkjg = info.getBkjg();
        StringBuffer sqlCancel = null;
        List param = new ArrayList<>();
        if (!userdw.equals(bkjg)) {//上级的人撤控申请，不走审核审批流程
            sqlCancel = new StringBuffer("UPDATE veh_suspinfo  set ywzt = '99' ,gxsj = sysdate ");
        } else {
            sqlCancel = new StringBuffer("UPDATE veh_suspinfo  set ywzt = '41' ,gxsj = sysdate ");
        }
        if (StringUtils.isNotBlank(info.getCkyydm())){
            sqlCancel.append(",ckyydm = ? ");
            param.add(info.getCkyydm());
        }

        if (StringUtils.isNotBlank(info.getCkyyms())){
            sqlCancel.append(",ckyyms = ? ");
            param.add(info.getCkyyms());
        }

        if (StringUtils.isNotBlank(info.getCxsqr())){
            sqlCancel.append(", cxsqr = ? ");
            param.add(info.getCxsqr());
        }

        if (StringUtils.isNotBlank(info.getCxsqrjh())){
            sqlCancel.append(",cxsqrjh = ? ");
            param.add(info.getCxsqrjh());
        }

        if (StringUtils.isNotBlank(info.getCxsqrmc())){
            sqlCancel.append(",cxsqrmc = ? ");
            param.add(info.getCxsqrmc());
        }


        if (StringUtils.isNotBlank(info.getCxsqdw())){
            sqlCancel.append(", cxsqdw = ? ");
            param.add(info.getCxsqdw());
        }


        if (StringUtils.isNotBlank(info.getCxsqdwmc())){
            sqlCancel.append(", cxsqdwmc = ? ");
            param.add(info.getCxsqdwmc());
        }


        if (StringUtils.isBlank(info.getBy1())) {
            sqlCancel.append(", by1 = to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')");
        }

        /**一键撤控的申请人的yhdh**/
        if (StringUtils.isNotBlank(info.getBy5())) {
            sqlCancel.append(", by5 = ?");
            param.add(info.getBy5());
        }

        sqlCancel.append(", cxsqsj = sysdate  where bkxh = ? ");
        param.add(info.getBkxh());


        int i = this.jdbcTemplate.update(sqlCancel.toString(),param.toArray());

        return i;
    }

}
