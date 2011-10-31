package com.physion.ovation.gui.ebuilder;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Scrollable;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import com.physion.ovation.gui.ebuilder.datamodel.RowData;
import com.physion.ovation.gui.ebuilder.datamodel.RowDataEvent;
import com.physion.ovation.gui.ebuilder.datamodel.RowDataListener;


/**
 * This is the panel that contains a list of RowPanels.
 * Each RowPanel handles one row in the expression tree.
 * Each RowPanel displays/edits one RowData object.
 *
 * TODO: Remove the commented out "zebra striping" code if
 * Physion decides it does not want to do something like that.
 */
public class ExpressionPanel
    extends JPanel
    implements RowDataListener, Scrollable {
	
	/**
	 * We never serialize this class, so this declaration is
	 * just to stop the compiler warning.
	 * You can suppress the warning instead if you want using
	 * @SuppressWarnings("serial")
	 */
	private static final long serialVersionUID = 1L;

	private RowData rootRow;


    /**
     * Create an ExpressionPanel that will display and edit the passed
     * in expression tree.
     */
    ExpressionPanel(RowData rootRow) {

        super(new GridLayout(1,1));
        setRootRow(rootRow);
    }


    /**
     * Set the expression tree this ExpressionPanel will display.
     */
    public void setRootRow(RowData rootRow) {

        if (this.rootRow != null)
            this.rootRow.removeRowDataListener(this);

        this.rootRow = rootRow;
        rootRow.addRowDataListener(this);
        createRowPanels();
    }


    public RowData getRootRow() {
        return(rootRow);
    }


    /**
     * Create all the RowPanels this ExpressionPanel contains.
     *
     * TODO: Make this code more clever so when rows are added
     * or deleted we don't remove all the rows and recreate
     * all the rows, but instead we only change what we need to.
     */
    public void createRowPanels() {

        removeAll();

        GridLayout layout = (GridLayout)getLayout();
        layout.setRows(rootRow.getDescendentCount()+1);

        //int count = 0;
        for (RowData rowData : rootRow.getRows()) {
            RowPanel rowPanel = new RowPanel(rowData);
            add(rowPanel);

            /**
             * Zebra stripe the rows.
             */
            /*
            if ((count % 2) == 0)
                rowPanel.setBackground(rowPanel.getBackground().darker());
            count++;
            */
        }

        /**
         * Make the scrollpane layout things now so that when
         * we need to figure out where the row is it is in its
         * final location.
         *
         * Another way to do this is to make the call
         * to ensureRowPanelVisible() inside of an invokeLater()
         * call.  That way the ensureRowPanelVisible() call
         * would get made after the "normal" layout validation
         * had occurred.
         */
        if (getScrollPane() != null)
            getScrollPane().validate();
    }
    

    /**
     * Get the RowPanel at the passed in index.
     * The root RowPanel, which is the one that is used
     * to select the Class Under Qualification, is at index 0.
     */
    /*
    private RowPanel getRowPanel(int index) {
        return((RowPanel)getComponent(index));
    }
    */


    /**
     * This method returns the RowPanel that is displaying/editing
     * the passed in rowData.  Returns null if no RowPanel in this
     * ExpressionPanel is handling the passed in rowData.
     */
    private RowPanel getRowPanel(RowData rowData) {

        for (Component component : getComponents()) {
            if (((RowPanel)component).getRowData() == rowData) {
                return((RowPanel)component);
            }
        }

        return(null);
    }


    /**
     * This is called when the expression tree we are displaying
     * is modified.
     *
     * If the change is one that changes the number of rows
     * in the GUI, recreate the RowPanels.
     *
     * TODO: Handle focus traversal properly if we want to have
     * the GUI usable from the keyboard.
     */
    public void rowDataChanged(RowDataEvent event) {

        /**
         * Until I write more clever code, recreate all the
         * rows when the number of rows are changed.
         *
         * In order to improve GUI performance, we really
         * should be clever about creating or deleting
         * just the one row involved in the change, and
         * then moving the rows below the newly created
         * row down, (or moving the rows below the deleted
         * row up).  But, that is not yet the case.
         */
        if ((event.getTiming() == RowDataEvent.TIMING_AFTER) &&
            ((event.getChangeType() == RowDataEvent.TYPE_CHILD_ADD) ||
             (event.getChangeType() == RowDataEvent.TYPE_CHILD_DELETE) ||
             (event.getChangeType() == RowDataEvent.TYPE_CUQ))) {

            createRowPanels();
        }

        /**
         * Set the focus appropriately.  For example, if we created
         * a new row, set the focus to the first component in that
         * row.
         */
        if ((event.getTiming() == RowDataEvent.TIMING_AFTER) &&
            ((event.getChangeType() == RowDataEvent.TYPE_CHILD_ADD) ||
             (event.getChangeType() == RowDataEvent.TYPE_CHILD_DELETE) ||
             (event.getChangeType() == RowDataEvent.TYPE_ATTRIBUTE) ||
             (event.getChangeType() == RowDataEvent.TYPE_CUQ))) {

            if (event.getChangeType() == RowDataEvent.TYPE_CHILD_ADD) {
                /**
                 * Set the focus to the new row.  Rows are always
                 * added to the end of the parent row's list of children.
                 * So, simply set the focus to the last child row of the
                 * row that caused the event.  I.e. the row that
                 * had the child added to it, which is the "originalRowData"
                 * member data of the RowDataEvent.
                 */
                RowData rowData = event.getOriginalRowData();
                rowData = rowData.getChildRows().get(
                    rowData.getChildRows().size()-1);

                /**
                 * Get the RowPanel that handles that rowData
                 * and set the focus to it.
                 */
                RowPanel rowPanel = getRowPanel(rowData);
                rowPanel.setFocusToFirstFocusableComponent();
                ensureRowPanelVisible(rowPanel);
            }
            else if ((event.getChangeType() ==
                      RowDataEvent.TYPE_CHILD_DELETE) ||
                     (event.getChangeType() == RowDataEvent.TYPE_CUQ)) {

                /**
                 * Set the focus to the first row.
                 *
                 * TODO:  Figure out a more user friendly rule as
                 * to what row and component should get the focus
                 * when a row is deleted.
                 */
                RowPanel rowPanel = getRowPanel(rootRow);
                rowPanel.setFocusToFirstFocusableComponent();
                ensureRowPanelVisible(rowPanel);
            }
        }
    }


    private JScrollPane getScrollPane() {

        JScrollPane scrollPane = null;
        Container parent = getParent();
        while (parent != null) {
            if (parent instanceof JScrollPane) {
                scrollPane = (JScrollPane)parent;
                break;
            }
            parent = parent.getParent();
        }

        return(scrollPane);
    }


    /**
     * This method ensures that the passed in RowPanel is visible in
     * the scrolling window.  If the passed rowPanel is not visible,
     * the scrolling window will be scrolled by the minimal amount
     * to make it visible.
     */
    public void ensureRowPanelVisible(RowPanel rowPanel) {

        JScrollPane scrollPane = getScrollPane();
        if (scrollPane == null)
            return;

        JViewport viewport = scrollPane.getViewport();

        Rectangle viewRect = viewport.getViewRect();
        //System.out.println("viewRect: "+viewRect);

        Point location = rowPanel.getLocation();
        //System.out.println("location: "+location);

        if (location.y < viewRect.y) {
            /**
             * The top of the rowPanel is scrolled above the top of the
             * scrollPane.
             */
            viewport.setViewPosition(new Point(location.y, location.y));
        }
        else if ((location.y + rowPanel.getHeight()) >
                 (viewRect.y + viewRect.height)) {
            /**
             * The bottom of the rowPanel is scrolled below the bottom
             * of the scrollPane.
             */

            int rowBottomPixel = rowPanel.getY() + rowPanel.getHeight()-1;

            int viewportTopPixel = rowBottomPixel - viewRect.height + 1; 

            viewport.setViewPosition(new Point(viewRect.x, viewportTopPixel));
        }
    }


    /**
     * Tell any JScrollPane that contains us that it should NOT
     * stretch us to fill the scrollPane.
     */
    @Override
    public boolean getScrollableTracksViewportHeight() {
        return(false);
    }


    /**
     * This method is required by the Scrollable interface
     * we implement, and is called by the JScrollPane that
     * contains us to decide whether it should stretch/shrink
     * us to fit the viewport (i.e. the visible portion), or
     * whether it should display a horizontal scrollbar.
     *
     * Make the RowPanels stretch to fill the width of the
     * scrollPane that contains us if the scrollPane is
     * larger than this ExpressionPanel would like to be.
     *
     * But, if the scrollPane's viewport is smaller than 
     * this ExpressionPanel would like to be, tell the
     * scrollPane to scroll us horizontally.
     */
    @Override
    public boolean getScrollableTracksViewportWidth() {

        JScrollPane scrollPane = getScrollPane();
        if (scrollPane == null) {
            /**
             * This should never happen, because no one should
             * call this method if we are not in a JScrollPane.
             */
            return(false);
        }

        JViewport viewport = scrollPane.getViewport();

        Rectangle viewRect = viewport.getViewRect();
        Dimension preferredSize = getPreferredSize();
        if (viewRect.width < preferredSize.width) {
            /**
             * The viewport is no wide enough for us, so
             * tell the scrollPane that it should NOT shrink
             * us to fit the viewport.  Instead, the scrollpane
             * will end up displaying a horizontal scrollbar so
             * the user can scroll us horizontally.
             */
            return(false);
        }

        /**
         * The viewport is larger than we need to be,
         * (or exactly the right size), so tell the
         * scrollPane to stretch us to fill the viewport.
         * (I.e. this ExpressionPanel, which is the "scrollable",
         * should "track" the viewport width.)
         */
        return(true);
    }


    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return(getPreferredSize());
    }


    /**
     * Return the number of pixels the JScrollPane that contains us
     * should scroll when the user does a "block" scroll.  E.g. the
     * user clicks above or below the scroll "thumb" or uses the
     * page up/down keys.
     *
     * This should scroll an entire "page" of rows.  Note, I think
     * it is unlikely that a user will have that many rows worth
     * of data in a real use case, but we should implement the
     * behavior anyway.
     */
    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect,
                                           int orientation, int direction) {

        if (orientation == SwingConstants.HORIZONTAL) {
            /**
             * When scrolling horizontally, scroll sideways
             * by the width of the viewport.
             */
            return(visibleRect.width);
        }
        else {
            return(visibleRect.height);
        }
    }


    /**
     * Return the number of pixels the JScrollPane that contains us
     * should scroll when the user clicks on a scrolling arrow.
     * When scrolling vertically, this is the height of one row, or
     * a lesser amount necessary to get a row aligned with the top or
     * bottom of the scrollpane.
     *
     * When scrolling horizontally, there isn't really a good
     * answer for how much scrolling one "unit" is.
     * Note, we don't currently scroll horizontally.
     */
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect,
                                          int orientation, int direction) {

        if (orientation == SwingConstants.HORIZONTAL) {
            /**
             * When scrolling horizontally, there isn't really a good
             * answer for how much scrolling one "unit" is.
             * So, just scroll by a percentage, (e.g. 25% =  0.25),
             * of the viewport width.
             */
            return((int)(visibleRect.width * 0.15));
        }

        /**
         * If we get here, the user is scrolling the window vertically.
         */

        /**
         * Get the first RowPanel to use to calculate
         * the height of a "unit", (i.e. one row), increment.
         */
        RowPanel rowPanel = getRowPanel(getRootRow());

        /**
         * If we don't have any rows yet, just return a number.
         * This number will never get used for scrolling because
         * the GUI will always have at least one row in it when it
         * is displayed.
         */
        if (rowPanel == null)
            return(10);

        Dimension size = rowPanel.getPreferredSize();

        if (direction > 0) {
            /**
             * When scrolling down, i.e. clicking the down arrow which
             * moves the scrollpane contents up, we want to make the
             * bottommost visible row align with the bottom of the
             * scrollpane.  If the bottommost visible row is already
             * aligned with the bottom of the scrollpane then that
             * means the number we return will be the height of one row.
             *
             * If the bottommost row is NOT aligned with the bottom
             * of the scrollpane, return the number of pixels that
             * WILL make it aligned with the bottom of the scrollpane.
             */

            int bottomPixel = visibleRect.height + visibleRect.y;
            int leftOver = bottomPixel % size.height;

            if (leftOver == 0) {
                /**
                 * The bottommost row is aligned with the bottom of the
                 * scrollpane, so return the height of one row as the
                 * amount to scroll one unit.
                 */
                return(size.height);
            }
            else {
                return(size.height - leftOver);
            }
        }
        else {  // direction < 0
            /**
             * When scrolling up, i.e. clicking the up arrow which
             * moves the scrollpane contents down, we want to make the
             * topmost visible row align with the top of the
             * scrollpane.  If the topmost visible row is already
             * aligned with the top of the scrollpane then that
             * means the number we return will be the height of one row.
             *
             * If the topmost row is NOT aligned with the top
             * of the scrollpane, return the number of pixels that
             * WILL make it aligned with the top of the scrollpane.
             */

            int topPixel = visibleRect.y;
            int leftOver = topPixel % size.height;

            if (leftOver == 0) {
                /**
                 * The topmost row is aligned with the top of the
                 * scrollpane, so return the height of one row as the
                 * amount to scroll one unit.
                 */
                return(size.height);
            }
            else {
                return(leftOver);
            }
        }
    }


    /**
     * This is just for debugging purposes.
     */
    public void print() {
        System.out.println("\nCurrent Value:\n"+rootRow.toString(false, ""));
        System.out.println("\nDebug Version:\n"+rootRow.toString());
    }
}
