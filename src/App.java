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
            if(args.length == 4) {
                mainRenderer = new Renderer(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
            }
        }
        System.out.println(args[1] + " - " + mainRenderer.h + " " + args[2] + " - " + mainRenderer.w);

        mainRenderer.clear();
        try {
            mainRenderer.drawLine(100,100,110,80);
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