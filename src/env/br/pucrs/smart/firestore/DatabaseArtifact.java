// CArtAgO artifact code for project explainable_agents

package br.pucrs.smart.firestore;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;

import br.pucrs.smart.firestore.models.OptimiserResult;
import br.pucrs.smart.firestore.models.Paciente;
import cartago.*;

public class DatabaseArtifact extends Artifact {
	Firestore db;
	private Gson gson = new Gson();

	void init(String databaseKeyPath) {
		try {
			InputStream serviceAccount = new FileInputStream(databaseKeyPath);
			GoogleCredentials credentials;

			credentials = GoogleCredentials.fromStream(serviceAccount);

			FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();
			FirebaseApp.initializeApp(options);

			db = FirestoreClient.getFirestore();
			new FirebaseFirestoreReactive(db);
			new FirebaseDb(db);
		    

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@OPERATION
	void getData(OpFeedbackParam<String> data) {
		try {
			String names = "Patient names: ";
			// asynchronously retrieve all users
			ApiFuture<QuerySnapshot> query = db.collection("pacientes").get();
			// ...
			// query.get() blocks on response
			QuerySnapshot querySnapshot;

			querySnapshot = query.get();

			List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			for (QueryDocumentSnapshot document : documents) {
				System.out.println("[DatabaseArtifact] Paciente: " + document.getId());
				System.out.println(gson.toJson(document.toObject(Paciente.class)));
				names = names + ", " + document.getString("nome");
			}
			data.set(names);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OPERATION
	void setOptimiserResult(OptimiserResult result, OpFeedbackParam<String> response) {
		try {
			response.set(FirebaseDb.addOptimiserResult(result));
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.set("[DatabaseArtifact] There was an error adding the document to the database");
		}
	}
	
	
	@OPERATION
	void updateValidationResult(String id, String response) {
		try {
			DocumentReference docRef = db.collection("validacoes").document(id);
			// (async) Update one field
			ApiFuture<WriteResult> future = docRef.update("retorno", response);
			WriteResult result = future.get();
			System.out.println("[DatabaseArtifact] Write result: " + result);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("[DatabaseArtifact] There was an error adding the document to the database");
		}
	}
	
	
	@OPERATION
	void allocByValidation(String id) {
		try {
			System.out.println("[DatabaseArtifact] Allocating id " + id);
			DocumentReference docRef = db.collection("validacoes").document(id);
			// (async) Update one field
			ApiFuture<WriteResult> future = docRef.update("alocar", true);
			WriteResult result = future.get();
			System.out.println("[DatabaseArtifact] Write result: " + result);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("[DatabaseArtifact] There was an error adding the document to the database");
		}
	}
	
	@OPERATION
	void cancelAllocation(String id) {
		try {
			System.out.println("[DatabaseArtifact] Cancelling id " + id);
			DocumentReference docRef = db.collection("validacoes").document(id);
			// (async) Update one field
			ApiFuture<WriteResult> future = docRef.update("concluido", true);
			WriteResult result = future.get();
			System.out.println("[DatabaseArtifact] Write result: " + result);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("[DatabaseArtifact] There was an error adding the document to the database");
		}
	}
}