/**
	@file	MainManual.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	사용설명서
*/
package android.parkingcam.manual;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.parkingcam.R;
import android.parkingcam.activity.BaseTemplate;
import android.parkingcam.camera.CameraCapture;
import android.parkingcam.common.Constants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 사용설명서
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
public class MainManual extends BaseTemplate
{
	private long mLnBackKeyPressedTime = 0;
    private Toast mToast;
	
	/**
	 * Activity 생성 이벤트
	 * @param savedInstanceState 상태정보
	 */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);      
        super.initTemplate(this, 0);
        initViewControl();
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
    	setScreenOrientation();
    }
    
    /**
	 * Activity 소멸 이벤트
	 */
	@Override  
	public void onDestroy() 
	{
		super.onDestroy();
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
        	moveTaskToBack(true);
        	finish();
            mToast.cancel();
        }
    }

    public void showGuide()
    {
    	mToast = Toast.makeText(getBaseContext(), "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
    	mToast.show();
    }
	
	/**
	 * View 관련 컨트롤 초기화한다.
	 */
	private void initViewControl() 
	{
		final ScrollView sView = new ScrollView(this);
		View view1 = View.inflate(this, R.layout.manual_1, null);
		View view2 = View.inflate(this, R.layout.manual_2, null);
		View view3 = View.inflate(this, R.layout.manual_3, null);
		sView.addView(view1);
		sView.addView(view2);
		sView.addView(view3);
		setContentView(sView);
		
		final Button btnStart = (Button)findViewById(R.id.btnStart);
		if(btnStart != null)
		{
			btnStart.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					SharedPreferences.Editor edit = mSpfPrefer.edit();
					edit.putBoolean(Constants.APP_FIRST_LOADING,  false);
			    	edit.commit();
			    	edit = null;
			    	
			    	Intent itCameraCapture = new Intent(getContext(), CameraCapture.class);
			    	startActivity(itCameraCapture);
			    	
		        	finish();
				}				
			});
		}
	}
}