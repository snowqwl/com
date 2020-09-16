package com.sunshine.monitor.comm.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.sunshine.monitor.comm.bean.Menu;
import com.sunshine.monitor.comm.util.AbstractComponent;
import com.sunshine.monitor.comm.util.MenuTrees;
/**
 * 菜单列表表签
 * @author YANGYANG
 *
 */
@Deprecated
public class MenusCheckboxTag extends SimpleTagSupport{
	
	public void doTag() throws JspException, IOException {
		// menu tree root
//		AbstractComponent component = MenuTrees.getInstance().getRoot();
//		StringBuffer chekboxStr = new StringBuffer(100);
//		buidMenuTreetoCheckbox(component,chekboxStr,"");
//		this.getJspContext().getOut().print(chekboxStr.toString());
	}
	
	private void buidMenuTreetoCheckbox(AbstractComponent c, StringBuffer chekboxStr, String parentId){
//		List<AbstractComponent> list = c.getChildren();
//		int size = list.size() ;
//		for(AbstractComponent component:list){
//			Menu menu = component.getMenu();
//			if(component.isComposite()){
//				chekboxStr.append("<div title=\""+menu.getName()+"\" icon=\"icon-ok\" style=\"padding:10px;\">");
//				buidMenuTreetoCheckbox(component,chekboxStr,menu.getId());
//			}else{
//				size = size -1 ;
//				chekboxStr.append("<input style=\"border:none;\" type=\"checkbox\" name=\"rolemenu\" value=\""+parentId+"_"+menu.getId()+"\"/>"+menu.getName());
//				if(size == 0){
//					chekboxStr.append("</br>");
//				}else{
//					chekboxStr.append("&nbsp;&nbsp;");
//				}
//			}
//		}
//		chekboxStr.append("</div>") ;
	}
}
