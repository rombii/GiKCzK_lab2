import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.Line;

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

    public Renderer(String filename, Integer h, Integer w) {
        render = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        this.filename = filename;
        this.h = h;
        this.w = w;
    }

    public Renderer(String filename, Integer h, Integer w, String lineAlgo) {
        render = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        this.filename = filename;
        this.h = h;
        this.w = w;
        if(lineAlgo.equals("LINE_BRESENHAM"))
            this.lineAlgo = LineAlgo.BRESENHAM;
        if(lineAlgo.equals("LINE_BRESENHAM_INT"))
            this.lineAlgo = LineAlgo.BRESENHAM_INT;
    }

    public void drawPoint(int x, int y) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);
        render.setRGB(x, y, white);
    }

    public void drawLine(int x0, int y0, int x1, int y1) {
        try {
            if (this.lineAlgo == LineAlgo.NAIVE) drawLineNaive(x0, y0, x1, y1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if(this.lineAlgo == LineAlgo.BRESENHAM) drawLineBresenham(x0, y0, x1, y1);
        if(this.lineAlgo == LineAlgo.BRESENHAM_INT) drawLineBresenhamInt(x0, y0, x1, y1);
    }

    public void drawLineNaive(int x0, int y0, int x1, int y1) throws Exception {
        if(x0 > this.w || y0 > this.h || x1 > this.w || y1 > this.h) {
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
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);

        int dx = x1-x0;
        int dy = y1-y0;
        float derr = Math.abs(dy/(float)(dx));
        float err = 0;

        int y = y0;

        for (int x=x0; x<=x1; x++) {
            render.setRGB(x, y, white);
            err += derr;
            if (err > 0.5) {
                y += (y1 > y0 ? 1 : -1);
                err -= 1.;
            }
        } // Oktanty:
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
                render.setRGB(x, y, (0) | (255 << 24));
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