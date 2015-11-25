/*
 * Developed by Team Crush
 * Tran Situ
 * Jason Wang
 * Vishnu Venkateswaran
 * Dana Thomas
 * Arad Margalit
 * Willa Zhao
 * USC CSCI 201 Fall 2015
 */

package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ProfileAboutScreen extends JPanel  {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JLabel namelabel;
    private JLabel username;
    private JLabel hometown;
    private JButton logoutbutton;

    public ProfileAboutScreen(final ClientPanel clientPanel){
        super();
        
        //Set layout manager
        setLayout(new BorderLayout()); //Three panels: Userinfo, logout, app info
        
        //Three rows: name, username, hometown
        
        //Initialize Components
        JPanel userinfo = new JPanel();
        JPanel logout = new JPanel();
        JPanel appinfo = new JPanel();
        
        userinfo.setLayout(new BorderLayout());
        appinfo.setLayout(new BoxLayout(appinfo, BoxLayout.Y_AXIS));
        logout.setLayout(new FlowLayout());
        
//----------------------------------------------------------------------
//Creating the User info section of Info Frame
        
        namelabel = new JLabel("", JLabel.CENTER);
        username = new JLabel("", JLabel.CENTER);
        hometown = new JLabel("", JLabel.CENTER);
        
        //Component Formatting/Design
        namelabel.setBorder(new EmptyBorder(60, 0, 16, 0));
        namelabel.setFont(new Font("Serif", Font.BOLD, 32));
        namelabel.setForeground(Color.WHITE);
        
        username.setBorder(new EmptyBorder(0, 0, 8, 0));
        username.setFont(new Font("Serif", Font.PLAIN, 22));
        username.setForeground(Color.WHITE);
        
        hometown.setBorder(new EmptyBorder(0, 0, 20, 0));
        hometown.setFont(new Font("Serif", Font.PLAIN, 28));
        hometown.setForeground(Color.WHITE);
        
        //Set design/background coloring
        userinfo.setBackground(new Color(70, 30, 100));
        
        //Add swing components to content page
        userinfo.add(namelabel, BorderLayout.NORTH);
        userinfo.add(username, BorderLayout.CENTER);
        userinfo.add(hometown, BorderLayout.SOUTH);
//-----------------------------------------------------------------------       
//Creating the App Information Section
        
        JLabel appversion = new JLabel("App Version 1.0", JLabel.CENTER);
        appversion.setFont(new Font("Serif", Font.ITALIC, 16));
        appversion.setBorder(new EmptyBorder(30, 0, 0, 0));
        
        JLabel developers = new JLabel("Developers:");
        developers.setFont(new Font("Serif", Font.BOLD, 14));
        JLabel name1 = new JLabel("Arad Margalit");
        JLabel name2 = new JLabel("Dana Thomas");
        JLabel name3 = new JLabel("Tran Situ");
        JLabel name4 = new JLabel("Vishnu Venkateswaran");
        JLabel name5 = new JLabel("Jason Wang");
        JLabel name6 = new JLabel("Willa Zhao");
        
        appversion.setAlignmentX(Component.CENTER_ALIGNMENT);
        appinfo.add(appversion);
        appinfo.add(Box.createVerticalGlue());
        
        developers.setAlignmentX(Component.CENTER_ALIGNMENT);
        appinfo.add(developers);
        
        name1.setAlignmentX(Component.CENTER_ALIGNMENT);
        appinfo.add(name1);
        name2.setAlignmentX(Component.CENTER_ALIGNMENT);
        appinfo.add(name2);
        name3.setAlignmentX(Component.CENTER_ALIGNMENT);
        appinfo.add(name3);
        name4.setAlignmentX(Component.CENTER_ALIGNMENT);
        appinfo.add(name4);
        name5.setAlignmentX(Component.CENTER_ALIGNMENT);
        appinfo.add(name5);
        name6.setAlignmentX(Component.CENTER_ALIGNMENT);
        appinfo.add(name6);
        
        appinfo.setBorder(new EmptyBorder(10, 10, 10, 10));

//-----------------------------------------------------------------------       
//Creating the Logout Section
        logoutbutton = new JButton("Logout");
        JLabel guestmessage = new JLabel("Register if you want to chat!");
        
        logoutbutton.setFont(new Font("Serif", Font.BOLD, 14));
        logoutbutton.setBackground(Color.GRAY); 
        logoutbutton.setForeground(new Color(70, 30, 100));
        logoutbutton.setMargin(new Insets(10, 10, 10, 10));
        
       if(clientPanel.getUser() == null){
            logout.add(guestmessage, BorderLayout.CENTER);
        }
        
        else{
            logout.add(logoutbutton, BorderLayout.CENTER);
            logoutbutton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {    
                    String tokill = clientPanel.getUser().getUsername();
                    clientPanel.sendMessage("SELFDESTRUCT: " + tokill);
                    System.exit(0);
                }
            });
        }
   
        // add code here for logout functionality
        
        logout.setBorder(new EmptyBorder(10, 10, 10, 10));
     
//-------------------------------------------------------------------------     
        this.add(userinfo, BorderLayout.NORTH);
        this.add(appinfo, BorderLayout.CENTER);
        this.add(logout, BorderLayout.SOUTH);
        //If guest, display extra message at the bottom saying that they could chat if they register.
        //Sign out button
    }
    
    public void setUserInfo(String uSymbol, String uName, String homeTown ) {   
        namelabel.setText(uSymbol);
        username.setText(uName);
        hometown.setText(homeTown);
    }

    
    
}