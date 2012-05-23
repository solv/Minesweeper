package minesweeper;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.*;
import game.Minesweeper;

/*
 * ゲームの管理クラス
 * 入力のためのリスナーと描画のためのパネルを継承している
 */
public class Manager extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 9182217648537328479L;

	// 開かれるマスの状態とマスの下の状態
	private final ArrayList<Mas> masList = new ArrayList<Mas>();
	private final ArrayList<MasType> masTypeList = new ArrayList<MasType>();

	// ゲームの開始可のフラグ
	private boolean runFlag;

	// NONEマスを開いていくために使用するメモ化用配列
	private boolean [][] openNoneMas_dp_dfs;

	// 起動時の初期化
	public Manager( final int width_num, final int height_num ){
		// 画像の読み込み
		for( int i = 1; i < 4; ++i ){
			drawer.Texture.addImage( "mas" + i + Minesweeper.IMAGE_EXT);
		}
		for( int i = 0; i < 10; ++i ){
			drawer.Texture.addImage( "masType" + i + Minesweeper.IMAGE_EXT);
		}
		// メモリの確保
		newGetMasList( width_num, height_num );
		// マスなどの初期化
		initGame();
	}
	// マスなどの配列の確保(マスの数で可変させる)
	public void newGetMasList( final int width_num, final int height_num ){
		// メモリを解放する
		masList.clear();
		masTypeList.clear();

		// 開かれるマスの状態とマスの下の状態のメモリを確保
		for( int w = 0; w < width_num; ++w ){
			for( int h = 0; h < height_num; ++h ){
				masList.add( new Mas( w * Minesweeper.MAS_SIZE, h * Minesweeper.MAS_SIZE ) );
				masTypeList.add( new MasType( w * Minesweeper.MAS_SIZE, h * Minesweeper.MAS_SIZE ) );
			}
		}

		// メモ化再帰用の配列の確保
		openNoneMas_dp_dfs = new boolean[width_num][height_num];
	}

	// マスなどの初期化
	public void initGame(){
		// 開始前なのでfalseにする
		runFlag = false;

		// メモ化再帰用配列の初期化
		for( int i = 0; i < openNoneMas_dp_dfs.length; ++i ){
			for( int j = 0; j < openNoneMas_dp_dfs[i].length; ++j ){
				openNoneMas_dp_dfs[i][j] =false;
			}
		}

		// マスをNONEにして、その下の情報を全て地雷無しにする
		for( Mas mas : masList ){
			mas.setNoneMas();
		}
		for( MasType masType : masTypeList ){
			masType.setAroundMineCount(0);
		}
	}

	// 最初に開いたマスとその周囲8マスに地雷を設置しないようにする
	private boolean aroundNoPutMine( int mx, int my, int x, int y ){
		for( int xx = -1; xx <= 1; ++xx ){
			for( int yy = -1; yy <= 1; ++yy ){
				if( x == (mx + xx) && y == (my + yy) ){ return true; }
			}
		}
		return false;
	}

	// ゲーム開始時に地雷を配置する
	private void putMine( final int mx, final int my ){
		// 指定個数分の地雷を配置する
		for( int i = 0; i < Minesweeper.maxMineNum; ++i ){
			// 地雷の位置が重複しないように置けない場合は配置をやり直す
			while(true){
				// 地雷の配置場所を決める
				final int x = (int)(Math.random() * Minesweeper.maxWidthMas);
				final int y = (int)(Math.random() * Minesweeper.maxHeightMas);

				// クリックした位置には地雷は設置しない
				if( aroundNoPutMine( mx, my, x, y ) ){
					continue;
				}

				// 指定位置に地雷を設置する
				final MasType masType = masTypeList.get( adjust(x,y) );

				if( masType.getTypeNumber() == MasType.NONE ){
					masType.setMine();
					break;
				}
			}
		}

		// 周囲にある地雷の数情報を設定する
		for( int x = 0; x < Minesweeper.maxWidthMas; ++x ){
			for( int y = 0; y < Minesweeper.maxHeightMas; ++y ){
				final MasType masType = masTypeList.get(adjust(x,y));

				// そのマスが地雷だったら無視
				if( masType.getTypeNumber() == MasType.MINE ){
					continue;
				}

				// 地雷の個数
				int mine = 0;

				// 周囲8マスを調べる
				for( int xx = -1; xx <= 1; ++xx ){
					for( int yy = -1; yy <= 1; ++yy ){
						final int tx = x + xx;
						final int ty = y + yy;
						if( checkMasOutOfRange( tx, ty ) &&
							masTypeList.get(adjust( tx, ty )).getTypeNumber() == MasType.MINE
						){
							++mine;
						}
					}
				}
				// 地雷の数を設定する
				masType.setAroundMineCount(mine);
			}
		}
	}

	// 深さ優先探索(dfs)とメモ化を用いて地雷が無いと確定しているマスを全て開く
	private void openNoneMas_dfs( final int x, final int y ){
		// 周囲8マスを調べる
		for( int vx = -1; vx <= 1; ++vx ){
			for( int vy = -1; vy <= 1; ++vy ){
				// 位置を移動させたX,Y
				final int tx = x + vx;
				final int ty = y + vy;

				// 中心位置なら無視 または 移動させた位置が配列の範囲内かどうか または 探索済みなら無視
				if( (vx == 0 && vy == 0) || !checkMasOutOfRange( tx, ty ) ||
					(openNoneMas_dp_dfs[tx][ty] == true)
				){
					continue;
				}

				openNoneMas_dp_dfs[tx][ty] = true; 			// 探索済みにする
				masList.get(adjust(tx,ty)).setOpendMas();	// マスを開く

				// 次の位置のマスも地雷が無ければ探索を続ける
				if( masTypeList.get(adjust(tx,ty)).getTypeNumber() == MasType.NONE ){
					openNoneMas_dfs( tx, ty );
				}
			}
		}
	}

	// ゲームが終了しているかどうか
	private final boolean checkGameEnd(){
		// 地雷を踏んたかどうか
		for( int i = 0; i < masTypeList.size(); ++i ){
			final MasType masType = masTypeList.get(i);
			final Mas mas = masList.get(i);

			// 開いたマスに地雷があったらゲームオーバー
			if( masType.getTypeNumber() == MasType.MINE && mas.getState() == Mas.State.OPENED ){
				// ゲームオーバーメッセージの表示
				JOptionPane.showMessageDialog(
					null, Minesweeper.END_MESSAGE,
					Minesweeper.GAME_TITLE, JOptionPane.PLAIN_MESSAGE
				);
				return true;
			}
		}

		// 全ての地雷を除去したかどうか
		for( int i = 0; i < masTypeList.size(); ++i ){
			final MasType masType = masTypeList.get(i);
			final Mas mas = masList.get(i);
			if( masType.getTypeNumber() == MasType.MINE ){
				continue;
			}
			if( mas.getState() == Mas.State.NONE ){
				return false;
			}
		}

		// ゲームクリアメッセージの表示
		JOptionPane.showMessageDialog(
			null, Minesweeper.CREAR_MESSAGE,
			Minesweeper.GAME_TITLE, JOptionPane.PLAIN_MESSAGE
		);

		return true;
	}

	// 左クリックでマスを開き、右クリックで旗を立てる
	@Override public void mouseClicked( final MouseEvent e ){
		final int mouseX = getMouseX(e);
		final int mouseY = getMouseY(e);

		// 初回クリック時に地雷を配置する
		if( !runFlag ){
			putMine( (int)(mouseX/Minesweeper.MAS_SIZE), (int)(mouseY/Minesweeper.MAS_SIZE) );
			runFlag = true;
		}

		for( Mas mas : masList ){
			final Point2D pos = mas.getPos();
			final Mas.State state = mas.getState();

			if( collisionMouseAndMas(pos,mouseX,mouseY) ){
				// 左クリック時
				if( e.getButton() == MouseEvent.BUTTON1 ){
					// 旗か開いているマスではない場合、マスを開く
					if( !(state == Mas.State.FLAG || state == Mas.State.OPENED) ){
						final int x = (int)(mas.getPos().getX() / Minesweeper.MAS_SIZE);
						final int y = (int)(mas.getPos().getY() / Minesweeper.MAS_SIZE);

						mas.setOpendMas();

						if( masTypeList.get(adjust(x,y)).getTypeNumber() == MasType.NONE ){
							openNoneMas_dfs(x,y); // 周囲8マスに地雷が無いマスの周囲を全て開く
						}
					}
					break;
				}
				// 右クリック時
				else if( e.getButton() == MouseEvent.BUTTON3 ){
					// 旗が立っていないならば旗を立てる
					if( state == Mas.State.SELECTED ){
						// 最大地雷数よりも少ない旗数ならば立てられる
						if( getMineNum() > 0 ){
							mas.setFlagMas();
						}
					}
					// 旗が立っていたら元に戻す
					else if( state == Mas.State.FLAG ){
						mas.setNoneMas();
					}
					break;
				}
			}
		}

		// 地雷数のメッセージを更新する
		application.Manager.getApp().setInformationMessage(getMineNum());
		// 再描画
		repaint();

		// ゲームが終了したかをチェックして、終了ならばゲームを初期化する
		if( checkGameEnd() ){
			initGame();
			runFlag = false;
		}
	}

	// マウスの位置でマスを選択状態にする
	@Override public void mouseMoved( final MouseEvent e ){
		// 旗か開いている状態以外のマスで選択されているマスを選択状態にする
		for( Mas mas : masList ){
			if( !(mas.getState() == Mas.State.FLAG || mas.getState() == Mas.State.OPENED) ){
				mas.setNoneMas();
				if( collisionMouseAndMas( mas.getPos(), getMouseX(e), getMouseY(e) ) ){
					mas.setSelectedMas();
				}
			}
		}
		repaint(); // 再描画
	}

	// 描画
	public void paint( final Graphics g ){
		// アプリケーションハンドル
		final Minesweeper app = application.Manager.getApp();

		// マスの下の状態に合わせて描画する画像を変える
		for( MasType masType : masTypeList ){
			g.drawImage(
				drawer.Texture.getTexture( "masType" + masType.getTypeNumber() + Minesweeper.IMAGE_EXT ),
				(int)masType.getPos().getX(), (int)masType.getPos().getY(), app
			);
		}

		// マスの状態に合わせて描画する画像を変える
		for( Mas mas : masList ){
			final int x = (int)mas.getPos().getX();
			final int y = (int)mas.getPos().getY();
			final Mas.State state = mas.getState();

			if( state == Mas.State.NONE ){
				g.drawImage( drawer.Texture.getTexture("mas1" + Minesweeper.IMAGE_EXT), x, y, app );
			}else if( state == Mas.State.SELECTED ){
				g.drawImage( drawer.Texture.getTexture("mas2" + Minesweeper.IMAGE_EXT), x, y, app );
			}else if( state == Mas.State.FLAG ){
				g.drawImage( drawer.Texture.getTexture("mas3" + Minesweeper.IMAGE_EXT), x, y, app );
			}
		}
	}

	// マウスの位置がマスの中に収まっているかをチェックする
	private final boolean collisionMouseAndMas( final Point2D mas_pos, final int mouseX, final int mouseY ){
		if( mas_pos.getX() <= mouseX && mouseX < (mas_pos.getX() + Minesweeper.MAS_SIZE) &&
			mas_pos.getY() <= mouseY && mouseY < (mas_pos.getY() + Minesweeper.MAS_SIZE)
		){
			return true;
		}
		return false;
	}

	// 推定地雷残数
	public final int getMineNum(){
		int mine = Minesweeper.maxMineNum;
		for( Mas mas : masList ){
			if( mas.getState() == Mas.State.FLAG ){
				--mine;
			}
		}
		return mine;
	}

	// マウスの位置Xの取得
	private final int getMouseX( final MouseEvent e ){
		return e.getX();
	}

	// マウスの位置Yの取得
	private final int getMouseY( final MouseEvent e ){
		return e.getY() - 26; // パネルの位置分ずらす
	}

	// X,Yの組み合わせを1次元に変換する
	private final int adjust( final int x, final int y ){
		return x * Minesweeper.maxHeightMas + y;
	}

	// X,Yの組み合わせが配列の範囲外の出ているかチェックする
	private final boolean checkMasOutOfRange( final int x, final int y ){
		return (0 <= x) && (x < Minesweeper.maxWidthMas) && (0 <= y) && (y < Minesweeper.maxHeightMas);
	}

	@Override public void mousePressed (MouseEvent e){}
	@Override public void mouseReleased(MouseEvent e){}
	@Override public void mouseEntered (MouseEvent e){}
	@Override public void mouseExited  (MouseEvent e){}
	@Override public void mouseDragged (MouseEvent e){}
}