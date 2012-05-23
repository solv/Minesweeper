package application;
import java.awt.Color;
import game.Minesweeper;;

/*
 * �A�v���P�[�V�����Ǘ��N���X
 */
public class Manager {
	// �A�v���P�[�V�����n���h��
	private static Minesweeper application	= null;
	// �E�C���h�E�̕�
	private static int windowWidth = 0;
	// �E�C���h�E�̍���
	private static int windowHeight = 0;

	// �Q�[���̏�����
	public static void initialize( final Minesweeper app, final int width, final int height ){
		// �E�C���h�E�̍����ƕ��̐ݒ�
		windowWidth		= width;
		windowHeight	= height;
		// �A�v���P�[�V�����n���h����ݒ肷��
		application = app;
		// �E�C���h�E�T�C�Y�̐ݒ�
		setWindowSize( width, height );
		// �w�i�F�̐ݒ�
		application.setBackground( Color.white );
	}
	// �A�v���P�[�V�����n���h���̎擾
	public static final Minesweeper getApp(){
		return application;
	}
	// �E�C���h�E�̕��̎擾
	public static int getWindowWidth(){
		return windowWidth;
	}
	// �E�C���h�E�̍����̎擾
	public static int getWindowHeight(){
		return windowHeight;
	}
	// �E�C���h�E�̕��ƍ����̐ݒ�
	public static void setWindowSize( final int width, final int height ){
		application.setSize( width, height );
	}
}