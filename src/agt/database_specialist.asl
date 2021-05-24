// Agent database_specialist in project bed-allocation-system

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start 
	: true 
<- 
	.print("Database specialist agent enabled.");
    getData(Data);
	.print("Data: ");
    .print(Data);
    .

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
