package game;

/*
 * ��Փx�����i�[����N���X
 */
public class DiffcultInfo {
	// �Ֆʂ̊e����
	private int masWidth;
	private int masHeight;
	private int mineNum;

	// ��Փx��ݒ肷��
	public DiffcultInfo( final int w, final int h, final int m ){
		masWidth = w;
		masHeight = h;
		mineNum = m;
	}

	// �Ֆʂ̏c�̃}�X����Ԃ�
	public int getMasWidth(){
		return masWidth;
	}
	// �Ֆʂ̉��̃}�X����Ԃ�
	public int getMasHeight(){
		return masHeight;
	}
	// �Ֆʂɂ���n���̐���Ԃ�
	public int getMineNum(){
		return mineNum;
	}
}