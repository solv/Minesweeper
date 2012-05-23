package game;

/*
 * 難易度情報を格納するクラス
 */
public class DiffcultInfo {
	// 盤面の各種情報
	private int masWidth;
	private int masHeight;
	private int mineNum;

	// 難易度を設定する
	public DiffcultInfo( final int w, final int h, final int m ){
		masWidth = w;
		masHeight = h;
		mineNum = m;
	}

	// 盤面の縦のマス数を返す
	public int getMasWidth(){
		return masWidth;
	}
	// 盤面の横のマス数を返す
	public int getMasHeight(){
		return masHeight;
	}
	// 盤面にある地雷の数を返す
	public int getMineNum(){
		return mineNum;
	}
}