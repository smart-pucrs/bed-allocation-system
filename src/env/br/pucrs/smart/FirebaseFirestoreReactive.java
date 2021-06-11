package br.pucrs.smart;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.pucrs.smart.interfaces.IValidator;
import br.pucrs.smart.models.firestore.Allocation;
import br.pucrs.smart.models.firestore.LaudosInternacao;
import br.pucrs.smart.models.firestore.Leito;
import br.pucrs.smart.models.firestore.Paciente;
import br.pucrs.smart.models.firestore.TempAloc;
import br.pucrs.smart.models.firestore.Validacao;


public class FirebaseFirestoreReactive {

	private final Firestore db;
	static IValidator mas = null;
	
	private Gson gson = new Gson();

	FirebaseFirestoreReactive(Firestore db) {
		System.out.println("## FirebaseFirestoreReactive started ##");
		this.db = db;
		observeData();

	}
	
	public static void setListener(IValidator agent) {

		System.out.println("## FirebaseFirestoreReactive setListener ##");
		 mas = agent;
	 }

	void observeData() {
		System.out.println("## observeData started ##");
		
		db.collection("tempAloc").addSnapshotListener(new EventListener<QuerySnapshot>() {
			@Override
			public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirestoreException e) {

				if (e != null) {
					System.err.println("-------------------Listen failed: " + e);
					return;
				}

				for (DocumentChange dc : snapshots.getDocumentChanges()) {
					switch (dc.getType()) {
					case ADDED:
						String json = dc.getDocument().getData().toString();
						JsonObject body = gson.fromJson(json, JsonObject.class);
						System.out.println("New: " + body);
						ArrayList<LaudosInternacao> laudos = new ArrayList<LaudosInternacao>();
						Validacao validation = new Validacao();
						if (body != null) {
							TempAloc temp = gson.fromJson(body, TempAloc.class);
							
							for (Allocation alloc : temp.getAllocation()) {
								try {
									LaudosInternacao laudo = new LaudosInternacao();
									laudo = getLaudosInternacao(alloc.getIdPaciente());
									if(laudo!=null) {
										laudo.setLeito(getLeito(alloc.getLeito()));
//										System.out.println("------------------laudo: " + gson.toJson(laudo));
										validation.addPacientes(laudo);
//										System.out.println("------------------validation: ");
									}
								
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (ExecutionException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}

							mas.receiveValidation(validation);

						}
						break;
					case MODIFIED:
						String json1 = dc.getDocument().getData().toString();
						JsonObject body1 = gson.fromJson(json1, JsonObject.class);
						System.out.println("------------------Modified: " + body1);
						break;
					case REMOVED:
						System.out.println("-------------------Removed: " + dc.getDocument().getData());
						break;
					default:
						break;
					}
				}

			}
		});
	}

	Paciente getPaciente(String idPaciente) throws InterruptedException, ExecutionException {
		
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
	
	Leito getLeito(String leitoNum) throws InterruptedException, ExecutionException {
		
		Query query = db.collection("leitos").whereEqualTo("numero", leitoNum);
		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {		
		if (document.exists()) {
			// convert document to POJO
			Leito leito = document.toObject(Leito.class);
			return leito;
		} else {
			System.out.println("No such document!");
		}
		}
		return null;
	}
	
	public LaudosInternacao getLaudosInternacao(String idPaciente) throws InterruptedException, ExecutionException {
		
		Query query = db.collection("laudosInternacao").whereEqualTo("idPaciente", idPaciente).whereEqualTo("ativo", true).whereEqualTo("internado", false);
		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			
		if (document.exists()) {
			// convert document to POJO
			LaudosInternacao laudos = document.toObject(LaudosInternacao.class);
			return laudos;
		} else {
			System.out.println("No such document!");
		}
		}
		return null;
	}

}