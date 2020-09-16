package com.sunshine.monitor.comm.util;

import java.util.List;

import com.sunshine.monitor.comm.bean.Menu;

public class MenuTreeUtil {
	
	/**
	 * 自定义构造JSON格式树，用于显示主菜单
	 * 前端用zTree 3.5显示
	 * @param c
	 * @param sb
	 */
	@Deprecated
	public static void getMenuTrees(AbstractComponent c, StringBuffer sb){
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
