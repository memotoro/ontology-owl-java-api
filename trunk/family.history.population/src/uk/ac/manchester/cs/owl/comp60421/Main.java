package uk.ac.manchester.cs.owl.comp60421;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 * Author: Pavel Klinov<br>
 * The University of Manchester<br>
 * Information Management Group<br>
 * 
 * This class MUST HAVE A ZERO ARGUMENT CONSTRUCTOR! <br>
 * 
 * Modified by: Guillermo Antonio Toro Bayona
 */
public class Main {
	// Hash
	public final static String HASH_SYMBOL = "#";
	// Individuals
	public final static String IND_SOURCE_NAME = "source_";
	public final static String IND_ROLE_NAME = "role_";
	public final static String IND_ROLE_PLAYED_NAME = "rolePlayed_";
	public final static String IND_PERSON_NAME = "person_";
	// Classes
	public final static String CLS_PERSON = "Person";
	public final static String CLS_ROLE = "Role";
	public final static String CLS_SOURCE = "Source";
	public final static String CLS_SOURCE_BIRTH = "BirthRecord";
	public final static String CLS_SOURCE_CENSUS = "Census";
	public final static String CLS_SOURCE_MARRIAGE = "MarriageRecord";
	public final static String CLS_SOURCE_DEATH = "DeathRecord";
	public final static String CLS_SOURCE_SERVICE = "ServiceRecord";
	public final static String CLS_ROLE_PLAYED = "RolePlayed";
	// Object properties
	public final static String OBJ_PRP_HAS_ROLE = "hasRole";
	public final static String OBJ_PRP_HAS_SOURCE = "hasSource";
	public final static String OBJ_PRP_PLAYS_ROLE = "playsRole";
	// Data properties
	public final static String DAT_PRP_HAS_GIVEN_NAME = "hasGivenName";
	public final static String DAT_PRP_HAS_SURNAME = "hasSurname";
	public final static String DAT_PRP_HAS_MARRIED_SURNAME = "hasMarriedSurname";
	public final static String DAT_PRP_HAS_BIRTH_YEAR = "hasBirthYear";
	public final static String DAT_PRP_HAS_YEAR = "hasYear";
	//
	public final static String STR_BIRTH = "birth";
	public final static String STR_CENSUS = "census";
	public final static String STR_DEATH = "death";
	public final static String STR_MARRIAGE = "marriage";
	public final static String STR_SERVICE = "service";

	/**
	 * Constructor
	 */
	protected Main() {
		// Do not specify a different constructor to this empty constructor!
	}

	protected void populateOntology(OWLOntologyManager manager,
			OWLOntology ontology, Collection<JobDataBean> beans) {
		// Initialize the maps
		Map<String, OWLIndividual> mapIndividuals = new HashMap<String, OWLIndividual>();
		// List of persons
		List<Main.Person> persons = parseCollectionBeans(beans);
		// IRI of the ontology
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI();
		// Data factory
		OWLDataFactory dataFactory = manager.getOWLDataFactory();
		// Class Person
		OWLClass classPerson = dataFactory.getOWLClass(IRI.create(ontologyIRI
				+ HASH_SYMBOL + CLS_PERSON));
		// Class RolePlayed
		OWLClass classRolePlayed = dataFactory.getOWLClass(IRI
				.create(ontologyIRI + HASH_SYMBOL + CLS_ROLE_PLAYED));
		// Class Role
		OWLClass classRole = dataFactory.getOWLClass(IRI.create(ontologyIRI
				+ HASH_SYMBOL + CLS_ROLE));
		// Initialize the map for Sources
		initializeMaps(manager, dataFactory, ontology, ontologyIRI, classRole,
				IND_ROLE_NAME, mapIndividuals);
		// Class Source
		OWLClass classSource = dataFactory.getOWLClass(IRI.create(ontologyIRI
				+ HASH_SYMBOL + CLS_SOURCE));
		// Initialize the map for Sources
		initializeMaps(manager, dataFactory, ontology, ontologyIRI,
				classSource, IND_SOURCE_NAME, mapIndividuals);
		// Create an assertion axiom
		OWLAxiom classAssertionAxiom = null;
		// Create an assertion for data properties
		OWLDataPropertyAssertionAxiom dataPropertyAssertionAxiom = null;
		// Create an assertion for object properties
		OWLObjectPropertyAssertionAxiom objectPropertyAssertionAxiom = null;
		// Create a data property
		OWLDataProperty dataProperty = null;
		// Create object property
		OWLObjectProperty objectProperty = null;
		// Control for persons and roles
		int personNumber = 1;
		int roleNumber = 1;
		// Loop for persons
		for (Person person : persons) {

			/**
			 * Individual Person
			 */

			// Create the name of the individual
			String individualPersonName = HASH_SYMBOL
					+ IND_PERSON_NAME
					+ lpadLeftForString(String.valueOf(personNumber++),
							persons.size());
			// Create an individual with the name created
			OWLIndividual individualPerson = dataFactory
					.getOWLNamedIndividual(IRI.create(ontologyIRI
							+ individualPersonName));
			// Create an assertion to set the type of the individual
			classAssertionAxiom = dataFactory.getOWLClassAssertionAxiom(
					classPerson, individualPerson);
			// Add the axiom to the ontology
			manager.addAxiom(ontology, classAssertionAxiom);
			// Validate the birth year of the person
			if (person.getBirthYear() != null) {
				// Create the data property for hasBirthYear
				dataProperty = dataFactory.getOWLDataProperty(IRI
						.create(ontologyIRI + HASH_SYMBOL
								+ DAT_PRP_HAS_BIRTH_YEAR));
				// Set the data property assertion with the specific value
				dataPropertyAssertionAxiom = dataFactory
						.getOWLDataPropertyAssertionAxiom(dataProperty,
								individualPerson, person.getBirthYear());
				// Add the axiom to the ontology
				manager.addAxiom(ontology, dataPropertyAssertionAxiom);
			}
			// Validate the married surname of the person
			if (person.getMarriedSurname() != null) {
				// Create the data property for hasMarriedSurname
				dataProperty = dataFactory.getOWLDataProperty(IRI
						.create(ontologyIRI + HASH_SYMBOL
								+ DAT_PRP_HAS_MARRIED_SURNAME));
				// Set the data property assertion with the specific value
				dataPropertyAssertionAxiom = dataFactory
						.getOWLDataPropertyAssertionAxiom(dataProperty,
								individualPerson, person.getMarriedSurname());
				// Add the axiom to the ontology
				manager.addAxiom(ontology, dataPropertyAssertionAxiom);
			}
			// Create the data property for hasGivenName
			dataProperty = dataFactory
					.getOWLDataProperty(IRI.create(ontologyIRI + HASH_SYMBOL
							+ DAT_PRP_HAS_GIVEN_NAME));
			// Set the data property assertion with the specific value
			dataPropertyAssertionAxiom = dataFactory
					.getOWLDataPropertyAssertionAxiom(dataProperty,
							individualPerson, person.getGivenName());
			// Add the axiom to the ontology
			manager.addAxiom(ontology, dataPropertyAssertionAxiom);
			// Create the data property for hasSurname
			dataProperty = dataFactory.getOWLDataProperty(IRI
					.create(ontologyIRI + HASH_SYMBOL + DAT_PRP_HAS_SURNAME));
			// Set the data property assertion with the specific value
			dataPropertyAssertionAxiom = dataFactory
					.getOWLDataPropertyAssertionAxiom(dataProperty,
							individualPerson, person.getSurname());
			// Add the axiom to the ontology
			manager.addAxiom(ontology, dataPropertyAssertionAxiom);
			// Loop to create different roles, roles played and sources
			for (Role role : person.getRoles()) {

				/**
				 * Individual Role Played
				 */

				// Name of the rolePlayed
				String individualRolePlayedName = HASH_SYMBOL
						+ IND_ROLE_PLAYED_NAME
						+ lpadLeftForString(String.valueOf(roleNumber++),
								mapIndividuals.size());
				// Create an individual with the name created
				OWLIndividual individualRolePlayed = dataFactory
						.getOWLNamedIndividual(IRI.create(ontologyIRI
								+ individualRolePlayedName));
				// Create an assertion to set the type of the individual
				classAssertionAxiom = dataFactory.getOWLClassAssertionAxiom(
						classRolePlayed, individualRolePlayed);
				// Add the axiom to the ontology
				manager.addAxiom(ontology, classAssertionAxiom);
				// Create the data property for hasYear
				dataProperty = dataFactory.getOWLDataProperty(IRI
						.create(ontologyIRI + HASH_SYMBOL + DAT_PRP_HAS_YEAR));
				// Set the data property assertion with the specific value
				dataPropertyAssertionAxiom = dataFactory
						.getOWLDataPropertyAssertionAxiom(dataProperty,
								individualRolePlayed, role.getYear());
				// Add the axiom to the ontology
				manager.addAxiom(ontology, dataPropertyAssertionAxiom);
				// Create the object property for playsRole
				objectProperty = dataFactory
						.getOWLObjectProperty(IRI.create(ontologyIRI
								+ HASH_SYMBOL + OBJ_PRP_PLAYS_ROLE));
				// Create the axiom
				objectPropertyAssertionAxiom = dataFactory
						.getOWLObjectPropertyAssertionAxiom(objectProperty,
								individualPerson, individualRolePlayed);
				// Add the axiom to the ontology
				manager.addAxiom(ontology, objectPropertyAssertionAxiom);

				/**
				 * Individual Source
				 */

				// Create an individual with the name created
				OWLIndividual individualSource = null;
				// Validate the name of the source
				if (role.getSource().toLowerCase().contains(STR_BIRTH)) {
					// Get from the map
					individualSource = mapIndividuals.get(HASH_SYMBOL
							+ CLS_SOURCE_BIRTH);
				} else if (role.getSource().toLowerCase().contains(STR_CENSUS)) {
					// Get from the map
					individualSource = mapIndividuals.get(HASH_SYMBOL
							+ CLS_SOURCE_CENSUS);
				} else if (role.getSource().toLowerCase().contains(STR_DEATH)) {
					// Get from the map
					individualSource = mapIndividuals.get(HASH_SYMBOL
							+ CLS_SOURCE_DEATH);
				} else if (role.getSource().toLowerCase()
						.contains(STR_MARRIAGE)) {
					// Get from the map
					individualSource = mapIndividuals.get(HASH_SYMBOL
							+ CLS_SOURCE_MARRIAGE);
				} else if (role.getSource().toLowerCase().contains(STR_SERVICE)) {
					// Get from the map
					individualSource = mapIndividuals.get(HASH_SYMBOL
							+ CLS_SOURCE_SERVICE);
				} else {
					// Get from the map
					individualSource = mapIndividuals.get(HASH_SYMBOL
							+ CLS_SOURCE);
				}
				// Create object property for hasSource
				objectProperty = dataFactory
						.getOWLObjectProperty(IRI.create(ontologyIRI
								+ HASH_SYMBOL + OBJ_PRP_HAS_SOURCE));
				// Create assertion axiom
				objectPropertyAssertionAxiom = dataFactory
						.getOWLObjectPropertyAssertionAxiom(objectProperty,
								individualRolePlayed, individualSource);
				// Add the axiom to the ontology
				manager.addAxiom(ontology, objectPropertyAssertionAxiom);

				/**
				 * Individual Role
				 */

				// Create individual Role
				OWLIndividual individualRole = mapIndividuals.get(HASH_SYMBOL
						+ role.getOccupation());
				// Validate null
				if (individualRole == null) {
					// Get generic individual role
					individualRole = mapIndividuals.get(HASH_SYMBOL + CLS_ROLE);
				}
				// Create object property for hasRole
				objectProperty = dataFactory.getOWLObjectProperty(IRI
						.create(ontologyIRI + HASH_SYMBOL + OBJ_PRP_HAS_ROLE));
				// Create the axiom
				objectPropertyAssertionAxiom = dataFactory
						.getOWLObjectPropertyAssertionAxiom(objectProperty,
								individualRolePlayed, individualRole);
				// Add the axiom to the ontology
				manager.addAxiom(ontology, objectPropertyAssertionAxiom);
			}
		}
	}

	/**
	 * Method that load an ontology
	 * 
	 * @param manager
	 *            OWLOntologyManager
	 * @param ontologyIRI
	 *            IRI ontologyIRI
	 * @return OWLOntology
	 * 
	 * @author Guillermo Antonio Toro Bayona
	 */
	protected OWLOntology loadOntology(OWLOntologyManager manager,
			IRI ontologyIRI) {
		// Create null ontolgy
		OWLOntology ontology = null;
		try {
			// Load the ontology
			ontology = manager.loadOntologyFromOntologyDocument(ontologyIRI);
		} catch (OWLOntologyCreationException e) {
			// Error loading the ontology
			System.out.println("Error loading the ontology: " + e.getMessage());
		}
		// Return the ontology
		return ontology;
	}

	/**
	 * Method that save the ontology in the file
	 * 
	 * @param manager
	 *            OWLOntologyManager
	 * @param ontology
	 *            OWLOntology
	 * @param locationIRI
	 *            IRI
	 * 
	 * @author Guillermo Antonio Toro Bayona
	 */
	protected void saveOntology(OWLOntologyManager manager,
			OWLOntology ontology, IRI locationIRI) {
		try {
			// Save the ontology
			manager.saveOntology(ontology, new OWLXMLOntologyFormat(),
					locationIRI);
		} catch (OWLOntologyStorageException e) {
			// Error saving the ontology
			System.out.println("Error saving the ontology: " + e.getMessage());
		}
	}

	/**
	 * Populate the Map with the Sources and Roles in the ontology
	 * 
	 * @param manager
	 *            OWLOntologyManager
	 * @param dataFactory
	 *            OWLDataFactory
	 * @param ontology
	 *            OWLOntology
	 * @param ontologyIRI
	 *            IRI
	 * @param classOntologyClass
	 *            OWLClass classOntologyClass
	 * @param namePrefix
	 *            String namePrefix
	 * @param mapIndividuals
	 *            Map<String, OWLIndividual> mapIndividuals
	 * 
	 * @author Guillermo Antonio Toro Bayona
	 */
	private void initializeMaps(OWLOntologyManager manager,
			OWLDataFactory dataFactory, OWLOntology ontology, IRI ontologyIRI,
			OWLClass classOntologyClass, String namePrefix,
			Map<String, OWLIndividual> mapIndividuals) {
		// Name
		String className = classOntologyClass.getIRI().getFragment();
		// Create an individual
		OWLIndividual individualClass = dataFactory.getOWLNamedIndividual(IRI
				.create(ontologyIRI + HASH_SYMBOL + namePrefix + className));
		// Create the axiom
		OWLClassAssertionAxiom classAssertionAxiom = dataFactory
				.getOWLClassAssertionAxiom(classOntologyClass, individualClass);
		// Add the axiom to the ontology
		manager.addAxiom(ontology, classAssertionAxiom);
		// Put in the map
		mapIndividuals.put(HASH_SYMBOL + className, individualClass);
		// Look for subclasses method
		lookForSubClasses(manager, dataFactory, ontology, ontologyIRI,
				classOntologyClass, namePrefix, mapIndividuals);
	}

	/**
	 * Method that look for subclasses with recursion strategy
	 * 
	 * @param manager
	 *            OWLOntologyManager
	 * @param dataFactory
	 *            OWLDataFactory
	 * @param ontology
	 *            OWLOntology
	 * @param ontologyIRI
	 *            IRI ontologyIRI
	 * @param classOntologyClass
	 *            OWLClass classOntologyClass
	 * @param namePrefix
	 *            String namePrefix
	 * @param mapIndividuals
	 *            Map<String, OWLIndividual> mapIndividuals
	 * 
	 * @author Guillermo Antonio Toro Bayona
	 */
	public void lookForSubClasses(OWLOntologyManager manager,
			OWLDataFactory dataFactory, OWLOntology ontology, IRI ontologyIRI,
			OWLClass classOntologyClass, String namePrefix,
			Map<String, OWLIndividual> mapIndividuals) {
		// Get the subclasses
		Collection<OWLClassExpression> classExpressions = classOntologyClass
				.getSubClasses(ontology);
		// Iterate the class expressions
		for (OWLClassExpression oce : classExpressions) {
			// Get the subclass as a class and not like class expression
			classOntologyClass = oce.asOWLClass();
			// Name
			String className = classOntologyClass.getIRI().getFragment();
			// Create an individual
			OWLIndividual individualClass = dataFactory
					.getOWLNamedIndividual(IRI.create(ontologyIRI + HASH_SYMBOL
							+ namePrefix + className));
			// Create the axiom
			OWLClassAssertionAxiom classAssertionAxiom = dataFactory
					.getOWLClassAssertionAxiom(classOntologyClass,
							individualClass);
			// Add the axiom to the ontology
			manager.addAxiom(ontology, classAssertionAxiom);
			// Put in the map
			mapIndividuals.put(HASH_SYMBOL + className, individualClass);
			// Recursion
			this.lookForSubClasses(manager, dataFactory, ontology, ontologyIRI,
					classOntologyClass, namePrefix, mapIndividuals);
		}
	}

	/**
	 * Method that return a List with person and its roles based on the beans
	 * collection
	 * 
	 * @param beans
	 *            Collection of JobDataBean
	 * @return List<Person> persons
	 * 
	 * @author Guillermo Antonio Toro Bayona
	 */
	private List<Person> parseCollectionBeans(Collection<JobDataBean> beans) {
		// Create the list
		List<Person> persons = new ArrayList<Main.Person>();
		// Create the person
		Person person = null;
		// Loop
		for (JobDataBean bean : beans) {
			// Validate if the person is null or if the data read is for the
			// same previous person
			if (person == null
					|| !(person.getGivenName().equals(bean.getGivenName()) && person
							.getSurname().equals(bean.getSurname()))) {
				// Instantiate new person
				person = new Person();
				// Set birth year
				person.setBirthYear(bean.getBirthYear() == null ? null : bean
						.getBirthYear());
				// Set given name
				person.setGivenName(bean.getGivenName());
				// Set surname
				person.setSurname(bean.getSurname());
				// Set married surname
				person.setMarriedSurname(bean.getMarriedSurname() == null ? null
						: bean.getMarriedSurname());
				// Add person to the list
				persons.add(person);
			}
			// Validate the structure of the Role.
			// If at least one is null the role is rejected.
			if (!(bean.getYear() == null || bean.getSource() == null || bean
					.getOccupation() == null)) {
				// Create new role
				Role role = new Role();
				// Set the year
				role.setYear(bean.getYear());
				// Set the source
				role.setSource(bean.getSource());
				// Set the occupation. Replace white space with underscore
				// to fit the list of roles created.
				role.setOccupation(bean.getOccupation().replace(" ", "_"));
				// Add to the roles list
				person.getRoles().add(role);
			}
		}
		// Return the list of persons
		return persons;
	}

	/**
	 * Method that implements the lpad method for names
	 * 
	 * @param input
	 *            String with the name
	 * @param size
	 *            int with the limit of size
	 * @return String with lpad 0
	 */
	private static String lpadLeftForString(String input, int size) {
		// Length difference
		int difference = (String.valueOf(size).length() + 1) - input.length();
		// String buffer
		StringBuffer stringBuffer = new StringBuffer();
		// Loop
		for (int i = 1; i <= difference; i++)
			stringBuffer.append("0");
		// Add the number
		stringBuffer.append(input);
		// Return
		return stringBuffer.toString();
	}

	/**
	 * Class that represents a person
	 * 
	 * @author Guillermo Antonio Toro Bayona
	 */
	class Person {
		/**
		 * Integer birthYear
		 */
		private Integer birthYear;
		/**
		 * String givenName
		 */
		private String givenName;
		/**
		 * String surname
		 */
		private String surname;
		/**
		 * String marriedSurname
		 */
		private String marriedSurname;
		/**
		 * List with the roles
		 */
		private List<Role> roles;

		/**
		 * Default constructor
		 */
		public Person() {
			this.roles = new ArrayList<Main.Role>();
		}

		/**
		 * Setters and getters
		 */

		public Integer getBirthYear() {
			return birthYear;
		}

		public void setBirthYear(Integer birthYear) {
			this.birthYear = birthYear;
		}

		public String getGivenName() {
			return givenName;
		}

		public void setGivenName(String givenName) {
			this.givenName = givenName;
		}

		public String getSurname() {
			return surname;
		}

		public void setSurname(String surname) {
			this.surname = surname;
		}

		public String getMarriedSurname() {
			return marriedSurname;
		}

		public void setMarriedSurname(String marriedSurname) {
			this.marriedSurname = marriedSurname;
		}

		public List<Role> getRoles() {
			return roles;
		}

		public void setRoles(List<Role> roles) {
			this.roles = roles;
		}
	}

	/**
	 * Class that represents a Role
	 * 
	 * @author Guillermo Antonio Toro Bayona
	 */
	class Role {
		/**
		 * Integer year
		 */
		private Integer year;
		/**
		 * String source
		 */
		private String source;
		/**
		 * String occupation
		 */
		private String occupation;

		/**
		 * Setters and getters
		 */

		public Integer getYear() {
			return year;
		}

		public void setYear(Integer year) {
			this.year = year;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getOccupation() {
			return occupation;
		}

		public void setOccupation(String occupation) {
			this.occupation = occupation;
		}
	}
}
