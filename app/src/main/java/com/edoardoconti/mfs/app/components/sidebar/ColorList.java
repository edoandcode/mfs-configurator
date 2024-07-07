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

package com.edoardoconti.mfs.app.components.sidebar;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.edoardoconti.mfs.model.Color;

import com.edoardoconti.mfs.app.components.GUIComponent;
import com.edoardoconti.mfs.app.util.GUIUtils;

/**
 * A list of colors that the user can choose from.
 */
public class ColorList implements GUIComponent {

    private final ScrollPane root;
    private final VBox colorListWrapper;
    private final Color[] colors;
    private final Map<Color, Button> colorButtons;
    private Consumer<Color> handleColorChange;
    private Color selectedColor;

    public ColorList(Color ...colors) {
        this.root = new ScrollPane();
        this.colorListWrapper = new VBox();
        this.colorButtons = new HashMap<>();
        this.colors = colors;
        build();
        initialize();
    }

    private void initialize() {
        if(colors.length == 0)
            return;
        for (Color color : colors) {
            addColor(color);
        }
        selectColor(colors[0]);
    }

    private void build() {
        GUIUtils.addClasses(this, "color-list");
        root.setPrefHeight(160);
        root.setContent(colorListWrapper);
        colorListWrapper.setSpacing(5);
    }

    private void addColor(Color color) {
        var colorButton = new Button();
        colorButton.setMinWidth(140);
        GUIUtils.addClasses(colorButton, "color-list-button");
        var colorWrapper = new HBox();
        colorWrapper.setSpacing(10);
        colorWrapper.setAlignment(Pos.CENTER_LEFT);
        var colorLabel = new Label(color.getName());
        var colorBullet = new Circle(6);
        colorBullet.setFill(Paint.valueOf(color.getHex()));
        colorBullet.setStroke(Paint.valueOf("#444444"));
        colorWrapper.getChildren().addAll(colorBullet, colorLabel);
        colorButton.setGraphic(colorWrapper);
        colorButtons.put(color, colorButton);
        colorListWrapper.getChildren().add(colorButton);
        addButtonListener(colorButton, color);
    }

    private void addButtonListener(Button button, Color color) {
        button.setOnAction(e -> {
            selectColor(color);
        });
    }

    private void selectColor(Color color) {
        colorButtons.values().forEach(b -> b.getStyleClass().remove("selected"));
        colorButtons.get(color).getStyleClass().add("selected");
        selectedColor = color;
        if(handleColorChange != null)
            handleColorChange.accept(selectedColor);
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void onColorChange(Consumer<Color> callback) {
        handleColorChange = callback;
    }

    @Override
    public ScrollPane getRootNode() {
        return root;
    }
}
