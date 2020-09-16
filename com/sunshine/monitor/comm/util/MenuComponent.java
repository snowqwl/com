package com.sunshine.monitor.comm.util;

import java.util.List;

import com.sunshine.monitor.comm.bean.Menu;
/**
 * Menu Component
 * @author OUYANG 2013/05/16
 *
 */
public class MenuComponent extends AbstractComponent {

	public MenuComponent(Menu menu) {
		super(menu);
	}
	
	@Override
	public boolean addChildMenu(AbstractComponent component) {
		throw new UnsupportedOperationException("Not support this operation!");
	}
	
	@Override
	public boolean removeChildMenu(AbstractComponent component) {
		throw new UnsupportedOperationException("Not support this operation!");
	}

	@Override
	public List<AbstractComponent> getChildren(){
		throw new UnsupportedOperationException("Not support this operation!");
	}
	
	@Override
	public boolean isComposite() {
		return false;
	}
	
	public Menu getMenu(){
		return this.menu;
	}
}
