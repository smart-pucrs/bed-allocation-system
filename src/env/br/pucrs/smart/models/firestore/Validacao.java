package br.pucrs.smart.models.firestore;

import java.util.List;
import java.util.ArrayList;

public class Validacao { 
	
		private List<LaudosInternacao> pacientes;
		private long saveAt;
		private String problema;
		private String plano;
		private String retorno;
		private boolean valido;
		private boolean concluido;
		private String id;
		private boolean alocar;
		
		public List<LaudosInternacao> getPacientes() {
			return pacientes;
		}
		public void setPacientes(List<LaudosInternacao> pacientes) {
			this.pacientes = pacientes;
		}
		public void addPacientes(LaudosInternacao paciente) {
			if (this.pacientes != null) {
				this.pacientes.add(paciente);
			} else {
				this.pacientes = new ArrayList<LaudosInternacao>();
				this.pacientes.add(paciente);
			}
		}
		public long getSaveAt() {
			return saveAt;
		}
		public void setSaveAt(long saveAt) {
			this.saveAt = saveAt;
		}
		public String getProblema() {
			return problema;
		}
		public void setProblema(String problema) {
			this.problema = problema;
		}
		public String getPlano() {
			return plano;
		}
		public void setPlano(String plano) {
			this.plano = plano;
		}
		public String getRetorno() {
			return retorno;
		}
		public void setRetorno(String retorno) {
			this.retorno = retorno;
		}
		public boolean isValido() {
			return valido;
		}
		public void setValido(boolean valido) {
			this.valido = valido;
		}
		public boolean isConcluido() {
			return concluido;
		}
		public void setConcluido(boolean concluido) {
			this.concluido = concluido;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public boolean isAlocar() {
			return alocar;
		}
		public void setAlocar(boolean alocar) {
			this.alocar = alocar;
		}

		
		@Override
		public String toString() {
		    StringBuilder sb = new StringBuilder();
		    sb.append("{ ");
		    if (id != null) {
	        	sb.append(" id : ");
	        	sb.append(id);
	        	sb.append(", ");
	        }
	        if (pacientes != null) {
		        sb.append(" pacientes : [");
		        for (LaudosInternacao p : pacientes) {
			        sb.append(p.toString());
			        sb.append(", ");
		        }
		        sb.append("] ");
		    }
	        	sb.append(" saveAt : ");
	        	sb.append(saveAt);
	        	sb.append(", ");
	        if (problema != null) {
	        	sb.append(" problema : ");
	        	sb.append(problema);
	        	sb.append(", ");
	        }
	        if (plano != null) {
	        	sb.append(" plano : ");
	        	sb.append(plano);
	        	sb.append(", ");
	        }
	        if (retorno != null) {
	        	sb.append(" retorno : ");
	        	sb.append(retorno);
	        	sb.append(", ");
	        }
	       	sb.append(" valido : ");
	       	sb.append(valido);
	        sb.append(", ");
	       	sb.append(" alocar : ");
	       	sb.append(alocar);
	        sb.append(", ");
	        sb.append(" concluido : ");
	       	sb.append(concluido);
	        sb.append("} ");
			return sb.toString();
		}

}
