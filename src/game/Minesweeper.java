package game;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * アプリケーションを基底クラス
 */
public class Minesweeper extends JApplet implements ActionListener {
	private static final long serialVersionUID = -6524026658046122714L;

	// 盤面の縦横サイズと合計地雷数
	// これが難易度の区分となる
	private static final DiffcultInfo[] diffcultInfo = {
		new DiffcultInfo(9,9,10),   new DiffcultInfo(16,16,40),
		new DiffcultInfo(30,16,99), new DiffcultInfo(40,30,200)
	};

	// 定数関連
	public static final int MAS_SIZE = 18;
	public static final int WINDOW_HEIGHT_PANEL = 26 + 36;
	public static final int MESSAGE_LENGTH = 23;
	public static final String START_STR = "start";
	public static final String IMAGE_EXT = ".png";
	public static final String CREAR_MESSAGE = "地雷を全て除去しました！\nGAME CREAR";
	public static final String END_MESSAGE = "地雷を踏んでしまいました！\nGAME OVER";
	public static final String GAME_TITLE = "MINE SWEEPER";

	// コンポーネント関連
	private static final JLabel informationMessage = new JLabel();
	private static final JButton startButton = new JButton(START_STR);
	private static final JPanel informationPanel = new JPanel();
	private static final JPanel buttonPanel = new JPanel();
	private static final JComboBox diffcultList = new JComboBox();

	// ゲーム管理関連
	// 初期化されている値はゲーム開始時になるもの
	public static int maxWidthMas = diffcultInfo[0].getMasWidth();
	public static int maxHeightMas = diffcultInfo[0].getMasHeight();
	public static int maxMineNum = diffcultInfo[0].getMineNum();
	private static int nowDiffcultListNumber = 0;
	private static minesweeper.Manager managerMine = null;

	// 初期化関連
	@Override public void init(){
		// アプリケーションの初期化
		application.Manager.initialize(
			this, MAS_SIZE * maxWidthMas,
			MAS_SIZE * maxHeightMas + WINDOW_HEIGHT_PANEL
		);
		managerMine = new minesweeper.Manager( maxWidthMas, maxHeightMas );
		this.addMouseListener(managerMine);
		this.addMouseMotionListener(managerMine);

		// コンテナペインの取得
		Container containerPain = this.getContentPane();
		// コンポーネントの配置
		containerPain.add(informationPanel,"North");
		containerPain.add(managerMine);
		containerPain.add(buttonPanel,"South");

		// コンポーネントの各種設定
		informationPanel.add(informationMessage);
		informationPanel.setBackground(Color.red);
		informationMessage.setText("地雷は残り"+ managerMine.getMineNum() +"個です");
		managerMine.setBackground(Color.DARK_GRAY);
		buttonPanel.add(startButton);
		buttonPanel.setBackground(Color.BLUE);
		startButton.addActionListener(this);

		diffcultList.addItem("初級");
		diffcultList.addItem("中級");
		diffcultList.addItem("上級");
		diffcultList.addItem("超上級");
		diffcultList.addActionListener(this);
		buttonPanel.add(diffcultList);
		nowDiffcultListNumber = diffcultList.getSelectedIndex();
	}

	// スタートボタンが押された時
	@Override public void actionPerformed(final ActionEvent e){
		if( e.getActionCommand().equals(START_STR) ){
			// 既存の難易度と違う物だった場合
			// 難易度情報の更新やウインドウサイズの変更、ゲーム自体の初期化を行っている
			if( diffcultList.getSelectedIndex() != nowDiffcultListNumber  ){
				nowDiffcultListNumber = diffcultList.getSelectedIndex();
				maxWidthMas = diffcultInfo[nowDiffcultListNumber].getMasWidth();
				maxHeightMas = diffcultInfo[nowDiffcultListNumber].getMasHeight();
				maxMineNum = diffcultInfo[nowDiffcultListNumber].getMineNum();
				application.Manager.setWindowSize(
					MAS_SIZE * maxWidthMas,
					MAS_SIZE * maxHeightMas + WINDOW_HEIGHT_PANEL
				);
				setInformationMessage(maxMineNum);
				managerMine.newGetMasList( maxWidthMas, maxHeightMas );
			}
			managerMine.initGame();
		}
	}

	// 上部メッセージのテキストの設定
	public void setInformationMessage( final int mineNum ){
		informationMessage.setText("地雷は残り"+ managerMine.getMineNum() +"個です");
	}
}