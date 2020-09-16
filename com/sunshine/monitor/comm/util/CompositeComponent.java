package com.sunshine.monitor.comm.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sunshine.monitor.comm.bean.Menu;
/**
 * Menu Tree composite Component
 * @author OUYANG 2013/05/16
 *
 */
public class CompositeComponent extends AbstractComponent {
	
	/**
	 * Save Children components
	 */
	private List<AbstractComponent> component = new ArrayList<AbstractComponent>();
	
	/**
	 * Set up Menu object
	 * @param menu
	 */
	public CompositeComponent(Menu menu){
		super(menu);
	}
	
	@Override
	public boolean addChildMenu(AbstractComponent component) {
		this.component.add(component);
		return true;
	}

	
	@Override
	public boolean removeChildMenu(AbstractComponent menuTree) {
		this.component.remove(component);
		return true;
	}

	/**
	 * Is or Not Composite component
	 */
	@Override
	public boolean isComposite() {
		return true;
	}
	
	@Override
	public List<AbstractComponent> getChildren(){
		return this.component ;
	}
	
	@Override
	public Iterator<?> iter(){
		return this.component.iterator() ;
	}
	
	public Menu getMenu(){
		return this.menu;
	}
}
