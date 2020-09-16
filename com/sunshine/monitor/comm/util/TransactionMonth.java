/*     */ package com.sunshine.monitor.comm.util;
/*     */ 
/*     */ public class TransactionMonth
/*     */ {
/*     */   private String _$6974;
/*     */   private String _$6975;
/*     */ 
/*     */   public TransactionMonth(String transMonth, String fromFormater, String toFormater)
/*     */   {
/*  41 */     this._$6974 = _$6979(transMonth, fromFormater);
/*  42 */     this._$6975 = _$6979(transMonth, toFormater);
/*  43 */     this._$6974 = (_$6979(transMonth, fromFormater) + "000000");
/*  44 */     this._$6975 = (_$6979(transMonth, toFormater) + "999999");
/*     */   }
/*     */ 
/*     */   public TransactionMonth(String fromFormater, String toFormater)
/*     */   {
/*  54 */     this(DateUtils.getYearMonthStr(1), fromFormater, toFormater);
/*     */   }
/*     */ 
/*     */   private static String _$6979(String month, String monthRangeFormater)
/*     */   {
/*  69 */     if ((month == null) || (monthRangeFormater == null))
/*     */     {
/*  71 */       throw new IllegalArgumentException("月份参数不能这空");
/*     */     }
/*  73 */     if (month.length() != 6)
/*     */     {
/*  75 */       throw new IllegalArgumentException("月份字符串必须是6位，格式为yyyymm");
/*     */     }
/*  77 */     if (!_$6982(monthRangeFormater))
/*     */     {
/*  79 */       throw new IllegalArgumentException("月份时间段格式定义字串格式不正确，正确的格式必须为[0|-1|1]:XX,其中xx为01~31之间的数");
/*     */     }
/*     */ 
/*  83 */     int detal = Integer.parseInt(monthRangeFormater.substring(0, monthRangeFormater.indexOf(":")));
/*     */ 
/*  85 */     String monthDate = monthRangeFormater.substring(monthRangeFormater.indexOf(":") + 1);
/*     */ 
/*  88 */     return DateUtils.add(month, 1, detal, 2) + monthDate;
/*     */   }
/*     */ 
/*     */   private static boolean _$6982(String monthRangeFormater)
/*     */   {
/* 101 */     if (monthRangeFormater == null)
/*     */     {
/* 103 */       return false;
/*     */     }
/* 105 */     if (monthRangeFormater.length() < 4)
/*     */     {
/* 107 */       return false;
/*     */     }
/* 109 */     int monthDetal = 0;
/*     */     try
/*     */     {
/* 112 */       monthDetal = Integer.parseInt(monthRangeFormater.substring(0, monthRangeFormater.indexOf(":")));
/*     */     }
/*     */     catch (NumberFormatException ex)
/*     */     {
/* 117 */       return false;
/*     */     }
/* 119 */     if ((monthDetal != -1) && (monthDetal != 0) && (monthDetal != 1))
/*     */     {
/* 121 */       return false;
/*     */     }
/* 123 */     String strDate = monthRangeFormater.substring(monthRangeFormater.indexOf(":") + 1);
/*     */     try
/*     */     {
/* 128 */       int intDate = Integer.parseInt(strDate);
/* 129 */       if ((intDate < 1) || (intDate > 31))
/*     */       {
/* 131 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (NumberFormatException ex1)
/*     */     {
/* 137 */       return false;
/*     */     }
    return true;
  }
 
  public String getFromDateStr()
 {
    return this._$6974;
   }
 
  public String getToDateStr()
   {
    return this._$6975;
   }
}
