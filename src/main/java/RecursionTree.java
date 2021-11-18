import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import processing.core.PApplet;

import java.util.ArrayList;

public class RecursionTree extends PApplet {
    public static void main(String[] args) {
        PApplet.main("RecursionTree", args);
    }

    public ArrayList<Branch> tree = new ArrayList<>();
    public ArrayList<Leave> leaves = new ArrayList<>();

    public int MAX_RECURSION = 15;

    public double MIN_BRANCH_THICKNESS = 0.0005;

    public int FIRST_BRANCH_LENGHT = 150;

    public int FIRST_BRANCH_THICKNESS = 70;

    public int[] WEIGHTS = {7, 5, 2, 3, 5, 3, 3, 5, 3, 2, 5, 7};

    public int WIND_SPEED = 5;

    public float NOISE_RADIUS = (float) 1.1;

    public double THICKNESS_FACTOR = 0.95;

    double CORR_FACTOR = 0.6; // Max 1.0 corretion 90 degrees

    public float WIND_FORCE = (float) 0.2;

    boolean RECORD = false;

    boolean ANIMATE = false;

    boolean DRAW_NEW = false;

    public Branch root;

    NoiseLoop noiseLoop;

    public int frames = 240;



    public void settings() {
        size(1250, 1250);
    }

    public void setup() {
        background(25,25,112);
        frameRate(30);
        noiseLoop = new NoiseLoop(NOISE_RADIUS, this);


        gui();
        createTree();
        addLeaves();
        showTree();

    }


    public void gui(){
        new GUI(this);

    }


    float a = 0;
    int counter;
    float percantage = 0;

    public void draw() {

        if (DRAW_NEW){
            createTree();
            addLeaves();
            showTree();
            DRAW_NEW = false;
        }


        if (ANIMATE){
            animateAndCapture();
        }
    }

    public void animateAndCapture(){
        if(RECORD){
            percantage = (float) (counter%frames) / (float) frames;
        }else{
            percantage = (float) (counter%frames) / (float) frames;
        }

        render(percantage);
        if (RECORD) {
            saveFrame("output/gif-"+nf(counter,3)+".png");
            if (counter == frames - 1){
                exit();
            }
        }
        counter++;
    }

    public void render(float percent){
        background(135, 206, 235);
        stroke(255);

        a = percent * TWO_PI;
        float r = noiseLoop.value(a);
        a += radians(WIND_SPEED);


        root.windUpdate(r);

        for (Branch b : tree) {
            b.animate();
        }

        for (Leave l : leaves) {
            l.show();
        }
    }

    public void createTree(){
        tree = new ArrayList<>();

        Vector2D begin = new Vector2D(width / 2, height);
        Vector2D end = new Vector2D(width / 2, height - FIRST_BRANCH_LENGHT);
        Vector2D left = new Vector2D((width / 2) - (FIRST_BRANCH_THICKNESS / 2), height);
        Vector2D right = new Vector2D((width / 2) + (FIRST_BRANCH_THICKNESS / 2), height);

        root = new Branch(begin, end, this, FIRST_BRANCH_THICKNESS, left, right,
                1, true, 0, 1,  CORR_FACTOR, THICKNESS_FACTOR, WIND_FORCE);
        tree.add(root);

        for (int i = 0; i < MAX_RECURSION; i++) {
            //addBranch();
            addBranchUntilSmall();
        }
    }

    public void showTree(){
        background(135, 206, 235);
        stroke(255);

        for (Branch b : tree) {
            b.show();
        }
        for (Leave l : leaves) {
            l.show();
        }
    }


    public void addBranchUntilSmall() {
        ArrayList<Branch> brancheAdd = new ArrayList<>();
        for (Branch b : tree) {
            if (!b.finished && b.thicknessBottom > MIN_BRANCH_THICKNESS) {
                brancheAdd.addAll(b.newBranches());
            }
        }
        tree.addAll(brancheAdd);
    }

    public void addLeaves() {

        leaves = new ArrayList<>();
        for (Branch branch : tree) {

            //dann sind gleich beide null
            if (branch.nextBranchLeft == null) {
                branch.leave = new Leave(branch.end, this);
                leaves.add(branch.leave);

            }
        }
    }

    public void addBranch() {
        ArrayList<Branch> brancheAdd = new ArrayList<>();
        for (Branch b : tree) {
            if (!b.finished) {
                brancheAdd.addAll(b.newBranches());

            }
        }
        tree.addAll(brancheAdd);
    }


    public double weightedRandom() {

        double[] num = new double[WEIGHTS.length];

        for (int i = 0; i < WEIGHTS.length; i++) {
            num[i] = Math.round(((double) 1 / WEIGHTS.length) * i * 100.0) / 100.0;
        }
        int total = 0;
        for (int w : WEIGHTS) {
            total += w;
        }
        double[] combined = new double[total];
        int currIndex = 0;
        for (int i = 0; i < WEIGHTS.length; i++) {
            for (int j = 0; j < WEIGHTS[i]; j++) {
                combined[currIndex] = num[i];
                currIndex++;
            }
        }
        return ((double) 1 / WEIGHTS.length) * Math.random() + combined[(int) (Math.random() * total)];
    }

    /*
    public void branch(float l) {
        line(0, 0, 0, -l);
        translate(0, -l);
        if (l > 4) {
            push();
            rotate(angle);
            branch((float) (l * 0.67));
            pop();

            push();
            rotate(-angle);
            branch((float) (l * 0.67));
            pop();
        }
        //line(0,0,0, (float) (-l * 0.67));
    }
     */
}
