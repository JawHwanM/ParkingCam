/**
	@file	ParkingWidgetDialog.java
	@date	2015/08/11
	@author	JawHwanM
	@brief	앱 위젯 Dialog
*/
package android.parkingcam.widget;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.parkingcam.R;
import android.parkingcam.activity.BaseTemplate;
import android.parkingcam.data.ParkDBResource;
import android.parkingcam.data.PhotoInfo;
import android.view.ContextThemeWrapper;

/**
 * 앱 위젯 Dialog
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
public class ParkingWidgetDialog extends BaseTemplate
{
	/**
     * Activity 생성 이벤트
     * @param savedInstanceState 상태정보
     */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		showWidgetDialog();
	}

	/**
     * Activity 시작 이벤트
     */    
    @Override
	public void onStart()
    {
    	super.onStart();
    }

	/**
     * Activity 중단 이벤트
     */    
    @Override
	public void onStop()
    {
    	super.onStop();
    }
    
    /**
     * Activity 실행/재실행 이벤트
     */    
    @Override
	public void onResume()
    {
    	super.onResume();
       	// 가로보기
	    setScreenOrientation();
    }  
    
    /**
     * Activity 멈춤 이벤트
     */ 
    @Override
	public void onPause() 
    {
        super.onPause();
    }
    
    /**
     * Activity 소멸 이벤트
     */        
    @Override
	public void onDestroy()
    {    	
    	super.releaseTemplate();
    	super.onDestroy();
    }
    
    @Override
    public void onBackPressed()
    {
    	moveTaskToBack(true);
    	finish();
    	android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    /**
     * Dialog를 보여준다
     */
    private void showWidgetDialog() 
    {
    	Intent itCurIntent = getIntent();
    	Bundle bundle 	= itCurIntent.getExtras();
   		if(bundle != null && "".equals(bundle) == false)
   		{
   			String strDate = itCurIntent.getExtras().getString("photoName");
   			String strMemo = selectMemo(strDate);
   			if(strMemo == null || "".equals(strMemo)) strMemo = "";
   			
   			AlertDialog.Builder adBuilder = new AlertDialog.Builder(getDialogContext());
   			adBuilder.setIcon(R.drawable.icn_camera);
   			adBuilder.setTitle("PHOTO MEMO");
            adBuilder.setMessage(strMemo);
            adBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                    dialog.dismiss();
                    doFinish();
                }
            });
            adBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() 
            {	
				@Override
				public void onCancel(DialogInterface dialog) 
				{
					dialog.dismiss();
                    doFinish();
				}
			});
            adBuilder.show();
   		}
	}
    
    /**
	 * 카테고리 코드리스트를 받아온다.
	 */
	public String selectMemo(String strPhotoName)
	{
		ParkDBResource clsResource = new ParkDBResource(this);
		List<PhotoInfo> lstPhoto = clsResource.getPhotoInfo(strPhotoName);
		String strMemo = "";
		try
		{	
			if(lstPhoto != null && lstPhoto.size() > 0)
			{
				strMemo = lstPhoto.get(0).getPhotoMemo();
			}
		}
		catch (Exception e)
		{
			showToastOnThread("데이터 검색중 에러가 발생하였습니다.");
			e.printStackTrace();
		}
		finally
		{
			if(clsResource != null)	{ clsResource = null; }
			if(lstPhoto != null) { lstPhoto = null; }
		}
		return strMemo;
	}
    
    /**
     * Dialog 버전별 테마
     * @return
     */
    private Context getDialogContext() 
    {
        final Context context;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
            context = new ContextThemeWrapper(this, android.R.style.Theme_Holo);
        else 
            context = new ContextThemeWrapper(this, android.R.style.Theme_Dialog);

        return context;
    }
    
    /**
     * Dialog 종료
     */
    private void doFinish()
    {
    	moveTaskToBack(true);
    	finish();
    	android.os.Process.killProcess(android.os.Process.myPid());
    }
}
