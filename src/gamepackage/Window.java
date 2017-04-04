package gamepackage;

import javax.swing.JFrame;

class Window extends JFrame
{ 
    public Window()
    {     
        add(new Board());
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }   
}