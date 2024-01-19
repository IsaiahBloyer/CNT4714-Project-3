import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main
{

    public static void main(String args[])
    {
        System.out.println("Starting");
        JFrame frame = new JFrame("Project Three: Two-Tier Client-Server Application Development With MySQL and JDBC");
        GUI gui = new GUI();
        frame.setContentPane(gui.window);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                try
                {
                    gui.close();
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        });
    }


}
