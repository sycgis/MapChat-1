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

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class MapChatClientWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private final static Dimension minSize = new Dimension(640,480);
	private final static Dimension maxSize = new Dimension(1280,960);
	private ClientPanel cp;
	
	{	
		setTitle("MapChat");
		setSize(maxSize);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(minSize);
		setMaximumSize(maxSize);
		cp = new ClientPanel();
		add(cp);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	MapChatClientWindow mccw = new MapChatClientWindow();
		    	mccw.setVisible(true);
		    }
		});
	}
}
