/**
	@file	Constants.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	상수 정의
*/

package android.parkingcam.common;

/**
 * 상수 정의
 * @author JawHwanM
 * @since 2015.08.10
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2015.08.10  JawHwanM          최초 생성
 * 
 * </pre>
 */
public class Constants 
{
	public static final String LANDSCAPE_SCREEN_ENABLED_KEY		= "landscape_screen_enabled";	/**< 화면 가로/세로 보기 키*/
	
	public static final String PHOTO_SAVE_FOLDER = "/DCIM/parkingcam";	/**< 사진저장 폴더	*/
	public static final int COUNTDOWN_MAX = 3000;						/**< 사진촬영 지연시간	**/
	
	public static final String WIDGET_ACTION_IMG_CLICK	= "android.appwidget.action.ACTION_IMG_CLICK";	/**< 위젯 이미지 클릭	*/
	public static final String WIDGET_ACTION_CLS_CLICK	= "android.appwidget.action.ACTION_CLS_CLICK";	/**< 위젯 닫기 클릭	*/
	public static final String WIDGET_ACTION_GPS_CLICK	= "android.appwidget.action.ACTION_GPS_CLICK";	/**< 위젯 GPS 클릭	*/
}
