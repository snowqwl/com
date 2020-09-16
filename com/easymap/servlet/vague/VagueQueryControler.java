package com.easymap.servlet.vague;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easymap.dao.IVagueDao;
import com.easymap.util.XMLOutputterUtil;

@Controller
@RequestMapping(value="/vagueQueryCtrl.do",params="method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VagueQueryControler  {
	
	@Autowired
	@Qualifier("VagueQueryDao")
	private IVagueDao vagueQueryDaoImpl;
	

	@RequestMapping()
	@ResponseBody
	public void vagueQuery(HttpServletRequest request,HttpServletResponse response,String userid){
		String userids = request.getParameter("userid");
		try {

			String querykey = request.getParameter("querykey");
			String querytype = request.getParameter("querytype");
			String layer = request.getParameter("layer");
			int startpos = Integer.parseInt(request.getParameter("startnum"));
			int num = Integer.parseInt(request.getParameter("num"));
			String points = request.getParameter("points");
			String vaguesel = request.getParameter("vaguesel");
			String selorg = request.getParameter("selorg");
			System.out.println("querykey:" + querykey);
			System.out.println("querytype:" + querytype);

			long start = System.currentTimeMillis();

			if(vaguesel == null){
				XMLOutputterUtil.response(vagueQueryDaoImpl.findVagueSearch(querytype,querykey,layer, startpos, num, points), response);
			}else{
				XMLOutputterUtil.response(vagueQueryDaoImpl.findVagueSearch(querytype,querykey,selorg,layer, startpos, num), response);
			}

			long end = System.currentTimeMillis();

			System.out.println("====================查询耗时:" + (end - start) + "毫秒========================");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping()
	@ResponseBody
	public void jczVagueQuery(HttpServletRequest request,HttpServletResponse response,String userid){
		String userids = request.getParameter("userid");
		try {
			String querykey = request.getParameter("querykey");
			String querytype = request.getParameter("querytype");
			String layer = request.getParameter("layer");
			int startpos = Integer.parseInt(request.getParameter("startnum"));
			int num = Integer.parseInt(request.getParameter("num"));
			String points = request.getParameter("points");
			String vaguesel = request.getParameter("vaguesel");
			String selorg = request.getParameter("selorg");
			System.out.println("querykey:" + querykey);
			System.out.println("querytype:" + querytype);

			long start = System.currentTimeMillis();

			if(vaguesel == null){
				XMLOutputterUtil.response(vagueQueryDaoImpl.findVagueSearch(querytype,querykey,layer, startpos, num, points), response);
			}else{
				XMLOutputterUtil.response(vagueQueryDaoImpl.findJczVagueSearch(querytype,querykey,selorg,layer, startpos, num), response);
			}

			long end = System.currentTimeMillis();

			System.out.println("====================查询耗时:" + (end - start) + "毫秒========================");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping()
	@ResponseBody
	public void kkVagueQuery(HttpServletRequest request,HttpServletResponse response,String userid){
		String userids = request.getParameter("userid");
		try {
			String querykey = request.getParameter("querykey");
			String querytype = request.getParameter("querytype");
			String layer = request.getParameter("layer");
			int startpos = Integer.parseInt(request.getParameter("startnum"));
			int num = Integer.parseInt(request.getParameter("num"));
			String points = request.getParameter("points");
			String vaguesel = request.getParameter("vaguesel");
			String selorg = request.getParameter("selorg");
			System.out.println("querykey:" + querykey);
			System.out.println("querytype:" + querytype);

			long start = System.currentTimeMillis();

			if(vaguesel == null){
				XMLOutputterUtil.response(vagueQueryDaoImpl.findVagueSearch(querytype,querykey,layer, startpos, num, points), response);
			}else{
				XMLOutputterUtil.response(vagueQueryDaoImpl.findkkVagueSearch(querytype,querykey,selorg,layer, startpos, num), response);
			}

			long end = System.currentTimeMillis();

			System.out.println("====================查询耗时:" + (end - start) + "毫秒========================");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping()
	@ResponseBody
	public void gzjkVagueQuery(HttpServletRequest request,HttpServletResponse response,String userid){
		String userids = request.getParameter("userid");
		try {
			String querykey = request.getParameter("querykey");
			String querytype = request.getParameter("querytype");
			String layer = request.getParameter("layer");
			int startpos = Integer.parseInt(request.getParameter("startnum"));
			int num = Integer.parseInt(request.getParameter("num"));
			String points = request.getParameter("points");
			String vaguesel = request.getParameter("vaguesel");
			String selorg = request.getParameter("selorg");
			System.out.println("querykey:" + querykey);
			System.out.println("querytype:" + querytype);

			long start = System.currentTimeMillis();

			if(vaguesel == null){
				XMLOutputterUtil.response(vagueQueryDaoImpl.findVagueSearch(querytype,querykey,layer, startpos, num, points), response);
			}else{
				XMLOutputterUtil.response(vagueQueryDaoImpl.findGajkVagueSearch(querytype,querykey,selorg,layer, startpos, num), response);
			}

			long end = System.currentTimeMillis();

			System.out.println("====================查询耗时:" + (end - start) + "毫秒========================");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping()
	@ResponseBody
	public void dzwlVagueQuery(HttpServletRequest request,HttpServletResponse response,String userid){
		String userids = request.getParameter("userid");
		try {
			String querykey = request.getParameter("querykey");
			String querytype = request.getParameter("querytype");
			String layer = request.getParameter("layer");
			int startpos = Integer.parseInt(request.getParameter("startnum"));
			int num = Integer.parseInt(request.getParameter("num"));
			String points = request.getParameter("points");
			String vaguesel = request.getParameter("vaguesel");
			String selorg = request.getParameter("selorg");
			System.out.println("querykey:" + querykey);
			System.out.println("querytype:" + querytype);

			long start = System.currentTimeMillis();

			if(vaguesel == null){
				XMLOutputterUtil.response(vagueQueryDaoImpl.findVagueSearch(querytype,querykey,layer, startpos, num, points), response);
			}else{
				XMLOutputterUtil.response(vagueQueryDaoImpl.findDzwlVagueSearch(querytype,querykey,selorg,layer, startpos, num), response);
			}

			long end = System.currentTimeMillis();

			System.out.println("====================查询耗时:" + (end - start) + "毫秒========================");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping()
	@ResponseBody
	public void pdtVagueQuery(HttpServletRequest request,HttpServletResponse response,String userid){
		String userids = request.getParameter("userid");
		try {
			String querykey = request.getParameter("querykey");
			String querytype = request.getParameter("querytype");
			String layer = request.getParameter("layer");
			int startpos = Integer.parseInt(request.getParameter("startnum"));
			int num = Integer.parseInt(request.getParameter("num"));
			String points = request.getParameter("points");
			String vaguesel = request.getParameter("vaguesel");
			String selorg = request.getParameter("selorg");
			System.out.println("querykey:" + querykey);
			System.out.println("querytype:" + querytype);

			long start = System.currentTimeMillis();

			if(vaguesel == null){
				XMLOutputterUtil.response(vagueQueryDaoImpl.findVagueSearch(querytype,querykey,layer, startpos, num, points), response);
			}else{
				XMLOutputterUtil.response(vagueQueryDaoImpl.findPdtVagueSearch(querytype,querykey,selorg,layer, startpos, num), response);
			}

			long end = System.currentTimeMillis();

			System.out.println("====================查询耗时:" + (end - start) + "毫秒========================");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
