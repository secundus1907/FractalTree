public class NoiseLoop {

    RecursionTree canvas;
    float diameter;

    public NoiseLoop(float diameter, RecursionTree canvas) {
        this.canvas = canvas;
        this.diameter = diameter;
    }

    float value(float angle){
        float xoff = canvas.map(canvas.cos(angle), -1,1,0, diameter);
        float yoff = canvas.map(canvas.sin(angle), -1,1,0,diameter);
        float r = canvas.noise(xoff, yoff);
        r = canvas.map(r, 0,1,0,1);
        return r;
    }
}
