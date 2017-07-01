package com.limin.blog.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.limin.blog.entity.User;
import com.limin.blog.entity.UserExample;
import com.limin.blog.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private UserService userService;
	
	@RequestMapping("/login")
	public ModelAndView login(String msg) {
		ModelAndView mv = new ModelAndView("/user/login");
		mv.addObject("user", new User());
		if(msg != null) {
			mv.addObject("msg",msg);
		}
		return mv;
	}
	@RequestMapping(value="/loginhandler")
	public String loginhandler(
			@RequestParam(value="username")String username,
			@RequestParam(value="password")String password,
			HttpSession session,
			RedirectAttributes redirectAttritubes) {
		
		User user = userService.login(username, password);
		if(user == null) {
			redirectAttritubes.addFlashAttribute("msg", "用户名或密码不正确");
			return "redirect:/user/login";
		}
		if(user.getState() == 0) {
			redirectAttritubes.addFlashAttribute("msg", "账号未激活");
			return "redirect:/user/login";
		}
		session.setAttribute("loginUser", user);
		return "redirect:/";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("loginUser");
		return "redirect:/";
	}
	
	@RequestMapping("/regist")
	public ModelAndView regist() {
		ModelAndView mv = new ModelAndView("/user/regist");
		mv.addObject("user", new User());
		return mv;
	}
	@RequestMapping("/registhandler")
	public String registhandler(@Valid User user , BindingResult br,Model model) {
		
		if(br.hasErrors()) {
			List<FieldError> list = br.getFieldErrors();
			System.out.println(list);
			return "/user/regist";
		}
		
		userService.regist(user);
		model.addAttribute("info", "我们向您发送了一封邮件，请尽快查收并激活账号！");
		return "info";
	}
	
	@RequestMapping("uniquename")
	@ResponseBody
	public String uniquename(String username) {
		UserExample userExample = new UserExample();
		userExample.createCriteria().andUsernameEqualTo(username);
		List<User> userList = userService.findByExample(userExample);
		if(userList.size() == 0) {
			return "unique";
		}
		return "exist";
	}
	
	@RequestMapping("/auth")
	public String auth(@RequestParam(value="code")String code, Model model) {
		
		UserExample userExample = new UserExample();
		userExample.createCriteria().andCodeEqualTo(code);
		List<User> userList = userService.findByExample(userExample);
		if(userList != null && userList.size() > 0) {
			User user = userList.get(0);
			user.setCode("");//清空认证码
			user.setState((byte) 1);//激活状态
			userService.update(user);
			return "redirect:/";
		}
		model.addAttribute("info", "错误的激活码！");
		return "info";
	}
	
	@RequestMapping("/info")
	public String info() {
		return "";
	}

	
}
