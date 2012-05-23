package minesweeper;
import java.awt.geom.Point2D;

/*
 * マスの下の状態
 */
public class MasType {
	// マスの周りの地雷の数や地雷かどうか
	private int aroundMineCount;
	// マスの座標
	private Point2D pos;

	// 残りの1～8は周りにある地雷の数を表す
	public static final int NONE = 0;
	public static final int MINE = 9;

	// 地雷設置前なのでデフォルトでは地雷の数は0
	public MasType( final int x, final int y ){
		aroundMineCount = NONE;
		pos = new Point2D.Double( x, y );
	}

	// そのマスの状態の取得
	public int getTypeNumber(){
		return aroundMineCount;
	}

	// 座標の取得
	public final Point2D getPos(){
		return pos;
	}

	// 地雷に設定する
	public void setMine(){
		aroundMineCount = MINE;
	}

	// count個地雷があることを設定する
	public void setAroundMineCount( final int count ){
		aroundMineCount = count;
	}
}