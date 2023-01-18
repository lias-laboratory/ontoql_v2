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
				"ALTER #CLASS \"Composant CFCA\" ADD Couleur ENUM ('argent','aucun','blanc','blanc/bleu','blanc/gris','blanc/jaune','blanc/marron','blanc/noir','blanc/orange','blanc/rose','blanc/rouge','blanc/vert','blanc/violet','bleu','bleu/blanc','bleu/jaune','bleu/marron','bleu/orange','bleu/rouge','gris','gris/blanc','gris/bleu','gris/marron','gris/orange','gris/rose','gris/vert','ivoire','jaune','jaune/gris','jaune/marron','jaune/noir','jaune/rose','jaune/vert','marron','marron/blanc','marron/bleu','marron/noir','marron/rouge','marron/vert','marron/violet','metal','metalique','muti','métal','métalique','naturel','noir','noir/blanc','noir/bleu','noir/gris','noir/jaune','noir/rouge','noir/saumon','noir/vert','noir/violet','orange','orange/blanc','orange/noir','rose','rose/bleu','rose/gris','rose/marron','rose/noir','rose/vert','rouge','rouge/blanc','rouge/bleu','rouge/gris','rouge/jaune','rouge/noir','rouge/orange','rouge/violet','transaprent','vert','vert/blanc','vert/bleu','vert/gris','vert/jaune','vert/rouge','vert/violet','verte','violet','violet/gris','violet/jaune','écrue') DESCRIPTOR (#code = '0002-41982799300025#02-7#1',#definition[fr] = 'Couleur extérieure du composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Normes\" String ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-8#1', #definition[fr] = 'Liste des normes auxquelles répond le composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Lien\" Ref(\"Composant CFCA\") ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-213#1', #definition[fr] = 'Composant fortement lié à celui décrit')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Références Clients\" String ARRAY DESCRIPTOR (#code = '0002-41982799300025#02-220#1', #definition[fr] = 'Identifiants présents chez les clients')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Obsolète\" Boolean DESCRIPTOR (#code = '0002-41982799300025#02-233#1', #definition[fr] = 'Indique que le composant ne doit plus être utilisé')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Composant Inverse\" Ref(\"Composant CFCA\") DESCRIPTOR (#code = '0002-41982799300025#02-70#1', #definition[fr] = 'Indique l''identifiant BE du connecteur inverse')");

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
				"ALTER #CLASS Connexions ADD \"Diamètre Mini IsolantBis\" Real DESCRIPTOR (#code = '0002-41982799300025#02-38#1', #definition[fr] = 'Diamètre minimum de l''isolant du composant lié')");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD \"Diamètre Maxi IsolantBis\" Real DESCRIPTOR (#code = '0002-41982799300025#02-39#1', #definition[fr] = 'Diamètre maximum de l''isolant du composant lié')");
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
				"CREATE EXTENT OF \"Joint Sur Fil\" (\"Fiche Technique\", \"Référence ERP\",\"Désignation\",\"Fabricant\",\"Référence Fabricant\",Représentation, Couleur, \"Normes\", \"Poids\", \"Température Min\", \"Température Max\", \"Diamètre Mini Isolant\", \"Diamètre Maxi Isolant\", \"Lien\", \"Références Clients\", \"Obsolète\", \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Bouchon\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Couleur, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Cale&Verrou\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Couleur, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Capot\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Couleur, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Détrompeur\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Couleur, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Joint Interface\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Couleur, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Obturateur\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Couleur, Normes, Poids, \"Température Min\", \"Température Max\", \"Diamètre Extérieur\",\"Longueur Obturateur\", \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Capuchon\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Couleur, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Accessoires Divers\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Couleur, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Prise Batterie\" (\"Fiche Technique\", \"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",Représentation, Couleur, Normes, Poids, \"Température Min\", \"Température Max\", \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");

		statement.executeUpdate(
				"CREATE EXTENT OF \"Cosse Oeil\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini IsolantBis\", \"Diamètre Maxi IsolantBis\", Coudée, \"Préco Sertissage\", Diamètre, \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Cosse Fourche\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini IsolantBis\", \"Diamètre Maxi IsolantBis\", Coudée, \"Préco Sertissage\", Diamètre, \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Harpon\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini IsolantBis\", \"Diamètre Maxi IsolantBis\", \"Préco Sertissage\", \"Pour Trou Diamètre\", \"Pour Epaisseur PCB\", Epissure, \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Bougie\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini IsolantBis\", \"Diamètre Maxi IsolantBis\", Coudée, \"Joint Sur Fil\", \"Pour Largeur\", \"Pour Hauteur\", \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Batterie\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini IsolantBis\", \"Diamètre Maxi IsolantBis\", Coudée, \"Préco Sertissage\", \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Femelle\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini IsolantBis\", \"Diamètre Maxi IsolantBis\", Coudée, \"Préco Sertissage\", \"Joint Sur Fil\", \"Pour Largeur\", \"Pour Hauteur\", \"Pour Diamètre\", \"Accrochage Connecteur\", Verrouillage, Dévérouillage, \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Mâle\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini IsolantBis\", \"Diamètre Maxi IsolantBis\", Coudée, \"Préco Sertissage\", \"Joint Sur Fil\", \"Accrochage Connecteur\", Verrouillage, Dévérouillage, Largeur, Hauteur, Diamètre, Epissure, \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
		statement.executeUpdate(
				"CREATE EXTENT OF \"Par Touche\" (\"Référence ERP\",Désignation,Fabricant,\"Référence Fabricant\",\"Fiche Technique\",Couleur, Normes, Poids,\"Température Min\", \"Température Max\", Représentation, Famille, \"Section Min\", \"Section Max\", \"Lg Denudage\", Finition, \"Matière Cosse\", \"Ampérage maximum 20°\", \"Diamètre Mini IsolantBis\", \"Diamètre Maxi IsolantBis\", Coudée, \"Préco Sertissage\", \"Joint Sur Fil\", \"Pour Largeur\", \"Pour Hauteur\", \"Pour Diamètre\", \"Pour Epaisseur PCB\", Epissure, \"Lien\", \"Références Clients\", Obsolète, \"Composant Inverse\")");
	}

	@Test
	public void testLoadWithList() {
		log.debug("MariusQLBulkloadTest.testLoadWithList()");

		final MariusQLBulkload mariusQLBulkload = this.getSession().getMariusQLBulkload();
		List<String> properties = new ArrayList<String>();
		properties.add("Fiche Technique");
		properties.add("Référence ERP");
		properties.add("Désignation");
		properties.add("Fabricant");
		properties.add("Référence Fabricant");
		properties.add("Représentation");
		properties.add("Couleur");
		properties.add("Normes");
		properties.add("Poids");
		properties.add("Température Min");
		properties.add("Température Max");
		properties.add("Références Clients");
		properties.add("Obsolète");

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
