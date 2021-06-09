// Agent assistant in project bed-allocation-system

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Assistant agent enabled.").

+!getValidationResult(Response)
<- 
	.send(validator,question,getValidationResult);
	.
	
+!setValidationResult(Result, Response)
<- 
	+Result;
	Response = "Estou analisando a sua validação.";
	.send(operator,assert,"Estou analisando a sua validação.")
	.

+!kqml_received(Sender,question,getValidationResult,MsgId)
	<-	.print("Sender:");
		.print(Sender);
		!getValidationResult(Response);
		.send(Sender,assert,Response).
		
+!kqml_received(validator,assert,Result,MsgId)
	<-	!setValidationResult(Result, Response).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
