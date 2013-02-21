package uk.ac.manchester.cs.owl.comp60421;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Collection;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;


/**
 * Author: Pavel Klinov<br>
 * The University of Manchester<br>
 * Information Management Group<br>
 *
 */
public class Test {

    public static void main(String[] args) throws Throwable {
    	
    	Main main = new Main();
    	//Load data from CSV
    	Collection<JobDataBean> beans =  new CSVParser().parse(new InputStreamReader(URI.create(args[0]).toURL().openStream()));
    	//Uncomment the following line to check CSV parsing
    	//for (JobDataBean bean : beans) System.out.println(bean);
    	
    	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    	//Your code should now load the ontology using the supplied manager
    	OWLOntology  ontology = main.loadOntology(manager, IRI.create(args[1]));
    	//Here's your central task - populate the ontology
    	main.populateOntology(manager, ontology, beans);
    	//Storing to disk
    	main.saveOntology(manager, ontology, IRI.create(new File(args[2]).toURI()));
    }
}
