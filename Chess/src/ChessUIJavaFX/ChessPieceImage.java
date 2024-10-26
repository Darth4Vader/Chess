package ChessUIJavaFX;

import ChessDataTypes.ChessData;
import ChessDataTypes.ChessPiece;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import ChessDataTypes.ChessData.Piece;
import ChessDataTypes.ChessData.TurnColor;

public class ChessPieceImage extends CanvasPane {

	private final ChessPiece piece;
    private Image image;
	
	public ChessPieceImage(ChessPiece piece) {
		this.piece = piece;
		loadImage();
		//this.setOpacity(0);
	}
	
	@Override
	protected void paintComponent() {
		int width = (int)(getWidth()*0.7);
		int height = (int)(getHeight()*0.7);
		System.out.println("width: " + width + " height: " + height);
		int x = (int)((getWidth() - width)*0.5);
		int y = (int)((getHeight() - height)*0.5);
		GraphicsContext gc = getCanvas().getGraphicsContext2D();
		gc.clearRect(0, 0, getWidth(), getHeight());
		gc.drawImage(image, x, y, width, height);
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
