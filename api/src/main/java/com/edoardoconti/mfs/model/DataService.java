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

package com.edoardoconti.mfs.model;

import java.util.List;


/**
 * An interface for managing data related to furniture and its components.
 * Provides methods for loading, creating, and removing furniture and components, as well as retrieving available colors.
 */
public interface DataService {

    /**
     * Creates a new furniture item in the data source.
     * This method is responsible for adding a new furniture item, defined by the {@code furniture} parameter,
     * and categorizing it under a specific type, as defined by the {@code furnitureType} parameter. The method
     * ensures that the furniture item is properly created and stored within the data source, allowing for
     * subsequent retrieval and manipulation.
     *
     * @param furniture The {@link Furniture} object representing the furniture item to be created. It must not be null.
     * @param furnitureType The {@link FurnitureType} that categorizes the furniture item. It must not be null.
     */
    void createFurniture(Furniture furniture, FurnitureType furnitureType);


    /**
     * Creates a new component and associates it with a specific furniture item and type.
     * This method is responsible for adding a new component, defined by the {@code component} parameter,
     * to a furniture item, identified by the {@code furniture} parameter, and categorizing it under a
     * specific type, as defined by the {@code furnitureType} parameter. The method ensures that the
     * component is properly created and associated with the furniture item within the data source,
     * allowing for subsequent retrieval and manipulation.
     *
     * @param furniture The {@link Furniture} object representing the furniture item to which the component will be added. It must not be null.
     * @param furnitureType The {@link FurnitureType} that categorizes the furniture item. It must not be null.
     * @param component The {@link Component}<{@link Module}> object representing the component to be created and added. It must not be null.
     */
    void createComponent(Furniture furniture, FurnitureType furnitureType, Component component);


    void removeComponent(Furniture furniture, Component component);


    List<Color> getAvailableColors();

    /**
     * Exports the list of all furniture from the data source.
     */
    String exportData();


    List<Furniture> importData(String data);
}
