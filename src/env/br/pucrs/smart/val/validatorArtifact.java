// CArtAgO artifact code for project validatorArtifact

package br.pucrs.smart.val;

import cartago.*;
import java.util.List;

public class validatorArtifact extends Artifact {
	private PDDL pddl = null;
	boolean domain = false;
	boolean problem = false;
	boolean plan = false;
	
	void init() {}

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
	void readProblem(String filename){
		if(domain){
			Parser.parseProblem(pddl, filename);
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
	void readPlan(String filename){
		if(problem){
			Parser.parsePlan(pddl, filename);
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

