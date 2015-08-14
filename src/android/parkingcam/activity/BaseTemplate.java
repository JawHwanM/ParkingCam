/**
	@file	BaseTemplate.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	기본 액티비티 템플릿
*/
package android.parkingcam.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.parkingcam.R;
import android.parkingcam.common.Credits;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 기본적인 액티비티를 구현하기 위한 템플릿클래스
 * @author JawHwanM
 * @version 1.0
 */
public class BaseTemplate extends Activity
{
	private long mLnBackKeyPressedTime = 0;
    private Toast mToast;
    
	private int mIntRayoutId		= 0;					/**< 레이아웃 ID */
	protected Context mCtxContext	= null;					/**< 컨텍스트 */
	protected SharedPreferences mSpfPrefer = null;			/**< 프레퍼런스*/
	
	/**
	 * 템플릿 초기화
	 * @param ctxContext 컨텍스트
	 * @param intRayoutId 레이아웃ID
	 */	
	public void initTemplate(Context ctxContext, int intRayoutId)
	{
		mCtxContext		= ctxContext;
		mIntRayoutId	= intRayoutId;
		
		setContentView(mIntRayoutId);
		mSpfPrefer = PreferenceManager.getDefaultSharedPreferences(getContext());
		setScreenOrientation();
	}

	/**
	 * 템플릿 제거
	 */		
	public void releaseTemplate()
	{
		mSpfPrefer		= null;		
		mCtxContext		= null;
	}
	
	/**
	 * 컨텍스트를 얻는다.
	 */		
	public Context getContext()
	{
		return mCtxContext;
	}
	
	/**
	 * 프레퍼런스를 얻는다.
	 */
	public SharedPreferences getPreferences()
	{
		return mSpfPrefer;
	}		
	
	/**
	 * 화면 가로/세로보기 설정
	 */	
	public void setScreenOrientation()
	{
	    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	
	/**
	 * 옵션메뉴 생성이벤트
	 * @param mnuOptMenu 메뉴 
	 */		
    @Override
	public boolean onCreateOptionsMenu(Menu mnuOptMenu)
    {
    	getMenuInflater().inflate(R.menu.parking_cam, mnuOptMenu);
        return true;
	}
    
    /**
     * 옵션메뉴 선택이벤트
	 * @param item 메뉴아이템
     */    
    @Override
	public boolean onOptionsItemSelected(MenuItem item)
    {
    	int intItemId = item.getItemId();
    	if(intItemId == R.id.omenu_settings)
    	{
    		showToastOnThread("Settings");
			/*Intent itClient = new Intent(getApplicationContext(), ParkingCam.class);
			startActivity(itClient);*/
    	}
    	else if(intItemId == R.id.omenu_credits)
    	{
    		showToastOnThread("Credits");
			Intent itClient = new Intent(getApplicationContext(), Credits.class);
			startActivity(itClient);
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    /**
     * Toast 메시지를 보여준다.
     * @param strMessage 메시지
     */    
    protected void showToastOnThread(final String strMessage)
    {
    	runOnUiThread(new Runnable()
    	{     
    		@Override
			public void run()
    		{
    			if(mCtxContext != null && strMessage != null) Toast.makeText(mCtxContext, strMessage, Toast.LENGTH_SHORT).show();
    		} 
    	});
    }    
      
    /**
     * Dialog 메시지를 보여준다.
     * @param strTitle 다이얼로그 제목 
     * @param strMessage 메시지
     */     
    protected void showDialogOnThread(final String strTitle, final String strMessage)
    {
    	showDialogOnThread(strTitle, strMessage, -1);
    }      
    /**
     * Dialog 메시지를 보여준다.
     * @param strTitle 다이얼로그 제목 
     * @param strMessage 메시지
     * @param intTimeout 타임아웃(밀리세컨)
     */       
    protected void showDialogOnThread(final String strTitle, final String strMessage, final int intTimeout)
    {
    	runOnUiThread(new Runnable()
    	{     
    		@Override
			public void run()
    		{
				AlertDialog.Builder adBuilder = new AlertDialog.Builder(mCtxContext); 
				adBuilder.setTitle(strTitle); 
				adBuilder.setMessage(strMessage);
				if(intTimeout > -1)
				{
					adBuilder.setCancelable(true);
				}
				else
				{
					adBuilder.setPositiveButton(getString(R.string.common_confirm),  null);
				}
				final AlertDialog adDialog = adBuilder.create();
				adDialog.show();   
				if(intTimeout > -1)
				{
					final Timer tmTimer = new Timer();
			        tmTimer.schedule(new TimerTask()
			        {
			            @Override
						public void run()
			            {
			            	adDialog.dismiss(); 
			                tmTimer.cancel();
			            }
			        }, intTimeout);
				}
    		} 
    	});    	
    }  
    
    /**
     * 날짜 타입에 0을 붙인다.
     * @return
     */
    protected String padZeros(int intDate, int length)
    {
    	String strCalDate = Integer.toString(intDate);
    	double dblMax = Math.pow(10, length);
    	if(intDate < dblMax)
    	{
    		strCalDate = "0" + strCalDate;
    	}
    	
    	return strCalDate;
    }
    
    @Override
    public void onBackPressed()
    {
        if (System.currentTimeMillis() > mLnBackKeyPressedTime + 2000)
        {
        	mLnBackKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= mLnBackKeyPressedTime + 2000)
        {
        	mToast.cancel();
        	moveTaskToBack(true);
        	finish();
        	android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public void showGuide()
    {
    	mToast = Toast.makeText(getBaseContext(), "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
    	mToast.show();
    }
}