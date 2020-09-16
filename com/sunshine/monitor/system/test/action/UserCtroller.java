package com.sunshine.monitor.system.test.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.system.test.bean.User;
import com.sunshine.monitor.system.test.service.UserManager;

@Controller
@RequestMapping(value="/user.do",params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserCtroller {
	
	@Autowired
	private UserManager userManager ;
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@RequestMapping
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response){
		try {
			System.out.println("第一次查询");
			List<User> list = this.userManager.findUserList();
			for(User user:list){
				System.out.println(user.getId() + "\t" + user.getUsername() + "\t" + user.getPassword());
			}
			
			System.out.println("");
			
			System.out.println("第二次查询");
			List<User> list2 = this.userManager.findUserList();
			for(User user:list2){
				System.out.println(user.getId() + "\t" + user.getUsername() + "\t" + user.getPassword());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView mv = new ModelAndView("test/test");
		return mv ;
	}
}