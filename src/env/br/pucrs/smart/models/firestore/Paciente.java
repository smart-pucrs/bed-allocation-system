package br.pucrs.smart.models.firestore;

public class Paciente {
	// Inf prontuario
	private String id;
	private String prontuario;
	// Inf Pessoais
	private boolean falecido;
	private String nome;
	private Long nascimento;
	private String genero;
	private String estadoCivil;
	private String nomeDaMae;
	private String nomeDoPai;
	private String raca;
	private String religiao;
	private String naturalidade;
	private String nacionalidade;
	private String escolaridade;
	// Contato
	private String telefone;
	private String email;
	// Endereço
	private Endereco endereco;
	// Documentos
	private String cartaoSus;
	private String rg;
	private String ufRg;
	private String orgaoEmissorRg;
	private Long emissaoRG;
	private String cpf;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProntuario() {
		return prontuario;
	}

	public void setProntuario(String prontuario) {
		this.prontuario = prontuario;
	}

	public boolean isFalecido() {
		return falecido;
	}

	public void setFalecido(boolean falecido) {
		this.falecido = falecido;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getNascimento() {
		return nascimento;
	}

	public void setNascimento(Long nascimento) {
		this.nascimento = nascimento;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getNomeDaMae() {
		return nomeDaMae;
	}

	public void setNomeDaMae(String nomeDaMae) {
		this.nomeDaMae = nomeDaMae;
	}

	public String getNomeDoPai() {
		return nomeDoPai;
	}

	public void setNomeDoPai(String nomeDoPai) {
		this.nomeDoPai = nomeDoPai;
	}

	public String getRaca() {
		return raca;
	}

	public void setRaca(String raca) {
		this.raca = raca;
	}

	public String getReligiao() {
		return religiao;
	}

	public void setReligiao(String religiao) {
		this.religiao = religiao;
	}

	public String getNaturalidade() {
		return naturalidade;
	}

	public void setNaturalidade(String naturalidade) {
		this.naturalidade = naturalidade;
	}

	public String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public String getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getCartaoSus() {
		return cartaoSus;
	}

	public void setCartaoSus(String cartaoSus) {
		this.cartaoSus = cartaoSus;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getUfRg() {
		return ufRg;
	}

	public void setUfRg(String ufRg) {
		this.ufRg = ufRg;
	}

	public String getOrgaoEmissorRg() {
		return orgaoEmissorRg;
	}

	public void setOrgaoEmissorRg(String orgaoEmissorRg) {
		this.orgaoEmissorRg = orgaoEmissorRg;
	}

	public Long getEmissaoRG() {
		return emissaoRG;
	}

	public void setEmissaoRG(Long emissaoRG) {
		this.emissaoRG = emissaoRG;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
//	    sb.append("CPF:").append(cpf);
	    sb.append("{ ");
	    if (id != null) {
	        sb.append(" id : ");
	        sb.append(id);
	        sb.append(", ");
	    }
		if (prontuario != null) {
	        sb.append(" prontuario : ");
	        sb.append(prontuario);
	        sb.append(", ");
	    }
		// Inf Pessoais
        sb.append(" falecido : ");
        sb.append(falecido);
        sb.append(", ");
		if (nome != null) {
	        sb.append(" nome : ");
	        sb.append(nome);
	        sb.append(", ");
	    }
	    if (nascimento != null) {
	        sb.append(" nascimento : ");
	        sb.append(nascimento);
	        sb.append(", ");
	    }
		if (genero != null) {
	        sb.append(" genero : ");
	        sb.append(genero);
	        sb.append(", ");
	    }
		if (estadoCivil != null) {
	        sb.append(" estadoCivil : ");
	        sb.append(estadoCivil);
	        sb.append(", ");
	    }
		if (nomeDaMae != null) {
	        sb.append(" nomeDaMae : ");
	        sb.append(nomeDaMae);
	        sb.append(", ");
	    }
		if (nomeDoPai != null) {
	        sb.append(" nomeDoPai : ");
	        sb.append(nomeDoPai);
	        sb.append(", ");
	    }
		if (raca != null) {
	        sb.append(" raca : ");
	        sb.append(raca);
	        sb.append(", ");
	    }
		if (religiao != null) {
	        sb.append(" religiao : ");
	        sb.append(religiao);
	        sb.append(", ");
	    }
		if (naturalidade != null) {
	        sb.append(" naturalidade : ");
	        sb.append(naturalidade);
	        sb.append(", ");
	    }
		if (nacionalidade != null) {
	        sb.append(" nacionalidade : ");
	        sb.append(nacionalidade);
	        sb.append(", ");
	    }
		if (escolaridade != null) {
	        sb.append(" escolaridade : ");
	        sb.append(escolaridade);
	        sb.append(", ");
	    }
		// Contato
		if (telefone != null) {
	        sb.append(" telefone : ");
	        sb.append(telefone);
	        sb.append(", ");
	    }
		if (email != null) {
	        sb.append(" email : ");
	        sb.append(email);
	        sb.append(", ");
	    }
		// Endereço
	    if (endereco != null) {
	        sb.append(" endereco : {");
	        if(endereco.getCep() != null) {
	            sb.append("  cep : ");
	            sb.append(endereco.getCep());
	            sb.append(", ");
	        }
	        if(endereco.getLogradouro() != null) {
	            sb.append("  logradouro : ");
	            sb.append(endereco.getLogradouro());
	            sb.append(", ");
	        }
	        if(endereco.getNumero() != null) {
	            sb.append("  numero : ");
	            sb.append(endereco.getNumero());
	            sb.append(", ");
	        }
	        if(endereco.getComplemento() != null) {
	            sb.append("  complemento : ");
	            sb.append(endereco.getComplemento());
	            sb.append(", ");
	        }
	        if(endereco.getBairro() != null) {
	            sb.append("  bairro : ");
	            sb.append(endereco.getBairro());
	            sb.append(", ");
	        }
	        if(endereco.getCidade() != null) {
	            sb.append("  cidade : ");
	            sb.append(endereco.getCidade());
	            sb.append(", ");
	        }
	        if(endereco.getUf() != null) {
	            sb.append("  uf : ");
	            sb.append(endereco.getUf());
	        }
	        sb.append(" }, ");
	    }
		// Documentos
		if (cartaoSus != null) {
	        sb.append(" cartaoSus : ");
	        sb.append(cartaoSus);
	        sb.append(", ");
	    }
		if (rg != null) {
	        sb.append(" rg : ");
	        sb.append(rg);
	        sb.append(", ");
	    }
		if (ufRg != null) {
	        sb.append(" ufRg : ");
	        sb.append(ufRg);
	        sb.append(", ");
	    }
		if (orgaoEmissorRg != null) {
	        sb.append(" orgaoEmissorRg : ");
	        sb.append(orgaoEmissorRg);
	        sb.append(", ");
	    }
	    if (emissaoRG != null) {
	        sb.append(" emissaoRG : ");
	        sb.append(emissaoRG);
	        sb.append(", ");
	    }
		if (cpf != null) {
	        sb.append(" cpf : ");
	        sb.append(cpf);
	    }
		sb.append("} ");
		return sb.toString();
	}
}
