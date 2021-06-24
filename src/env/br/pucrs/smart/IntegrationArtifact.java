// CArtAgO artifact code for project explainable_agents

package br.pucrs.smart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//import java.util.logging.Logger;

import br.pucrs.smart.interfaces.IAgent;
import br.pucrs.smart.models.FollowupEventInput;
import br.pucrs.smart.models.OutputContexts;
import br.pucrs.smart.models.ResponseDialogflow;
import cartago.*;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class IntegrationArtifact extends Artifact implements IAgent {
	//private Logger logger = Logger.getLogger("ArtefatoIntegracao." + IntegrationArtifact.class.getName());
	String jasonResponse = null;
	Boolean awaitingResponse = true;
	Boolean generatedEvent = false;
	String intentEvent = "";
	OutputContexts jasonOutputContext = null;
	String session = null;
	FollowupEventInput followupEventInput = null;
	HashMap<String, Object> jasonOutputParameters = null;
	
	void init() {
		RestImpl.setListener(this);
	}
	
	@OPERATION
	void replyWithEvent(String response, String eventName) {
		this.jasonResponse = response;
		this.followupEventInput = new FollowupEventInput();
		this.followupEventInput.setName(eventName);
		this.followupEventInput.setLanguageCode("pt-BR");
	}
	
	@OPERATION
	void replyWithContext(String response, OutputContexts context) {
		this.jasonResponse = response;
		this.jasonOutputContext = context;
	}
	
	@OPERATION
	void reply(String response) {
		if (this.awaitingResponse) {
			this.jasonResponse = response;
		} else {
			System.out.println("Reply arrived late");
		}
	}
	
	@OPERATION
	void contextBuilder(String responseId, String contextName, OpFeedbackParam<OutputContexts> outputContext) {
	    OutputContexts context = new OutputContexts();
	    context.setName(this.session + "/contexts/" + contextName);
	    context.setLifespanCount(1);
	    outputContext.set(context);
	}

	@Override
	public ResponseDialogflow processarIntencao(String responseId, String intentName, HashMap<String, Object> parameters, List<OutputContexts> outputContexts, String session) {
		this.session = session;
		this.jasonOutputParameters = parameters;
		this.awaitingResponse = true;
		ResponseDialogflow response = new ResponseDialogflow();
		if (!this.generatedEvent || !this.intentEvent.equals(intentName)) {
//			System.out.println("Entrou no IF ");
			this.generatedEvent = false;
			this.intentEvent = "";
			if (intentName != null) {
				execInternalOp("createRequestBelief", responseId, intentName, parameters, outputContexts, session);
				System.out.println("Defining observable property");
			} else {
				System.out.println("Could not set observable property");
				response.setFulfillmentText("Unrecognized intent");
			}
		}
		int i = 0;
		while (this.jasonResponse == null && i <= 300) {
			try {
				Thread.sleep(10);
				i++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (this.jasonResponse != null) {
			System.out.println("Agent jason's response: " + this.jasonResponse);
			response.setFulfillmentText(this.jasonResponse);
			if (this.jasonOutputContext != null) {
				response.addOutputContexts(this.jasonOutputContext);
				this.jasonOutputContext = null;
			}
			if (this.followupEventInput != null) {
				response.setFollowupEventInput(this.followupEventInput);
				this.followupEventInput = null;
			}
			this.jasonResponse = null;
			this.awaitingResponse = false;
			this.generatedEvent = false;
			this.intentEvent = "";
		} else {
			System.out.println("No response from agent jason");
			//response.setFulfillmentText("Sem resposta do agente");
			FollowupEventInput newEvent = new FollowupEventInput();
			newEvent.setName(removeSpaces(intentName));
			newEvent.setLanguageCode("pt-BR");
			if (this.jasonOutputParameters != null) {
				newEvent.setParameters(this.jasonOutputParameters);
			}
			response.setFollowupEventInput(newEvent);
			this.intentEvent = intentName;
			this.generatedEvent = true;
		}
		this.jasonOutputParameters = null;
		return response;
	}
	
	String removeSpaces(String phrase) {
//		System.out.println(phrase.replaceAll(" ", ""));
		return phrase.replaceAll(" ", "");
	}
	
	// return a list of param(Key1, Value1)
	ListTerm createParamBelief(HashMap<String, Object> parameters) {
		Collection<Term> terms = new LinkedList<Term>();
		for(Map.Entry<String, Object> entry : parameters.entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    Literal l = ASSyntax.createLiteral("param", ASSyntax.createString(key));
		    if (value instanceof String) {
		    	l.addTerm(ASSyntax.createString(value));
		    	terms.add(l);
		    } else if (value instanceof ArrayList){
		    	ArrayList<String> valueArr = (ArrayList<String>) value;
		    	Collection<Term> valuesInTerms = new LinkedList<Term>();
		    	for (String element : valueArr) {
		    		valuesInTerms.add(ASSyntax.createString(element));
				}
		    	l.addTerm(ASSyntax.createList(valuesInTerms));
		    	terms.add(l);
		    } else if (value instanceof Integer){
		    	Integer valueInt = (Integer) value;
		    	l.addTerm(ASSyntax.createNumber(valueInt));
		    	terms.add(l);
		    } else if (value instanceof Double){
		    	Double valueDoub = (Double) value;
		    	l.addTerm(ASSyntax.createNumber(valueDoub));
		    	terms.add(l);
		    } else {
		    	
		    	System.out.println("Parameter " + key + " value reported in unknown format" + value.getClass());
		    }
		}
		return ASSyntax.createList(terms);
	}
	
	
	//return a list of context(Name, LifespanCount, [param(Key2, Value2), param(Key3, Value3)])
	ListTerm createContextBelief(List<OutputContexts> outputContexts) {
		Collection<Term> terms = new LinkedList<Term>();
		for (OutputContexts outputContext : outputContexts) {
			Literal l = ASSyntax.createLiteral("context", ASSyntax.createString(getContextName(outputContext.getName())));
			l.addTerm(ASSyntax.createString(outputContext.getLifespanCount()));
			if (outputContext.getParameters() != null) {
				ListTerm parametersList = createParamBelief(outputContext.getParameters());
				l.addTerm(parametersList);
			}
			terms.add(l);
		}
		
		return ASSyntax.createList(terms);			
	}
	
	String getContextName(String context) {
            String contextName = context.substring(context.indexOf("/contexts/")+10, context.length());
            return contextName;
    }
	
	//add to belief base a request(ResponseId, IntentName, [param(Key, Value), param(Key1, Value1)], [context(Name, LifespanCount, [param(Key2, Value2), param(Key3, Value3)])])
	@INTERNAL_OPERATION
	void createRequestBelief(String responseId, String intentName, HashMap<String, Object> parameters, List<OutputContexts> outputContexts, String session) {
		ListTerm contextsList = null;
		ListTerm paramBelief = null;
		String origin = "";
		if (outputContexts != null) {
			contextsList = createContextBelief(outputContexts);
		}
		if (parameters != null) {
			paramBelief = createParamBelief(parameters);
		}
		if(session.contains("explainable-agent-mnlh")){
			origin = "nurse";
		} else if (session.contains("explainable-agent-operato-gvrw")){
			origin = "operator";
		} else {
			origin = "undefined";
		}
		defineObsProperty("request", ASSyntax.createString(origin), ASSyntax.createString(responseId), ASSyntax.createString(intentName), paramBelief, contextsList);
	}
}
