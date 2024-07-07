package com.edoardoconti.mfs.utils;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.edoardoconti.mfs.model.Furniture;
import com.edoardoconti.mfs.model.Component;
import com.edoardoconti.mfs.model.Module;

public final class OntologyServiceUtils {

    private final OntModel model;
    private final String NS;

    public OntologyServiceUtils(OntModel model, String NS) {
        this.model = model;
        this.NS = NS;
    }

    public OntClass getClass(String className) {
        if(className.startsWith(NS))
            return model.getOntClass(className);
        return model.getOntClass(NS + className);
    }

    public OntProperty getProperty(String propertyName) {
        if(propertyName.startsWith(NS))
            return model.getOntProperty(propertyName);
        return model.getOntProperty(NS + propertyName);
    }

    public Individual getIndividual(String individualName) {
        if(individualName.startsWith(NS))
            return model.getIndividual(individualName);
        return model.getIndividual(NS + individualName);
    }



    public Individual getColorIndividual(String hexValue) {
        if(hexValue.startsWith("#"))
            hexValue = hexValue.substring(1);
        // get color with the corresponding hex value
        // if there are multiple colors with the same hex value, return the one with the shortest name
        List<QuerySolution> solution = SPARQLQuery("SELECT ?color WHERE { ?color rdf:type dbo:Colour . ?color dbo:colourHexCode \"" + hexValue +"\" . } GROUP BY ?color ORDER BY MIN(STR(?color)) LIMIT 1");
        if(solution.isEmpty())
            throw new IllegalArgumentException("No color found with the hex value " + hexValue);
        String colorURI = solution.getFirst().get("color").toString();
        return model.getIndividual(colorURI);
    }


    public final Function<Individual, BiConsumer<String, String>> dataPropertySetter = individual -> (propertyName, value) -> {
        OntProperty property = getProperty(propertyName);
        XSDDatatype dataType = getRangeDataType(property);
        individual.addProperty(property, ModelFactory.createOntologyModel().createTypedLiteral(value, dataType));
    };

    public final Function<Individual, BiConsumer<String, Individual>> objectPropertySetter = individual -> (propertyName, value) -> {
        OntProperty property = getProperty(propertyName);
        individual.addProperty(property, value);
    };

    public String generateURI(Furniture furniture) {
        return NS + formatURI("_" + furniture.getName());
    }

    public String generateURI(Module module, Component component) {
        return NS + formatURI("_" + module.toString() + component.toString());
    }

    public String generateURI(Component furnitureComponent, Furniture furniture) {
        return NS + formatURI("_" + furnitureComponent.toString() + "_" + furniture.getName());
    }



    public List<QuerySolution> SPARQLQuery(String query) {
        String prefixes = getPrefixes();
        Query q = QueryFactory.create(prefixes + query);
        QueryExecution qexec = QueryExecutionFactory.create(q, model);
        try (qexec) {
            ResultSet results = qexec.execSelect();
            return ResultSetFormatter.toList(results);
        }
    }


    /// private

    private XSDDatatype getRangeDataType(OntProperty property) {
        Resource range = property.getRange();
        List<XSDDatatype> xsdDatatypes = getAllXSDDatatypes();
        for (XSDDatatype xsdDatatype : xsdDatatypes) {
            if (xsdDatatype.getURI().equals(range.getURI())) {
                return xsdDatatype;
            }
        }
        return null;
    }

    private List<XSDDatatype> getAllXSDDatatypes() {
        List<XSDDatatype> xsdDatatypes = new ArrayList<>();
        Field[] fields = XSDDatatype.class.getDeclaredFields();
        for (Field field : fields) {
            if (XSDDatatype.class.isAssignableFrom(field.getType())) {
                try {
                    XSDDatatype datatype = (XSDDatatype) field.get(null);
                    xsdDatatypes.add(datatype);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("An error occurred while getting the XSD datatypes.", e);
                }
            }
        }
        return xsdDatatypes;
    }


    private String getPrefixes() {
        return String.join(
                " ",
                model.getNsPrefixMap().entrySet().stream()
                        .map(entry -> "PREFIX " + entry.getKey() + ": <" + entry.getValue() + ">\n")
                        .toArray(String[]::new)
        );
    }

    private String parseXSDDataType(String xsdDataTypeString) {
        return xsdDataTypeString.substring(0, xsdDataTypeString.indexOf("^^")).replace("\"", "");
    }

    private String formatURI(String stringName) {
        return stringName.replace("{", "_").replace("}", "_").replace("#", "").replaceAll("\\s","").toUpperCase();
    }

}
