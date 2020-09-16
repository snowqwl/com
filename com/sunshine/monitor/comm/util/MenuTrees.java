package com.sunshine.monitor.comm.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.bean.Menu;
/**
 * 初始化菜单树
 * @author YANGYANG
 *
 */
public class MenuTrees {
	
	private static MenuTrees instance;
	
	private  Map<String, Menu> menuMap ;
	
	private static Logger log = LoggerFactory.getLogger(MenuTrees.class);
	
	private MenuTrees(){
		menuMap = new ConcurrentHashMap<String, Menu>();
	}
	
	public static MenuTrees getInstance(){
		if(instance == null ){
			instance = new MenuTrees();
		}
		return instance;
	}

	public  boolean addMenu(Menu menu){
		if(!menuMap.containsKey(menu.getId())){
			menuMap.put(menu.getId(), menu);
			return true ;
		}
		return false ;
	}
	
	public  boolean removeMenu(Menu menu){
		if(menuMap.containsKey(menu.getId())){
			menuMap.remove(menu.getId());
			return true ;
		}
		return false;
	}
	
	public  boolean removeMenu(String id){
		if(menuMap.containsKey(id)){
			menuMap.remove(id);
			return true ;
		}
		return false;
	}
	
	public  boolean updateMenu(Menu menu){
		if(menuMap.containsKey(menu.getId())){
			menuMap.remove(menu.getId());
			menuMap.put(menu.getId(), menu);
			return true ;
		}
		return false ;
	}
	
	public Map<String, Menu> getMenuMap(){
		return this.menuMap;
	}
	
	/**
	 * 清空菜单树
	 */
	public void clearMenuTrees(){
		this.menuMap.clear() ;
	}
	
	/**
	 * 自定义构造JSON格式树，用于显示主菜单
	 * 前端用zTree 3.5（复杂数据表示）
	 * @param c
	 * @param sb
	 */
	public void getMenuTrees(AbstractComponent c, StringBuffer sb){
		List<AbstractComponent> testList = c.getChildren();
		int flag = testList.size();
		for(AbstractComponent component:testList){
			Menu menu = component.getMenu();
			if(component.isComposite()){
				sb.append("{name:").append(menu.getId());
				sb.append(",open:true");
				sb.append(",ymdz:").append(menu.getYmdz());
				int childCounts = component.getChildren().size();
				if(childCounts==0){
					sb.append(",isParent:true},");
				}else{
					sb.append(",children:[");
				}
				System.out.println(component.getMenu());
				getMenuTrees(component, sb);
			}else{
				--flag;
				sb.append("{name:").append(menu.getId());
				sb.append(",ymdz:").append(menu.getYmdz());
				sb.append("}");
				if(flag != 0){
					sb.append(",");
				}else{
					sb.append("]},");
				}
				System.out.println("\t -->" + component.getMenu());
			}
		}
	}
	
}
