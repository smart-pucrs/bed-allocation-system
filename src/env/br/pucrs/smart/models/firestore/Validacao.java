package br.pucrs.smart.models.firestore;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Validacao { 
	
		private List<LaudosInternacao> pacientes;
		private Date saveAt;
		private String problema;
		private String plano;
		private String retorno;
		private String erro;
		private String motivo;
		private boolean valido;
		private boolean concluido;
		
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
		public Date getSaveAt() {
			return saveAt;
		}
		public void setSaveAt(Date saveAt) {
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
		public String getErro() {
			return erro;
		}
		public void setErro(String erro) {
			this.erro = erro;
		}
		public String getMotivo() {
			return motivo;
		}
		public void setMotivo(String motivo) {
			this.motivo = motivo;
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
		
		@Override
		public String toString() {
		    StringBuilder sb = new StringBuilder();
		    sb.append("{ ");
	        if (pacientes != null) {
		        sb.append(" pacientes : [");
		        for (LaudosInternacao p : pacientes) {
			        sb.append(p.toString());
			        sb.append(", ");
		        }
		        sb.append("] ");
		    }        
	        if (saveAt != null) {
	        	sb.append(" saveAt : ");
	        	sb.append(saveAt);
	        	sb.append(", ");
	        }
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
	        if (erro != null) {
	        	sb.append(" erro : ");
	        	sb.append(erro);
	        	sb.append(", ");
	        }
	        if (motivo != null) {
	        	sb.append(" motivo : ");
	        	sb.append(motivo);
	        	sb.append(", ");
	        }
	       	sb.append(" valido : ");
	       	sb.append(valido);
	        sb.append(", ");
	        sb.append(" concluido : ");
	       	sb.append(concluido);
	        sb.append("} ");
			return sb.toString();
		}


}
