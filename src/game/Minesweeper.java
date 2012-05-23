package game;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * �A�v���P�[�V���������N���X
 */
public class Minesweeper extends JApplet implements ActionListener {
	private static final long serialVersionUID = -6524026658046122714L;

	// �Ֆʂ̏c���T�C�Y�ƍ��v�n����
	// ���ꂪ��Փx�̋敪�ƂȂ�
	private static final DiffcultInfo[] diffcultInfo = {
		new DiffcultInfo(9,9,10),   new DiffcultInfo(16,16,40),
		new DiffcultInfo(30,16,99), new DiffcultInfo(40,30,200)
	};

	// �萔�֘A
	public static final int MAS_SIZE = 18;
	public static final int WINDOW_HEIGHT_PANEL = 26 + 36;
	public static final int MESSAGE_LENGTH = 23;
	public static final String START_STR = "start";
	public static final String IMAGE_EXT = ".png";
	public static final String CREAR_MESSAGE = "�n����S�ď������܂����I\nGAME CREAR";
	public static final String END_MESSAGE = "�n���𓥂�ł��܂��܂����I\nGAME OVER";
	public static final String GAME_TITLE = "MINE SWEEPER";

	// �R���|�[�l���g�֘A
	private static final JLabel informationMessage = new JLabel();
	private static final JButton startButton = new JButton(START_STR);
	private static final JPanel informationPanel = new JPanel();
	private static final JPanel buttonPanel = new JPanel();
	private static final JComboBox diffcultList = new JComboBox();

	// �Q�[���Ǘ��֘A
	// ����������Ă���l�̓Q�[���J�n���ɂȂ����
	public static int maxWidthMas = diffcultInfo[0].getMasWidth();
	public static int maxHeightMas = diffcultInfo[0].getMasHeight();
	public static int maxMineNum = diffcultInfo[0].getMineNum();
	private static int nowDiffcultListNumber = 0;
	private static minesweeper.Manager managerMine = null;

	// �������֘A
	@Override public void init(){
		// �A�v���P�[�V�����̏�����
		application.Manager.initialize(
			this, MAS_SIZE * maxWidthMas,
			MAS_SIZE * maxHeightMas + WINDOW_HEIGHT_PANEL
		);
		managerMine = new minesweeper.Manager( maxWidthMas, maxHeightMas );
		this.addMouseListener(managerMine);
		this.addMouseMotionListener(managerMine);

		// �R���e�i�y�C���̎擾
		Container containerPain = this.getContentPane();
		// �R���|�[�l���g�̔z�u
		containerPain.add(informationPanel,"North");
		containerPain.add(managerMine);
		containerPain.add(buttonPanel,"South");

		// �R���|�[�l���g�̊e��ݒ�
		informationPanel.add(informationMessage);
		informationPanel.setBackground(Color.red);
		informationMessage.setText("�n���͎c��"+ managerMine.getMineNum() +"�ł�");
		managerMine.setBackground(Color.DARK_GRAY);
		buttonPanel.add(startButton);
		buttonPanel.setBackground(Color.BLUE);
		startButton.addActionListener(this);

		diffcultList.addItem("����");
		diffcultList.addItem("����");
		diffcultList.addItem("�㋉");
		diffcultList.addItem("���㋉");
		diffcultList.addActionListener(this);
		buttonPanel.add(diffcultList);
		nowDiffcultListNumber = diffcultList.getSelectedIndex();
	}

	// �X�^�[�g�{�^���������ꂽ��
	@Override public void actionPerformed(final ActionEvent e){
		if( e.getActionCommand().equals(START_STR) ){
			// �����̓�Փx�ƈႤ���������ꍇ
			// ��Փx���̍X�V��E�C���h�E�T�C�Y�̕ύX�A�Q�[�����̂̏��������s���Ă���
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

	// �㕔���b�Z�[�W�̃e�L�X�g�̐ݒ�
	public void setInformationMessage( final int mineNum ){
		informationMessage.setText("�n���͎c��"+ managerMine.getMineNum() +"�ł�");
	}
}