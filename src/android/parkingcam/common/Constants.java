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
	public static final String PARKING_CAM_APP_ID 			= "ParkingCam";					/**< APP ID	*/	
	public static final String APP_FIRST_LOADING			= "app_first_loading";			/**< 앱 처음실행여부 */
	public static final String LANDSCAPE_SCREEN_ENABLED_KEY	= "landscape_screen_enabled";	/**< 화면 가로/세로 보기 키 */
	
	public static final String TABLE_NAME_USER_INFO		= "USER_INFO";		/**< 사용자 정보 */
	public static final String TABLE_NAME_PHOTO_INFO	= "PHOTO_INFO";		/**< 사진 정보 */
	
	public static final int DEVICE_DISPLAY_MEDIUM 	= 800 * 600;
	public static final int DEVICE_DISPLAY_LARGE 	= 1024 * 600;
	public static final int DEVICE_DISPLAY_XLARGE 	= 1280 * 800;
	
	public static final String PHOTO_SAVE_FOLDER 		= "/parkingcam";	/**< 사진저장 폴더	*/
	public static final long COUNTDOWN_MAX 				= 3000;				/**< 사진촬영 지연시간	**/
	public static final long ANIMATE_DURATION 			= 1500;				/**< 애니매이션 효과시간	**/
	
	public static final String WIDGET_ACTION_IMG_CLICK	= "android.appwidget.action.ACTION_IMG_CLICK";	/**< 위젯 이미지 클릭	*/
	public static final String WIDGET_ACTION_CLS_CLICK	= "android.appwidget.action.ACTION_CLS_CLICK";	/**< 위젯 닫기 클릭	*/
	public static final int WIDGET_WIDTH				= 145;	/**< 위젯 너비 Default	*/
	public static final int WIDGET_HEIGHT				= 145;	/**< 위젯 높이 Default	*/
	
	public static final int MANUAL_LAYOUT_COUNT 	= 3;	/**< 화면수	*/
	public static final int SNAP_VELOCITY 			= 100;	/**< 화면전환 드래그 속도 최소값 pixel/s	*/
	public static final int TOUCH_STATE_SCROLLING 	= 0;	/**< 상태 : 현재 스크롤중	*/
	public static final int TOUCH_STATE_NORMAL 		= 1;	/**< 상태 : 스크롤 멈춤	*/
	
	public static final double MAP_DEFAULT_LAT = 37.477269;		/**< 기본 Y좌표	*/
	public static final double MAP_DEFAULT_LNG = 126.981631;	/**< 기본 X좌표	*/
}
