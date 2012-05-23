package minesweeper;
import java.awt.geom.Point2D;

/*
 * �}�X�̉��̏��
 */
public class MasType {
	// �}�X�̎���̒n���̐���n�����ǂ���
	private int aroundMineCount;
	// �}�X�̍��W
	private Point2D pos;

	// �c���1�`8�͎���ɂ���n���̐���\��
	public static final int NONE = 0;
	public static final int MINE = 9;

	// �n���ݒu�O�Ȃ̂Ńf�t�H���g�ł͒n���̐���0
	public MasType( final int x, final int y ){
		aroundMineCount = NONE;
		pos = new Point2D.Double( x, y );
	}

	// ���̃}�X�̏�Ԃ̎擾
	public int getTypeNumber(){
		return aroundMineCount;
	}

	// ���W�̎擾
	public final Point2D getPos(){
		return pos;
	}

	// �n���ɐݒ肷��
	public void setMine(){
		aroundMineCount = MINE;
	}

	// count�n�������邱�Ƃ�ݒ肷��
	public void setAroundMineCount( final int count ){
		aroundMineCount = count;
	}
}