package br.pucrs.smart.firestore.models;

public class Allocation {
	
	private String idPaciente;
	private String leito;
	private Paciente pacienteData;
	private Leito leitoData;
	private LaudosInternacao laudo;
	
	
	
	public String getIdPaciente() {
		return idPaciente;
	}
	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
	}
	public String getLeito() {
		return leito;
	}
	public void setLeito(String leito) {
		this.leito = leito;
	}
	public Paciente getPacienteData() {
		return pacienteData;
	}
	public void setPacienteData(Paciente pacienteData) {
		this.pacienteData = pacienteData;
	}
	public Leito getLeitoData() {
		return leitoData;
	}
	public void setLeitoData(Leito leitoData) {
		this.leitoData = leitoData;
	}
	public LaudosInternacao getLaudo() {
		return laudo;
	}
	public void setLaudo(LaudosInternacao laudo) {
		this.laudo = laudo;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("{ ");
        if (idPaciente != null) {
        	sb.append(" idPaciente : ");
        	sb.append(idPaciente);
        	sb.append(", ");
        }
        if (leito != null) {
        	sb.append(" leito : ");
        	sb.append(leito);
        }
        if (pacienteData != null) {
        	sb.append(", ");
        	sb.append(" pacienteData : ");
        	sb.append(pacienteData.toString());
        }
        if (leitoData != null) {
        	sb.append(", ");
        	sb.append(" leitoData : ");
        	sb.append(leitoData.toString());
        }
        if (laudo != null) {
        	sb.append(", ");
        	sb.append(" laudo:  ");
        	sb.append(laudo.toString());
        }
        sb.append("} ");
		return sb.toString();
	}
	
	
}
