package drawer;
import java.awt.*;
import java.util.*;

/*
 * 画像を管理する
 */
public class Texture {
	// 画像をファイル名をキーとしてHashMapで管理
	private static Map<String,Image> m_ImageList = new HashMap<String,Image>();
	// 画像を管理するユーティリティ
	private static MediaTracker tracker = new MediaTracker(application.Manager.getApp());

	// ファイル名に応じた画像を取得する
	public static final Image getTexture( final String name ){
		return m_ImageList.get(name);
	}
	// ファイル名をキーとして画像を読み込む
	public static void addImage( final String name ){
		// 画像を読み込む
		Image image = application.Manager.getApp().getImage( application.Manager.getApp().getDocumentBase(), name );

		// 画像を即座に読み込む
		try {
			tracker.addImage(image,0);
			tracker.waitForID(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 画像管理リストに追加
		m_ImageList.put( name, image );
	}
}