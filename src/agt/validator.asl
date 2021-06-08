// Agent validator in project bed-allocation-system

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Validator agent enabled.").
//read domain
//+!start: domainFile(X) <- readDomain(X).
////read problem
//+domain(_): problemFile(X) <- readProblem(X).
////read plan
//+problem(_): planFile(X) <- readPlan(X).
////try plan
//+plan(_): outFile(X) <- writeReport(X); tryPlan.
//+plan(_): true <- tryPlan.

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
