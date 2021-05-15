!responder(ResponseId, "Aloc", Params, Contexts).

+request(Origin, ResponseId, IntentName, Params, Contexts)
	:true
<-
	.print("Recebido request ",IntentName," do Dialog");
	!responder(ResponseId, IntentName, Params, Contexts);
	.
	
+!responder(ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Call Jason Agent")
<-
	reply("Olá, eu sou seu agente Jason, em que posso lhe ajudar?");
	.
	
+!responder(ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Aloc")
<-
	reply("Alocacao em progresso");
	alocLeitos(Params);
	.
	
+!responder(ResponseId, IntentName, Params, Contexts)
	: (IntentName == "pddl")
<-
	reply("pddl em progresso");
	pddl;
	.
	
{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
