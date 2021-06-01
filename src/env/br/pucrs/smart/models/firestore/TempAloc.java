package br.pucrs.smart.models.firestore;

public class TempAloc {
	private String Especialidade;
	private String cpf;
	private String estado;
	private String except;
	private String genero;
	private String leito;
	private String nomePaciente;
	private String tipoDeLeito;
	
	public String getEspecialidade() {
		return Especialidade;
	}
	public void setEspecialidade(String especialidade) {
		Especialidade = especialidade;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getExcept() {
		return except;
	}
	public void setExcept(String except) {
		this.except = except;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getLeito() {
		return leito;
	}
	public void setLeito(String leito) {
		this.leito = leito;
	}
	public String getNomePaciente() {
		return nomePaciente;
	}
	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}
	public String getTipoDeLeito() {
		return tipoDeLeito;
	}
	public void setTipoDeLeito(String tipoDeLeito) {
		this.tipoDeLeito = tipoDeLeito;
	}
	
}
