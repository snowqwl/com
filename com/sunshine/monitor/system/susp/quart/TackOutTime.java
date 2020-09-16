package com.sunshine.monitor.system.susp.quart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.maintain.SmsFacade;
import com.sunshine.monitor.comm.maintain.dao.MaintainDao;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.susp.dao.SuspinfoApproveDao;

/**
 * 统计超时定时任务,发短信提醒用户
 * 
 * @author admin
 * 
 */
public class TackOutTime {
	
	public void SuspinfoCancelOut() {
		SuspinfoApproveDao suspinfoApproveDao = (SuspinfoApproveDao) SpringApplicationContext
				.getStaticApplicationContext().getBean("suspinfoApproveDao");
		MaintainDao maintainDao = (MaintainDao) SpringApplicationContext
		.getStaticApplicationContext().getBean("maintainDao");
		// 为避免数据量过大的情况，使用分页查询实现
		int page = 1;
		int pagesize = 2000;
		SmsFacade smsFacade = new SmsFacade();
		Map<String, String> dxcondition = new HashMap<String, String>();
		while (true) {
			try {
				List<Map<String, Object>> list = (List<Map<String, Object>>) suspinfoApproveDao
						.getSuspinfoOuttime(page, pagesize).get("rows");
				for(Map<String,Object> map : list) {
					String mobile = map.get("lxdh").toString();
					String jsyh = map.get("bkr").toString();
					String content = "您有"+map.get("overcount").toString()+"条超时未撤控记录尚未及时处理";
					smsFacade.sendAndPostSms(mobile, content, null);
					dxcondition.put("xh", "");
					dxcondition.put("ywlx", "004");
					dxcondition.put("dxjsr", jsyh);
					dxcondition.put("dxjshm", mobile);
					dxcondition.put("dxnr", content);
					String value="id,xh,ywlx,dxjsr,dxjshm,dxnr";
					String values=""
						+"SEQ_FRM_COMMUNICATION.nextval,'"
						+dxcondition.get("xh")
						+"','"
						+dxcondition.get("ywlx")
						+"','"
						+dxcondition.get("dxjsr")
						+"','"
						+dxcondition.get("dxjshm")
						+"','"
						+dxcondition.get("dxnr")
						+"'"
						;
					maintainDao.saveDxsj(value,values);
				}
				
				page++;
				if(list.size()<2000) {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}
}
