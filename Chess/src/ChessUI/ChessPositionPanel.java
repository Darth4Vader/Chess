package ChessUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.naming.InitialContext;
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import ChessDataTypes.ChessData.TurnColor;
import ChessDataTypes.ChessPiece;
import ChessDataTypes.ChessPosition;

public class ChessPositionPanel extends JPanel implements RemoveDragListener, ContainerListener, MouseListener {
	
	private ChessMain board;
	private final ChessPosition position;
	private ChessPiecePanel piece;
	private boolean isPossible;
	
	public ChessPositionPanel(ChessPosition position, ChessMain board) {
		this.position = position;
		this.isPossible = false;
		this.board = board;
		TurnColor color = position.getBackgroundColor();
		this.setLayout(new BorderLayout());
		if(color == TurnColor.WHITE)
			this.setBackground(board.getWhiteColor());
		else
			this.setBackground(board.getBlackColor());
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.addMouseListener(this);
		this.addContainerListener(this);
		initiateChessPiecePanel();
	}
	
	public void setIfPossible(boolean isPossible) {
		this.isPossible = isPossible;
	}
	
	@Override 
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(isPossible) {
			g.setColor(new Color(128, 128, 128, 150));
			if(piece == null) {
				int width = (int)(getWidth()*0.3);
				int height = (int)(getHeight()*0.3);
				int x = (int)((getWidth() - width)*0.5);
				int y = (int)((getHeight() - height)*0.5);
				g.drawOval(x, y, width, height);
				g.fillOval(x, y, width, height);
			}
			else {
				g.drawOval(0, 0, getWidth(), getHeight());
			}
		}
	}
	
	private void initiateChessPiecePanel() {
		ChessPiece piece = position.getChessPiece();
		if(piece != null) {
			this.piece = new ChessPiecePanel(piece, board);
			this.piece.setChessPositionPanel(this);
			this.add(this.piece, BorderLayout.CENTER);
		}
		else
			this.piece = null;
	}
	
	public void setChessPiecePanel(ChessPiecePanel piece) {
		removeAll();
		this.piece = piece;
		if(this.piece != null)
			this.add(this.piece, BorderLayout.CENTER);
		board.refreshFrame();
	}
	
	public ChessPosition getPosition() {
		return position;
	}
	
	public boolean isEmpty() {
		return this.piece == null;
	}
	
	public ChessPiecePanel getChessPiecePanel() {
		return piece;
	}
	
	@Override
	public JLayeredPane removeWhenDragged() {
		if(piece != null) {
			this.remove(piece);
			board.refreshFrame();
		}
		return board.getLayeredPane();
		
	}

	@Override
	public void addAfterDragged() {
		if(piece != null) {
			this.add(piece, BorderLayout.CENTER);
			board.refreshFrame();
		}
	}

	@Override
	public void componentAdded(ContainerEvent e) {
		if(!board.getChessBoard().isGameActivate())
			board.addVictoryPanel();
	}

	@Override
	public void componentRemoved(ContainerEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		if(piece != null) {
			if(e.getButton() == MouseEvent.BUTTON3)
				piece.requestFocusInWindow();
		}
		else
			this.requestFocusInWindow();
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	public boolean equals(ChessPositionPanel chessSquare) {
		return getPosition().equals(chessSquare.getPosition());
	}
}