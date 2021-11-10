package br.pucrs.smart.firestore.models;

public class LaudosInternacao {

	private String age;
	private boolean ativo;
	private String crmMedico;
	private Long dataAlta;
	private Long dataInternacao;
	private String especialidade;
	private String genero;
	private String  id;
	private String idPaciente;
	private boolean internado;
	private Leito leito;
	private String MedicoResponsavel;
	private String nomePaciente;
	private String prontuario;
	private String tipoDeCuidado;
	private String tipoDeEncaminhamento;
	private String tipoDeEstadia;
	private String tipoDeLeito;
	private String birthtype;
	private String valorCuidado;
	private String numLeito;


	public String get(String param) {
		switch (param) {
		case "age":
			return age;
		case "especialidade":
			return especialidade;
		case "genero":
			return genero;
		case "id":
			return id;
		case "idPaciente":
			return idPaciente;
		case "nomePaciente":
			return nomePaciente;
		case "prontuario":
			return prontuario;
		case "tipoDeCuidado":
			return tipoDeCuidado;
		case "tipoDeEncaminhamento":
			return tipoDeEncaminhamento;
		case "tipoDeEstadia":
			return tipoDeEstadia;
		case "tipoDeLeito":
			return tipoDeLeito;
		case "birthtype":
			return birthtype;
		case "valorCuidado":
			return valorCuidado;
		case "numLeito":
			return numLeito;
		default:
			return null;
		}
	}

	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	public String getCrmMedico() {
		return crmMedico;
	}
	public void setCrmMedico(String crmMedico) {
		this.crmMedico = crmMedico;
	}
	public Long getDataAlta() {
		return dataAlta;
	}
	public void setDataAlta(Long dataAlta) {
		this.dataAlta = dataAlta;
	}
	public Long getDataInternacao() {
		return dataInternacao;
	}
	public void setDataInternacao(Long dataInternacao) {
		this.dataInternacao = dataInternacao;
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
	public String getIdPaciente() {
		return idPaciente;
	}
	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
	}
	public boolean isInternado() {
		return internado;
	}
	public void setInternado(boolean internado) {
		this.internado = internado;
	}
	public Leito getLeito() {
		return leito;
	}
	public void setLeito(Leito leito) {
		this.leito = leito;
	}
	public String getMedicoResponsavel() {
		return MedicoResponsavel;
	}
	public void setMedicoResponsavel(String medicoResponsavel) {
		MedicoResponsavel = medicoResponsavel;
	}
	public String getNomePaciente() {
		return nomePaciente;
	}
	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}
	public String getProntuario() {
		return prontuario;
	}
	public void setProntuario(String prontuario) {
		this.prontuario = prontuario;
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
	public String getValorCuidado() {
		return valorCuidado;
	}
	public void setValorCuidado(String valorCuidado) {
		this.valorCuidado = valorCuidado;
	}	
	public String getNumLeito() {
		return numLeito;
	}
	public void setNumLeito(String numLeito) {
		this.numLeito = numLeito;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("{ ");
        if (age != null) {
        	sb.append(" age : ");
        	sb.append(age);
        	sb.append(", ");
        }
       	sb.append(" ativo : ");
       	sb.append(ativo);
        sb.append(", ");
        if (crmMedico != null) {
        	sb.append(" crmMedico : ");
        	sb.append(crmMedico);
        	sb.append(", ");
        }
        if (dataAlta != null) {
        	sb.append(" dataAlta : ");
        	sb.append(dataAlta);
        	sb.append(", ");
        }
        if (dataInternacao != null) {
        	sb.append(" dataInternacao : ");
        	sb.append(dataInternacao);
        	sb.append(", ");
        }
        if (especialidade != null) {
        	sb.append(" especialidade : ");
        	sb.append(especialidade);
        	sb.append(", ");
        }
        if (genero != null) {
        	sb.append(" genero : ");
        	sb.append(genero);
        	sb.append(", ");
        }
        if (id != null) {
        	sb.append(" id : ");
        	sb.append(id);
        	sb.append(", ");
        }
        if (idPaciente != null) {
        	sb.append(" idPaciente : ");
        	sb.append(idPaciente);
        	sb.append(", ");
        }
       	sb.append(" internado : ");
       	sb.append(internado);
        if (leito != null) {
        	sb.append(", ");
        	sb.append(" leito : ");
        	sb.append(leito.toString());
        }
        if (MedicoResponsavel != null) {
        	sb.append(", ");
        	sb.append(" MedicoResponsavel : ");
        	sb.append(MedicoResponsavel);
        	sb.append(", ");
        }
        if (nomePaciente != null) {
        	sb.append(" nomePaciente : ");
        	sb.append(nomePaciente);
        	sb.append(", ");
        }
        if (prontuario != null) {
        	sb.append(" prontuario : ");
        	sb.append(prontuario);
        	sb.append(", ");
        }
        if (tipoDeCuidado != null) {
        	sb.append(" tipoDeCuidado : ");
        	sb.append(tipoDeCuidado);
        	sb.append(", ");
        }
        if (tipoDeEncaminhamento != null) {
        	sb.append(" tipoDeEncaminhamento : ");
        	sb.append(tipoDeEncaminhamento);
        	sb.append(", ");
        }
        if (tipoDeEstadia != null) {
        	sb.append(" tipoDeEstadia : ");
        	sb.append(tipoDeEstadia);
        }
        if (tipoDeLeito != null) {
        	sb.append(", ");
        	sb.append(" tipoDeLeito : ");
        	sb.append(tipoDeLeito);
        }
        if (birthtype != null) {
        	sb.append(", ");
        	sb.append(" birthtype : ");
        	sb.append(birthtype);
        }
        if (valorCuidado != null) {
        	sb.append(", ");
        	sb.append(" valorCuidado : ");
        	sb.append(valorCuidado);
        }
        if (numLeito != null) {
        	sb.append(", ");
        	sb.append(" numLeito : ");
        	sb.append(numLeito);
        }
        sb.append("} ");
		return sb.toString();
	}

}
