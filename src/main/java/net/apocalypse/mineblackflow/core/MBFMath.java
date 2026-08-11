package net.apocalypse.mineblackflow.core;

public class MBFMath {
    public static float[] splitRGB(int rgb){
        float r = (rgb >> 16) & 0xFF;
        float g = (rgb >> 8) & 0xFF;
        float b = rgb & 0xFF;
        return new float[]{r/255, g/255, b/255};
    }
}
