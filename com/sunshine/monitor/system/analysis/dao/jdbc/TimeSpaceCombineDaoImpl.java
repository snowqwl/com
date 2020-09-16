package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.CombineCondition;
import com.sunshine.monitor.system.analysis.dao.TimeSpaceCombineDao;

@Repository("timeSpaceCombineDao")
public class TimeSpaceCombineDaoImpl extends BaseDaoImpl implements
		TimeSpaceCombineDao {

	public Map<String, Object> combineAnalysis(Map filter, String consql) {
		String sql = " select gcxh,hphm,kdbh,hpys,fxbh,gcsj,tp1 from veh_passrec where 1=1 "
				+ consql + " order by hphm, gcsj desc";
		System.out.println(sql);
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}

	public Map<String, Object> trackCount(Map filter, String consql) {
		String sql = "select distinct hphm,count(distinct(substr(kdbh, 1, 6))) over(partition by substr(kdbh, 1, 6), hphm) as xzqs,count(distinct(kdbh)) over(partition by kdbh, hphm) as kks,count(distinct(trunc(gcsj))) over(partition by trunc(gcsj), hphm) as ts,count(hphm) over(partition by hphm) as cs from veh_passrec where gcxh in( select gcxh from veh_passrec where 1=1 "
				+ consql + " )order by xzqs desc, kks desc, ts desc,cs desc";
		System.out.println(sql);
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}

	public Map<String, Object> offenInOut(Map filter, String consql) {
		String sql = " select hphm,xzqs,kks,ts,cs from (select distinct hphm,count(distinct(substr(kdbh, 1, 6))) over(partition by substr(kdbh, 1, 6), hphm) as xzqs,count(distinct(kdbh)) over(partition by kdbh, hphm) as kks,count(distinct(trunc(gcsj))) over(partition by trunc(gcsj), hphm) as ts,count(hphm) over(partition by hphm) as cs from veh_passrec  where 1 = 1 "
				+ consql + " order by xzqs desc, kks desc, ts desc, cs desc";
		System.out.println(sql);
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}

	public Map<String, Object> supposeAnalysis(Map filter, String consql,
			String[] condition) {
		String sql = " select gcxh,hphm,kdbh,hpys,fxbh,gcsj,tp1 from veh_passrec where hphm in ( select hphm from ( ";
		for(int i =0;i<condition.length;i++) {
			sql += condition[i];
			if(i<condition.length-1) {
				sql +=" union all ";
			}
		}
		sql += ") group by hphm having count(hphm) ="+condition.length + ") "+consql;
		System.out.println(sql);
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}
	
	public Map<String, Object> supposeAnalysisTrack(Map filter, String consql,
			String[] condition) {
		String sql = " select distinct hphm,count(distinct(substr(kdbh, 1, 6))) over(partition by substr(kdbh, 1, 6), hphm) as xzqs,count(distinct(kdbh)) over(partition by kdbh, hphm) as kks,count(distinct(trunc(gcsj))) over(partition by trunc(gcsj), hphm) as ts,count(hphm) over(partition by hphm) as cs from veh_passrec where hphm in ( select hphm from ( ";
		for(int i =0;i<condition.length;i++) {
			sql += condition[i];
			if(i<condition.length-1) {
				sql +=" union all ";
			}
		}
		sql += ") group by hphm having count(hphm) ="+condition.length + ") "+consql+ " order by xzqs desc, kks desc, ts desc,cs desc";
		System.out.println(sql);
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}
	
}
