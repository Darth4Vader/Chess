package ChessUIJavaFX;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseListener;

import javax.naming.InitialContext;
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import ChessDataTypes.ChessData.TurnColor;
import ChessDataTypes.ChessPiece;
import ChessDataTypes.ChessPosition;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ChessPositionPanel extends BorderPane implements RemoveDragListener, ContainerListener, MouseListener {
	
	private ChessMain board;
	private final ChessPosition position;
	private ChessPiecePanel piece;
	private boolean isPossible;
	
	public ChessPositionPanel(ChessPosition position, ChessMain board) {
		this.position = position;
		this.isPossible = false;
		this.board = board;
		TurnColor color = position.getBackgroundColor();
		this.setBackground(Background.fill(color == TurnColor.WHITE ? board.getWhiteColor() : board.getBlackColor()));
		this.setBorder(Border.stroke(Color.GRAY));
		this.addContainerListener(this);
		this.setOnMousePressed(e -> {
            if(piece != null) {
                if(e.getButton() == MouseButton.SECONDARY)
                    piece.requestFocusInWindow();
            }
            else
                this.requestFocus();
        });
		this.getChildren().addListener((ListChangeListener<javafx.scene.Node>) c -> {
            while(c.next()) {
                if(c.wasAdded()) {
                    for(Node node : c.getAddedSubList()) {
	                    if(node instanceof ChessPiecePanel) {
	                        piece = (ChessPiecePanel) node;
	                        piece.setChessPositionPanel(this);
	                    }
                    }
                }
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
			this.setCenter(this.piece);
		}
		else
			this.piece = null;
	}
	
	public void setChessPiecePanel(ChessPiecePanel piece) {
		removeAll();
		this.piece = piece;
		if(this.piece != null)
			this.setCenter(this.piece);
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
			this.setCenter(null);
			board.refreshFrame();
		}
		return board.getLayeredPane();
		
	}

	@Override
	public void addAfterDragged() {
		if(piece != null) {
			this.setCenter(this.piece);
			board.refreshFrame();
		}
	}

	@Override
	public void componentAdded(ContainerEvent e) {
		if(!board.getChessBoard().isGameActivate())
			board.addVictoryPanel();
	}
	
	public boolean equals(ChessPositionPanel chessSquare) {
		return getPosition().equals(chessSquare.getPosition());
	}
}