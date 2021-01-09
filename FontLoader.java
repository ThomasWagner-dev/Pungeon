import greenfoot.Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontLoader {

    /**
     * Loads a font as a Greenfoot.Font from a file
     * @param fontFile the file the font is stored in
     * @param size size in px of the font
     * @return a fabric new Font
     */
    public static Font loadFont(File fontFile, int size) {
        try {
            // Creates an awt font from the file as true type font (.ttf)
            java.awt.Font font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, fontFile);
            //generates an graphics environment for idk what, not my script, but scisneromams i'm just commenting this crap
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //registers the font in the graphics environment
            graphicsEnvironment.registerFont(font);
            //fetches the name of the font
            String name = font.getName();
            //creates a new Greenfootfont of the size and name
            return new Font(name, size);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //returns null if errors were thrown
        return null;
    }
}