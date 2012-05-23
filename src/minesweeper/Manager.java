package minesweeper;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.*;
import game.Minesweeper;

/*
 * �Q�[���̊Ǘ��N���X
 * ���͂̂��߂̃��X�i�[�ƕ`��̂��߂̃p�l�����p�����Ă���
 */
public class Manager extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 9182217648537328479L;

	// �J�����}�X�̏�Ԃƃ}�X�̉��̏��
	private final ArrayList<Mas> masList = new ArrayList<Mas>();
	private final ArrayList<MasType> masTypeList = new ArrayList<MasType>();

	// �Q�[���̊J�n�̃t���O
	private boolean runFlag;

	// NONE�}�X���J���Ă������߂Ɏg�p���郁�����p�z��
	private boolean [][] openNoneMas_dp_dfs;

	// �N�����̏�����
	public Manager( final int width_num, final int height_num ){
		// �摜�̓ǂݍ���
		for( int i = 1; i < 4; ++i ){
			drawer.Texture.addImage( "mas" + i + Minesweeper.IMAGE_EXT);
		}
		for( int i = 0; i < 10; ++i ){
			drawer.Texture.addImage( "masType" + i + Minesweeper.IMAGE_EXT);
		}
		// �������̊m��
		newGetMasList( width_num, height_num );
		// �}�X�Ȃǂ̏�����
		initGame();
	}
	// �}�X�Ȃǂ̔z��̊m��(�}�X�̐��ŉς�����)
	public void newGetMasList( final int width_num, final int height_num ){
		// ���������������
		masList.clear();
		masTypeList.clear();

		// �J�����}�X�̏�Ԃƃ}�X�̉��̏�Ԃ̃��������m��
		for( int w = 0; w < width_num; ++w ){
			for( int h = 0; h < height_num; ++h ){
				masList.add( new Mas( w * Minesweeper.MAS_SIZE, h * Minesweeper.MAS_SIZE ) );
				masTypeList.add( new MasType( w * Minesweeper.MAS_SIZE, h * Minesweeper.MAS_SIZE ) );
			}
		}

		// �������ċA�p�̔z��̊m��
		openNoneMas_dp_dfs = new boolean[width_num][height_num];
	}

	// �}�X�Ȃǂ̏�����
	public void initGame(){
		// �J�n�O�Ȃ̂�false�ɂ���
		runFlag = false;

		// �������ċA�p�z��̏�����
		for( int i = 0; i < openNoneMas_dp_dfs.length; ++i ){
			for( int j = 0; j < openNoneMas_dp_dfs[i].length; ++j ){
				openNoneMas_dp_dfs[i][j] =false;
			}
		}

		// �}�X��NONE�ɂ��āA���̉��̏���S�Ēn�������ɂ���
		for( Mas mas : masList ){
			mas.setNoneMas();
		}
		for( MasType masType : masTypeList ){
			masType.setAroundMineCount(0);
		}
	}

	// �ŏ��ɊJ�����}�X�Ƃ��̎���8�}�X�ɒn����ݒu���Ȃ��悤�ɂ���
	private boolean aroundNoPutMine( int mx, int my, int x, int y ){
		for( int xx = -1; xx <= 1; ++xx ){
			for( int yy = -1; yy <= 1; ++yy ){
				if( x == (mx + xx) && y == (my + yy) ){ return true; }
			}
		}
		return false;
	}

	// �Q�[���J�n���ɒn����z�u����
	private void putMine( final int mx, final int my ){
		// �w������̒n����z�u����
		for( int i = 0; i < Minesweeper.maxMineNum; ++i ){
			// �n���̈ʒu���d�����Ȃ��悤�ɒu���Ȃ��ꍇ�͔z�u����蒼��
			while(true){
				// �n���̔z�u�ꏊ�����߂�
				final int x = (int)(Math.random() * Minesweeper.maxWidthMas);
				final int y = (int)(Math.random() * Minesweeper.maxHeightMas);

				// �N���b�N�����ʒu�ɂ͒n���͐ݒu���Ȃ�
				if( aroundNoPutMine( mx, my, x, y ) ){
					continue;
				}

				// �w��ʒu�ɒn����ݒu����
				final MasType masType = masTypeList.get( adjust(x,y) );

				if( masType.getTypeNumber() == MasType.NONE ){
					masType.setMine();
					break;
				}
			}
		}

		// ���͂ɂ���n���̐�����ݒ肷��
		for( int x = 0; x < Minesweeper.maxWidthMas; ++x ){
			for( int y = 0; y < Minesweeper.maxHeightMas; ++y ){
				final MasType masType = masTypeList.get(adjust(x,y));

				// ���̃}�X���n���������疳��
				if( masType.getTypeNumber() == MasType.MINE ){
					continue;
				}

				// �n���̌�
				int mine = 0;

				// ����8�}�X�𒲂ׂ�
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
				// �n���̐���ݒ肷��
				masType.setAroundMineCount(mine);
			}
		}
	}

	// �[���D��T��(dfs)�ƃ�������p���Ēn���������Ɗm�肵�Ă���}�X��S�ĊJ��
	private void openNoneMas_dfs( final int x, final int y ){
		// ����8�}�X�𒲂ׂ�
		for( int vx = -1; vx <= 1; ++vx ){
			for( int vy = -1; vy <= 1; ++vy ){
				// �ʒu���ړ�������X,Y
				final int tx = x + vx;
				final int ty = y + vy;

				// ���S�ʒu�Ȃ疳�� �܂��� �ړ��������ʒu���z��͈͓̔����ǂ��� �܂��� �T���ς݂Ȃ疳��
				if( (vx == 0 && vy == 0) || !checkMasOutOfRange( tx, ty ) ||
					(openNoneMas_dp_dfs[tx][ty] == true)
				){
					continue;
				}

				openNoneMas_dp_dfs[tx][ty] = true; 			// �T���ς݂ɂ���
				masList.get(adjust(tx,ty)).setOpendMas();	// �}�X���J��

				// ���̈ʒu�̃}�X���n����������ΒT���𑱂���
				if( masTypeList.get(adjust(tx,ty)).getTypeNumber() == MasType.NONE ){
					openNoneMas_dfs( tx, ty );
				}
			}
		}
	}

	// �Q�[�����I�����Ă��邩�ǂ���
	private final boolean checkGameEnd(){
		// �n���𓥂񂽂��ǂ���
		for( int i = 0; i < masTypeList.size(); ++i ){
			final MasType masType = masTypeList.get(i);
			final Mas mas = masList.get(i);

			// �J�����}�X�ɒn������������Q�[���I�[�o�[
			if( masType.getTypeNumber() == MasType.MINE && mas.getState() == Mas.State.OPENED ){
				// �Q�[���I�[�o�[���b�Z�[�W�̕\��
				JOptionPane.showMessageDialog(
					null, Minesweeper.END_MESSAGE,
					Minesweeper.GAME_TITLE, JOptionPane.PLAIN_MESSAGE
				);
				return true;
			}
		}

		// �S�Ă̒n���������������ǂ���
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

		// �Q�[���N���A���b�Z�[�W�̕\��
		JOptionPane.showMessageDialog(
			null, Minesweeper.CREAR_MESSAGE,
			Minesweeper.GAME_TITLE, JOptionPane.PLAIN_MESSAGE
		);

		return true;
	}

	// ���N���b�N�Ń}�X���J���A�E�N���b�N�Ŋ��𗧂Ă�
	@Override public void mouseClicked( final MouseEvent e ){
		final int mouseX = getMouseX(e);
		final int mouseY = getMouseY(e);

		// ����N���b�N���ɒn����z�u����
		if( !runFlag ){
			putMine( (int)(mouseX/Minesweeper.MAS_SIZE), (int)(mouseY/Minesweeper.MAS_SIZE) );
			runFlag = true;
		}

		for( Mas mas : masList ){
			final Point2D pos = mas.getPos();
			final Mas.State state = mas.getState();

			if( collisionMouseAndMas(pos,mouseX,mouseY) ){
				// ���N���b�N��
				if( e.getButton() == MouseEvent.BUTTON1 ){
					// �����J���Ă���}�X�ł͂Ȃ��ꍇ�A�}�X���J��
					if( !(state == Mas.State.FLAG || state == Mas.State.OPENED) ){
						final int x = (int)(mas.getPos().getX() / Minesweeper.MAS_SIZE);
						final int y = (int)(mas.getPos().getY() / Minesweeper.MAS_SIZE);

						mas.setOpendMas();

						if( masTypeList.get(adjust(x,y)).getTypeNumber() == MasType.NONE ){
							openNoneMas_dfs(x,y); // ����8�}�X�ɒn���������}�X�̎��͂�S�ĊJ��
						}
					}
					break;
				}
				// �E�N���b�N��
				else if( e.getButton() == MouseEvent.BUTTON3 ){
					// ���������Ă��Ȃ��Ȃ�Ί��𗧂Ă�
					if( state == Mas.State.SELECTED ){
						// �ő�n�����������Ȃ������Ȃ�Η��Ă���
						if( getMineNum() > 0 ){
							mas.setFlagMas();
						}
					}
					// ���������Ă����猳�ɖ߂�
					else if( state == Mas.State.FLAG ){
						mas.setNoneMas();
					}
					break;
				}
			}
		}

		// �n�����̃��b�Z�[�W���X�V����
		application.Manager.getApp().setInformationMessage(getMineNum());
		// �ĕ`��
		repaint();

		// �Q�[�����I�����������`�F�b�N���āA�I���Ȃ�΃Q�[��������������
		if( checkGameEnd() ){
			initGame();
			runFlag = false;
		}
	}

	// �}�E�X�̈ʒu�Ń}�X��I����Ԃɂ���
	@Override public void mouseMoved( final MouseEvent e ){
		// �����J���Ă����ԈȊO�̃}�X�őI������Ă���}�X��I����Ԃɂ���
		for( Mas mas : masList ){
			if( !(mas.getState() == Mas.State.FLAG || mas.getState() == Mas.State.OPENED) ){
				mas.setNoneMas();
				if( collisionMouseAndMas( mas.getPos(), getMouseX(e), getMouseY(e) ) ){
					mas.setSelectedMas();
				}
			}
		}
		repaint(); // �ĕ`��
	}

	// �`��
	public void paint( final Graphics g ){
		// �A�v���P�[�V�����n���h��
		final Minesweeper app = application.Manager.getApp();

		// �}�X�̉��̏�Ԃɍ��킹�ĕ`�悷��摜��ς���
		for( MasType masType : masTypeList ){
			g.drawImage(
				drawer.Texture.getTexture( "masType" + masType.getTypeNumber() + Minesweeper.IMAGE_EXT ),
				(int)masType.getPos().getX(), (int)masType.getPos().getY(), app
			);
		}

		// �}�X�̏�Ԃɍ��킹�ĕ`�悷��摜��ς���
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

	// �}�E�X�̈ʒu���}�X�̒��Ɏ��܂��Ă��邩���`�F�b�N����
	private final boolean collisionMouseAndMas( final Point2D mas_pos, final int mouseX, final int mouseY ){
		if( mas_pos.getX() <= mouseX && mouseX < (mas_pos.getX() + Minesweeper.MAS_SIZE) &&
			mas_pos.getY() <= mouseY && mouseY < (mas_pos.getY() + Minesweeper.MAS_SIZE)
		){
			return true;
		}
		return false;
	}

	// ����n���c��
	public final int getMineNum(){
		int mine = Minesweeper.maxMineNum;
		for( Mas mas : masList ){
			if( mas.getState() == Mas.State.FLAG ){
				--mine;
			}
		}
		return mine;
	}

	// �}�E�X�̈ʒuX�̎擾
	private final int getMouseX( final MouseEvent e ){
		return e.getX();
	}

	// �}�E�X�̈ʒuY�̎擾
	private final int getMouseY( final MouseEvent e ){
		return e.getY() - 26; // �p�l���̈ʒu�����炷
	}

	// X,Y�̑g�ݍ��킹��1�����ɕϊ�����
	private final int adjust( final int x, final int y ){
		return x * Minesweeper.maxHeightMas + y;
	}

	// X,Y�̑g�ݍ��킹���z��͈̔͊O�̏o�Ă��邩�`�F�b�N����
	private final boolean checkMasOutOfRange( final int x, final int y ){
		return (0 <= x) && (x < Minesweeper.maxWidthMas) && (0 <= y) && (y < Minesweeper.maxHeightMas);
	}

	@Override public void mousePressed (MouseEvent e){}
	@Override public void mouseReleased(MouseEvent e){}
	@Override public void mouseEntered (MouseEvent e){}
	@Override public void mouseExited  (MouseEvent e){}
	@Override public void mouseDragged (MouseEvent e){}
}