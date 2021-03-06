import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Renderer {

    public enum LineAlgo { NAIVE, BRESENHAM, BRESENHAM_INT}

    private BufferedImage render;
    public int h = 200;
    public int w = 200;

    private final String filename;
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
            if (this.lineAlgo == LineAlgo.BRESENHAM) drawLineBresenham(x0, y0, x1, y1);
            if (this.lineAlgo == LineAlgo.BRESENHAM_INT) drawLineBresenhamInt(x0, y0, x1, y1);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
//            algorytm dda
//            int step = Math.max(Math.abs(dx),Math.abs(dy));
//            float xinc = dx/(float)step;
//            float yinc = dy/(float)step;
//            float xtemp = x0;
//            float ytemp = y0;
//            for(int i = 0; i < step; i++) {
//                drawPoint(x0, y0);
//                xtemp += xinc;
//                ytemp += yinc;
//                if(Math.abs(xtemp)%1 >= 0.5) {
//                    x0=(int) Math.ceil(xtemp);
//                }
//                else {
//                    x0=(int) Math.floor(xtemp);
//                }
//                if(Math.abs(ytemp)%1 >= 0.5) {
//                    y0=(int) Math.ceil(ytemp);
//                }
//                else {
//                    y0=(int) Math.floor(ytemp);
//                }
//            }

            float a = dy/(float)dx;
            float b = y0-(a*x0);
            while(x0 != x1){
                if(Math.abs(a)>1) {//duzy spadek/wzrost
                    float ytemp;
                    if(x0<x1) {//sprawdzanie w którą stronę linia jest rysowania zakładając punkt x0 jako początek układu
                        ytemp = (x0 + 1) * a + b;
                    }
                    else {
                        ytemp = (x0 - 1) * a + b;
                    }
                    if(y0 < ytemp){//dopełnianie linii aby nie było przerw
                        for (int y = y0; y < (int)ytemp; y++) {
                            drawPoint(x0, y);
                        }
                    } else {
                        for (int y = y0; y > (int)ytemp; y--) {
                            drawPoint(x0, y);
                        }
                    }
                }
                if(x0>x1) {//sprawdzanie w którą stronę rysujemy linie
                    x0--;
                } else {
                    x0++;
                }
                if((a*x0+b)%1 > 0.5){//zaokrąglenie do pełnego pixela
                    y0 = (int) Math.ceil(a*x0+b);
                } else {
                    y0 = (int) Math.floor(a*x0+b);
                }
                drawPoint(x0,y0);
            }
            drawPoint(x1,y1);

        }
    }

    public void drawLineBresenham(int x0, int y0, int x1, int y1) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);

        int dx = x1-x0;
        int dy = y1-y0;
        if(dx == 0) {
            for(int i = Math.min(y0,y1); i < Math.max(y0,y1); i++){//linia pionowa
                drawPoint(x0,i);
            }
        }
        if(dy == 0) {
            for(int i = Math.min(x0,x1); i < Math.max(x0,x1); i++){//linia pozioma
                drawPoint(i,y0);
            }
        }
        float derr = Math.abs(dy/(float)(dx));
        float err = 0;
        if(Math.abs(derr) == 1) {//linia gdzie a = 1 i b = 0;
            if(x0<x1){
                int x = x0;
                for (int y = y0; y <= y1; y++) {
                    drawPoint(x,y);
                    x++;
                }
                for (int y = y0; y >= y1; y--) {
                    drawPoint(x,y);
                    x++;
                }
            }
            if(x0>x1){
                int x = x0;
                for (int y = y0; y <= y1; y++) {
                    drawPoint(x,y);
                    x--;
                }
                for (int y = y0; y >= y1; y--) {
                    drawPoint(x,y);
                    x--;
                }
            }
        }
        if(derr<1) {
            int y = y0;
            if(x0<x1) {
                for (int x = x0; x <= x1; x++) {
                    render.setRGB(x, y, white);
                    err += derr;
                    if (err > 0.5) {
                        y += (y1 > y0 ? 1 : -1);
                        err -= 1.;
                    }
                }
            } else {
                for (int x = x0; x >= x1; x--) {
                    render.setRGB(x, y, white);
                    err += derr;
                    if (err > 0.5) {
                        y += (y1 > y0 ? 1 : -1);
                        err -= 1.;
                    }
                }
            }

        }else if(derr>1) {
            int x = x0;
            if(y0<y1) {
                for (int y = y0; y <= y1; y++) {
                    render.setRGB(x, y, white);
                    err += derr%1;
                    if (err > 0.5) {
                        x += (x1 > x0 ? 1 : -1);
                        err -= 1.;
                    }
                }
            } else {
                for (int y = y0; y >= y1; y--) {
                    render.setRGB(x, y, white);
                    err += derr%1;
                    if (err > 0.5) {
                        x += (x1 > x0 ? 1 : -1);
                        err -= 1.;
                    }
                }
            }
        }
    }// Oktany: 1,2,3,4,5,6,7,8

    public void drawLineBresenhamInt(int x0, int y0, int x1, int y1) {
        int dx = x1 - x0;
        int dy = y1 - y0;
        if(dx == 0) {
            for(int i = Math.min(y0,y1); i < Math.max(y0,y1); i++){//linia pionowa
                drawPoint(x0,i);
            }
        }
        if(dy == 0) {
            for(int i = Math.min(x0,x1); i < Math.max(x0,x1); i++){//linia pozioma
                drawPoint(i,y0);
            }
        } else {
            if(Math.abs(dy) < Math.abs(dx)) {//linia o wzroscie < 1
                int x = x0;
                int y = y0;
                if(x0 > x1) {
                    x = x1;
                    y = y1;
                    dx = x0 - x1;
                    dy = y0 - y1;
                }
                int yi = 1;
                if(dy < 0) {
                    yi = -1;
                    dy = dy*(-1);
                }
                int P = 2 * dy - dx;
                while(x != Math.max(Math.abs(x0),Math.abs(x1))) {
                    drawPoint(x, y);
                    x++;
                    if(P < 0) {
                        P = P + 2 * dy;
                    } else {
                        P = P + 2 * dy - 2 * dx;
                        y+=yi;
                    }
                }
            } else if(Math.abs(dy) > Math.abs(dx)) {// linia o wzroscie > 1
                int x = x0;
                int y = y0;
                if(y0 > y1) {
                    x = x1;
                    y = y1;
                    dx = x0 - x1;
                    dy = y0 - y1;
                }
                int xi = 1;
                if (dx < 0) {
                    xi = -1;
                    dx = dx*(-1);
                }
                int P = 2 * dx - dy;
                while(y != Math.max(Math.abs(y0),Math.abs(y1))) {
                    drawPoint(x, y);
                    y++;
                    if(P < 0) {
                        P = P + 2 * dx;
                    } else {
                        P = P + 2 * dx - 2 * dy;
                        x+=xi;
                    }
                }
            } else {
                while(x0 != x1) {// linia o wzroscie = 1
                    drawPoint(x0,y0);
                    if(x0 > x1)
                        x0--;
                    else
                        x0++;
                    if(y0 > y1)
                        y0--;
                    else
                        y0++;
                }
            }
        }
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