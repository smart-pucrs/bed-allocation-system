package br.pucrs.smart.validator;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import br.pucrs.smart.firestore.models.LaudosInternacao;
import br.pucrs.smart.firestore.models.PddlStrings;


public class PddlBuilder {
	
	private ArrayList<LaudosInternacao> pacientes;
	private Gson gson = new Gson();
	
	public PddlBuilder(List<LaudosInternacao> list) {
		this.pacientes = (ArrayList<LaudosInternacao>) list;
//		System.out.println("## PddlBuilder created ##");		
	}
	
	public PddlStrings buildPddl() {
		PddlStrings builtPddl = new PddlStrings();
//		int countPlano = 0;
		StringBuilder problem = new StringBuilder();
		StringBuilder objects = new StringBuilder();
		StringBuilder initPatient = new StringBuilder();
		StringBuilder initLeito = new StringBuilder();
		StringBuilder goal = new StringBuilder();
		StringBuilder plan = new StringBuilder();
				
		for (LaudosInternacao paciente : pacientes) {
			// #### Problem
			objects.append('\n').append("  " + concatA(paciente.getIdPaciente()) + " - patient ");
			
			initPatient.append("  (patient" + myTrim(paciente.getEspecialidade()) + " " + concatA(paciente.getIdPaciente())+ ")").append("\n");
			initPatient.append("  (patientspecialty " + concatA(paciente.getIdPaciente()) + " " + myTrim(paciente.getEspecialidade()) + ")").append("\n");
			initPatient.append("  (patientage " + concatA(paciente.getIdPaciente()) + " " + myTrim(paciente.getAge()) + ")").append("\n");
			initPatient.append("  (patientgender " + concatA(paciente.getIdPaciente()) + " " + myTrim(paciente.getGenero()) + ")").append("\n");
			initPatient.append("  (patientcare " + concatA(paciente.getIdPaciente()) + " " + myTrim(paciente.getTipoDeCuidado()) + ")").append("\n");
			initPatient.append("  (patientorigin " + concatA(paciente.getIdPaciente()) + " " + myTrim(paciente.getTipoDeEncaminhamento()) + ")").append("\n");
			initPatient.append("  (patientroomtype " + concatA(paciente.getIdPaciente()) + " " + myTrim(paciente.getTipoDeLeito()) + ")").append("\n");
			initPatient.append("  (patientstay " + concatA(paciente.getIdPaciente()) + " " + myTrim(paciente.getTipoDeEstadia()) + ")").append("\n");
			if (paciente.getEspecialidade().equals("Isolamento")) {
			    initPatient.append("\n").append("  (isolation " + concatA(paciente.getIdPaciente()) + ")");
			}
			if (paciente.getEspecialidade().equals("Obstetricia")) {
			    initPatient.append("\n").append("  (patientbirthtype " + concatA(paciente.getIdPaciente()) + " " + myTrim(paciente.getBirthtype()) + ")");
			}
			
			goal.append(" (allocated " + concatA(paciente.getIdPaciente()) + ")");
			
			// ### bed
			if (paciente.getLeito() != null) {
			    objects.append("\n").append("  " + concatA(paciente.getLeito().getId()) + " - bed ");

			    initLeito.append("\n").append("  (bedfree " + concatA(paciente.getLeito().getId()) + ")").append("\n");
			    initLeito.append("  (bed" + myTrim(paciente.getLeito().getEspecialidade()) + " " + concatA(paciente.getLeito().getId()) + ")").append("\n");
			    initLeito.append("  (bedspecialty " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getLeito().getEspecialidade()) + ")").append("\n");
			    initLeito.append("  (bedage " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getLeito().getAge()) + ")").append("\n");
			    initLeito.append("  (bedgender " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getLeito().getGenero()) + ")").append("\n");
			    initLeito.append("  (bedcare " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getLeito().getTipoDeCuidado()) + ")").append("\n");
			    initLeito.append("  (bedorigin " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getLeito().getTipoDeEncaminhamento()) + ")").append("\n");
			    initLeito.append("  (bedroomtype " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getLeito().getTipoDeLeito()) + ")").append("\n");
			    initLeito.append("  (bedstay " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getTipoDeEstadia()) + ")").append("\n");
			    

			    if (paciente.getLeito().getEspecialidade().equals("Isolamento")) {
			        initLeito.append("  (bedisolation " + concatA(paciente.getLeito().getId()) + ")");
			    }
			    if (paciente.getLeito().getEspecialidade().equals("Obstetricia")) {
			        initLeito.append("  (bedbirthtype " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getLeito().getBirthtype()) + ")");
			    }
			    
			}
			

			// #### Plan
			switch (paciente.getEspecialidade()) {
			case "UTI":
			    plan.append("( alocateuti " + concatA(paciente.getIdPaciente()) + ")").append("\n");
			    break;

			case "Isolamento":
			    plan.append("( allocateisolation " + concatA(paciente.getIdPaciente()) + " " + concatA(paciente.getLeito().getId()) + ")").append("\n");
			    break;

			case "Obstetrícia":
			    plan.append("( allocateobstetricia " + concatA(paciente.getIdPaciente()) + " " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getBirthtype()) + ")").append("\n");
			    break;

			case "UCL – Unidade de Cuidados Especiais":
			    plan.append("( allocateucl " + concatA(paciente.getIdPaciente()) + " " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getAge()) + ")").append("\n");
			    break;

			case "AVC":
			    plan.append("( allocateavc " + concatA(paciente.getIdPaciente()) + " " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getGenero()) + ")").append("\n");
			    break;

			case "Psiquiatria":
			    plan.append("( allocatepsiquiatria " + concatA(paciente.getIdPaciente()) + " " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getGenero()) + ")").append("\n");
			    break;

			case "Cirurgia bariátrica":
			    plan.append("( allocatecirurgiabariatrica " + concatA(paciente.getIdPaciente()) + " " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getGenero()) + ")").append("\n");
			    break;

			case "Ginecologia":
			    plan.append("( allocateginecologia " + concatA(paciente.getIdPaciente()) + " " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getTipoDeLeito()) + ")").append("\n");
			    break;

			default:
			    plan.append("( allocate " + concatA(paciente.getIdPaciente()) + " " + concatA(paciente.getLeito().getId()) + " " + myTrim(paciente.getEspecialidade()) + " " + myTrim(paciente.getTipoDeEstadia()) + " " + myTrim(paciente.getTipoDeLeito()) + " " + myTrim(paciente.getTipoDeEncaminhamento()) + " " + myTrim(paciente.getGenero()) + " " + myTrim(paciente.getAge()) + " " + myTrim(paciente.getTipoDeCuidado()) + ")").append("\n");
			    break;
			}

//	        countPlano = countPlano + 1;
		}
		
		problem.append("(define (problem hospital-problem)").append("\n");
		problem.append("(:domain hospitaldomain)").append("\n");
		problem.append("(:objects").append("\n");
		problem.append("\n");
		problem.append("  ;variacoes possiveis").append("\n");
		problem.append("  minimos - care").append("\n");
		problem.append("  intensivos - care").append("\n");
		problem.append("  semiintensivos - care").append("\n");
		problem.append("  geral - specialty").append("\n");
		problem.append("  cardiologia - specialty").append("\n");
		problem.append("  cirurgiabariatrica - specialty").append("\n");
		problem.append("  cirurgiacardiaca - specialty").append("\n");
		problem.append("  uclunidadedecuidadosespeciais - specialty").append("\n");
		problem.append("  cirurgiadigestiva - specialty").append("\n");
		problem.append("  cirurgiavascular - specialty").append("\n");
		problem.append("  endovascular - specialty").append("\n");
		problem.append("  gastro - specialty").append("\n");
		problem.append("  ginecologia - specialty").append("\n");
		problem.append("  infecto - specialty").append("\n");
		problem.append("  medicinainterna - specialty").append("\n");
		problem.append("  neurologia - specialty").append("\n");
		problem.append("  obstetricia - specialty").append("\n");
		problem.append("  oncologia - specialty").append("\n");
		problem.append("  pneumo - specialty").append("\n");
		problem.append("  psiquiatria - specialty").append("\n");
		problem.append("  uti - specialty").append("\n");
		problem.append("  aborto - birthtype").append("\n");
		problem.append("  nascimento - birthtype").append("\n");
		problem.append("  crianca - age").append("\n");
		problem.append("  adulto - age").append("\n");
		problem.append("  adolescente - age").append("\n");
		problem.append("  indefinido - age").append("\n");
		problem.append("  masculino - gender").append("\n");
		problem.append("  feminino - gender").append("\n");
		problem.append("  eletivo - origin").append("\n");
		problem.append("  agudo - origin").append("\n");
		problem.append("  clinico - roomtype").append("\n");
		problem.append("  cirurgico - roomtype").append("\n");
		problem.append("  longapermanencia - stay").append("\n");
		problem.append("  girorapido - stay").append("\n");
		problem.append("\n");
		problem.append("  ").append(objects).append("\n");
		problem.append("  )").append("\n");
		problem.append("  (:init ").append("\n");
		problem.append(" ").append(initPatient).append("\n");
		problem.append("  ").append(initLeito).append("\n");
		problem.append("  )").append("\n");
		problem.append("  (:goal (and ").append(goal).append("\n");
		problem.append("  )").append("\n");
		problem.append(" )").append("\n");
		problem.append(")");
		
		builtPddl.setPlan(plan.toString());
		
		builtPddl.setProblem(problem.toString());
		
		return builtPddl;
	}
	
	
	 String myTrim(String str) {
		 str=str.replaceAll("-", "");
		return Normalizer.normalize(StringUtils.deleteWhitespace(str.toLowerCase()), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	 String concatA(String uid) {
	    return 'a' + uid;
	  }

}