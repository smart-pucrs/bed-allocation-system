// Agent database_specialist in project bed-allocation-system

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start 
	: true 
<- 
	.print("Database specialist agent enabled.");
//  getData(Data);
//	.print("Data: ");
//  .print(Data);
    .
    
    
+!saveOptimiserResult
<-
	getOptimiserResult(Result);
	setOptimiserResult(Result, Response);
	.print(Response);
	.
+!updateValidationResult(update(Id, Response))
<-
	updateValidationResult(Id, Response);
	.
	
+!allocByValidation(Id)
<-
	allocByValidation(Id);
	.

+!cancelAllocation(Id)
<-
	cancelAllocation(Id);
	.
	
+!kqml_received(assistant,assert,saveOptimiserResult,MsgId)
	<-	.print("Agent assistant wants to save the optimiser result"); // optimiserResult(IsAllAllocated,notAlloc([PacienteName]), sugestedAllocation([alloc(PacienteName, NumLeito)])) -> where IsAllAllocated is boolean
		!saveOptimiserResult;
		.
+!kqml_received(assistant,assert,update(Id, Response),MsgId)
	<-	.print("Agent assistant wants to update the validation result"); 
		!updateValidationResult(update(Id, Response));
		.
+!kqml_received(assistant,assert,allocByValidation(Id),MsgId)
	<-	.print("Agent assistant wants to allocate patients using validation result"); 
		!allocByValidation(Id);
		.
+!kqml_received(assistant,assert,cancelAllocation(Id),MsgId)
	<-	.print("Agent assistant wants to cancell the allocation by validation result"); 
		!cancelAllocation(Id);
		.

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
