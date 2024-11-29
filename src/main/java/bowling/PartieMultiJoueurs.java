package bowling;

import java.util.HashMap;

public class PartieMultiJoueurs implements IPartieMultiJoueurs {

	private HashMap<String, PartieMonoJoueur> laPartie;
	private String[] noms;
	private int nombreDeJoueurs;
	private int numeroJoueurAct = -1;

	public String getPhraseRetour() {
		// Retourne la phrase indiquant quel est le prochain tir.
		return "Prochain tir : joueur " + noms[numeroJoueurAct] +
			", tour n° " + laPartie.get(noms[numeroJoueurAct]).numeroTourCourant() +
			", boule n° " + laPartie.get(noms[numeroJoueurAct]).numeroProchainLancer();
	}

	@Override
	public String demarreNouvellePartie(String[] nomsDesJoueurs) throws IllegalArgumentException {
		if (nomsDesJoueurs == null || nomsDesJoueurs.length == 0) {
			throw new IllegalArgumentException("Le tableau de noms est vide");
		}

		// Initialisation
		laPartie = new HashMap<>();
		this.noms = nomsDesJoueurs;
		nombreDeJoueurs = nomsDesJoueurs.length;
		numeroJoueurAct = 0;

		for (String nom : nomsDesJoueurs) {
			laPartie.put(nom, new PartieMonoJoueur());
		}

		return getPhraseRetour();
	}

	@Override
	public String enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException {
		if (numeroJoueurAct == -1) {
			throw new IllegalStateException("La partie n'a pas commencé");
		}

		// Vérifier si tous les joueurs ont terminé la partie
		boolean partieTerminee = true;
		for (String nom : noms) {
			if (!laPartie.get(nom).estTerminee()) {
				partieTerminee = false;
				break;
			}
		}

		if (partieTerminee) {
			return "Partie terminée";
		}

		// Enregistrement du lancer pour le joueur courant
		PartieMonoJoueur joueurPartie = laPartie.get(noms[numeroJoueurAct]);
		joueurPartie.enregistreLancer(nombreDeQuillesAbattues);

		// Si le joueur a terminé son tour (strike ou 2 lancers), on passe au joueur suivant
		if (joueurPartie.estTerminee()) {
			numeroJoueurAct = (numeroJoueurAct + 1) % nombreDeJoueurs;
		}

		// Retourne la phrase avec l'état du prochain lancer
		return getPhraseRetour();
	}

	@Override
	public int scorePour(String nomDuJoueur) throws IllegalArgumentException {
		if (laPartie.get(nomDuJoueur) == null) {
			throw new IllegalArgumentException(nomDuJoueur + " ne joue pas dans cette partie");
		}

		return laPartie.get(nomDuJoueur).score();
	}
}
