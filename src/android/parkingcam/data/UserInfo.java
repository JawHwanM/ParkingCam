/**
	@file	UserInfo.java
	@date	2015/08/10
	@author	JaeHwanM
	@brief	사용자정보
*/
package android.parkingcam.data;

/**
 * 데이터베이스를 사용자 테이블 정의한 클래스
 * @author JaeHwanM
 * @version 1.0
 */
public class UserInfo 
{
	private String mStrFirstLoad;	/**< 처음 로딩여부 **/
	
	/**
	 * 처음 로딩여부를 리턴한다.
	 * @return 처음 로딩여부
	 */
	public String getFirstLoad()		{ return mStrFirstLoad;	}
	
	/**
	 * 처음 로딩여부를 설정한다.
	 * @param strFirstLoad 처음 로딩여부
	 */
	public void setFirstLoad(String strFirstLoad)		{ this.mStrFirstLoad 	= strFirstLoad;	}
}
