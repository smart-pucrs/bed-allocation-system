package br.pucrs.smart.validator;

import br.pucrs.smart.firestore.models.TempAloc;
import br.pucrs.smart.firestore.models.Validacao;

public interface IValidator {
	void receiveValidation(Validacao val, TempAloc tempAloc);
}
