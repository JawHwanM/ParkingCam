/**
	@file	ParkingWidgetProvider.java
	@date	2015/08/11
	@author	JawHwanM
	@brief	앱 위젯 제공자
*/

package android.parkingcam.widget;

import java.io.File;
import java.util.Locale;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.parkingcam.R;
import android.parkingcam.common.Constants;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * 앱 위젯 제공자
 * @author JawHwanM
 * @since 2015.08.11
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2015.08.11  JawHwanM          최초 생성
 * 
 * </pre>
 */
public class ParkingWidgetProvider extends AppWidgetProvider
{		
	private static String mStrImage = "";		/**< 위젯 이미지파일 경로	*/
	
	/**
	 * 브로드캐스트 리시버 이벤트
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		System.out.println("*onReceive()");
		
		String action = intent.getAction();	// 이벤트 액션 종류 (이미지갱신 / 닫기 / GPS)
		if(Constants.WIDGET_ACTION_IMG_CLICK.equals(action))
		{
			Toast.makeText(context, "Image Event in Widget!", Toast.LENGTH_SHORT).show();
			AppWidgetManager appWidgetMgr = AppWidgetManager.getInstance(context);
			this.onUpdate(context, appWidgetMgr, appWidgetMgr.getAppWidgetIds(new ComponentName(context, getClass())));
		}
		
		if(Constants.WIDGET_ACTION_CLS_CLICK.equals(action))
		{
			//TODO:: 닫기 이벤트
			if("".equals(mStrImage) == false)
			{
				File deleteFile = new File(mStrImage);
				if(deleteFile.exists())
				{
					deleteFile.delete();
					mStrImage = "";
					AppWidgetManager appWidgetMgr = AppWidgetManager.getInstance(context);
					this.onUpdate(context, appWidgetMgr, appWidgetMgr.getAppWidgetIds(new ComponentName(context, getClass())));
				}
				Toast.makeText(context, "Delete Image!", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(context, "Have no Image!", Toast.LENGTH_SHORT).show();
			}
		}
		
		if(Constants.WIDGET_ACTION_GPS_CLICK.equals(action))
		{
			//TODO:: GPS 이벤트
			Toast.makeText(context, "GPS Event in Widget!", Toast.LENGTH_SHORT).show();
		}
		
		super.onReceive(context, intent);
	}
	
	/**
	 * 위젯 갱신 주기에 따른 이벤트
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetMgr, int[] appWidgetIds)
	{
		System.out.println("*onUpdate()");
		appWidgetIds = appWidgetMgr.getAppWidgetIds(new ComponentName(context, getClass()));
		for(int intIdIndex = 0; intIdIndex < appWidgetIds.length; intIdIndex++)
		{
			updateAppWidget(context, appWidgetMgr, appWidgetIds[intIdIndex]);
		}
		
		super.onUpdate(context, appWidgetMgr, appWidgetIds);
	}
	
	/**
	 * 위젯 생성시 호출되는 이벤트
	 */
	@Override
	public void onEnabled(Context context)
	{
		System.out.println("*onEnabled()");
		super.onEnabled(context);
	}
	
	/**
	 * 위젯의 마지막 인스턴스가 제거될때 호출되는 이벤트
	 */
	@Override
	public void onDisabled(Context context)
	{
		System.out.println("*onDisabled()");
		super.onDisabled(context);
	}
	
	/**
	 * 위젯이 사용자에 의해 제거될때 호출되는 이벤트
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds)
	{
		System.out.println("*onDelete()");
		mStrImage = null;
		super.onDeleted(context, appWidgetIds);
	}
	
	/**
	 * 개별ID 별로 갱신처리를 한다.
	 * @param context
	 * @param appWidgetMgr
	 * @param appWidgetId
	 */
	public static void updateAppWidget(Context context, AppWidgetManager appWidgetMgr, int appWidgetId)
	{
		System.out.println("*updateAppWidget()");
		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.parking_widget);		
		
		mStrImage = setImage();
		if("".equals(mStrImage) == false)
		{
			updateViews.setViewVisibility(R.id.btnClose, View.VISIBLE);
			updateViews.setViewVisibility(R.id.btnGPS, View.VISIBLE);
			updateViews.setImageViewBitmap(R.id.parkingImg, BitmapFactory.decodeFile(mStrImage));
		}
		else
		{
			updateViews.setViewVisibility(R.id.btnClose, View.GONE);
			updateViews.setViewVisibility(R.id.btnGPS, View.GONE);
			updateViews.setImageViewResource(R.id.parkingImg, R.drawable.icn_camera);
		}
    			
		PendingIntent imgClick = PendingIntent.getBroadcast(context, 0, new Intent(Constants.WIDGET_ACTION_IMG_CLICK), PendingIntent.FLAG_UPDATE_CURRENT);
		updateViews.setOnClickPendingIntent(R.id.parkingImg, imgClick);
		
		PendingIntent clsClick = PendingIntent.getBroadcast(context, 0, new Intent(Constants.WIDGET_ACTION_CLS_CLICK), PendingIntent.FLAG_UPDATE_CURRENT);
		updateViews.setOnClickPendingIntent(R.id.btnClose, clsClick);
		
		PendingIntent gpsClick = PendingIntent.getBroadcast(context, 0, new Intent(Constants.WIDGET_ACTION_GPS_CLICK), PendingIntent.FLAG_UPDATE_CURRENT);
		updateViews.setOnClickPendingIntent(R.id.btnGPS, gpsClick);
		
		//this.onUpdate(context, appWidgetMgr, appWidgetMgr.getAppWidgetIds(new ComponentName(context, getClass())));
		
    	appWidgetMgr.updateAppWidget(appWidgetId, updateViews);
	}
	
	/**
	 * 가장 최신 이미지 파일 경로를 찾아준다
	 */
	public static String setImage()
	{
		System.out.println("*setImage()");
		String strFileData = "";
		
		// 다운로드 경로를 외장메모리 사용자 지정 폴더로 함.  
        String ext = Environment.getExternalStorageState();
        if(ext.equals(Environment.MEDIA_MOUNTED)) 
        {
        	String strBaseFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PHOTO_SAVE_FOLDER;
            File dir = new File(strBaseFolder);
            if(dir.isDirectory() == false)
            {
            	dir.mkdirs();
            }
            else
            {
            	File[] lstFile = dir.listFiles();
            	if(lstFile.length > 0)
            	{
            		// 가장 최신 파일 검색
            		long lnMaxDate = 0;
                	int intDestIndex = 0;
            		for(int intIndex = 0; intIndex < lstFile.length; intIndex++)
                	{
                		String fileName = lstFile[intIndex].getName();
                		fileName = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
                		// Image 파일 확장자 확인
                		if(fileName.toLowerCase(Locale.getDefault()).equals("png") 
                		|| fileName.toLowerCase(Locale.getDefault()).equals("jpg")
                		|| fileName.toLowerCase(Locale.getDefault()).equals("jpeg"))
                		{
                			long lnDate = lstFile[intIndex].lastModified();
                    		if(lnMaxDate < lnDate)
                    		{
                    			intDestIndex = intIndex;
                    			lnMaxDate = lnDate;
                    		}
                		}
                	}
            		
            		// 파일 경로 저장
            		strFileData = strBaseFolder + File.separator + lstFile[intDestIndex].getName();
            	}
            }
        }
        return strFileData;
	}
}