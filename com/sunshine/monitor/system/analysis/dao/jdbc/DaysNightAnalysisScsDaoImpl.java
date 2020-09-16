package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.DayNigntNewAnaiysis;
import com.sunshine.monitor.system.analysis.dao.DaysNightAnalysisScsDao;

@Repository("DaysNightAnalysisScsDao")
public class DaysNightAnalysisScsDaoImpl extends ScsBaseDaoImpl implements DaysNightAnalysisScsDao{
	public Map<String,Object> queryList(DayNigntNewAnaiysis dn,Map<String, Object> filter)throws Exception{
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		String kdbhstr="";
		if(dn.getKdbh()!=null&&dn.getKdbh()!=""){
		String[] kdbhArray = dn.getKdbh().split(",");
		for(int j = 0;j<kdbhArray.length;j++){
				kdbhstr+="'"+kdbhArray[j]+"'";
			if(j<kdbhArray.length-1){
				kdbhstr+=",";
			}
		}
		sb.append(" and kdbh in (").append(kdbhstr).append(")  ");
		}
		//连续的先将满足查询条件的过车数据查出
		sb2.append(" select a.hphm,a.hpzl,a.hpys,to_date(gcsj,'yyyy-mm-dd') as gcsj from (select distinct hphm,hpzl,hpys,gcsj from ").append(" veh_passrec_sd where gcsj>='").append(dn.getKssj().trim()+" 00:00:00'")
		   .append(" and gcsj<='").append(dn.getJssj().trim()+" 23:59:59' ").append(sb.toString()).append(")a,")
		   .append(" (select distinct hphm,hpzl,hpys from ( select hphm,hpzl,hpys,sum(case  when gcsd>='").append(dn.getZkssj().trim())
		   .append(" ' and gcsd<='").append(dn.getZjssj()).append("' then 1 else 0 end) bts ")
		   .append(" from veh_passrec_sd where gcsj>='").append(dn.getKssj().trim()+" 00:00:00'")
		   .append(" and gcsj<='").append(dn.getJssj().trim()+" 23:59:59'").append(sb.toString()).append(" group by hphm,hpzl,hpys ")
		   .append(" )t1 where bts = 0 )b where a.hphm=b.hphm and a.hpzl=b.hpzl and a.hpys = b.hpys order by a.hphm,a.hpzl,a.hpys,a.gcsj DESC ");

		
		
		String temp_sql="";
		String lxts_like ="";
		String nont_hphm=" having hphm not in ('车牌','-','号牌未知','无车牌','拒识/无牌','------','无牌','-------','0000000','nullplate','湘')";
		//页面排序
		String sort = "daypercent";
		if(filter.get("sort") != null)
			sort = filter.get("sort").toString();
		String order = "desc";
		if(filter.get("order") != null)
			order = filter.get("order").toString();
		
		Map<String,Object> map = null;
		int zfycgz = Integer.parseInt(dn.getZfycgz());
		switch(zfycgz){
			case 0:
				
				temp_sql = " SELECT t1.hphm,t1.hpzl,t1.hpys,t1.nightCount,t1.dayCount,t1.total,"
						 + " round((t1.dayCount/t1.total::numeric)*100,2) dayPercent,round((t1.nightCount/t1.total::numeric)*100,2) nightPercent from"
						 + " ("
						 + " SELECT hphm,hpzl,hpys,"
						 + " SUM (CASE WHEN gcsd >= '"+dn.getZkssj()+"' AND gcsd <= '"+dn.getZjssj()+"' THEN 1 ELSE 0 END) nightCount,"
						 + " SUM (CASE WHEN gcsd >= '"+dn.getZkssj()+"' AND gcsd <= '"+dn.getZjssj()+"' THEN 0 ELSE 1 END) dayCount,COUNT (hphm) total"
						 + " FROM veh_passrec_sd "
						 + " WHERE gcsj >= '"+dn.getKssj().trim()+" 00:00:00'	AND gcsj <= '"+dn.getJssj().trim()+" 23:59:59'"
						 + " GROUP BY hphm,hpzl,hpys"
						 + " HAVING hphm NOT IN ('车牌','-','号牌未知','无车牌','拒识/无牌','------','无牌','-------','0000000','nullplate','湘')"
						 + " ) t1"
						 + " ORDER BY "+sort+" "+order;
				break;
			case 1:				
				
				int lxts = Integer.parseInt(dn.getThresholds());
				for(int i=0;i<lxts-1;i++){
					lxts_like+="1";
				}
				temp_sql = " select hphm,hpzl,hpys,length(sjcstr) as ycs from ("+
						   " select hphm,hpzl,hpys,trim( both '' from array_to_string(array_agg(sjc order by gcsj ),'')) as sjcstr from( "+
						   " select hphm,hpzl,hpys,gcsj,decode(to_char(gcsj-lag(gcsj,1,gcsj)over(PARTITION by hphm,hpzl,hpys  order by gcsj,hphm),'dd'),'00','0','01','1','+') as sjc from ( "+sb2.toString()+
						   " )t )a  group by hphm,hpzl,hpys "+nont_hphm+" )b where strpos(replace(sjcstr,'0',''), '"+lxts_like+"')>0 order by length(sjcstr) desc ";						
				break;
				
			case 2:
				String[] mulDate = dn.getMuldate().split(",");
				sb.append(" and  (");
				for(int n=0;n<mulDate.length;n++){
					String datekssj = mulDate[n].trim()+" 00:00:00";
					String datejssj = mulDate[n].trim()+" 23:59:59";
					sb.append(" (gcsj>='").append(datekssj).append("' and ");
					sb.append(" gcsj<='").append(datejssj).append("') ");
					if(n<mulDate.length-1){
						sb.append(" or ");
					}
				}
				sb.append(" ) ");
		
				temp_sql = " select hphm, hpzl, hpys, total as ycs from (select hphm,hpzl,hpys,"+
				   " sum(case when gcsd >= '"+dn.getZkssj()+"' AND gcsd <= '"+dn.getZjssj()+"' THEN 1 else 0 end) bts,count(hphm) total from "+
				   " (select distinct hphm, hpzl, hpys, gcsj, gcrq, gcsd FROM veh_passrec_sd "+
				   " WHERE 1=1 "+sb.toString()+") t1 "+
				   " GROUP BY hphm, hpzl, hpys "+nont_hphm+") t2 where bts = 0 order by total desc ";
				break;
		}
		 map =this.findPageForMap(temp_sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}

	@Override
	public Map<String, Object> queryDayNightPageList(DayNigntNewAnaiysis dn,Map<String, Object> filter) throws Exception {
		Map<String,Object> map = null;
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer(); //时间间隔
		StringBuffer sb2 = new StringBuffer();//昼夜时间间隔
		String kdbhstr="";
		sb1.append(" and gcsj>= '").append(dn.getKssj().trim()+" 00:00:00").append("' and ");
		sb1.append(" gcsj<= '").append(dn.getJssj().trim()+" 23:59:59").append("' ");
		int zfycgz = Integer.parseInt(dn.getZfycgz());
		if(zfycgz!=2){
			sb.append(sb1.toString());
		}
		if(dn.getKdbh()!=null&&dn.getKdbh()!=""){
			String[] kdbhArray = dn.getKdbh().split(",");
			for(int j = 0;j<kdbhArray.length;j++){
					kdbhstr+="'"+kdbhArray[j]+"'";
				if(j<kdbhArray.length-1){
					kdbhstr+=",";
				}
			}
			sb.append(" and kdbh in (").append(kdbhstr).append(")  ");
		}		
		sb2.append(" ((to_char(gcsj,'hh24:mi:ss')>='").append(dn.getQykssj().trim()).append("' and to_char(gcsj,'hh24:mi:ss')<='").append(dn.getQyjssj().trim()).append("') or ");
		sb2.append(" (to_char(gcsj,'hh24:mi:ss')>='").append(dn.getHykssj().trim()).append("' and to_char(gcsj,'hh24:mi:ss')<='").append(dn.getHyjssj().trim()).append("'))  ");
	 				
		if(zfycgz==2){
			String[] mulDate = dn.getMuldate().split(",");
			sb.append("  and (");
			for(int n=0;n<mulDate.length;n++){
				String datekssj = mulDate[n].trim()+" 00:00:00";
				String datejssj = mulDate[n].trim()+" 23:59:59";
				sb.append(" (gcsj>='").append(datekssj).append("' and ");
				sb.append(" gcsj<='").append(datejssj).append("') ");
				if(n<mulDate.length-1){
					sb.append(" or ");
				}
			}
			sb.append(" ) ");
		}
		
//		String nont_hphm=" having hphm not in ('车牌','-','号牌未知','无车牌','拒识/无牌','------','无牌','-------','0000000','nullplate','湘')";
		String temp_sql=" select gcxh,kdbh,gcsj,fxbh,hphm,hpzl,hpys,xzqh,tp1 from (select distinct max(gcxh)over(partition by hphm,hpzl,hpys,kdbh,gcsj order by gcsj desc) gcxh,kdbh,gcsj,fxbh,hphm,hpzl,hpys,substr(kdbh,0,6) xzqh,max(tp1)over(partition by hphm,hpzl,hpys,kdbh,gcsj order by gcsj desc) tp1  from (select * from veh_passrec where 1=1 "+sb.toString()+" )a where "+sb2.toString()+" and  hphm='"+dn.getHphm()+"' and hpzl='"+dn.getHpzl()+"' and hpys = '"+dn.getHpys()+"')t1  order by gcsj desc ";
		map =this.findPageForMap(temp_sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}

	@Override
	public int getForDayNightTotal(DayNigntNewAnaiysis dn,
			Map<String, Object> filter) throws Exception {
		String sql = getSql(dn, filter);
		return super.getTotal(sql);
	}

	@Override
	public List<Map<String, Object>> queryForDayNightListExt(DayNigntNewAnaiysis dn,Map<String, Object> filter) throws Exception {
		String sql = getSql(dn, filter);
		List<Map<String, Object>> list = this.getPageDatas(sql,
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return list;
	}
	
	public String getSql(DayNigntNewAnaiysis dn, Map<String, Object> filter) throws Exception{
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		String kdbhstr="";
		if(dn.getKdbh()!=null&&dn.getKdbh()!=""){
		String[] kdbhArray = dn.getKdbh().split(",");
		for(int j = 0;j<kdbhArray.length;j++){
				kdbhstr+="'"+kdbhArray[j]+"'";
			if(j<kdbhArray.length-1){
				kdbhstr+=",";
			}
		}
		sb.append(" and kdbh in (").append(kdbhstr).append(")  ");
		}
		//连续的先将满足查询条件的过车数据查出
		sb2.append(" select a.hphm,a.hpzl,a.hpys,to_date(gcsj,'yyyy-mm-dd') as gcsj from (select distinct hphm,hpzl,hpys,gcsj from ").append(" veh_passrec_sd where gcsj>='").append(dn.getKssj().trim()+" 00:00:00'")
		   .append(" and gcsj<='").append(dn.getJssj().trim()+" 23:59:59' ").append(sb.toString()).append(")a,")
		   .append(" (select distinct hphm,hpzl,hpys from ( select hphm,hpzl,hpys,sum(case  when gcsd>='").append(dn.getZkssj().trim())
		   .append(" ' and gcsd<='").append(dn.getZjssj()).append("' then 1 else 0 end) bts ")
		   .append(" from veh_passrec_sd where gcsj>='").append(dn.getKssj().trim()+" 00:00:00'")
		   .append(" and gcsj<='").append(dn.getJssj().trim()+" 23:59:59'").append(sb.toString()).append(" group by hphm,hpzl,hpys ")
		   .append(" )t1 where bts = 0 )b where a.hphm=b.hphm and a.hpzl=b.hpzl and a.hpys = b.hpys order by a.hphm,a.hpzl,a.hpys,a.gcsj DESC ");
	
		
		
		String temp_sql="";
		String lxts_like ="";
		String nont_hphm=" having hphm not in ('车牌','-','号牌未知','无车牌','拒识/无牌','------','无牌','-------','0000000','nullplate','湘')";
		//页面排序
		String sort = "daypercent";
		if(filter.get("sort") != null)
			sort = filter.get("sort").toString();
		String order = "desc";
		if(filter.get("order") != null)
			order = filter.get("order").toString();
		
		Map<String,Object> map = null;
		int zfycgz = Integer.parseInt(dn.getZfycgz());
		switch(zfycgz){
			case 0:
				
				temp_sql = " SELECT t1.hphm,t1.hpzl,t1.hpys,t1.nightCount,t1.dayCount,"//t1.total,
						 + " round((t1.dayCount/t1.total::numeric)*100,2) dayPercent,round((t1.nightCount/t1.total::numeric)*100,2) nightPercent from"
						 + " ("
						 + " SELECT hphm,hpzl,hpys,"
						 + " SUM (CASE WHEN gcsd >= '"+dn.getZkssj()+"' AND gcsd <= '"+dn.getZjssj()+"' THEN 1 ELSE 0 END) nightCount,"
						 + " SUM (CASE WHEN gcsd >= '"+dn.getZkssj()+"' AND gcsd <= '"+dn.getZjssj()+"' THEN 0 ELSE 1 END) dayCount,COUNT (hphm) total"
						 + " FROM veh_passrec_sd "
						 + " WHERE gcsj >= '"+dn.getKssj().trim()+" 00:00:00'	AND gcsj <= '"+dn.getJssj().trim()+" 23:59:59'"
						 + " GROUP BY hphm,hpzl,hpys"
						 + " HAVING hphm NOT IN ('车牌','-','号牌未知','无车牌','拒识/无牌','------','无牌','-------','0000000','nullplate','湘')"
						 + " ) t1"
						 + " ORDER BY "+sort+" "+order;
				break;
			case 1:				
				
				int lxts = Integer.parseInt(dn.getThresholds());
				for(int i=0;i<lxts-1;i++){
					lxts_like+="1";
				}
				temp_sql = " select hphm,hpzl,hpys,length(sjcstr) as ycs from ("+
						   " select hphm,hpzl,hpys,trim( both '' from array_to_string(array_agg(sjc order by gcsj ),'')) as sjcstr from( "+
						   " select hphm,hpzl,hpys,gcsj,decode(to_char(gcsj-lag(gcsj,1,gcsj)over(PARTITION by hphm,hpzl,hpys  order by gcsj,hphm),'dd'),'00','0','01','1','+') as sjc from ( "+sb2.toString()+
						   " )t )a  group by hphm,hpzl,hpys "+nont_hphm+" )b where strpos(replace(sjcstr,'0',''), '"+lxts_like+"')>0 order by length(sjcstr) desc ";						
				break;
				
			case 2:
				String[] mulDate = dn.getMuldate().split(",");
				sb.append(" and  (");
				for(int n=0;n<mulDate.length;n++){
					String datekssj = mulDate[n].trim()+" 00:00:00";
					String datejssj = mulDate[n].trim()+" 23:59:59";
					sb.append(" (gcsj>='").append(datekssj).append("' and ");
					sb.append(" gcsj<='").append(datejssj).append("') ");
					if(n<mulDate.length-1){
						sb.append(" or ");
					}
				}
				sb.append(" ) ");
		
				temp_sql = " select hphm, hpzl, hpys, total as ycs from (select hphm,hpzl,hpys,"+
				   " sum(case when gcsd >= '"+dn.getZkssj()+"' AND gcsd <= '"+dn.getZjssj()+"' THEN 1 else 0 end) bts,count(hphm) total from "+
				   " (select distinct hphm, hpzl, hpys, gcsj, gcrq, gcsd FROM veh_passrec_sd "+
				   " WHERE 1=1 "+sb.toString()+") t1 "+
				   " GROUP BY hphm, hpzl, hpys "+nont_hphm+") t2 where bts = 0 order by total desc ";
				break;
		}
		return temp_sql;
	}

}
