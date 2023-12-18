import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MouseMacro {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MacroFrame frame = new MacroFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

class MacroFrame extends JFrame {
    private JButton runButton;
    private JButton stopButton;
    private JComboBox<Integer> timeIntervalBox;
    private JTextField xField;
    private JTextField yField;
    private JLabel currentMousePoint;
    private Robot robot;
    private Timer timer;

    public MacroFrame() {
        setTitle("김현우전용 매크로");
        setBounds(100, 100, 450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        runButton = new JButton("실행(F1)");
        runButton.setBounds(34, 45, 97, 23);
        getContentPane().add(runButton);

        stopButton = new JButton("중지(F2)");
        stopButton.setBounds(34, 78, 97, 23);
        getContentPane().add(stopButton);

        Integer[] seconds = {1, 2, 3, 4, 5};
        timeIntervalBox = new JComboBox<>(seconds);
        timeIntervalBox.setBounds(34, 111, 57, 21);
        getContentPane().add(timeIntervalBox);

        JLabel xLabel = new JLabel("x좌표");
        xLabel.setBounds(34, 142, 57, 15);
        getContentPane().add(xLabel);

        xField = new JTextField();
        xField.setBounds(101, 142, 116, 21);
        getContentPane().add(xField);
        xField.setColumns(10);

        JLabel yLabel = new JLabel("y좌표");
        yLabel.setBounds(34, 173, 57, 15);
        getContentPane().add(yLabel);

        yField = new JTextField();
        yField.setBounds(101, 173, 116, 21);
        getContentPane().add(yField);
        yField.setColumns(10);

        currentMousePoint = new JLabel("현재 마우스 좌표: ");
        currentMousePoint.setBounds(34, 204, 200, 21);
        getContentPane().add(currentMousePoint);

        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startMacro();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopMacro();
            }
        });

        Timer mousePointTimer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Point mousePoint = MouseInfo.getPointerInfo().getLocation();
                currentMousePoint.setText("현재 마우스 좌표: (" + mousePoint.x + ", " + mousePoint.y + ")");
            }
        });
        mousePointTimer.start();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (e.getKeyCode() == KeyEvent.VK_F1) {
                        startMacro();
                        return true;
                    } else if (e.getKeyCode() == KeyEvent.VK_F2) {
                        stopMacro();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void startMacro() {
        int x = Integer.parseInt(xField.getText().trim());
        int y = Integer.parseInt(yField.getText().trim());
        Point clickPoint = new Point(x, y);
        int interval = (int) timeIntervalBox.getSelectedItem();
        timer = new Timer(interval * 1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                robot.mouseMove(clickPoint.x, clickPoint.y);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }
        });
        timer.start();
    }

    private void stopMacro() {
        if (timer != null) {
            timer.stop();
        }
    }
}
