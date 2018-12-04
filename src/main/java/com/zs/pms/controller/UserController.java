package com.zs.pms.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zs.pms.exception.AppException;
import com.zs.pms.po.TDept;
import com.zs.pms.po.TUSer;
import com.zs.pms.service.DepService;
import com.zs.pms.service.UserService;
import com.zs.pms.vo.QueryUser;

/*
 * 用户控制器
 * **/
@Controller
public class UserController {
	@Autowired
	UserService us;//用户服务
	@Autowired
	DepService ds;//部门服务

	@RequestMapping("/user/list.do") // 二级
	public String list(QueryUser query, String page, ModelMap map) {
		// page是空
		if (page == null || "".equals(page)) {
			page = "1";// 默认第一页
		}
		// 带回分页数据
		map.addAttribute("LIST", us.queryByPage(query, Integer.parseInt(page)));
		// 带回总页数
		map.addAttribute("PAGECONT", us.queryPageCount(query));
		// 回带当前页数
		map.addAttribute("PAGE", page);
		// 回带查询条件
		map.addAttribute("QUERY", query);
		return "user/list";
	}

	@RequestMapping("/user/toadd.do")
	public String toadd(ModelMap map){
		//获取一级部门列表
		List<TDept> list1=ds.queryByPid(0);
		map.addAttribute("DLIST", list1);
		//获取默认一级部门下的二级部门列表
		List<TDept> list2=ds.queryByPid(list1.get(0).getId());
		map.addAttribute("DLIST2", list2);
		return "user/add";
	}
	@RequestMapping("/user/add.do")
	public String add(TUSer user,ModelMap map,HttpSession session){
		try {
			//获取session中的用户信息
			TUSer cuser=(TUSer) session.getAttribute("CUSER");
			user.setCreator(user.getId());//创建人
			user.setIsenabled(1);//可用
	
			us.insert(user);
			return "redirect:list.do";
		} catch (AppException e) {
			// TODO Auto-generated catch block
			map.addAttribute("MSG", e.getErrMsg());
			return this.toadd(map);
		}
	}
	@RequestMapping("/user/delete.do")
	public String delete(int id){
		try {
			us.delete(id);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:list.do";
	}
	@RequestMapping("/user/getdep.do")
	@ResponseBody
	public List<TDept> getDept(int pid){
		List<TDept> list=ds.queryByPid(pid);
		return  list;
	}
	@RequestMapping("/user/deletes.do")
	public String deletes(int[] ids){
		us.deleteByIds(ids);
		return "redirect:list.do";
	}
	@RequestMapping("/user/get.do")
	public String get(int id,ModelMap model){
		TUSer user=us.queryById(id);
		model.addAttribute("USER",user);
		//获得一级部门表
		List<TDept>list1=ds.queryByPid(0);
		model.addAttribute("DLIST",list1);
		//获得该用户的一级部门下的二级部门列表
		List<TDept>list2=ds.queryByPid(user.getDept().getPid());
		model.addAttribute("DLIST2",list2);
		
		return "user/update";
	}
	@RequestMapping("/user/update.do")
	public String update(TUSer user,HttpSession session,ModelMap model){
		//获得session中的用户信息
		TUSer cuser=(TUSer)session.getAttribute("CUSER");
		user.setUpdator(cuser.getId());
		user.setPic("");
		try {
			us.update(user);
			return "redirect:list.do";
		} catch (AppException e) {
			// TODO Auto-generated catch block
			model.addAttribute("MSG",e.getErrMsg());
			return get(user.getId(),model);
		}
	}

}
