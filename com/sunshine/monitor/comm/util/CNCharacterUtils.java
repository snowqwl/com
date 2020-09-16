package com.sunshine.monitor.comm.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.ArrayUtils;

public class CNCharacterUtils {
	
	private static Character.UnicodeBlock[] chineseCharBlock = {
			Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ,
			Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS,
			Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A,
			Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B,
			Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION,
			Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS,
			Character.UnicodeBlock.GENERAL_PUNCTUATION
		};
	
	public static boolean isChinese(Character ch){
		Character.UnicodeBlock charBlock = Character.UnicodeBlock.of(ch);
		return ArrayUtils.contains(chineseCharBlock, charBlock);
	}
	
	public static String encodeChineseChar(String str,String encode) throws UnsupportedEncodingException{
		char[] strChar = str.toCharArray();
		for(int i=0;i<strChar.length;i++){
			Character ch = strChar[i];
			if(isChinese(ch)){
				str = str.replaceFirst(ch.toString(), URLEncoder.encode(ch.toString(),encode));
			}
		}
		return str;
	}
	
}
