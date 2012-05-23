package drawer;
import java.awt.*;
import java.util.*;

/*
 * �摜���Ǘ�����
 */
public class Texture {
	// �摜���t�@�C�������L�[�Ƃ���HashMap�ŊǗ�
	private static Map<String,Image> m_ImageList = new HashMap<String,Image>();
	// �摜���Ǘ����郆�[�e�B���e�B
	private static MediaTracker tracker = new MediaTracker(application.Manager.getApp());

	// �t�@�C�����ɉ������摜���擾����
	public static final Image getTexture( final String name ){
		return m_ImageList.get(name);
	}
	// �t�@�C�������L�[�Ƃ��ĉ摜��ǂݍ���
	public static void addImage( final String name ){
		// �摜��ǂݍ���
		Image image = application.Manager.getApp().getImage( application.Manager.getApp().getDocumentBase(), name );

		// �摜�𑦍��ɓǂݍ���
		try {
			tracker.addImage(image,0);
			tracker.waitForID(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// �摜�Ǘ����X�g�ɒǉ�
		m_ImageList.put( name, image );
	}
}