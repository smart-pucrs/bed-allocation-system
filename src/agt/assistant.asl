!start.

/* Plans */

+!start : true <- .print("Assistant agent enabled.").//; !getOptimisedAllocation.

+!getOptimisedAllocation
<- 
	.send(optimiser,question,getOptimisedAllocation);
	.
	
+!getValidationResult
<- 
	.send(validator,question,getValidationResult);
	.

/* 
 * Validation Result
 */

+!setValidationResult(Result) // result(Id, WasInformed, IsValid, Errors) Errors = [err(Nome, Leito, [mot(Type, Predicate, PredType)])];  
<- 
	+Result; 
	.print("Result: ");
	.print(Result);
	!analiseResult(Result, Response);
	.print("Sending response to agent operador");
	.send(operator,assert,Response);
	!sendToDatabase(Result, Response);
	.

+!sendToDatabase(result(Id, WasInformed, IsValid, Errors), Response)
<-
	
	.send(database,assert,update(Id, Response));
	.abolish(lastValidation(FirstId));
	+lastValidation(Id).
	
+!analiseResult(result(Response), Resp)
	: (Response == "Validation not received")
<-
	Resp = "Desculpe, não recebi o seu plano de alocação para validar. Por favor, envie novamente.";
	.
+!analiseResult(result(Id, WasInformed, IsValid, Errors), Resp)
	: (IsValid == true)
<-
	Resp = "O seu plano de alocação de leitos não possui nenhuma falha. Posso confirmar a alocação?";
	.
+!analiseResult(result(Id, WasInformed, IsValid, Errors), Resp)
	: (IsValid == false)
<-
	Temp = "O seu plano de alocação de leitos possui falhas. Houve um erro ao alocar os seguintes pacientes: ";
	!analiseErrors(Errors, Temp, Response);
	.concat(Response, "Devo confirmar a alocação mesmo assim ou prefere que eu sugira uma alocação otimizada?", Resp);
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
	
/* 
 * Optimisation Result
 */
	
+!analiseOptimisation(optimiserResult(IsAllAllocated,notAlloc(NotAllocList),sugestedAllocation(SugestionList)), Response)
	: IsAllAllocated == "true" & url(URL) & .length(SugestionList,X) & X>0
<-
	.concat("Ok, gerei uma alocação otimizada mantendo o maior número possível de quartos livres e deixando os pacientes mais graves próximos da sala de enfermagem. Você pode vê-la nesse endereço: ", URL, Response);
	.
	
+!analiseOptimisation(optimiserResult(IsAllAllocated,notAlloc(NotAllocList),sugestedAllocation(SugestionList)), Response)
	: IsAllAllocated == "false" & url(URL) & .length(SugestionList,X) & X>0
<-
	.concat("Ok, gerei uma alocação otimizada, porém não conseguirei alocar todos os pacientes ", Temp);
	!getNotAlloc(NotAllocList, Result);
	.concat(Temp, Result, "Você pode ver minha sugestão nesse endereço: ", URL, Response);
	.
	
+!analiseOptimisation(optimiserResult(IsAllAllocated,notAlloc(NotAllocList),sugestedAllocation(SugestionList)), Response)
	: .length(SugestionList,X) & X==0
<-
	.concat("Desculpe-me, mas com os dados disponíveis atualmente, não foi possível gerar uma alocação otimizada.", Response);
	.
	
+!getNotAlloc(NotAllocList, Result)
	: .length(NotAllocList,X) & X==1
<-
	.concat("pois não localizei leito adequado para o paciente ", Temp);
	!getNotAllocName(NotAllocList, Resp);
	.concat(Temp, Resp, Result);
	.
+!getNotAlloc(NotAllocList, Result)
	: .length(NotAllocList,X) & X>1
<-
	.concat("pois não localizei leitos adequados para os pacientes ", Temp);
	!getNotAllocNames(NotAllocList, "", Resp);
	.concat(Temp, Resp, Result);
	.
+!getNotAllocName([Patient|[]], Resp)
<-
	.concat(Patient, ". ", Resp);
	.
	
+!getNotAllocNames([Patient|[]], Temp, Result)
<-
	.concat(Temp, "e ", Patient, ". ", Result);
	.
+!getNotAllocNames([Patient|RestOfTheList], Temp, Result)
<-
	.concat(Temp, Patient, ", ", T);
	!getNotAllocNames(RestOfTheList, T, Result);
	.
	
/*
 * Allocation of Patients
 */

+!allocPatients(Response) //By validation
	: lastValidation(Id)
<-
	.print("Allocationg");
	.send(database,assert,allocByValidation(Id));
	.abolish(lastValidation(Id));
	Response = "Ok, estou alocando os pacientes conforme solicitado";
	.
	
+!allocPatients
<-
	.print("Canceling");
	Response = "Desculpe, eu não tenho a informação relativa a validação, pode por favor solicitar uma nova validação?";
	.
	
+!dontAllocPatients(Response)
	: lastValidation(Id)
<-
	.print("Canceling");
	.send(database,assert,cancelAllocation(Id));
	.abolish(lastValidation(Id));
	Response = "Ok, estou cancelando conforme solicitado";
	.
	
+!dontAllocPatients
<-
	.print("Cancellating");
	Response = "Desculpe, eu não tenho a informação relativa a validação.";
	.	


/* 
 * Kqml Plans
 */

+!kqml_received(Sender,question,getValidationResult,MsgId)
	<-	.print("Agent ", Sender, " requesting validation result.");
		!getValidationResult.
		
+!kqml_received(validator,assert,Result,MsgId)
	<-	.print("Response received from agent validador");
		!setValidationResult(Result).

+!kqml_received(Sender,question,getOptimisedAllocation,MsgId)
	<-	.print("Agent ", Sender, " requesting an optmised allocation.");
		!getOptimisedAllocation.

+!kqml_received(Sender,question,allocPatients,MsgId)
	<-	.print("Agent ", Sender, " requesting confirmation of allocation."); // based on validation
		!allocPatients(Response);
		.send(operator,assert,Response).

+!kqml_received(Sender,question,dontAllocPatients,MsgId)
	<-	.print("Agent ", Sender, " requesting cancellation of allocation.");
		!dontAllocPatients.

+!kqml_received(optimiser,assert,Result,MsgId)
	<-	.print("Result received from agent optimiser"); // optimiserResult(IsAllAllocated,notAlloc([PacienteName]), sugestedAllocation([alloc(PacienteName, NumLeito)])) -> where IsAllAllocated is boolean
		!analiseOptimisation(Result, Response);
		.send(database,assert,saveOptimiserResult);
		.send(operator,assert,Response);
		.


{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
