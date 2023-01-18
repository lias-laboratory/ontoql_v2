/*********************************************************************************
 * This file is part of MariusQL Project.
 * Copyright (C) 2014  LIAS - ENSMA
 *   Teleport 2 - 1 avenue Clement Ader
 *   BP 40109 - 86961 Futuroscope Chasseneuil Cedex - FRANCE
 * 
 * MariusQL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MariusQL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with MariusQL.  If not, see <http://www.gnu.org/licenses/>.
 **********************************************************************************/
package fr.ensma.lias.mariusql.benchmark;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.benchmark.generator.iml.InsertStatementGenerator;
import fr.ensma.lias.mariusql.benchmark.util.Timer;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;

/**
 * This test class is for CFCA ontology model
 * 
 * @author Florian MHUN
 */
public class MariusQLCFCATest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLCFCATest.class);

	@Test
	public void createCFCAOntology() throws ClassNotFoundException, SQLException {
		log.info("MariusQLCFCATest.createCFCAOntology()");

		boolean createExtentEnabled = true;

		MariusQLSession session = this.getSession();
		session.setReferenceLanguage(MariusQLConstants.FRENCH);
		session.setDefaultNameSpace("http://www.cfca.fr/");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Timer t1 = new Timer();

		t1.start();

		// CFCA
		statement.executeUpdate("CREATE #CLASS \"Composant CFCA\" (DESCRIPTOR (#code ='0002-41982799300025#01-1#1')))");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Fiche Technique\" URIType DESCRIPTOR (#code = '0002-41982799300025#02-1#1', #definition[fr] = 'Référence externe à une description technique du composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Référence ERP\" String DESCRIPTOR (#code = '0002-41982799300025#02-2#1', #definition[fr] = 'Référence créée dans l''ERP')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Désignation\" String DESCRIPTOR (#code = '0002-41982799300025#02-3#1', #definition[fr] = 'Désignation')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Fabricant\" String DESCRIPTOR (#code = '0002-41982799300025#02-4#1',#definition[fr] = 'Fabricant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Référence Fabricant\" String DESCRIPTOR (#code = '0002-41982799300025#02-5#1',#definition[fr] = 'Référence fabricant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Représentation\" URIType DESCRIPTOR (#code = '0002-41982799300025#02-6#1',#definition[fr] = 'Représentation du composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Normes\" String ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-8#1', #definition[fr] = 'Liste des normes auxquelles répond le composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Lien\" Ref(\"Composant CFCA\") ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-213#1', #definition[fr] = 'Composant fortement lié à celui décrit')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Références Clients\" String ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-220#1', #definition[fr] = 'Identifiants présents chez les clients')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Obsolète\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-233#1', #definition[fr] = 'Indique que le composant ne doit plus être utilisé')");

		// Accessoires
		statement.executeUpdate(
				"CREATE #CLASS \"Accessoires\" UNDER \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-2#1'))");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires\" ADD \"Poids\" Real DESCRIPTOR (#code = '0002-41982799300025#02-10#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires\" ADD \"Température Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-12#1', #definition[fr] = 'Température minimum supportée par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires\" ADD \"Température Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-13#1', #definition[fr] = 'Température maximum supportée par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires\" ADD \"Diamètre Extérieur\" Real DESCRIPTOR (#code = '0002-41982799300025#02-14#1', #definition[fr] = 'Diamètre extérieur obturateur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires\" ADD \"Longueur Obturateur\" Real DESCRIPTOR (#code = '0002-41982799300025#02-15#1', #definition[fr] = 'Longueur d''un obturateur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires\" ADD \"Diamètre Mini Isolant\" Real DESCRIPTOR (#code = '0002-41982799300025#02-16#1', #definition[fr] = 'Diamètre minimum de l''isolant du composant lié')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires\" ADD \"Diamètre Maxi Isolant\" Real DESCRIPTOR (#code = '0002-41982799300025#02-17#1', #definition[fr] = 'Diamètre maximum de l''isolant du composant lié')");

		statement.executeUpdate(
				"CREATE #CLASS \"Joint Sur Fil\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-3#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Bouchon\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-4#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Cale&Verrou\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-5#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Capot\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-6#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Détrompeur\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-7#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Joint Interface\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-8#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Obturateur\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-9#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Capuchon\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-10#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Accessoires Divers\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-11#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Prise Batterie\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-12#1'))");

		// Divers
		statement.executeUpdate("CREATE #CLASS \"Divers\" (DESCRIPTOR (#code = '0002-41982799300025#01-237#1'))");

		// Surcôte
		statement.executeUpdate(
				"CREATE #CLASS \"Surcôte\" UNDER Divers (DESCRIPTOR (#code = '0002-41982799300025#01-240#1'))");

		// Ports
		statement.executeUpdate(
				"CREATE #CLASS \"Ports\" UNDER Divers (DESCRIPTOR (#code = '0002-41982799300025#01-13#1'))");
		statement.executeUpdate(
				"ALTER #CLASS \"Ports\" ADD \"Référence ERP\" String DESCRIPTOR (#code = '0002-41982799300025#02-19#1', #definition[fr] = 'Référence créée dans l''ERP')");
		statement.executeUpdate(
				"ALTER #CLASS \"Ports\" ADD \"Nom\" String DESCRIPTOR (#code = '0002-41982799300025#02-20#1', #definition[fr] = 'Nom du port du connecteur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Ports\" ADD \"Famille Cosse\" String ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-21#1', #definition[fr] = 'Famille de cosse eligible')");
		statement.executeUpdate(
				"ALTER #CLASS \"Ports\" ADD \"Obturateur\" Ref(Obturateur) ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-22#1', #definition[fr] = 'Indique les obturateurs')");
		statement.executeUpdate(
				"ALTER #CLASS \"Ports\" ADD \"Longueur Dénudage\" Real DESCRIPTOR (#code = '0002-41982799300025#02-23#1', #definition[fr] = 'Longueur de dénudage si aucune cosse')");
		statement.executeUpdate(
				"ALTER #CLASS \"Ports\" ADD Surcôtes Ref(Surcôte) ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-230#1', #definition[fr] = 'Précise le(s) surcôte(s) possible(s)')");

		statement.executeUpdate(
				"CREATE #CLASS \"Ports Connecteur\" UNDER Ports (DESCRIPTOR (#code = '0002-41982799300025#01-14#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Ports DPI\" UNDER Ports (DESCRIPTOR (#code = '0002-41982799300025#01-15#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Ports Composant Elec\" UNDER Ports (DESCRIPTOR (#code = '0002-41982799300025#01-16#1'))");

		// Surcôte Suite
		statement.executeUpdate(
				"ALTER #CLASS Surcôte ADD \"Référence BE du Port\" Ref(Ports) DESCRIPTOR (#code = '0002-41982799300025#02-231#1', #definition[fr] = 'Identifiant du port associé')");
		statement.executeUpdate(
				"ALTER #CLASS Surcôte ADD Orientation String DESCRIPTOR (#code = '0002-41982799300025#02-228#1', #definition[fr] = 'Désignation de l''orientation')");
		statement.executeUpdate(
				"ALTER #CLASS Surcôte ADD Valeur Real DESCRIPTOR (#code = '0002-41982799300025#02-229#1', #definition[fr] = 'Valeur de la surcôte du port')");

		// Connexions
		statement.executeUpdate(
				"CREATE #CLASS Connexions UNDER \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-17#1'))");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-25#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Température Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-27#1', #definition[fr] = 'Température minimum supportée par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Température Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-28#1', #definition[fr] = 'Température maximum supportée par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Famille String DESCRIPTOR (#code = '0002-41982799300025#02-29#1', #definition[fr] = 'Permet de connaître les cosses admissibles')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Section Min\" Real DESCRIPTOR (#code = '0002-41982799300025#02-31#1', #definition[fr] = 'Section minimale acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Section Max\" Real DESCRIPTOR (#code = '0002-41982799300025#02-32#1', #definition[fr] = 'Section maximale acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Lg Denudage\" Real DESCRIPTOR (#code = '0002-41982799300025#02-33#1', #definition[fr] = 'Longueur de dénudage de la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Matière Cosse\" String DESCRIPTOR (#code = '0002-41982799300025#02-35#1', #definition[fr] = 'Matière de la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Finition String DESCRIPTOR (#code = '0002-41982799300025#02-36#1', #definition[fr] = 'Matière du plaquage')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Ampérage maximum 20°\" Real DESCRIPTOR (#code = '0002-41982799300025#02-37#1', #definition[fr] = 'Ampérage supporté par la cosse à 20°C')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Diamètre Mini Isolant\" Real DESCRIPTOR (#code = '0002-41982799300025#02-38#1', #definition[fr] = 'Diamètre minimum de l''isolant du composant lié')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Diamètre Maxi Isolant\" Real DESCRIPTOR (#code = '0002-41982799300025#02-39#1', #definition[fr] = 'Diamètre maximum de l''isolant du composant lié')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Coudée Int DESCRIPTOR (#code = '0002-41982799300025#02-41#1', #definition[fr] = 'Précise l''angle de coudage')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Préco Sertissage\" URIType DESCRIPTOR (#code = '0002-41982799300025#02-42#1', #definition[fr] = 'Préconisation sertissage')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Joint Sur Fil\" REF(\"Joint Sur Fil\") ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-43#1', #definition[fr] = 'Liste des joints sur fil acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Pour Largeur\" Real DESCRIPTOR (#code = '0002-41982799300025#02-44#1', #definition[fr] = 'Indique quelle largeur doit faire la cosse inverse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Pour Hauteur\" Real DESCRIPTOR (#code = '0002-41982799300025#02-45#1', #definition[fr] = 'Indique quelle hauteur doit faire la cosse inverse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Pour Diamètre\" Real DESCRIPTOR (#code = '0002-41982799300025#02-46#1', #definition[fr] = 'Indique quelle diamètre doit faire la cosse inverse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Accrochage Connecteur\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-47#1', #definition[fr] = 'Indique si la cosse possède un ergot pour une fixation dans un connecteur')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Verrouillage Boolean DESCRIPTOR (#code = '0002-41982799300025#02-48#1', #definition[fr] = 'Système de verrouillage sur la cosse inverse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Dévérouillage Boolean DESCRIPTOR (#code = '0002-41982799300025#02-49#1', #definition[fr] = 'Système de dévérouillage avec la cosse inverse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Largeur Real DESCRIPTOR (#code = '0002-41982799300025#02-50#1', #definition[fr] = 'Largeur de la partie active')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Hauteur Real DESCRIPTOR (#code = '0002-41982799300025#02-51#1', #definition[fr] = 'Hauteur de la partie active')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Diamètre Real DESCRIPTOR (#code = '0002-41982799300025#02-52#1', #definition[fr] = 'Diamètre de la partie active')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Pour Trou Diamètre\" Real DESCRIPTOR (#code = '0002-41982799300025#02-53#1', #definition[fr] = 'Indique pour quelle largeur de trou est prévue la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Pour Epaisseur PCB\" Real DESCRIPTOR (#code = '0002-41982799300025#02-54#1', #definition[fr] = 'Indique pour quelle largeur de Pcb est prévue la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Epissure Boolean DESCRIPTOR (#code = '0002-41982799300025#02-56#1', #definition[fr] = 'Indique si le composant peut servir de base à une épissure')");

		statement.executeUpdate(
				"CREATE #CLASS \"Cosse Oeil\" UNDER Connexions (DESCRIPTOR (#code = '0002-41982799300025#01-18#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Cosse Fourche\" UNDER Connexions (DESCRIPTOR (#code = '0002-41982799300025#01-19#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Harpon\" UNDER Connexions (DESCRIPTOR (#code = '0002-41982799300025#01-20#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Bougie\" UNDER Connexions (DESCRIPTOR (#code = '0002-41982799300025#01-21#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Batterie\" UNDER Connexions (DESCRIPTOR (#code = '0002-41982799300025#01-22#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Femelle\" UNDER Connexions (DESCRIPTOR (#code = '0002-41982799300025#01-23#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Mâle\" UNDER Connexions (DESCRIPTOR (#code = '0002-41982799300025#01-24#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Par Touche\" UNDER Connexions (DESCRIPTOR (#code = '0002-41982799300025#01-25#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Manchon\" UNDER Connexions (DESCRIPTOR (#code = '0002-41982799300025#01-26#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Splice\" UNDER Connexions (DESCRIPTOR (#code = '0002-41982799300025#01-27#1'))");

		// Connecteurs
		statement.executeUpdate(
				"CREATE #CLASS Connecteurs UNDER \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-28#1'))");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-57#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Température Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-59#1', #definition[fr] = 'Température minimum supportée par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Température Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-60#1', #definition[fr] = 'Température maximum supportée par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD Famille String DESCRIPTOR (#code = '0002-41982799300025#02-61#1', #definition[fr] = 'Appelation commerciale du connecteur')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Nombre de Voies\" Int DESCRIPTOR (#code = '0002-41982799300025#02-62#1', #definition[fr] = 'Nombre de voies')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Sert Après\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-64#1', #definition[fr] = 'Connecteur nécessitant un sertissage après enfilage')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD Etanche boolean DESCRIPTOR (#code = '0002-41982799300025#02-66#1', #definition[fr] = 'Précise que le connecteur peut être rendu étanche à l''aide d''accessoire')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Liste des Ports\" Ref(Ports) ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-67#1', #definition[fr] = 'Description de chaque voie')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Pas des Voies\" Real DESCRIPTOR (#code = '0002-41982799300025#02-68#1', #definition[fr] = 'Distance en mm entre 2 Voies')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Nbre Rangée\" Int DESCRIPTOR (#code = '0002-41982799300025#02-69#1', #definition[fr] = 'Nombre de rangée sur lesquelles sont réparties les voies')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Connecteur Inverse\" Ref(Connecteurs) DESCRIPTOR (#code = '0002-41982799300025#02-70#1', #definition[fr] = 'Indique l''identifiant BE du connecteur inverse')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Second verrouillage\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-71#1', #definition[fr] = 'Présence d''un second verrouillage')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Matière Connecteur\" String DESCRIPTOR (#code = '0002-41982799300025#02-72#1', #definition[fr] = 'Matière du Connecteur')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD Blindé Boolean DESCRIPTOR (#code = '0002-41982799300025#02-73#1', #definition[fr] = 'Connecteur blindé')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD Fixation Boolean DESCRIPTOR (#code = '0002-41982799300025#02-74#1', #definition[fr] = 'Composant possédant un support pour une fixation')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Diamètre Mini Isolant\" Real DESCRIPTOR (#code = '0002-41982799300025#02-75#1', #definition[fr] = 'Diamètre minimum de l''isolant du composant lié')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Section Min\" Real DESCRIPTOR (#code = '0002-41982799300025#02-76#1', #definition[fr] = 'Section minimale acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Section Max\" Real DESCRIPTOR (#code = '0002-41982799300025#02-77#1', #definition[fr] = 'Section maximale acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Intensite Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-78#1', #definition[fr] = 'Intensité maximale que peut accepter le connecteur')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Tension Admisible\" Int DESCRIPTOR (#code = '0002-41982799300025#02-79#1', #definition[fr] = 'Tension admissible en volt pour un connecteur')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Orientation Surcôte\" String ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-224#1', #definition[fr] = 'Liste le nombre de surcôte possible')");

		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 1 Voie\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-29#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 2 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-30#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 3 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-31#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 4 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-32#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 5 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-33#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 6 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-34#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 7 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-35#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 8 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-36#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 9 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-37#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 10 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-38#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 11 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-39#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 12 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-40#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 13 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-41#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 14 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-42#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 15 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-43#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 16 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-44#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 17 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-45#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 18 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-46#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 19 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-47#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 20 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-48#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 21 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-49#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 22 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-50#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 23 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-51#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 24 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-52#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 25 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-53#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 26 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-54#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 27 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-55#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 28 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-56#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 29 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-57#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 30 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-58#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 32 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-59#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 34 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-60#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 35 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-61#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 36 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-62#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 37 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-63#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 40 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-64#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 42 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-65#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 48 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-66#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 52 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-67#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 54 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-68#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 55 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-69#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 56 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-70#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 58 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-71#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 64 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-72#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 70 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-73#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 90 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-74#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 94 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-75#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Connecteur 96 Voies\" UNDER Connecteurs (DESCRIPTOR (#code = '0002-41982799300025#01-244#1'))");

		// MonoConducteur
		statement.executeUpdate(
				"CREATE #CLASS MonoConducteur under \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-76#1')))");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-80#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Température Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-82#1', #definition[fr] = 'Température minimum supportée par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Température Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-83#1', #definition[fr] = 'Température maximum supportée par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Type Conducteur\" String DESCRIPTOR (#code = '0002-41982799300025#02-84#1', #definition[fr] = 'Famille du conducteur')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Section Mono\" Real DESCRIPTOR (#code = '0002-41982799300025#02-85#1', #definition[fr] = 'Section du monoConducteur (mm²)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Diamètre Extérieur Min\" Real DESCRIPTOR (#code = '0002-41982799300025#02-86#1',#definition[fr] = 'Diamètre extérieur nominal - Tolérance de la gaine (mm)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Diamètre Extérieur Nominal\" Real DESCRIPTOR (#code = '0002-41982799300025#02-87#1',#definition[fr] = 'Diamètre extérieur nominal (mm)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Diamètre Extérieur Max\" Real DESCRIPTOR (#code = '0002-41982799300025#02-88#1',#definition[fr] = 'Diamètre extérieur nominal + Tolérance de la gaine (mm)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Nombre de Brins\" Int DESCRIPTOR (#code = '0002-41982799300025#02-89#1', #definition[fr] = 'Nombre de brins composant le conducteur')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Diamètre des Brins\" Real DESCRIPTOR (#code = '0002-41982799300025#02-90#1', #definition[fr] = 'Diamètre des brins composant le conducteur (mm)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Nature du Conducteur\" String DESCRIPTOR (#code = '0002-41982799300025#02-91#1', #definition[fr] = 'Matériau conducteur')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Poids Cuivre\" Real DESCRIPTOR (#code = '0002-41982799300025#02-92#1', #definition[fr] = 'Poids du cuivre dans un mètre de conducteurs (Kg/m)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Film Papier/Plastique\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-93#1',#definition[fr] = 'Présence d''un film sous la gaine exterieur')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Lg Conditionnement\" Real DESCRIPTOR (#code = '0002-41982799300025#02-232#1',#definition[fr] = 'Longueur maximum exploitable')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Nature Isolant\" String DESCRIPTOR (#code = '0002-41982799300025#02-95#1', #definition[fr] = 'Matière de la gaine extérieure')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Resistance Linéique\" Real DESCRIPTOR (#code = '0002-41982799300025#02-96#1', #definition[fr] = 'Résistance linéique du conducteur (Ohm/Km)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Tension Phase/Phase\" Real DESCRIPTOR (#code = '0002-41982799300025#02-97#1', #definition[fr] = 'Tension entre les phases (V)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Double Isolation\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-98#1', #definition[fr] = 'Présence de deux gaines autour du conducteur')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Dénudage Etagé\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-215#1',#definition[fr] = 'Indique la nécessité d''utiliser une boite rotative pour le dégainage en automatique')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Est Fil de Multi\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-234#1',#definition[fr] = 'Indique si le monoconducteur appartient à un conducteur complexe')");

		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.22 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-77#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.25 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-78#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.34 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-79#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.38 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-236#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.5 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-80#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.6 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-81#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.7 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-82#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.75 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-83#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 1 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-84#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 1.34 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-85#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 1.4 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-86#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 1.5 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-87#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 2 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-88#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 2.5 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-89#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 3 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-90#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 4 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-91#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 5 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-92#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 6 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-93#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 7 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-94#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 10 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-95#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 16 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-96#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 20 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-97#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 25 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-98#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 35 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-99#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 40 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-100#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 50 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-101#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 60 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-102#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 70 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-103#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 75 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-104#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 95 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-105#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 120 mm²\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-106#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur 10AWG\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-107#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur 12AWG\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-108#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur 14AWG\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-109#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur 16AWG\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-110#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur 18AWG\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-111#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur 20AWG\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-112#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur 22AWG\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-113#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur 24AWG\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-114#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Tresse\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-115#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Brin\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-116#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Autre Section\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-117#1'))");

		// Composants électriques
		statement.executeUpdate(
				"CREATE #CLASS \"Composants Electriques\" UNDER \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-118#1'))");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-99#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD \"Température Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-101#1', #definition[fr] = 'Températeur Minimum')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD \"Température Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-102#1', #definition[fr] = 'Températeur Maximum')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD \"Nombre de Pôles\" Int DESCRIPTOR (#code = '0002-41982799300025#02-103#1', #definition[fr] = 'Nombre de pôle du composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD Polarisé Boolean DESCRIPTOR (#code = '0002-41982799300025#02-104#1', #definition[fr] = 'Echange possible entre les pôles')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD \"Liste des Ports\" Ref(Ports) ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-106#1', #definition[fr] = 'Description de chaque voie')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD Tension Real DESCRIPTOR (#code = '0002-41982799300025#02-107#1', #definition[fr] = 'Tension d''utilisation')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD Résistance Real DESCRIPTOR (#code = '0002-41982799300025#02-108#1', #definition[fr] = 'Résistance Nominale')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD Puissance Real DESCRIPTOR (#code = '0002-41982799300025#02-109#1', #definition[fr] = 'Puissance consommée')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD Intensité Real DESCRIPTOR (#code = '0002-41982799300025#02-110#1', #definition[fr] = 'Ampérage du composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD \"Capacité (farad)\" Real DESCRIPTOR (#code = '0002-41982799300025#02-111#1', #definition[fr] = 'Capacité du condensateur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD Betta Real DESCRIPTOR (#code = '0002-41982799300025#02-112#1', #definition[fr] = 'Mesure absolue de la température en Kelvin')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD \"Température de Coupure\" Int DESCRIPTOR (#code = '0002-41982799300025#02-113#1', #definition[fr] = 'Température à laquelle la sécurité se coupe')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD \"Configuration Surcôte\" String ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-225#1', #definition[fr] = 'Liste le nombre de surcôte possible')");

		statement.executeUpdate(
				"CREATE #CLASS Voyant UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-119#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Transformateur UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-120#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Thermostat UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-121#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Thermocouple UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-122#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Thermistance UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-123#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Telerupteur UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-124#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Switch UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-125#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Sonde UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-126#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Sécurité UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-127#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Résistance UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-128#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Relais UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-129#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Régulateur UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-130#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Potentiomètre UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-131#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Minirupteur UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-132#1'))");
		statement.executeUpdate(
				"CREATE #CLASS MicroRupteur UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-133#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Limiteur UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-134#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Led UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-135#1'))");
		statement.executeUpdate(
				"CREATE #CLASS InterRupteur UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-136#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Fusible UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-137#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Filtre UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-138#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Composants Electriques divers\" UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-140#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Disjoncteur UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-141#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Diode UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-142#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Détecteur UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-143#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Contacteur UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-144#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Condensateur UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-145#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Circuit UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-146#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Centrale UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-147#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Capteur UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-148#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Buzzer UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-149#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Bouton UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-150#1'))");

		// Conducteurs Complexes
		statement.executeUpdate(
				"CREATE #CLASS \"Conducteurs Complexes\" under \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-151#1')))");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-114#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Température Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-116#1', #definition[fr] = 'Température minimum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Température Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-117#1', #definition[fr] = 'Température maximum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Type Conducteur\" String DESCRIPTOR (#code = '0002-41982799300025#02-118#1', #definition[fr] = 'Type pour MultiConducteurs')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Nombre Conducteur\" Int DESCRIPTOR (#code = '0002-41982799300025#02-119#1',#definition[fr] = 'Nombre de conducteur sous la gaine hors brin et tresse')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Poids Cuivre\" Real DESCRIPTOR (#code = '0002-41982799300025#02-120#1', #definition[fr] = 'Section en mm2 du conducteur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Diamètre Extérieur Min\" Real DESCRIPTOR (#code = '0002-41982799300025#02-121#1',#definition[fr] = 'Diamètre extérieur nominal - Tolérance de la gaine (mm)')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Diamètre Extérieur Nominal\" Real DESCRIPTOR (#code = '0002-41982799300025#02-122#1',#definition[fr] = 'Diamètre extérieur nominal (mm)')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Diamètre Extérieur Max\" Real DESCRIPTOR (#code = '0002-41982799300025#02-123#1',#definition[fr] = 'Diamètre extérieur nominal + Tolérance de la gaine (mm)')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD Tresse Boolean DESCRIPTOR (#code = '0002-41982799300025#02-124#1',#definition[fr] = 'Présence d''une tresse dans le multiConducteur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Brin de Masse\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-125#1',#definition[fr] = 'Présence d''un brin de masse')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Film Papier/Plastique\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-126#1',#definition[fr] = 'Présence d''un film sous la gaine exterieur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Lg Conditionnement\" Real DESCRIPTOR (#code = '0002-41982799300025#02-128#1',#definition[fr] = 'Longueur maximum exploitable')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Nature Isolant\" String DESCRIPTOR (#code = '0002-41982799300025#02-129#1', #definition[fr] = 'Matière de la gaine extérieure')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Tension Phase/Neutre\" Real DESCRIPTOR (#code = '0002-41982799300025#02-130#1', #definition[fr] = 'Matière de la gaine extérieure')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Tension Phase/Phase\" Real DESCRIPTOR (#code = '0002-41982799300025#02-131#1', #definition[fr] = 'Tension entre deux phases')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Fil Vert/Jaune\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-132#1',#definition[fr] = 'Présence d''un fil Vert/Jaune parmi les conducteurs')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"MonoConducteurs Liés\" Ref(MonoConducteur) ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-133#1',#definition[fr] = 'Identifiant des conducteurs liés')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Connecteur Gauche\" Ref(Connecteurs) DESCRIPTOR (#code = '0002-41982799300025#02-134#1',#definition[fr] = 'Lien vers le connecteur équipé')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Connecteur Droit\" Ref(Connecteurs) DESCRIPTOR (#code = '0002-41982799300025#02-135#1',#definition[fr] = 'Lien vers le connecteur équipé')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Section Principale\" Real DESCRIPTOR (#code = '0002-41982799300025#02-236#1',#definition[fr] = 'Section retrouvée majoritairement dans le multiconducteur')");

		statement.executeUpdate(
				"CREATE #CLASS \"2 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-153#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"3 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-154#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"4 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-155#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"5 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-156#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"6 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-157#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"7 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-158#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"8 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-159#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"9 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-160#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"10 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-161#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"11 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-162#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"12 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-163#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"13 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-164#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"14 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-165#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"15 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-166#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"16 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-167#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"17 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-168#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"18 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-169#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"19 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-170#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"20 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-171#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"24 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-172#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"25 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-173#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"30 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-174#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"36 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-175#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"40 Conducteurs\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-242#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Conducteurs équipés\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-176#1'))");

		// Accessoires Habillages
		statement.executeUpdate(
				"CREATE #CLASS \"Accessoires Habillages\" under \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-177#1')))");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-136#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD \"Température Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-138#1', #definition[fr] = 'Température minimum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD \"Température Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-139#1', #definition[fr] = 'Température maximum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD Fendu Boolean DESCRIPTOR (#code = '0002-41982799300025#02-212#1', #definition[fr] = 'Indique si l''accesoire habillage est fendu ou pas')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD \"Diamètre Intérieur\" REAL DESCRIPTOR (#code = '0002-41982799300025#02-140#1',#definition[fr] = 'Diamètre intérieur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD Matière String DESCRIPTOR (#code = '0002-41982799300025#02-141#1',#definition[fr] = 'Matière de la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD Pas String DESCRIPTOR (#code = '0002-41982799300025#02-142#1',#definition[fr] = 'Pas exprimé en PG ou M des composants')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD \"Epaisseur Tôle\" Real DESCRIPTOR (#code = '0002-41982799300025#02-143#1',#definition[fr] = 'Epaisseur de la tôle où peut être mis le passe fil')");

		statement.executeUpdate(
				"CREATE #CLASS \"Presse Etoupe\" UNDER \"Accessoires Habillages\" (DESCRIPTOR (#code = '0002-41982799300025#01-178#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Raccord UNDER \"Accessoires Habillages\" (DESCRIPTOR (#code = '0002-41982799300025#01-179#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Joint Plat\" UNDER \"Accessoires Habillages\" (DESCRIPTOR (#code = '0002-41982799300025#01-180#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Joint Set\" UNDER \"Accessoires Habillages\" (DESCRIPTOR (#code = '0002-41982799300025#01-181#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Ecrou Accessoires\" UNDER \"Accessoires Habillages\" (DESCRIPTOR (#code = '0002-41982799300025#01-182#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Réducteur UNDER \"Accessoires Habillages\" (DESCRIPTOR (#code = '0002-41982799300025#01-183#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Passe Fils\" UNDER \"Accessoires Habillages\" (DESCRIPTOR (#code = '0002-41982799300025#01-184#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Repère UNDER \"Accessoires Habillages\" (DESCRIPTOR (#code = '0002-41982799300025#01-243#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Ferrite UNDER \"Accessoires Habillages\" (DESCRIPTOR (#code = '0002-41982799300025#01-139#1'))");

		// DPI
		statement.executeUpdate(
				"CREATE #CLASS DPI under \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-185#1')))");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-144#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Température Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-146#1', #definition[fr] = 'Température minimum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Température Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-147#1', #definition[fr] = 'Température maximum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Nombre de Voies\" Int DESCRIPTOR (#code = '0002-41982799300025#02-148#1', #definition[fr] = 'Nombre de voies')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Section Min\" Real DESCRIPTOR (#code = '0002-41982799300025#02-149#1', #definition[fr] = 'Section minimale acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Section Max\" Real DESCRIPTOR (#code = '0002-41982799300025#02-150#1', #definition[fr] = 'Section maximale acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Nombre de Contacts\" Int DESCRIPTOR (#code = '0002-41982799300025#02-151#1', #definition[fr] = 'Section maximale acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Diamètre Mini Isolant\" Real DESCRIPTOR (#code = '0002-41982799300025#02-152#1', #definition[fr] = 'Diamètre minimum de l''isolant du composant lié')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Diamètre Maxi Isolant\" Real DESCRIPTOR (#code = '0002-41982799300025#02-153#1', #definition[fr] = 'Diamètre maximum de l''isolant du composant lié')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Direct/Indirect\" STRING DESCRIPTOR (#code = '0002-41982799300025#02-154#1', #definition[fr] = 'Indique si le dpi est prévu pour un branchement direct sur un PCB')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD Marquage STRING DESCRIPTOR (#code = '0002-41982799300025#02-155#1', #definition[fr] = 'Marquage du composant')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Cloison Lat\" Int DESCRIPTOR (#code = '0002-41982799300025#02-156#1', #definition[fr] = 'Nombre de cloison latérale')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Cloison Cent\" Int DESCRIPTOR (#code = '0002-41982799300025#02-157#1', #definition[fr] = 'Nombre de cloison centrale')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Clé de Verrouillage\" Int DESCRIPTOR (#code = '0002-41982799300025#02-158#1', #definition[fr] = 'Nombre de clé de verrouillage')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Pas des Voies\" Real DESCRIPTOR (#code = '0002-41982799300025#02-159#1', #definition[fr] = 'Distance en mm entre 2 Voies')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Liste des Ports\" Ref(Ports) ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-160#1', #definition[fr] = 'Description de chaque voie')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD Surcôtes Ref(Surcôte) ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-214#1', #definition[fr] = 'Précise le(s) surcôte(s) possible(s)')");

		statement.executeUpdate(
				"CREATE #CLASS \"Autre Pas\" UNDER DPI (DESCRIPTOR (#code = '0002-41982799300025#01-186#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Pas de 2.5\" UNDER DPI (DESCRIPTOR (#code = '0002-41982799300025#01-187#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Pas de 3.96\" UNDER DPI (DESCRIPTOR (#code = '0002-41982799300025#01-188#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Pas de 5\" UNDER DPI (DESCRIPTOR (#code = '0002-41982799300025#01-189#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Rast 2.5\" UNDER DPI (DESCRIPTOR (#code = '0002-41982799300025#01-190#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Rast 2.5 PRO\" UNDER DPI (DESCRIPTOR (#code = '0002-41982799300025#01-191#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Rast 5\" UNDER DPI (DESCRIPTOR (#code = '0002-41982799300025#01-192#1'))");

		// Emballages
		statement.executeUpdate(
				"CREATE #CLASS Emballage under \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-193#1')))");
		statement.executeUpdate(
				"ALTER #CLASS Emballage ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-161#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"ALTER #CLASS Emballage ADD Largeur Real DESCRIPTOR (#code = '0002-41982799300025#02-163#1',#definition[fr] = 'Largeur de l''emballage')");
		statement.executeUpdate(
				"ALTER #CLASS Emballage ADD Marquage STRING DESCRIPTOR (#code = '0002-41982799300025#02-164#1', #definition[fr] = 'Marquage du composant')");
		statement.executeUpdate(
				"ALTER #CLASS Emballage ADD Hauteur Real DESCRIPTOR (#code = '0002-41982799300025#02-165#1',#definition[fr] = 'Hauteur de l''emballage')");
		statement.executeUpdate(
				"ALTER #CLASS Emballage ADD \"Poids Maximum\" Real DESCRIPTOR (#code = '0002-41982799300025#02-166#1',#definition[fr] = 'Poids maximum que peut contenir l''emballage')");

		statement.executeUpdate(
				"CREATE #CLASS Bac UNDER Emballage (DESCRIPTOR (#code = '0002-41982799300025#01-194#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Carton UNDER Emballage (DESCRIPTOR (#code = '0002-41982799300025#01-195#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Couvercle UNDER Emballage (DESCRIPTOR (#code = '0002-41982799300025#01-196#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Film UNDER Emballage (DESCRIPTOR (#code = '0002-41982799300025#01-197#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Intercalaire UNDER Emballage (DESCRIPTOR (#code = '0002-41982799300025#01-198#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Sac UNDER Emballage (DESCRIPTOR (#code = '0002-41982799300025#01-199#1'))");
		statement.executeUpdate(
				"CREATE #CLASS SacBulle UNDER Emballage (DESCRIPTOR (#code = '0002-41982799300025#01-200#1'))");

		// Habillage
		statement.executeUpdate(
				"CREATE #CLASS Habillage under \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-201#1')))");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-167#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD \"Température Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-169#1', #definition[fr] = 'Température minimum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD \"Température Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-170#1', #definition[fr] = 'Température maximum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD \"Diamètre Intérieur\" REAL DESCRIPTOR (#code = '0002-41982799300025#02-171#1',#definition[fr] = 'Diamètre intérieur')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD Fendue BOOLEAN DESCRIPTOR (#code = '0002-41982799300025#02-172#1',#definition[fr] = 'Indique si la gaine est fendue ou non')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD \"Lg Conditionnement\" REAL DESCRIPTOR (#code = '0002-41982799300025#02-210#1', #definition[fr] = 'Longueur maximum exploitable en mm')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD \"Diamètre Extérieur\" Real DESCRIPTOR (#code = '0002-41982799300025#02-174#1', #definition[fr] = 'Diamètre extérieur de la gaine')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD Matière String DESCRIPTOR (#code = '0002-41982799300025#02-175#1', #definition[fr] = 'Matière de la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD Profil String DESCRIPTOR (#code = '0002-41982799300025#02-176#1', #definition[fr] = 'Profil d''une gaine annelée')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD \"Diamètre Après Rétreint\" REAL DESCRIPTOR (#code = '0002-41982799300025#02-177#1', #definition[fr] = 'Diamètre après rétreint de la gaine thermo')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD \"Collante\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-237#1', #definition[fr] = 'Indique si une Gaine Thermo est ThermoCollante')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD \"Marquage\" String DESCRIPTOR (#code = '0002-41982799300025#02-238#1', #definition[fr] = 'Indique le marquage inscrit sur la gaine')");

		statement.executeUpdate(
				"CREATE #CLASS GAR UNDER Habillage (DESCRIPTOR (#code = '0002-41982799300025#01-202#1'))");
		statement.executeUpdate(
				"CREATE #CLASS GANF UNDER Habillage (DESCRIPTOR (#code = '0002-41982799300025#01-203#1'))");
		statement.executeUpdate(
				"CREATE #CLASS GAF UNDER Habillage (DESCRIPTOR (#code = '0002-41982799300025#01-204#1'))");
		statement.executeUpdate(
				"CREATE #CLASS GAFL UNDER Habillage (DESCRIPTOR (#code = '0002-41982799300025#01-205#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Zipper UNDER Habillage (DESCRIPTOR (#code = '0002-41982799300025#01-206#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Tressée UNDER Habillage (DESCRIPTOR (#code = '0002-41982799300025#01-207#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Spiralée UNDER Habillage (DESCRIPTOR (#code = '0002-41982799300025#01-208#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Lisse UNDER Habillage (DESCRIPTOR (#code = '0002-41982799300025#01-209#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Gaines Thermos\" UNDER Habillage (DESCRIPTOR (#code = '0002-41982799300025#01-210#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Feutrine UNDER Habillage (DESCRIPTOR (#code = '0002-41982799300025#01-211#1'))");

		// RubansColliers
		statement.executeUpdate(
				"CREATE #CLASS RubansColliers under \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-212#1')))");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-178#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD \"Température Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-180#1', #definition[fr] = 'Température minimum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD \"Température Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-181#1', #definition[fr] = 'Température maximum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD Matière String DESCRIPTOR (#code = '0002-41982799300025#02-182#1', #definition[fr] = 'Matière de la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD Marquage String DESCRIPTOR (#code = '0002-41982799300025#02-183#1', #definition[fr] = 'Marquage du composant')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD Largeur Real DESCRIPTOR (#code = '0002-41982799300025#02-184#1', #definition[fr] = 'Largeur du support')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD Longueur Real DESCRIPTOR (#code = '0002-41982799300025#02-185#1', #definition[fr] = 'Longueur du support')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD \"Diamètre Fixation\" Real DESCRIPTOR (#code = '0002-41982799300025#02-186#1', #definition[fr] = 'Diamètre fixation')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD Diamètre Real DESCRIPTOR (#code = '0002-41982799300025#02-187#1', #definition[fr] = 'Diamètre de la pastille')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD \"Forme Agrafe\" String DESCRIPTOR (#code = '0002-41982799300025#02-188#1', #definition[fr] = 'Forme de l''agraphe')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD \"Diamètre Toron Maxi\" Real DESCRIPTOR (#code = '0002-41982799300025#02-189#1', #definition[fr] = 'Diamètre maximal du toron à fixer')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD \"Diamètre Toron Mini\" Real DESCRIPTOR (#code = '0002-41982799300025#02-190#1', #definition[fr] = 'Diamètre minimal du toron à fixer')");

		statement.executeUpdate(
				"CREATE #CLASS Collier UNDER RubansColliers (DESCRIPTOR (#code = '0002-41982799300025#01-213#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Collier Agrafe\" UNDER RubansColliers (DESCRIPTOR (#code = '0002-41982799300025#01-214#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Etiquette UNDER RubansColliers (DESCRIPTOR (#code = '0002-41982799300025#01-215#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Pastille UNDER RubansColliers (DESCRIPTOR (#code = '0002-41982799300025#01-216#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Pion Fixation\" UNDER RubansColliers (DESCRIPTOR (#code = '0002-41982799300025#01-217#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Ruban Adhésif\" UNDER RubansColliers (DESCRIPTOR (#code = '0002-41982799300025#01-218#1'))");

		// Surmoulage
		statement.executeUpdate(
				"CREATE #CLASS Surmoulage under \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-219#1')))");
		statement.executeUpdate(
				"ALTER #CLASS Surmoulage ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-191#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"ALTER #CLASS Surmoulage ADD \"Température Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-193#1', #definition[fr] = 'Température minimum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS Surmoulage ADD \"Température Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-194#1', #definition[fr] = 'Température maximum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS Surmoulage ADD \"Matière à Surmouler\" String DESCRIPTOR (#code = '0002-41982799300025#02-195#1', #definition[fr] = 'Pointe sur la matière utilisée')");
		statement.executeUpdate(
				"ALTER #CLASS Surmoulage ADD \"Volume Matière\" Real DESCRIPTOR (#code = '0002-41982799300025#02-196#1', #definition[fr] = 'Volume de matière nécessaire à l''injection pour le surmoulage')");

		statement.executeUpdate(
				"CREATE #CLASS \"Basse Pression\" UNDER Surmoulage (DESCRIPTOR (#code = '0002-41982799300025#01-220#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Haute Pression\" UNDER Surmoulage (DESCRIPTOR (#code = '0002-41982799300025#01-221#1'))");

		// TYFermeture
		statement.executeUpdate(
				"CREATE #CLASS TYFermeture under \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-223#1')))");
		statement.executeUpdate(
				"ALTER #CLASS TYFermeture ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-197#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"ALTER #CLASS TYFermeture ADD \"Température Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-199#1', #definition[fr] = 'Température minimum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS TYFermeture ADD \"Température Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-200#1', #definition[fr] = 'Température maximum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS TYFermeture ADD Matière String DESCRIPTOR (#code = '0002-41982799300025#02-201#1', #definition[fr] = 'Matière de la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS TYFermeture ADD Fixation Boolean DESCRIPTOR (#code = '0002-41982799300025#02-202#1', #definition[fr] = 'Composant possédant un support pour une fixation')");

		statement.executeUpdate(
				"CREATE #CLASS Y UNDER TYFermeture (DESCRIPTOR (#code = '0002-41982799300025#01-224#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Réducteur TYFermeture\" UNDER TYFermeture (DESCRIPTOR (#code = '0002-41982799300025#01-225#1'))");
		statement.executeUpdate(
				"CREATE #CLASS M UNDER TYFermeture (DESCRIPTOR (#code = '0002-41982799300025#01-226#1'))");
		statement.executeUpdate(
				"CREATE #CLASS T UNDER TYFermeture (DESCRIPTOR (#code = '0002-41982799300025#01-227#1'))");
		statement.executeUpdate(
				"CREATE #CLASS A UNDER TYFermeture (DESCRIPTOR (#code = '0002-41982799300025#01-228#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Fermeture UNDER TYFermeture (DESCRIPTOR (#code = '0002-41982799300025#01-229#1'))");

		// Visserie
		statement.executeUpdate(
				"CREATE #CLASS Visserie under \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-230#1')))");
		statement.executeUpdate(
				"ALTER #CLASS Visserie ADD \"Température Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-204#1', #definition[fr] = 'Température minimum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS Visserie ADD \"Température Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-205#1', #definition[fr] = 'Température maximum supportée par le composant (degré Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS Visserie ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-206#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"ALTER #CLASS Visserie ADD \"Diamètre Intérieur\" REAL DESCRIPTOR (#code = '0002-41982799300025#02-207#1',#definition[fr] = 'Diamètre intérieur')");
		statement.executeUpdate(
				"ALTER #CLASS Visserie ADD \"Diamètre Extérieur\" REAL DESCRIPTOR (#code = '0002-41982799300025#02-208#1',#definition[fr] = 'Diamètre extérieur')");
		statement.executeUpdate(
				"ALTER #CLASS Visserie ADD Longueur REAL DESCRIPTOR (#code = '0002-41982799300025#02-209#1',#definition[fr] = 'Longueur')");

		statement.executeUpdate(
				"CREATE #CLASS Ecrou UNDER Visserie (DESCRIPTOR (#code = '0002-41982799300025#01-231#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Goujon UNDER Visserie (DESCRIPTOR (#code = '0002-41982799300025#01-232#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Goupille UNDER Visserie (DESCRIPTOR (#code = '0002-41982799300025#01-233#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Rondelle UNDER Visserie (DESCRIPTOR (#code = '0002-41982799300025#01-234#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Vis UNDER Visserie (DESCRIPTOR (#code = '0002-41982799300025#01-235#1'))");

		// Liste Configuration TY
		statement.executeUpdate(
				"CREATE #CLASS \"Liste Configuration TY\" under Divers (DESCRIPTOR (#code = '0002-41982799300025#01-239#1')))");
		statement.executeUpdate(
				"ALTER #CLASS \"Liste Configuration TY\" ADD \"Référence BE\" Int DESCRIPTOR (#code = '0002-41982799300025#02-219#1', #definition[fr] = 'Clé')");
		statement.executeUpdate(
				"ALTER #CLASS \"Liste Configuration TY\" ADD Diamètre Real DESCRIPTOR (#code = '0002-41982799300025#02-221#1', #definition[fr] = 'Diamètre admissible')");
		statement.executeUpdate(
				"ALTER #CLASS \"Liste Configuration TY\" ADD Profil String DESCRIPTOR (#code = '0002-41982799300025#02-222#1', #definition[fr] = 'Profil pour diamètre admissible')");

		// Liste Sortie TY
		statement.executeUpdate(
				"CREATE #CLASS \"Liste Sortie TY\" under Divers (DESCRIPTOR (#code = '0002-41982799300025#01-238#1')))");
		statement.executeUpdate(
				"ALTER #CLASS \"Liste Sortie TY\" ADD \"Référence BE du TY\" REF(\"TYFermeture\") DESCRIPTOR (#code = '0002-41982799300025#02-217#1', #definition[fr] = 'Lien')");
		statement.executeUpdate(
				"ALTER #CLASS \"Liste Sortie TY\" ADD \"Référence ERP\" String DESCRIPTOR (#code = '0002-41982799300025#02-226#1', #definition[fr] = 'Référence crée dans l''ERP')");
		statement.executeUpdate(
				"ALTER #CLASS \"Liste Sortie TY\" ADD Sortie String DESCRIPTOR (#code = '0002-41982799300025#02-218#1', #definition[fr] = 'Repérage de la sortie')");
		statement.executeUpdate(
				"ALTER #CLASS \"Liste Sortie TY\" ADD \"Configuration Admissible\" REF(\"Liste Configuration TY\") ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-216#1', #definition[fr] = 'Diamètre admissible en fonction du profil')");

		t1.snapshot("CREATE CLASS");

		if (createExtentEnabled) {
			statement.executeUpdate(
					"CREATE EXTENT OF \"Liste Sortie TY\" (\"Référence BE du TY\", \"Référence ERP\",Sortie, \"Configuration Admissible\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Joint Sur Fil\" (\"Fiche Technique\", \"Référence ERP\",\"Désignation\",\"Fabricant\",\"Référence Fabricant\",\"Représentation\", \"Normes\", \"Poids\", \"Température Min\", \"Température Max\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", \"Lien\", \"Références Clients\", \"Obsolète\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Bouchon\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Cale&Verrou\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Capot\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Détrompeur\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Joint Interface\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Obturateur\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Normes, Poids, \"Température Min\", \"Température Max\", \"Diamètre Extérieur\",\"Longueur Obturateur\", \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Capuchon\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Accessoires Divers\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Prise Batterie\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète)");

			statement.executeUpdate(
					"CREATE EXTENT OF \"Ports Connecteur\" (\"Référence ERP\", \"Nom\", \"Famille Cosse\", \"Obturateur\", \"Longueur Dénudage\", Surcôtes)");
			statement.executeUpdate("CREATE EXTENT OF \"Ports DPI\" (\"Référence ERP\", \"Nom\",Surcôtes)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Ports Composant Elec\" (\"Référence ERP\", \"Nom\", \"Famille Cosse\", \"Obturateur\", \"Longueur Dénudage\",Surcôtes)");

			statement.executeUpdate("CREATE EXTENT OF Surcôte (\"Référence BE du Port\", Orientation, Valeur)");

			statement.executeUpdate(
					"CREATE EXTENT OF \"Cosse Oeil\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", Coudée, \"Préco Sertissage\", Diamètre, \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Cosse Fourche\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", Coudée, \"Préco Sertissage\", Diamètre, \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Harpon\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", \"Préco Sertissage\", \"Pour Trou Diamètre\", \"Pour Epaisseur PCB\", Epissure, \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Bougie\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", Coudée, \"Joint Sur Fil\", \"Pour Largeur\", \"Pour Hauteur\", \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Batterie\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", Coudée, \"Préco Sertissage\", \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Femelle\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", Coudée, \"Préco Sertissage\", \"Joint Sur Fil\", \"Pour Largeur\", \"Pour Hauteur\", \"Pour Diamètre\", \"Accrochage Connecteur\", Verrouillage, Dévérouillage, \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Mâle\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", Coudée, \"Préco Sertissage\", \"Joint Sur Fil\", \"Accrochage Connecteur\", Verrouillage, Dévérouillage, Largeur, Hauteur, Diamètre, Epissure, \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Par Touche\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", Coudée, \"Préco Sertissage\", \"Joint Sur Fil\", \"Pour Largeur\", \"Pour Hauteur\", \"Pour Diamètre\", \"Pour Epaisseur PCB\", Epissure, \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Manchon\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", Epissure, \"Lien\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Splice\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", \"Joint Sur Fil\", Epissure, \"Lien\", \"Références Clients\", Obsolète)");

			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 1 Voie\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 2 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 3 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 4 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 5 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 6 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 7 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 8 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 9 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 10 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 11 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 12 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 13 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 14 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 15 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 16 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 17 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 18 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 19 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 20 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 21 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 22 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 23 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 24 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 25 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 26 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 27 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 28 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 29 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 30 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 32 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 34 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 35 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 36 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 37 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 40 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 42 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 48 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 52 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 54 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 55 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 56 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 58 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 64 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 70 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 90 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 94 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 96 Voies\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Famille, \"Nombre de Voies\", \"Sert Après\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rangée\", \"Connecteur Inverse\", \"Second verrouillage\", \"Matière Connecteur\", Blindé, Fixation,  \"Diamètre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surcôte\", \"Références Clients\", Obsolète)");

			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.22 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.25 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.34 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.38 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.5 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.6 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.7 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.75 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 1 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 1.34 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 1.4 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 1.5 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 2 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 2.5 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 3 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 4 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 5 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 6 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 7 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 10 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 16 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 20 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 25 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 35 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 40 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 50 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 60 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 70 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 75 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 95 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 120 mm²\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 10AWG\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 12AWG\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 14AWG\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 16AWG\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 18AWG\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 20AWG\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 22AWG\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 24AWG\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF Tresse (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids,\"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF Brin (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Autre Section\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\",\"Section Mono\", \"Diamètre Extérieur Min\",\"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", \"Nombre de Brins\", \"Diamètre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Linéique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"Références Clients\", Obsolète, \"Dénudage Etagé\", \"Est Fil de Multi\")");

			statement.executeUpdate(
					"CREATE EXTENT OF Voyant (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Transformateur (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Thermostat (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Thermocouple (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Thermistance (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, Betta, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Telerupteur (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Switch (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Sonde (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Sécurité (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Température de Coupure\", \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Résistance (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Relais (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Régulateur (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Potentiomètre (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Minirupteur (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF MicroRupteur (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Limiteur (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Led (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF InterRupteur (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Fusible (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Filtre (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Composants Electriques divers\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Disjoncteur (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Diode (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Détecteur (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Contacteur (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Condensateur (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Capacité (farad)\", \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Circuit (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Centrale (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Capteur (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Buzzer (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Résistance, Puissance, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Bouton (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Représentation\", \"Nombre de Pôles\", \"Polarisé\", \"Liste des Ports\", Tension, Intensité, \"Lien\", \"Configuration Surcôte\", \"Références Clients\", Obsolète)");

			statement.executeUpdate(
					"CREATE EXTENT OF \"2 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"3 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"4 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"5 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"6 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"7 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"8 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"9 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"10 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"11 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"12 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"13 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"14 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"15 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"16 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"17 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"18 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"19 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"20 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"24 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"25 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"30 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"36 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"40 Conducteurs\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  Lien, \"Références Clients\", Obsolète, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Conducteurs équipés\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diamètre Extérieur Min\", \"Diamètre Extérieur Nominal\", \"Diamètre Extérieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Liés\",  \"Connecteur Gauche\", \"Connecteur Droit\", Lien, \"Références Clients\", Obsolète, \"Section Principale\")");

			statement.executeUpdate(
					"CREATE EXTENT OF \"Presse Etoupe\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, Fendu, \"Diamètre Intérieur\", Matière, Pas, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Raccord (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, Fendu, \"Diamètre Intérieur\", Matière, Pas, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Joint Plat\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, Fendu, \"Diamètre Intérieur\", Matière, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Joint Set\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, Fendu, \"Diamètre Intérieur\", Matière, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Ecrou Accessoires\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, Fendu, \"Diamètre Intérieur\", Matière, Pas, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Réducteur (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, Fendu, \"Diamètre Intérieur\", Matière, Pas, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Passe Fils\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, Fendu, \"Diamètre Intérieur\", Matière, \"Epaisseur Tôle\", Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Repère (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, Fendu, \"Diamètre Intérieur\", Matière, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Ferrite (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, Fendu, \"Diamètre Intérieur\", Matière, Lien, \"Références Clients\", Obsolète)");

			statement.executeUpdate(
					"CREATE EXTENT OF \"Autre Pas\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, \"Nombre de Voies\", \"Section Min\", \"Section Max\", \"Nombre de Contacts\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", \"Direct/Indirect\", \"Marquage\", \"Cloison Lat\", \"Cloison Cent\", \"Clé de Verrouillage\", \"Pas des Voies\", \"Liste des Ports\", Lien, Surcôtes, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Pas de 2.5\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, \"Nombre de Voies\", \"Section Min\", \"Section Max\", \"Nombre de Contacts\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", \"Direct/Indirect\", \"Marquage\", \"Cloison Lat\", \"Cloison Cent\", \"Clé de Verrouillage\", \"Pas des Voies\", \"Liste des Ports\", Lien, Surcôtes, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Pas de 3.96\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, \"Nombre de Voies\", \"Section Min\", \"Section Max\", \"Nombre de Contacts\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", \"Direct/Indirect\", \"Marquage\", \"Cloison Lat\", \"Cloison Cent\", \"Clé de Verrouillage\", \"Pas des Voies\", \"Liste des Ports\", Lien, Surcôtes, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Pas de 5\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, \"Nombre de Voies\", \"Section Min\", \"Section Max\", \"Nombre de Contacts\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", \"Direct/Indirect\", \"Marquage\", \"Cloison Lat\", \"Cloison Cent\", \"Clé de Verrouillage\", \"Pas des Voies\", \"Liste des Ports\", Lien, Surcôtes, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Rast 2.5\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, \"Nombre de Voies\", \"Section Min\", \"Section Max\", \"Nombre de Contacts\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", \"Direct/Indirect\", \"Marquage\", \"Cloison Lat\", \"Cloison Cent\", \"Clé de Verrouillage\", \"Pas des Voies\", \"Liste des Ports\", Lien, Surcôtes, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Rast 2.5 PRO\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, \"Nombre de Voies\", \"Section Min\", \"Section Max\", \"Nombre de Contacts\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", \"Direct/Indirect\", \"Marquage\", \"Cloison Lat\", \"Cloison Cent\", \"Clé de Verrouillage\", \"Pas des Voies\", \"Liste des Ports\", Lien, Surcôtes, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Rast 5\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\",\"Température Max\", Représentation, \"Nombre de Voies\", \"Section Min\", \"Section Max\", \"Nombre de Contacts\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", \"Direct/Indirect\", \"Marquage\", \"Cloison Lat\", \"Cloison Cent\", \"Clé de Verrouillage\", \"Pas des Voies\", \"Liste des Ports\", Lien, Surcôtes, \"Références Clients\", Obsolète)");

			statement.executeUpdate(
					"CREATE EXTENT OF Bac (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, Largeur, Marquage, Hauteur, \"Poids Maximum\", Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Carton (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, Largeur, Marquage, Hauteur, \"Poids Maximum\", Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Couvercle (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, Largeur, Marquage, Hauteur, \"Poids Maximum\", Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Film (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, Largeur, Marquage, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Intercalaire (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, Largeur, Marquage, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Sac (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, Largeur, Marquage, Hauteur, \"Poids Maximum\", Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF SacBulle (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, Largeur, Marquage, Hauteur, \"Poids Maximum\", Lien, \"Références Clients\", Obsolète)");

			statement.executeUpdate(
					"CREATE EXTENT OF GAR (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, \"Diamètre Intérieur\", Fendue, \"Lg Conditionnement\", \"Diamètre Extérieur\", Matière, Profil, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF GANF (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, \"Diamètre Intérieur\", Fendue, \"Lg Conditionnement\",\"Diamètre Extérieur\", Matière, Profil, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF GAF (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, \"Diamètre Intérieur\", Fendue, \"Lg Conditionnement\",\"Diamètre Extérieur\", Matière, Profil, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF GAFL (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, \"Diamètre Intérieur\", Fendue, \"Lg Conditionnement\",\"Diamètre Extérieur\", Matière, Profil, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Zipper (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, \"Diamètre Intérieur\", Fendue, \"Lg Conditionnement\",\"Diamètre Extérieur\", Matière, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Tressée (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, \"Diamètre Intérieur\", Fendue, \"Lg Conditionnement\",\"Diamètre Extérieur\", Matière, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Spiralée (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, \"Diamètre Intérieur\", Fendue, \"Lg Conditionnement\",\"Diamètre Extérieur\", Matière, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Lisse (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, \"Diamètre Intérieur\", Fendue, \"Lg Conditionnement\",\"Diamètre Extérieur\", Matière, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Gaines Thermos\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, \"Diamètre Intérieur\", Fendue, \"Lg Conditionnement\",\"Diamètre Extérieur\", Matière, \"Diamètre Après Rétreint\", Lien, \"Références Clients\", Obsolète, Collante, Marquage)");
			statement.executeUpdate(
					"CREATE EXTENT OF Feutrine (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, \"Diamètre Intérieur\", Fendue, \"Lg Conditionnement\",\"Diamètre Extérieur\", Matière, Lien, \"Références Clients\", Obsolète)");

			statement.executeUpdate(
					"CREATE EXTENT OF Collier (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Matière, Marquage, Largeur, Longueur, \"Diamètre Toron Maxi\", \"Diamètre Toron Mini\", Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Collier Agrafe\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Matière, Marquage, Largeur, Longueur, \"Diamètre Fixation\", \"Forme Agrafe\", \"Diamètre Toron Maxi\", \"Diamètre Toron Mini\", Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Etiquette (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Matière, Marquage, Largeur, Longueur, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Pastille (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Matière, Marquage, Diamètre, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Pion Fixation\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Matière, Marquage, Largeur, Longueur, \"Diamètre Fixation\", \"Forme Agrafe\", Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Ruban Adhésif\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Matière, Marquage, Largeur, Lien, \"Références Clients\", Obsolète)");

			statement.executeUpdate(
					"CREATE EXTENT OF Y (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Matière, Fixation, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Réducteur TYFermeture\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Matière, Fixation, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF M (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Matière, Fixation, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF T (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Matière, Fixation, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF A (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Matière, Fixation, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Fermeture (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, Matière, Fixation, Lien, \"Références Clients\", Obsolète)");

			statement.executeUpdate(
					"CREATE EXTENT OF \"Basse Pression\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, \"Matière à Surmouler\", \"Volume Matière\", Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Haute Pression\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Température Min\", \"Température Max\", Représentation, \"Matière à Surmouler\", \"Volume Matière\", Lien, \"Références Clients\", Obsolète)");

			statement.executeUpdate(
					"CREATE EXTENT OF Ecrou (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Représentation, Normes, \"Température Min\", \"Température Max\",Poids, \"Diamètre Intérieur\", \"Diamètre Extérieur\", Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Goujon (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Représentation, Normes, \"Température Min\", \"Température Max\",Poids, \"Diamètre Extérieur\", Longueur, Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Goupille (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Représentation, Normes, \"Température Min\", \"Température Max\",Poids, \"Diamètre Intérieur\", Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Rondelle (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Représentation, Normes, \"Température Min\", \"Température Max\",Poids, \"Diamètre Intérieur\", \"Diamètre Extérieur\", Lien, \"Références Clients\", Obsolète)");
			statement.executeUpdate(
					"CREATE EXTENT OF Vis (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\", \"Fiche Technique\", Représentation, Normes, \"Température Min\", \"Température Max\",Poids, \"Diamètre Extérieur\", Longueur, Lien, \"Références Clients\", Obsolète)");

			statement.executeUpdate("CREATE EXTENT OF \"Liste Configuration TY\" (\"Référence BE\",Diamètre, Profil)");

			t1.snapshot("CREATE EXTENT");
		}

		List<String> classNames = Arrays.asList("Liste Sortie TY");
		System.out.println("Generating instance for " + classNames.size() + " classes...");
		InsertStatementGenerator generator = new InsertStatementGenerator(getSession());

		// Uncomment these lines if you want to extract generated statement into a file
		// PrintWriter writer = null;
		// try {
		// writer = new PrintWriter("insert-fixtures.mql", "UTF-8");
		// generator.setOuputWriter(writer);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }

		Timer t2 = new Timer();

		generator.setCurrentModelClass("\"Fermeture\"");
		String mql1 = generator.generate();

		generator.setCurrentModelClass("\"Liste Sortie TY\"");
		String mql2 = generator.generate();

		t2.start();
		// Generate 1000 instances per class.
		for (int i = 0; i < 1000; i++) {
			statement.executeUpdate(mql1);
		}
		t2.snapshot("INSERT INTO \"Fermeture\"");

		// Generate 1000 instances per class.
		for (int i = 0; i < 1000; i++) {
			statement.executeUpdate(mql2);
		}

		t2.snapshot("INSERT INTO \"Liste Sortie TY\"");

		// Add ref value.
		MariusQLResultSet rs = statement.executeQuery("SELECT rid FROM \"Fermeture\"");
		rs.next();
		Long rid = rs.getLong(1);
		statement.executeUpdate("UPDATE \"Liste Sortie TY\" SET \"Référence BE du TY\" = " + rid);

		t1.start();
		// Generate 1000 select per class.
		for (int i = 0; i < 1000; i++) {
			statement.executeUpdate(
					"select * from \"Liste Sortie TY\" ty, \"Composant CFCA\" f where ty.\"Référence BE du TY\" = f.rid");
		}
		t1.snapshot("SELECT \"Liste Sortie TY\" JOIN \"Fermeture\"");

		// if (writer != null) {
		// writer.close();
		// }

		session.rollback();
		statement.close();
	}
}
