package es.ucm.fdi.dalgs.module.web;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.domain.Module;
import es.ucm.fdi.dalgs.module.service.ModuleService;

@Controller
public class ModuleController {
	@Autowired
	private ModuleService serviceModule;


	private Boolean showAll;

	public Boolean getShowAll() {
		return showAll;
	}

	public void setShowAll(Boolean showAll) {
		this.showAll = showAll;
	}



	/**
	 * methods for adding modules
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/add.htm", method = RequestMethod.GET)
	public String getAddNewModuleForm(Model model, @PathVariable("degreeId") Long id_degree) {
		//		Module newModule = new Module();
		// newDegree.setCode(serviceDegree.getNextCode());
		if(!model.containsAttribute("module"))
			model.addAttribute(new Module());
		model.addAttribute("valueButton", "Add");
		return "module/form";
	}

	@RequestMapping(value = "/degree/{degreeId}/module/add.htm", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String processAddNewModule(
			@PathVariable("degreeId") Long id_degree,
			@ModelAttribute("module") Module newModule,
			BindingResult resultBinding, RedirectAttributes attr) {

		if (!resultBinding.hasErrors()){
			ResultClass<Module> result = serviceModule.addModule(newModule, id_degree);
			if (!result.hasErrors())
				//		if (created)
				return "redirect:/degree/" +id_degree+ ".htm";		
			else{
				//			model.addAttribute("addModule", result.getSingleElement());
				if (result.isElementDeleted()){
					attr.addFlashAttribute("unDelete", result.isElementDeleted()); 
					attr.addFlashAttribute("module", result.getSingleElement());

					//			model.addAttribute("valueButton", "Add");
				}
				else attr.addFlashAttribute("module", newModule);
				attr.addFlashAttribute("errors", result.getErrorsList());
			}
		}else{
			attr.addFlashAttribute("module", newModule);
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.module",
					resultBinding);

		}
		return "redirect:/degree/"+id_degree+"/module/add.htm";
	}


	@RequestMapping(value = "/degree/{degreeId}/module/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@ModelAttribute("module") Module module,
			@PathVariable("degreeId") Long id_degree,
			BindingResult resultBinding, RedirectAttributes attr) {

		if (!resultBinding.hasErrors()){
			ResultClass<Module> result = serviceModule.unDeleteModule(module, id_degree);

			if (!result.hasErrors()){
				attr.addFlashAttribute("module", result.getSingleElement());
				return "redirect:/degree/" + id_degree +  "/module/" + result.getSingleElement().getId() + "/modify.htm";
			
			}else{
				//			attr.addFlashAttribute("module", module);
				if (result.isElementDeleted())
					attr.addAttribute("unDelete", true); 
				//			model.addAttribute("valueButton", "Add");
				attr.addFlashAttribute("errors", result.getErrorsList());

			}
		}else{
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.module",
					resultBinding);

		}
		attr.addFlashAttribute("module", module);
		return "redirect:/degree/"+id_degree+"/module/add.htm";
	}


	/**
	 * Methods for modify modules
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/modify.htm", method = RequestMethod.POST)
	public String formModifyModule(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@ModelAttribute("module") Module modify,
			BindingResult resultBinding, RedirectAttributes attr)

	{
		if (!resultBinding.hasErrors()){
			ResultClass<Boolean> result = serviceModule.modifyModule(modify, id_module, id_degree);
			if (!result.hasErrors())

				return "redirect:/degree/" + id_degree + ".htm";
			else{
				//			attr.addAttribute("modifyModule", modify);
				//			if (result.isElementDeleted()){
				//
				//				attr.addAttribute("addModule", modify);
				//				//					model.addAttribute("unDelete", true); 
				attr.addFlashAttribute("errors", result.getErrorsList());
				//				attr.addAttribute("valueButton", "Modify");

				//			}	
//				attr.addAttribute("errors", result.getErrorsList());

			}
		}
		else{
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.module",
					resultBinding);

		}
		attr.addFlashAttribute("module", modify);
		return "redirect:/degree/"+id_degree+"/module/"+id_module+"/modify.htm";

	}

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/modify.htm", method = RequestMethod.GET)
	protected String formModifyModules(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			Model model)
					throws ServletException {

		//		ModelAndView model = new ModelAndView();
		if (!model.containsAttribute("module")){
			Module p = serviceModule.getModule(id_module).getSingleElement();
			model.addAttribute("module", p);
		}	
		model.addAttribute("valueButton", "Modify");
		//		model.setViewName("module/add");

		return "module/form";
	}

	/**
	 * Methods for delete modules
	 */

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteModules(@PathVariable("moduleId") Long id_module,
			@PathVariable("degreeId") Long id_degree)
					throws ServletException {

		if (serviceModule.deleteModule(serviceModule.getModule(id_module).getSingleElement()
				).getSingleElement()) {
			return "redirect:/degree/" + id_degree + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view modules
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewModule(@PathVariable("moduleId") Long id_module,
			@PathVariable("degreeId") Long id_degree,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean show)
					throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		// Degree p = serviceDegree.getDegree(id);

		Module p = serviceModule.getModuleAll(id_module, show).getSingleElement();
		myModel.put("showAll", show);
		myModel.put("module", p);
		if (p.getTopics() != null)
			myModel.put("topics", p.getTopics());
		//		myModel.put("moduleId", p.getId());

		return new ModelAndView("module/view", "model", myModel);
	}
	
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/restore.htm")
	// Every Post have to return redirect
	public String restoreModule(@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module) {
		ResultClass<Module> result = serviceModule.unDeleteModule(serviceModule.getModule(id_module).getSingleElement(), id_degree);
		if (!result.hasErrors())
			//			if (created)
			return "redirect:/degree/"+id_degree+".htm";
		else{
			return "redirect:/error.htm";

		}

	}
	
	

}
