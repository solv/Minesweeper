package minesweeper;
import java.awt.geom.Point2D;

/*
 * 開かれるマスの状態
 */
public class Mas {
	// マスの状態
	public static enum State {
		NONE,
		SELECTED,
		OPENED,
		FLAG,
	}
	private State m_State; // 現在の状態
	private Point2D m_Pos; // 表示する座標

	// 生成時は何も無いマスとなる
	public Mas( final int x, final int y ){
		setState(State.NONE);
		m_Pos = new Point2D.Double( x, y );
	}

	// それぞれのマスの状態の設定を行なう
	// 簡易化するためメソッドを呼ぶだけで設定出来るようになっている
	public void setNoneMas(){
		setState(State.NONE);
	}
	public void setSelectedMas(){
		setState(State.SELECTED);
	}
	public void setOpendMas(){
		setState(State.OPENED);
	}
	public void setFlagMas(){
		setState(State.FLAG);
	}

	// 状態の設定
	private void setState( final State state ){
		m_State = state;
	}

	// 状態の取得
	public final State getState(){
		return m_State;
	}

	// 座標の取得
	public final Point2D getPos(){
		return m_Pos;
	}
}