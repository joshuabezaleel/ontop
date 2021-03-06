package it.unibz.inf.ontop.owlrefplatform.owlapi.example;

/*
 * #%L
 * ontop-quest-owlapi
 * %%
 * Copyright (C) 2009 - 2014 Free University of Bozen-Bolzano
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import it.unibz.inf.ontop.model.OBDAModel;
import it.unibz.inf.ontop.owlrefplatform.owlapi.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;

/*
 * Use the sample database using H2 from
 * https://github.com/ontop/ontop/wiki/InstallingTutorialDatabases
 *
 * Please use the pre-bundled H2 server from the above link
 *
 */
public class QuestOWLExample {


    final String owlfile = "src/main/resources/example/exampleBooks.owl";
    final String obdafile = "src/main/resources/example/exampleBooks.obda";

    public void runQuery() throws Exception {

		/*
         * Load the ontology from an external .owl file.
		 */
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(owlfile));

		/*
         * Load the OBDA model from an external .obda file
		 */
        OBDAModel obdaModel = new MappingLoader().loadFromOBDAFile(obdafile);

		/*
         * Create the instance of Quest OWL reasoner.
		 */
        QuestOWLFactory factory = new QuestOWLFactory();
        QuestOWLConfiguration config = QuestOWLConfiguration.builder().obdaModel(obdaModel).build();
        QuestOWL reasoner = factory.createReasoner(ontology, config);

		/*
         * Get the book information that is stored in the database
		 */
        String sparqlQuery = "PREFIX : <http://meraka/moss/exampleBooks.owl#> \n" +
                " SELECT DISTINCT ?x ?title ?author ?genre ?edition \n" +
                " WHERE { ?x a :Book; :title ?title; :genre ?genre; :writtenBy ?y; :hasEdition ?z. \n" +
                "         ?y a :Author; :name ?author. \n" +
                "         ?z a :Edition; :editionNumber ?edition }";

        try (/*
              * Prepare the data connection for querying.
		 	 */
             QuestOWLConnection conn = reasoner.getConnection();
             QuestOWLStatement st = conn.createStatement()) {

            long t1 = System.currentTimeMillis();
            QuestOWLResultSet rs = st.executeTuple(sparqlQuery);
            int columnSize = rs.getColumnCount();
            while (rs.nextRow()) {
                for (int idx = 1; idx <= columnSize; idx++) {
                    OWLObject binding = rs.getOWLObject(idx);
                    System.out.print(ToStringRenderer.getInstance().getRendering(binding) + ", ");
                }
                System.out.print("\n");
            }
            rs.close();
            long t2 = System.currentTimeMillis();

			/*
             * Print the query summary
			 */
            QuestOWLStatement qst = st;
            String sqlQuery = qst.getUnfolding(sparqlQuery);

            System.out.println();
            System.out.println("The input SPARQL query:");
            System.out.println("=======================");
            System.out.println(sparqlQuery);
            System.out.println();

            System.out.println("The output SQL query:");
            System.out.println("=====================");
            System.out.println(sqlQuery);

            System.out.println("Query Execution Time:");
            System.out.println("=====================");
            System.out.println((t2 - t1) + "ms");

        } finally {
            reasoner.dispose();
        }
    }


    /**
     * Main client program
     */
    public static void main(String[] args) {
        try {
            QuestOWLExample example = new QuestOWLExample();
            example.runQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
