// CArtAgO artifact code for project smart

package br.pucrs.smart;
import cartago.*;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;

public class FStoreArtifact extends Artifact {
	private FStore aloc;
	
	void init() {
		try{
			aloc = new FStore();
		}catch(Exception e){
			
		}
	}
	
	@OPERATION
	//aloca todos os leitos
	void alocLeitos(OpFeedbackParam<String> response) {
		System.out.println("operation called");
		response.set("Ok, gerei uma alocação otimizada mantendo o maior número possível de quartos livres e deixando os pacientes mais graves próximos da sala de enfermagem. Você pode vê-la clicando aqui: https://explainable-agent.firebaseapp.com/optimized");
	}
	
		

	@OPERATION
	//aloca todos os leitos
	void alocLeitos(Object[] value) {
		try{
			//tempo limite
			String aux = ((String)value[0]).split(",")[1];
			int segundos = Integer.valueOf(aux.substring(0,aux.length()-1));
			
			//inicializa os valores do banco de dados
			aloc.init();
			
			aloc.testC();
			int limit;
			//pacientes que podem ser movidos, default 0 
			aux = ((String)value[1]).split(",")[1];
			try{
				limit = Integer.valueOf(aux.substring(0,aux.length()-1));
			}catch(Exception e){
				limit = 0;
			}
			
			aloc.pacienteBL("34345454354 32323233232 54532513216".split(" "));
			
			aloc.testC();
			
			//gera o modelo dos quartos
			aloc.quartoOut(limit);
			//gera os dados dos pacientes
			aloc.pacienteOut();
			
			//roda o GLPSOL
			aloc.runAloc(segundos);
			System.out.println("proc");
			//processa o output
			aloc.procAloc();
			System.out.println("print");
			//printa o resultado para a tela
			aloc.printAloc();
			
			System.out.println("\n--------------------");
			
		}catch(Exception e){
			System.out.println(e);
		}
	}

	@OPERATION
	//aloca todos os leitos
	void pddl() {
		try{
			aloc.pddl();
//			aloc.tests();
		}catch(Exception e){
			System.out.println(e);
		}
	}
}

