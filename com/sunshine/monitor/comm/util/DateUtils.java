/*     */ package com.sunshine.monitor.comm.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ 
/*     */ public class DateUtils
/*     */ {
/*     */   public static final int DB_STORE_DATE = 1;
/*     */   public static final int HYPHEN_DISPLAY_DATE = 2;
/*     */   public static final int DOT_DISPLAY_DATE = 3;
/*     */   public static final int CN_DISPLAY_DATE = 4;
/*     */ 
/*     */   public static String getCurrTimeStr(int formatType)
/*     */   {
/*  47 */     return getTimeStr(new Date(), formatType);
/*     */   }
/*     */ 
/*     */   public static String getTimeStr(Date date, int formatType)
/*     */   {
/*  59 */     if ((formatType < 1) || (formatType > 4))
/*     */     {
/*  61 */       throw new IllegalArgumentException("时间格式化类型不是合法的值。");
/*     */     }
/*     */ 
/*  65 */     String formatStr = null;
/*  66 */     switch (formatType)
/*     */     {
/*     */     case 1:
/*  69 */       formatStr = "yyyyMMddHHmmss";
/*  70 */       break;
/*     */     case 2:
/*  72 */       formatStr = "yyyy'-'MM'-'dd HH:mm:ss";
/*  73 */       break;
/*     */     case 3:
/*  75 */       formatStr = "yyyy.MM.dd HH:mm:ss";
/*  76 */       break;
/*     */     case 4:
/*  78 */       formatStr = "yyyy'年'MM'月'dd HH:mm:ss";
/*     */     }
/*     */ 
/*  81 */     SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
/*  82 */     return sdf.format(date);
/*     */   }
/*     */ 
/*     */   public static String getCurrDateStr(int formatType)
/*     */   {
/*  93 */     return getDateStr(new Date(), formatType);
/*     */   }
/*     */ 
/*     */   public static String getDateStr(Date date, int formatType)
/*     */   {
/* 104 */     if ((formatType < 1) || (formatType > 4))
/*     */     {
/* 106 */       throw new IllegalArgumentException("时间格式化类型不是合法的值。");
/*     */     }
/*     */ 
/* 110 */     String formatStr = null;
/* 111 */     switch (formatType)
/*     */     {
/*     */     case 1:
/* 114 */       formatStr = "yyyyMMdd";
/* 115 */       break;
/*     */     case 2:
/* 117 */       formatStr = "yyyy-MM-dd";
/* 118 */       break;
/*     */     case 3:
/* 120 */       formatStr = "yyyy.MM.dd";
/* 121 */       break;
/*     */     case 4:
/* 123 */       formatStr = "yyyy'年'MM'月'dd";
/*     */     }
/*     */ 
/* 126 */     SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
/* 127 */     return sdf.format(date);
/*     */   }
/*     */ 
/*     */   public static String getYearMonthStr(int formatType)
/*     */   {
/* 138 */     return getYearMonthStr(new Date(), formatType);
/*     */   }
/*     */ 
/*     */   public static String getYearMonthStr(Date date, int formatType)
/*     */   {
/* 149 */     if ((formatType < 1) || (formatType > 4))
/*     */     {
/* 151 */       throw new IllegalArgumentException("时间格式化类型不是合法的值。");
/*     */     }
/*     */ 
/* 155 */     String formatStr = null;
/* 156 */     switch (formatType)
/*     */     {
/*     */     case 1:
/* 159 */       formatStr = "yyyyMM";
/* 160 */       break;
/*     */     case 2:
/* 162 */       formatStr = "yyyy-MM";
/* 163 */       break;
/*     */     case 3:
/* 165 */       formatStr = "yyyy.MM";
/* 166 */       break;
/*     */     case 4:
/* 168 */       formatStr = "yyyy'年'MM'月'";
/*     */     }
/*     */ 
/* 171 */     SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
/* 172 */     return sdf.format(date);
/*     */   }
/*     */ 
/*     */   public static String toDisplayStr(String dateStr, int formatType)
/*     */   {
/* 184 */     if ((formatType < 1) || (formatType > 4))
/*     */     {
/* 186 */       throw new IllegalArgumentException("时间格式化类型不是合法的值。");
/*     */     }
/* 188 */     if ((dateStr == null) || (dateStr.length() < 6) || (dateStr.length() > 14) || (formatType == 1))
/*     */     {
/* 191 */       return StringUtils.toVisualString(dateStr);
/*     */     }
/*     */ 
/* 195 */     char[] charArr = null;
/* 196 */     switch (formatType)
/*     */     {
/*     */     case 2:
/* 199 */       charArr = new char[] { '-', '-', ' ', ':', ':' };
/*     */ 
/* 202 */       break;
/*     */     case 3:
/* 204 */       charArr = new char[] { '.', '.', ' ', ':', ':' };
/*     */ 
/* 207 */       break;
/*     */     case 4:
/* 209 */       charArr = new char[] { '年', '月', ' ', ':', ':' };
/*     */ 
/* 212 */       break;
/*     */     default:
/* 214 */       charArr = new char[] { '-', '-', ' ', ':', ':' };
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 220 */       SimpleDateFormat sdf_1 = null;
/* 221 */       SimpleDateFormat sdf_2 = null;
/* 222 */       switch (dateStr.length())
/*     */       {
/*     */       case 6:
/* 225 */         sdf_1 = new SimpleDateFormat("yyyyMM");
/* 226 */         sdf_2 = new SimpleDateFormat("yyyy'" + charArr[0] + "'MM");
/*     */ 
/* 228 */         break;
/*     */       case 8:
/* 230 */         sdf_1 = new SimpleDateFormat("yyyyMMdd");
/* 231 */         sdf_2 = new SimpleDateFormat("yyyy'" + charArr[0] + "'MM'" + charArr[1] + "'dd");
/*     */ 
/* 233 */         break;
/*     */       case 10:
/* 235 */         sdf_1 = new SimpleDateFormat("yyyyMMddHH");
/* 236 */         sdf_2 = new SimpleDateFormat("yyyy'" + charArr[0] + "'MM'" + charArr[1] + "'dd'" + "+charArr[2]" + "'HH");
/*     */ 
/* 239 */         break;
/*     */       case 12:
/* 241 */         sdf_1 = new SimpleDateFormat("yyyyMMddHHmm");
/* 242 */         sdf_2 = new SimpleDateFormat("yyyy'" + charArr[0] + "'MM'" + charArr[1] + "'dd'" + charArr[2] + "'HH'" + charArr[3] + "'mm");
/*     */ 
/* 245 */         break;
/*     */       case 14:
/* 247 */         sdf_1 = new SimpleDateFormat("yyyyMMddHHmmss");
/* 248 */         sdf_2 = new SimpleDateFormat("yyyy'" + charArr[0] + "'MM'" + charArr[1] + "'dd'" + charArr[2] + "'HH'" + charArr[3] + "'mm'" + charArr[4] + "'ss");
/*     */ 
/* 252 */         break;
/*     */       case 7:
/*     */       case 9:
/*     */       case 11:
/*     */       case 13:
/*     */       default:
/* 254 */         return dateStr;
/*     */       }
/* 256 */       return sdf_2.format(sdf_1.parse(dateStr));
/*     */     }
/*     */     catch (ParseException ex) {
/*     */     }
/* 260 */     return dateStr;
/*     */   }
/*     */ 
/*     */   public static String toStoreStr(String dateStr)
/*     */   {
/* 272 */     if ((dateStr == null) || (dateStr.trim().equals("")))
/*     */     {
/* 274 */       return "";
/*     */     }
/* 276 */     StringBuffer strBuf = new StringBuffer();
/* 277 */     for (int i = 0; i < dateStr.length(); i++)
/*     */     {
/* 279 */       if ((dateStr.charAt(i) < '0') || (dateStr.charAt(i) > '9'))
/*     */         continue;
/* 281 */       strBuf.append(dateStr.charAt(i));
/*     */     }
/*     */ 
/* 284 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   public static String birthdayToAge(String birthdayStr)
/*     */   {
/* 294 */     if ((birthdayStr == null) || (birthdayStr.length() < 6))
/*     */     {
/* 296 */       return "";
/*     */     }
/*     */ 
/* 300 */     int birthYear = Integer.parseInt(birthdayStr.substring(0, 4));
/* 301 */     int birthMonth = Integer.parseInt(birthdayStr.substring(4, 6));
/* 302 */     Calendar cal = new GregorianCalendar();
/* 303 */     int currYear = cal.get(1);
/* 304 */     int currMonth = cal.get(2);
/* 305 */     int age = currYear - birthYear;
/* 306 */     age -= (currMonth < birthMonth ? 1 : 0);
/* 307 */     return "" + age;
/*     */   }
/*     */ 
/*     */   public static String add(String dateTimeStr, int formatType, int detal, int field)
/*     */   {
/* 322 */     if ((dateTimeStr == null) || (dateTimeStr.length() < 6))
/*     */     {
/* 324 */       return dateTimeStr;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 330 */       String formatStr = null;
/* 331 */       switch (formatType)
/*     */       {
/*     */       case 1:
/* 334 */         formatStr = "yyyyMMddHHmmss";
/* 335 */         break;
/*     */       case 2:
/* 337 */         formatStr = "yyyy-MM-dd HH:mm:ss";
/* 338 */         break;
/*     */       case 3:
/* 340 */         formatStr = "yyyy.MM.dd HH:mm:ss";
/* 341 */         break;
/*     */       case 4:
/* 343 */         formatStr = "yyyy'年'MM'月' HH：mm：ss";
/* 344 */         break;
/*     */       default:
/* 346 */         formatStr = "yyyyMMddHHmmss";
/*     */       }
/*     */ 
/* 350 */       formatStr = formatStr.substring(0, dateTimeStr.length());
/* 351 */       SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
/* 352 */       Date d = sdf.parse(dateTimeStr);
/* 353 */       GregorianCalendar g = new GregorianCalendar();
/* 354 */       g.setTime(d);
/* 355 */       g.add(field, detal);
/* 356 */       d = g.getTime();
/* 357 */       return sdf.format(d);
/*     */     }
/*     */     catch (ParseException ex)
/*     */     {
/* 361 */       ex.printStackTrace();
/* 362 */     }return dateTimeStr;
/*     */   }
/*     */ 
/*     */   public static String getDateFormat(Date date, String outFmt)
/*     */   {
/* 391 */     if (null == date)
/*     */     {
/* 393 */       return null;
/*     */     }
/* 395 */     if ((null == outFmt) || ("".equals(outFmt.trim())))
/*     */     {
/* 397 */       outFmt = "yyyyMMdd";
/*     */     }
/*     */ 
/* 400 */     String retu = null;
/* 401 */     SimpleDateFormat dateFormat = null;
/*     */     try
/*     */     {
/* 404 */       dateFormat = new SimpleDateFormat(outFmt);
/*     */     }
/*     */     catch (IllegalArgumentException iaex)
/*     */     {
/* 408 */       dateFormat = new SimpleDateFormat("yyyyMMdd");
/*     */     }
/* 410 */     retu = dateFormat.format(date);
/*     */ 
/* 412 */     dateFormat = null;
/*     */ 
/* 414 */     return retu;
/*     */   }
/*     */ 
/*     */   public static String getDateFormat(Date date)
/*     */   {
/* 426 */     return getDateFormat(date, "yyyyMMdd");
/*     */   }
/*     */ 
/*     */   public static String getDateFormat(String outFmt)
/*     */   {
/* 438 */     return getDateFormat(new Date(), outFmt);
/*     */   }
/*     */ 
/*     */   public static String getDateFormat()
/*     */   {
/* 448 */     return getDateFormat(new Date(), "yyyyMMdd");
/*     */   }
/*     */ 
/*     */   public static String getDateFormat(long millis, String outFmt)
/*     */   {
/* 462 */     Date d = new Date(millis);
/* 463 */     Calendar calendar = Calendar.getInstance();
/* 464 */     calendar.setTime(d);
/* 465 */     String retu = getDateFormat(calendar.getTime(), outFmt);
/* 466 */     calendar = null;
/* 467 */     return retu;
/*     */   }
/*     */ 
/*     */   public static String getDateFormat(String datestr, String inFmt, String outFmt)
/*     */     throws ParseException
/*     */   {
/* 487 */     if ((null == datestr) || ("".equals(datestr.trim())))
/*     */     {
/* 489 */       return datestr;
/*     */     }
/*     */ 
/* 492 */     if ((null == inFmt) || ("".equals(inFmt.trim())))
/*     */     {
/* 494 */       return datestr;
/*     */     }
/*     */ 
/* 497 */     if ((null == outFmt) || ("".equals(outFmt.trim())))
/*     */     {
/* 499 */       outFmt = "yyyyMMdd";
/*     */     }
/*     */ 
/* 502 */     Date inDate = getDate(datestr, inFmt);
/*     */ 
/* 504 */     if (null == inDate)
/*     */     {
/* 506 */       return datestr;
/*     */     }
/*     */ 
/* 509 */     String retu = getDateFormat(inDate, outFmt);
/* 510 */     inDate = null;
/* 511 */     return retu;
/*     */   }
/*     */ 
/*     */   public static String getDateFormat(String datestr, String inFmt)
/*     */     throws ParseException
/*     */   {
/* 527 */     return getDateFormat(datestr, inFmt, "yyyyMMdd");
/*     */   }
/*     */ 
/*     */   public static Date getDate(String datestr, String inFmt)
/*     */     throws ParseException
/*     */   {
/* 543 */     if ((null == datestr) || ("".equals(datestr.trim())))
/*     */     {
/* 545 */       return null;
/*     */     }
/*     */ 
/* 548 */     if ((null == inFmt) || ("".equals(inFmt.trim())))
/*     */     {
/* 550 */       inFmt = "yyyyMMdd";
/*     */     }
/*     */ 
/* 553 */     Date inDate = null;
/*     */ 
/* 556 */     SimpleDateFormat inDateFormat = new SimpleDateFormat(inFmt);
/* 557 */     inDateFormat.setLenient(true);
/* 558 */     inDate = inDateFormat.parse(datestr);
/*     */ 
/* 560 */     inDateFormat = null;
/* 561 */     return inDate;
/*     */   }
/*     */ 
/*     */   public static Date addDate(Date date, int CALENDARFIELD, int amount)
/*     */   {
/* 598 */     if (null == date)
/*     */     {
/* 600 */       return date;
/*     */     }
/* 602 */     Calendar calendar = Calendar.getInstance();
/* 603 */     calendar.setTime(date);
/* 604 */     calendar.add(CALENDARFIELD, amount);
/* 605 */     return calendar.getTime();
/*     */   }
/*     */ 
/*     */   public static Date addDate(String datestr, int CALENDARFIELD, int amount)
/*     */     throws ParseException
/*     */   {
/* 624 */     return addDate(getDate(datestr, "yyyyMMdd"), CALENDARFIELD, amount);
/*     */   }
/*     */ 
/*     */   public static int getAge(Date birthday, Date date2)
/*     */   {
/* 638 */     if ((null == birthday) || (null == date2))
/*     */     {
/* 640 */       return -1;
/*     */     }
/*     */ 
/* 643 */     if (birthday.after(date2))
/*     */     {
/* 645 */       return -1;
/*     */     }
/*     */ 
/* 648 */     int ibdYear = StringUtils.getInt(getDateFormat(birthday, "yyyy"), -1);
/* 649 */     int idate2Year = StringUtils.getInt(getDateFormat(date2, "yyyy"), -1);
/*     */ 
/* 651 */     if ((ibdYear < 0) || (idate2Year < 0))
/*     */     {
/* 653 */       return -1;
/*     */     }
/* 655 */     if (ibdYear > idate2Year)
/*     */     {
/* 657 */       return -1;
/*     */     }
/*     */ 
/* 660 */     return idate2Year - ibdYear + 1;
/*     */   }
/*     */ 
/*     */   public static int getAge(Date birthday)
/*     */   {
/* 672 */     return getAge(birthday, new Date());
/*     */   }
/*     */ 
/*     */   public static int getAge(String birthdaystr)
/*     */     throws ParseException
/*     */   {
/* 686 */     return getAge(getDate(birthdaystr, "yyyyMMdd"));
/*     */   }
/*     */ 
/*     */   public static long getTimeValue(String datetime, String inFmt)
/*     */     throws ParseException
/*     */   {
/* 701 */     return getDate(datetime, inFmt).getTime();
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 709 */       System.out.println("date:" + getDate("04-1-6", "yy-MM-dd"));
/*     */     }
/*     */     catch (ParseException ex)
/*     */     {
/* 714 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ }