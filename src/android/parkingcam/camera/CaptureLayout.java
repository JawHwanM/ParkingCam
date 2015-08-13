/**
	@file	CameraLayout.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	카메라 레이아웃
*/

package android.parkingcam.camera;

import android.parkingcam.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 카메라 레이아웃
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
public final class CaptureLayout extends View 
{
	private final Paint mPntFocus = new Paint();
	private final Rect mRectBox;
	private final int mIntMaskColor;
	private final int mIntFrameColor;
	private Context mCtxContext;
	private final Paint mPntBox = new Paint();
	private boolean mBoolFocusImageDisplay = false;
	private boolean mBoolFocusImageOrientation = false;  // horizontal: false, vertical:true
	private boolean mBoolFocus = false; 
	private boolean mBoolDrawFocused = false; 

	private String mStrMessage = "";

	public CaptureLayout(Context ctxContext, AttributeSet attrs) 
	{
		super(ctxContext, attrs);
		mCtxContext	= ctxContext;
		mRectBox = new Rect();
		Resources resources = getResources();
		
		mIntMaskColor	= resources.getColor(R.color.transparent);	// 캡춰레이아웃의 마스크색상
		mIntFrameColor	= resources.getColor(R.color.transparent);	// 캡춰레이아웃의 프레임색상
	}

	public void drawFocusIcon(boolean boolFocusImageDisplay, boolean boolFocusImageOrientation)
	{
		mBoolFocusImageDisplay = boolFocusImageDisplay;
		mBoolFocusImageOrientation = boolFocusImageOrientation;
		invalidate();
	}

	public void drawFocused(boolean boolDrawFocused, boolean boolFocus)
	{
		mBoolDrawFocused = boolDrawFocused;
		mBoolFocus = boolFocus;
		invalidate();
	}

	public void setMessage(String strMessage)
	{
		mStrMessage = strMessage;
		invalidate();
	}

	@Override
	public void onDraw(Canvas cvsCanvas) 
	{
		try 
		{
			int intWidth = cvsCanvas.getWidth();
			int intHeight = cvsCanvas.getHeight();
			
			int intBorder = 10;
			Rect rectFrame = new Rect(intBorder, intBorder, (intWidth - intBorder), (intHeight - intBorder));

			mPntBox.setColor(mIntMaskColor);
			mRectBox.set(0, 0, intWidth, rectFrame.top);
			cvsCanvas.drawRect(mRectBox, mPntBox);
			
			mRectBox.set(0, rectFrame.top, rectFrame.left, rectFrame.bottom + 1);
			cvsCanvas.drawRect(mRectBox, mPntBox);
			
			mRectBox.set(rectFrame.right + 1, rectFrame.top, intWidth, rectFrame.bottom + 1);
			cvsCanvas.drawRect(mRectBox, mPntBox);
			
			mRectBox.set(0, rectFrame.bottom + 1, intWidth, intHeight);
			cvsCanvas.drawRect(mRectBox, mPntBox);
			
			// 검정 프레임
			mPntBox.setColor(mIntFrameColor);
			mRectBox.set(rectFrame.left, rectFrame.top, rectFrame.right + 1, rectFrame.top + 2);
			cvsCanvas.drawRect(mRectBox, mPntBox);
			
			mRectBox.set(rectFrame.left, rectFrame.top + 2, rectFrame.left + 2, rectFrame.bottom - 1);
			cvsCanvas.drawRect(mRectBox, mPntBox);
			
			mRectBox.set(rectFrame.right - 1, rectFrame.top, rectFrame.right + 1, rectFrame.bottom - 1);
			cvsCanvas.drawRect(mRectBox, mPntBox);
			
			mRectBox.set(rectFrame.left, rectFrame.bottom - 1, rectFrame.right + 1, rectFrame.bottom + 1);
			cvsCanvas.drawRect(mRectBox, mPntBox);
		
	
			if(mBoolFocusImageDisplay)
			{
				Bitmap bm;
				if(mBoolFocusImageOrientation)
				{
					// 가로
					bm = BitmapFactory.decodeResource(mCtxContext.getResources(), R.drawable.icn_landscape);
					cvsCanvas.drawBitmap(bm, 20, 20, null);
				}
				else
				{ 
					// 세로
					bm = BitmapFactory.decodeResource(mCtxContext.getResources(), R.drawable.icn_portrait);
					cvsCanvas.drawBitmap(bm, 20, intHeight - 80, null);
				}
			}
			
			if(mBoolDrawFocused)
			{
				if (mBoolFocus)
					mPntFocus.setColor(Color.GREEN);
				else
					mPntFocus.setColor(Color.RED);
				
				Paint pntBlack = new Paint();
				pntBlack.setColor(Color.BLACK);
				cvsCanvas.drawCircle(rectFrame.left+50, rectFrame.bottom - 50, 11, pntBlack);
				cvsCanvas.drawCircle(rectFrame.left+50, rectFrame.bottom - 50, 10, mPntFocus);
			}
			
			// 상단 메시지 
			if(mStrMessage.length() > 0)
			{
				Paint pntText = new Paint();
				pntText.setColor(Color.GREEN);
				pntText.setTextSize(15);
				//pntText.setFakeBoldText(true);
				pntText.setStyle(Paint.Style.FILL_AND_STROKE);
				cvsCanvas.drawText(mStrMessage, rectFrame.left+60, rectFrame.top + 30, pntText);
			}
		} 
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}