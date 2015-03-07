package es.ucm.fdi.dalgs.academicTerm.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.validation.Valid;

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

import es.ucm.fdi.dalgs.academicTerm.service.AcademicTermService;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.degree.service.DegreeService;
import es.ucm.fdi.dalgs.domain.AcademicTerm;

@Controller
public class AcademicTermController {

	@Autowired
	private AcademicTermService serviceAcademicTerm;

	@Autowired
	private DegreeService serviceDegree;

	@Autowired
	private CourseService serviceCourse;

//	@ModelAttribute("degrees")
//	public List<Degree> degree() {
//		return serviceDegree.getAll();
//	}

	/**
	 * Methods for adding academicTerms
	 */
	@RequestMapping(value = "/academicTerm/add.htm", method = RequestMethod.GET)
	protected String getAddNewAcademicTermForm(Model model) {

		AcademicTerm newAcademicTerm = new AcademicTerm();
		model.addAttribute("addAcademicTerm", newAcademicTerm);
		return "academicTerm/add";

	}

	@RequestMapping(value = "/academicTerm/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewAcademicTerm(
			@ModelAttribute("addacademicTerm") @Valid AcademicTerm newAcademicTerm,
			BindingResult result, Model model) {

		if (newAcademicTerm.getDegree() == null)
			return "redirect://academicTerm/add.htm";

		if (!result.hasErrors()) {

			boolean created = serviceAcademicTerm
					.addAcademicTerm(newAcademicTerm).getE();
			if (created)
				return "redirect:/academicTerm/page/0.htm?showAll";
			else
				return "redirect:/academicTerm/add.htm";
		}
		return "redirect:/error.htm";
	}

	/**
	 * Methods for list academic terms of a term
	 */
	@RequestMapping(value = "/academicTerm/page/{pageIndex}.htm")
	protected ModelAndView formViewAcademicTerm(@PathVariable("pageIndex") Integer pageIndex, 
			@RequestParam(value = "showAll", defaultValue="false") Boolean showAll)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();
		
		/*if (showAll== false) showAll=true;
		else showAll= false;*/
			
		List<AcademicTerm> p = serviceAcademicTerm.getAcademicsTerm(pageIndex, showAll).getE();
		myModel.put("showAll", showAll);
		myModel.put("academicTerms", p);
		Integer numberOfPages = serviceAcademicTerm.numberOfPages(showAll).getE();
		myModel.put("numberOfPages",numberOfPages );
		myModel.put("currentPage", pageIndex);

		return new ModelAndView("academicTerm/list", "model", myModel);
	}

	@RequestMapping(value = "/academicTerm/{academicId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewAcademicTermDegree(
			@PathVariable("academicId") Long id_academic)
			throws ServletException {
		Map<String, Object> myModel = new HashMap<String, Object>();

		AcademicTerm a = serviceAcademicTerm.getAcademicTerm(id_academic).getE();
		myModel.put("academicTerm", a);

		// List<Course> courses =
		// serviceCourse.getCoursesByAcademicTerm(id_degree);

		// if (courses != null)
		myModel.put("courses", a.getCourses());

		return new ModelAndView("academicTerm/view", "model", myModel);
	}

	/**
	 * Methods for modifying a Term
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/modify.htm", method = RequestMethod.GET)
	protected String formModifyAcademics(
			@PathVariable("academicId") Long id_academic, Model model)
			throws ServletException {

		AcademicTerm aT = serviceAcademicTerm.getAcademicTerm(id_academic).getE();
		model.addAttribute("academicTerm", aT);
//		model.addAttribute("degree", aT.getDegree());

		return "academicTerm/modify";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/modify.htm", method = RequestMethod.POST)
	public String formModifySystem(@PathVariable("academicId") Long academicId,
			@ModelAttribute("academicTerm") AcademicTerm newTerm,
			BindingResult result, Model model) {

		if (!result.hasErrors()) {
			AcademicTerm at = serviceAcademicTerm.getAcademicTerm(academicId).getE();
			at.setTerm(newTerm.getTerm());
			boolean success = serviceAcademicTerm.modifyAcademicTerm(at);
			if (success)
				return "redirect:/academicTerm/page/0.htm?showAll";

		}
		return "redirect:/error.htm";

	}

	/**
	 * Delete an academicTerm
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteAcademicTerm(
			@PathVariable("academicId") Long id_academic)
			throws ServletException {

		if (serviceAcademicTerm.deleteAcademicTerm(serviceAcademicTerm.getAcademicTerm(id_academic).getE()).getE()) {
			return "redirect:/academicTerm/page/0.htm?showAll";
		} else
			return "redirect:/error.htm";
	}

}