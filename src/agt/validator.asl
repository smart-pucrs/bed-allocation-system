// Agent validator in project bed-allocation-system

/* Initial beliefs and rules */

/* Initial goals */

!start.

+result(Id, WasInformed, IsValid, Errors)
<- .print("Validação concluída").

/* Plans */

+!start : true <- .print("Validator agent enabled.").

+!getValidationResult(Response)
	: result(Id, WasInformed, IsValid, Errors) & (WasInformed == false)
<-
	.print("Validation completed");
	.print("result(Id, WasInformed, IsValid, Errors)");
	.print(result(Id, WasInformed, IsValid, Errors));
	Response = result(Id, WasInformed, IsValid, Errors);
	.abolish(result(Id, WasInformed, IsValid, Errors));
	.abolish(waiting);
	+result(Id, true, IsValid, Errors)
	.

+!getValidationResult(Response)
	: waiting
<-
	.print("Validation not received");
	Response = result("Validation not received");
	.abolish(waiting);
	.
	
	
+!getValidationResult(Response)
<-
	.print("Validation not completed yet, waiting.");
	+waiting;
	.wait({+result(Id, false, IsValid, Errors)}, 10000, EventTime);
	.print("I waited for ", EventTime, " milliseconds");
	!getValidationResult(Response);
	.

+!kqml_received(Sender,question,getValidationResult,MsgId)
	<-	.print("Agent ", Sender, " requesting validation result.");
		!getValidationResult(Response);
		.print("Answering to ", Sender);
		.send(Sender,assert,Response).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
