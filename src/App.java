import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    String version = "0.02";

    public static void main(String[] args) {
        Renderer mainRenderer = new Renderer(System.getProperty("user.home")+"/render.png");
        if(args.length != 0) {
            mainRenderer = new Renderer(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        }
        mainRenderer.clear();
        try {
            mainRenderer.drawLineNaive(100, 100, 150, 100);
            mainRenderer.drawLineNaive(100, 100, 100, 150);
            mainRenderer.drawLineNaive(50, 100, 100, 100);
            mainRenderer.drawLineNaive(100, 50, 100, 100);

            mainRenderer.drawLineNaive(100, 100, 150, 120);
            mainRenderer.drawLineNaive(100, 100, 120, 150);

            mainRenderer.drawLineNaive(100, 100, 80, 150);
            mainRenderer.drawLineNaive(100, 100, 50, 120);

            mainRenderer.drawLineNaive(100, 100, 50, 80);
            mainRenderer.drawLineNaive(100, 100, 80, 50);

            mainRenderer.drawLineNaive(100, 100, 150, 80);
            mainRenderer.drawLineNaive(100, 100, 120, 50);
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