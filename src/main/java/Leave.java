import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Leave {

    public RecursionTree canvas;
    public Vector2D pos;
    public int red;
    public int green;
    public int blue;
    public int c = 4;
    public int d = 8;


    public Leave(Vector2D pos, RecursionTree canvas){
        this.pos = new Vector2D(pos.getX(), pos.getY() + (d/2));
        this.canvas = canvas;
        this.red = (int) (Math.random() * 254);
        this.green = (int) (Math.random() * 100);
        this.blue = (int) (Math.random() * 20);
    }

    public void show(){
        canvas.stroke(red, green, blue);
        canvas.fill(red, green, blue);

        System.out.println();
        canvas.ellipse( (float)pos.getX(), (float)pos.getY(), c, d);
    }


}
