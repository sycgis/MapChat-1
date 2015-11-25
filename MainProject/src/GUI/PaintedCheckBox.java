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

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JCheckBox;

public class PaintedCheckBox extends JCheckBox{

	private static final long serialVersionUID = 1L;

	private Image mImage;
	
	private final Image mSelected;
	private final Image mNotSelected;
	
	public PaintedCheckBox(Image inSelected, Image inNotSelected) {
		mSelected = inSelected;
		mImage = mNotSelected  = inNotSelected;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(isSelected()) mImage = mSelected;
		else mImage = mNotSelected;
		g.drawImage(mImage, 0, 0, getWidth(), getHeight(), null);
	}
}
