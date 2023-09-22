package ChessUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import ChessDataTypes.Chess;
import ChessDataTypes.ChessData;
import ChessDataTypes.ChessData.Piece;
import ChessDataTypes.ChessData.TurnColor;
import ChessDataTypes.ChessMove;
import ChessDataTypes.ChessMoves;
import ChessDataTypes.ChessPiece;
import ChessDataTypes.ChessPosition;
import FilleUtilities.FilesUtils;
import OtherUtilities.ImageUtils;
import SwingUtilities.SwingUtils;

public class ChessPiecePanel extends JPanel implements FocusListener, MouseMotionListener, MouseListener, AncestorListener {
	
	private ChessMain board;
	private final ChessPiece piece;
	private Piece currentType;
    private ChessPositionPanel position;
    private Image image;
    
	public ChessPiecePanel(ChessPiece piece, ChessMain board) {
		this.piece = piece;
		this.currentType = Piece.PIECE_UNKOWN;
		this.board = board;
		updateImage();
		this.setOpaque(false);
		this.addAncestorListener(this);
		this.addFocusListener(this);
		this.addMouseMotionListener(this);
		this.addMouseListener(RemoveDragListener.createDefaultDragListener());
		this.addMouseListener(this);
	}
	
	public TurnColor getColor() {
		return piece.getColor();
	}
	
	public Piece getType() {
		return piece.getType();
	}
	
	public ChessPiece getChessPiece() {
		return piece;
	}
	
	public void setChessPositionPanel(ChessPositionPanel position) {
		this.position = position;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		updateImage();
		int width = (int)(getWidth()*0.7);
		int height = (int)(getHeight()*0.7);
		int x = (int)((getWidth() - width)*0.5);
		int y = (int)((getHeight() - height)*0.5);
		g.drawImage(image, x, y, width, height, this);
		//System.out.println(position.getPosition() + "  " + piece.getPosition());
		checkUpdates();
	}
	
	private void checkUpdates() {
		ChessPosition positionPanel = this.position.getPosition();
		ChessPosition currentPosition = piece.getPosition();
		//System.out.println("Lo " + positionPanel + "  " + currentPosition + "  " + (positionPanel != null && !positionPanel.equals(positionPanel)));
		if(positionPanel != null && !positionPanel.equals(currentPosition)) {
			System.out.println("Enter\n\n\n\n\n");
			System.out.println(positionPanel + "  " + currentPosition);
			this.position.setChessPiecePanel(null);
			this.position = null;
			if(currentPosition != null) {
				this.position = board.getChessPositionPanel(currentPosition);
				if(this.position != null) {
					this.position.setChessPiecePanel(this);
					//this.board.refreshFrame();
				}
			}
		}
	}
	
	private void updateImage() {
		Piece type = this.piece.getType();
		if(!type.equals(this.currentType)) {
			this.currentType = type;
			this.image = ChessMain.loadImage("/chess_save/" + ChessData.getFileName(piece) + ".png");
		}
	}
	
	private ChessMoves possibleMoves;
	
	@Override
	public void focusGained(FocusEvent e) {
		System.out.println("Currebt p " + position.getPosition() + " \n\n\n\n\n");
		System.out.println("\n\n\n\n\n\n");
		System.out.println(position.getPosition().getChessPiece());
		possibleMoves = board.getChessBoard().getPossibleMoves(position.getPosition());
		updateMovesPossibilities(true);
	}

	@Override
	public void focusLost(FocusEvent e) {
		updateMovesPossibilities(false);
	}
	
	private void updateMovesPossibilities(boolean canMove) {
		if(possibleMoves != null) {
	        for(ChessMove move : possibleMoves.getPossibleMoves()) {
	        	ChessPosition position = move.getMoveToPosition();
	        	ChessPositionPanel positionPanel = board.getChessPositionPanel(position);
	        	if(positionPanel != null)
	        		positionPanel.setIfPossible(canMove);
	        }
	        board.refreshFrame();
		}
	}
	
	private ChessPositionPanel prevSquare;
	private boolean canDrag;
	private Border prevBorder;
	private Point currentPoint = new Point(0,0);
	private Point mouseDownCompCoords = new Point(0,0);
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(canDrag == true) {
		    Point currCoords = this.getLocation();
		    Point newMouseDownCompCoords = e.getPoint();
		    currentPoint = new Point(currCoords.x + newMouseDownCompCoords.x - mouseDownCompCoords.x, currCoords.y + newMouseDownCompCoords.y - mouseDownCompCoords.y);
		    Dimension size = getSize();
		    Rectangle bounds = board.getContentPane().getBounds();
		    if(!(currentPoint.y + size.height > bounds.height) &&
		    		!(currentPoint.x + size.width > bounds.width) &&
		    		!(currentPoint.y < bounds.y) &&  !(currentPoint.x < bounds.x))
		    	this.setLocation(currentPoint);
		    Point point = new Point((int)(currentPoint.x + (this.getWidth()*0.5)), (int)(currentPoint.y + (this.getHeight()*0.5)));
		    Component component = board.getContentPane().getComponentAt(point);
			if(component instanceof ChessPositionPanel) {
				ChessPositionPanel destPosition = (ChessPositionPanel) component;
					
				if(prevSquare == null || !prevSquare.equals(destPosition)) {
					if(prevSquare != null)
						prevSquare.setBorder(prevBorder);
					prevSquare = destPosition;prevBorder = prevSquare.getBorder();
					destPosition.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		this.requestFocusInWindow();
		if(e.getButton() != MouseEvent.BUTTON1) return;
		mouseDownCompCoords = e.getPoint();
	    Point currCoords = this.getLocation();
	    Point newMouseDownCompCoords = e.getPoint();
	    currentPoint = new Point(currCoords.x + newMouseDownCompCoords.x - mouseDownCompCoords.x, currCoords.y + newMouseDownCompCoords.y - mouseDownCompCoords.y);
		canDrag = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() != MouseEvent.BUTTON1) return;
		if(prevSquare != null)
			prevSquare.setBorder(prevBorder);
	    Point point = new Point((int)(currentPoint.x + (this.getWidth()*0.5)), (int)(currentPoint.y + (this.getHeight()*0.5)));
		Component component = board.getContentPane().getComponentAt(point);
		if(component instanceof ChessPositionPanel && possibleMoves != null) {
			ChessPositionPanel destPosition = (ChessPositionPanel) component;
			List<ChessMove> list = possibleMoves.getPossibleMoves();
			if(list.size() != 0) {
				boolean b = true;
				for(ChessMove move : list) {
					if(destPosition.getPosition().equals(move.getMoveToPosition())) {
						board.switchTurn(move);
						b = false;
						break;
					}
				}
				if(b)
					this.requestFocusInWindow();
			}
		}
		canDrag = false;
		mouseDownCompCoords = new Point(0,0);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));		
	}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void ancestorAdded(AncestorEvent event) {
		Component component = this.getParent();
		if(component instanceof ChessPositionPanel && !(component instanceof JLayeredPane)) {
			this.position = (ChessPositionPanel) component;
		}
	}

	@Override
	public void ancestorRemoved(AncestorEvent event) {}

	@Override
	public void ancestorMoved(AncestorEvent event) {}
	
	
	@Override
	public String toString() {
		return piece != null ? piece.toString() : "Panel have no Piece";
	}
	
}