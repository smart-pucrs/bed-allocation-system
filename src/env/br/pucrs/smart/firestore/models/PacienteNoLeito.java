package br.pucrs.smart.firestore.models;

public class PacienteNoLeito {

	private String prontuario;
	private String nome;
	private String genero;
	private String idade;
	
	public String getProntuario() {
		return prontuario;
	}
	public void setProntuario(String prontuario) {
		this.prontuario = prontuario;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getIdade() {
		return idade;
	}
	public void setIdade(String idade) {
		this.idade = idade;
	}
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
	    sb.append("{ ");
	    if(prontuario != null) {
	        sb.append(" prontuario : ");
	        sb.append(prontuario);
	        sb.append(", ");
	    }
	    if(nome != null) {
	        sb.append(" nome : ");
	        sb.append(nome);
	        sb.append(", ");
	    }
	    if(genero != null) {
	        sb.append(" genero : ");
	        sb.append(genero);
	        sb.append(", ");
	    }
	    if(idade != null) {
	        sb.append(" idade : ");
	        sb.append(idade);
	    }
        sb.append("} ");
		return sb.toString();
	}


}
