/**
	@file	CameraMgr.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	카메라 관리자
*/

package android.parkingcam.camera;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.os.Message;
import android.parkingcam.R;
import android.view.SurfaceHolder;

/**
 * 카메라 관리자
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
@SuppressWarnings("deprecation")
public class CameraMgr 
{
	private static CameraMgr mStatInstance;		
	private static Camera mCmrCamera;						/**< 카메라 */
	private final Context mCtxContext;				 		/**< 컨텍스트	*/
	private boolean mBoolPreviewing;						/**< 미리보기 모드 여부 */
	private Handler mHdrParentMessageHandler;				/**< 부모의 메시지 핸들러 */				
	private SurfaceHolder mShParentSurfaceHolder = null;	/**< 부모의 surface 홀더 */
	//private boolean mBoolLandOrientation = false;
  
	Camera.PictureCallback mLsnrPictureListener = new Camera.PictureCallback()
	{
		@Override
		public void onPictureTaken(byte[] bytData, Camera cmrCamera)
		{			
			CameraCapture.compareTime("JPG 생성시작");
			if (bytData == null)
			{
				//
			}
			else
			{
				Message message = mHdrParentMessageHandler.obtainMessage(R.id.PHOTO_CAMERA_MGR_REQUST_PICTURE, bytData);
				message.sendToTarget();
				mHdrParentMessageHandler = null;
				mCmrCamera.startPreview();
				CameraCapture.compareTime("핸들러에게 이미지 전달");
			}
      }
	};

	public void requestPicture(Handler handler) 
	{
	    if (mCmrCamera != null && mBoolPreviewing) 
	    {
	    	mHdrParentMessageHandler = handler;
	    }
	}

	private Camera.AutoFocusCallback mLsnrAutoFocusCallbackListener = new Camera.AutoFocusCallback() 
	{
		@Override
		public void onAutoFocus(boolean boolSuccess, Camera camera) 
		{
			//Logger.d(");
			if (boolSuccess)
			{
				// 자동 포커스 성공시	    	
				mHdrParentMessageHandler.sendEmptyMessage(R.id.PHOTO_CAMERA_MGR_FOCUS_SUCCEDED);
			}
			else
			{
				mHdrParentMessageHandler.sendEmptyMessage(R.id.PHOTO_CAMERA_MGR_FOCUS_FAILED);
			}
		}
	};

	public void requestCameraFocus(Handler handler) 
	{
		if (mCmrCamera != null && mBoolPreviewing)
			mHdrParentMessageHandler = handler;
	}

	public static void initialize(Context ctxContext) 
	{
		if (mStatInstance == null) mStatInstance = new CameraMgr(ctxContext);
	}
  
	public static CameraMgr getInstance() 
	{
		return mStatInstance;
	}

	private CameraMgr(Context ctxContext) 
	{
		mCtxContext 		= ctxContext;
		mCmrCamera			= null;
		mBoolPreviewing		= false;
	}

	public void setSurfaceHolder(SurfaceHolder shHolder)
	{
		mShParentSurfaceHolder = shHolder;
	}
  
	/**
	 * 상위의 Surface Holder를 이용하여  카메라 드라이버를 오픈한다.
	 */
	public boolean openDriver(boolean boolLandOrientation) 
	{
		//mBoolLandOrientation = boolLandOrientation;
		
		if (mShParentSurfaceHolder == null)
		{
			return false;
		}
	  
		if (mCmrCamera == null) 
		{
			try 
			{
				mCmrCamera = Camera.open();
				setCameraParameters();
				mCmrCamera.setPreviewDisplay(mShParentSurfaceHolder);
			}
			catch (IOException e) 
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public void openDriver(boolean boolLandOrientation, SurfaceHolder sfHolder) 
	{
		//mBoolLandOrientation = boolLandOrientation;
		if(mCmrCamera == null)
		{
			try
			{
				mCmrCamera = Camera.open();
				setCameraParameters();
				mCmrCamera.setPreviewDisplay(sfHolder);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

  	/**
  	 * 카메라 종료
  	 */
	public void closeDriver() 
	{
		if (mCmrCamera != null) 
		{
			mCmrCamera.release();
			mCmrCamera = null;
			
			mShParentSurfaceHolder = null;
		}
	}

	/**
	 * 카메라 미리보기 모드 시작
	 */
	public void startPreview() 
	{
		if (mCmrCamera != null && mBoolPreviewing == false) 
		{
			mCmrCamera.startPreview();
			mBoolPreviewing = true;
		}
	}

	/**
	 * 카메라 미리보기 모드 중지
	 */
	public void stopPreview() 
	{
		if (mCmrCamera != null && mBoolPreviewing == true) 
		{
			mCmrCamera.setPreviewCallback(null);
			mCmrCamera.stopPreview();
			
			mBoolPreviewing = false;
		}
	}

	public void requestAutoFocus() 
	{
		if (mCmrCamera != null && mBoolPreviewing == true) 
	    {
	    	mCmrCamera.autoFocus(mLsnrAutoFocusCallbackListener);
	    }
 	}  

	public void getPicture()
	{
		mCmrCamera.takePicture(null, null, mLsnrPictureListener);
	}

	public Camera.Parameters getCameraParameters() 
	{
		return mCmrCamera.getParameters();
	}
	
	public void setCameraParameters() 
	{
		Camera.Parameters pmParams = mCmrCamera.getParameters();
		List<Camera.Size> lstPreviewSize = pmParams.getSupportedPreviewSizes();

		if(lstPreviewSize.size() > 0)
		{
			Camera.Size szCamSize = lstPreviewSize.get(0);
	
			pmParams.setPreviewSize(szCamSize.width, szCamSize.height);
			pmParams.setPictureSize(szCamSize.width, szCamSize.height);
			pmParams.setWhiteBalance(Parameters.WHITE_BALANCE_AUTO);
			mCmrCamera.setParameters(pmParams);
		}
	}
	
	public static void setDisplayOrientation(Activity activity, boolean boolLandOrientation)
	{
		try
		{
			/*
			int intDegrees	= 0;
			int intResult	= 0;
			int intRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
			switch (intRotation) 
			{
				case Surface.ROTATION_0		: intDegrees = 0; break;
				case Surface.ROTATION_90	: intDegrees = 90; break;
				case Surface.ROTATION_180	: intDegrees = 180; break;
				case Surface.ROTATION_270	: intDegrees = 270; break;
			}
			intResult  = (90 - intDegrees + 360) % 360;
			*/
			if(mCmrCamera != null)
			{
				if(boolLandOrientation)
				{
					mCmrCamera.setDisplayOrientation(0);
				}
				else
				{
					mCmrCamera.setDisplayOrientation(90);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}