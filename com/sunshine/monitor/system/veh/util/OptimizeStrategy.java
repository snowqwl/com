package com.sunshine.monitor.system.veh.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;





/**
 * 索引策略
 * @author Administrator
 *
 */
public class OptimizeStrategy implements Cloneable{
	private  static OptimizeStrategy opt = new OptimizeStrategy();
	

	//带别名的构造器
	public  OptimizeStrategy(String alias){
		this.alias=alias;
	}
	//默认构造
	public OptimizeStrategy(){
		super();
	}
	
	//获取克隆实例,减少创建实体类的资源(推荐用这种方法进行实体构造)
	public static OptimizeStrategy getCloneEntity(){
		try {
			return (OptimizeStrategy) opt.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		//如果获取克隆实例失败就重新创建一个实例
		return new OptimizeStrategy(); 
	}
	
	//获取克隆实例,减少创建实体类的资源(推荐用这种方法进行实体构造)--带别名
	public static OptimizeStrategy getCloneEntity(String alias){
		try {
			opt.setAlias(alias);
			return (OptimizeStrategy) opt.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		//如果获取克隆实例失败就重新创建一个实例
		return new OptimizeStrategy(alias); 
	}
	
	//别名
	private  String alias="t";
	
	//条件：号牌号码
	private final  Map<Integer,String> INDEX = new HashMap<Integer,String>();
	
	//index1默认为为最高级索引
	public final  String index1="INDEX_PASSREC_HPHM_GCSJ";
	public final  String index2="INDEX_PASSREC_HPHM_GCSJ";
	public final  String index3="LI_PASSREC_GCSJ_KDBH_FXBH";
	public final  String index4="LI_PASSREC_GCSJ_HPHM_KDBH_FXBH";
	
	
	{
		INDEX.put(1, "/*+index("+alias+" "+index1+")*/");
		INDEX.put(2, "/*+index("+alias+" "+index2+")*/");
		INDEX.put(3, "/*+index("+alias+" "+index3+")*/");
		INDEX.put(4, "/*+index("+alias+" "+index4+")*/");
	}
	
	//索引代号
	private int code=0 ;
	//索引描述
	private String desc="" ;
    //索引集合
	private List<String> indexList =new ArrayList<String>();
	
	//使用多个索引
	public String algorithmMix(){
		StringBuffer index = new StringBuffer("/*+index(");
		index.append(alias);
		if(check()){
			if(indexList.size()>0){
				for(int i=0;i<indexList.size();i++){
				index.append(" ");
				index.append(indexList.get(i));
				}
				index.append(")*/");
				return index.toString();
			}
		}else{
			return index.append(" ").append(index1).append(")*/").toString();
		 }
		
		return "";
	}
	

	public String algorithm(){
		String index = INDEX.get(code);
		if(StringUtils.isNotBlank(index))
		return index;
		return "";
	}
	
	//检测是否已经最高级别索引 1 代表最高级别索引
	public boolean check(){
		if(code==1){
			return false;
		}
		return true;
	}
	
	public void addIndexList(String index) {
		indexList.add(index);
	}
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public  Map<Integer, String> getIndex() {
		return INDEX;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
