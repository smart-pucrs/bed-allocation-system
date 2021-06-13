package br.pucrs.smart.models.firestore;

public class Leito {
	
	private String age;
	private String especialidade;
	private String genero;
	private String  id;
	private String numero;
	private PacienteNoLeito paciente;
	private String quarto;
	private String status;
	private String tipoDeCuidado;
	private String tipoDeEncaminhamento;
	private String tipoDeEstadia;
	private String tipoDeLeito;
	private String birthtype;
	private String dist;
	
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getEspecialidade() {
		return especialidade;
	}
	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public PacienteNoLeito getPaciente() {
		return paciente;
	}
	public void setPaciente(PacienteNoLeito paciente) {
		this.paciente = paciente;
	}
	public String getQuarto() {
		return quarto;
	}
	public void setQuarto(String quarto) {
		this.quarto = quarto;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTipoDeCuidado() {
		return tipoDeCuidado;
	}
	public void setTipoDeCuidado(String tipoDeCuidado) {
		this.tipoDeCuidado = tipoDeCuidado;
	}
	public String getTipoDeEncaminhamento() {
		return tipoDeEncaminhamento;
	}
	public void setTipoDeEncaminhamento(String tipoDeEncaminhamento) {
		this.tipoDeEncaminhamento = tipoDeEncaminhamento;
	}
	public String getTipoDeEstadia() {
		return tipoDeEstadia;
	}
	public void setTipoDeEstadia(String tipoDeEstadia) {
		this.tipoDeEstadia = tipoDeEstadia;
	}
	public String getTipoDeLeito() {
		return tipoDeLeito;
	}
	public void setTipoDeLeito(String tipoDeLeito) {
		this.tipoDeLeito = tipoDeLeito;
	}
	public String getBirthtype() {
		return birthtype;
	}
	public void setBirthtype(String birthtype) {
		this.birthtype = birthtype;
	}
	public String getDist() {
		return dist;
	}
	public void setDist(String dist) {
		this.dist = dist;
	}
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
	    sb.append("{ ");
	    if(age != null) {
	        sb.append(" age : ");
	        sb.append(age);
	        sb.append(", ");
	    }
	    if(especialidade != null) {
	        sb.append(" especialidade : ");
	        sb.append(especialidade);
	        sb.append(", ");
	    }
	    if(genero != null) {
	        sb.append(" genero : ");
	        sb.append(genero);
	        sb.append(", ");
	    }
	    if(id != null) {
	        sb.append(" id : ");
	        sb.append(id);
	        sb.append(", ");
	    }
	    if(numero != null) {
	        sb.append(" numero : ");
	        sb.append(numero);
	        sb.append(", ");
	    }
	    if(paciente != null) {
	        sb.append(" paciente : ");
	        sb.append(paciente.toString());
	        sb.append(", ");
	    }
	    if(quarto != null) {
	        sb.append(" quarto : ");
	        sb.append(quarto);
	        sb.append(", ");
	    }
	    if(status != null) {
	        sb.append(" status : ");
	        sb.append(status);
	        sb.append(", ");
	    }
	    if(tipoDeCuidado != null) {
	        sb.append(" tipoDeCuidado : ");
	        sb.append(tipoDeCuidado);
	        sb.append(", ");
	    }
	    if(tipoDeEncaminhamento != null) {
	        sb.append(" tipoDeEncaminhamento : ");
	        sb.append(tipoDeEncaminhamento);
	        sb.append(", ");
	    }
	    if(tipoDeEstadia != null) {
	        sb.append(" tipoDeEstadia : ");
	        sb.append(tipoDeEstadia);
	    }
	    if(tipoDeLeito != null) {
	        sb.append(", ");
	        sb.append(" tipoDeLeito : ");
	        sb.append(tipoDeLeito);
	    }
	    if(birthtype != null) {
	        sb.append(", ");
	        sb.append(" birthtype : ");
	        sb.append(birthtype);
	    }
	    if(dist != null) {
	        sb.append(", ");
	        sb.append(" dist : ");
	        sb.append(dist);
	    }
        sb.append("}");
		return sb.toString();
	}
}
