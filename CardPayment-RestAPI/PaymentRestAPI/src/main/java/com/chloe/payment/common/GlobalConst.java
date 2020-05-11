package com.chloe.payment.common;

/**
 * 설명 : common const 관리 
 * @author  chloelee
 */
public class GlobalConst {

	/** Try Count */
	public static final int MAX_RETRY		= 3;
	public static final long RETRY_SLEEP	= 10;
    
    /** Delimiter */
    public static final String DELIM_COLON       = ":";
    public static final String DELIM_UNDERBAR    = "_";
    public static final String DELIM_FILEPATH    = "/";
    public static final String DELIM_PIPE        = "|";
    public static final String DELIM_QUESTION    = "?";
    public static final char DELIM_NULL          = 0x00;

    public static final String CHARSET_EUC_HYPHEN_KR    = "EUC-KR";
    public static final String CHARSET_EUC_UNDERBAR_KR  = "EUC_KR";
    public static final String CHARSET_UTF8             = "UTF-8";

    public static final String TRAN_DATA_TYPE_PAYMENT	= "PAYMENT   ";
    public static final String TRAN_DATA_TYPE_CANCEL	= "CANCEL    ";
    
    public static final String RESP_TYPE_PAYMENT		= "PAYMENT";
    public static final String RESP_TYPE_CANCEL			= "CANCEL";
    
    public static final int ALIGN_RIGHT_ZERO 			= 0;
    public static final int ALIGN_RIGHT_EMPTY			= 1;
    public static final int	ALIGN_LEFT_EMPTY			= 2;
}
