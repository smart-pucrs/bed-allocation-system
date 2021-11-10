// CArtAgO artifact code for project smart

package br.pucrs.smart.optimiser;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import br.pucrs.smart.firestore.FirebaseDb;
import br.pucrs.smart.firestore.models.Allocation;
import br.pucrs.smart.firestore.models.LaudosInternacao;
import br.pucrs.smart.firestore.models.Leito;
import br.pucrs.smart.firestore.models.OptimiserResult;
import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class OptimiserArtifact extends Artifact {
	private Optimiser optimiser;
	private OptimiserResult optimiserResult;

	void init() {
		try {
			optimiser = new Optimiser();
		} catch (Exception e) {
		}
	}

	@OPERATION
	void getOptimiserResult(OpFeedbackParam<OptimiserResult> response) {
		response.set(this.optimiserResult);
	}

	@OPERATION
	// suggests allocation for all beds
	void suggestOptimisedAllocation(OpFeedbackParam<Literal> response) {
		if (optimiser == null)
			init();
		try {
			this.optimiserResult = optimiser.getOptimisationResult();
//			System.out.println("this.optimiserResult");
//			System.out.println(this.optimiserResult);
			Literal optimiserBelief = createOptimiserBelief(this.optimiserResult);
			System.out.println(optimiserBelief);
			response.set(optimiserBelief);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// return a Literal like
	// optimiserResult(IsAllAllocated,notAlloc([PacienteName]), sugestedAllocation([alloc(PacienteName, NumLeito)])) 
	// where IsAllAllocated is boolean
	Literal createOptimiserBelief(OptimiserResult op) {

		Literal l = ASSyntax.createLiteral("optimiserResult");
		l.addTerm(ASSyntax.createString(op.isAllAllocated()));
		l.addTerm(createNotAllocBelief(op));
		l.addTerm(createSugestedAllocationBelief(op));
		return l;
	}

	// return a Literal like notAlloc([PacienteName])
	Literal createNotAllocBelief(OptimiserResult op) {

		Literal l = ASSyntax.createLiteral("notAlloc");
		Collection<Term> terms = new LinkedList<Term>();
		for (String id : op.getNotAllocated()) {
			for (LaudosInternacao p : op.getLaudosData()) {
				if (p.getIdPaciente().equals(id)) {
					terms.add(ASSyntax.createString(p.getNomePaciente()));
				}
			}
		}
		l.addTerm(ASSyntax.createList(terms));
		return l;
	}

	// return a Literal like sugestedAllocation([alloc(PacienteName, NumLeito)])
	Literal createSugestedAllocationBelief(OptimiserResult op) {

		Literal l = ASSyntax.createLiteral("sugestedAllocation");
		Collection<Term> terms = new LinkedList<Term>();
		for (Allocation a : op.getSugestedAllocation()) {
			for (LaudosInternacao p : op.getLaudosData()) {
				if (p.getIdPaciente().equals(a.getIdPaciente())) {
					Literal allocLiteral = ASSyntax.createLiteral("alloc");
					allocLiteral.addTerm(ASSyntax.createString(p.getNomePaciente()));
					allocLiteral.addTerm(ASSyntax.createString(p.getLeito().getNumero()));
					terms.add(allocLiteral);
				}
			}
		}
		l.addTerm(ASSyntax.createList(terms));
		return l;
	}

//	@OPERATION
//	// allocates all beds
//	void pddl() {
//		try {
//			optimiser.pddl();
////			optimiser.tests();
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}

	OptimiserResult getData(OptimiserResult op) throws InterruptedException, ExecutionException {
		OptimiserResult optimiserResult = new OptimiserResult();
		optimiserResult.setAllAllocated(op.isAllAllocated());
		optimiserResult.setAlreadySuggested(false);
		optimiserResult.setSugestedAllocation(op.getSugestedAllocation());
		optimiserResult.setNotAllocated(op.getNotAllocated());
		if (op.getSugestedAllocation() != null) {
			for (Allocation optimiser : op.getSugestedAllocation()) {
				LaudosInternacao laudo = FirebaseDb.getLaudosInternacaoByIdPaciente(optimiser.getIdPaciente());
				if (laudo != null) {
//				System.out.println(laudo.toString());

					String bedNumber = optimiser.getLeito();
//						System.out.println("Leito: " + bedNumber);
					Leito bed = FirebaseDb.getLeitoByNum(bedNumber);
					if (bed != null) {
//						System.out.println(bed.toString());
						laudo.setLeito(bed);
					}

					optimiserResult.addLaudosData(laudo);
				}
			}
		}
		if (op.getNotAllocated() != null && !op.getNotAllocated().isEmpty()) {
//			System.out.println("entreou no segundo for");
			for (String id : op.getNotAllocated()) {
				LaudosInternacao laudo = FirebaseDb.getLaudosInternacaoByIdPaciente(id);
				optimiserResult.addLaudosData(laudo);
			}
		}
		return optimiserResult;
	}
}