import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;

class Server02 
{
    JFrame win;
    JPanel p1;
    JTextArea t1;
    JTextField t2;
    JButton b1;
    static Box vertical = Box.createVerticalBox();

    DataOutputStream dout; // Declare dout here

    public Server02()
    {
        win = new JFrame("Chat Application");
        win.setLayout(null);

        // Top Panel Setup
        p1 = new JPanel();
        p1.setLayout(null);
        p1.setBounds(0, 0, 400, 80);
        p1.setBackground(new Color(0,128,128));
        win.add(p1);

        // Dummy icons for design (Replace with actual paths if needed)
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("b1.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel l = new JLabel(new ImageIcon(i2));
        l.setBounds(10, 20, 20, 20);
        p1.add(l);

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("pro1.png"));
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        JLabel l1 = new JLabel(new ImageIcon(i5));
        l1.setBounds(40, 7, 60, 60);
        p1.add(l1);

        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("call.png"));
        Image i8 = i7.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        JLabel l2 = new JLabel(new ImageIcon(i8));
        l2.setBounds(300, 20, 50, 50);
        p1.add(l2);

        ImageIcon i9 = new ImageIcon(ClassLoader.getSystemResource("dot.png"));
        Image     i10=i9.getImage().getScaledInstance(30,30, Image.SCALE_DEFAULT);
        JLabel l3=new JLabel(new ImageIcon(i10));
        l3.setFont(new Font("Serif", Font.BOLD,30));
        l3.setBounds(360,20,30,30);
        p1.add(l3);

        // Text Area (Chat Window)
        t1 = new JTextArea();
        t1.setBounds(3, 83, 390, 380);
        t1.setEditable(false); // Disable editing by the user
        win.add(t1);

        JPanel a1=new JPanel();
        a1.setBounds(5,75,440,570);
        win.add(a1);

        // Text Field (Input)
        t2 = new JTextField();
        t2.setBounds(0, 480, 300, 50);

        win.add(t2);

        // Send Button
        b1 = new JButton("Send");
        b1.setBounds(300, 480, 85, 30);
        b1.setBackground(new Color(0,128,128));
        // b1.setEnabled(false); // Disable the button initially
        win.add(b1);

        // Window Properties
        win.setSize(400, 550);
        win.setLocationRelativeTo(null);
        win.setVisible(true);

        // Button ActionListener
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try 
                {
                    String out = t2.getText(); // Get text from t2 (input field)

                    if (!out.isEmpty())
                     {
                        JPanel p2 = formatLabel(out); // Format the output for display
                        t1.append(out + "\n"); // Append text to the chat area

                        // Add message to the right side of the chat area
                        JPanel right = new JPanel(new BorderLayout());
                        right.add(p2, BorderLayout.LINE_END);
                        vertical.add(right);
                        vertical.add(Box.createVerticalStrut(10));
                        t1.setLayout(new BorderLayout());
                        t1.add(vertical, BorderLayout.PAGE_START);

                        dout.writeUTF(out); // Send the message over the network
                        // t2.setText(""); // Clear the input field
                    }

                    win.repaint();
                    win.invalidate();
                    win.validate();
                } 
                catch (IOException ex) 
                {
                    ex.printStackTrace();
                }
            }
        });
    }

    // Formatting the Label for Display in the Chat Area
    public static JPanel formatLabel(String out) 
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width:150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(0, 15, 15, 50));

        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }

    // Main method to start the server
    public static void main(String[] args) 
    {
        Server02 server = new Server02();

        try 
        {
            ServerSocket skt = new ServerSocket(6001);
            while (true) 
            {
                Socket s = skt.accept();
                DataInputStream din = new DataInputStream(s.getInputStream());
                server.dout = new DataOutputStream(s.getOutputStream()); // Initialize dout here

                server.b1.setEnabled(true); // Enable the Send button once dout is initialized
                while (true) 
                {
                    String msg = din.readUTF();
                    JPanel panel = formatLabel(msg);

                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    vertical.add(left);
                    server.win.validate();
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
