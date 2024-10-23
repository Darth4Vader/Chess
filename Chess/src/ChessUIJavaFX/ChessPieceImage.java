package ChessUIJavaFX;

import ChessDataTypes.ChessData;
import ChessDataTypes.ChessPiece;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import ChessDataTypes.ChessData.Piece;
import ChessDataTypes.ChessData.TurnColor;

public class ChessPieceImage extends JPanel {

	private final ChessPiece piece;
    private Image image;
	
	public ChessPieceImage(ChessPiece piece) {
		this.piece = piece;
		loadImage();
		this.setOpaque(false);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Dimension size = getSize();
		int width = (int)(getWidth()*0.7);
		int height = (int)(getHeight()*0.7);
		System.out.println("width: " + width + " height: " + height);
		int x = (int)((getWidth() - width)*0.5);
		int y = (int)((getHeight() - height)*0.5);
		g.drawImage(image, x, y, width, height, this);
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
	
	protected void loadImage() {
		this.image = ChessMain.loadImage("/chess_save/" + ChessData.getFileName(piece) + ".png");
	}
	
	@Override
	public String toString() {
		return piece != null ? piece.toString() : "Panel have no Piece";
	}
}
