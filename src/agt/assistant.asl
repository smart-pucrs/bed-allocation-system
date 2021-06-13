// Agent assistant in project bed-allocation-system

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Assistant agent enabled.").

+!getOptimisedAllocation
<- 
	.send(optimiser,question,getOptimisedAllocation);
	.
	
+!getValidationResult
<- 
	.send(validator,question,getValidationResult);
	.
	
+!setValidationResult(Result) // result(WasInformed, IsValid, Errors) Errors = [err(Nome, Leito, [mot(Type, Predicate, PredType)])];  
<- 
	+Result; 
	.print("Result: ");
	.print(Result);
	!analiseResult(Result, Response);
	.print(Response);
	.print("Sending response to agent operador");
	.send(operator,assert,Response)
	.
	
+!analiseResult(result(WasInformed, IsValid, Errors), Resp)
	: (IsValid == true)
<-
	Resp = "O seu plano de alocação de leitos não possui nenhuma falha. Posso confirmar a alocação?";
	.
+!analiseResult(result(WasInformed, IsValid, Errors), Resp)
	: (IsValid == false)
<-
	Temp = "O seu plano de alocação de leitos possui falhas. Houve um erro ao alocar os seguintes pacientes: ";
	!analiseErrors(Errors, Temp, Response);
	.concat(Response, "Devo confirmar a alocação mesmo assim ou prefere que eu sugira uma alocação otimizada?", Resp);
	.print(Resp);
	.
+!analiseErrors([err(Nome, Leito, Motives)|[]], Temp, Resp)
<-
	.concat(Temp," E ", Nome, " no leito ", Leito, " - Pois o leito  ", Leito ," não é de ", T);
	!analiseMotives(Motives, T, Resp);
	.
+!analiseErrors([err(Nome, Leito, Motives)|Rest], Temp, Resp)
<-
	.concat(Temp, Nome, " no leito ", Leito, " - Pois o leito  ", Leito ," não é de ", T);
	!analiseMotives(Motives, T, Response);
	!analiseErrors(Rest, Response, Resp)
	.
+!analiseMotives([mot(Type, Predicate, PredType)|[]], Temp, Resp)
	: Type == "missingPositive"
<-
	.concat(Temp, " e ", Predicate, " ", PredType, " como é o caso do paciente. ", Resp);
	.
+!analiseMotives([mot(Type, Predicate, PredType)|Rest], Temp, Resp)
	: Type == "missingPositive"
<-
	.concat(Temp, Predicate, " ", PredType, ", ", T);
	!analiseMotives(Rest, T, Resp);
	.

+!kqml_received(Sender,question,getValidationResult,MsgId)
	<-	.print("Agent ", Sender, " requesting validation result.");
		!getValidationResult.
		
+!kqml_received(validator,assert,Result,MsgId)
	<-	.print("Response received from agent validador");
		!setValidationResult(Result).

+!kqml_received(Sender,question,getOptimisedAllocation,MsgId)
	<-	.print("Agent ", Sender, " requesting an optmised allocation.");
		!getOptimisedAllocation.
			
+!kqml_received(optimiser,assert,Response,MsgId)
	<-	.print("Response received from agent optimiser");
		.send(operator,assert,Response).


{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
