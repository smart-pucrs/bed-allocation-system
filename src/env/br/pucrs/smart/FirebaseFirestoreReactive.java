package br.pucrs.smart;

import java.util.ArrayList;
import java.util.List;
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

import br.pucrs.smart.models.firestore.Allocation;
import br.pucrs.smart.models.firestore.LaudosInternacao;
import br.pucrs.smart.models.firestore.Leito;
import br.pucrs.smart.models.firestore.Paciente;
import br.pucrs.smart.models.firestore.TempAloc;


public class FirebaseFirestoreReactive {

	private final Firestore db;

	private Gson gson = new Gson();

	FirebaseFirestoreReactive(Firestore db) {
		System.out.println("## PddlBuilder created ##");
		this.db = db;
		observeData();

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
						System.out.println("------------------New: " + body);
						List<Allocation> allocData = new ArrayList<>();
						ArrayList<LaudosInternacao> laudos = new ArrayList<>();
						
						if (body != null) {
							TempAloc temp = gson.fromJson(body, TempAloc.class);
						

							
							for (Allocation c : temp.getAllocation()) {
								System.out.println("------------------ccccccccccc: " + gson.toJson( c));
								try {
//									c.setPacienteData(getPaciente(c.getIdPaciente()));
//									c.setLaudo(getLaudosInternacao(c.getIdPaciente()));
//									c.setLeitoData(getLeito(c.getLeito()));
									LaudosInternacao l = new LaudosInternacao();
									l = getLaudosInternacao(c.getIdPaciente());
									System.out.println("------------------llllllllllllllllll: " + gson.toJson( l));
									if(l!=null) {
										l.setLeito(getLeito(c.getLeito()));
										laudos.add(l);
									}
								
									
//									allocData.add(c);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (ExecutionException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							PddlBuilder a = new PddlBuilder(laudos);
							List<String> toPddl = a.buildPddl();
							System.out.println("### Strings formadas: ");
							for (String str : toPddl) {
								System.out.println(str);
								
							}
							
//							System.out.println("new allocData: " + allocData.toString());
							
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
			System.out.println(paciente.toString());
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
		  System.out.println(document.getId());
		
		if (document.exists()) {
			// convert document to POJO
			Leito leito = document.toObject(Leito.class);
			System.out.println(leito.toString());
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
		  System.out.println(document.getId());
		
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

}