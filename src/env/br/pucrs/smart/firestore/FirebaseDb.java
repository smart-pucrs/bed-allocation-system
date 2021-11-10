package br.pucrs.smart.firestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.gson.Gson;

import br.pucrs.smart.firestore.models.LaudosInternacao;
import br.pucrs.smart.firestore.models.Leito;
import br.pucrs.smart.firestore.models.OptimiserResult;
import br.pucrs.smart.firestore.models.Paciente;
import br.pucrs.smart.firestore.models.ResultVal;
import br.pucrs.smart.firestore.models.TempAloc;
import br.pucrs.smart.firestore.models.Validacao;
import jason.stdlib.foreach;

public class FirebaseDb {

	private static Firestore db;

	private Gson gson = new Gson();

	public FirebaseDb(Firestore db) {
//		System.out.println("## FirebaseDb started ##");
		this.db = db;
	}

	
	public static List<QueryDocumentSnapshot> getRegras() throws InterruptedException, ExecutionException{
		 return db.collection("quartoRegras").get().get().getDocuments();
	}
	
	public static Set<Entry<String, Object>> getCharacteristics() throws InterruptedException, ExecutionException{
		 return db.collection("caracteristicas").document("paciente").get().get()
					.getData().entrySet();
	}
	
	public static List<QueryDocumentSnapshot> getExcecoes() throws InterruptedException, ExecutionException{
		 return db.collection("excecoes").get().get().getDocuments();
	}
	
	public static List<Leito> getLeitos() throws InterruptedException, ExecutionException{
		Query query = db.collection("leitos");
		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		List<Leito> leitos = new ArrayList<Leito>();
		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {

			if (document.exists()) {
				leitos.add(document.toObject(Leito.class));
				
			} else {
				System.out.println("[FirebaseDb] No such document!");
			}
		}
		return leitos; 
		
	}
	
	public static List<LaudosInternacao> getLaudosInternacaoAtivos()
			throws InterruptedException, ExecutionException {

		Query query = db.collection("laudosInternacao").whereEqualTo("ativo", true);
		ApiFuture<QuerySnapshot> querySnapshot = query.get();
		List<LaudosInternacao> laudos = new ArrayList<LaudosInternacao>();
		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {

			if (document.exists()) {
				laudos.add(document.toObject(LaudosInternacao.class));
			} else {
				System.out.println("[FirebaseDb] No such document!");
			}
		}
		return laudos;
	}

	
	public static Paciente getPacienteById(String idPaciente) throws InterruptedException, ExecutionException {

		DocumentReference docRef = db.collection("pacientes").document(idPaciente);
		// asynchronously retrieve the document
		ApiFuture<DocumentSnapshot> future = docRef.get();
		// block on response
		DocumentSnapshot document = future.get();
		if (document.exists()) {
			// convert document to POJO
			Paciente paciente = document.toObject(Paciente.class);
			return paciente;
		} else {
			System.out.println("[FirebaseDb] No such document!");
		}

		return null;
	}

	public static Leito getLeitoByNum(String leitoNum) throws InterruptedException, ExecutionException {

		Query query = db.collection("leitos").whereEqualTo("numero", leitoNum);
		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			if (document.exists()) {
				// convert document to POJO
				Leito leito = document.toObject(Leito.class);

//			System.out.println(leito.toString());
				return leito;
			} else {
				System.out.println("[FirebaseDb] No such document!");
			}
		}
		return null;
	}

	public static Leito getLeitoById(String id) throws InterruptedException, ExecutionException {

		Query query = db.collection("leitos").whereEqualTo("id", id);
		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			if (document.exists()) {
				// convert document to POJO
				Leito leito = document.toObject(Leito.class);

//		System.out.println(leito.toString());
				return leito;
			} else {
				System.out.println("[FirebaseDb] No such document!");
			}
		}
		return null;
	}

	public static LaudosInternacao getLaudosInternacaoByIdPaciente(String idPaciente)
			throws InterruptedException, ExecutionException {

		Query query = db.collection("laudosInternacao").whereEqualTo("idPaciente", idPaciente)
				.whereEqualTo("ativo", true).whereEqualTo("internado", false);
		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {

			if (document.exists()) {
				// convert document to POJO
				LaudosInternacao laudos = document.toObject(LaudosInternacao.class);
//			System.out.println(laudos.toString());
				return laudos;
			} else {
				System.out.println("[FirebaseDb] No such document!");
			}
		}
		return null;
	}

	public static String addOptimiserResult(OptimiserResult op) throws InterruptedException, ExecutionException {
		ApiFuture<DocumentReference> addedDocRef = db.collection("optimiserTest").add(op);
//		ApiFuture<DocumentReference> addedDocRef = db.collection("optimiserResult").add(op);
		op.setId(addedDocRef.get().getId());
		System.out.println("[FirebaseDb] Added document with ID: " + op.getId());
		// Update an existing document
		DocumentReference docRef = db.collection("optimiserTest").document(op.getId());
//		DocumentReference docRef = db.collection("optimiserResult").document(op.getId());
		// (async) Update one field
		ApiFuture<WriteResult> future = docRef.update("id", op.getId());
		WriteResult result = future.get();
		return "Write result: " + result;
	}

	public static String addValidationResult(List<LaudosInternacao> laudos, boolean valido, String guid, String problem, String plan)
			throws InterruptedException, ExecutionException {
		Validacao val = new Validacao();
//		List<LaudosInternacao> laudos = new ArrayList<>();
//		for (ResultVal resultVal : finalResult) {
//			LaudosInternacao laudo = getLaudosInternacaoByIdPaciente(resultVal.getIdPaciente());
//			laudo.setLeito(getLeitoById(resultVal.getIdLeito()));
//			laudos.add(laudo);
//		}
		val.setPacientes(laudos);
		val.setConcluido(false);
		val.setSaveAt((new Date()).getTime());
		val.setValido(valido);
		val.setId(guid);
		val.setAlocar(false);
		val.setProblema(problem);
		val.setPlano(plan);
		
		DocumentReference docRef = db.collection("validacoes").document(val.getId());
		// (async) Update one field
		ApiFuture<WriteResult> future = docRef.set(val);
		WriteResult result = future.get();
//		System.out.println("[FirebaseDb] Write result: " + result);
		return "Write result: " + result;
	}

	public static String setTempAlocValidated(String id) throws InterruptedException, ExecutionException {

		DocumentReference docRef = db.collection("tempAloc").document(id);
		// (async) Update one field
		ApiFuture<WriteResult> future = docRef.update("validated", true);
		WriteResult result = future.get();
		return "Write result: " + result;
	}

}
