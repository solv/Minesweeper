package minesweeper;
import java.awt.geom.Point2D;

/*
 * �J�����}�X�̏��
 */
public class Mas {
	// �}�X�̏��
	public static enum State {
		NONE,
		SELECTED,
		OPENED,
		FLAG,
	}
	private State m_State; // ���݂̏��
	private Point2D m_Pos; // �\��������W

	// �������͉��������}�X�ƂȂ�
	public Mas( final int x, final int y ){
		setState(State.NONE);
		m_Pos = new Point2D.Double( x, y );
	}

	// ���ꂼ��̃}�X�̏�Ԃ̐ݒ���s�Ȃ�
	// �ȈՉ����邽�߃��\�b�h���ĂԂ����Őݒ�o����悤�ɂȂ��Ă���
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

	// ��Ԃ̐ݒ�
	private void setState( final State state ){
		m_State = state;
	}

	// ��Ԃ̎擾
	public final State getState(){
		return m_State;
	}

	// ���W�̎擾
	public final Point2D getPos(){
		return m_Pos;
	}
}