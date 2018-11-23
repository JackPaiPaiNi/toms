package cn.tcl.common;

import java.security.MessageDigest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * MD5加密方法
 */
public abstract class MD5
{
	// ----------------------------------------------------- Properties

	/**
	 * Log4j
	 */
	private static final Log log = LogFactory.getLog( MD5.class );

	/**
	 * 十六进制字符
	 */
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	// ----------------------------------------------------- Constructors

	/**
	 * 隐藏构造函数防止实例化
	 */
	private MD5()
	{
	}

	// ----------------------------------------------------- Methods

	/**
	 * 字节数组转为十六进制字符串
	 * 
	 * @param b 字节数组
	 * @return 十六进制字符串
	 */
	private static String byteArrayToHexString( byte[] b )
	{
		final StringBuffer result = new StringBuffer();

		for ( int i = 0; i < b.length; i++ )
		{
			result.append( byteToHexString( b[i] ) );
		}

		return result.toString();
	}

	/**
	 * 单字节转十六进制字符
	 * 
	 * @param b 单字节
	 * @return 十六进制字符
	 */
	private static String byteToHexString( byte b )
	{
		int n = b;

		if ( n < 0 )
		{
			n = 256 + n;
		}

		int d1 = n / 16;
		int d2 = n % 16;

		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param str 源字符串
	 * @return 加密字符串
	 */
	public static String md5( String str )
	{
		if ( str == null )
		{
			throw new NullPointerException( "the md5 src string is not be null" );
		}

		String resultString = null;

		try
		{
			resultString = new String( str );
			MessageDigest md = MessageDigest.getInstance( "MD5" );
			resultString = byteArrayToHexString( md.digest( resultString.getBytes() ) );
		}
		catch ( Exception e )
		{
			if ( log.isErrorEnabled() )
			{
				log.error( "md5 string has a error:" + e.getMessage(), e );
			}
		}

		return resultString;
	}
}
