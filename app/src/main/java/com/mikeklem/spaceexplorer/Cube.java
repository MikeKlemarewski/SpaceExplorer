package com.mikeklem.spaceexplorer;

import org.apache.commons.lang3.ArrayUtils;


/**
 * Created by cjr on 6/18/14.
 */
public class Cube {

    float[] frontTopLeft;
    float[] frontTopRight;
    float[] frontBottomLeft;
    float[] frontBottomRight;
    float[] backTopLeft;
    float[] backTopRight;
    float[] backBottomLeft;
    float[] backBottomRight;
    float[] coordinates;
    float[] colors;

    public Cube (float x, float y, float z, float size, float temperature) {
        this.frontTopLeft = new float[] {
                (x - size), (y + size) , (z + size)
        };
        this.frontTopRight = new float[] {
                (x + size), (y + size) , (z + size)
        };
        this.frontBottomLeft = new float[] {
                (x - size), (y - size) , (z + size)
        };
        this.frontBottomRight = new float[] {
                (x + size), (y - size) , (z + size)
        };
        this.backTopLeft = new float[] {
                (x - size), (y + size) , (z - size)
        };
        this.backTopRight = new float[] {
                (x + size), (y + size) , (z - size)
        };
        this.backBottomLeft = new float[] {
                (x - size), (y - size) , (z - size)
        };
        this.backBottomRight = new float[] {
                (x + size), (y - size) , (z - size)
        };

        float[][] chords = new float[][]{
            // Front face
            frontTopLeft, frontBottomLeft, frontTopRight,
            frontBottomLeft, frontBottomRight, frontTopRight,

            // Right face
            frontTopRight, frontBottomRight, backTopRight,
            frontBottomRight, backBottomRight, backTopRight,

            // Back face
            backTopRight, backBottomRight, backTopLeft,
            backBottomRight, backBottomLeft, backTopLeft,

            // Left Face
            backTopLeft, backBottomLeft, frontTopLeft,
            backBottomLeft, frontBottomLeft, frontTopLeft,

            // Top face
            backTopLeft, frontTopLeft, backTopRight,
            frontTopLeft, frontTopRight, backTopRight,

            // Bottom face
            backBottomRight, frontBottomRight, backBottomLeft,
            frontBottomRight, frontBottomLeft, backBottomLeft
        };

        this.coordinates = addAllArrays(chords);

        float[] rgb = kToRGBA(temperature, 1.0f);
        float[][] tmpColors = new float[][]{};
        for (int i = 0; i < 36; i++) {
            tmpColors = ArrayUtils.addAll(tmpColors, rgb);
        }

        this.colors = addAllArrays(tmpColors);
    }

    public static final float[] CUBE_NORMALS = new float[] {
            // Front face
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            // Right face
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            // Back face
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,

            // Left face
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,

            // Top face
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            // Bottom face
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f
    };

    public float[] addAllArrays(float[][] arrays) {
        float[] result = new float[] {};
        for (int i = 0; i < arrays.length; i++) {
            result = ArrayUtils.addAll(result, arrays[i]);
        }
        return result;
    };

    public float[] getCoordinates() {
        return this.coordinates;
    };

    public float[] getColors() {
        return this.colors;
    };

    /**
     * Do a rough estimation of kelvin temperature to RGB
     * http://www.tannerhelland.com/4435/convert-temperature-rgb-algorithm-code/
     */
    public float[] kToRGBA(float temp, float brightness){

        float temperature = temp / 100f;
        double red;
        double green;
        double blue;

        if (temperature <= 66){
            red = 255;
        } else {
            red = temperature - 60;
            red = 329.698727466 * Math.pow(red, -0.1332047592);
            if (red < 0){
                red = 0;
            }
            if (red > 255){
                red = 255;
            }
        }

        if (temperature <= 66){
            green = temperature;
            green = 99.4708025861 * Math.log(green) - 161.1195681661;
            if (green < 0 ) {
                green = 0;
            }
            if (green > 255) {
                green = 255;
            }
        } else {
            green = temperature - 60;
            green = 288.1221695283 * Math.pow(green, -0.0755148492);
            if (green < 0 ) {
                green = 0;
            }
            if (green > 255) {
                green = 255;
            }
        }

        if (temperature >= 66){
            blue = 255;
        } else {
            if (temperature <= 19){
                blue = 0;
            } else {
                blue = temperature - 10;
                blue = 138.5177312231 * Math.log(blue) - 305.0447927307;
                if (blue < 0){
                    blue = 0;
                }
                if (blue > 255){
                    blue = 255;
                }
            }
        }

        float[] result = new float[]{
            Math.round(red),
            Math.round(green),
            Math.round(blue),
            brightness
        };

        return result;

    }

    public void move(float x, float y, float z) {
        this.frontTopLeft = new float[] {
            (this.frontTopLeft[0] + x), (this.frontTopLeft[1] + y) , (this.frontTopLeft[2] + z)
        };
        this.frontTopRight = new float[] {
            (this.frontTopRight[0] + x), (this.frontTopRight[1] + y) , (this.frontTopRight[2] + z)
        };
        this.frontBottomLeft = new float[] {
            (this.frontBottomLeft[0] + x), (this.frontBottomLeft[1] + y) , (this.frontBottomLeft[2] + z)
        };
        this.frontBottomRight = new float[] {
            (this.frontBottomRight[0] + x), (this.frontBottomRight[1] + y), (this.frontBottomRight[2] + z)
        };
        this.backTopLeft = new float[] {
            (this.backTopLeft[0] + x), (this.backTopLeft[1] + y), (this.backTopLeft[2] + z)
        };
        this.backTopRight = new float[] {
            (this.backTopRight[0] + x), (this.backTopRight[1] + y), (this.backTopRight[2] + z)
        };
        this.backBottomLeft = new float[] {
            (this.backBottomLeft[0] + x), (this.backBottomLeft[1] + y), (this.backBottomLeft[2] + z)
        };
        this.backBottomRight = new float[] {
            (this.backBottomRight[0] + x), (this.backBottomRight[1] + y), (this.backBottomRight[2] + z)
        };

        float[][] coords = new float[][]{
                // Front face
                frontTopLeft, frontBottomLeft, frontTopRight,
                frontBottomLeft, frontBottomRight, frontTopRight,

                // Right face
                frontTopRight, frontBottomRight, backTopRight,
                frontBottomRight, backBottomRight, backTopRight,

                // Back face
                backTopRight, backBottomRight, backTopLeft,
                backBottomRight, backBottomLeft, backTopLeft,

                // Left Face
                backTopLeft, backBottomLeft, frontTopLeft,
                backBottomLeft, frontBottomLeft, frontTopLeft,

                // Top face
                backTopLeft, frontTopLeft, backTopRight,
                frontTopLeft, frontTopRight, backTopRight,

                // Bottom face
                backBottomRight, frontBottomRight, backBottomLeft,
                frontBottomRight, frontBottomLeft, backBottomLeft
        };

        this.coordinates = addAllArrays(coords);
    }
}
