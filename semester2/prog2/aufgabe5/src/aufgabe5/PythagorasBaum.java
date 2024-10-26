package aufgabe5;

public class PythagorasBaum {
    private static final double min = 0.001;

    public static void main(String[] args) {
        StdDraw.show(0); //beschleunigte Ausgabe

        StdDraw.setCanvasSize(600, 600);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenRadius(0.002);

        //Variante 1: Konstanter Neigungswinkel
        PythagorasBaum1(0.5, 0, 31.4, 0.13);

        //Variante 2: Zufälliger Neigungswinkel
        //PythagorasBaum2(0.5, 0, 0, 0.11);

        StdDraw.show(0);
    }

    //Variante 1: Konstanter Neigungswinkel
    static void PythagorasBaum1(double x, double y, double angel, double size) {
        if (size < min) {
            StdDraw.setPenColor(StdDraw.GREEN);
        } else {
            StdDraw.setPenColor(StdDraw.ORANGE);
        }

        double delta = 0.5; //Winkel, um den jedes nachfolgende Quadrat gegenüber dem vorherigen gedreht wird

        double c = size * Math.cos(angel);
        double s = size * Math.sin(angel);

        double xB = x + c;
        double yB = y + s;
        double xC = x + c - s;
        double yC = y + s + c;
        double xD = x - s;
        double yD = y + c;

        double u = size * Math.cos(delta);
        double v = size * Math.sin(delta);
        double xE = xD + u * Math.cos(angel + delta);
        double yE = yD + u * Math.sin(angel + delta);

        //StdDraw.line(x, y, xB, yB); // A -> B
        StdDraw.line(xB, yB, xC, yC); // B -> C
        //StdDraw.line(xC, yC, xD, yD); // C -> D
        StdDraw.line(xD, yD, x, y); // D -> A

        if (size >= min) {
            PythagorasBaum1(xD, yD, angel + delta, u); // linkes Quadrat
            PythagorasBaum1(xE, yE, angel + delta - Math.PI/2, v); // rechtes Quadrat; winkel modifizieren
        }
    }

    //Variante 2: Zufälliger Neigungswinkel
    static void PythagorasBaum2(double x, double y, double angel, double size) {
        if (size < min) {
            StdDraw.setPenColor(StdDraw.GREEN);
        } else {
            StdDraw.setPenColor(StdDraw.ORANGE);
        }

        double height = Math.random() * size + size;
        double gamma = Math.toRadians(Math.random() * 45 + 10); //Zufälliger Neigungswinkel; von grad in bogenmaß

        double c = size * Math.cos(angel);
        double s = size * Math.sin(angel);
        double r = height * Math.sin(angel);
        double h = height * Math.cos(angel);

        double xB = x + c;
        double yB = y + s;
        double xC = x + c - r;
        double yC = y + s + h;
        double xD = x - r;
        double yD = y + h;

        double u = size * Math.cos(gamma);
        double v = size * Math.sin(gamma);
        double xE = xD + u * Math.cos(angel + gamma);
        double yE = yD + u * Math.sin(angel + gamma);

        //StdDraw.line(x, y, xB, yB); // A -> B
        StdDraw.line(xB, yB, xC, yC); // B -> C
        //StdDraw.line(xC, yC, xD, yD); // C -> D
        StdDraw.line(xD, yD, x, y); // D -> A

        if (size >= min) {
            PythagorasBaum2(xD, yD, angel + gamma, u); //linkes Rechteck
            PythagorasBaum2(xE, yE, angel + gamma - Math.PI/2, v); //rechtes Rechteck; winkel modifizieren
        }
    }
}
