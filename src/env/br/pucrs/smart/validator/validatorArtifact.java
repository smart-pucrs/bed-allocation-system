// CArtAgO artifact code for bed allocation validator

package br.pucrs.smart.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;

import br.pucrs.smart.firestore.FirebaseFirestoreReactive;
import br.pucrs.smart.firestore.FirebaseDb;
import br.pucrs.smart.firestore.models.ErrorVal;
import br.pucrs.smart.firestore.models.LaudosInternacao;
import br.pucrs.smart.firestore.models.PddlStrings;
import br.pucrs.smart.firestore.models.ResultVal;
import br.pucrs.smart.firestore.models.TempAloc;
import br.pucrs.smart.firestore.models.Validacao;
import cartago.Artifact;
import cartago.INTERNAL_OPERATION;
import cartago.OPERATION;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class validatorArtifact extends Artifact implements IValidator{
	private static PDDL pddl = null;
	boolean domain = false;
	boolean problem = false;
	boolean plan = false;
	private List<LaudosInternacao> laudos;

	private Gson gson = new Gson();
	
	void init() {
		FirebaseFirestoreReactive.setListener(this);
	}

	@OPERATION
	void readDomain(String filename){
		pddl = Parser.parseDomain(filename);
		if(pddl.domain != null){
			domain = true;
			defineObsProperty("domain", pddl.domain);
		}else{
			failed("Invalid domain file");
		}
	}

	@OPERATION
	void readProblem(String filename, String problemString){
		if(domain){
			Parser.parseProblem(pddl, filename, problemString);
			if(pddl.problem != null){
				problem = true;
				defineObsProperty("problem", pddl.problem);
			}else{
				failed("Invalid problem file");
			}
		}else{
			failed("No domain");
		}
	}

	@OPERATION
	void readPlan(String filename, String planString){
		if(problem){
			Parser.parsePlan(pddl, filename, planString);
			if(pddl.hasPlan()){
				plan = true;
				defineObsProperty("plan", filename);
			}else{
				failed("Invalid plan file");	
			}
		}else{
			failed("No problem");
		}
	}

	@OPERATION
	void writeReport(String filename){
		if(plan){
			pddl.valOut(filename);
		}else{
			failed("No plan");
		}
	}

	//add to belief base 
	@INTERNAL_OPERATION
	void createBelief(String str) {
		defineObsProperty(str);
	}
	
	//add to belief base 
	@INTERNAL_OPERATION
	void createBelief(String str, String str2) {
		defineObsProperty(str, str2);
	}
		
	//add to belief base result(WasInformed, IsValid, Errors) Errors = [err(Nome, Leito, [mot(Type, Predicate, PredType)])];  
	@INTERNAL_OPERATION
	void createResultBelief(boolean isValid, UUID guid, List<ResultVal> finalResult) {

//		System.out.println("### ---------------------------------- finalResult");
//		for (ResultVal r : finalResult) {
			
//			System.out.println(r.toString());
//		}
		defineObsProperty("result", guid.toString(), false, isValid, createMotiveBelief(finalResult)); // result(Id, WasInformed, IsValid, Errors)
	}
	
	ListTerm createMotiveBelief(List<ResultVal> finalResult) {
		Collection<Term> terms = new LinkedList<Term>();
		for (ResultVal r : finalResult) {
			Literal l = ASSyntax.createLiteral("err", ASSyntax.createString(r.getNomePaciente()));
			l.addTerm(ASSyntax.createString(r.getNumeroLeito()));
			if (r.getErrors() != null) {
				ListTerm errorsList = createErrorBelief(r.getErrors());
				l.addTerm(errorsList);
			}
			terms.add(l);
		}
		
		return ASSyntax.createList(terms);			
	}
	
	ListTerm createErrorBelief(List<ErrorVal> errorsVal) {
		Collection<Term> terms = new LinkedList<Term>();
		for (ErrorVal e : errorsVal) {
			Literal l = ASSyntax.createLiteral("mot", ASSyntax.createString(e.getType()));
			l.addTerm(ASSyntax.createString(e.getPredicado()));
			l.addTerm(ASSyntax.createString(e.getPredType()));
			terms.add(l);
		}
		
		return ASSyntax.createList(terms);			
	}
	
	String getPacienteName(String id) {
		for (LaudosInternacao l : laudos) {
			if (l.getIdPaciente().equals(id)) return l.getNomePaciente();
		}
		return null;
	}
	
	String getLeitoNumber(String id) {
		for (LaudosInternacao l : laudos) {
			if (l.getLeito().getId().equals(id)) return l.getLeito().getNumero();
		}
		return null;
	}

	
	String changePredType(String str) {
		switch (str) {
	    case "bedstay":
	      return "Tipo de Estadia ";
	    case "bedroomtype":
	      return "Tipo ";
	    case "bedorigin":
	      return "Tipo de Encaminhamento ";
	    case "bedgender":
	      return "Gênero ";
	    case "bedage":
	      return "Idade ";
	    case "bedbirthtype":
	      return "Tipo de nascimento ";
	    case "bedcare":
	      return "Cuidados ";
	    case "bedspecialty":
	      return "Especialidade ";
	    case "bedisolation":
	      return "Isolamento ";
	    case "minimos":
	      return "Mínimos";
	    case "intensivos":
	      return "Intensivos";
	    case "semiintensivos":
	      return "Semi-Intensivos";
	    case "geral":
	      return "Geral";
	    case "cardiologia":
	      return "Cardiologia";
	    case "cirurgiabariatrica":
	      return "Cirurgia Bariatrica";
	    case "cirurgiacardiaca":
	      return "Cirurgia Cardíaca";
	    case "uclunidadedecuidadosespeciais":
	      return "UCL";
	    case "cirurgiadigestiva":
	      return "Cirurgia Digestiva";
	    case "cirurgiavascular":
	      return "Cirurgia Vascular";
	    case "endovascular":
	      return "Endovascular";
	    case "gastro":
	      return "Gastro";
	    case "ginecologia":
	      return "Ginecologia";
	    case "infecto":
	      return "Infecto";
	    case "medicinainterna":
	      return "Medicina Interna";
	    case "neurologia":
	      return "Neurologia";
	    case "obstetricia":
	      return "Obstetrícia";
	    case "oncologia":
	      return "Oncologia";
	    case "pneumo":
	      return "Pneumo";
	    case "psiquiatria":
	      return "Psiquiatria";
	    case "uti":
	      return "UTI";
	    case "aborto":
	      return "Aborto";
	    case "nascimento":
	      return "Nascimento";
	    case "crianca":
	      return "Infantil";
	    case "adulto":
	      return "Adulto";
	    case "adolescente":
	      return "Adolescente";
	    case "masculino":
	      return "Masculino";
	    case "feminino":
	      return "Feminino";
	    case "eletivo":
	      return "Eletivo";
	    case "agudo":
	      return "Agudo";
	    case "clinico":
	      return "Clínico";
	    case "cirurgico":
	      return "Cirúrgico";
	    case "longapermanencia":
	      return "Longa Permanência";
	    case "girorapido":
	      return "Giro Rápido";
	    default:
	      return " ";
	  }
		
	}
	
	
	@Override
	public void receiveValidation(Validacao val, TempAloc tempAloc) {
		this.laudos = val.getPacientes();
		
		/*********************************************
		 * Transforming database data into pddl files
		 * *******************************************/

		PddlBuilder a = new PddlBuilder(val.getPacientes());
		PddlStrings pddlStrings = a.buildPddl();
		val.setProblema(pddlStrings.getProblem());
		val.setPlano(pddlStrings.getPlan());

		/********************************************/
		
		/***************************************
		 * Calling PDDL validator
		 * *************************************/

		pddl = Parser.parseDomain("src/resources/domain.pddl"); 
		Parser.parseProblem(pddl, "problem", pddlStrings.getProblem());
		Parser.parsePlan(pddl, "plan", pddlStrings.getPlan());		
		List<Object[]> out = pddl.tryPlanForce(false);
		
		/***************************************
		 * Building result
		 * *************************************/

		List<ResultVal> finalResult = new ArrayList<>();
		for(Object[] o : out){
			ResultVal resultVal = new ResultVal();
//			System.out.print("Error in action \"( " );
			resultVal.setValid(false);
			int i = 0;
			for(String s : (String[])o[0]) {				
//				System.out.print(s + " ");
				if (i==1) resultVal.setIdPaciente(takeOutA(s));
				if (i==2) resultVal.setIdLeito(takeOutA(s));
				i++;
			}
//			System.out.println(")\"");
			if(((Object[])(o[1])).length == 0){
				ErrorVal errorVal = new ErrorVal();
//				System.out.println("Invalid parameters");
				errorVal.setType("invalidParameters");
				resultVal.addErrors(errorVal);
			}else{				
				List<String[]> l = (List<String[]>)((Object[])(o[1]))[0];
				if(l.size() > 0){
//					System.out.println("Missing positive predicates");
					for(String[] str : l){
						ErrorVal errorVal = new ErrorVal();
						errorVal.setType("missingPositive");
//						System.out.println(" ");
						int j = 0;
						for(String s : str) {
//							System.out.println("s["+j+"]: "+s);
							if (j==0) errorVal.setPredicado(changePredType(s));
							if (j==1) errorVal.setId(takeOutA(s));
							if (j==2) errorVal.setPredType(changePredType(s));
							j++;
						}
						resultVal.addErrors(errorVal);
//						System.out.println("");
					}
				}
				l = (List<String[]>)((Object[])(o[1]))[1];
				if(l.size() > 0){
//					System.out.println("Present negative predicates");
					for(String[] str : l){
						ErrorVal errorVal = new ErrorVal();
						errorVal.setType("presentNegative");
//						System.out.println(" ");
						int j = 0;
						for(String s : str) {
//							System.out.println("s["+j+"]: "+s);
							if (j==0) errorVal.setPredicado(changePredType(s));
							if (j==1) errorVal.setId(takeOutA(s));
							if (j==2) errorVal.setPredType(changePredType(s));
							j++;
						}
						resultVal.addErrors(errorVal);
//						System.out.println("");
					}
				}
				
			}
			resultVal.setNomePaciente(getPacienteName(resultVal.getIdPaciente()));
			resultVal.setNumeroLeito(getLeitoNumber(resultVal.getIdLeito()));
			System.out.println("[validatorArtifact] ResultVal");
			System.out.println("[validatorArtifact] "+ resultVal.toString());
			finalResult.add(resultVal);
		}
		UUID guid = java.util.UUID.randomUUID();
		execInternalOp("createResultBelief", pddl.goalAchieved(), guid, finalResult);
		try {
			FirebaseDb.addValidationResult(this.laudos, pddl.goalAchieved(), guid.toString(), pddlStrings.getProblem(), pddlStrings.getPlan());
			FirebaseDb.setTempAlocValidated(tempAloc.getId());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pddl.valOut("src/resources/output.tex");
	}
	
	String takeOutA(String str) {
		StringBuilder newStr = new StringBuilder();
		newStr.append(str);
		return (newStr.deleteCharAt(0)).toString();
	}
}