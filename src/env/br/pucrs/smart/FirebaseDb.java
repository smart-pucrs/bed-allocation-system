package br.pucrs.smart;

import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.gson.Gson;

import br.pucrs.smart.models.firestore.LaudosInternacao;
import br.pucrs.smart.models.firestore.Leito;
import br.pucrs.smart.models.firestore.OptimiserResult;
import br.pucrs.smart.models.firestore.Paciente;

public class FirebaseDb {
	
	private static Firestore db;
	
	private Gson gson = new Gson();

	FirebaseDb(Firestore db) {
		System.out.println("## FirebaseDb started ##");
		this.db = db;
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
			System.out.println("No such document!");
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
			System.out.println("No such document!");
		}
		}
		return null;
	}
	
	public static LaudosInternacao getLaudosInternacaoByIdPaciente(String idPaciente) throws InterruptedException, ExecutionException {
		
		Query query = db.collection("laudosInternacao").whereEqualTo("idPaciente", idPaciente).whereEqualTo("ativo", true).whereEqualTo("internado", false);
		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			
		if (document.exists()) {
			// convert document to POJO
			LaudosInternacao laudos = document.toObject(LaudosInternacao.class);
//			System.out.println(laudos.toString());
			return laudos;
		} else {
			System.out.println("No such document!");
		}
		}
		return null;
	}
	
	public static String addOptimiserResult(OptimiserResult op) throws InterruptedException, ExecutionException {
		ApiFuture<DocumentReference> addedDocRef = db.collection("optimiserResult").add(op);
		return "Added document with ID: " + addedDocRef.get().getId();
	}

}
