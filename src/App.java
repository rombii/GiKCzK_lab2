import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    String version = "0.02";
    static Renderer.LineAlgo lineAlgo = Renderer.LineAlgo.NAIVE;
    public static void main(String[] args) {
        Renderer mainRenderer = new Renderer(System.getProperty("user.home")+"/render.png");
        if(args.length != 0) {
            mainRenderer = new Renderer(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            if (args.length == 4) {
                mainRenderer = new Renderer(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
            }
        }
        mainRenderer.clear();
        try {
            mainRenderer.drawLine(100, 100, 150, 100);
            mainRenderer.drawLine(100, 100, 100, 150);
            mainRenderer.drawLine(50, 100, 100, 100);
            mainRenderer.drawLine(100, 50, 100, 100);

            mainRenderer.drawLine(100, 100, 150, 120);
            mainRenderer.drawLine(100, 100, 150, 150);
            mainRenderer.drawLine(100, 100, 120, 150);

            mainRenderer.drawLine(100, 100, 80, 150);
            mainRenderer.drawLine(100, 100, 50, 150);
            mainRenderer.drawLine(100, 100, 50, 120);

            mainRenderer.drawLine(100, 100, 50, 80);
            mainRenderer.drawLine(100, 100, 50, 50);
            mainRenderer.drawLine(100, 100, 80, 50);

            mainRenderer.drawLine(100, 100, 150, 80);
            mainRenderer.drawLine(100, 100, 150, 50);
            mainRenderer.drawLine(100, 100, 120, 50);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            mainRenderer.save();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getVersion() {
        return this.version;
    }
}