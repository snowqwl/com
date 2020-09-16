package com.sunshine.monitor.comm.util.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.dao.annotation.Column;
import com.sunshine.monitor.comm.util.ReflectUtils;
import com.sunshine.monitor.comm.util.orm.bean.PreSqlEntry;
import com.sunshine.monitor.comm.util.orm.bean.StaticEntry;
import com.sunshine.monitor.comm.util.orm.exception.FormatSqlException;
import com.sunshine.monitor.comm.util.orm.exception.InitTableInfoException;
import com.sunshine.monitor.comm.util.orm.exception.ORMException;
import com.sunshine.monitor.comm.util.orm.exception.ReflectMethodException;
import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.comm.util.orm.helper.Pager;

/**
 * 自动生成相关的pre_sql 
 * 只针对 oracle
 * @author lifenghu
 *
 */
public class SqlUtils {
	private static TableInfos tableInfos = TableInfos.INSTANS;
	private static Logger log = LoggerFactory.getLogger(SqlUtils.class);
	private static Pattern p = Pattern.compile("(?<=select).+(?=from.+)");
	private static Object cacheLock = new Object();
	/** 非线程安全类，必须在同步块内插入 */
	private static Map<String,Method> methoCache = new HashMap<String,Method>();
	private static List<Convert> convertList = new ArrayList<Convert>();
	static{
		//字符转日期
		convertList.add(new Convert() {
			public boolean isCanConvert(int dbType, Object obj) {
				if(dbType == Types.DATE || dbType == Types.TIMESTAMP|| dbType == Types.TIME){
					if(obj!=null && obj instanceof String && StringUtils.isNotBlank((String)obj)) return true;
				}
				return false;
			}
			public Object exec(Object obj) {
				try {
					return DateUtils.parseDate(obj.toString(), new String[]{"yyyy-MM-dd HH:mm:ss"});
				} catch (ParseException e) {
					throw new RuntimeException("不符合日期格式字段无法插入:"+obj);
				}
			}
		});
	}
	
	private SqlUtils(){ }
	
	public static PreSqlEntry getPreSelectSqlByPage(PreSqlEntry entry, Pager pager) 
			throws FormatSqlException {
		String sql = entry.getSql().trim();
		Matcher m = p.matcher(sql);
		if(!m.find()) throw new FormatSqlException(" 无法解析的格式，格式必须为 select ... from ");
		String selectStr = m.group();
		String selectStrInner = "ROWNUM as rNum , " +selectStr;
		String pagerSql = "select  %1$s  from ( select %2$s from ( %3$s ) t " +
				"where ROWNUM <= %4$s ) t where t.rNum >= %5$s";
		String resultSql = String.format(pagerSql, selectStr, selectStrInner,sql,
				pager.getEndRow(),pager.getStartRow());
		entry.setSql(resultSql);
		return entry;
	}
	
	public static PreSqlEntry getPreSelectSql(String tableName, String selectStr ,Cnd cnd)
			throws InitTableInfoException {
		StringBuilder sqlBuff = new StringBuilder("select :selectStr from ") ; 
		sqlBuff.append(tableName);
		sqlBuff.append(" t  ");
		//TODO 没有进行数据库类型检查，不安全，有待改进
		sqlBuff.append(" where ");
		sqlBuff.append(cnd.getConditions().toSql()).append(" ");
		
		if(null != cnd.getOrders())  sqlBuff.append(cnd.getOrders().toSql()).append(" ");;
		
		String sql = sqlBuff.toString().replace(":selectStr", selectStr==null?" t.* " : selectStr);
		PreSqlEntry p = new PreSqlEntry();
		p.setSql(sql);
		List<Object>values = cnd.getConditions().getValues();
		p.setValues(values);
		String outStr = "执行SQL:"+p.getSql()+"/value:";
		for(Object value : values){
			outStr += value.toString()+",";
		}
		log.debug(outStr);
		return p;
	}
	
	/**
	 * 此方法依赖字段注明 {@link com.sunshine.monitor.comm.dao.annotation.Column} , 不依赖于数据库表
	 * @param tableName
	 * @param clazz
	 * @param defaultFieldSqlMap
	 * @return
	 * @throws InitTableInfoException
	 * @throws ReflectMethodException
	 * @throws ParseException
	 */
	public static Object[] getPreInsertSql(String tableName,Class<?> clazz,Map<String,String> defaultFieldSqlMap ) 
			throws InitTableInfoException, 
			ReflectMethodException, ParseException{
			Field[] fs = clazz.getDeclaredFields();
			List<String> fields = new ArrayList<String>();
			StringBuilder sb = new StringBuilder("insert into "+tableName+"(");
			StringBuilder preSb = new StringBuilder();
			for(int i=0 ; i<fs.length ; i++ ){
				Column c = fs[i].getAnnotation(Column.class);
				if(c == null ) {
					continue;
				}
				String fieldName = fs[i].getName();
				String value = defaultFieldSqlMap==null?null:defaultFieldSqlMap.get(fieldName);
				sb.append(fieldName).append(",");
				if(value == null){
					fields.add(fs[i].getName());
					preSb.append("?,");
				} else {
					preSb.append(value).append(",");
				}
			}
			sb.delete(sb.length()-1, sb.length());
			preSb.delete(preSb.length()-1, preSb.length());
			sb.append(") values (");
			sb.append(preSb).append(")");
			Object[] result = new Object[2];
			result[0] = sb.toString();
			result[1] = fields;
			return result;
	}
	
	/**
	 * 给出一个插入的sql,插入所有obj不为null的字段值
	 * @param <T>
	 * @param obj orm类
	 * @param tableName 表名
	 * @param userDefSql 自定义的 (..,key) values (..,value) value为SQL语句,例如 key:value=id:'seq.nextVal()'
	 * @return
	 * @throws InitTableInfoException 
	 * @throws ReflectMethodException 
	 * @throws ParseException  数据库为Date , orm类为String 字段，但无法解析
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	public static PreSqlEntry getPreInsertSqlByObject(String tableName,Object obj,Map<String,String> userMap ) 
			throws InitTableInfoException, 
			ReflectMethodException, ParseException{
		Map<String,Object> notNullMap = getNotNullGetFieldMap(tableName,obj);
		joinUserMap(notNullMap, userMap);
		StringBuffer insertSb = new StringBuffer("insert into ");
		
		insertSb.append(tableName).append(" ( ")
			.append(StringUtils.join(notNullMap.keySet(), ","))
			.append(" ) values ( ");
		List<Object > values = new ArrayList<Object>();
		for(Map.Entry<String, Object> fieldEntry : notNullMap.entrySet()){
			if(fieldEntry.getValue() instanceof StaticEntry){
				insertSb.append(fieldEntry.getValue().toString()).append(",");
			}else {
				insertSb.append("?,");
				values.add(convert(tableName, fieldEntry.getKey(), fieldEntry.getValue()));
			}
		}
		insertSb.delete(insertSb.length()-1, insertSb.length());
		insertSb.append(" ) ");
		PreSqlEntry entry = new PreSqlEntry();
		entry.setSql(insertSb.toString());
		entry.setValues(values);
		if(log.isDebugEnabled()){
		String outStr = "执行SQL:"+entry.getSql()+"/value:";
			for(Object value : values){
				outStr += value.toString()+",";
			}
			log.debug(outStr);
		}
		return entry;
	}
	
	/**
	 * 给出一个更新的sql,更新所有obj不为null的字段值
	 * @param tableName 更新表名
	 * @param obj 映射对象
	 * @param defaultFieldSqlMap 自定义的 (..,key) values (..,value) value为SQL语句
	 * @param key 更新的key where key=keyValue
	 * @return
	 * @throws ORMException 
	 */
	public static PreSqlEntry getPreUpdateSqlByObject(final String tableName,final Object obj,
			Map<String,String> userMap ,final String... keys) 
			throws ORMException {
		Map<String,Object> keyMap = new HashMap<String,Object>();
		Map<String,Object> notNullMap = getNotNullGetFieldMap(tableName,obj);
		joinUserMap(notNullMap, userMap);
		
		for(String key:keys){
			Object keyValue = notNullMap.remove(key);
			keyMap.put(key,keyValue);
			if(keyValue == null) throw new ORMException("更新主键值不可为null");
		}
		
		StringBuilder sqlSb = new StringBuilder("update ");
		sqlSb.append(tableName).append(" ").append("set").append(" ");
		List<Object> values = new ArrayList<Object>();
		for(Map.Entry<String, Object> fieldEntry : notNullMap.entrySet()){
			if(fieldEntry.getValue() instanceof StaticEntry){
				sqlSb.append(fieldEntry.getKey()).append("=").append(fieldEntry.getValue());
			} else {
				sqlSb.append(fieldEntry.getKey()).append("= ?");
				values.add(convert(tableName,fieldEntry.getKey(),fieldEntry.getValue()));
			}
			sqlSb.append(",");
		}
		sqlSb.delete(sqlSb.length()-1, sqlSb.length());
		sqlSb.append(" where ");
		for(String key : keys) {
			sqlSb.append(key).append(" = ? and ");
			values.add(convert(tableName,key,keyMap.get(key)));
		}
		sqlSb.delete(sqlSb.length()-5,sqlSb.length());
		PreSqlEntry entry = new PreSqlEntry();
		entry.setSql(sqlSb.toString());
		entry.setValues(values);
		if(log.isDebugEnabled()){
			String outStr = "执行SQL:"+entry.getSql()+"/value:";
			for(Object value : values){
				outStr += value.toString()+",";
			}
			log.debug(outStr);
		}
		return entry;
	}
	
	private static Object convert(String tableName, String key, Object obj) throws InitTableInfoException {
		int dbType = tableInfos.get(tableName).get(key);
		for(Convert c : convertList){
			if(c.isCanConvert(dbType, obj)) return c.exec(obj);
		}
		return obj;
	}
	
	private static <T> Map<String,Object> getNotNullGetFieldMap(String tableName , T obj) 
			throws InitTableInfoException, 
			ReflectMethodException{
		Field[] fields = obj.getClass().getDeclaredFields();
		Map<String,Object> map = new HashMap<String,Object>();
		for(Field f : fields){
			//检查数据库是否存在该字段
			if(tableInfos.get(tableName).get(f.getName()) == null) continue;
			Method m = buildGetMethod(obj.getClass(),f.getName());
			
			Object objValue;
			try {
				objValue = m.invoke(obj);
			} catch (Exception e) {
				throw new ReflectMethodException(e);
			} 
			if(objValue!=null){
				map.put(f.getName(), objValue);
			}
				
		}
		return map;
	}
	
	private static Method buildGetMethod(Class<?> clazz,String fieldName){
		String getMehodName = "get"+StringUtils.capitalize(fieldName);
		String key = clazz.getName()+getMehodName;
		Method method = methoCache.get(key);
		if(method == null){
			synchronized (cacheLock) {
				if(method == null){
					try {
						method = clazz.getMethod(getMehodName);
					} catch (SecurityException e) {
					} catch (NoSuchMethodException e) {
					}
					methoCache.put(key, method);
				}
			}
		}
		return method;
	}
	
//	private static Method buildSetMethod(Class<?> clazz,String fieldName){
//		throw new UnsupportedOperationException("未实现方法");
//	}
	
	private static void joinUserMap(Map<String,Object> notNullMap, Map<String, String> userMap){
		if(userMap == null || userMap.size()==0) return ;
		for(Map.Entry<String, String> entry : userMap.entrySet()){
			notNullMap.put(entry.getKey(), new StaticEntry(entry.getValue()));
		}
	}
}
