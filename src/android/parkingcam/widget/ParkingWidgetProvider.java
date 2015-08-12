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
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
	private static String mStrExt 			= "";
	private static String mStrBaseFolder 	= "";
	
	private static String mStrImage = "";						/**< 위젯 이미지파일 경로	*/
	private static String mStrImageName = "";					/**< 위젯 이미지파일 이름	*/
	private static int mIntWidth  = Constants.WIDGET_WIDTH;		/**< 위젯 크기(너비)	*/
	private static int mIntHeight = Constants.WIDGET_HEIGHT;	/**< 위젯 크기(높이)	*/
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
			//TODO:: DB 삭제 이벤트 - mStrImageName(PK)
			if("".equals(mStrImage) == false && "".equals(mStrImageName) == false)
			{
				File deleteFile = new File(mStrImage);
				if(deleteFile.exists())
				{
					deleteFile.delete();
					mStrImage = "";
					mStrImageName = "";
					AppWidgetManager appWidgetMgr = AppWidgetManager.getInstance(context);
					this.onUpdate(context, appWidgetMgr, appWidgetMgr.getAppWidgetIds(new ComponentName(context, getClass())));
					Toast.makeText(context, "Delete Image!", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(context, "Have no Image!", Toast.LENGTH_SHORT).show();
				}
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
		mStrExt 		= Environment.getExternalStorageState();
		mStrBaseFolder 	= Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.PHOTO_SAVE_FOLDER;
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
	 * 위젯의 상태가 변경될 때 호출되는 이벤트
	 */
	public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetMgr, int appWidgetId, Bundle newOptions)
	{
		int intWidgetMaxWidth = appWidgetMgr.getAppWidgetOptions(appWidgetId).getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
		int intWidgetMaxHeight = appWidgetMgr.getAppWidgetOptions(appWidgetId).getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
		mIntWidth = intWidgetMaxWidth;
		mIntHeight = intWidgetMaxHeight;
		this.onUpdate(context, appWidgetMgr, appWidgetMgr.getAppWidgetIds(new ComponentName(context, getClass())));
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
		
		mStrImage = getLastImagePath();
		if("".equals(mStrImage) == false)
		{
			Bitmap bitmap = BitmapFactory.decodeFile(mStrImage);
			bitmap = doResizeBitmap(mIntWidth, mIntHeight);
			updateViews.setViewVisibility(R.id.btnClose, View.VISIBLE);
			updateViews.setViewVisibility(R.id.btnGPS, View.VISIBLE);
			updateViews.setImageViewBitmap(R.id.parkingImg, bitmap);
			/*if(doCheckBitmapFitsInMemory(bitmap.getWidth(), bitmap.getHeight(), bitmap.getDensity()))
			{
				updateViews.setViewVisibility(R.id.btnClose, View.VISIBLE);
				updateViews.setViewVisibility(R.id.btnGPS, View.VISIBLE);
				updateViews.setImageViewBitmap(R.id.parkingImg, bitmap);
			}
			else
			{
				updateViews.setViewVisibility(R.id.btnClose, View.GONE);
				updateViews.setViewVisibility(R.id.btnGPS, View.GONE);
				updateViews.setImageViewResource(R.id.parkingImg, R.drawable.icn_camera);
			}*/
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
		
    	appWidgetMgr.updateAppWidget(appWidgetId, updateViews);
    	//appWidgetMgr.partiallyUpdateAppWidget(appWidgetId, updateViews);
	}
	
	/**
	 * 가장 최신 이미지 파일 경로를 찾아준다
	 */
	public static String getLastImagePath()
	{
		System.out.println("*getLastImagePath()");
		String strFileData = "";
		
		// 다운로드 경로를 외장메모리 사용자 지정 폴더로 함.        
        if(mStrExt.equals(Environment.MEDIA_MOUNTED)) 
        {
            File dir = new File(mStrBaseFolder);
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
                		mStrImageName = fileName;
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
            		strFileData = mStrBaseFolder + File.separator + lstFile[intDestIndex].getName();
            	}
            }
        }
        return strFileData;
	}
	
	/**
	 * 비트맵(이미지) 사이즈 재조정
	 * @param width	너비
	 * @param height 높이
	 * @return 비트맵 이미지
	 */
	public static Bitmap doResizeBitmap(int width, int height)
	{
		System.out.println("*doResizeBitmap()");
		BitmapFactory.Options options = new BitmapFactory.Options();
    	options.inPreferredConfig = Config.RGB_565;
		options.inJustDecodeBounds = true;
    	
    	BitmapFactory.decodeFile(mStrImage, options);    	
    	if(options.outWidth <= 0 || options.outHeight <= 0) return null;
    	
		float flWidth 	= options.outWidth/width;
		float flHeight 	= options.outHeight/height;
		float scale = flWidth > flHeight ? flWidth : flHeight;
		
		if(scale >= 8) options.inSampleSize = 8;
		else if(scale >= 6) options.inSampleSize = 6;
		else if(scale >= 4) options.inSampleSize = 4;
		else if(scale >= 2) options.inSampleSize = 2;
		else options.inSampleSize = 1;
		
		options.inJustDecodeBounds = false;		
		return BitmapFactory.decodeFile(mStrImage, options);
	}
	
	/**
	 * 메모리에 맞는 비트맵(이미지) 체크
	 * @param bmpwidth 너비
	 * @param bmpheight 높이
	 * @param bmpdensity 
	 * @return 결과
	 */
	/*public static boolean doCheckBitmapFitsInMemory(long width,long height, int density )
	{
		System.out.println("*doCheckBitmapFitsInMemory()");
		long reqsize = width*height*density;
	    long allocNativeHeap = Debug.getNativeHeapAllocatedSize();
	    final long heapPad = (long)Math.max(4*1024*1024,Runtime.getRuntime().maxMemory()*0.1);
	    long lnReqSum = reqsize + allocNativeHeap + heapPad;
	    long lnMaxMemory = Runtime.getRuntime().maxMemory();
	    System.out.println("lnReqSum = "+lnReqSum);
	    System.out.println("lnMaxMemory = "+lnMaxMemory);
	    if(lnReqSum >= lnMaxMemory)
	    {
	        return false;
	    }	    
	    return true;
	}*/
}