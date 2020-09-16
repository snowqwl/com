package com.sunshine.monitor.system.analysis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.system.analysis.bean.JmYearInspection;
import com.sunshine.monitor.system.analysis.dao.YearInspectionDao;
import com.sunshine.monitor.system.analysis.service.YearInspectionService;

@Service
@Transactional
public class YearInspectionServiceImpl implements YearInspectionService {

	@Autowired
	private YearInspectionDao yearInspectionDao;
	/**
	 * 车辆检测结果数据导出
	 * @return
	 */
	@OperationAnnotation(type=OperationType.DOWNLOAD_RESULT,description = "车辆检测结果数据导出")
	public void downloadResult() {
		

	}

	public int saveYearInspection(JmYearInspection bean) throws Exception {

		return this.yearInspectionDao.saveYearInspection(bean);
	}

}
