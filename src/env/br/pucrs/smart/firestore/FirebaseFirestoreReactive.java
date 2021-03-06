package br.pucrs.smart.firestore;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.pucrs.smart.firestore.models.Allocation;
import br.pucrs.smart.firestore.models.LaudosInternacao;
import br.pucrs.smart.firestore.models.TempAloc;
import br.pucrs.smart.firestore.models.Validacao;
import br.pucrs.smart.validator.IValidator;

public class FirebaseFirestoreReactive {

	private final Firestore db;
	static IValidator mas = null;

	private Gson gson = new Gson();

	FirebaseFirestoreReactive(Firestore db) {
//		System.out.println("## FirebaseFirestoreReactive started ##");
		this.db = db;
		observeData();

	}

	public static void setListener(IValidator agent) {

//		System.out.println("## FirebaseFirestoreReactive setListener ##");
		mas = agent;
	}

	void observeData() {
//		System.out.println("## observeData started ##");

		db.collection("tempAloc").addSnapshotListener(new EventListener<QuerySnapshot>() {
			@Override
			public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirestoreException e) {

				if (e != null) {
					System.err.println("-------------------Listen failed: " + e);
					return;
				}

				for (DocumentChange dc : snapshots.getDocumentChanges()) {
					switch (dc.getType()) {
					case MODIFIED:
						String json = dc.getDocument().getData().toString();
						System.out.println("[FirebaseFirestoreReactive] Received allocation to validate");
						JsonObject body = gson.fromJson(json, JsonObject.class);
						ArrayList<LaudosInternacao> laudos = new ArrayList<LaudosInternacao>();
						Validacao validation = new Validacao();
						if (body != null) {
							TempAloc temp = gson.fromJson(body, TempAloc.class);
							if (!temp.isValidated()) {
								for (Allocation alloc : temp.getAllocation()) {
									try {
										LaudosInternacao laudo = new LaudosInternacao();
										laudo = FirebaseDb.getLaudosInternacaoByIdPaciente(alloc.getIdPaciente());
										if (laudo != null) {
											laudo.setLeito(FirebaseDb.getLeitoByNum(alloc.getLeito()));
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
								System.out.println("[FirebaseFirestoreReactive] Calling validator");
								mas.receiveValidation(validation, temp);
							}
						}
						break;
					case ADDED:
						String json1 = dc.getDocument().getData().toString();
						JsonObject body1 = gson.fromJson(json1, JsonObject.class);
//						System.out.println("------------------New: " + body1);
						break;
					case REMOVED:
//						System.out.println("-------------------Removed: " + dc.getDocument().getData());
						break;
					default:
						break;
					}
				}

			}
		});
	}
}