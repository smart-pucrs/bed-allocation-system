// CArtAgO artifact code for project validatorArtifact

package br.pucrs.smart.val;

import java.util.List;

import com.google.gson.Gson;

import br.pucrs.smart.FirebaseFirestoreReactive;
import br.pucrs.smart.PddlBuilder;
import br.pucrs.smart.interfaces.IValidator;
import br.pucrs.smart.models.firestore.PddlStrings;
import br.pucrs.smart.models.firestore.Validacao;
import cartago.Artifact;
import cartago.INTERNAL_OPERATION;
import cartago.OPERATION;
import jason.asSyntax.ASSyntax;

public class validatorArtifact extends Artifact implements IValidator{
	private static PDDL pddl = null;
	boolean domain = false;
	boolean problem = false;
	boolean plan = false;

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

	@OPERATION
	void tryPlan(){
		if(hasObsProperty("goalAchieved")) removeObsProperty("goalAchieved");
		if(hasObsProperty("failedAction")) removeObsProperty("failedAction");
		if(hasObsProperty("invalidParameters")) removeObsProperty("invalidParameters");
		while(hasObsProperty("missingPositive")) removeObsProperty("missingPositive");
		while(hasObsProperty("presentNegative")) removeObsProperty("presentNegative");
		if(plan){			
			Object[] out = pddl.tryPlan(false);
			if(out != null){
				if(hasObsProperty("planSucceeded")) removeObsProperty("planSucceeded");
				defineObsProperty("planFailed");
				//action
				String[] auxA = (String[])out[0];
				String aux = auxA[0];
				for(int i = 0; i < auxA.length; i++) aux += " " + auxA[i];
				defineObsProperty("failedAction", aux);
				
				if(((Object[])(out[1])).length == 0){
					defineObsProperty("invalidParameters");
				}else{
					List<String[]> l = (List<String[]>)((Object[])(out[1]))[0];
					if(l.size() > 0){
						for(String[] str : l){			
							aux = str[0];
							for(int i = 0; i < str.length; i++) aux += " " + str[i];
							defineObsProperty("missingPositive", aux);
						}
					}
					l = (List<String[]>)((Object[])(out[1]))[1];
					if(l.size() > 0){
						for(String[] str : l){		
							aux = str[0];
							for(int i = 0; i < str.length; i++) aux += " " + str[i];
							defineObsProperty("presentNegative", aux);
						}
					}
				}
			}
			if(hasObsProperty("planFailed")) removeObsProperty("planFailed");
			defineObsProperty("planSucceeded");
			if(pddl.goalAchieved()) defineObsProperty("goalAchieved");
		}else{
			if(hasObsProperty("planSucceeded")) removeObsProperty("planSucceeded");			
			if(hasObsProperty("planFailed")) removeObsProperty("planFailed");
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
		
	//add to belief base result(WasInformed, IsValid, Errors) Errors = [String];  
	@INTERNAL_OPERATION
	void createResultBelief(boolean isValid, String errors) {
		defineObsProperty("result", false, isValid, errors);
	}	

	@Override
	public void receiveValidation(Validacao val) {
		// TODO Auto-generated method stub
		System.out.println(val.toString());
		

		System.out.println("### chamando PddlBuilder");
		PddlBuilder a = new PddlBuilder(val.getPacientes());
		PddlStrings pddlStrings = a.buildPddl();
		val.setProblema(pddlStrings.getProblem());
		val.setPlano(pddlStrings.getPlan());
		System.out.println("### Strings formadas: ");
		System.out.println("** Problem **");
		System.out.println(pddlStrings.getProblem());
		System.out.println("** Plan **");
		System.out.println(pddlStrings.getPlan());
		
		System.out.println("#### Chamando validador -------------------------------- ###");
		pddl = Parser.parseDomain("src/resources/domain.pddl"); 
		Parser.parseProblem(pddl, "problem", pddlStrings.getProblem());
		Parser.parsePlan(pddl, "plan", pddlStrings.getPlan());
		
//		List<Object[]> out = pddl.tryPlanForce(false);
		
		
		if(hasObsProperty("goalAchieved")) removeObsProperty("goalAchieved");
		if(hasObsProperty("failedAction")) removeObsProperty("failedAction");
		if(hasObsProperty("invalidParameters")) removeObsProperty("invalidParameters");
		while(hasObsProperty("missingPositive")) removeObsProperty("missingPositive");
		while(hasObsProperty("presentNegative")) removeObsProperty("presentNegative");
//		if(plan){			
			Object[] out = pddl.tryPlan(false);
			String err = "";
			if(out != null){
				if(hasObsProperty("planSucceeded")) removeObsProperty("planSucceeded");
//				defineObsProperty("planFailed");
				execInternalOp("createBelief", "planFailed");
				//action
				String[] auxA = (String[])out[0];
				String aux = auxA[0];
				for(int i = 0; i < auxA.length; i++) aux += " " + auxA[i];
//				defineObsProperty("failedAction", aux);
				execInternalOp("createBelief", "failedAction", aux);
				if(((Object[])(out[1])).length == 0){
//					defineObsProperty("invalidParameters");
					execInternalOp("createBelief", "invalidParameters");
					err = "invalidParameters";
				}else{
					List<String[]> l = (List<String[]>)((Object[])(out[1]))[0];
					if(l.size() > 0){
						for(String[] str : l){			
							aux = str[0];
							for(int i = 0; i < str.length; i++) aux += " " + str[i];
//							defineObsProperty("missingPositive", aux);

							execInternalOp("createBelief", "missingPositive", aux);
							err = "missingPositive : " + aux;
						}
					}
					l = (List<String[]>)((Object[])(out[1]))[1];
					if(l.size() > 0){
						for(String[] str : l){		
							aux = str[0];
							for(int i = 0; i < str.length; i++) aux += " " + str[i];
//							defineObsProperty("presentNegative", aux);
							execInternalOp("createBelief", "presentNegative", aux);
							err = "presentNegative : " + aux;
						}
					}
				}
			}
			if(hasObsProperty("planFailed")) removeObsProperty("planFailed");
//			defineObsProperty("planSucceeded");
			execInternalOp("createBelief", "planSucceeded");
			if(pddl.goalAchieved()) execInternalOp("createBelief", "goalAchieved"); //defineObsProperty("goalAchieved");
			System.out.println("Goal Achieved: " + pddl.goalAchieved());
			execInternalOp("createResultBelief", pddl.goalAchieved(), err);
			pddl.valOut("src/resources/output.tex");

	}
		
//		System.out.println("#### out ###");
//		System.out.println(gson.toJson(out));
//		for(Object[] o : out){			
//			System.out.print("Error in action \"( " );
//			for(String s : (String[])o[0]) System.out.print(s + " ");
//			System.out.println(")\"");
//			if(((Object[])(o[1])).length == 0){
//				System.out.println("Invalid parameters");
//			}else{
//				List<String[]> l = (List<String[]>)((Object[])(o[1]))[0];
//				if(l.size() > 0){
//					System.out.println("Missing positive predicates");
//					for(String[] str : l){
//						System.out.print(" ");
//						for(String s : str) System.out.print(s + " ");
//						System.out.println("");
//					}
//				}
//				l = (List<String[]>)((Object[])(o[1]))[1];
//				if(l.size() > 0){
//					System.out.println("Present negative predicates");
//					for(String[] str : l){
//						System.out.print(" ");
//						for(String s : str) System.out.print(s + " ");
//						System.out.println("");
//					}
//				}
//			}
//		}
//		
//		System.out.println("valdone");
//		
		
//	}
	
	//%PLACEHOLDER%
	/*public void tests(){
		PDDL test = Parser.parseDomain("domain.pddl");
		//test.printDomain();
		Parser.parseProblem(test, "problem.pddl");
		Parser.parsePlan(test, "plan.pddl");
		List<Object[]> out = test.tryPlanForce(false);
		for(Object[] o : out){			
			System.out.print("Error in action \"( " );
			for(String s : (String[])o[0]) System.out.print(s + " ");
			System.out.println(")\"");
			if(((Object[])(o[1])).length == 0){
				System.out.println("Invalid parameters");
			}else{
				List<String[]> l = (List<String[]>)((Object[])(o[1]))[0];
				if(l.size() > 0){
					System.out.println("Missing positive predicates");
					for(String[] str : l){
						System.out.print(" ");
						for(String s : str) System.out.print(s + " ");
						System.out.println("");
					}
				}
				l = (List<String[]>)((Object[])(o[1]))[1];
				if(l.size() > 0){
					System.out.println("Present negative predicates");
					for(String[] str : l){
						System.out.print(" ");
						for(String s : str) System.out.print(s + " ");
						System.out.println("");
					}
				}
			}
		}
		System.out.println("valdone");
	}*/
}

