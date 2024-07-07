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

package com.edoardoconti.mfs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

import com.edoardoconti.mfs.model.*;

/**
 * This class is used to control the activities of the modular furnishing configurator.
 */
public class Controller {
    private List<Furniture> furnitureList;
    private final List<Color> colorList;
    private FurnitureType furnitureTypeFactory;
    private ComponentFactory componentFactory;
    private Furniture activeFurniture;
    private Position activePosition;
    private Color activeColor;
    private Collection<Component> selectedComponents;
    private final DataService dataService;


    /**
     * Constructs a Controller instance, initializing it with a DataService.
     * This constructor sets up the initial state of the Controller by initializing the furniture list,
     * color list, and selected components collection. It fetches the available colors from the provided
     * DataService, which acts as a bridge to the data layer, allowing for operations such as fetching
     * available colors and interacting with furniture and components data.
     *
     * @param dataService The DataService instance to be used for data operations. It is expected to be
     *                    non-null and initialized. This service provides methods to interact with the
     *                    underlying data storage, such as fetching available colors, creating furniture,
     *                    and components, among others.
     */
    public Controller(DataService dataService) {
        Objects.requireNonNull(dataService, "DataService cannot be null");
        this.dataService = dataService;
        furnitureList = new LinkedList<>();
        colorList = dataService.getAvailableColors();
        selectedComponents = new HashSet<>();
    }


    /**
     * Sets the furniture factory to be used by the Controller.
     *
     * @param furnitureTypeFactory The FurnitureType instance to be used as the factory for creating
     *                             furniture objects. It is expected to be non-null and initialized.
     */
    public void setFurnitureFactory(FurnitureType furnitureTypeFactory) {
        this.furnitureTypeFactory = furnitureTypeFactory;
    }

    /**
     * Sets the component factory to be used by the Controller.
     *
     * @param componentFactory The ComponentFactory instance to be used as the factory for creating
     *                          component objects. It is expected to be non-null and initialized.
     */
    public void setComponentFactory(ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    public void setActiveColor(Color color) {
        activeColor = color;
    }

    public void setActivePosition(Position position) {
        activePosition = position;
    }

    /**
     * Sets the active furniture based on the provided name.
     *
     * @param name The name of the furniture to set as active. It is expected to be non-null and
     *             correspond to an existing furniture in the furniture list.
     * @throws IllegalArgumentException If the furniture with the provided name is not found in the
     *                                  furniture list.
     */
    public void setActiveFurniture(String name) {
        activeFurniture = furnitureList.stream()
                .filter(furniture -> furniture.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Furniture not found"));
    }

    /**
     * Creates a new furniture with the provided name, width, height, and module size.
     *
     * @param name       The name of the furniture to create. It is expected to be non-null and non-empty.
     * @param width      The width of the furniture to create. It is expected to be a positive integer.
     * @param height     The height of the furniture to create. It is expected to be a positive integer.
     * @param moduleSize The module size of the furniture to create. It is expected to be a positive integer.
     * @throws IllegalArgumentException If the name is empty, a furniture with the same name already exists,
     *                                  or the module size is not a positive integer.
     */
    public void createFurniture( String name, int width, int height, int moduleSize) {
        if(name.isEmpty())
            throw new IllegalArgumentException("Name is required");
        if(furnitureList.stream().anyMatch(furniture -> furniture.getName().equals(name)))
            throw new IllegalArgumentException("Furniture with the same name already exists");
        Furniture furniture = furnitureTypeFactory.create( name, width, height, moduleSize);
        furnitureList.add(furniture);
        dataService.createFurniture(furniture, furnitureTypeFactory);
    }

    /**
     * Creates a new component based on the active furniture, position, and color.
     *
     * @return The newly created component.
     */
    public Component createComponent() {
        Component component = componentFactory.create(activeFurniture.getModuleSize(), activePosition, activeColor);
        // call data service to create new component containing the new module
        activeFurniture.addComponent(component);
        dataService.createComponent(activeFurniture, furnitureTypeFactory, component);
        return component;
    }

    public Component joinComponents() {
        // TODO: implement joinComponents
        return null;
    }


    public Furniture getActiveFurniture() {
        return activeFurniture;
    }

    public List<Furniture> getFurnitures() {
        return furnitureList;
    }

    public List<Color> getColors() {
        return colorList;
    }

    public Position getActivePosition() {
        return activePosition;
    }

    public Color getActiveColor() {
        return activeColor;
    }

    /**
     * Exports the data to the specified file.
     *
     * @param file The file to export the data to. It is expected to be non-null and initialized.
     * @throws IOException If an error occurs while exporting the data.
     */
    public void exportData(File file) throws IOException {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(dataService.exportData());
            writer.close();
        }catch (IOException e) {
            throw new IOException("Error exporting data", e);
        }
    }

    /**
     * Imports the data from the specified file.
     *
     * @param file The file to import the data from. It is expected to be non-null and initialized.
     * @throws IOException If an error occurs while importing the data.
     */
    public void importData(File file) throws IOException {
        StringBuilder data = new StringBuilder();
        try (Stream<String> lines = Files.lines(file.toPath())) {
            lines.forEach(line -> data.append(line).append("\n"));
        } catch (IOException e) {
            throw new IOException("Error importing data", e);
        }
        furnitureList =  dataService.importData(data.toString());
    }
}
