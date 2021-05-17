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
		String credentials = "eexplainable-agent-firebase-adminsdk-u4w9g-4d5281c4dc.json";
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
		quartos = new HashMap<>();
		for(QueryDocumentSnapshot leito : db.collection("leitos").get().get().getDocuments()){
			String quartoNam = leito.getString("quarto");
			String leitoNam = leito.getString("numero");
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
		for(QueryDocumentSnapshot snap: db.collection("prontuarios").get().get().getDocuments()){
			Paciente p = new Paciente();
			Map prontuario = snap.getData();
			Map paciente = (Map)prontuario.get("paciente");
			//nome e cpf
			p.nome = (String)paciente.get("nome");
			p.cpf = (String)paciente.get("cpf");
			p.valorCuidado = 0;
			
			//internacao atual 
			Map intern = null; 
			for(Map curIntern : (List<Map>)prontuario.get("internacoes")){
				//internacao ativa
				if((boolean)curIntern.get("ativo")) {
					
					intern = curIntern;
					//internado
					if((boolean)curIntern.get("internado")) {
						String leito = (String)((Map)curIntern.get("leito")).get("numero");
						leitoAloc.put(leito,p);
					}
					
					//%fix%
					if(curIntern.get("valorCuidado") != null) p.valorCuidado = Float.parseFloat(String.valueOf(curIntern.get("valorCuidado")));
			
					break;
				}
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
	*	Inicializa todos os valores do bando de dados
	***************************************************/
	public void init() throws ExecutionException, InterruptedException{
		initQuartos();
		initPacientes();
		initExcecoes();		
	}
}