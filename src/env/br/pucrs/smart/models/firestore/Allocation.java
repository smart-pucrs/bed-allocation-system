package br.pucrs.smart.models.firestore;

public class Allocation {
	
	private String idPaciente;
	private String leito;
	private Paciente pacienteData;
	private Leito leitoData;
	
	
	
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
        	sb.append(", ");
        }
        if (pacienteData != null) {
        	sb.append(" pacienteData: { ");
        	if (pacienteData.getId() != null) {
        	    sb.append(" id : ");
        	    sb.append(pacienteData.getId());
        	    sb.append(", ");
        	}
        	if (pacienteData.getProntuario() != null) {
        	    sb.append(" prontuario : ");
        	    sb.append(pacienteData.getProntuario());
        	    sb.append(", ");
        	}
        	// Inf Pessoais
        	sb.append(" falecido : ");
        	sb.append(pacienteData.isFalecido());
        	sb.append(", ");
        	if (pacienteData.getNome() != null) {
        	    sb.append(" nome : ");
        	    sb.append(pacienteData.getNome());
        	    sb.append(", ");
        	}
        	if (pacienteData.getNascimento() != null) {
        	    sb.append(" nascimento : ");
        	    sb.append(pacienteData.getNascimento());
        	    sb.append(", ");
        	}
        	if (pacienteData.getGenero() != null) {
        	    sb.append(" genero : ");
        	    sb.append(pacienteData.getGenero());
        	    sb.append(", ");
        	}
        	if (pacienteData.getEstadoCivil() != null) {
        	    sb.append(" estadoCivil : ");
        	    sb.append(pacienteData.getEstadoCivil());
        	    sb.append(", ");
        	}
        	if (pacienteData.getNomeDaMae() != null) {
        	    sb.append(" nomeDaMae : ");
        	    sb.append(pacienteData.getNomeDaMae());
        	    sb.append(", ");
        	}
        	if (pacienteData.getNomeDoPai() != null) {
        	    sb.append(" nomeDoPai : ");
        	    sb.append(pacienteData.getNomeDoPai());
        	    sb.append(", ");
        	}
        	if (pacienteData.getRaca() != null) {
        	    sb.append(" raca : ");
        	    sb.append(pacienteData.getRaca());
        	    sb.append(", ");
        	}
        	if (pacienteData.getReligiao() != null) {
        	    sb.append(" religiao : ");
        	    sb.append(pacienteData.getReligiao());
        	    sb.append(", ");
        	}
        	if (pacienteData.getNaturalidade() != null) {
        	    sb.append(" naturalidade : ");
        	    sb.append(pacienteData.getNaturalidade());
        	    sb.append(", ");
        	}
        	if (pacienteData.getNacionalidade() != null) {
        	    sb.append(" nacionalidade : ");
        	    sb.append(pacienteData.getNacionalidade());
        	    sb.append(", ");
        	}
        	if (pacienteData.getEscolaridade() != null) {
        	    sb.append(" escolaridade : ");
        	    sb.append(pacienteData.getEscolaridade());
        	    sb.append(", ");
        	}
        	// Contato
        	if (pacienteData.getTelefone() != null) {
        	    sb.append(" telefone : ");
        	    sb.append(pacienteData.getTelefone());
        	    sb.append(", ");
        	}
        	if (pacienteData.getEmail() != null) {
        	    sb.append(" email : ");
        	    sb.append(pacienteData.getEmail());
        	    sb.append(", ");
        	}
        	// Endereço
        	if (pacienteData.getEndereco() != null) {
        	    sb.append(" endereco : {");
        	    if(pacienteData.getEndereco().getCep() != null) {
        	        sb.append("  cep : ");
        	        sb.append(pacienteData.getEndereco().getCep());
        	        sb.append(", ");
        	    }
        	    if(pacienteData.getEndereco().getLogradouro() != null) {
        	        sb.append("  logradouro : ");
        	        sb.append(pacienteData.getEndereco().getLogradouro());
        	        sb.append(", ");
        	    }
        	    if(pacienteData.getEndereco().getNumero() != null) {
        	        sb.append("  numero : ");
        	        sb.append(pacienteData.getEndereco().getNumero());
        	        sb.append(", ");
        	    }
        	    if(pacienteData.getEndereco().getComplemento() != null) {
        	        sb.append("  complemento : ");
        	        sb.append(pacienteData.getEndereco().getComplemento());
        	        sb.append(", ");
        	    }
        	    if(pacienteData.getEndereco().getBairro() != null) {
        	        sb.append("  bairro : ");
        	        sb.append(pacienteData.getEndereco().getBairro());
        	        sb.append(", ");
        	    }
        	    if(pacienteData.getEndereco().getCidade() != null) {
        	        sb.append("  cidade : ");
        	        sb.append(pacienteData.getEndereco().getCidade());
        	        sb.append(", ");
        	    }
        	    if(pacienteData.getEndereco().getUf() != null) {
        	        sb.append("  uf : ");
        	        sb.append(pacienteData.getEndereco().getUf());
        	    }
        	    sb.append(" }, ");
        	}
        	// Documentos
        	if (pacienteData.getCartaoSus() != null) {
        	    sb.append(" cartaoSus : ");
        	    sb.append(pacienteData.getCartaoSus());
        	    sb.append(", ");
        	}
        	if (pacienteData.getRg() != null) {
        	    sb.append(" rg : ");
        	    sb.append(pacienteData.getRg());
        	    sb.append(", ");
        	}
        	if (pacienteData.getUfRg() != null) {
        	    sb.append(" ufRg : ");
        	    sb.append(pacienteData.getUfRg());
        	    sb.append(", ");
        	}
        	if (pacienteData.getOrgaoEmissorRg() != null) {
        	    sb.append(" orgaoEmissorRg : ");
        	    sb.append(pacienteData.getOrgaoEmissorRg());
        	    sb.append(", ");
        	}
        	if (pacienteData.getEmissaoRG() != null) {
        	    sb.append(" emissaoRG : ");
        	    sb.append(pacienteData.getEmissaoRG());
        	    sb.append(", ");
        	}
        	if (pacienteData.getCpf() != null) {
        	    sb.append(" cpf : ");
        	    sb.append(pacienteData.getCpf());
        	}
        	sb.append("}, ");
        }
        if (leitoData != null) {
        	sb.append(" leitoData: { ");
        	if(leitoData.getAge() != null) {
        	    sb.append(" age : ");
        	    sb.append(leitoData.getAge());
        	    sb.append(", ");
        	}
        	if(leitoData.getEspecialidade() != null) {
        	    sb.append(" especialidade : ");
        	    sb.append(leitoData.getEspecialidade());
        	    sb.append(", ");
        	}
        	if(leitoData.getGenero() != null) {
        	    sb.append(" genero : ");
        	    sb.append(leitoData.getGenero());
        	    sb.append(", ");
        	}
        	if(leitoData.getId() != null) {
        	    sb.append(" id : ");
        	    sb.append(leitoData.getId());
        	    sb.append(", ");
        	}
        	if(leitoData.getNumero() != null) {
        	    sb.append(" numero : ");
        	    sb.append(leitoData.getNumero());
        	    sb.append(", ");
        	}
        	if(leitoData.getPaciente() != null) {
        	    sb.append(" paciente : { ");
        	    if(leitoData.getPaciente().getProntuario() != null) {
        	        sb.append(" prontuario : ");
        	        sb.append(leitoData.getPaciente().getProntuario());
        	        sb.append(", ");
        	    }
        	    if(leitoData.getPaciente().getNome() != null) {
        	        sb.append(" nome : ");
        	        sb.append(leitoData.getPaciente().getNome());
        	        sb.append(", ");
        	    }
        	    if(leitoData.getPaciente().getGenero() != null) {
        	        sb.append(" genero : ");
        	        sb.append(leitoData.getPaciente().getGenero());
        	        sb.append(", ");
        	    }
        	    if(leitoData.getPaciente().getIdade() != null) {
        	        sb.append(" idade : ");
        	        sb.append(leitoData.getPaciente().getIdade());
        	    }
        	    sb.append("}, ");
        	}
    	    if(leitoData.getQuarto() != null) {
    	        sb.append(" quarto : ");
    	        sb.append(leitoData.getQuarto());
    	        sb.append(", ");
    	    }
        	if(leitoData.getStatus() != null) {
        	    sb.append(" status : ");
        	    sb.append(leitoData.getStatus());
        	    sb.append(", ");
        	}
        	if(leitoData.getTipoDeCuidado() != null) {
        	    sb.append(" tipoDeCuidado : ");
        	    sb.append(leitoData.getTipoDeCuidado());
        	    sb.append(", ");
        	}
        	if(leitoData.getTipoDeEncaminhamento() != null) {
        	    sb.append(" tipoDeEncaminhamento : ");
        	    sb.append(leitoData.getTipoDeEncaminhamento());
        	    sb.append(", ");
        	}
        	if(leitoData.getTipoDeEstadia() != null) {
        	    sb.append(" tipoDeEstadia : ");
        	    sb.append(leitoData.getTipoDeEstadia());
        	    sb.append(", ");
        	}
        	if(leitoData.getTipoDeLeito() != null) {
        	    sb.append(" tipoDeLeito : ");
        	    sb.append(leitoData.getTipoDeLeito());
        	}
        	sb.append("} ");
        }
        sb.append("} ");
		return sb.toString();
	}
	
}
