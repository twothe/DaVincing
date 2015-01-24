/* 1:  */ package hx.utils;
/* 2:  */ 
/* 3:  */ import java.io.PrintStream;
/* 4:  */ 
/* 5:  */ public class Debug
/* 6:  */ {
/* 7:  */   public static <T> void log(T... thing)
/* 8:  */   {
/* 9:7 */     StringBuilder sb = new StringBuilder();
/* ::8 */     for (T i : thing) {
/* ;:8 */       sb.append(i + ", ");
/* <:  */     }
/* =:9 */     System.err.println(sb.toString());
/* >:  */   }
/* ?:  */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.utils.Debug
 * JD-Core Version:    0.7.0.1
 */