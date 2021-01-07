import greenfoot.Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontLoader
{

    public static Font loadFont(File fontFile, int size)
    {
        try
        {
            java.awt.Font font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(font);
            String name = font.getName();
            return new Font(name, size);
        } catch (FontFormatException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}