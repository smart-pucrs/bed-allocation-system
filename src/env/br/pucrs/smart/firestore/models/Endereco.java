package br.pucrs.smart.firestore.models;

public class Endereco {
	private String cep;
	private String logradouro;
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String uf;
	
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
	    sb.append("{ ");
		if(cep != null) {
            sb.append(" cep : ");
            sb.append(cep);
            sb.append(", ");
        }
        if(logradouro != null) {
            sb.append(" logradouro : ");
            sb.append(logradouro);
            sb.append(", ");
        }
        if(numero != null) {
            sb.append(" numero : ");
            sb.append(numero);
            sb.append(", ");
        }
        if(complemento != null) {
            sb.append(" complemento : ");
            sb.append(complemento);
            sb.append(", ");
        }
        if(bairro != null) {
            sb.append(" bairro : ");
            sb.append(bairro);
            sb.append(", ");
        }
        if(cidade != null) {
            sb.append(" cidade : ");
            sb.append(cidade);
            sb.append(", ");
        }
        if(uf != null) {
            sb.append(" uf : ");
            sb.append(uf);
        }
        sb.append("} ");
		return sb.toString();
	}
	
}
