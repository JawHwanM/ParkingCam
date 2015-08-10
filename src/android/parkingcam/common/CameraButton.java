/**
	@file	CameraButton.java
	@date	2015/08/10
	@author	JawHwanM
	@brief	카메라 버튼 뷰
*/

package android.parkingcam.common;

import android.parkingcam.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 카메라 버튼 뷰
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
public class CameraButton extends View 
{
	private int mIntPaddingWidth = 0; 
	private int mIntPaddingHeight = 0;
	private final String mStrLabel;
	private int mIntImageResId;
	private Bitmap mBtmImage;
	private Context	mCtxContext;
	
	public CameraButton(Context ctxContext, int intResImage, String strLabel, int intStretchWidth, int intStretchHeigth) 
	{
		super(ctxContext);
		mCtxContext = ctxContext;
		
		this.mStrLabel = strLabel;
		this.mIntImageResId = intResImage;
		if (intStretchHeigth == 0 && intStretchWidth == 0)
		{
			this.mBtmImage = BitmapFactory.decodeResource(ctxContext.getResources(), mIntImageResId);
		}
		else
		{
			Bitmap imageAux = BitmapFactory.decodeResource(ctxContext.getResources(),mIntImageResId);
			this.mBtmImage = Bitmap.createScaledBitmap(imageAux, intStretchWidth, intStretchHeigth, true);
		}
		setFocusable(true);
		setBackgroundColor(Color.TRANSPARENT);
		
		setClickable(true);
	}
	
	public CameraButton(Context ctxContext, AttributeSet asAttrSet) 
	{
		super(ctxContext, asAttrSet);
		/*
		String strName;
		String strValue;
		for (int intCnt = 0; intCnt < asAttrSet.getAttributeCount(); intCnt++)
		{
			strName = asAttrSet.getAttributeName(intCnt);
			strValue = asAttrSet.getAttributeValue(intCnt);
			System.out.println(strName + " = " + strValue);
		}
		*/
		mCtxContext = ctxContext;
		this.mStrLabel = "";
		this.mIntImageResId = R.drawable.icn_camera;
		this.mBtmImage = BitmapFactory.decodeResource(ctxContext.getResources(), mIntImageResId);
		setBackgroundColor(Color.TRANSPARENT);
		
		setClickable(true);
	}

	public void setImage(int intImageResId, int intStretchWidth, int intStretchHeigth)
	{
		this.mIntImageResId = intImageResId;
		if (intStretchHeigth == 0 && intStretchWidth == 0)
		{
			this.mBtmImage = BitmapFactory.decodeResource(mCtxContext.getResources(), mIntImageResId);
		}
		else
		{
			Bitmap imageAux = BitmapFactory.decodeResource(mCtxContext.getResources(), mIntImageResId);
			this.mBtmImage = Bitmap.createScaledBitmap(imageAux, intStretchWidth, intStretchHeigth, true);
		}
	}	

	public void setMargin(int intPaddingWidth, int intPaddingHeight)
	{
		mIntPaddingWidth	= intPaddingWidth;
		mIntPaddingHeight	= intPaddingHeight;
	}

	@Override
	protected void onFocusChanged(boolean boolGainFocus, int intDirection, Rect rectPrevFocusedRect)
	{
		if (boolGainFocus == true)
		{
		    this.setBackgroundColor(Color.rgb(255, 165, 0));
		}
		else
		{
			setBackgroundColor(Color.TRANSPARENT);
		}
	}

	@Override
	protected void onDraw(Canvas cvsCanvas)
	{
		Paint pntText = new Paint();
		//pntText.setColor(Color.BLACK);
		if(mBtmImage != null)
		{
			cvsCanvas.drawBitmap(mBtmImage, mIntPaddingWidth / 2, mIntPaddingHeight / 2, null);
			if (mStrLabel != null) cvsCanvas.drawText(mStrLabel, mIntPaddingWidth / 2, (mIntPaddingHeight / 2) + mBtmImage.getHeight() + 8, pntText);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	{
		setMeasuredDimension(measureWidth(widthMeasureSpec), MeasureHeight(heightMeasureSpec));
	}
	
	private int measureWidth(int intMeasureSpec)
	{
		int intPreferred = 0;
		if(mBtmImage != null) intPreferred = mBtmImage.getWidth();
		return getMeasurement(intMeasureSpec, intPreferred);
	}

	private int MeasureHeight(int intMeasureSpec)
	{
		int intPreferred = 0;
		if(mBtmImage != null) intPreferred = mBtmImage.getHeight();
		return getMeasurement(intMeasureSpec, intPreferred);
	}	

	private int getMeasurement(int intMeasureSpec, int intPreferred)
	{
		int specSize = MeasureSpec.getSize(intMeasureSpec);
		int measurement = 0;
	
		switch(MeasureSpec.getMode(intMeasureSpec))
		{
			case MeasureSpec.EXACTLY	: measurement = specSize; break;
			case MeasureSpec.AT_MOST	: measurement = Math.min(intPreferred, specSize); break;
			default						: measurement = intPreferred;
			break;
		}
		return measurement;
	}

	public String getLabel()
	{
		return mStrLabel;
	}

	public int getImageResId()
	{
		return mIntImageResId;
	}	
}