// Agent communication_specialist in project bed-allocation-system

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Communication specialist agent enabled.").

+request(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: true
<-
	.print("Request received - ",IntentName," from Dialog");
	!responder(RequestedBy, ResponseId, IntentName, Params, Contexts);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Call Jason Agent")
<-
	reply("Olá, eu sou seu agente Jason, em que posso lhe ajudar?");
	.

+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Get Validation Result")
<-
	
	.print("Chatbot of ", RequestedBy, " is requesting plan validation.");
	.send(assistant,question,getValidationResult);
//	reply("Olá, eu sou seu agente Jason, em que posso lhe ajudar?");
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Call Intent By Event")
<-
	replyWithEvent("Respondendo com um evento", "testEvent");
	.

+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Intent Called By Event")
<-
	reply("Respondendo a uma intenção chamada por um evento");
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Call With Contexts and Parameters")
<-
	.print("Os contextos e parâmetros serão listados a seguir.");
	!printContexts(Contexts);
	!printParameters(Params);
	reply("Olá, eu sou seu agente Jason, recebi seus contextos e parâmetros");
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Call With Parameters")
<-
	.print("Os parâmetros serão listados a seguir.");
	!printParameters(Params);
	reply("Olá, eu sou seu agente Jason, recebi seus parâmetros");
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Call With Contexts")
<-
	.print("Os contextos serão listados a seguir.");
	!printContexts(Contexts);
	reply("Olá, eu sou seu agente Jason, recebi seus contextos");
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Reply With Context")
<-
	.print("O contexto será criado a seguir.");
	contextBuilder(ResponseId, "contexto-teste", Context);
	.print("Contexo criado: ", Context);
	replyWithContext("Olá, eu sou seu agente Jason, e estou respondendo com contexto", Context);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: true
<-
	reply("Desculpe, não reconheço essa intensão");
	.

+!printContexts([]).
+!printContexts([Context|List])
<-
	.print(Context);
	!printContexts(List);
	.

+!printParameters([]).
+!printParameters([Param|List])
<-
	.print(Param)
	!printParameters(List)
	.
	
+!hello
    : True
<-
    .print("hello world");
    .

+!kqml_received(Sender,assert,Response,MsgId)
	<-	.print("Answering to chatbot: ", Response);
		reply(Response).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }