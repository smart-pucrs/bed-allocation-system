(define (problem aloc)
	(:domain hospital)

	(:objects 
		;pacientes
		23102103133 - paciente  32135131355 - paciente  23266656502 - paciente  09090909091 - paciente  34343434324 - paciente  23433454353 - paciente  93092302930 - paciente  
		;leitos
		100a - leito  100c - leito  100b - leito  112a - leito  112d - leito  112c - leito  112b - leito  113b - leito  113a - leito  114a - leito  114b - leito  115b - leito  115c - leito  115a - leito  116b - leito  116c - leito  116a - leito  117e - leito  117a - leito  117c - leito  117b - leito  117d - leito  
		;Caracteristicas
		tipoDeEncaminhamento--Eletivo - tipoDeEncaminhamento  tipoDeEncaminhamento--Agudo - tipoDeEncaminhamento  tipoDeEncaminhamento--_NONE - tipoDeEncaminhamento  
		genero--Masculino - genero  genero--Feminino - genero  
	)

	(:init 
		(tipoDeEncaminhamento-Eletivo 93092302930)
		(genero-Masculino 93092302930)
		(tipoDeEncaminhamento-Eletivo 23102103133)
		(genero-Feminino 23102103133)
		(tipoDeEncaminhamento-Agudo 34343434324)
		(genero-Feminino 34343434324)
		(tipoDeEncaminhamento-Eletivo 23433454353)
		(genero-Feminino 23433454353)
		(tipoDeEncaminhamento-Agudo 23266656502)
		(genero-Masculino 23266656502)
		(tipoDeEncaminhamento-Agudo 32135131355)
		(genero-Feminino 32135131355)
		(tipoDeEncaminhamento-_NONE 09090909091)
		(genero-Masculino 09090909091)
	)

	(:goal (and
			(in 34343434324 112d)
			(in 09090909091 100c)
			(in 93092302930 100a)
			(in 23433454353 112a)
			(in 23266656502 113b)
			(in 32135131355 115b)
			(in 23102103133 116b)
			(not(ocupado 100b))
			(not(ocupado 112c))
			(not(ocupado 112b))
			(not(ocupado 113a))
			(not(ocupado 114a))
			(not(ocupado 114b))
			(not(ocupado 115c))
			(not(ocupado 115a))
			(not(ocupado 116c))
			(not(ocupado 116a))
			(not(ocupado 117e))
			(not(ocupado 117a))
			(not(ocupado 117c))
			(not(ocupado 117b))
			(not(ocupado 117d))
		)
	)

)