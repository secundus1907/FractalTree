import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI {

    RecursionTree canvas;

    public GUI(RecursionTree canvas){
        this.canvas = canvas;

        JFrame frame = new JFrame();
//---------------------------------------------------------------------//
        JButton buttonNewTree = new JButton("New Tree");
        buttonNewTree.addActionListener(e -> canvas.DRAW_NEW = true);

        JButton buttonAnimate = new JButton("Toggle Animate");
        buttonAnimate.addActionListener(e -> canvas.ANIMATE = !canvas.ANIMATE);




        JLabel labelRecursion = new JLabel("Maximum Recursion");
        JSlider sliderRecursion = new JSlider(0,100, 15);
        sliderRecursion.addChangeListener(e -> canvas.MAX_RECURSION = sliderRecursion.getValue());

        JLabel labelLength = new JLabel("First Branch Lenght");
        JSlider sliderLength = new JSlider(0,1000, 150);
        sliderLength.addChangeListener(e -> canvas.FIRST_BRANCH_LENGHT = sliderLength.getValue());

        JLabel labelThick = new JLabel("First Branch Thickness");
        JSlider sliderThick = new JSlider(0,500, 70);
        sliderThick.addChangeListener(e -> canvas.FIRST_BRANCH_THICKNESS = sliderThick.getValue());

        JLabel labelMinBranch = new JLabel("Minimum Branch Thickness ");
        JSlider sliderMinBranch = new JSlider(1,1000, 500);
        sliderMinBranch.setInverted(true);
        sliderMinBranch.addChangeListener(e -> canvas.MIN_BRANCH_THICKNESS = (double) 1 /Math.pow(sliderMinBranch.getValue(), 1.25));

        JLabel labelWindSpeed = new JLabel("Wind Speed");
        JSlider sliderWindSpeed = new JSlider(0,20, 5);
        sliderWindSpeed.addChangeListener(e -> canvas.WIND_SPEED = sliderWindSpeed.getValue());

        JLabel labelNoise = new JLabel("Noise Radius");
        JSlider sliderNoise = new JSlider(0,50, 11);
        sliderNoise.addChangeListener(e -> canvas.NOISE_RADIUS = (float) 0.1 * sliderNoise.getValue());

        JLabel labelThickFactor = new JLabel("Thickness Factor");
        JSlider sliderThickFactor = new JSlider(500,1000, 950);
        sliderThickFactor.addChangeListener(e -> canvas.THICKNESS_FACTOR = (double)0.001 * sliderThickFactor.getValue());

        JLabel labelCorrFactor = new JLabel("Correction to Top Factor");
        JSlider sliderCorrFactor = new JSlider(0,100, 60);
        sliderCorrFactor.addChangeListener(e -> canvas.CORR_FACTOR = (double) 0.01 *  sliderCorrFactor.getValue());

        JLabel labelWindForce = new JLabel("Wind Force");
        JSlider sliderWindForce = new JSlider(0,100, 20);
        sliderWindForce.addChangeListener(e -> canvas.WIND_FORCE = (float) 0.01 * sliderWindForce.getValue());

        JLabel labelWeights= new JLabel("Weights Array");
        JTextField textFieldWeights = new JTextField();
        JButton confirmWeights = new JButton("Confirm");
        confirmWeights.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = textFieldWeights.getText();
                char[] numbers = new char[str.length()];
                for (int i = 0; i < str.length(); i++) {
                    numbers[i] = str.charAt(i);
                }
                int[]weights = new int[numbers.length];
                for (int i = 0; i < numbers.length; i++) {
                    weights[i] = numbers[i] -'0';
                }
                canvas.WEIGHTS = weights;
            }
        });





//---------------------------------------------------------------------//
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 400,200));
        panel.setLayout(new GridLayout(15,1));
//---------------------------------------------------------------------//
        panel.add(buttonNewTree);
        panel.add(buttonAnimate);

        panel.add(labelRecursion);
        panel.add(sliderRecursion);

        panel.add(labelLength);
        panel.add(sliderLength);

        panel.add(labelThick);
        panel.add(sliderThick);

        panel.add(labelMinBranch);
        panel.add(sliderMinBranch);

        panel.add(labelWindSpeed);
        panel.add(sliderWindSpeed);

        panel.add(labelNoise);
        panel.add(sliderNoise);

        panel.add(labelThickFactor);
        panel.add(sliderThickFactor);

        panel.add(labelCorrFactor);
        panel.add(sliderCorrFactor);

        panel.add(labelWindForce);
        panel.add(sliderWindForce);

        panel.add(labelWeights);
        panel.add(textFieldWeights);
        panel.add(confirmWeights);



//---------------------------------------------------------------------//
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Tree Settings");
        frame.pack();
        frame.setVisible(true);
    }

}
