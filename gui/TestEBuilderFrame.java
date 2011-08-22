import javax.swing.JFrame;

public class TestEBuilderFrame
    extends JFrame {

    TestEBuilderFrame() {

        super("Test EBuilder Frame");
        final EBuilderPanel panel = new EBuilderPanel();
        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }
}
