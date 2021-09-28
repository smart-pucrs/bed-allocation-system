package br.pucrs.smart.interfaces;

import br.pucrs.smart.models.firestore.TempAloc;
import br.pucrs.smart.models.firestore.Validacao;

public interface IValidator {
	void receiveValidation(Validacao val, TempAloc tempAloc);
}
