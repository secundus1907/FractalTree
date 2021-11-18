import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;


public class Branch {

    RecursionTree canvas;

    Vector2D begin;
    Vector2D end;
    Vector2D endFirst;
    Vector2D beginFirst;

    Vector2D topLeft;
    Vector2D topRight;

    Vector2D topLeftPrev;
    Vector2D topRightPrev;

    public Branch nextBranchLeft = null;
    public Branch nextBranchRight = null;
    public Leave leave = null;

    public boolean isLeft;

    public double thicknessBottom;
    public double thicknessTop;

    public double lenghtFactor;

    public double angle;

    public double THICKNESS_FACTOR = 0.95;
    double CORR_FACTOR = 0.6; // Max 1.0 corretion 90 degrees
    public float WIND_FORCE = (float) 0.1;

    public double propertyNum;

    public boolean finished = false;

    public Branch(Vector2D begin, Vector2D end, RecursionTree canvas, double thicknessBottom, Vector2D topLeftPrev, Vector2D topRightPrev,
                  double num, boolean isLeft, double angle, double lenghtFactor, double CORR_FACTOR, double THICKNESS_FACTOR, float WIND_FORCE) {
        this.CORR_FACTOR = CORR_FACTOR;
        this.THICKNESS_FACTOR = THICKNESS_FACTOR;
        this.WIND_FORCE = WIND_FORCE;
        this.begin = begin;
        this.beginFirst = begin;
        this.end = end;
        this.endFirst = end;
        this.thicknessBottom = thicknessBottom;
        this.thicknessTop = this.thicknessBottom * THICKNESS_FACTOR;
        this.topLeftPrev = topLeftPrev;
        this.topRightPrev = topRightPrev;
        this.isLeft = isLeft;
        this.angle = angle;
        this.lenghtFactor = lenghtFactor;

        SetCalculateTop();

        //this.topLeft = calculateTop(true);
        //this.topRight = calculateTop(false);

        this.propertyNum = num;
        this.canvas = canvas;
    }

    public void SetCalculateTop() {
        Vector2D beginEnd = new Vector2D(begin.getX() - end.getX(), begin.getY() - end.getY());
        Vector2D orth = new Vector2D(beginEnd.getY(), -beginEnd.getX());

        this.topLeft = end.add(orth.normalize().scalarMultiply(-thicknessTop / 2));
        this.topRight = end.add(orth.normalize().scalarMultiply(thicknessTop / 2));
    }
/*
    public Vector2D calculateTop(boolean left){
        Vector2D beginEnd = new Vector2D( begin.getX() - end.getX() , begin.getY() - end.getY());
        Vector2D orth = new Vector2D(beginEnd.getY(), -beginEnd.getX());

        if (left) {
            return end.add(orth.normalize().scalarMultiply(-thicknessTop / 2));
        }
        else {
            return end.add(orth.normalize().scalarMultiply(thicknessTop / 2));
        }
    }
    */

    public void show() {

        canvas.stroke(52, 28, 2);
        canvas.fill(52, 28, 2);
        canvas.quad((float) topRightPrev.getX(), (float) topRightPrev.getY(), (float) topRight.getX(), (float) topRight.getY(),
                (float) topLeft.getX(), (float) topLeft.getY(), (float) topLeftPrev.getX(), (float) topLeftPrev.getY());
    }

    public void animate() {

        canvas.stroke(52, 28, 2);
        canvas.fill(52, 28, 2);
        canvas.quad((float) topRightPrev.getX(), (float) topRightPrev.getY(), (float) topRight.getX(), (float) topRight.getY(),
                (float) topLeft.getX(), (float) topLeft.getY(), (float) topLeftPrev.getX(), (float) topLeftPrev.getY());
    }

    public void windUpdate(float noiseNum) {

        float rotation = (noiseNum - (float) 0.5) * (WIND_FORCE / (float) Math.pow(thicknessBottom, 0.1));
        Vector2D dir = end.subtract(begin);
        if (nextBranchLeft == null) {
            leave.pos = end;
        }
        if (nextBranchRight == null) {
            leave.pos = end;
        }


        if (nextBranchLeft != null) {
            Vector2D dirL = rotateVector(nextBranchLeft.angle + rotation, dir).scalarMultiply(nextBranchLeft.lenghtFactor);
            nextBranchLeft.end = end.add(dirL);
            nextBranchLeft.begin = end;
            nextBranchLeft.topLeftPrev = topLeft;
            nextBranchLeft.topRightPrev = topRight;
            nextBranchLeft.SetCalculateTop();
            nextBranchLeft.windUpdate(noiseNum);
        }
        if (nextBranchRight != null) {
            Vector2D dirR = rotateVector(nextBranchRight.angle + rotation, dir).scalarMultiply(nextBranchRight.lenghtFactor);
            nextBranchRight.end = end.add(dirR);
            nextBranchRight.begin = end;
            nextBranchRight.topLeftPrev = topLeft;
            nextBranchRight.topRightPrev = topRight;
            nextBranchRight.SetCalculateTop();
            nextBranchRight.windUpdate(noiseNum);
        }



    }

    public ArrayList<Branch> newBranches() {
        ArrayList<Branch> bothBranches = new ArrayList<>();
        double randomNum = canvas.weightedRandom();

        nextBranchLeft = branch(true, 1.0 - randomNum);
        bothBranches.add(nextBranchLeft);
        nextBranchRight = branch(false, randomNum);
        bothBranches.add(nextBranchRight);


        return bothBranches;
    }

    public Branch branch(boolean isLeft, double num) {
        finished = true;
        Vector2D dir = end.subtract(begin);

        double angleLeft = calculateAngle(num, true);
        double angleRigth = calculateAngle(num, false);

        double lenghtFactor = calculateLengthFactor(num);
        double thickness = thicknessTop * (1 - num);

        if (isLeft) {
            dir = rotateVector(-angleLeft, dir).scalarMultiply(lenghtFactor);
        } else {
            dir = rotateVector(angleRigth, dir).scalarMultiply(lenghtFactor);
        }
        Vector2D newEnd = end.add(dir);

        if (isLeft) {
            return new Branch(end, newEnd, canvas, thickness, topLeft, topRight, num, isLeft, -angleLeft, lenghtFactor, CORR_FACTOR, THICKNESS_FACTOR, WIND_FORCE);
        } else {
            return new Branch(end, newEnd, canvas, thickness, topLeft, topRight, num, isLeft, angleRigth, lenghtFactor, CORR_FACTOR, THICKNESS_FACTOR, WIND_FORCE);
        }

    }


    public double calculateAngle(double num, boolean left) {

        double angle = (Math.PI / 180.0) * num * 90.0;

        if (left == this.isLeft) {
            double correctionAngle = angle - ((Math.PI / 2) - ((Math.PI / 2) - angle)) * (Math.random() * CORR_FACTOR);
            return correctionAngle;
        }
        return angle;
        //return (Math.PI / 180.0) * num * 90.0;
    }

    public double calculateLengthFactor(double num) {

        //TODO maybe thickness
        //return (Math.pow(num,2)*0.75);
        return 1.0 - (num * 0.67);
        //return  1.0 -  Math.pow(num,2);
    }

    public Vector2D rotateVector(double n, Vector2D a) {
        double rx = (a.getX() * Math.cos(n)) - (a.getY() * Math.sin(n));
        double ry = (a.getX() * Math.sin(n)) + (a.getY() * Math.cos(n));
        return new Vector2D(rx, ry);

    }

    //WHY, just for a random weighted double...

}
