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
	private static boolean mBoolAppFirstLoading = false;	/**< 앱 처음 실행 여부	*/
	
	/**
     *  생성 이벤트
     */	
	@Override
	public void onCreate()
	{
		super.onCreate();
	}
	
	public static void setAppFirstLoading(boolean boolAppFirstLoading)
	{
		mBoolAppFirstLoading = boolAppFirstLoading;
	}
	
	public static boolean getAppFirstLoading()
	{
		return mBoolAppFirstLoading;
	}
}
