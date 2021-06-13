// Agent validator in project bed-allocation-system

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Validator agent enabled.").

+!getValidationResult(Response)
	: result(WasInformed, IsValid, Errors) & (WasInformed == false)
<-
	.print("Validation completed");
	Response = result(WasInformed, IsValid, Errors);
	.abolish(result(WasInformed, IsValid, Errors));
	+result(true, IsValid, Errors)
	.
	
+!getValidationResult(Response)
<-
	.print("Validation not completed yet, waiting.");
	.wait({+result(false, IsValid, Errors)}, 15000);
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
