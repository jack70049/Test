package controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import model.CustomerBean;
import model.CustomerService;

@Controller
@SessionAttributes(names={"user"})
public class LoginController {
	@Autowired
	ApplicationContext context;
	@Autowired
	MessageSource messageSource;
	@Autowired
	private CustomerService customerService;
	@RequestMapping(
			path="/secure/login.controller",
			method={RequestMethod.GET, RequestMethod.POST}
	)
	public String processing(@RequestParam(name="username") String username,
			@RequestParam("password") String password, Model model) {
// 驗證資料
		Map<String, String> errors = new HashMap<>();
		model.addAttribute("errors", errors);
		
		Locale locale = LocaleContextHolder.getLocale();
		
		if (username == null || username.trim().length() == 0) {
			errors.put("username", 
					messageSource.getMessage("login.username.required", null, locale));
		}
		if (password == null || password.trim().length() == 0) {
			errors.put("password", 
					messageSource.getMessage("login.password.required", null, locale));
		}
		if (errors != null && !errors.isEmpty()) {
			return "login.error";
		}

// 呼叫model
		CustomerBean bean = customerService.login(username, password);

// 根據model執行結果呼叫view元件
		if (bean == null) {
			errors.put("password", messageSource.getMessage("login.error", null, locale));
			return "login.error";
		} else {
			model.addAttribute("user", bean);
			return "login.success";
		}
	}
}
