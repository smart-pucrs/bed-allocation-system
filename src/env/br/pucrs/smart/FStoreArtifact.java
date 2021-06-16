// CArtAgO artifact code for project smart

package br.pucrs.smart;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.pucrs.smart.models.firestore.OptimiserResult;
import br.pucrs.smart.models.firestore.Paciente;
import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;

public class FStoreArtifact extends Artifact {
	private FStore aloc;
	
	void init() {
		try{
			aloc = new FStore();
		}catch(Exception e){
		}
	}
	
	@OPERATION
	//aloca todos os leitos
	void alocLeitos(OpFeedbackParam<String> response) {
		if(aloc == null) init();
		System.out.println("operation called");
		try{
			aloc.init();
			aloc.runAloc(10); //10 segundos max
			aloc.procAloc();
			OptimiserResult result = aloc.optInit();
			System.out.println(result.toString());
			
			
			response.set("Ok, gerei uma alocação otimizada mantendo o maior número possível de quartos livres e deixando os pacientes mais graves próximos da sala de enfermagem. Você pode vê-la clicando aqui: https://explainable-agent.firebaseapp.com/optimized");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// return a list of notAloc([PacienteName])
	Literal createParamBelief(List<String> idsPaciente) throws InterruptedException, ExecutionException {
			
			Literal l = ASSyntax.createLiteral("notAloc");
			for (String id : idsPaciente) {
				Paciente p = FirebaseDb.getPacienteById(id);
				l.addTerm(ASSyntax.createString(p.getNome()));
			}
			return l;
		}
		

	@OPERATION
	//aloca todos os leitos
	void pddl() {
		try{
			aloc.pddl();
//			aloc.tests();
		}catch(Exception e){
			System.out.println(e);
		}
	}
}

;