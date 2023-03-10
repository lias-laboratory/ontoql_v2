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
package fr.ensma.lias.mariusql.bulkload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;

/**
 * @author Mickael Baron
 */
public class MariusQLBulkloadTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLBulkloadTest.class);

	@Before
	public void setup() {
		this.getSession().setReferenceLanguage(MariusQLConstants.FRENCH);
		this.getSession().setDefaultNameSpace("http://www.cfca.fr/");
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

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
				"ALTER #CLASS \"Composant CFCA\" ADD Couleur ENUM ('argent','aucun','blanc','blanc/bleu','blanc/gris','blanc/jaune','blanc/marron','blanc/noir','blanc/orange','blanc/rose','blanc/rouge','blanc/vert','blanc/violet','bleu','bleu/blanc','bleu/jaune','bleu/marron','bleu/orange','bleu/rouge','gris','gris/blanc','gris/bleu','gris/marron','gris/orange','gris/rose','gris/vert','ivoire','jaune','jaune/gris','jaune/marron','jaune/noir','jaune/rose','jaune/vert','marron','marron/blanc','marron/bleu','marron/noir','marron/rouge','marron/vert','marron/violet','metal','metalique','muti','m??tal','m??talique','naturel','noir','noir/blanc','noir/bleu','noir/gris','noir/jaune','noir/rouge','noir/saumon','noir/vert','noir/violet','orange','orange/blanc','orange/noir','rose','rose/bleu','rose/gris','rose/marron','rose/noir','rose/vert','rouge','rouge/blanc','rouge/bleu','rouge/gris','rouge/jaune','rouge/noir','rouge/orange','rouge/violet','transaprent','vert','vert/blanc','vert/bleu','vert/gris','vert/jaune','vert/rouge','vert/violet','verte','violet','violet/gris','violet/jaune','??crue') DESCRIPTOR (#code = '0002-41982799300025#02-7#1',#definition[fr] = 'Couleur ext??rieure du composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Normes\" String ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-8#1', #definition[fr] = 'Liste des normes auxquelles r??pond le composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Lien\" Ref(\"Composant CFCA\") ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-213#1', #definition[fr] = 'Composant fortement li?? ?? celui d??crit')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"R??f??rences Clients\" String ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-220#1', #definition[fr] = 'Identifiants pr??sents chez les clients')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Obsol??te\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-233#1', #definition[fr] = 'Indique que le composant ne doit plus ??tre utilis??')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Composant Inverse\" Ref(\"Composant CFCA\") DESCRIPTOR (#code = '0002-41982799300025#02-70#1', #definition[fr] = 'Indique l''identifiant BE du connecteur inverse')");

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
				"ALTER #CLASS Connexions ADD \"Diam??tre Mini IsolantBis\" Real DESCRIPTOR (#code = '0002-41982799300025#02-38#1', #definition[fr] = 'Diam??tre minimum de l''isolant du composant li??')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Diam??tre Maxi IsolantBis\" Real DESCRIPTOR (#code = '0002-41982799300025#02-39#1', #definition[fr] = 'Diam??tre maximum de l''isolant du composant li??')");
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
				"CREATE EXTENT OF \"Joint Sur Fil\" (\"Fiche Technique\", \"R??f??rence ERP\",\"D??signation\",\"Fabricant\",\"R??f??rence Fabricant\",Repr??sentation, Couleur, \"Normes\", \"Poids\", \"Temp??rature Min\", \"Temp??rature Max\", \"Diam??tre Mini Isolant\", \"Diam??tre Maxi Isolant\", \"Lien\", \"R??f??rences Clients\", \"Obsol??te\", \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Bouchon\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Couleur, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Cale&Verrou\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Couleur, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Capot\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Couleur, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"D??trompeur\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Couleur, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Joint Interface\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Couleur, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Obturateur\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Couleur, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Diam??tre Ext??rieur\",\"Longueur Obturateur\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Capuchon\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Couleur, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Accessoires Divers\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Couleur, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Prise Batterie\" (\"Fiche Technique\", \"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",Repr??sentation, Couleur, Normes, Poids, \"Temp??rature Min\", \"Temp??rature Max\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");

		statement.executeUpdate(
				"CREATE EXTENT OF \"Cosse Oeil\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini IsolantBis\", \"Diam??tre Maxi IsolantBis\", Coud??e, \"Pr??co Sertissage\", Diam??tre, \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Cosse Fourche\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini IsolantBis\", \"Diam??tre Maxi IsolantBis\", Coud??e, \"Pr??co Sertissage\", Diam??tre, \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Harpon\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini IsolantBis\", \"Diam??tre Maxi IsolantBis\", \"Pr??co Sertissage\", \"Pour Trou Diam??tre\", \"Pour Epaisseur PCB\", Epissure, \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Bougie\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini IsolantBis\", \"Diam??tre Maxi IsolantBis\", Coud??e, \"Joint Sur Fil\", \"Pour Largeur\", \"Pour Hauteur\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Batterie\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini IsolantBis\", \"Diam??tre Maxi IsolantBis\", Coud??e, \"Pr??co Sertissage\", \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Femelle\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini IsolantBis\", \"Diam??tre Maxi IsolantBis\", Coud??e, \"Pr??co Sertissage\", \"Joint Sur Fil\", \"Pour Largeur\", \"Pour Hauteur\", \"Pour Diam??tre\", \"Accrochage Connecteur\", Verrouillage, D??v??rouillage, \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"M??le\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini IsolantBis\", \"Diam??tre Maxi IsolantBis\", Coud??e, \"Pr??co Sertissage\", \"Joint Sur Fil\", \"Accrochage Connecteur\", Verrouillage, D??v??rouillage, Largeur, Hauteur, Diam??tre, Epissure, \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Par Touche\" (\"R??f??rence ERP\",D??signation,Fabricant,\"R??f??rence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Temp??rature Min\", \"Temp??rature Max\", Repr??sentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Mati??re Cosse\", \"Amp??rage maximum 20??\", \"Diam??tre Mini IsolantBis\", \"Diam??tre Maxi IsolantBis\", Coud??e, \"Pr??co Sertissage\", \"Joint Sur Fil\", \"Pour Largeur\", \"Pour Hauteur\", \"Pour Diam??tre\", \"Pour Epaisseur PCB\", Epissure, \"Lien\", \"R??f??rences Clients\", Obsol??te, \"Composant Inverse\")");
	}

	@Test
	public void testLoadWithList() {
		log.debug("MariusQLBulkloadTest.testLoadWithList()");

		final MariusQLBulkload mariusQLBulkload = this.getSession().getMariusQLBulkload();
		List<String> properties = new ArrayList<String>();
		properties.add("Fiche Technique");
		properties.add("R??f??rence ERP");
		properties.add("D??signation");
		properties.add("Fabricant");
		properties.add("R??f??rence Fabricant");
		properties.add("Repr??sentation");
		properties.add("Couleur");
		properties.add("Normes");
		properties.add("Poids");
		properties.add("Temp??rature Min");
		properties.add("Temp??rature Max");
		properties.add("R??f??rences Clients");
		properties.add("Obsol??te");

		List<List<String>> values = new ArrayList<List<String>>();
		for (int i = 0; i < 5000; i++) {
			values.add(Arrays.asList("http://www.google.fr" + i, "referenceERP" + i, "designation" + i, "fabricant" + i,
					"ref fabricant" + i, "representation" + i, "couleur" + i,
					"ARRAY['normes1" + i + "','normes2" + i + "']", "37.0", "5", "9",
					"ARRAY['refClient1','refClient2']", "true"));
		}

		final BulkloadReport loadClassInstancesByCollection = mariusQLBulkload.loadClassInstancesByCollection("Bouchon",
				properties, values);
		Assert.assertEquals(5000, loadClassInstancesByCollection.getInsertedRowCount());
		Assert.assertTrue(loadClassInstancesByCollection.getDuration() > 100);
	}
}
