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

import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void hexConstructorWithHash_shouldCreateColorWithCorrectHex() {
        Color color = new Color("#ff0000");
        assertEquals("#ff0000", color.getHex());
    }

    @Test
    void hexConstructorWithoutHash_shouldCreateColorWithCorrectHex() {
        Color color = new Color("ff0000");
        assertEquals("#ff0000", color.getHex());
    }

    @Test
    void rgbConstructor_shouldCreateColorWithCorrectRGB() {
        Color color = new Color(255, 0, 0);
        assertEquals(Map.of("r", 255, "g", 0, "b", 0), color.getRgb());
    }

    @Test
    void hexToRgbConversion_withValidHex_shouldReturnCorrectRGB() {
        assertEquals(Map.of("r", 255, "g", 0, "b", 0), Color.hexToRgb("#ff0000"));
    }

    @Test
    void rgbToHexConversion_withValidRGB_shouldReturnCorrectHex() {
        assertEquals("#ff0000", Color.rgbToHex(255, 0, 0));
    }

    @Test
    void hexToRgbConversion_withInvalidHex_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Color.hexToRgb("zzzzzz"));
    }

    @Test
    void constructorWithHexAndName_shouldSetBothPropertiesCorrectly() {
        Color color = new Color("#ff0000", "Bright Red");
        assertEquals("#ff0000", color.getHex());
        assertEquals("Bright Red", color.getName());
    }

    @Test
    void constructorWithRGBAndName_shouldSetAllPropertiesCorrectly() {
        Color color = new Color(255, 0, 0, "Bright Red");
        assertEquals(Map.of("r", 255, "g", 0, "b", 0), color.getRgb());
        assertEquals("#ff0000", color.getHex());
        assertEquals("Bright Red", color.getName());
    }

    @Test
    void toString_shouldReturnStringRepresentationIncludingHexAndName() {
        Color color = new Color("#ff0000", "Bright Red");
        String expected = "Color{, hex='#ff0000', name='Bright Red'}";
        assertEquals(expected, color.toString());
    }
}