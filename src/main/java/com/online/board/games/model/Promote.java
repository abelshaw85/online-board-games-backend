package com.online.board.games.model;

public class Promote extends Action {
	private RowColPosition promoteLocation;
	private String promotionPieceName;
	
	public Promote() {
		super("Promote");
	}

	public Promote(RowColPosition promoteLocation, String promotionPieceName) {
		this();
		this.promoteLocation = promoteLocation;
		this.promotionPieceName = promotionPieceName;
	}

	public RowColPosition getPromoteLocation() {
		return promoteLocation;
	}

	public void setPromoteLocation(RowColPosition promoteLocation) {
		this.promoteLocation = promoteLocation;
	}

	public String getPromotionPieceName() {
		return promotionPieceName;
	}

	public void setPromotionPieceName(String promotionPieceName) {
		this.promotionPieceName = promotionPieceName;
	}

	@Override
	public String toString() {
		return "Promote [promoteLocation=" + promoteLocation + ", promotionPiece=" + promotionPieceName + "]";
	}
}
