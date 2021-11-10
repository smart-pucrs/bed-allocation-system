package br.pucrs.smart.optimiser;

//java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
//exceptions
import java.io.IOException;
import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.QueryDocumentSnapshot;

import br.pucrs.smart.firestore.FirebaseDb;
import br.pucrs.smart.firestore.models.LaudosInternacao;
import br.pucrs.smart.firestore.models.Leito;
import br.pucrs.smart.firestore.models.OptimiserResult;

public class Optimiser extends DataForTheOptimiser {

	// simbolo de caracteristica nao definida
	private final String NONE = "_NONE";

	// Quartos com excecoes
	Map<String, OpPaciente> leitoAlocEx; // leito -> paciente;
	Map<String, OpQuarto> quartosEx; // nome do quarto -> objeto do quarto(leitos e regras)
	List<String> keysRegras;

	// %PLACEHOLDER%
	public Optimiser() throws IOException {
		EQUALS = "%igual%";

		try {
			initRules();
			initCharacteristics();
		} catch (ExecutionException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error downloading rules and/or characteristics");
		}
	}

	public void initRules() throws ExecutionException, InterruptedException {
		// inicializa as regras
		regras = new HashMap<>();
		keysRegras = new ArrayList<String>();
		List<QueryDocumentSnapshot> regrasDocs = FirebaseDb.getRegras();
		for (QueryDocumentSnapshot regrasDoc : regrasDocs) {
			for (Map.Entry<String, Object> regra : regrasDoc.getData().entrySet()) {
				regras.put(regra.getKey(), (Map) regra.getValue());
			}
			keysRegras.add(regrasDoc.getId());
		}
	}

	public void initCharacteristics() throws InterruptedException, ExecutionException {
		// inicializa as caracteristicas possiveis
		caracts = new ArrayList<>();
		caractMap = new HashMap<>();
		Set<Entry<String, Object>> characteristicsFromDB = FirebaseDb.getCharacteristics();
		for (Map.Entry<String, Object> caract : characteristicsFromDB) {
			OpCaract c = new OpCaract(caract.getKey());
			c.opts = (List) caract.getValue();
			caracts.add(c);
			caractMap.put(caract.getKey(), c);
		}
		for (int i = 0; i < caracts.size(); i++) {
			caracts.get(i).id = i;
		}
	}

	/**************************************************
	 * Inicializa os valores dos quartos para o GLPSol
	 ***************************************************/
	@Override
	public void initQuartos() throws ExecutionException, InterruptedException {

		// inicializa os quartos e leitos
		leitos = new HashMap<>();
		quartos = new HashMap<>();
		List<Leito> dbLeitos = FirebaseDb.getLeitos();
		for (Leito leito : dbLeitos) {
			String quartoNum = leito.getQuarto();
			String leitoNum = leito.getNumero();
			leitos.put(leitoNum, leito);

			float valorCuidado = leito.getDist() != null ? 1000 - Float.parseFloat(String.valueOf(leito.getDist()))
					: 1000;

			OpQuarto quartoObj;
			// adiciona leito ao quarto existente
			if (quartos.containsKey(quartoNum)) {
				quartoObj = quartos.get(quartoNum);
				// cria um quarto novo a partir do leito
			} else {
				quartoObj = new OpQuarto();
				quartoObj.regras = new ArrayList<>();
				quartoObj.valorCuidado = valorCuidado;
				for (String parameter : keysRegras) {
					String key = leito.get(parameter);
					Map m = (Map) regras.get(key);
					if (m != null) {
						quartoObj.regras.add(m);
						quartoObj.regrasID = key;
						break;
					}
				}
				quartos.put(quartoNum, quartoObj); // <numero do quarto, objeto quarto com leitos dentro>
			}
			quartoObj.numLeitos.add(leitoNum);
		}
	}

	/**************************************************
	 * Inicializa os valores dos pacientes para o GLPSol
	 ***************************************************/
	@Override
	public void initPacientes() throws ExecutionException, InterruptedException {
		// inicializa os pacientes
		pacientes = new ArrayList<>();
		pacientesMap = new HashMap<>();
		leitoAloc = new HashMap<String, OpPaciente>();
		List<LaudosInternacao> laudos = FirebaseDb.getLaudosInternacaoAtivos();
		for (LaudosInternacao laudo : laudos) {
			OpPaciente p = new OpPaciente();
			p.nome = laudo.getNomePaciente();
			p.id = laudo.getIdPaciente();
			p.valorCuidado = 0;
			p.leitoP = "";
			p.laudo = laudo;

			if (laudo.isInternado()) {
				p.leitoP = laudo.getLeito().getNumero();
				leitoAloc.put(laudo.getLeito().getNumero(), p);
			}

			/* 
			 * valor positivo = tenta colocar mais perto dos quartos de enfermeira
			 * 0 = ignora
			 * valor negativo = coloca mais longe 
			 */
			if (laudo.getValorCuidado() != null) {
				p.valorCuidado = Float.parseFloat(String.valueOf(laudo.getValorCuidado()));
			} else {
				p.valorCuidado = 0;
			}

			// caracteristicas
			p.caracts = new int[caracts.size()];
			for (int i = 0; i < caracts.size(); i++) {
				OpCaract curCar = caracts.get(i);
				p.caracts[i] = curCar.opts.indexOf(laudo.get(curCar.caract));
				// caracteristica nao existe no paciente
				if (p.caracts[i] == -1) {
					p.caracts[i] = curCar.opts.indexOf(NONE);
				}
			}
			// adiciona ao array e mapa
			pacientes.add(p);
			pacientesMap.put(p.id, p);
		}

	}

	/**************************************************
	 * Print das alocacoes
	 ***************************************************/
	@Override
	public void printAloc() throws IOException {
		// quartos alocados
		super.printAloc();

		// quartos com excecoes
		for (Map.Entry<String, OpQuarto> quarto : quartosEx.entrySet()) {
			System.out.println(quarto.getKey() + " (Exc)");
			for (String leito : quarto.getValue().numLeitos) {
				System.out.printf("\tLeito %s", leito);
				OpPaciente p = leitoAlocEx.get(leito);
				// leito nao alocado
				if (p == null) {
					System.out.println(" : ---");
				} else {
					System.out.printf(" : %s\n", p.nome);
				}
			}
		}

		// pacientes nao alocados
		if (nAloc.size() > 0)
			printNAloc();
	}

	/**************************************************
	 * Remove quartos com execoes
	 ***************************************************/
	public void initExcecoes() throws ExecutionException, InterruptedException {
		quartosEx = new HashMap();
		leitoAlocEx = new HashMap();
		for (QueryDocumentSnapshot snap : FirebaseDb.getExcecoes()) {
			String quarto = (String) snap.getData().get("quarto");
			if (quartos.get(quarto) == null)
				continue;
			for (String leito : quartos.get(quarto).numLeitos) {
				OpPaciente p = leitoAloc.remove(leito);
				if (p != null) {
					pacientes.remove(p);
					leitoAlocEx.put(leito, pacientesMap.remove(p.id));
				}
			}
			quartosEx.put(quarto, quartos.remove(quarto));
		}
	}

	/**************************************************
	 * Inicializa todos os valores do banco de dados
	 ***************************************************/
	public void init() throws ExecutionException, InterruptedException {
		initQuartos();
		initPacientes();
		initExcecoes();
	}

	public OptimiserResult getOptimisationResult() throws ExecutionException, InterruptedException, IOException {
		init();
		quartoOut(0);
		pacienteOut();
		runAloc(10); // 10 segundos max
		procAloc();
		OptimiserResult result = optInit();
		return result;
	}

}