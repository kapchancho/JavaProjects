import javax.swing.*;

public class MainWindow extends JFrame {
    public static void main(String[] args) {
        new MainWindow();
    }

    public MainWindow(){
        setTitle("SnakeApp");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(316,336);
        setResizable(false);
        add(new GameField());
        setLocation(500,150);
        setVisible(true);
    }


}
