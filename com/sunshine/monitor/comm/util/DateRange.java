/*     */ package com.sunshine.monitor.comm.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.GregorianCalendar;
/*     */ 
/*     */ public class DateRange
/*     */ {
/*     */   private String _$6933;
/*     */   private String _$6934;
/*     */   public static final int ALL_DATETIME = 0;
/*     */   public static final int TODAY = 1;
/*     */   public static final int THIS_WEEK = 2;
/*     */   public static final int THIS_MONTH = 3;
/*     */   public static final int THIS_QUARTER = 4;
/*     */   public static final int THIS_YEAR = 5;
/*     */   public static final int YESTERDAY = 6;
/*     */   public static final int LAST_5_DAY = 7;
/*     */   public static final int ADVANCE = 10;
/*     */   public static final int DATE_TYPE_8 = 1;
/*     */   public static final int DATE_TYPE_14 = 2;
/*     */ 
/*     */   public DateRange(int dateRangeType, int dateType)
/*     */   {
/*  42 */     GregorianCalendar calender = new GregorianCalendar();
/*  43 */     String str_8 = DateUtils.getCurrDateStr(1);
/*  44 */     String str_14 = DateUtils.getCurrTimeStr(1);
/*  45 */     if (dateType == 1)
/*     */     {
/*  47 */       switch (dateRangeType)
/*     */       {
/*     */       case 0:
/*  50 */         this._$6933 = "00000000";
/*  51 */         this._$6934 = "99999999";
/*  52 */         break;
/*     */       case 1:
/*  54 */         this._$6933 = str_8;
/*  55 */         this._$6934 = str_8;
/*  56 */         break;
/*     */       case 2:
/*  58 */         calender.add(7, -calender.get(7) + 1);
/*     */ 
/*  60 */         this._$6933 = DateUtils.getDateStr(calender.getTime(), 1);
/*     */ 
/*  62 */         this._$6934 = str_8;
/*  63 */         break;
/*     */       case 3:
/*  65 */         calender.add(5, -calender.get(5) + 1);
/*     */ 
/*  67 */         this._$6933 = DateUtils.getDateStr(calender.getTime(), 1);
/*     */ 
/*  69 */         this._$6934 = str_8;
/*  70 */         break;
/*     */       case 5:
/*  72 */         calender.add(6, -calender.get(6) + 1);
/*     */ 
/*  74 */         this._$6933 = DateUtils.getDateStr(calender.getTime(), 1);
/*     */ 
/*  76 */         this._$6934 = str_8;
/*  77 */         break;
/*     */       case 6:
/*  79 */         calender.add(6, -1);
/*  80 */         this._$6933 = DateUtils.getDateStr(calender.getTime(), 1);
/*     */ 
/*  82 */         this._$6934 = DateUtils.getDateStr(calender.getTime(), 1);
/*     */ 
/*  84 */         break;
/*     */       case 7:
/*  86 */         calender.add(6, -4);
/*  87 */         this._$6933 = DateUtils.getDateStr(calender.getTime(), 1);
/*     */ 
/*  89 */         this._$6934 = str_8;
/*  90 */         break;
/*     */       case 4:
/*     */       default:
/*  92 */         break;
/*     */       }
/*     */     }
/*  95 */     else if (dateType == 2)
/*     */     {
/*  97 */       switch (dateRangeType)
/*     */       {
/*     */       case 0:
/* 100 */         this._$6933 = "00000000000000";
/* 101 */         this._$6934 = "99991231235959";
/* 102 */         break;
/*     */       case 1:
/* 104 */         this._$6933 = (str_8 + "000000");
/* 105 */         this._$6934 = (str_8 + "235959");
/* 106 */         break;
/*     */       case 2:
/* 108 */         calender.add(7, -calender.get(7) + 1);
/*     */ 
/* 110 */         this._$6933 = (DateUtils.getDateStr(calender.getTime(), 1) + "000000");
/*     */ 
/* 112 */         this._$6934 = (str_8 + "235959");
/* 113 */         break;
/*     */       case 3:
/* 119 */         break;
/*     */       case 5:
/* 121 */         calender.add(6, -calender.get(6) + 1);
/*     */ 
/* 123 */         this._$6933 = (DateUtils.getDateStr(calender.getTime(), 1) + "000000");
/* 124 */         this._$6934 = (str_8 + "235959");
/* 125 */         break;
/*     */       case 6:
/* 127 */         calender.add(6, -1);
/* 128 */         this._$6933 = (DateUtils.getDateStr(calender.getTime(), 1) + "000000");
/*     */ 
/* 130 */         this._$6934 = (DateUtils.getDateStr(calender.getTime(), 1) + "235959");
/*     */ 
/* 132 */         break;
/*     */       case 7:
/* 134 */         calender.add(6, -4);
/* 135 */         this._$6933 = (DateUtils.getDateStr(calender.getTime(), 1) + "000000");
/*     */ 
/* 137 */         this._$6934 = (str_8 + "235959");
/* 138 */         break;
/*     */       case 4:
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getBeginDatetime()
/*     */   {
/* 147 */     return this._$6933;
/*     */   }
/*     */ 
/*     */   public String getEndDatetime()
/*     */   {
/* 152 */     return this._$6934;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 157 */     String str = "开始时间：" + this._$6933 + "结束时间：" + this._$6934;
/* 158 */     return str;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 163 */     DateRange d = new DateRange(1, 2);
/* 164 */     System.out.println("from:" + d.getBeginDatetime() + " end:" + d.getEndDatetime());
/*     */   }
/*     */ }