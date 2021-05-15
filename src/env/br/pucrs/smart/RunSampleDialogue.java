package br.pucrs.smart;

import jacamo.infra.JaCaMoLauncher;
import jason.JasonException;

public class RunSampleDialogue {

	public static void main(String[] args) {
		try {			
			JaCaMoLauncher.main(new String[] {"explainable_agents.jcm"});
		} catch (JasonException e) {
			System.out.println("Exception: "+e.getMessage());
			e.printStackTrace();
		}

	}

}
