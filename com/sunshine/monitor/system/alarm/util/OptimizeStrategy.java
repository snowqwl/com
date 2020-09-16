package com.sunshine.monitor.system.alarm.util;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/**
 * 索引策略
 * @author Administrator
 * @see 
 */
public class OptimizeStrategy implements Cloneable {
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
	
	

	public final  String index2="IDX_VEH_ALARMREC_BJDL";
	public final  String index3="IDX_VEH_ALARMREC_BJDWDM";
	public final  String index4="IDX_VEH_ALARMREC_BJSJ";
	public final  String index5="IDX_VEH_ALARMREC_GCXH";
	public final  String index6="IDX_VEH_ALARMREC_GXSJ";
	public final  String index7="IDX_VEH_ALARMREC_HPHM";
	public final  String index8="IDX_VEH_ALARMREC_KDBH";
	public final  String index9="IDX_VEH_ALARMREC_QRR";
	public final  String index10="IDX_VEH_ALARMREC_QRSJ";
	public final  String index11="IDX_VEH_ALARMREC_QRZT";
	public final  String index12="IDX_VEH_ALARMREC_SFFK";
	public final  String index13="IDX_VEH_ALARMREC_SFLJ";
	public final  String index14="IDX_VEH_ALARMREC_SFXDZL";
	public final  String index15="PK_VEH_ALARMREC_BJXH";
	/*//条件：过车时间
	private final static Map<Integer,String> INDEX_GCSJ = new HashMap<Integer,String>();

	//条件： 过车时间 卡点编号 方向编号 车道编号
	private final static Map<Integer,String> INDEX_GCSJ_KDBH_FXBH_CDBH = new HashMap<Integer,String>();*/
	
	//条件：号牌号码 过车时间
	//private final static String INDEX_HPHM_GCSJ ="/*+index("+alias+" LI_PASSREC_HPHM_GCSJ)*/";

	//条件：卡点编号 方向编号 车道编号
	//private final static String INDEX_KDBH_FXBH_CDBH ="/*+index("+alias+" LI_KDBH_FXBH_CDBH)*/";
	//条件：
	//private final static String INDEX_MIX ="/*+index("+alias+" LI_PASSREC_HPHM LI_PASSREC_GCSJ LI_PASSREC_HPHM LI_PASSREC_HPHM_GCSJ LI_GCSJ_KDBH_FXBH_CDBH LI_KDBH_FXBH_CDBH)*/";
	
	{   

		INDEX.put(2, "/*+index("+alias+" "+index2+")*/");
		INDEX.put(3, "/*+index("+alias+" "+index3+")*/");
		INDEX.put(4, "/*+index("+alias+" "+index4+")*/");
		INDEX.put(5, "/*+index("+alias+" "+index5+")*/");
		INDEX.put(6, "/*+index("+alias+" "+index6+")*/");
		INDEX.put(7, "/*+index("+alias+" "+index7+")*/");
		INDEX.put(8, "/*+index("+alias+" "+index8+")*/");
		INDEX.put(9, "/*+index("+alias+" "+index9+")*/");
		INDEX.put(10, "/*+index("+alias+" "+index10+")*/");
		INDEX.put(11, "/*+index("+alias+" "+index11+")*/");
		INDEX.put(12, "/*+index("+alias+" "+index12+")*/");
		INDEX.put(13, "/*+index("+alias+" "+index13+")*/");
		INDEX.put(14, "/*+index("+alias+" "+index14+")*/");
		INDEX.put(15, "/*+index("+alias+" "+index15+")*/");		
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
		if(indexList.size()>0){
			for(int i=0;i<indexList.size();i++){
			index.append(" ");
			index.append(indexList.get(i));
			}
			index.append(")*/");
			return index.toString();
		}
		return "";
	}
	
	//检测是否已经最高级别索引 1 代表最高级别索引
	public boolean check(){
		if(code==1){
			return false;
		}
		return true;
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
	public List<String> getIndexList() {
		return indexList;
	}
	public void addIndexList(String index) {
		indexList.add(index);
	}
	
}
