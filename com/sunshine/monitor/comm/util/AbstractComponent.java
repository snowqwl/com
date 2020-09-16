package com.sunshine.monitor.comm.util;

import java.util.Iterator;
import java.util.List;

import com.sunshine.monitor.comm.bean.Menu;

/**
 * Building Menu Trees
 * 
 * @author OUYANG 2013/05/16
 *
 */
public abstract class AbstractComponent implements Cloneable{
	
	// Menu self
	public Menu menu;
	
	public AbstractComponent(Menu menu){
		this.menu = menu ;
	}
	
	/**
	 * Add Child Menu
	 * @return
	 */
	public abstract boolean addChildMenu(AbstractComponent component);
	
	/**
	 * Remove Child Menu
	 * @return
	 */
	public abstract boolean removeChildMenu(AbstractComponent component) ;
	
	/**
	 * Is or not Composite component
	 * @return
	 */
	public abstract boolean isComposite();
	
	
	/**
	 * Children menu
	 * @return
	 */
	public List<AbstractComponent> getChildren(){
		return null ;
	}
	
	/**
	 * Trees iterator(遍列树)
	 * @return
	 */
	public Iterator<?> iter(){
		return null ;
	}
	
	/**
	 * 获取菜单
	 * @return
	 */
	public abstract Menu getMenu();
	
}
