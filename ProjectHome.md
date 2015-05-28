Ontology-owl-java-API: Simple example using Ontology OWL API for Java. The source code works with an ontology file that contains a basic structure of one hierarchy classification.

The code reads the file as an ontology, and loads the ontology into memory. After that, reads a file with some data that is necessary to populate the ontology file to query it. The source code parses the data and creates some beans with that.

Then, it creates different axioms based on some rules of the data. Once all the different axioms are created (object properties, class axioms and so on), the new ontology is save in a new file and it is possible to query it with semantic queries to extract information from it.