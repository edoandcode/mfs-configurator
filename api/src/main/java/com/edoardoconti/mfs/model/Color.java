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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a color in RGB and Hexadecimal format.
 * This class provides methods to convert between RGB and Hexadecimal color formats.
 */
public final class Color implements Nameable {
    private final Map<String, Integer> rgb;
    private final String hex;
    private final String name;


    /**
     * Constructs a new Color object with the specified hexadecimal color string.
     *
     * @param hex the hexadecimal color string
     */
    public Color(String hex) {
        this(hex, hex.startsWith("#") ? hex : "#" + hex);
    }

    /**
     * Constructs a new Color object with the specified RGB values.
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     */
    public Color(int r, int g, int b) {
        this(r, g, b, Color.rgbToHex(r, g, b));
    }

    /**
     * Constructs a new Color object with the specified RGB values and name.
     *
     * @param r    the red component
     * @param g    the green component
     * @param b    the blue component
     * @param name the name of the color
     */
    public Color(int r, int g, int b, String name) {
        this.rgb = Map.of("r", r, "g", g, "b", b);
        this.hex = Color.rgbToHex(r, g, b);
        this.name = name;
    }

    /**
     * Constructs a new Color object with the specified hexadecimal color string and name.
     *
     * @param hex  the hexadecimal color string
     * @param name the name of the color
     */
    public Color(String hex, String name) {
        if (!hex.startsWith("#"))
            hex = "#" + hex;
        this.hex = hex;
        this.rgb = Color.hexToRgb(hex);
        this.name = name;
    }

    /**
     * Returns the hexadecimal representation of the color.
     *
     * @return the hexadecimal color string
     */
    public String getHex() {
        return hex;
    }

    /**
     * Returns the RGB representation of the color.
     *
     * @return a map containing the red, green, and blue components
     */
    public Map<String, Integer> getRgb() {
        return rgb;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Color{" +
                ", hex='" + hex + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    // static

    /**
     * Converts a hexadecimal color string to an RGB map.
     *
     * @param hex the hexadecimal color string
     * @return a map containing the red, green, and blue components
     */
    public static Map<String, Integer> hexToRgb(String hex) {
        Pattern pattern = Pattern.compile("^#?([a-fA-F\\d]{2})([a-fA-F\\d]{2})([a-fA-F\\d]{2})$");
        Matcher matcher = pattern.matcher(hex);
        if (matcher.matches()) {
            int r = Integer.parseInt(matcher.group(1), 16);
            int g = Integer.parseInt(matcher.group(2), 16);
            int b = Integer.parseInt(matcher.group(3), 16);
            return Map.of("r", r, "g", g, "b", b);
        }
        throw new IllegalArgumentException("Invalid hexadecimal color string: " + hex);
    }

    /**
     * Converts RGB values to a hexadecimal color string.
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @return the hexadecimal color string
     */
    public static String rgbToHex(int r, int g, int b) {
        return String.format("#%02x%02x%02x", r, g, b);
    }


}