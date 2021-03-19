import greenfoot.GreenfootImage;
import greenfoot.util.GraphicsUtilities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.CancellationException;

public class Utils
{
    /**
     * Draws the text vertically centered around y, and horizontally centered around x
     * @param gfi  The GreenfootImage to center the text on
     * @param text The text to draw
     * @param x    the x coord to center around, use negative (out of bounds) values to place in middle of image
     * @param y    the y coord to center around, use negative (out of bounds) values to place in middle of image
     */
    public static void drawCenteredText(GreenfootImage gfi, Color color, Font font, String text, int x, int y)
    {
        drawText(gfi, color, font, text, x, y, VerticalAlignment.CENTER, HorizontalAlignment.CENTER);
    }
    /**
     * Draws the text vertically centered around y, and right justified at x
     * @param gfi The GreenfootImage to draw the text on
     * @param text The text to draw
     * @param x    the x coord to center around, use negative (out of bounds) values to place in middle of image
     * @param y    the y coord to center around, use negative (out of bounds) values to place in middle of image
     */
    public static void drawRightCenteredText(GreenfootImage gfi, Color color, Font font, String text, int x, int y)
    {
        drawText(gfi, color, font, text, x, y, VerticalAlignment.CENTER, HorizontalAlignment.RIGHT);
    }

    /**
     * @param gfi  The GreenfootImage to draw the text on
     * @param text The text to draw
     * @param x    the x coord to draw around, use negative (out of bounds) values to place in middle of image
     * @param y    the y coord to draw around, use negative (out of bounds) values to place in middle of image
     */
    public static void drawText(GreenfootImage gfi, Color color, Font font, String text, int x, int y, VerticalAlignment verticalAlignment, HorizontalAlignment horizontalAlignment)
    {
        drawText(gfi.getAwtImage(), color, font, text, x, y, verticalAlignment, horizontalAlignment);
    }

    /**
     * @param bfi  The BufferedImage to draw the text on
     * @param text The text to draw
     * @param x    the x coord to draw around, use negative (out of bounds) values to place in middle of image
     * @param y    the y coord to draw around, use negative (out of bounds) values to place in middle of image
     */
    public static void drawText(BufferedImage bfi, Color color, Font font, String text, int x, int y, VerticalAlignment verticalAlignment, HorizontalAlignment horizontalAlignment)
    {
        Graphics2D graphics = bfi.createGraphics();
        if (graphics == null)
        {
            System.err.println("No valid graphics");
            return;
        }

        graphics.setColor(color);
        graphics.setFont(font);

        if (x < 0 || x >= bfi.getWidth())
        {
            x = bfi.getWidth() / 2;
        }
        if (y < 0 || y >= bfi.getHeight())
        {
            y = bfi.getHeight() / 2;
        }

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        String[] lines = GraphicsUtilities.splitLines(text);
        FontMetrics fontMetrics = graphics.getFontMetrics(font);
        int height = fontMetrics.getHeight();
        if(verticalAlignment == VerticalAlignment.CENTER)
        {
            y -= (height * (lines.length / 2));
        }
        if (verticalAlignment == VerticalAlignment.BOTTOM)
        {
            y -= height * lines.length;
        }
        y += (height / 4);
        for (int i = 0; i < lines.length; i++)
        {
            int modX = x;
            if(horizontalAlignment == HorizontalAlignment.CENTER)
            {
                modX = x - (fontMetrics.stringWidth(lines[i]) / 2);
            }
            if(horizontalAlignment == HorizontalAlignment.RIGHT)
            {
                modX = x - fontMetrics.stringWidth(lines[i]);
            }
            graphics.drawString(lines[i], modX, y + i * height);
        }

        graphics.dispose();
    }

    public static int[] getStringDimensions(java.awt.Font font, String text) {
        Graphics2D graphics = (new GreenfootImage("Invis.png")).getAwtImage().createGraphics();
        if (graphics == null)
        {
            return new int[] {0,0};
        }

        graphics.setFont(font);

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        String[] lines = GraphicsUtilities.splitLines(text);
        FontMetrics fontMetrics = graphics.getFontMetrics(font);
        int height = fontMetrics.getHeight();
        int width = Arrays.asList(lines).stream().map(fontMetrics::stringWidth).max((a, b) -> a - b).orElse(0);
        return new int[] {width, height};
    }

    enum VerticalAlignment
    {
        TOP,
        CENTER,
        BOTTOM
    }

    enum HorizontalAlignment
    {
        LEFT,
        CENTER,
        RIGHT
    }
}