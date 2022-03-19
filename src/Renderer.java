import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Renderer {

    public enum LineAlgo { NAIVE, BRESENHAM, BRESENHAM_INT; }

    private BufferedImage render;
    public int h = 200;
    public int w = 200;

    private String filename;
    private LineAlgo lineAlgo = LineAlgo.NAIVE;

    public Renderer(String filename) {
        render = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        this.filename = filename;
    }

    public Renderer(String filename, int h, int w) {
        render = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        this.filename = filename;
        this.h = h;
        this.w = w;
    }

    public void drawPoint(int x, int y) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);
        render.setRGB(x, y, white);
    }

    public void drawLine(int x0, int y0, int x1, int y1, LineAlgo lineAlgo) {
        try {
            if (lineAlgo == LineAlgo.NAIVE) drawLineNaive(x0, y0, x1, y1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if(lineAlgo == LineAlgo.BRESENHAM) drawLineBresenham(x0, y0, x1, y1);
        if(lineAlgo == LineAlgo.BRESENHAM_INT) drawLineBresenhamInt(x0, y0, x1, y1);
    }

    public void drawLineNaive(int x0, int y0, int x1, int y1) throws Exception {
        if(x0 > 200 || y0 > 200 || x1 > 200 || y1 > 200) {
            throw new Exception("Współrzędne poza zakresem");
        }
        int dx = x1 - x0;
        int dy = y1 - y0;
        if (dx == 0) { // linia pionowa
            while (y0 != y1) {
                if (y0 < y1) {
                    y0++;
                } else {
                    y0--;
                }
                drawPoint(x0, y0);
            }
        }
        else if (dy == 0) { // linia pozioma
            while (x0 != x1) {
                if (x0 < x1) {
                    x0++;
                } else {
                    x0--;
                }
                drawPoint(x0, y0);
            }
        }
        else { // ukosna
            float a = (float) dy / (float) dx;
            float b = y0 - (a*x0);
            if(Math.abs(a) >= 1) { //wzrost maly/duzy
                drawPoint(x0,y0);
                while (x0 != x1) {
                    if ((a * x0 + b) % 1 < 0.5) {
                        if (x0 < x1) {
                            y1 = (int) Math.floor(a * (x0 + 1) + b);
                        } else {
                            y1 = (int) Math.floor(a * (x0 - 1) + b);
                        }
                        for (int i = y0; i < y1; i++) {
                            drawPoint(x0, i);
                        }
                        for (int i = y0; i > y1; i--) {
                            drawPoint(x0, i);
                        }
                    } else {
                        if (x0 < x1) {
                            y1 = (int) Math.ceil(a * (x0 + 1) + b);
                        } else {
                            y1 = (int) Math.ceil(a * (x0 - 1) + b);
                        }
                        for (int i = y0; i < y1; i++) {
                            drawPoint(x0, i);
                        }
                        for (int i = y0; i > y1; i--) {
                            drawPoint(x0, i);
                        }
                    }
                    y0 = y1;
                    if (x0 < x1) {
                        x0++;
                    } else {
                        x0--;
                    }
                    drawPoint(x0, y0);
                }
            }
            else {
                //y0++;
                drawPoint(x0,y0);
                while (y0 != y1) {
                    if ((y0-b)/a % 1 < 0.5) {
                        x1 = (int) Math.floor((y0-b)/a);
                        for (int i = x0; i < x1; i++) {
                            drawPoint(i, y0);
                        }
                        for (int i = x0; i > x1; i--) {
                            drawPoint(i, y0);
                        }
                    } else {
                        x1 = (int) Math.ceil((y0-b)/a);
                        for (int i = x0; i < x1; i++) {
                            drawPoint(i, y0);
                        }
                        for (int i = x0; i > x1; i--) {
                            drawPoint(i, y0);
                        }
                    }
                    x0 = x1;
                    if (y0 < y1) {
                        y0++;
                    } else {
                        y0--;
                    }
                    drawPoint(x0, y0);
                    //render.setRGB(x0,y0,255 | (0 << 8) | (255 << 16) | (255 << 24));
                }
            }
        }
        render.setRGB(100,100,128 | (0 << 8) | (255 << 16) | (255 << 24));
    }

    public void drawLineBresenham(int x0, int y0, int x1, int y1) {
        // TODO: zaimplementuj
    }

    public void drawLineBresenhamInt(int x0, int y0, int x1, int y1) {
        // TODO: zaimplementuj
    }

    public void save() throws IOException {
        File outputfile = new File(filename);
        render = Renderer.verticalFlip(render);
        ImageIO.write(render, "png", outputfile);
    }

    public void clear() {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int black = 0 | (0 << 8) | (0 << 16) | (255 << 24);
                render.setRGB(x, y, black);
            }
        }
    }

    public static BufferedImage verticalFlip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage flippedImage = new BufferedImage(w, h, img.getColorModel().getTransparency());
        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return flippedImage;
    }
}