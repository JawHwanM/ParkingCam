/**
	@file	AppContext.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	어플리케이션 컨텍스트
*/
package android.parkingcam;

import android.app.Application;

/**
 * 어플리케이션 컨텍스트 클래스
 * @author JawHwanM
 * @version 1.0
 */
public class AppContext extends Application
{
	private static double mDblLatitude = 0;
	private static double mDblLongitude = 0;
	
	/**
     *  생성 이벤트
     */	
	@Override
	public void onCreate()
	{
		super.onCreate();
	}
	
	public static void setLatitude(double dblLatitude) { mDblLatitude = dblLatitude; }
	public static void setLongitude(double dblLongitude) { mDblLongitude = dblLongitude; }
	
	public static double getLatitude() { return mDblLatitude; }
	public static double getLongitude() { return mDblLongitude; }
}
