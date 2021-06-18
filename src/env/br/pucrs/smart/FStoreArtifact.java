// CArtAgO artifact code for project smart

package br.pucrs.smart;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.pucrs.smart.models.firestore.Allocation;
import br.pucrs.smart.models.firestore.LaudosInternacao;
import br.pucrs.smart.models.firestore.Leito;
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
		try {
			aloc = new FStore();
		} catch (Exception e) {
		}
	}

	@OPERATION
	// aloca todos os leitos
	void alocLeitos(OpFeedbackParam<String> response) {
		if (aloc == null)
			init();
		System.out.println("operation called");
		try {
			aloc.init();
			aloc.quartoOut(0);
			aloc.pacienteOut();
			aloc.runAloc(10); // 10 segundos max
			aloc.procAloc();
			OptimiserResult result = aloc.optInit();
			System.out.println(result.toString());
			OptimiserResult optimiserResult = getData(result);
			System.out.println(optimiserResult.toString());
//			String r = FirebaseDb.addOptimiserResult(optimiserResult);
//			System.out.println(r);
			response.set(
					"Ok, gerei uma alocação otimizada mantendo o maior número possível de quartos livres e deixando os pacientes mais graves próximos da sala de enfermagem. Você pode vê-la clicando aqui: https://explainable-agent.firebaseapp.com/optimized");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	OptimiserResult getData(OptimiserResult op) throws InterruptedException, ExecutionException {
		OptimiserResult optimiserResult = new OptimiserResult();
		optimiserResult.setAllAllocated(op.isAllAllocated());
		optimiserResult.setAlreadySuggested(false);
		optimiserResult.setSugestedAllocation(op.getSugestedAllocation());
		optimiserResult.setNotAllocated(op.getNotAllocated());
		if (op.getSugestedAllocation() != null) {
			for (Allocation aloc : op.getSugestedAllocation()) {
				LaudosInternacao laudo = FirebaseDb.getLaudosInternacaoByIdPaciente(aloc.getIdPaciente());
				if (laudo != null ) {
				System.out.println(laudo.toString());
				
					String num = aloc.getLeito();
						System.out.println("Leito: " + num);
						Leito le = FirebaseDb.getLeitoByNum(num);
						if (le != null) {
						System.out.println(le.toString());
						laudo.setLeito(le);
						}

						optimiserResult.addLaudosData(laudo);
				}
			}
		}
		if (op.getNotAllocated() != null && !op.getNotAllocated().isEmpty()) {
			System.out.println("entreou no segundo for");
			for (String id : op.getNotAllocated()) {
				LaudosInternacao laudo = FirebaseDb.getLaudosInternacaoByIdPaciente(id);
				optimiserResult.addLaudosData(laudo);
			}
		}
		return optimiserResult;
	}

	// return a list of notAloc([PacienteName])
	Literal createParamBelief(List<String> idsPaciente) throws InterruptedException, ExecutionException {

		Literal l = ASSyntax.createLiteral("notAloc");
		for (String id : idsPaciente) {
			LaudosInternacao p = FirebaseDb.getLaudosInternacaoByIdPaciente(id);

			l.addTerm(ASSyntax.createString(p.getNomePaciente()));
		}
		return l;
	}

	@OPERATION
	// aloca todos os leitos
	void pddl() {
		try {
			aloc.pddl();
//			aloc.tests();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}