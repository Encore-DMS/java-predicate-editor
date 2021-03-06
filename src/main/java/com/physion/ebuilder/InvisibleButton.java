/**
 * Copyright (c) 2011. Physion Consulting LLC
 * All rights reserved.
 */
package com.physion.ebuilder;

import java.awt.Graphics;
import javax.swing.JButton;


/**
 * This subclass of JButton overrides the normal paint() method
 * so that we can optionally NOT paint the button in the GUI.
 *
 * This button takes up space in its parent layout, but if its
 * "draw" flag is set to false, it does not draw itself or
 * accept focus.
 *
 * TODO:  There must be an easier solution to this.
 * I'd swear I've done this at some point in the past
 * without having to create a subclass.
 */
class InvisibleButton
    extends JButton {

	/**
	 * We never serialize this class, so this declaration is
	 * just to stop the compiler warning.
	 * You can suppress the warning instead if you want using
	 * @SuppressWarnings("serial")
	 */
	private static final long serialVersionUID = 1L;

    /**
     * If this is set to false, we will not draw ourselves or take
     * the focus.
     */
    boolean draw;


    /**
     * Create a button with the passed in label.
     * By default, we will draw ourselves and work
     * just like a normal button.
     */
    public InvisibleButton(String label) {
        super(label);
        draw = true;
    }


    /**
     * Don't draw ourselves if our draw flag is set to false.
     */
    @Override
    public void paint(Graphics g) {
        if (draw)
            super.paint(g);
    }


    /**
     * If you pass false for the draw flag, then this button will
     * not draw itself or accept the focus.  But, it will still take
     * up space in whatever Container is its parent.
     */
    public void setDraw(boolean draw) {

        this.draw = draw;

        /**
         * If we are not drawn, we also should not accept the focus.
         */
        setEnabled(draw);

        if (getParent() != null)
            getParent().repaint();
    }
}
