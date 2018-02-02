package controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import misc.PrimitiveNumberEditor;
import model.ProductBean;
import model.ProductService;

@Controller
@SessionAttributes(names={"errors","select"})
public class ProductController {
	@Autowired
	private ProductService productService;
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(java.util.Date.class, 
			new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
		webDataBinder.registerCustomEditor(int.class,
				new PrimitiveNumberEditor(java.lang.Integer.class, true));
		webDataBinder.registerCustomEditor(double.class,
				new PrimitiveNumberEditor(java.lang.Double.class, true));
	}

	@RequestMapping(
		path={"/pages/product.controller"},
		method={RequestMethod.GET, RequestMethod.POST}
	)
	public String processGet(ProductBean bean, BindingResult bindingResult, 
		@RequestParam(name="prodaction") String prodaction, 
		@RequestParam Map<String, String> data,Model model) {
		System.out.println("data=" + data);
		Map<String, String> errors = new HashMap<>();
		model.addAttribute("errors", errors);
		if(bindingResult!=null && bindingResult.hasErrors()) {
			if(bindingResult.getFieldError("id")!=null) {
				errors.put("xxx1", "Id必須是整數");
			}
			if(bindingResult.getFieldError("price")!=null) {
				errors.put("xxx2", "Price必須是數字");
			}
			if(bindingResult.getFieldErrorCount("make")!=0) {
				errors.put("xxx3", "Make必須是日期，並且符合YYYY/MM/DD的格式");
			}
			if(bindingResult.getFieldErrorCount("expire")!=0) {
				errors.put("xxx4", "Expire必須是整數");
			}
		}
		if("Insert".equals(prodaction) || "Update".equals(prodaction) || "Delete".equals(prodaction)) {
			if(data.get("id")==null || data.get("id").length()==0) {
				errors.put("xxx1", "請輸入Id以便執行"+prodaction);
			}
		}
		
		if(errors!=null && !errors.isEmpty()) {
			return "product.error";
		}
		
//根據model執行結果呼叫view元件
		
		if("Select".equals(prodaction)) {
			List<ProductBean> result = productService.select(bean);
			model.addAttribute("select", result);
			return "display";
		} else if("Insert".equals(prodaction)) {
			ProductBean result = productService.insert(bean);
			if(result==null) {
				errors.put("action", "Insert fail");
			} else {
				model.addAttribute("insert", result);
			}
			return "product.error";
			
		} else if("Update".equals(prodaction)) {
			ProductBean result = productService.update(bean);
			if(result==null) {
				errors.put("action", "Update fail");
			} else {
				model.addAttribute("update", result);
			}
			return "product.error";
			
		} else if("Delete".equals(prodaction)) {
			boolean result = productService.delete(bean);
			if(!result) {
				model.addAttribute("delete", 0);
			} else {
				model.addAttribute("delete", 1);
			}
			return "product.error";

		} else {
			errors.put("action", "Unknown Action:"+prodaction);
			return "product.error";
		}
	}
	
}
