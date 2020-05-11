package com.chloe.payment.common;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static void main(String[] args) {

		/**
		 * 카드번호 마스킹 테스트
		 */
		StringUtil masking = new StringUtil();

		// 카드 번호는 10~16자리
		String cardNum1 = "1234567890";// 최소 10자리
		String cardNum2 = "1234567890123456"; // 최대 16자리
		String cardNum3 = "123456789"; // 5자리가 잘못 들어갔을 때

		String tmpMasking = "";

		tmpMasking = masking.maskingCardNum(cardNum3);
		System.out.println("Masking Number : " + tmpMasking);
		
		
		/**
		 * 구분자(|) 로 카드번호, 유효기간, cvf 번호를 하나의 String 으로 만들기.
		 */
		GlobalConst pc = new GlobalConst();
		
		String cardNum = "1234567890"; //10자리 카드번호
		String expireMonth = "2204"; // 4자리 숫자
		String cvc = "333"; // 3자리 숫자
		
		String personInfo = null;
		String[] personSecurity = new String[3];
		personSecurity[0] = cardNum;
		personSecurity[1] = expireMonth;
		personSecurity[2] = cvc;
		
		personInfo = resultCodeJoin(personSecurity, pc.DELIM_PIPE, true);
		System.out.println("## 파이프로 붙여준 String: " + personInfo);
		
//        cfFailblockBlockResultCode = itaConf.get("failback.block.resultcode").trim();
//        userBlockResultCode = StringUtil.resultCodeSplit(cfFailblockBlockResultCode, IBConst.DELIM_PIPE, true);

	}

	/**********************************************************************
	 * 설명 : 카드번호 앞 6자리,뒤 3자리를 제외한 카드번호를 '*' 로 마스킹 처리한다.
	 *      (카드번호는 10자리~최대16자리 숫자)
	 * @param cardNumber : 카드번호 마스킹
	 **********************************************************************/
	public String maskingCardNum(String cardNumber) {

		if (cardNumber.length() >= 10 && cardNumber.length() <= 16) {

			// null체크
			if (cardNumber == null || "".equals(cardNumber)) {
				return cardNumber;
			}
			
			String regex = ("^(\\d{6})?(\\d{1,7})?(\\d{3})$");
			Matcher matcher = Pattern.compile(regex).matcher(cardNumber);

			if (matcher.find()) {
				String replaceTarget = matcher.group(2);
				char[] c = new char[replaceTarget.length()];
				Arrays.fill(c, '*');
				return cardNumber.replace(replaceTarget, String.valueOf(c));
			} else {
				return cardNumber;
			}

		} else {
			return cardNumber;
		}

	}
	
	/**********************************************************************
	 * 설 명 : 카드번호, 유효기간, cvc 번호를 받아  "|" 를 구분자로 넣어 하나의 문자열로 만든다.
	 * @param strTarget : 합칠 문자열
	 * @param strDelim : 구분자
	 * @param bContainNull : 구분되어진 문자열중 공백문자열의 포함여부.
	 *        true : 포함, false : 포함하지 않음.
	 * @return 합친 문자열을 반환한다.
	 **********************************************************************/
	public static String resultCodeJoin(String[] strTarget, String strDelim, boolean bContainNull) {

		if(strTarget.length == 0 || strTarget == null){
			return "";
		}
		
		StringJoiner sj = new StringJoiner(strDelim);
		for(int i=0;i<strTarget.length;i++){
			sj.add(strTarget[i]);
		}
		return sj.toString();

	}

	/**********************************************************************
	 * 설 명 : 대상문자열(strTarget)에서 구분문자열(|)을 기준으로 문자열을 분리하여 각 분리된 문자열을 배열에
	 * 할당하여 반환한다.
	 * @param strTarget : 분리 대상 문자열
	 * @param strDelim : 구분시킬 문자열
	 * @param bContainNull : 구분되어진 문자열중 공백문자열의 포함여부.
	 *        true : 포함, false : 포함하지 않음.
	 * @return 분리된 문자열을 순서대로 배열에 격납하여 반환한다.
	 **********************************************************************/
	public static String[] resultCodeSplit(String strTarget, String strDelim, boolean bContainNull) {
		int index = 0;
		String[] resultStrArray = null;

		try {
			resultStrArray = new String[search(strTarget, strDelim) + 1];
			String strCheck = new String(strTarget);

			while (strCheck.length() != 0) {
				int begin = strCheck.indexOf(strDelim);

				if (begin == -1) {
					resultStrArray[index] = strCheck;

					break;
				} else {
					int end = begin + strDelim.length();

					if (bContainNull) {
						resultStrArray[index++] = strCheck.substring(0, begin)
								.trim();
					}

					strCheck = strCheck.substring(end);

					if ((strCheck.length() == 0) && bContainNull) {
						resultStrArray[index] = strCheck;

						break;
					}
				}
			}
		} catch (Exception e) {
		}

		return resultStrArray;
	}

	/**********************************************************************
	 * 설 명 : 대상문자열(strTarget)에서 지정문자열(strSearch)이 검색된 횟수를,
	 *       지정문자열이 없으면 0 을 반환한다.
	 * @param strTarget : 대상문자열
	 * @param strSearch : 검색할 문자열
	 * @return 지정문자열이 검색되었으면 검색된 횟수를, 검색되지 않았으면 0 을 반환한다.
	 **********************************************************************/
	public static int search(String strTarget, String strSearch) {
		int result = 0;

		try {
			String strCheck = new String(strTarget);

			for (int i = 0; i < strTarget.length();) {
				int loc = strCheck.indexOf(strSearch);

				if (loc == -1) {
					break;
				} else {
					result++;
					i = loc + strSearch.length();
					strCheck = strCheck.substring(i);
				}
			}
		} catch (Exception e) {
		}

		return result;
	}

}
