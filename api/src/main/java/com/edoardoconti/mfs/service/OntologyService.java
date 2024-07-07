/*
 * MIT License
 *
 * Copyright (c) 2024 Edoardo Conti
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.edoardoconti.mfs.service;



import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.ValidityReport;

import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.Callable;

import com.edoardoconti.mfs.model.*;
import com.edoardoconti.mfs.model.Module;
import com.edoardoconti.mfs.model.shelvingsystem.ShelvingModule;
import com.edoardoconti.mfs.model.shelvingsystem.ShelvingSystem;

import com.edoardoconti.mfs.utils.OntologyServiceUtils;

/**
 * A service that provides methods to interact with the ontology.
 */
public class OntologyService implements DataService {

    private final String ontologySource = "https://raw.githubusercontent.com/edoandcode/modular-furnishing-system-ontology/main/modular-furnishing-system-ontology.rdf";
    private final OntModel model ;
    private final String NS;
    private final OntologyServiceUtils utils;

    public OntologyService() {
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        handleException(() -> model.read(ontologySource));
        this.NS = model.getNsPrefixURI("mfs");
        this.utils = new OntologyServiceUtils(model, NS);
    }

    @Override
    public void createFurniture(Furniture furniture, FurnitureType furnitureType) {
        // create new furniture
        switch (furnitureType) {
            case SHELVING -> createShelvingFurniture((ShelvingSystem) furniture);
        };
    }

    @Override
    public void createComponent(Furniture furniture, FurnitureType furnitureType,  Component component) {
        OntClass componentClass = utils.getClass("FurnitureComponent");
        Individual newComponent = componentClass.createIndividual(utils.generateURI(component, furniture));
        // set object properties: partOf, hasModule
        var setComponentObjectProperty = utils.objectPropertySetter.apply(newComponent);
        setComponentObjectProperty.accept("isPartOf", utils.getIndividual(utils.generateURI(furniture)));
        for (Module module : component.getModules()) {
            setComponentObjectProperty.accept("hasModule", createModule(module, component, furniture, furnitureType));
        }
    }

    @Override
    public void removeComponent(Furniture furniture, Component component) {
        // TODO: remove component
        throw new UnsupportedOperationException("This data service not support removing components.");
    }


    @Override
    public List<Color> getAvailableColors() {
        List<QuerySolution> solutions = utils.SPARQLQuery("SELECT ?colorName ?hexValue WHERE { ?color rdf:type dbo:Colour . ?color dbp:title ?colorName . ?color dbo:colourHexCode ?hexValue . }");
        return solutions.stream()
                .map(solution -> {
                    String hexValue = solution.get("hexValue").toString();
                    String colorName = solution.get("colorName").toString();
                    return new Color(hexValue, colorName);
                })
                .toList();
    }


    @Override
    public String exportData() {
        validateModel();
        StringWriter stringWriter = new StringWriter();
        model.write(stringWriter, "RDF/XML");
        return stringWriter.toString();
    }

    @Override
    public List<Furniture> importData(String data) {
        throw new UnsupportedOperationException("This data service not support importing data.");
    }

    // private

    private void createShelvingFurniture(ShelvingSystem furniture) {
        OntClass furnitureClass = utils.getClass( "Shelving");
        Individual newFurniture = furnitureClass.createIndividual(utils.generateURI(furniture));
        // set properties: name, moduleSize, with, height
        var setProperty = utils.dataPropertySetter.apply(newFurniture);
        setProperty.accept("name", furniture.getName());
        setProperty.accept("moduleSize", Integer.toString(furniture.getModuleSize()));
        setProperty.accept("width", Integer.toString(furniture.getWidth()));
        setProperty.accept("height", Integer.toString(furniture.getHeight()));
    }

    private Individual createModule(Module module, Component component, Furniture furniture, FurnitureType furnitureType) {
        // create new module
        return switch (furnitureType) {
            case SHELVING -> createShelvingModule((ShelvingModule) module, component, furniture);
        };
    }

    private Individual createShelvingModule(ShelvingModule module, Component component, Furniture furniture) {
        OntClass moduleClass = utils.getClass("ShelvingModule");
        Individual newModule = moduleClass.createIndividual(utils.generateURI(module, component));
        // get component
        Individual componentIndividual = utils.getIndividual(utils.generateURI(component, furniture));
        // get color
        Individual colorIndividual = utils.getColorIndividual(module.getColor().getHex());
        // set data properties: size, hasDoor, isBackClosed
        var setDataProperty = utils.dataPropertySetter.apply(newModule);
        setDataProperty.accept("size", Integer.toString(module.getSize()));
        setDataProperty.accept("hasDoor", Boolean.toString(module.hasDoor()));
        setDataProperty.accept("isBackClosed", Boolean.toString(module.isBackClosed()));
        // set object properties: color, position
        var setObjectProperty = utils.objectPropertySetter.apply(newModule);
        setObjectProperty.accept("color", colorIndividual);
        setObjectProperty.accept("isModuleOf", componentIndividual);
        return newModule;
    }

    private void validateModel() {
        var infModel = ModelFactory.createRDFSModel(model);
        ValidityReport validity = infModel.validate();
        if (!validity.isValid())
            throw new RuntimeException("Model is not valid");
    }

    private <T> void handleException(Callable<T> callable) {
        try {
            callable.call();
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while working with the ontology. Original exception: " + e.getClass().getSimpleName(), e);
        }
    }

}
