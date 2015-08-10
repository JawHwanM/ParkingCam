/**
	@file	CameraCapture.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	사진 촬영
*/

package android.parkingcam.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.parkingcam.R;
import android.parkingcam.activity.BaseTemplate;
import android.parkingcam.common.CameraButton;
import android.parkingcam.common.Constants;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 사진 촬영
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
public class CameraCapture extends BaseTemplate implements SurfaceHolder.Callback
{
	private String mStrCurDate 					= "";				/**< 현재 날짜/시각 */
    private String mStrSavePath;   									/**< 저장 경로 	*/
    private String mStrSaveFolder 				= "/DCIM/parkingcam";	/**< 저장 폴더 	*/
	
	private CameraButton mBtnCamera				= null;		/**< 카메라(사진찍기) 버튼	*/
	private LinearLayout mLlCameraNextMenu		= null;;	/**< 카메라 다음 메뉴 레이아웃	*/
	
	private boolean mBoolLandOrientation		= true;		/**< 가로(landscape) 모드 여부 */
	private boolean mBoolScreenRequestPicture	= false;	/**< 화면사진 요청 여부	*/
	private boolean mBoolSurfaceCreated			= false;	/**< surface 생성여부	*/
	private boolean mBoolSdCardMounted			= true;		/**< SD 카드 마운트 가능여부 */
	private boolean mBoolPreviewReady			= false;	/**< 프리뷰 준비 여부	*/
	private boolean mBoolFocusButtonPressed		= false;	/**< 포커스 버튼 클릭 여부	*/

	private CaptureLayout mClsCaptureLayout;				/**< 캡쳐된 레이아웃	*/

	private boolean mBoolSDCardRegister			= false;	/**< SD 카드 레지스터 여부	*/
	private boolean mBoolTakePhotoProgress		= false;	/**< 사진찍기 프로그래스 여부	*/
	
	/**
	 * 테마리소스 적용시 발생하는 이벤트
     * @param theme 테마 
     * @param resid 리소스 ID
     * @param first 이 스타일이 테마에 적용되는 처음 인 경우 true로 설정 
	 */		
	@Override  
	protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first)  
	{  
	    super.onApplyThemeResource(theme, resid, first);
	    theme.applyStyle(android.R.style.Theme_Panel, true);  
	}
	
	/**
	 * Activity 생성 이벤트
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// 타이틀 바 출력안함
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		super.initTemplate(this, R.layout.camera_capture);
		
		try
		{
			this.initViewControl();
			this.initCameraControl();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		// 다운로드 경로를 외장메모리 사용자 지정 폴더로 함.  
        String ext = Environment.getExternalStorageState();  
        if (ext.equals(Environment.MEDIA_MOUNTED)) 
        {  
            mStrSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + mStrSaveFolder;  
        }
	}
	
	/**
	 * Activity 재생성 이벤트
	 */
	@Override
	protected void onResume() 
	{
		super.onResume();
		
		setScreenOrientation();
		
		mBoolSdCardMounted = checkSDCardState();

		// SD카드와 관련된 이벤트를 필터링 하여 수신
		IntentFilter ifIntentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
		ifIntentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		ifIntentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
		ifIntentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		ifIntentFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
		ifIntentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		ifIntentFilter.addDataScheme("file");
		
		registerReceiver(mBrSDcardEventListener, ifIntentFilter);
		
		mBoolSDCardRegister = true;
		
		startCameraCapture();
	}
	
	/**
	 * Activity 멈춤 이벤트
	 */
	@Override
	protected void onPause() 
	{
		// SD카드 브로드케스트 리시버가 등록되어있다면
		if (mBoolSDCardRegister)
		{
			unregisterReceiver(mBrSDcardEventListener);
			mBoolSDCardRegister = false;
		}
		stopCameraCapture();

		super.onPause();
	}
	
	@Override
	protected void onPostResume() 
	{
		super.onPostResume();
	}

	/**
	 * Activity 소멸 이벤트
	 */
	@Override  
	public void onDestroy() 
	{		
		//File file = new File(mStrSavePath + File.separator + mStrCurDate+".png");
		//if(file.exists()) file.delete();
		
		stopCamera();
		super.onDestroy();
	}
	
	/**
	 * View관련 컨트롤을 초기화한다.
	 */ 
	private void initViewControl()
	{
		Window objWindow = getWindow();

		DisplayMetrics displayMetrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);	    
    	WindowManager.LayoutParams lpParams = getWindow().getAttributes();
    	lpParams.width =  displayMetrics.widthPixels - 40;
    	lpParams.height = displayMetrics.heightPixels- 100;
    	
    	lpParams.screenBrightness = 1;
    	
    	objWindow.setAttributes(lpParams);
		//objWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// 현재 보여지고 있는 액티비티 상에서 입력값이 없어도 계속 화면이 꺼지지 않고 유지되도록 한다.
		objWindow.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		CameraButton btnClose = (CameraButton)findViewById(R.id.btnClose);			
		btnClose.setImage(R.drawable.icn_camera_close, 0, 0);
		btnClose.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		
		mBtnCamera = (CameraButton)findViewById(R.id.btnCamera);
		mBtnCamera.setImage(R.drawable.icn_camera, 0, 0);
		mBtnCamera.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mBoolScreenRequestPicture  = true;
				requestCameraFocus();
			}
		});
		
		mLlCameraNextMenu = (LinearLayout)findViewById(R.id.llCameraNextMenu);
		mLlCameraNextMenu.setVisibility(View.GONE);
		
		CameraButton btnTakePicture = (CameraButton)findViewById(R.id.btnTakePicture);			
		btnTakePicture.setImage(R.drawable.icn_camera, 0, 0);
		btnTakePicture.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startCameraCapture();
			}
		});			
		mClsCaptureLayout = (CaptureLayout)findViewById(R.id.clCaptureLayoutView);
		if(mClsCaptureLayout != null) mClsCaptureLayout.setBackgroundColor(Color.TRANSPARENT);
	}
	
	/**
	 * Camera관련 컨트롤을 초기화한다.
	 */ 
	private void initCameraControl()
	{   
		CameraMgr.initialize(getApplication());
		
		mBoolSdCardMounted = checkSDCardState();
		mBoolSurfaceCreated = false;

		// 카메라 시작 이벤트 전송
		mHdrMessageHandler.sendEmptyMessage(R.id.PHOTO_CAMERA_START);
	}
	
	/**
	 * 카메라 버튼 변경
	 * @param boolEnable
	 */
	public void toggleCameraButton(boolean boolEnable)
	{
		if(boolEnable == true)
		{
			mBtnCamera.setVisibility(View.VISIBLE);
			mLlCameraNextMenu.setVisibility(View.GONE);
			
			File file = new File(mStrSavePath + File.separator + mStrCurDate+".png");
			if(file.exists()) file.delete();
		}
		else
		{
			mBtnCamera.setVisibility(View.GONE);
			mLlCameraNextMenu.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 카메라 캡쳐 시작
	 */
	public void startCameraCapture()
	{
		toggleCameraButton(true);
		
		mHdrMessageHandler.sendEmptyMessage(R.id.PHOTO_CAMERA_START);
		SurfaceView sfvSurfacePreview = (SurfaceView) findViewById(R.id.surfaceview_preview);
		if(sfvSurfacePreview != null)
		{
			SurfaceHolder surfaceHolder = sfvSurfacePreview.getHolder();
			if (mBoolSurfaceCreated) 
			{				
				initCamera(surfaceHolder);
			}
			else 
			{
				surfaceHolder.addCallback(this);
				if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
					surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			}
		}	
	}

	/**
	 * 카메라 캡쳐 정지
	 */
	public void stopCameraCapture()
	{
		checkStorageState(false);
		stopCamera();		
	}
	
	/**
	 * 카메라 surface 변경
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{
		
	}
	
	/**
	 * 카메라 surface 생성
	 */
	@Override
	public void surfaceCreated(SurfaceHolder shHolder) 
	{
		if(mBoolSurfaceCreated == false) 
		{
			mBoolSurfaceCreated = true;
			setCameraSurfaceHolder(shHolder);
			mHdrMessageHandler.sendEmptyMessage(R.id.PHOTO_CAMERA_START);
		}
	}
	
	/**
	 * 카메라 surface 소멸
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		mBoolSurfaceCreated = false;		
	}
	
	/*
	 * ----------------------------------------------------------------------------------------
	 * 카메라 관련
	 * ----------------------------------------------------------------------------------------
	 */

	/**
	 * 카메라의 surface holder를 세팅한다.
	 */
	private void setCameraSurfaceHolder(SurfaceHolder surfaceHolder)
	{
		if (surfaceHolder == null || CameraMgr.getInstance() == null) return;
		CameraMgr.getInstance().setSurfaceHolder(surfaceHolder);
	}

	/**
	 * 카메라 초기화
	 * @param surfaceHolder
	 */
	private void initCamera(SurfaceHolder surfaceHolder) 
	{
		if (CameraMgr.getInstance() == null) return;
		mBoolPreviewReady = true;

		mBoolLandOrientation = mSpfPrefer.getBoolean(Constants.LANDSCAPE_SCREEN_ENABLED_KEY, false);
		
		CameraMgr.getInstance().openDriver(mBoolLandOrientation, surfaceHolder);
		CameraMgr.getInstance().startPreview();
	}

	/**
	 * 카메라 초기화
	 */
	private void initCamera() 
	{	
		if (CameraMgr.getInstance() == null) return;
		mBoolPreviewReady = true;
		mBoolLandOrientation = mSpfPrefer.getBoolean(Constants.LANDSCAPE_SCREEN_ENABLED_KEY, false);
		CameraMgr.getInstance().openDriver(mBoolLandOrientation);
		CameraMgr.setDisplayOrientation(CameraCapture.this, mBoolLandOrientation);
		CameraMgr.getInstance().startPreview();
	}

	/**
	 * 카메라 미리보기를 멈추고 드라이버를 닫는다.
	 */
	private void stopCamera()
	{
		CameraMgr.getInstance().stopPreview();
		CameraMgr.getInstance().closeDriver();
		
		mBoolPreviewReady = false;	
	}
	
	/*
	 * ----------------------------------------------------------------------------------------
	 * Keyboard
	 * ----------------------------------------------------------------------------------------
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		switch (keyCode) 
		{
			case KeyEvent.KEYCODE_FOCUS:
			{
				if (event.getRepeatCount() == 0) return requestCameraFocus();
				return true;
			}
			
			case KeyEvent.KEYCODE_CAMERA:
			{
				if (event.getRepeatCount() == 0) return requestCameraTakePicture();
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 카메라 포커스 요청
	 * @return
	 */
	private boolean requestCameraFocus()
	{
		if (mBoolSdCardMounted == false) return false;
		if (mBoolTakePhotoProgress == true) return false;

		mClsCaptureLayout.setMessage(getString(R.string.photo_request_camera_focus));
		
		mBoolFocusButtonPressed = true;
		mClsCaptureLayout.drawFocusIcon(true, mBoolLandOrientation);  
		CameraMgr.getInstance().requestCameraFocus(mHdrMessageHandler);
		CameraMgr.getInstance().requestAutoFocus();
		return true;
	}

	/**
	 * 카메라로부터 사진을 가져오도록 요청한다.
	 * @return
	 */
	private boolean requestCameraTakePicture()
	{
		if (mBoolSdCardMounted == false) return false;
		if (mBoolTakePhotoProgress == true) return false;

		mBoolTakePhotoProgress = true;
		
		if (mBoolPreviewReady)
		{
			compareTime("requestCameraTakePicture()");
			mClsCaptureLayout.setMessage(getString(R.string.photo_request_camera_take_picture));
			mClsCaptureLayout.drawFocused(false, false);
			mClsCaptureLayout.drawFocusIcon(false, mBoolLandOrientation);
			CameraMgr.getInstance().requestPicture(mHdrMessageHandler);
			CameraMgr.getInstance().getPicture();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) 
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_FOCUS :
				mBoolFocusButtonPressed = false;
				mClsCaptureLayout.drawFocusIcon(false, mBoolLandOrientation);
				mClsCaptureLayout.drawFocused(false, false);
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	/*
	 * ----------------------------------------------------------------------------------------------------
	 * 이벤트 메시지 핸들러 
	 * ---------------------------------------------------------------------------------------------------- 
	 */
	private Handler mHdrMessageHandler = new Handler() 
	{
		@Override
		public void handleMessage(Message msgData) 
		{
			switch(msgData.what)
			{
			case R.id.PHOTO_RESTART_CAPTURE_MODE:
				break;

				// ---------------------------------------------------------------
			case R.id.PHOTO_CAMERA_START :
				initCamera();
				if (mBoolSdCardMounted == false)
					checkStorageState(true);
				break;

				// ---------------------------------------------------------------
			case R.id.PHOTO_CAMERA_MGR_REQUST_PICTURE :	// JPG로 파일출력
				
				compareTime("이미지 저장 시작");
				
				FileOutputStream fosImage = null;
				try
				{
					Calendar calTraceDate = Calendar.getInstance();
					mStrCurDate = "";
			        mStrCurDate = 	Integer.toString(calTraceDate.get(Calendar.YEAR))+
			          				padZeros(calTraceDate.get(Calendar.MONTH)+1, 2)+
			          				padZeros(calTraceDate.get(Calendar.DAY_OF_MONTH), 2)+
			          				padZeros(calTraceDate.get(Calendar.HOUR_OF_DAY), 2)+
			          				padZeros(calTraceDate.get(Calendar.MINUTE), 2)+
			          				padZeros(calTraceDate.get(Calendar.SECOND), 2);
					
					// 사진 임시 저장 
					fosImage = new FileOutputStream(mStrSavePath + File.separator + mStrCurDate+".png");
					fosImage.write((byte[])msgData.obj, 0, ((byte[]) msgData.obj).length);
					fosImage.close();
					compareTime("이미지 저장 끝");
					
					mBoolTakePhotoProgress = false;
					mClsCaptureLayout.setMessage("");
					
					stopCamera();
					toggleCameraButton(false);
				}
				catch (Exception e) 
				{
					mBoolTakePhotoProgress = false;
					mClsCaptureLayout.setMessage("");
					mHdrMessageHandler.sendEmptyMessage(R.id.PHOTO_RESTART_CAPTURE_MODE);
				}
				finally
				{
					fosImage = null;
					msgData.obj = null;
				}
				break;
				// ---------------------------------------------------------------	
			case R.id.PHOTO_UPLOAD_FINISHED :	// 전송완료
				
				startCameraCapture();
				break;

			case R.id.PHOTO_CAMERA_MGR_FOCUS_SUCCEDED :

				// 자동 포커스 성공시
				if (mBoolFocusButtonPressed == false) return;				
				mClsCaptureLayout.drawFocused(true, true);
				
				playSoundOnFocus();
				vibrate();
				if (mBoolScreenRequestPicture)
				{
					requestCameraTakePicture();
				}
				mBoolScreenRequestPicture = false;
				break;

				// ---------------------------------------------------------------
			case R.id.PHOTO_CAMERA_MGR_FOCUS_FAILED :
				
				if (mBoolFocusButtonPressed == false) return;				
				mClsCaptureLayout.drawFocused(true, false);
				
				vibrate();
				mBoolScreenRequestPicture = false;
				break;		

			default:
				break;
			}
			super.handleMessage(msgData);
		}
	};

	/**
	 * SD카드의 상태를 출력한다.
	 */
	private void checkStorageState(boolean boolShow)
	{
		String strState = Environment.getExternalStorageState();
		int intResId = 0;

		if (strState == Environment.MEDIA_CHECKING) 
			intResId = R.string.photo_sd_card_preparing;
		else 
			intResId = R.string.photo_sd_card_unmount;
		
		if(boolShow) 
		{
			Toast.makeText(CameraCapture.this, getString(intResId), Toast.LENGTH_LONG).show();
		} 
	}

	private final BroadcastReceiver mBrSDcardEventListener = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			String strAction = intent.getAction();
			if (strAction.equals(Intent.ACTION_MEDIA_MOUNTED)) 
			{
				// SD 카드를 쓸수 있음
				mBoolSdCardMounted = true;
			} 
			else if(strAction.equals(Intent.ACTION_MEDIA_UNMOUNTED) || strAction.equals(Intent.ACTION_MEDIA_CHECKING)) 
			{
				// SD 카드 쓸수 없음
				checkStorageState(true);
				mBoolSdCardMounted = false;
			} 
			else
			{
				if (strAction.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)) 
				{
					checkStorageState(false);
					showToastOnThread(getString(R.string.photo_sd_card_wait));
				} 
				else
				{
					if (strAction.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) 
					{
						checkStorageState(false);
					}
				}
			}
		}
	}; 	

	/**
	 * SD 카드의 상태를 얻는다.
	 * @return 
	 */
	public static boolean checkSDCardState()
	{
		boolean boolResult = true;
		String strSDCardState = Environment.getExternalStorageState();
		if (strSDCardState.compareTo(Environment.MEDIA_MOUNTED) != 0)
		{
			boolResult = false;
		}
		return boolResult;
	}

	/**
	 * 기기 소리를 켠다(알림음)
	 */
	private void playSoundOnFocus()
	{
		ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_SYSTEM, 100);
		tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
	}

	/**
	 * 기기 진동을 준다
	 */
	private void vibrate()
	{
		Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(200);
	}
	
	private static long mLngCurTime = 0;
	public static void compareTime(String strStatus)
	{
		long lngBackupTime = mLngCurTime;
		Calendar now = Calendar.getInstance();
		long lngCurTime = now.getTimeInMillis();
		System.out.println("*CompareTime");
		System.out.println("lngBackupTime : "+lngBackupTime);
		System.out.println("lngCurTime : "+lngCurTime);
		mLngCurTime = lngCurTime;
	}
}