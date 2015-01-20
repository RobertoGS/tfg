package com.example.tfg.web;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Activity;
import com.example.tfg.domain.Competence;
import com.example.tfg.domain.CompetenceStatus;
import com.example.tfg.domain.Course;
import com.example.tfg.domain.Subject;
import com.example.tfg.service.AcademicTermService;
import com.example.tfg.service.ActivityService;
import com.example.tfg.service.CompetenceService;
import com.example.tfg.service.CourseService;
import com.example.tfg.service.SubjectService;
import com.example.tfg.web.form.FormModifyActivity;

@Controller
public class ActivityController {


	@Autowired
	private ActivityService serviceActivity;

	//@Autowired
	//private SubjectService serviceSubject;

	@Autowired
	private CourseService serviceCourse;
	
	@Autowired
	private CompetenceService serviceCompetence; 
	
	@Autowired
	private AcademicTermService serviceAcademicTerm;
	
	private static final Logger logger = LoggerFactory
			.getLogger(ActivityController.class);

	

//	@ModelAttribute("competences")
//	public List<Competence> competences() {
//		return serviceCompetence.getAll();
//	}
	
//	@ModelAttribute("courses")
//	public List<Course> courses() {
//		return serviceCourse.getAll();
//	}
	
//	@ModelAttribute("competenceStatus")
//	public List<CompetenceStatus> competencestatus() {
//		return (List<CompetenceStatus>) col;
//	}

//	private List<CompetenceStatus> col = new ArrayList<CompetenceStatus>();
	/**
	 * Methods for adding activities
	 */
	

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/add.htm", method = RequestMethod.GET)
	protected String getAddNewActivityForm(@PathVariable("academicId") Long id_Long, @PathVariable("courseId") Long id_course, Model model) {
		Activity newActivity = new Activity();
		newActivity.setCode(serviceActivity.getNextCode());
		newActivity.setCourse(serviceCourse.getCourse(id_course));
		model.addAttribute("addactivity", newActivity);		
	
		return "activity/add";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewCompetence(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("addactivity") @Valid Activity newactivity, BindingResult result, 
			Model model) {
		
//		if(!result.hasErrors()){
//			/*if( competencestatus.getPercentage() <= 0.0 || competencestatus.getPercentage() > 100.0)		
//				return "redirect:/activity/add.htm";
//				
//			for(CompetenceStatus cs: col){
//					if (cs.getCompetence()getId_competence() == competencestatus.getId_competence()){
//						return "redirect:/activity/add.htm";
//					}
//			}			*/
//			
//			//col.add(competencestatus);
//			return "redirect:/activity/add.htm";
			
			// return "redirect:/activity/add.htm";

			//newactivity.getCompetenceStatus().add(competencestatus);  //CASCA AQUI!!!
			//model.addAttribute("addactivity", newactivity);
			//return "redirect:/activity/add.htm";
		//}
		
		//if(course == null)
		//	return "redirect:/activity/add.htm";
		
		if (!result.hasErrors()) {
			newactivity.setCourse(serviceCourse.getCourse(id_course));
			
			boolean created = serviceActivity.addActivity(newactivity);
			if (created){
				
				return "redirect:/academicTerm/" + id_academicTerm + "/course/"+id_course+"/activity/"+ newactivity.getId() +"/modify.htm";
			}
			else
				return "redirect:/activity/add.htm";
		}
		return "redirect:/error.htm";
	}



	/**
	 * Methods for listing activities
	 */

//	@RequestMapping(value = "/course/{idCourse}/activity/list.htm")
//	public ModelAndView handleRequestActivityList(@PathVariable("idCourse") Long id_course, HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//
//		Map<String, Object> myModel = new HashMap<String, Object>();
//
//		List<Activity> result = serviceActivity.getActivitiesForCourse(id_course);
//		myModel.put("activities", result);
//
//		return new ModelAndView("activity/list", "model", myModel);
//	}

	/**
	 * Methods for modifying activities
	 */
	
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/modify.htm", method = RequestMethod.GET)
	protected String formModifyActivities(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course, @PathVariable("activityId") long id_activity,
			Model model) throws ServletException {

		Activity p = serviceActivity.getActivity(id_activity);
		AcademicTerm at = serviceAcademicTerm.getAcademicTerm(id_academic);
		model.addAttribute("idCourse",id_course);
		List<Competence> competences = serviceCompetence.getCompetencesForDegree(at.getDegree().getId());
		
		FormModifyActivity fAct = new FormModifyActivity();
		fAct.setActivity(p);
		fAct.setCompetences(competences);
		
		model.addAttribute("competenceStatus", fAct.getActivity().getCompetenceStatus());
		model.addAttribute("modifyactivity", fAct.getActivity());
//		model.addAttribute("competences", fAct.getCompetences());
//		CompetenceStatus cs = new CompetenceStatus();
//		model.addAttribute("addcompetencestatus", cs);
		
		
		
	
		return "activity/modifyChoose";
//		return "/course/"+id_course+"/activity/"+id_academic+"/modifyChoose";
	}
//	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/modify.htm", method = RequestMethod.GET)
//	protected String formModifyActivitiess(
//			@PathVariable("academicId") Long id_academic,
//			@PathVariable("courseId") Long id_course, @PathVariable("activityId") long id_activity,
//			Model model) throws ServletException {
//
//		
//		AcademicTerm at = serviceAcademicTerm.getAcademicTerm(id_academic);
//		model.addAttribute("idCourse",id_course);
//		List<Competence> competences = serviceCompetence.getCompetencesForDegree(at.getDegree().getId());
//		
//		
//		model.addAttribute("competences", competences);
//		CompetenceStatus cs = new CompetenceStatus();
//		model.addAttribute("addcompetencestatus", cs);
//		
//		
//		
//	
//		return "activity/modifyChoose";
////		return "/course/"+id_course+"/activity/"+id_academic+"/modifyChoose";
//	}
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/modify.htm", params="button1", method = RequestMethod.POST)
	public String formModifySystem(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") Long id_activity,
			@ModelAttribute("addcompetencestatus") @Valid CompetenceStatus competenceStatus,BindingResult result,
			Model model)

	{

		
		if (!result.hasErrors()) {
			Activity a = serviceActivity.getActivity(id_activity);
			a.getCompetenceStatus().add(competenceStatus);
			//modify.setCompetenceStatus(col);
			boolean success = serviceActivity.modifyActivity(a);
			if (success){
				
				return "redirect:/academicTerm/"+id_academicTerm +"/course/"+ id_course+ "/activity/" +id_activity+"/modify.htm";
			}
		}
		return "redirect:/error.htm";
	}

	@RequestMapping(value = "/course/{idCourse}/activity/{activityId}/addCompetenceStatus.htm")
	protected String formModifyActivitiesCompetenceStatus(@PathVariable("idCourse") Long id_course, 
			@PathVariable("activityId") long id,
			@ModelAttribute("addcompetencestatus") @Valid CompetenceStatus competencestatus, BindingResult result,
			Model model) throws ServletException {
	
		Activity p = serviceActivity.getActivity(id);
		if (!result.hasErrors()) 
			p.getCompetenceStatus().add(competencestatus);
		
		//model.addAttribute("addcompetencestatus",new CompetenceStatus());


		return "/course/"+id_course+"/activity/"+id+"/modifyChoose";
	}
	

	/**
	 * Method for delete an activities
	 */
	
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteActivity(
			@PathVariable("academicId") Long id_AcademicTerm,
			@PathVariable("courseId") Long id_course,@PathVariable("activityId") Long id_activity)
			throws ServletException {

		if (serviceActivity.deleteActivity(id_activity)) {
			return "redirect:/academicTerm/"+ id_AcademicTerm + "/course/"+id_course+".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Method for delete an competence status of activities
	 */
	
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/competenceStatus/{compStatusId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteCompetenceStatusActivity(
			@PathVariable("academicId") Long id_AcademicTerm,
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") long id_Activity,@PathVariable("compStatusId") Long id_competenceStatus)
			throws ServletException {

		
		if (serviceActivity.deleteCompetenceActivity(id_competenceStatus, id_Activity)) {
			return "redirect:/academicTerm/"+  id_AcademicTerm+ "/course/"+id_course+"/activity/"+ id_Activity+".htm";
		} else
			return "redirect:/error.htm";
	}



	/**
	 * Methods for view subjects
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewActivity(
			@PathVariable("academicId")Long id_academic,
			@PathVariable("courseId") Long id_course,@PathVariable("activityId") long id_activity)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();


		Activity a = serviceActivity.getActivity(id_academic);

		model.put("activity", a);
		model.put("activityId", id_activity);
	
		model.put("competenceStatus", a.getCompetenceStatus());

		return new ModelAndView("activity/view", "model", model);
	}
	
/*
	public Collection<CompetenceStatus> getCol() {
		return col;
	}

	public void setCol(List<CompetenceStatus> col) {
		this.col = col;
	}*/
	
	/**
	 * For binding the courses of the activity
	 */
/*	@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "courses",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						if (element instanceof Course) {
							logger.info("Converting...{}", element);
							return element;
						}
						if (element instanceof String) {
							Course course = serviceCourse.getCourse(element.toString());
								
							logger.info("Loking up {} to {}", element,course);

						
							return course;
						}
						System.out.println("Don't know what to do with: "
								+ element);
						return null;
					}
				});
	}*/
}