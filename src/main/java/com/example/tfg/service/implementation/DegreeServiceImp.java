package com.example.tfg.service.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.classes.ResultClass;
import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;
import com.example.tfg.repository.DegreeDao;
import com.example.tfg.service.AcademicTermService;
import com.example.tfg.service.CompetenceService;
import com.example.tfg.service.DegreeService;
import com.example.tfg.service.ModuleService;

@Service
public class DegreeServiceImp implements DegreeService {

	@Autowired
	private DegreeDao daoDegree;

	//	@Autowired
	//	private SubjectService serviceSubject;

	@Autowired
	private ModuleService serviceModule;

	@Autowired
	private CompetenceService serviceCompetence;

	@Autowired
	private AcademicTermService serviceAcademicTerm;

	@Transactional(readOnly=false)
	public ResultClass<Boolean> addDegree(Degree degree) {

		Degree degreeExists = daoDegree.existByCode(degree.getInfo().getCode());
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if( degreeExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");
			if (degreeExists.isDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");
				
			}
			result.setErrorsList(errors);
		}
		else{
			boolean r = daoDegree.addDegree(degree);
			if (r) 
				result.setE(true);
		}
		return result;

		

	
	}

	@Transactional(readOnly = true)
	public List<Degree> getAll() {
		return daoDegree.getAll();
	}

	//	public boolean modifyDegree(Degree degree) {
	//
	//		return daoDegree.saveDegree(degree);
	//	}

	@Transactional(readOnly = false)
	public boolean modifyDegree(Degree degree, Long id_degree) {

		Degree modifydegree = daoDegree.getDegree(id_degree);
		modifydegree.setInfo(degree.getInfo());
		//		if (degree.getCode() != null)
		//			Modifydegree.setCode(degree.getCode());
		//		if (degree.getName() != null)
		//			Modifydegree.setName(degree.getName());
		//		if (degree.getDescription() != null)
		//			Modifydegree.setDescription(degree.getDescription());
		return daoDegree.saveDegree(modifydegree);
	}

	@Transactional(readOnly = false)
	public Degree getDegree(Long id) {
		return daoDegree.getDegree(id);
	}

	@Transactional(readOnly = false)
	public boolean deleteDegree(Long id) {
		Degree d = daoDegree.getDegree(id);
		boolean deleteModules = serviceModule.deleteModulesForDegree(d);
		boolean deleteCompetences = serviceCompetence
				.deleteCompetencesForDegree(d);
		Collection<AcademicTerm> academicList = serviceAcademicTerm.getAcademicTermsByDegree(id);

		boolean deleteAcademic = serviceAcademicTerm.deleteAcademicTerm(academicList);
		if (deleteModules && deleteCompetences && deleteAcademic) {


			//			for (AcademicTerm a : serviceAcademicTerm
			//					.getAcademicTermsByDegree(id)) {
			//				serviceAcademicTerm.deleteAcademicTerm(a.getId());
			//			}
			return daoDegree.deleteDegree(d);
		} else
			return false;
	}

	@Transactional(readOnly = true)
	public Degree getDegreeSubject(Subject p) {

		return daoDegree.getDegreeSubject(p);
	}

	@Transactional(readOnly = true)
	public String getNextCode() {
		return daoDegree.getNextCode();

	}

	@Transactional(readOnly = true)
	public Degree getDegreeAll(Long id) {


		Degree d = daoDegree.getDegree(id);
		d.setModules(serviceModule.getModulesForDegree(id));
		d.setCompetences(serviceCompetence.getCompetencesForDegree(id));
		return d;
	}

	@Override
	public void unDeleteDegree(Degree degree) {
		// TODO Auto-generated method stub
		
	}

}
