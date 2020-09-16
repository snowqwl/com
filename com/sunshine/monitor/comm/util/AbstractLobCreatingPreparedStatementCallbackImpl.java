package com.sunshine.monitor.comm.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
/**
 * 图片、文件二进制操作
 * 需要配置:NativeJdbcExtractor、lobHandler(spring-db.xml)
 * @author OUYANG
 *
 */
public class AbstractLobCreatingPreparedStatementCallbackImpl extends
		AbstractLobCreatingPreparedStatementCallback {
	private Object parameterObject;

	public Object getParameterObject() {
		return this.parameterObject;
	}

	public void setParameterObject(Object parameterObject) {
		this.parameterObject = parameterObject;
	}

	public AbstractLobCreatingPreparedStatementCallbackImpl(
			LobHandler lobHandler) {
		super(lobHandler);
	}

	public AbstractLobCreatingPreparedStatementCallbackImpl(
			LobHandler lobHandler, Object parameterObject) {
		super(lobHandler);
		setParameterObject(parameterObject);
	}

	protected void setValues(PreparedStatement pstmt, LobCreator lobCreator)
			throws SQLException, DataAccessException {
	}
}
