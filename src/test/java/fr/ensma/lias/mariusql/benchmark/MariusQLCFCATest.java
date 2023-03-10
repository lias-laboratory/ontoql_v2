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
				"ALTER #CLASS \"Composant CFCA\" ADD \"Fiche Technique\" URIType DESCRIPTOR (#code = '0002-41982799300025#02-1#1', #definition[fr] = 'R??f??rence externe ?? une description technique du composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"R??f??rence ERP\" String DESCRIPTOR (#code = '0002-41982799300025#02-2#1', #definition[fr] = 'R??f??rence cr????e dans l''ERP')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"D??signation\" String DESCRIPTOR (#code = '0002-41982799300025#02-3#1', #definition[fr] = 'D??signation')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Fabricant\" String DESCRIPTOR (#code = '0002-41982799300025#02-4#1',#definition[fr] = 'Fabricant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"R??f??rence Fabricant\" String DESCRIPTOR (#code = '0002-41982799300025#02-5#1',#definition[fr] = 'R??f??rence fabricant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Repr??sentation\" URIType DESCRIPTOR (#code = '0002-41982799300025#02-6#1',#definition[fr] = 'Repr??sentation du composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Normes\" String ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-8#1', #definition[fr] = 'Liste des normes auxquelles r??pond le composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Lien\" Ref(\"Composant CFCA\") ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-213#1', #definition[fr] = 'Composant fortement li?? ?? celui d??crit')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"R??f??rences Clients\" String ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-220#1', #definition[fr] = 'Identifiants pr??sents chez les clients')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Obsol??te\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-233#1', #definition[fr] = 'Indique que le composant ne doit plus ??tre utilis??')");

		// Accessoires
		statement.executeUpdate(
				"CREATE #CLASS \"Accessoires\" UNDER \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-2#1'))");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires\" ADD \"Poids\" Real DESCRIPTOR (#code = '0002-41982799300025#02-10#1', #definition[fr] = 'Poids d''une unit?? de composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires\" ADD \"Temp??rature Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-12#1', #definition[fr] = 'Temp??rature minimum support??e par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires\" ADD \"Temp??rature Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-13#1', #definition[fr] = 'Temp??rature maximum support??e par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires\" ADD \"Diam??tre Ext??rieur\" Real DESCRIPTOR (#code = '0002-41982799300025#02-14#1', #definition[fr] = 'Diam??tre ext??rieur obturateur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires\" ADD \"Longueur Obturateur\" Real DESCRIPTOR (#code = '0002-41982799300025#02-15#1', #definition[fr] = 'Longueur d''un obturateur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires\" ADD \"Diam??tre Mini Isolant\" Real DESCRIPTOR (#code = '0002-41982799300025#02-16#1', #definition[fr] = 'Diam??tre minimum de l''isolant du composant li??')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires\" ADD \"Diam??tre Maxi Isolant\" Real DESCRIPTOR (#code = '0002-41982799300025#02-17#1', #definition[fr] = 'Diam??tre maximum de l''isolant du composant li??')");

		statement.executeUpdate(
				"CREATE #CLASS \"Joint Sur Fil\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-3#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Bouchon\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-4#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Cale&Verrou\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-5#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Capot\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-6#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"D??trompeur\" UNDER \"Accessoires\" (DESCRIPTOR (#code = '0002-41982799300025#01-7#1'))");
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

		// Surc??te
		statement.executeUpdate(
				"CREATE #CLASS \"Surc??te\" UNDER Divers (DESCRIPTOR (#code = '0002-41982799300025#01-240#1'))");

		// Ports
		statement.executeUpdate(
				"CREATE #CLASS \"Ports\" UNDER Divers (DESCRIPTOR (#code = '0002-41982799300025#01-13#1'))");
		statement.executeUpdate(
				"ALTER #CLASS \"Ports\" ADD \"R??f??rence ERP\" String DESCRIPTOR (#code = '0002-41982799300025#02-19#1', #definition[fr] = 'R??f??rence cr????e dans l''ERP')");
		statement.executeUpdate(
				"ALTER #CLASS \"Ports\" ADD \"Nom\" String DESCRIPTOR (#code = '0002-41982799300025#02-20#1', #definition[fr] = 'Nom du port du connecteur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Ports\" ADD \"Famille Cosse\" String ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-21#1', #definition[fr] = 'Famille de cosse eligible')");
		statement.executeUpdate(
				"ALTER #CLASS \"Ports\" ADD \"Obturateur\" Ref(Obturateur) ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-22#1', #definition[fr] = 'Indique les obturateurs')");
		statement.executeUpdate(
				"ALTER #CLASS \"Ports\" ADD \"Longueur D??nudage\" Real DESCRIPTOR (#code = '0002-41982799300025#02-23#1', #definition[fr] = 'Longueur de d??nudage si aucune cosse')");
		statement.executeUpdate(
				"ALTER #CLASS \"Ports\" ADD Surc??tes Ref(Surc??te) ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-230#1', #definition[fr] = 'Pr??cise le(s) surc??te(s) possible(s)')");

		statement.executeUpdate(
				"CREATE #CLASS \"Ports Connecteur\" UNDER Ports (DESCRIPTOR (#code = '0002-41982799300025#01-14#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Ports DPI\" UNDER Ports (DESCRIPTOR (#code = '0002-41982799300025#01-15#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Ports Composant Elec\" UNDER Ports (DESCRIPTOR (#code = '0002-41982799300025#01-16#1'))");

		// Surc??te Suite
		statement.executeUpdate(
				"ALTER #CLASS Surc??te ADD \"R??f??rence BE du Port\" Ref(Ports) DESCRIPTOR (#code = '0002-41982799300025#02-231#1', #definition[fr] = 'Identifiant du port associ??')");
		statement.executeUpdate(
				"ALTER #CLASS Surc??te ADD Orientation String DESCRIPTOR (#code = '0002-41982799300025#02-228#1', #definition[fr] = 'D??signation de l''orientation')");
		statement.executeUpdate(
				"ALTER #CLASS Surc??te ADD Valeur Real DESCRIPTOR (#code = '0002-41982799300025#02-229#1', #definition[fr] = 'Valeur de la surc??te du port')");

		// Connexions
		statement.executeUpdate(
				"CREATE #CLASS Connexions UNDER \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-17#1'))");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-25#1', #definition[fr] = 'Poids d''une unit?? de composant')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Temp??rature Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-27#1', #definition[fr] = 'Temp??rature minimum support??e par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Temp??rature Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-28#1', #definition[fr] = 'Temp??rature maximum support??e par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Famille String DESCRIPTOR (#code = '0002-41982799300025#02-29#1', #definition[fr] = 'Permet de conna??tre les cosses admissibles')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Section Min\" Real DESCRIPTOR (#code = '0002-41982799300025#02-31#1', #definition[fr] = 'Section minimale acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Section Max\" Real DESCRIPTOR (#code = '0002-41982799300025#02-32#1', #definition[fr] = 'Section maximale acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Lg Denudage\" Real DESCRIPTOR (#code = '0002-41982799300025#02-33#1', #definition[fr] = 'Longueur de d??nudage de la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Mati??re Cosse\" String DESCRIPTOR (#code = '0002-41982799300025#02-35#1', #definition[fr] = 'Mati??re de la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Finition String DESCRIPTOR (#code = '0002-41982799300025#02-36#1', #definition[fr] = 'Mati??re du plaquage')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Amp??rage maximum 20??\" Real DESCRIPTOR (#code = '0002-41982799300025#02-37#1', #definition[fr] = 'Amp??rage support?? par la cosse ?? 20??C')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Diam??tre Mini Isolant\" Real DESCRIPTOR (#code = '0002-41982799300025#02-38#1', #definition[fr] = 'Diam??tre minimum de l''isolant du composant li??')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Diam??tre Maxi Isolant\" Real DESCRIPTOR (#code = '0002-41982799300025#02-39#1', #definition[fr] = 'Diam??tre maximum de l''isolant du composant li??')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Coud??e Int DESCRIPTOR (#code = '0002-41982799300025#02-41#1', #definition[fr] = 'Pr??cise l''angle de coudage')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Pr??co Sertissage\" URIType DESCRIPTOR (#code = '0002-41982799300025#02-42#1', #definition[fr] = 'Pr??conisation sertissage')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Joint Sur Fil\" REF(\"Joint Sur Fil\") ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-43#1', #definition[fr] = 'Liste des joints sur fil acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Pour Largeur\" Real DESCRIPTOR (#code = '0002-41982799300025#02-44#1', #definition[fr] = 'Indique quelle largeur doit faire la cosse inverse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Pour Hauteur\" Real DESCRIPTOR (#code = '0002-41982799300025#02-45#1', #definition[fr] = 'Indique quelle hauteur doit faire la cosse inverse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Pour Diam??tre\" Real DESCRIPTOR (#code = '0002-41982799300025#02-46#1', #definition[fr] = 'Indique quelle diam??tre doit faire la cosse inverse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Accrochage Connecteur\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-47#1', #definition[fr] = 'Indique si la cosse poss??de un ergot pour une fixation dans un connecteur')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Verrouillage Boolean DESCRIPTOR (#code = '0002-41982799300025#02-48#1', #definition[fr] = 'Syst??me de verrouillage sur la cosse inverse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD D??v??rouillage Boolean DESCRIPTOR (#code = '0002-41982799300025#02-49#1', #definition[fr] = 'Syst??me de d??v??rouillage avec la cosse inverse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Largeur Real DESCRIPTOR (#code = '0002-41982799300025#02-50#1', #definition[fr] = 'Largeur de la partie active')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Hauteur Real DESCRIPTOR (#code = '0002-41982799300025#02-51#1', #definition[fr] = 'Hauteur de la partie active')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Diam??tre Real DESCRIPTOR (#code = '0002-41982799300025#02-52#1', #definition[fr] = 'Diam??tre de la partie active')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Pour Trou Diam??tre\" Real DESCRIPTOR (#code = '0002-41982799300025#02-53#1', #definition[fr] = 'Indique pour quelle largeur de trou est pr??vue la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Pour Epaisseur PCB\" Real DESCRIPTOR (#code = '0002-41982799300025#02-54#1', #definition[fr] = 'Indique pour quelle largeur de Pcb est pr??vue la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Epissure Boolean DESCRIPTOR (#code = '0002-41982799300025#02-56#1', #definition[fr] = 'Indique si le composant peut servir de base ?? une ??pissure')");

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
				"CREATE #CLASS \"M??le\" UNDER Connexions (DESCRIPTOR (#code = '0002-41982799300025#01-24#1'))");
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
				"ALTER #CLASS Connecteurs ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-57#1', #definition[fr] = 'Poids d''une unit?? de composant')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Temp??rature Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-59#1', #definition[fr] = 'Temp??rature minimum support??e par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Temp??rature Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-60#1', #definition[fr] = 'Temp??rature maximum support??e par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD Famille String DESCRIPTOR (#code = '0002-41982799300025#02-61#1', #definition[fr] = 'Appelation commerciale du connecteur')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Nombre de Voies\" Int DESCRIPTOR (#code = '0002-41982799300025#02-62#1', #definition[fr] = 'Nombre de voies')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Sert Apr??s\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-64#1', #definition[fr] = 'Connecteur n??cessitant un sertissage apr??s enfilage')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD Etanche boolean DESCRIPTOR (#code = '0002-41982799300025#02-66#1', #definition[fr] = 'Pr??cise que le connecteur peut ??tre rendu ??tanche ?? l''aide d''accessoire')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Liste des Ports\" Ref(Ports) ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-67#1', #definition[fr] = 'Description de chaque voie')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Pas des Voies\" Real DESCRIPTOR (#code = '0002-41982799300025#02-68#1', #definition[fr] = 'Distance en mm entre 2 Voies')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Nbre Rang??e\" Int DESCRIPTOR (#code = '0002-41982799300025#02-69#1', #definition[fr] = 'Nombre de rang??e sur lesquelles sont r??parties les voies')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Connecteur Inverse\" Ref(Connecteurs) DESCRIPTOR (#code = '0002-41982799300025#02-70#1', #definition[fr] = 'Indique l''identifiant BE du connecteur inverse')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Second verrouillage\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-71#1', #definition[fr] = 'Pr??sence d''un second verrouillage')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Mati??re Connecteur\" String DESCRIPTOR (#code = '0002-41982799300025#02-72#1', #definition[fr] = 'Mati??re du Connecteur')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD Blind?? Boolean DESCRIPTOR (#code = '0002-41982799300025#02-73#1', #definition[fr] = 'Connecteur blind??')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD Fixation Boolean DESCRIPTOR (#code = '0002-41982799300025#02-74#1', #definition[fr] = 'Composant poss??dant un support pour une fixation')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Diam??tre Mini Isolant\" Real DESCRIPTOR (#code = '0002-41982799300025#02-75#1', #definition[fr] = 'Diam??tre minimum de l''isolant du composant li??')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Section Min\" Real DESCRIPTOR (#code = '0002-41982799300025#02-76#1', #definition[fr] = 'Section minimale acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Section Max\" Real DESCRIPTOR (#code = '0002-41982799300025#02-77#1', #definition[fr] = 'Section maximale acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Intensite Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-78#1', #definition[fr] = 'Intensit?? maximale que peut accepter le connecteur')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Tension Admisible\" Int DESCRIPTOR (#code = '0002-41982799300025#02-79#1', #definition[fr] = 'Tension admissible en volt pour un connecteur')");
		statement.executeUpdate(
				"ALTER #CLASS Connecteurs ADD \"Orientation Surc??te\" String ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-224#1', #definition[fr] = 'Liste le nombre de surc??te possible')");

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
				"ALTER #CLASS MonoConducteur ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-80#1', #definition[fr] = 'Poids d''une unit?? de composant')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Temp??rature Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-82#1', #definition[fr] = 'Temp??rature minimum support??e par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Temp??rature Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-83#1', #definition[fr] = 'Temp??rature maximum support??e par le composant')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Type Conducteur\" String DESCRIPTOR (#code = '0002-41982799300025#02-84#1', #definition[fr] = 'Famille du conducteur')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Section Mono\" Real DESCRIPTOR (#code = '0002-41982799300025#02-85#1', #definition[fr] = 'Section du monoConducteur (mm??)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Diam??tre Ext??rieur Min\" Real DESCRIPTOR (#code = '0002-41982799300025#02-86#1',#definition[fr] = 'Diam??tre ext??rieur nominal - Tol??rance de la gaine (mm)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Diam??tre Ext??rieur Nominal\" Real DESCRIPTOR (#code = '0002-41982799300025#02-87#1',#definition[fr] = 'Diam??tre ext??rieur nominal (mm)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Diam??tre Ext??rieur Max\" Real DESCRIPTOR (#code = '0002-41982799300025#02-88#1',#definition[fr] = 'Diam??tre ext??rieur nominal + Tol??rance de la gaine (mm)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Nombre de Brins\" Int DESCRIPTOR (#code = '0002-41982799300025#02-89#1', #definition[fr] = 'Nombre de brins composant le conducteur')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Diam??tre des Brins\" Real DESCRIPTOR (#code = '0002-41982799300025#02-90#1', #definition[fr] = 'Diam??tre des brins composant le conducteur (mm)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Nature du Conducteur\" String DESCRIPTOR (#code = '0002-41982799300025#02-91#1', #definition[fr] = 'Mat??riau conducteur')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Poids Cuivre\" Real DESCRIPTOR (#code = '0002-41982799300025#02-92#1', #definition[fr] = 'Poids du cuivre dans un m??tre de conducteurs (Kg/m)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Film Papier/Plastique\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-93#1',#definition[fr] = 'Pr??sence d''un film sous la gaine exterieur')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Lg Conditionnement\" Real DESCRIPTOR (#code = '0002-41982799300025#02-232#1',#definition[fr] = 'Longueur maximum exploitable')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Nature Isolant\" String DESCRIPTOR (#code = '0002-41982799300025#02-95#1', #definition[fr] = 'Mati??re de la gaine ext??rieure')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Resistance Lin??ique\" Real DESCRIPTOR (#code = '0002-41982799300025#02-96#1', #definition[fr] = 'R??sistance lin??ique du conducteur (Ohm/Km)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Tension Phase/Phase\" Real DESCRIPTOR (#code = '0002-41982799300025#02-97#1', #definition[fr] = 'Tension entre les phases (V)')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Double Isolation\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-98#1', #definition[fr] = 'Pr??sence de deux gaines autour du conducteur')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"D??nudage Etag??\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-215#1',#definition[fr] = 'Indique la n??cessit?? d''utiliser une boite rotative pour le d??gainage en automatique')");
		statement.executeUpdate(
				"ALTER #CLASS MonoConducteur ADD \"Est Fil de Multi\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-234#1',#definition[fr] = 'Indique si le monoconducteur appartient ?? un conducteur complexe')");

		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.22 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-77#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.25 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-78#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.34 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-79#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.38 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-236#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.5 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-80#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.6 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-81#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.7 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-82#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 0.75 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-83#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 1 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-84#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 1.34 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-85#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 1.4 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-86#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 1.5 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-87#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 2 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-88#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 2.5 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-89#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 3 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-90#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 4 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-91#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 5 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-92#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 6 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-93#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 7 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-94#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 10 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-95#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 16 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-96#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 20 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-97#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 25 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-98#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 35 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-99#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 40 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-100#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 50 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-101#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 60 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-102#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 70 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-103#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 75 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-104#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 95 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-105#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"MonoConducteur Section 120 mm??\" UNDER MonoConducteur (DESCRIPTOR (#code = '0002-41982799300025#01-106#1'))");
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

		// Composants ??lectriques
		statement.executeUpdate(
				"CREATE #CLASS \"Composants Electriques\" UNDER \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-118#1'))");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-99#1', #definition[fr] = 'Poids d''une unit?? de composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD \"Temp??rature Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-101#1', #definition[fr] = 'Temp??rateur Minimum')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD \"Temp??rature Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-102#1', #definition[fr] = 'Temp??rateur Maximum')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD \"Nombre de P??les\" Int DESCRIPTOR (#code = '0002-41982799300025#02-103#1', #definition[fr] = 'Nombre de p??le du composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD Polaris?? Boolean DESCRIPTOR (#code = '0002-41982799300025#02-104#1', #definition[fr] = 'Echange possible entre les p??les')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD \"Liste des Ports\" Ref(Ports) ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-106#1', #definition[fr] = 'Description de chaque voie')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD Tension Real DESCRIPTOR (#code = '0002-41982799300025#02-107#1', #definition[fr] = 'Tension d''utilisation')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD R??sistance Real DESCRIPTOR (#code = '0002-41982799300025#02-108#1', #definition[fr] = 'R??sistance Nominale')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD Puissance Real DESCRIPTOR (#code = '0002-41982799300025#02-109#1', #definition[fr] = 'Puissance consomm??e')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD Intensit?? Real DESCRIPTOR (#code = '0002-41982799300025#02-110#1', #definition[fr] = 'Amp??rage du composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD \"Capacit?? (farad)\" Real DESCRIPTOR (#code = '0002-41982799300025#02-111#1', #definition[fr] = 'Capacit?? du condensateur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD Betta Real DESCRIPTOR (#code = '0002-41982799300025#02-112#1', #definition[fr] = 'Mesure absolue de la temp??rature en Kelvin')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD \"Temp??rature de Coupure\" Int DESCRIPTOR (#code = '0002-41982799300025#02-113#1', #definition[fr] = 'Temp??rature ?? laquelle la s??curit?? se coupe')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composants Electriques\" ADD \"Configuration Surc??te\" String ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-225#1', #definition[fr] = 'Liste le nombre de surc??te possible')");

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
				"CREATE #CLASS S??curit?? UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-127#1'))");
		statement.executeUpdate(
				"CREATE #CLASS R??sistance UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-128#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Relais UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-129#1'))");
		statement.executeUpdate(
				"CREATE #CLASS R??gulateur UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-130#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Potentiom??tre UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-131#1'))");
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
				"CREATE #CLASS D??tecteur UNDER \"Composants Electriques\" (DESCRIPTOR (#code = '0002-41982799300025#01-143#1'))");
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
				"ALTER #CLASS \"Conducteurs Complexes\" ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-114#1', #definition[fr] = 'Poids d''une unit?? de composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Temp??rature Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-116#1', #definition[fr] = 'Temp??rature minimum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Temp??rature Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-117#1', #definition[fr] = 'Temp??rature maximum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Type Conducteur\" String DESCRIPTOR (#code = '0002-41982799300025#02-118#1', #definition[fr] = 'Type pour MultiConducteurs')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Nombre Conducteur\" Int DESCRIPTOR (#code = '0002-41982799300025#02-119#1',#definition[fr] = 'Nombre de conducteur sous la gaine hors brin et tresse')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Poids Cuivre\" Real DESCRIPTOR (#code = '0002-41982799300025#02-120#1', #definition[fr] = 'Section en mm2 du conducteur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Diam??tre Ext??rieur Min\" Real DESCRIPTOR (#code = '0002-41982799300025#02-121#1',#definition[fr] = 'Diam??tre ext??rieur nominal - Tol??rance de la gaine (mm)')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Diam??tre Ext??rieur Nominal\" Real DESCRIPTOR (#code = '0002-41982799300025#02-122#1',#definition[fr] = 'Diam??tre ext??rieur nominal (mm)')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Diam??tre Ext??rieur Max\" Real DESCRIPTOR (#code = '0002-41982799300025#02-123#1',#definition[fr] = 'Diam??tre ext??rieur nominal + Tol??rance de la gaine (mm)')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD Tresse Boolean DESCRIPTOR (#code = '0002-41982799300025#02-124#1',#definition[fr] = 'Pr??sence d''une tresse dans le multiConducteur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Brin de Masse\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-125#1',#definition[fr] = 'Pr??sence d''un brin de masse')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Film Papier/Plastique\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-126#1',#definition[fr] = 'Pr??sence d''un film sous la gaine exterieur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Lg Conditionnement\" Real DESCRIPTOR (#code = '0002-41982799300025#02-128#1',#definition[fr] = 'Longueur maximum exploitable')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Nature Isolant\" String DESCRIPTOR (#code = '0002-41982799300025#02-129#1', #definition[fr] = 'Mati??re de la gaine ext??rieure')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Tension Phase/Neutre\" Real DESCRIPTOR (#code = '0002-41982799300025#02-130#1', #definition[fr] = 'Mati??re de la gaine ext??rieure')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Tension Phase/Phase\" Real DESCRIPTOR (#code = '0002-41982799300025#02-131#1', #definition[fr] = 'Tension entre deux phases')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Fil Vert/Jaune\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-132#1',#definition[fr] = 'Pr??sence d''un fil Vert/Jaune parmi les conducteurs')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"MonoConducteurs Li??s\" Ref(MonoConducteur) ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-133#1',#definition[fr] = 'Identifiant des conducteurs li??s')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Connecteur Gauche\" Ref(Connecteurs) DESCRIPTOR (#code = '0002-41982799300025#02-134#1',#definition[fr] = 'Lien vers le connecteur ??quip??')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Connecteur Droit\" Ref(Connecteurs) DESCRIPTOR (#code = '0002-41982799300025#02-135#1',#definition[fr] = 'Lien vers le connecteur ??quip??')");
		statement.executeUpdate(
				"ALTER #CLASS \"Conducteurs Complexes\" ADD \"Section Principale\" Real DESCRIPTOR (#code = '0002-41982799300025#02-236#1',#definition[fr] = 'Section retrouv??e majoritairement dans le multiconducteur')");

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
				"CREATE #CLASS \"Conducteurs ??quip??s\" UNDER \"Conducteurs Complexes\" (DESCRIPTOR (#code = '0002-41982799300025#01-176#1'))");

		// Accessoires Habillages
		statement.executeUpdate(
				"CREATE #CLASS \"Accessoires Habillages\" under \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-177#1')))");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-136#1', #definition[fr] = 'Poids d''une unit?? de composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD \"Temp??rature Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-138#1', #definition[fr] = 'Temp??rature minimum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD \"Temp??rature Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-139#1', #definition[fr] = 'Temp??rature maximum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD Fendu Boolean DESCRIPTOR (#code = '0002-41982799300025#02-212#1', #definition[fr] = 'Indique si l''accesoire habillage est fendu ou pas')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD \"Diam??tre Int??rieur\" REAL DESCRIPTOR (#code = '0002-41982799300025#02-140#1',#definition[fr] = 'Diam??tre int??rieur')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD Mati??re String DESCRIPTOR (#code = '0002-41982799300025#02-141#1',#definition[fr] = 'Mati??re de la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD Pas String DESCRIPTOR (#code = '0002-41982799300025#02-142#1',#definition[fr] = 'Pas exprim?? en PG ou M des composants')");
		statement.executeUpdate(
				"ALTER #CLASS \"Accessoires Habillages\" ADD \"Epaisseur T??le\" Real DESCRIPTOR (#code = '0002-41982799300025#02-143#1',#definition[fr] = 'Epaisseur de la t??le o?? peut ??tre mis le passe fil')");

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
				"CREATE #CLASS R??ducteur UNDER \"Accessoires Habillages\" (DESCRIPTOR (#code = '0002-41982799300025#01-183#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Passe Fils\" UNDER \"Accessoires Habillages\" (DESCRIPTOR (#code = '0002-41982799300025#01-184#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Rep??re UNDER \"Accessoires Habillages\" (DESCRIPTOR (#code = '0002-41982799300025#01-243#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Ferrite UNDER \"Accessoires Habillages\" (DESCRIPTOR (#code = '0002-41982799300025#01-139#1'))");

		// DPI
		statement.executeUpdate(
				"CREATE #CLASS DPI under \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-185#1')))");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-144#1', #definition[fr] = 'Poids d''une unit?? de composant')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Temp??rature Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-146#1', #definition[fr] = 'Temp??rature minimum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Temp??rature Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-147#1', #definition[fr] = 'Temp??rature maximum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Nombre de Voies\" Int DESCRIPTOR (#code = '0002-41982799300025#02-148#1', #definition[fr] = 'Nombre de voies')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Section Min\" Real DESCRIPTOR (#code = '0002-41982799300025#02-149#1', #definition[fr] = 'Section minimale acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Section Max\" Real DESCRIPTOR (#code = '0002-41982799300025#02-150#1', #definition[fr] = 'Section maximale acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Nombre de Contacts\" Int DESCRIPTOR (#code = '0002-41982799300025#02-151#1', #definition[fr] = 'Section maximale acceptable')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Diam??tre Mini Isolant\" Real DESCRIPTOR (#code = '0002-41982799300025#02-152#1', #definition[fr] = 'Diam??tre minimum de l''isolant du composant li??')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Diam??tre Maxi Isolant\" Real DESCRIPTOR (#code = '0002-41982799300025#02-153#1', #definition[fr] = 'Diam??tre maximum de l''isolant du composant li??')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Direct/Indirect\" STRING DESCRIPTOR (#code = '0002-41982799300025#02-154#1', #definition[fr] = 'Indique si le dpi est pr??vu pour un branchement direct sur un PCB')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD Marquage STRING DESCRIPTOR (#code = '0002-41982799300025#02-155#1', #definition[fr] = 'Marquage du composant')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Cloison Lat\" Int DESCRIPTOR (#code = '0002-41982799300025#02-156#1', #definition[fr] = 'Nombre de cloison lat??rale')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Cloison Cent\" Int DESCRIPTOR (#code = '0002-41982799300025#02-157#1', #definition[fr] = 'Nombre de cloison centrale')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Cl?? de Verrouillage\" Int DESCRIPTOR (#code = '0002-41982799300025#02-158#1', #definition[fr] = 'Nombre de cl?? de verrouillage')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Pas des Voies\" Real DESCRIPTOR (#code = '0002-41982799300025#02-159#1', #definition[fr] = 'Distance en mm entre 2 Voies')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD \"Liste des Ports\" Ref(Ports) ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-160#1', #definition[fr] = 'Description de chaque voie')");
		statement.executeUpdate(
				"ALTER #CLASS DPI ADD Surc??tes Ref(Surc??te) ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-214#1', #definition[fr] = 'Pr??cise le(s) surc??te(s) possible(s)')");

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
				"ALTER #CLASS Emballage ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-161#1', #definition[fr] = 'Poids d''une unit?? de composant')");
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
				"ALTER #CLASS Habillage ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-167#1', #definition[fr] = 'Poids d''une unit?? de composant')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD \"Temp??rature Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-169#1', #definition[fr] = 'Temp??rature minimum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD \"Temp??rature Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-170#1', #definition[fr] = 'Temp??rature maximum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD \"Diam??tre Int??rieur\" REAL DESCRIPTOR (#code = '0002-41982799300025#02-171#1',#definition[fr] = 'Diam??tre int??rieur')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD Fendue BOOLEAN DESCRIPTOR (#code = '0002-41982799300025#02-172#1',#definition[fr] = 'Indique si la gaine est fendue ou non')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD \"Lg Conditionnement\" REAL DESCRIPTOR (#code = '0002-41982799300025#02-210#1', #definition[fr] = 'Longueur maximum exploitable en mm')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD \"Diam??tre Ext??rieur\" Real DESCRIPTOR (#code = '0002-41982799300025#02-174#1', #definition[fr] = 'Diam??tre ext??rieur de la gaine')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD Mati??re String DESCRIPTOR (#code = '0002-41982799300025#02-175#1', #definition[fr] = 'Mati??re de la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD Profil String DESCRIPTOR (#code = '0002-41982799300025#02-176#1', #definition[fr] = 'Profil d''une gaine annel??e')");
		statement.executeUpdate(
				"ALTER #CLASS Habillage ADD \"Diam??tre Apr??s R??treint\" REAL DESCRIPTOR (#code = '0002-41982799300025#02-177#1', #definition[fr] = 'Diam??tre apr??s r??treint de la gaine thermo')");
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
				"CREATE #CLASS Tress??e UNDER Habillage (DESCRIPTOR (#code = '0002-41982799300025#01-207#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Spiral??e UNDER Habillage (DESCRIPTOR (#code = '0002-41982799300025#01-208#1'))");
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
				"ALTER #CLASS RubansColliers ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-178#1', #definition[fr] = 'Poids d''une unit?? de composant')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD \"Temp??rature Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-180#1', #definition[fr] = 'Temp??rature minimum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD \"Temp??rature Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-181#1', #definition[fr] = 'Temp??rature maximum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD Mati??re String DESCRIPTOR (#code = '0002-41982799300025#02-182#1', #definition[fr] = 'Mati??re de la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD Marquage String DESCRIPTOR (#code = '0002-41982799300025#02-183#1', #definition[fr] = 'Marquage du composant')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD Largeur Real DESCRIPTOR (#code = '0002-41982799300025#02-184#1', #definition[fr] = 'Largeur du support')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD Longueur Real DESCRIPTOR (#code = '0002-41982799300025#02-185#1', #definition[fr] = 'Longueur du support')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD \"Diam??tre Fixation\" Real DESCRIPTOR (#code = '0002-41982799300025#02-186#1', #definition[fr] = 'Diam??tre fixation')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD Diam??tre Real DESCRIPTOR (#code = '0002-41982799300025#02-187#1', #definition[fr] = 'Diam??tre de la pastille')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD \"Forme Agrafe\" String DESCRIPTOR (#code = '0002-41982799300025#02-188#1', #definition[fr] = 'Forme de l''agraphe')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD \"Diam??tre Toron Maxi\" Real DESCRIPTOR (#code = '0002-41982799300025#02-189#1', #definition[fr] = 'Diam??tre maximal du toron ?? fixer')");
		statement.executeUpdate(
				"ALTER #CLASS RubansColliers ADD \"Diam??tre Toron Mini\" Real DESCRIPTOR (#code = '0002-41982799300025#02-190#1', #definition[fr] = 'Diam??tre minimal du toron ?? fixer')");

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
				"CREATE #CLASS \"Ruban Adh??sif\" UNDER RubansColliers (DESCRIPTOR (#code = '0002-41982799300025#01-218#1'))");

		// Surmoulage
		statement.executeUpdate(
				"CREATE #CLASS Surmoulage under \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-219#1')))");
		statement.executeUpdate(
				"ALTER #CLASS Surmoulage ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-191#1', #definition[fr] = 'Poids d''une unit?? de composant')");
		statement.executeUpdate(
				"ALTER #CLASS Surmoulage ADD \"Temp??rature Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-193#1', #definition[fr] = 'Temp??rature minimum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS Surmoulage ADD \"Temp??rature Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-194#1', #definition[fr] = 'Temp??rature maximum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS Surmoulage ADD \"Mati??re ?? Surmouler\" String DESCRIPTOR (#code = '0002-41982799300025#02-195#1', #definition[fr] = 'Pointe sur la mati??re utilis??e')");
		statement.executeUpdate(
				"ALTER #CLASS Surmoulage ADD \"Volume Mati??re\" Real DESCRIPTOR (#code = '0002-41982799300025#02-196#1', #definition[fr] = 'Volume de mati??re n??cessaire ?? l''injection pour le surmoulage')");

		statement.executeUpdate(
				"CREATE #CLASS \"Basse Pression\" UNDER Surmoulage (DESCRIPTOR (#code = '0002-41982799300025#01-220#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"Haute Pression\" UNDER Surmoulage (DESCRIPTOR (#code = '0002-41982799300025#01-221#1'))");

		// TYFermeture
		statement.executeUpdate(
				"CREATE #CLASS TYFermeture under \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-223#1')))");
		statement.executeUpdate(
				"ALTER #CLASS TYFermeture ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-197#1', #definition[fr] = 'Poids d''une unit?? de composant')");
		statement.executeUpdate(
				"ALTER #CLASS TYFermeture ADD \"Temp??rature Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-199#1', #definition[fr] = 'Temp??rature minimum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS TYFermeture ADD \"Temp??rature Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-200#1', #definition[fr] = 'Temp??rature maximum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS TYFermeture ADD Mati??re String DESCRIPTOR (#code = '0002-41982799300025#02-201#1', #definition[fr] = 'Mati??re de la cosse')");
		statement.executeUpdate(
				"ALTER #CLASS TYFermeture ADD Fixation Boolean DESCRIPTOR (#code = '0002-41982799300025#02-202#1', #definition[fr] = 'Composant poss??dant un support pour une fixation')");

		statement.executeUpdate(
				"CREATE #CLASS Y UNDER TYFermeture (DESCRIPTOR (#code = '0002-41982799300025#01-224#1'))");
		statement.executeUpdate(
				"CREATE #CLASS \"R??ducteur TYFermeture\" UNDER TYFermeture (DESCRIPTOR (#code = '0002-41982799300025#01-225#1'))");
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
				"ALTER #CLASS Visserie ADD \"Temp??rature Min\" Int DESCRIPTOR (#code = '0002-41982799300025#02-204#1', #definition[fr] = 'Temp??rature minimum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS Visserie ADD \"Temp??rature Max\" Int DESCRIPTOR (#code = '0002-41982799300025#02-205#1', #definition[fr] = 'Temp??rature maximum support??e par le composant (degr?? Celcius)')");
		statement.executeUpdate(
				"ALTER #CLASS Visserie ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-206#1', #definition[fr] = 'Poids d''une unit?? de composant')");
		statement.executeUpdate(
				"ALTER #CLASS Visserie ADD \"Diam??tre Int??rieur\" REAL DESCRIPTOR (#code = '0002-41982799300025#02-207#1',#definition[fr] = 'Diam??tre int??rieur')");
		statement.executeUpdate(
				"ALTER #CLASS Visserie ADD \"Diam??tre Ext??rieur\" REAL DESCRIPTOR (#code = '0002-41982799300025#02-208#1',#definition[fr] = 'Diam??tre ext??rieur')");
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
				"ALTER #CLASS \"Liste Configuration TY\" ADD \"R??f??rence BE\" Int DESCRIPTOR (#code = '0002-41982799300025#02-219#1', #definition[fr] = 'Cl??')");
		statement.executeUpdate(
				"ALTER #CLASS \"Liste Configuration TY\" ADD Diam??tre Real DESCRIPTOR (#code = '0002-41982799300025#02-221#1', #definition[fr] = 'Diam??tre admissible')");
		statement.executeUpdate(
				"ALTER #CLASS \"Liste Configuration TY\" ADD Profil String DESCRIPTOR (#code = '0002-41982799300025#02-222#1', #definition[fr] = 'Profil pour diam??tre admissible')");

		// Liste Sortie TY
		statement.executeUpdate(
				"CREATE #CLASS \"Liste Sortie TY\" under Divers (DESCRIPTOR (#code = '0002-41982799300025#01-238#1')))");
		statement.executeUpdate(
				"ALTER #CLASS \"Liste Sortie TY\" ADD \"R??f??rence BE du TY\" REF(\"TYFermeture\") DESCRIPTOR (#code = '0002-41982799300025#02-217#1', #definition[fr] = 'Lien')");
		statement.executeUpdate(
				"ALTER #CLASS \"Liste Sortie TY\" ADD \"R??f??rence ERP\" String DESCRIPTOR (#code = '0002-41982799300025#02-226#1', #definition[fr] = 'R??f??rence cr??e dans l''ERP')");
		statement.executeUpdate(
				"ALTER #CLASS \"Liste Sortie TY\" ADD Sortie String DESCRIPTOR (#code = '0002-41982799300025#02-218#1', #definition[fr] = 'Rep??rage de la sortie')");
		statement.executeUpdate(
				"ALTER #CLASS \"Liste Sortie TY\" ADD \"Configuration Admissible\" REF(\"Liste Configuration TY\") ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-216#1', #definition[fr] = 'Diam??tre admissible en fonction du profil')");

		t1.snapshot("CREATE CLASS");

		if (createExtentEnabled) {
			statement.executeUpdate(
					"CREATE EXTENT OF \"Liste Sortie TY\" (\"R??f??rence BE du TY\", \"R??f??rence ERP\",Sortie, \"Configuration Admissible\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Joint Sur Fil\" (\"Fiche Technique\", \"R??f??rence ERP\",\"D??signation\",\"Fabricant\",\"R??f??rence Fabricant\",\"Repr??sentation\", \"Normes\", \"Poids\", \"Temp??rature Min\", \"Temp??rature Max\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", \"Lien\", \"R??f??rences Clients\", \"Obsol??te\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Bouchon\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Cale&Verrou\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Capot\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"D??trompeur\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Joint Interface\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Obturateur\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Diam??tre Ext??rieur\",\"Longueur Obturateur\", \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Capuchon\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Accessoires Divers\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Prise Batterie\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te)");

			statement.executeUpdate(
					"CREATE EXTENT OF \"Ports Connecteur\" (\"R??f??rence ERP\", \"Nom\", \"Famille Cosse\", \"Obturateur\", \"Longueur D??nudage\", Surc??tes)");
			statement.executeUpdate("CREATE EXTENT OF \"Ports DPI\" (\"R??f??rence ERP\", \"Nom\",Surc??tes)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Ports Composant Elec\" (\"R??f??rence ERP\", \"Nom\", \"Famille Cosse\", \"Obturateur\", \"Longueur D??nudage\",Surc??tes)");

			statement.executeUpdate("CREATE EXTENT OF Surc??te (\"R??f??rence BE du Port\", Orientation, Valeur)");

			statement.executeUpdate(
					"CREATE EXTENT OF \"Cosse Oeil\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", Coud??e, \"Pr??co Sertissage\", Diam??tre, \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Cosse Fourche\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", Coud??e, \"Pr??co Sertissage\", Diam??tre, \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Harpon\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", \"Pr??co Sertissage\", \"Pour Trou Diam??tre\", \"Pour Epaisseur PCB\", Epissure, \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Bougie\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", Coud??e, \"Joint Sur Fil\", \"Pour Largeur\", \"Pour Hauteur\", \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Batterie\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", Coud??e, \"Pr??co Sertissage\", \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Femelle\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", Coud??e, \"Pr??co Sertissage\", \"Joint Sur Fil\", \"Pour Largeur\", \"Pour Hauteur\", \"Pour Diam??tre\", \"Accrochage Connecteur\", Verrouillage, D??v??rouillage, \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"M??le\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", Coud??e, \"Pr??co Sertissage\", \"Joint Sur Fil\", \"Accrochage Connecteur\", Verrouillage, D??v??rouillage, Largeur, Hauteur, Diam??tre, Epissure, \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Par Touche\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", Coud??e, \"Pr??co Sertissage\", \"Joint Sur Fil\", \"Pour Largeur\", \"Pour Hauteur\", \"Pour Diam??tre\", \"Pour Epaisseur PCB\", Epissure, \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Manchon\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", Epissure, \"Lien\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Splice\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", \"Joint Sur Fil\", Epissure, \"Lien\", \"R??f??rences Clients\", Obsol??te)");

			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 1 Voie\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 2 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 3 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 4 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 5 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 6 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 7 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 8 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 9 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 10 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 11 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 12 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 13 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 14 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 15 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 16 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 17 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 18 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 19 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 20 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 21 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 22 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 23 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 24 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 25 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 26 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 27 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 28 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 29 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 30 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 32 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 34 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 35 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 36 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 37 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 40 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 42 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 48 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 52 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 54 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 55 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 56 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 58 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 64 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 70 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 90 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 94 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Connecteur 96 Voies\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Nombre de Voies\", \"Sert Apr??s\", Etanche, \"Liste des Ports\", \"Pas des Voies\", \"Nbre Rang??e\", \"Connecteur Inverse\", \"Second verrouillage\", \"Mati??re Connecteur\", Blind??, Fixation,  \"Diam??tre Mini Isolant\" , \"Section Min\", \"Section Max\", \"Intensite Max\", \"Tension Admisible\", \"Lien\", \"Orientation Surc??te\", \"R??f??rences Clients\", Obsol??te)");

			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.22 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.25 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.34 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.38 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.5 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.6 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.7 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 0.75 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 1 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 1.34 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 1.4 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 1.5 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 2 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 2.5 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 3 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 4 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 5 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 6 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 7 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 10 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 16 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 20 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 25 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 35 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 40 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 50 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 60 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 70 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 75 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 95 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur Section 120 mm??\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 10AWG\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 12AWG\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 14AWG\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 16AWG\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 18AWG\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 20AWG\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 22AWG\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"MonoConducteur 24AWG\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF Tresse (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids,\"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF Brin (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Autre Section\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\",\"Section Mono\", \"Diam??tre Ext??rieur Min\",\"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", \"Nombre de Brins\", \"Diam??tre des Brins\", \"Nature du Conducteur\", \"Poids Cuivre\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Resistance Lin??ique\", \"Tension Phase/Phase\",\"Double Isolation\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"D??nudage Etag??\", \"Est Fil de Multi\")");

			statement.executeUpdate(
					"CREATE EXTENT OF Voyant (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Transformateur (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Thermostat (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Thermocouple (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Thermistance (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, Betta, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Telerupteur (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Switch (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Sonde (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF S??curit?? (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Temp??rature de Coupure\", \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF R??sistance (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Relais (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF R??gulateur (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Potentiom??tre (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Minirupteur (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF MicroRupteur (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Limiteur (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Led (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF InterRupteur (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Fusible (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Filtre (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Composants Electriques divers\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Disjoncteur (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Diode (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF D??tecteur (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Contacteur (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Condensateur (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Capacit?? (farad)\", \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Circuit (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Centrale (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Capteur (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Buzzer (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, R??sistance, Puissance, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Bouton (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Repr??sentation\", \"Nombre de P??les\", \"Polaris??\", \"Liste des Ports\", Tension, Intensit??, \"Lien\", \"Configuration Surc??te\", \"R??f??rences Clients\", Obsol??te)");

			statement.executeUpdate(
					"CREATE EXTENT OF \"2 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"3 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"4 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"5 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"6 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"7 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"8 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"9 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"10 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"11 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"12 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"13 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"14 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"15 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"16 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"17 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"18 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"19 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"20 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"24 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"25 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"30 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"36 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"40 Conducteurs\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Conducteurs ??quip??s\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", \"Type Conducteur\", \"Nombre Conducteur\", \"Poids Cuivre\", \"Diam??tre Ext??rieur Min\", \"Diam??tre Ext??rieur Nominal\", \"Diam??tre Ext??rieur Max\", Tresse, \"Brin de Masse\", \"Film Papier/Plastique\", \"Lg Conditionnement\", \"Nature Isolant\", \"Tension Phase/Neutre\", \"Tension Phase/Phase\", \"Fil Vert/Jaune\", \"MonoConducteurs Li??s\",  \"Connecteur Gauche\", \"Connecteur Droit\", Lien, \"R??f??rences Clients\", Obsol??te, \"Section Principale\")");

			statement.executeUpdate(
					"CREATE EXTENT OF \"Presse Etoupe\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, Fendu, \"Diam??tre Int??rieur\", Mati??re, Pas, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Raccord (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, Fendu, \"Diam??tre Int??rieur\", Mati??re, Pas, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Joint Plat\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, Fendu, \"Diam??tre Int??rieur\", Mati??re, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Joint Set\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, Fendu, \"Diam??tre Int??rieur\", Mati??re, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Ecrou Accessoires\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, Fendu, \"Diam??tre Int??rieur\", Mati??re, Pas, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF R??ducteur (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, Fendu, \"Diam??tre Int??rieur\", Mati??re, Pas, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Passe Fils\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, Fendu, \"Diam??tre Int??rieur\", Mati??re, \"Epaisseur T??le\", Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Rep??re (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, Fendu, \"Diam??tre Int??rieur\", Mati??re, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Ferrite (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, Fendu, \"Diam??tre Int??rieur\", Mati??re, Lien, \"R??f??rences Clients\", Obsol??te)");

			statement.executeUpdate(
					"CREATE EXTENT OF \"Autre Pas\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, \"Nombre de Voies\", \"Section Min\", \"Section Max\", \"Nombre de Contacts\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", \"Direct/Indirect\", \"Marquage\", \"Cloison Lat\", \"Cloison Cent\", \"Cl?? de Verrouillage\", \"Pas des Voies\", \"Liste des Ports\", Lien, Surc??tes, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Pas de 2.5\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, \"Nombre de Voies\", \"Section Min\", \"Section Max\", \"Nombre de Contacts\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", \"Direct/Indirect\", \"Marquage\", \"Cloison Lat\", \"Cloison Cent\", \"Cl?? de Verrouillage\", \"Pas des Voies\", \"Liste des Ports\", Lien, Surc??tes, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Pas de 3.96\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, \"Nombre de Voies\", \"Section Min\", \"Section Max\", \"Nombre de Contacts\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", \"Direct/Indirect\", \"Marquage\", \"Cloison Lat\", \"Cloison Cent\", \"Cl?? de Verrouillage\", \"Pas des Voies\", \"Liste des Ports\", Lien, Surc??tes, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Pas de 5\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, \"Nombre de Voies\", \"Section Min\", \"Section Max\", \"Nombre de Contacts\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", \"Direct/Indirect\", \"Marquage\", \"Cloison Lat\", \"Cloison Cent\", \"Cl?? de Verrouillage\", \"Pas des Voies\", \"Liste des Ports\", Lien, Surc??tes, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Rast 2.5\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, \"Nombre de Voies\", \"Section Min\", \"Section Max\", \"Nombre de Contacts\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", \"Direct/Indirect\", \"Marquage\", \"Cloison Lat\", \"Cloison Cent\", \"Cl?? de Verrouillage\", \"Pas des Voies\", \"Liste des Ports\", Lien, Surc??tes, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Rast 2.5 PRO\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, \"Nombre de Voies\", \"Section Min\", \"Section Max\", \"Nombre de Contacts\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", \"Direct/Indirect\", \"Marquage\", \"Cloison Lat\", \"Cloison Cent\", \"Cl?? de Verrouillage\", \"Pas des Voies\", \"Liste des Ports\", Lien, Surc??tes, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Rast 5\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\",\"Temp??rature Max\", Repr??sentation, \"Nombre de Voies\", \"Section Min\", \"Section Max\", \"Nombre de Contacts\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", \"Direct/Indirect\", \"Marquage\", \"Cloison Lat\", \"Cloison Cent\", \"Cl?? de Verrouillage\", \"Pas des Voies\", \"Liste des Ports\", Lien, Surc??tes, \"R??f??rences Clients\", Obsol??te)");

			statement.executeUpdate(
					"CREATE EXTENT OF Bac (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, Largeur, Marquage, Hauteur, \"Poids Maximum\", Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Carton (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, Largeur, Marquage, Hauteur, \"Poids Maximum\", Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Couvercle (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, Largeur, Marquage, Hauteur, \"Poids Maximum\", Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Film (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, Largeur, Marquage, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Intercalaire (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, Largeur, Marquage, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Sac (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, Largeur, Marquage, Hauteur, \"Poids Maximum\", Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF SacBulle (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, Largeur, Marquage, Hauteur, \"Poids Maximum\", Lien, \"R??f??rences Clients\", Obsol??te)");

			statement.executeUpdate(
					"CREATE EXTENT OF GAR (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, \"Diam??tre Int??rieur\", Fendue, \"Lg Conditionnement\", \"Diam??tre Ext??rieur\", Mati??re, Profil, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF GANF (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, \"Diam??tre Int??rieur\", Fendue, \"Lg Conditionnement\",\"Diam??tre Ext??rieur\", Mati??re, Profil, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF GAF (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, \"Diam??tre Int??rieur\", Fendue, \"Lg Conditionnement\",\"Diam??tre Ext??rieur\", Mati??re, Profil, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF GAFL (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, \"Diam??tre Int??rieur\", Fendue, \"Lg Conditionnement\",\"Diam??tre Ext??rieur\", Mati??re, Profil, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Zipper (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, \"Diam??tre Int??rieur\", Fendue, \"Lg Conditionnement\",\"Diam??tre Ext??rieur\", Mati??re, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Tress??e (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, \"Diam??tre Int??rieur\", Fendue, \"Lg Conditionnement\",\"Diam??tre Ext??rieur\", Mati??re, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Spiral??e (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, \"Diam??tre Int??rieur\", Fendue, \"Lg Conditionnement\",\"Diam??tre Ext??rieur\", Mati??re, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Lisse (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, \"Diam??tre Int??rieur\", Fendue, \"Lg Conditionnement\",\"Diam??tre Ext??rieur\", Mati??re, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Gaines Thermos\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, \"Diam??tre Int??rieur\", Fendue, \"Lg Conditionnement\",\"Diam??tre Ext??rieur\", Mati??re, \"Diam??tre Apr??s R??treint\", Lien, \"R??f??rences Clients\", Obsol??te, Collante, Marquage)");
			statement.executeUpdate(
					"CREATE EXTENT OF Feutrine (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, \"Diam??tre Int??rieur\", Fendue, \"Lg Conditionnement\",\"Diam??tre Ext??rieur\", Mati??re, Lien, \"R??f??rences Clients\", Obsol??te)");

			statement.executeUpdate(
					"CREATE EXTENT OF Collier (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Mati??re, Marquage, Largeur, Longueur, \"Diam??tre Toron Maxi\", \"Diam??tre Toron Mini\", Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Collier Agrafe\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Mati??re, Marquage, Largeur, Longueur, \"Diam??tre Fixation\", \"Forme Agrafe\", \"Diam??tre Toron Maxi\", \"Diam??tre Toron Mini\", Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Etiquette (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Mati??re, Marquage, Largeur, Longueur, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Pastille (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Mati??re, Marquage, Diam??tre, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Pion Fixation\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Mati??re, Marquage, Largeur, Longueur, \"Diam??tre Fixation\", \"Forme Agrafe\", Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Ruban Adh??sif\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Mati??re, Marquage, Largeur, Lien, \"R??f??rences Clients\", Obsol??te)");

			statement.executeUpdate(
					"CREATE EXTENT OF Y (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Mati??re, Fixation, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"R??ducteur TYFermeture\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Mati??re, Fixation, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF M (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Mati??re, Fixation, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF T (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Mati??re, Fixation, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF A (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Mati??re, Fixation, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Fermeture (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Mati??re, Fixation, Lien, \"R??f??rences Clients\", Obsol??te)");

			statement.executeUpdate(
					"CREATE EXTENT OF \"Basse Pression\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, \"Mati??re ?? Surmouler\", \"Volume Mati??re\", Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF \"Haute Pression\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, \"Mati??re ?? Surmouler\", \"Volume Mati??re\", Lien, \"R??f??rences Clients\", Obsol??te)");

			statement.executeUpdate(
					"CREATE EXTENT OF Ecrou (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Repr??sentation, Normes, \"Temp??rature Min\", \"Temp??rature Max\",Poids, \"Diam??tre Int??rieur\", \"Diam??tre Ext??rieur\", Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Goujon (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Repr??sentation, Normes, \"Temp??rature Min\", \"Temp??rature Max\",Poids, \"Diam??tre Ext??rieur\", Longueur, Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Goupille (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Repr??sentation, Normes, \"Temp??rature Min\", \"Temp??rature Max\",Poids, \"Diam??tre Int??rieur\", Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Rondelle (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Repr??sentation, Normes, \"Temp??rature Min\", \"Temp??rature Max\",Poids, \"Diam??tre Int??rieur\", \"Diam??tre Ext??rieur\", Lien, \"R??f??rences Clients\", Obsol??te)");
			statement.executeUpdate(
					"CREATE EXTENT OF Vis (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\", \"Fiche Technique\", Repr??sentation, Normes, \"Temp??rature Min\", \"Temp??rature Max\",Poids, \"Diam??tre Ext??rieur\", Longueur, Lien, \"R??f??rences Clients\", Obsol??te)");

			statement.executeUpdate("CREATE EXTENT OF \"Liste Configuration TY\" (\"R??f??rence BE\",Diam??tre, Profil)");

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
		statement.executeUpdate("UPDATE \"Liste Sortie TY\" SET \"R??f??rence BE du TY\" = " + rid);

		t1.start();
		// Generate 1000 select per class.
		for (int i = 0; i < 1000; i++) {
			statement.executeUpdate(
					"select * from \"Liste Sortie TY\" ty, \"Composant CFCA\" f where ty.\"R??f??rence BE du TY\" = f.rid");
		}
		t1.snapshot("SELECT \"Liste Sortie TY\" JOIN \"Fermeture\"");

		// if (writer != null) {
		// writer.close();
		// }

		session.rollback();
		statement.close();
	}
}
