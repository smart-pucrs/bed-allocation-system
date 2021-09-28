package br.pucrs.smart;

//firestore connect
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.auth.oauth2.GoogleCredentials;

//firestore files
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.io.FileInputStream;

//call db
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

//java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//exceptions
import java.io.IOException;
import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;

import br.pucrs.smart.models.firestore.*;


public class FStore extends DBConnect{
	//firestore
	private Firestore db;
	
	//simbolo de caracteristica nao definida
	private final String NONE = "_NONE";
	
	//Quartos com excecoes
	Map<String, Paciente> leitoAlocEx; //leito -> paciente; 
	Map<String, Quarto> quartosEx; //nome do quarto -> objeto do quarto(leitos e regras) 
	
	//%PLACEHOLDER%
	public FStore() throws IOException{
		System.out.println("Construtor Fstore");
//		String credentials = "eexplainable-agent-firebase-adminsdk-u4w9g-4d5281c4dc.json";
		String credentials = "src/resources/explainable-agent-d4391ab8fb68.json";
		db = FirestoreOptions.getDefaultInstance().toBuilder()
			.setCredentials(GoogleCredentials.fromStream(new FileInputStream(credentials)))
			.build().getService();
		EQUALS = "%igual%"; 
	}
	
	/**************************************************
	*	Inicializa o banco de dados
	***************************************************/
	public FStore(Firestore db){
		this.db = db;
		EQUALS = "%igual%"; 
	}
	
	
	/**************************************************
	*	Inicializa os valores dos quartos para o GLPSol
	***************************************************/
	@Override
	public void initQuartos() throws ExecutionException, InterruptedException{
		//inicializa as regras
		regras = new HashMap<>();
		List<String> keys = new ArrayList<String>();
		for(QueryDocumentSnapshot regrasDoc : db.collection("quartoRegras").get().get().getDocuments()){
			for(Map.Entry<String, Object> regra : regrasDoc.getData().entrySet()){
				regras.put(regra.getKey(), (Map)regra.getValue());
			}
			keys.add(regrasDoc.getId());
		}
		
		//inicializa as caracteristicas possiveis 
		caracts = new ArrayList<>();
		caractMap = new HashMap<>();
		for(Map.Entry<String, Object> caract : db.collection("caracteristicas").document("paciente").get().get().getData().entrySet()){
			Caract c = new Caract(caract.getKey());
			c.opts = (List)caract.getValue();
			caracts.add(c);
			caractMap.put(caract.getKey(),c);
		}
		for(int i = 0; i < caracts.size(); i++){
			caracts.get(i).id = i;
		}
		
		//inicializa os quartos e leitos
		leitos = new HashMap<>();
		quartos = new HashMap<>();
		for(QueryDocumentSnapshot leito : db.collection("leitos").get().get().getDocuments()){
			String quartoNam = leito.getString("quarto");
			String leitoNam = leito.getString("numero");
			Leito l = new Leito();
			l.setAge(leito.getString("age"));
			l.setEspecialidade(leito.getString("especialidade"));
			l.setGenero(leito.getString("genero"));
			l.setId(leito.getString("id"));
			l.setNumero(leito.getString("numero"));
			l.setQuarto(leito.getString("quarto"));
			l.setStatus(leito.getString("status"));
			l.setTipoDeCuidado(leito.getString("tipoDeCuidado"));
			l.setTipoDeEncaminhamento(leito.getString("tipoDeEncaminhamento"));
			l.setTipoDeEstadia(leito.getString("tipoDeEstadia"));
			l.setTipoDeLeito(leito.getString("tipoDeLeito"));
			//l.setBirthtype(leito.getString("birthtype"));
			l.setDist(leito.getString("dist"));
			leitos.put(leitoNam, l);
			
			//%fix%
			float valorCuidado = 1000 - Float.parseFloat(String.valueOf(leito.get("dist")));
			
			Quarto quartoObj;
			//adiciona leito ao quarto existente
			if (quartos.containsKey(quartoNam)){
				quartoObj = quartos.get(quartoNam);
			//cria um quarto novo a partir do leito
			}else {
				quartoObj = new Quarto();
				quartoObj.tipo = new ArrayList<>();
				quartoObj.valorCuidado = valorCuidado;
				for(String parameter : keys){
					String key = leito.getString(parameter);
					Map m = (Map)regras.get(key);
					if(m != null){
						quartoObj.tipo.add(m);
						quartoObj.tipoID = key;
						break;
					}
				}
				quartos.put(quartoNam, quartoObj);
			}				
			quartoObj.leitos.add(leitoNam);
		}		
	}
	
	/**************************************************
	*	Inicializa os valores dos pacientes para o GLPSol
	***************************************************/
	@Override
	public void initPacientes() throws ExecutionException, InterruptedException{
		//inicializa os pacientes
		pacientes = new ArrayList<>();
		pacientesMap = new HashMap<>();
		leitoAloc = new HashMap<String, Paciente>();
		for(QueryDocumentSnapshot snap: db.collection("laudosInternacao").get().get().getDocuments()){
			Paciente p = new Paciente();
			Map laudoIntern = snap.getData();
			Map paciente = (Map)db.collection("pacientes").document((String)laudoIntern.get("idPaciente")).get().get().getData();
			LaudosInternacao laudo = new LaudosInternacao();
			//nome e cpf
			p.id = (String)laudoIntern.get("idPaciente");
			p.nome = (String)paciente.get("nome");
			p.cpf = (String)paciente.get("cpf");
			p.valorCuidado = 0;
			p.leitoP = "";
			
			//internacao atual 
			Map intern = null; 
			boolean ativo = false;
			//internacao ativa
			if((boolean)laudoIntern.get("ativo")) {
				//leito
				Leito leito = new Leito();
				Map lMap = (Map)laudoIntern.get("leito");
				leito.setAge((String)laudoIntern.get("age"));
				leito.setEspecialidade((String)laudoIntern.get("especialidade"));
				leito.setGenero((String)laudoIntern.get("genero"));
				leito.setNumero((String)laudoIntern.get("numero"));
				//leito.setPaciente((String)laudoIntern.get("numero"));
				leito.setQuarto((String)laudoIntern.get("quarto"));
				leito.setStatus((String)laudoIntern.get("status"));
				leito.setTipoDeCuidado((String)laudoIntern.get("tipoDeCuidado"));
				leito.setTipoDeEncaminhamento((String)laudoIntern.get("tipoDeEncaminhamento"));
				leito.setTipoDeEstadia((String)laudoIntern.get("tipoDeEstadia"));
				leito.setTipoDeLeito((String)laudoIntern.get("tipoDeLeito"));
				leito.setBirthtype((String)laudoIntern.get("birthtype"));
				leito.setDist((String)laudoIntern.get("dist"));
				//laudo
				laudo.setAtivo(true);
				laudo.setAge((String)laudoIntern.get("age"));
				laudo.setCrmMedico((String)laudoIntern.get("crmMedico"));
				laudo.setGenero((String)laudoIntern.get("genero"));
				laudo.setId((String)laudoIntern.get("id"));
				laudo.setIdPaciente((String)laudoIntern.get("idPaciente"));
				laudo.setEspecialidade((String) laudoIntern.get("especialidade"));
				laudo.setNomePaciente(p.nome);
				laudo.setInternado((boolean)laudoIntern.get("internado"));
				laudo.setLeito(leito);
				laudo.setMedicoResponsavel((String)laudoIntern.get("medicoResponsavel"));
				laudo.setProntuario((String)laudoIntern.get("prontuario"));
				laudo.setTipoDeCuidado((String)laudoIntern.get("tipoDeCuidado"));
				laudo.setTipoDeEncaminhamento((String)laudoIntern.get("tipoDeEncaminhamento"));
				laudo.setTipoDeEstadia((String)laudoIntern.get("tipoDeEstadia"));
				laudo.setTipoDeLeito((String)laudoIntern.get("tipoDeLeito"));
				laudo.setBirthtype((String)laudoIntern.get("birthtype"));
				p.laudo = laudo;
				
				intern = laudoIntern;
				//internado
				if((boolean)laudoIntern.get("internado")) {
					laudo.setDataInternacao((Integer)(int)(long)laudoIntern.get("dataInternacao"));
					String leitoS = (String)((Map)laudoIntern.get("leito")).get("numero");
//					System.out.println(p.nome);
//					System.out.println(leitoS);
					leitoAloc.put(leitoS,p);
//					System.out.println(leitoAloc);
					p.leitoP = leitoS;
				}
//				System.out.println("");
				//%fix%
				if(laudoIntern.get("valorCuidado") != null) p.valorCuidado = Float.parseFloat(String.valueOf(laudoIntern.get("valorCuidado")));
				ativo = true;
			}else{
				continue;
			}
				
			//caracteristicas
			p.caracts = new int[caracts.size()];
			for(int i = 0; i < caracts.size(); i++){
				Caract curCar = caracts.get(i);
				p.caracts[i] = curCar.opts.indexOf(paciente.get(curCar.caract));
				//caracteristica nao existe no paciente
				if(p.caracts[i] == -1){
					if(intern != null) p.caracts[i] = curCar.opts.indexOf(intern.get(curCar.caract));
					//caracteristica nao existe na internacao
					if(p.caracts[i] == -1){
						p.caracts[i] = curCar.opts.indexOf(NONE);
					}
				}
			}
			//adiciona ao array e mapa
			pacientes.add(p);
			pacientesMap.put(p.cpf, p);
		}
	}
	
	
	/**************************************************
	*	Print das alocacoes
	***************************************************/
	@Override
	public void printAloc() throws IOException{
		//quartos alocados
		super.printAloc();
		
		//quartos com excecoes 
		for(Map.Entry<String, Quarto> quarto : quartosEx.entrySet()){
			System.out.println(quarto.getKey() + " (Exc)");
			for(String leito : quarto.getValue().leitos){
				System.out.printf("\tLeito %s", leito);
				Paciente p = leitoAlocEx.get(leito);
				//leito nao alocado
				if(p == null){
					System.out.println(" : ---");
				}else{
					System.out.printf(" : %s\n", p.nome);
				}
			}
		}
		
		//pacientes nao alocados
		if(nAloc.size() > 0) printNAloc();
	}
	
	
	/**************************************************
	*	Remove quartos com execoes
	***************************************************/
	public void initExcecoes() throws ExecutionException, InterruptedException{
		quartosEx = new HashMap();
		leitoAlocEx = new HashMap();
		for(QueryDocumentSnapshot snap: db.collection("excecoes").get().get().getDocuments()){
			String quarto = (String)snap.getData().get("quarto");
			if(quartos.get(quarto) == null) continue;
			for(String leito : quartos.get(quarto).leitos){
				Paciente p = leitoAloc.remove(leito);
				if(p != null){
					pacientes.remove(p);
					leitoAlocEx.put(leito, pacientesMap.remove(p.cpf));
				}
			}
			quartosEx.put(quarto, quartos.remove(quarto));
		}
	}
	
	
	/**************************************************
	*	Inicializa todos os valores do banco de dados
	***************************************************/
	public void init() throws ExecutionException, InterruptedException{
		initQuartos();
		initPacientes();
		initExcecoes();		
	}
	
	public OptimiserResult getOptimisationResult() throws ExecutionException, InterruptedException, IOException {
		init();
		initExcecoes();
		quartoOut(0);
		pacienteOut();
		runAloc(10); // 10 segundos max
		procAloc();
		OptimiserResult result = optInit();
		return result;
	}
	
}