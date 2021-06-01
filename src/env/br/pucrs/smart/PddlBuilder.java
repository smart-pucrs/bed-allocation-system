package br.pucrs.smart;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.gson.Gson;

import br.pucrs.smart.models.firestore.Paciente;
import br.pucrs.smart.models.firestore.TempAloc;

public class PddlBuilder {
	
	private final Firestore db;

	private Gson gson = new Gson();
	
	PddlBuilder(Firestore db) {
		System.out.println("## PddlBuilder created ##");
	    this.db = db;
	    observeData();
	  }
	
	void observeData() {
		System.out.println("## observeData started ##");
		db.collection("tempAloc")
	    .addSnapshotListener(new EventListener<QuerySnapshot>() {
	      @Override
	      public void onEvent(@Nullable QuerySnapshot snapshots,
	                          @Nullable FirestoreException e) {
	    	System.out.println("## onEvent called ##");  
	        if (e != null) {
	          System.err.println("Listen failed:" + e);
	          return;
	        }

	        List<QueryDocumentSnapshot> documents = snapshots.getDocuments();
			for (QueryDocumentSnapshot document : documents) {
				System.out.println("TempAloc ID: " + document.getId());
				System.out.println(gson.toJson(document.toObject(TempAloc.class)));
			}
	        
	        
	        
	        List<String> tempAloc = new ArrayList<>();
	        for (DocumentSnapshot doc : snapshots) {
//	        	System.out.println(gson.toJson(snapshots.toObjects(TempAloc.class)));
	          if (doc.get("Especialidade") != null) {
	        	  tempAloc.add(doc.getString("Especialidade"));
	          }
	        }
	        System.out.println("Current tempAloc: " + tempAloc);
	      }
	    });
	}

}
