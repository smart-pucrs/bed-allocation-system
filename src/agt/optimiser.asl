// Agent optimiser in project bed-allocation-system

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Optimiser agent enabled.").//; suggestOptimisedAllocation(R).

+!getOptimisedAllocation(Response)
<-
	.print("Calling Optimiser.");
	suggestOptimisedAllocation(Response); // optimiserResult(IsAllAlocated,notAlloc([PacienteName]), sugestedAllocation([alloc(PacienteName, NumLeito)])) -> where IsAllAlocated is boolean
	+Response;
	.

+!kqml_received(Sender,question,getOptimisedAllocation,MsgId)
	<-	.print("Agent ", Sender, " requesting an optmised allocation.");
		!getOptimisedAllocation(Response);
		.send(Sender,assert,Response).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
