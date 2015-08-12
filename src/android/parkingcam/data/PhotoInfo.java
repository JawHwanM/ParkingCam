/**
	@file	PhotoInfo.java
	@date	2015/08/10
	@author	JaeHwanM
	@brief	사진정보
*/
package android.parkingcam.data;

/**
 * 데이터베이스를 사진테이블 정의한 클래스
 * @author JaeHwanM
 * @version 1.0
 */
public class PhotoInfo 
{
	private String mStrPhotoDate;	/**< 사진 날짜(PK) **/
	private String mStrPhotoMemo;	/**< 사진 메모 **/
	private String mStrCoordX;		/**< X좌표	*/
	private String mStrCoordY;		/**< Y좌표	*/
	
	/**
	 * 사진 날짜(PK)를 리턴한다.
	 * @return 사진 날짜(PK)
	 */
	public String getPhotoDate()		{ return mStrPhotoDate;	}
	
	/**
	 * 사진 메모를 리턴한다.
	 * @return 사진 메모
	 */
	public String getPhotoMemo()		{ return mStrPhotoMemo;	}
	
	/**
	 * X좌표를 리턴한다.
	 * @return X좌표
	 */
	public String getCoordX()		{ return mStrCoordX;	}
	
	/**
	 * Y좌표를 리턴한다.
	 * @return Y좌표
	 */
	public String getCoordY()		{ return mStrCoordY;	}
	
	/**
	 * 사진 날짜(PK)를 설정한다.
	 * @param strPhotoDate 사진 날짜(PK)
	 */
	public void setPhotoDate(String strPhotoDate)		{ this.mStrPhotoDate	= strPhotoDate;	}
	
	/**
	 * 사진 메모를 설정한다.
	 * @param strPhotoMemo 사진 메모
	 */
	public void setPhotoMemo(String strPhotoMemo)		{ this.mStrPhotoMemo	= strPhotoMemo;	}
	
	/**
	 * X좌표를 설정한다.
	 * @param strCoordX X좌표
	 */
	public void setCoordX(String strCoordX)		{ this.mStrCoordX	= strCoordX;	}
	
	/**
	 * Y좌표를 설정한다.
	 * @param strCoordY Y좌표
	 */
	public void setCoordY(String strCoordY)		{ this.mStrCoordY	= strCoordY;	}
}
