 package com.sunshine.monitor.comm.util;
 
/*      */ import java.text.DecimalFormat;
/*      */ 
/*      */ public class StringUtils
/*      */ {
/*      */   public static int getInt(String intStr, int intDef)
/*      */   {
/*  199 */     if ((null == intStr) || ("".equals(intStr.trim())))
/*      */     {
/*  201 */       return intDef;
/*      */     }
/*      */ 
/*  204 */     int intRetu = intDef;
/*      */ 
/*  206 */     Double db = new Double(intStr);
/*  207 */     intRetu = db.intValue();
/*  208 */     return intRetu;
/*      */   }
/*      */ 
/*      */   public static int getInt(String intStr)
/*      */   {
/*  220 */     return getInt(intStr, 0);
/*      */   }
/*      */ 
/*      */   public static double getDouble(String dbstr, double dbDef)
/*      */   {
/*  235 */     if ((null == dbstr) || ("".equals(dbstr.trim())))
/*      */     {
/*  237 */       return dbDef;
/*      */     }
/*  239 */     double dbRetu = dbDef;
/*  240 */     Double db = new Double(dbstr);
/*  241 */     dbRetu = db.doubleValue();
/*  242 */     return dbRetu;
/*      */   }
/*      */ 
/*      */   public static double getDouble(String dbstr)
/*      */   {
/*  254 */     return getDouble(dbstr, 0.0D);
/*      */   }
/*      */ 
/*      */   public static long getLong(String longstr, long longDef)
/*      */   {
/*  268 */     if ((null == longstr) || ("".equals(longstr.trim())))
/*      */     {
/*  270 */       return longDef;
/*      */     }
/*  272 */     long longRetu = longDef;
/*      */ 
/*  274 */     Double db = new Double(longstr);
/*  275 */     longRetu = db.longValue();
/*      */ 
/*  277 */     return longRetu;
/*      */   }
/*      */ 
/*      */   public static long getLong(String longstr)
/*      */   {
/*  289 */     return getLong(longstr, 0L);
/*      */   }
/*      */ 
/*      */   public static boolean getBoolean(String booleanstr, boolean booleanDef)
/*      */   {
/*  304 */     if (null == booleanstr)
/*      */     {
/*  306 */       return booleanDef;
/*      */     }
/*  308 */     boolean booleanRetu = booleanDef;
/*  309 */     if ("true".equalsIgnoreCase(booleanstr.trim()))
/*      */     {
/*  311 */       booleanRetu = true;
/*      */     }
/*      */ 
/*  314 */     return booleanRetu;
/*      */   }
/*      */ 
/*      */   public static boolean getBoolean(String booleanstr)
/*      */   {
/*  328 */     return getBoolean(booleanstr, false);
/*      */   }
/*      */ 
/*      */   public static String getNumFormat(double db, String fmt)
/*      */   {
/*  343 */     String retu = "";
/*  344 */     if ((null == fmt) || ("".equals(fmt.trim())))
/*      */     {
/*  346 */       return Double.toString(db);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  351 */       DecimalFormat decimalformat = new DecimalFormat(fmt);
/*  352 */       retu = decimalformat.format(db);
/*  353 */       decimalformat = null;
/*      */     }
/*      */     catch (IllegalArgumentException iaex)
/*      */     {
/*  358 */       retu = Double.toString(db);
/*      */     }
/*  360 */     return retu;
/*      */   }
/*      */ 
/*      */   public static String getNumFormat(double db)
/*      */   {
/*  372 */     return getNumFormat(db, "0.00");
/*      */   }
/*      */ 
/*      */   public static String getNumFormat(String numStr, String fmt)
/*      */   {
/*  388 */     double db = getDouble(numStr, 0.0D);
/*  389 */     return getNumFormat(db, fmt);
/*      */   }
/*      */ 
/*      */   public static String getNumFormat(String numStr)
/*      */   {
/*  402 */     return getNumFormat(numStr, "0.00");
/*      */   }
/*      */ 
/*      */   public static String htmlEncode(String str)
/*      */   {
/*  414 */     String retu = null;
/*  415 */     if ((null == str) || ("".equals(str.trim())))
/*      */     {
/*  417 */       retu = str;
/*      */     }
/*      */     else
/*      */     {
/*  421 */       String html = str;
/*  422 */       StringUtils tool = new StringUtils();
/*  423 */       html = replaceString(html, "&", "&amp;");
/*  424 */       html = replaceString(html, "<", "&lt;");
/*  425 */       html = replaceString(html, ">", "&gt;");
/*  426 */       html = replaceString(html, "\r\n", "\n");
/*  427 */       html = replaceString(html, "\n", "<br>");
/*  428 */       html = replaceString(html, "\t", "    ");
/*  429 */       html = replaceString(html, " ", "&nbsp;");
/*  430 */       html = replaceString(html, "\"", "&quot;");
/*  431 */       retu = html;
/*  432 */       html = null;
/*      */     }
/*  434 */     return retu;
/*      */   }
/*      */ 
/*      */   public static String replaceString(String sourceStr, String oldStr, String newStr, boolean isIgnoreCase)
/*      */   {
/*  455 */     if ((sourceStr == null) || (oldStr == null) || (oldStr.equals("")))
/*      */     {
/*  457 */       return null;
/*      */     }
/*  459 */     String str_RetStr = sourceStr;
/*  460 */     int pos = str_RetStr.indexOf(oldStr);
/*  461 */     while (pos != -1)
/*      */     {
/*  463 */       str_RetStr = str_RetStr.substring(0, pos) + newStr + str_RetStr.substring(pos + oldStr.length());
/*      */ 
/*  465 */       pos = str_RetStr.indexOf(oldStr, pos + newStr.length());
/*      */     }
/*  467 */     return str_RetStr;
/*      */   }
/*      */ 
/*      */   public static String replaceString(String sourceStr, String oldStr, String newStr)
/*      */   {
/*  485 */     return replaceString(sourceStr, oldStr, newStr, false);
/*      */   }
/*      */ 
/*      */   public static String[] splitString(String sourceStr, String splitStr, boolean isTrim)
/*      */   {
/*  506 */     if ((sourceStr == null) || (splitStr == null))
/*      */     {
/*  508 */       return null;
/*      */     }
/*      */ 
/*  511 */     if (isTrim)
/*      */     {
/*  514 */       while (sourceStr.indexOf(splitStr) == 0)
/*      */       {
/*  516 */         sourceStr = sourceStr.substring(splitStr.length());
/*      */       }
/*      */ 
/*  521 */       while (sourceStr.indexOf(splitStr, sourceStr.length() - splitStr.length()) > -1)
/*      */       {
/*  524 */         sourceStr = sourceStr.substring(0, sourceStr.length() - splitStr.length());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  529 */     if ((sourceStr.equals("")) || (splitStr.equals("")))
/*      */     {
/*  531 */       return null;
/*      */     }
/*  533 */     return splitString(sourceStr, splitStr);
/*      */   }
/*      */ 
/*      */   public static String[] splitString(String sourceStr, String splitStr)
/*      */   {
/*  543 */     if ((sourceStr == null) || (splitStr == null))
/*      */     {
/*  545 */       return null;
/*      */     }
/*  547 */     if ((sourceStr.equals("")) || (splitStr.equals("")))
/*      */     {
/*  549 */       return null;
/*      */     }
/*  551 */     int int_ArraySize = subStringCount(sourceStr, splitStr);
/*      */ 
/*  553 */     if (int_ArraySize == -1)
/*      */     {
/*  555 */       return null;
/*      */     }
/*      */ 
/*  558 */     sourceStr = sourceStr + splitStr;
/*      */ 
/*  560 */     String[] str_RetArr = new String[int_ArraySize + 1];
/*  561 */     int int_pos = sourceStr.indexOf(splitStr);
/*  562 */     int int_ArrayPos = 0;
/*  563 */     while (int_pos != -1)
/*      */     {
/*  565 */       str_RetArr[(int_ArrayPos++)] = sourceStr.substring(0, int_pos);
/*  566 */       sourceStr = sourceStr.substring(int_pos + splitStr.length());
/*  567 */       int_pos = sourceStr.indexOf(splitStr);
/*      */     }
/*      */ 
/*  570 */     return str_RetArr;
/*      */   }
/*      */ 
/*      */   public static int subStringCount(String sourceStr, String subStr)
/*      */   {
/*  581 */     if ((sourceStr == null) || (subStr == null))
/*      */     {
/*  583 */       return -1;
/*      */     }
/*  585 */     if ((sourceStr.equals("")) || (subStr.equals("")))
/*      */     {
/*  587 */       return -1;
/*      */     }
/*  589 */     int result = 0;
/*  590 */     int int_pos = sourceStr.toUpperCase().indexOf(subStr.toUpperCase());
/*  591 */     while (int_pos != -1)
/*      */     {
/*  593 */       result++;
/*  594 */       int_pos = sourceStr.toUpperCase().indexOf(subStr.toUpperCase(), int_pos + subStr.length());
/*      */     }
/*      */ 
/*  598 */     return result;
/*      */   }
/*      */ 
/*      */   public static String arrayToString(String[] array, String splitStr)
/*      */   {
/*  617 */     if ((null == array) || (0 == array.length))
/*      */     {
/*  619 */       return "";
/*      */     }
/*  621 */     if (null == splitStr)
/*      */     {
/*  623 */       splitStr = "";
/*      */     }
/*  625 */     StringBuffer strBuf = new StringBuffer("");
/*  626 */     int Len = array.length;
/*  627 */     for (int i = 0; i < Len - 1; i++)
/*      */     {
/*  629 */       strBuf.append(null == array[i] ? "" : array[i]).append(splitStr);
/*      */     }
/*  631 */     strBuf.append(null == array[(Len - 1)] ? "" : array[(Len - 1)]);
/*      */ 
/*  633 */     return strBuf.toString();
/*      */   }
/*      */ 
/*      */   public static String arrayToString(String[] array)
/*      */   {
/*  646 */     return arrayToString(array, "|");
/*      */   }
/*      */ 
/*      */   public static boolean isNullBlank(String str)
/*      */   {
/*  658 */     return (null == str) || ("".equals(str.trim()));
/*      */   }
/*      */ 
/*      */   public static String decomposeString(String sourceStr, String splitStr, int pos)
/*      */   {
/*  687 */     String retu = "";
/*  688 */     if ((null == sourceStr) || ("".equals(sourceStr.trim())))
/*      */     {
/*  690 */       return "";
/*      */     }
/*      */ 
/*  693 */     if (pos <= 0)
/*      */     {
/*  695 */       return "";
/*      */     }
/*      */ 
/*  698 */     if ((null == splitStr) || ("".equals(splitStr)))
/*      */     {
/*  700 */       return sourceStr;
/*      */     }
/*      */ 
/*  703 */     sourceStr = sourceStr + splitStr;
/*  704 */     String tmpStr = sourceStr;
/*      */ 
/*  706 */     int splitLen = splitStr.length();
/*  707 */     int tmpLen = tmpStr.length();
/*      */ 
/*  709 */     for (int i = 0; i < pos - 1; i++)
/*      */     {
/*  711 */       int tmpPosi = tmpStr.indexOf(splitStr);
/*  712 */       if ((tmpPosi < 0) || (tmpPosi + splitLen >= tmpLen))
/*      */       {
/*  714 */         tmpStr = splitStr;
/*  715 */         break;
/*      */       }
/*      */ 
/*  719 */       tmpStr = tmpStr.substring(tmpPosi + splitLen);
/*      */     }
/*      */ 
/*  722 */     retu = tmpStr.substring(0, tmpStr.indexOf(splitStr));
/*  723 */     return retu;
/*      */   }
/*      */ 
/*      */   public static String decomposeString(String sourceStr, int pos)
/*      */   {
/*  740 */     return decomposeString(sourceStr, "|", pos);
/*      */   }
/*      */ 
/*      */   public static String trim(String sourceStr, char removeChar)
/*      */   {
/*  757 */     if (sourceStr == null)
/*      */     {
/*  759 */       return null;
/*      */     }
/*  761 */     sourceStr = sourceStr.trim();
/*  762 */     int loInt_begin = 0; int loInt_end = 0;
/*  763 */     int loInt_len = sourceStr.length();
/*  764 */     for (int i = 0; i < loInt_len; i++)
/*      */     {
/*  766 */       if (sourceStr.charAt(i) != removeChar)
/*      */         break;
/*  768 */       loInt_begin++;
/*      */     }
/*      */ 
/*  775 */     for (int i = 0; i < loInt_len; i++)
/*      */     {
/*  777 */       if (sourceStr.charAt(loInt_len - 1 - i) != removeChar)
/*      */         break;
/*  779 */       loInt_end++;
/*      */     }
/*      */ 
/*  786 */     return sourceStr.substring(loInt_begin, loInt_len - loInt_end);
/*      */   }
/*      */ 
/*      */   public static String substring(String sourceStr, int len, String appendStr)
/*      */   {
/*  803 */     if ((null == sourceStr) || ("".equals(sourceStr)))
/*      */     {
/*  805 */       return sourceStr;
/*      */     }
/*  807 */     if (len <= 0)
/*      */     {
/*  809 */       return "";
/*      */     }
/*      */ 
/*  812 */     if (null == appendStr)
/*      */     {
/*  814 */       appendStr = "";
/*      */     }
/*      */ 
/*  817 */     int sourceLen = sourceStr.length();
/*  818 */     if (len >= sourceLen)
/*      */     {
/*  820 */       return sourceStr;
/*      */     }
/*      */ 
/*  824 */     return sourceStr.substring(0, len) + appendStr;
/*      */   }
/*      */ 
/*      */   public static String random(int length)
/*      */   {
/*  837 */     String retu = "";
/*      */ 
/*  839 */     char[] letters = _$4328();
/*  840 */     int[] numbers = _$4330();
/*      */ 
/*  842 */     for (int i = 0; i < length; i++)
/*      */     {
/*  844 */       int d1 = (int)(Math.random() * 10.0D) % 2;
/*  845 */       if (d1 == 0)
/*      */       {
/*  847 */         int d2 = (int)(Math.random() * 100.0D) % 52;
/*  848 */         retu = retu + letters[d2];
/*      */       } else {
/*  850 */         if (d1 != 1)
/*      */           continue;
/*  852 */         retu = retu + (int)(Math.random() * 10.0D);
/*      */       }
/*      */     }
/*  855 */     return retu;
/*      */   }
/*      */ 
/*      */   public static String randomString(int length)
/*      */   {
/*  867 */     String retu = "";
/*      */ 
/*  869 */     char[] letters = _$4328();
/*      */ 
/*  871 */     for (int i = 0; i < length; i++)
/*      */     {
/*  873 */       int d2 = (int)(Math.random() * 100.0D) % 52;
/*  874 */       retu = retu + letters[d2];
/*      */     }
/*  876 */     return retu;
/*      */   }
/*      */ 
/*      */   public static String randomNumber(int length)
/*      */   {
/*  888 */     String retu = "";
/*  889 */     int[] numbers = _$4330();
/*      */ 
/*  891 */     for (int i = 0; i < length; i++)
/*      */     {
/*  893 */       retu = retu + (int)(Math.random() * 10.0D);
/*      */     }
/*  895 */     return retu;
/*      */   }
/*      */ 
/*      */   private static char[] _$4328()
/*      */   {
/*  904 */     char[] ca = new char[52];
/*  905 */     for (int i = 0; i < 26; i++)
/*      */     {
/*  907 */       ca[i] = (char)(65 + i);
/*      */     }
/*  909 */     for (int i = 26; i < 52; i++)
/*      */     {
/*  911 */       ca[i] = (char)(71 + i);
/*      */     }
/*  913 */     return ca;
/*      */   }
/*      */ 
/*      */   private static int[] _$4330()
/*      */   {
/*  922 */     int[] na = new int[10];
/*  923 */     for (int i = 0; i < 10; i++)
/*      */     {
/*  925 */       na[i] = i;
/*      */     }
/*  927 */     return na;
/*      */   }
/*      */ 
/*      */   public static String encrypt(String source, boolean flag)
/*      */   {
/*  943 */     if ((null == source) || ("".equals(source.trim())))
/*      */     {
/*  945 */       return source;
/*      */     }
/*  947 */     String LS_KEY1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*(),.;[]{}";
/*  948 */     String LS_KEY2 = "&*()AD;[BFLCGH]{PQ}EI!JKRYZ1MSTXNO23UV,.Wcd45ef6lm7g@h0a#$nij8ob%^ptvz9ku~qrswxy";
/*      */ 
/*  950 */     char[] ch_source = source.toCharArray();
/*      */ 
/*  952 */     StringBuffer strBuf = new StringBuffer(ch_source.length);
/*      */ 
/*  955 */     int i = 0; for (int Len = ch_source.length; i < Len; i++)
/*      */     {
/*  957 */       if (flag)
/*      */       {
/*  959 */         int pos = LS_KEY1.indexOf(ch_source[i]);
/*  960 */         if (pos >= 0)
/*      */         {
/*  962 */           strBuf.append(LS_KEY2.substring(pos, pos + 1));
/*      */         }
/*      */         else
/*      */         {
/*  966 */           strBuf.append(ch_source[i]);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  971 */         int pos = LS_KEY2.indexOf(ch_source[i]);
/*  972 */         if (pos >= 0)
/*      */         {
/*  974 */           strBuf.append(LS_KEY1.substring(pos, pos + 1));
/*      */         }
/*      */         else
/*      */         {
/*  978 */           strBuf.append(ch_source[i]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  983 */     return strBuf.toString();
/*      */   }
/*      */ 
/*      */   public static String toVisualString(String sourceStr)
/*      */   {
/*  994 */     if ((sourceStr == null) || (sourceStr.equals("")))
/*      */     {
/*  996 */       return "";
/*      */     }
/*      */ 
/* 1000 */     return sourceStr;
/*      */   }
/*      */ 
/*      */   public static String pad(String sourceStr, int length, char withChar, boolean isPadToEnd)
/*      */   {
/* 1015 */     if (sourceStr == null)
/*      */     {
/* 1017 */       return null;
/*      */     }
/* 1019 */     if (sourceStr.length() >= length)
/*      */     {
/* 1021 */       return sourceStr;
/*      */     }
/*      */ 
/* 1024 */     StringBuffer sb = new StringBuffer(sourceStr);
/* 1025 */     for (int i = 0; i < length - sourceStr.length(); i++)
/*      */     {
/* 1027 */       if (isPadToEnd)
/*      */       {
/* 1029 */         sb.append(withChar);
/*      */       }
/*      */       else
/*      */       {
/* 1033 */         sb.insert(0, withChar);
/*      */       }
/*      */     }
/* 1036 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/* 1045 */     String[] ss = splitString("1000000360,", ",", true);
/* 1046 */     for (int i = 0; i < ss.length; i++)
/*      */     {
/* 1048 */       System.out.println("ss" + ss[i]);
/*      */     }
/*      */   }
/*      */   public static int parseInt(String str, int defaultValue){
/*      */       int intValue = defaultValue;
/*      */       try {
/*      */			if("null".equalsIgnoreCase(str)){
/*      */				intValue = defaultValue;
/*      */          }else{
/*      */  			intValue = Integer.parseInt(str);	
/*      */			}
/*      */       } catch (Exception e) {
/*      */   	}
/*      */      return intValue;
/*      */   }
/*      */ }