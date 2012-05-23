package application;
import java.awt.Color;
import game.Minesweeper;;

/*
 * アプリケーション管理クラス
 */
public class Manager {
	// アプリケーションハンドル
	private static Minesweeper application	= null;
	// ウインドウの幅
	private static int windowWidth = 0;
	// ウインドウの高さ
	private static int windowHeight = 0;

	// ゲームの初期化
	public static void initialize( final Minesweeper app, final int width, final int height ){
		// ウインドウの高さと幅の設定
		windowWidth		= width;
		windowHeight	= height;
		// アプリケーションハンドルを設定する
		application = app;
		// ウインドウサイズの設定
		setWindowSize( width, height );
		// 背景色の設定
		application.setBackground( Color.white );
	}
	// アプリケーションハンドルの取得
	public static final Minesweeper getApp(){
		return application;
	}
	// ウインドウの幅の取得
	public static int getWindowWidth(){
		return windowWidth;
	}
	// ウインドウの高さの取得
	public static int getWindowHeight(){
		return windowHeight;
	}
	// ウインドウの幅と高さの設定
	public static void setWindowSize( final int width, final int height ){
		application.setSize( width, height );
	}
}