package com.fw.common.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fw.common.utils.DateUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;


public final class ExcelUtils {
    
    @SuppressWarnings({ "deprecation", "static-access" })
    public static String getValue(Cell cell) {
        if(null == cell) {
            return null;
        }
        if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            if(HSSFDateUtil.isCellDateFormatted(cell)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
            }
            return String.valueOf(cell.getNumericCellValue());
        } else if(cell.getCellType() == cell.CELL_TYPE_FORMULA) {
            try {  
               return String.valueOf(cell.getStringCellValue());  
            } catch (IllegalStateException e) {  
                return String.valueOf(cell.getNumericCellValue());  
            }  
        } else {
            // 返回字符串类型的值
            return String.valueOf(cell.getStringCellValue());
        }
    }
    
    public static Double stringToDouble(String numStr) {
        if(StringUtils.isNotBlank(numStr)) {
            BigDecimal bd = new BigDecimal(numStr);
            return Double.valueOf(bd.toPlainString());
        }
        return null;
    }
    
    public static Integer stringToInt(String numStr) {
    	if(StringUtils.isNotBlank(numStr)) {
    		numStr = numStr.indexOf(".") >=  0 ? numStr.substring(0, numStr.indexOf(".")) : numStr;
            return Integer.valueOf(numStr);
        }
    	return null;
    }
    
    public static Date stringToDate(String date) {
    	if(StringUtils.isNotBlank(date)) {
            return DateUtils.parseDate(date);
        }
    	return null;
    }
    
    
    public static BigDecimal stringToBig(String numStr) {
        if(StringUtils.isNotBlank(numStr)) {
            BigDecimal bd = new BigDecimal(numStr.replaceAll(",", ""));
            return new BigDecimal(bd.toPlainString());
        }
        return null;
    }
    
    /*private Double stringToFour(String numStr) {
        if(StringUtils.isNotBlank(numStr)) {
            BigDecimal b= new BigDecimal(numStr);
            return b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return null;
    }*/
    
    public static BigDecimal stringToFourBig(String numStr) {
        if (StringUtils.isNotBlank(numStr)) {
            BigDecimal b = new BigDecimal(numStr);
            return b.setScale(4, BigDecimal.ROUND_HALF_UP);
        }
        return null;
    }

    /*private Double twoDecimals(String numStr) {
        if(StringUtils.isNotBlank(numStr)) {
            BigDecimal b=new BigDecimal(Double.valueOf(numStr));
            return b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return null;
    }*/
    
    public static String twoPoint(String s) {
        if(StringUtils.isNotBlank(s)) {
            if(s.contains(".")) {
                BigDecimal b=new BigDecimal(Double.valueOf(s));
                return "" + b.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
            } else {
                return s;
            }
        }
        return null;
    }
    
    public static String removePoint(String s) {
        if(StringUtils.isNotBlank(s)) {
            if(s.contains(".")) {
                BigDecimal b=new BigDecimal(Double.valueOf(s));
                return "" + b.setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
            } else {
                return s;
            }
        }
        return null;
    }
    
    public static void main(String[] args) {
    	
    	System.err.println(stringToInt("11.0"));
    	System.out.println("--------------------");
        System.out.println("ZSVO_Vowxn6AGoGAAAID-rMuPgw768".length());
        String c = "&lt;?xml&nbsp;version=&quot;1.0&quot;&nbsp;encoding=&quot;utf-8&quot;&nbsp;?&gt;&lt;template&nbsp;id=&quot;44b8b6670b56d17d&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;variables&gt;&lt;variable&nbsp;name=&quot;owner&quot;&nbsp;type=&quot;STRING&quot;&nbsp;/&gt;&lt;variable&nbsp;name=&quot;dispatcher&quot;&nbsp;type=&quot;STRING&quot;&nbsp;/&gt;&lt;variable&nbsp;name=&quot;amount&quot;&nbsp;type=&quot;NUMBER/STRING/BOOLEAN&quot;&nbsp;/&gt;&lt;/variables&gt;&lt;activities&gt;&lt;activity&nbsp;id=&quot;44b8b6670b56d17d_客户经理提交申请&quot;&nbsp;code=&quot;0&quot;&nbsp;name=&quot;客户经理提交申请&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;START&quot;&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;44b8b6670b56d17d_调查&lt;/target&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;activity&nbsp;id=&quot;44b8b6670b56d17d_调查&quot;&nbsp;code=&quot;2&quot;&nbsp;name=&quot;调查&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;NORMAL&quot;&gt;&lt;approvers&gt;&lt;allocation&gt;ONE&lt;/allocation&gt;&lt;method&gt;ActivityApproverByOwner&lt;/method&gt;&lt;/approvers&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;44b8b6670b56d17d_调查_co0&quot;&nbsp;code=&quot;Submit&quot;&nbsp;name=&quot;提交&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;null&quot;&gt;&lt;/conclusion&gt;&lt;/conclusions&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;44b8b6670b56d17d_资料审查&lt;/target&gt;&lt;conclusion&gt;44b8b6670b56d17d_调查_co0&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;activity&nbsp;id=&quot;44b8b6670b56d17d_资料审查&quot;&nbsp;code=&quot;8&quot;&nbsp;name=&quot;资料审查&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;NORMAL&quot;&gt;&lt;approvers&gt;&lt;allocation&gt;ALL&lt;/allocation&gt;&lt;method&gt;ActivityApproverByGroup.group1&lt;/method&gt;&lt;refuse&gt;investigate&lt;/refuse&gt;&lt;/approvers&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;44b8b6670b56d17d_资料审查_co0&quot;&nbsp;code=&quot;pass&quot;&nbsp;name=&quot;通过&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;44b8b6670b56d17d_资料审查_co1&quot;&nbsp;code=&quot;return&quot;&nbsp;name=&quot;退回&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;null&quot;&gt;&lt;/conclusion&gt;&lt;/conclusions&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;44b8b6670b56d17d_调查&lt;/target&gt;&lt;conclusion&gt;44b8b6670b56d17d_资料审查_co1&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;44b8b6670b56d17d_待放款&lt;/target&gt;&lt;conclusion&gt;44b8b6670b56d17d_资料审查_co0&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;activity&nbsp;id=&quot;44b8b6670b56d17d_待放款&quot;&nbsp;code=&quot;512&quot;&nbsp;name=&quot;待放款&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;NORMAL&quot;&gt;&lt;approvers&gt;&lt;allocation&gt;ALL&lt;/allocation&gt;&lt;method&gt;ActivityApproverByGroup.group1&lt;/method&gt;&lt;refuse&gt;investigate&lt;/refuse&gt;&lt;/approvers&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;44b8b6670b56d17d_待放款_co0&quot;&nbsp;code=&quot;pass&quot;&nbsp;name=&quot;通过&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;44b8b6670b56d17d_待放款_co1&quot;&nbsp;code=&quot;return&quot;&nbsp;name=&quot;退回&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;null&quot;&gt;&lt;/conclusion&gt;&lt;/conclusions&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;44b8b6670b56d17d_调查&lt;/target&gt;&lt;conclusion&gt;44b8b6670b56d17d_待放款_co1&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;/activities&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;44b8b6670b56d17d_待放款_co0&quot;&nbsp;code=&quot;Pass&quot;&nbsp;name=&quot;待放款&quot;&nbsp;/&gt;&lt;/conclusions&gt;&lt;/template&gt;";
        String d = "&lt;?xml&nbsp;version=&quot;1.0&quot;&nbsp;encoding=&quot;utf-8&quot;&nbsp;?&gt;&lt;template&nbsp;id=&quot;0818edc165dbd26a&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;variables&gt;&lt;variable&nbsp;name=&quot;owner&quot;&nbsp;type=&quot;STRING&quot;&nbsp;/&gt;&lt;variable&nbsp;name=&quot;dispatcher&quot;&nbsp;type=&quot;STRING&quot;&nbsp;/&gt;&lt;variable&nbsp;name=&quot;amount&quot;&nbsp;type=&quot;NUMBER/STRING/BOOLEAN&quot;&nbsp;/&gt;&lt;/variables&gt;&lt;activities&gt;&lt;activity&nbsp;id=&quot;0818edc165dbd26a_客户经理提交申请&quot;&nbsp;code=&quot;0&quot;&nbsp;name=&quot;客户经理提交申请&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;START&quot;&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;0818edc165dbd26a_调查&lt;/target&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;activity&nbsp;id=&quot;0818edc165dbd26a_调查&quot;&nbsp;code=&quot;2&quot;&nbsp;name=&quot;调查&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;NORMAL&quot;&gt;&lt;approvers&gt;&lt;allocation&gt;ONE&lt;/allocation&gt;&lt;method&gt;ActivityApproverByOwner&lt;/method&gt;&lt;/approvers&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;0818edc165dbd26a_调查_co0&quot;&nbsp;code=&quot;Submit&quot;&nbsp;name=&quot;提交&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;null&quot;&gt;&lt;/conclusion&gt;&lt;/conclusions&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;0818edc165dbd26a_资料审查&lt;/target&gt;&lt;conclusion&gt;0818edc165dbd26a_调查_co0&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;activity&nbsp;id=&quot;0818edc165dbd26a_资料审查&quot;&nbsp;code=&quot;8&quot;&nbsp;name=&quot;资料审查&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;NORMAL&quot;&gt;&lt;approvers&gt;&lt;allocation&gt;ALL&lt;/allocation&gt;&lt;method&gt;ActivityApproverByGroup.group1&lt;/method&gt;&lt;refuse&gt;investigate&lt;/refuse&gt;&lt;/approvers&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;0818edc165dbd26a_资料审查_co0&quot;&nbsp;code=&quot;pass&quot;&nbsp;name=&quot;通过&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;0818edc165dbd26a_资料审查_co1&quot;&nbsp;code=&quot;return&quot;&nbsp;name=&quot;退回&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;null&quot;&gt;&lt;/conclusion&gt;&lt;/conclusions&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;0818edc165dbd26a_调查&lt;/target&gt;&lt;conclusion&gt;0818edc165dbd26a_资料审查_co1&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;0818edc165dbd26a_待放款&lt;/target&gt;&lt;conclusion&gt;0818edc165dbd26a_资料审查_co0&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;activity&nbsp;id=&quot;0818edc165dbd26a_待放款&quot;&nbsp;code=&quot;512&quot;&nbsp;name=&quot;待放款&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;NORMAL&quot;&gt;&lt;approvers&gt;&lt;allocation&gt;ALL&lt;/allocation&gt;&lt;method&gt;ActivityApproverByGroup.group1&lt;/method&gt;&lt;refuse&gt;investigate&lt;/refuse&gt;&lt;/approvers&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;0818edc165dbd26a_待放款_co0&quot;&nbsp;code=&quot;pass&quot;&nbsp;name=&quot;通过&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;0818edc165dbd26a_待放款_co1&quot;&nbsp;code=&quot;return&quot;&nbsp;name=&quot;退回&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;null&quot;&gt;&lt;/conclusion&gt;&lt;/conclusions&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;0818edc165dbd26a_调查&lt;/target&gt;&lt;conclusion&gt;0818edc165dbd26a_待放款_co1&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;/activities&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;0818edc165dbd26a_待放款_co0&quot;&nbsp;code=&quot;Pass&quot;&nbsp;name=&quot;待放款&quot;&nbsp;/&gt;&lt;/conclusions&gt;&lt;/template&gt;";

        String a = "&lt;?xml&nbsp;version=&quot;1.0&quot;&nbsp;encoding=&quot;utf-8&quot;&nbsp;?&gt;&lt;template&nbsp;id=&quot;0b9029487abea6ba&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;variables&gt;&lt;variable&nbsp;name=&quot;owner&quot;&nbsp;type=&quot;STRING&quot;&nbsp;/&gt;&lt;variable&nbsp;name=&quot;dispatcher&quot;&nbsp;type=&quot;STRING&quot;&nbsp;/&gt;&lt;variable&nbsp;name=&quot;amount&quot;&nbsp;type=&quot;NUMBER/STRING/BOOLEAN&quot;&nbsp;/&gt;&lt;/variables&gt;&lt;activities&gt;&lt;activity&nbsp;id=&quot;0b9029487abea6ba_客户经理提交申请&quot;&nbsp;code=&quot;0&quot;&nbsp;name=&quot;客户经理提交申请&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;START&quot;&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;0b9029487abea6ba_调查&lt;/target&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;activity&nbsp;id=&quot;0b9029487abea6ba_调查&quot;&nbsp;code=&quot;2&quot;&nbsp;name=&quot;调查&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;NORMAL&quot;&gt;&lt;approvers&gt;&lt;allocation&gt;ONE&lt;/allocation&gt;&lt;method&gt;ActivityApproverByOwner&lt;/method&gt;&lt;/approvers&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;0b9029487abea6ba_调查_co0&quot;&nbsp;code=&quot;Submit&quot;&nbsp;name=&quot;提交&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;null&quot;&gt;&lt;/conclusion&gt;&lt;/conclusions&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;0b9029487abea6ba_资料审查&lt;/target&gt;&lt;conclusion&gt;0b9029487abea6ba_调查_co0&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;activity&nbsp;id=&quot;0b9029487abea6ba_资料审查&quot;&nbsp;code=&quot;8&quot;&nbsp;name=&quot;资料审查&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;NORMAL&quot;&gt;&lt;approvers&gt;&lt;allocation&gt;ALL&lt;/allocation&gt;&lt;method&gt;ActivityApproverByGroup.group1&lt;/method&gt;&lt;refuse&gt;investigate&lt;/refuse&gt;&lt;/approvers&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;0b9029487abea6ba_资料审查_co0&quot;&nbsp;code=&quot;pass&quot;&nbsp;name=&quot;通过&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;0b9029487abea6ba_资料审查_co1&quot;&nbsp;code=&quot;return&quot;&nbsp;name=&quot;退回&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;null&quot;&gt;&lt;/conclusion&gt;&lt;/conclusions&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;0b9029487abea6ba_调查&lt;/target&gt;&lt;conclusion&gt;0b9029487abea6ba_资料审查_co1&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;0b9029487abea6ba_待放款&lt;/target&gt;&lt;conclusion&gt;0b9029487abea6ba_资料审查_co0&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;activity&nbsp;id=&quot;0b9029487abea6ba_待放款&quot;&nbsp;code=&quot;512&quot;&nbsp;name=&quot;待放款&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;NORMAL&quot;&gt;&lt;approvers&gt;&lt;allocation&gt;ALL&lt;/allocation&gt;&lt;method&gt;ActivityApproverByGroup.group1&lt;/method&gt;&lt;refuse&gt;investigate&lt;/refuse&gt;&lt;/approvers&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;0b9029487abea6ba_待放款_co0&quot;&nbsp;code=&quot;pass&quot;&nbsp;name=&quot;通过&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;0b9029487abea6ba_待放款_co1&quot;&nbsp;code=&quot;return&quot;&nbsp;name=&quot;退回&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;null&quot;&gt;&lt;/conclusion&gt;&lt;/conclusions&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;0b9029487abea6ba_调查&lt;/target&gt;&lt;conclusion&gt;0b9029487abea6ba_待放款_co1&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;/activities&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;0b9029487abea6ba_待放款_co0&quot;&nbsp;code=&quot;Pass&quot;&nbsp;name=&quot;待放款&quot;&nbsp;/&gt;&lt;/conclusions&gt;&lt;/template&gt;";
        String b = "&lt;?xml&nbsp;version=&quot;1.0&quot;&nbsp;encoding=&quot;utf-8&quot;&nbsp;?&gt;&lt;template&nbsp;id=&quot;1ac3965c1f134dad&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;variables&gt;&lt;variable&nbsp;name=&quot;owner&quot;&nbsp;type=&quot;STRING&quot;&nbsp;/&gt;&lt;variable&nbsp;name=&quot;dispatcher&quot;&nbsp;type=&quot;STRING&quot;&nbsp;/&gt;&lt;variable&nbsp;name=&quot;amount&quot;&nbsp;type=&quot;NUMBER/STRING/BOOLEAN&quot;&nbsp;/&gt;&lt;/variables&gt;&lt;activities&gt;&lt;activity&nbsp;id=&quot;1ac3965c1f134dad_客户经理提交申请&quot;&nbsp;code=&quot;0&quot;&nbsp;name=&quot;客户经理提交申请&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;START&quot;&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;1ac3965c1f134dad_调查&lt;/target&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;activity&nbsp;id=&quot;1ac3965c1f134dad_调查&quot;&nbsp;code=&quot;2&quot;&nbsp;name=&quot;调查&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;NORMAL&quot;&gt;&lt;approvers&gt;&lt;allocation&gt;ONE&lt;/allocation&gt;&lt;method&gt;ActivityApproverByOwner&lt;/method&gt;&lt;/approvers&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;1ac3965c1f134dad_调查_co0&quot;&nbsp;code=&quot;Submit&quot;&nbsp;name=&quot;提交&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;null&quot;&gt;&lt;/conclusion&gt;&lt;/conclusions&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;1ac3965c1f134dad_资料审查&lt;/target&gt;&lt;conclusion&gt;1ac3965c1f134dad_调查_co0&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;activity&nbsp;id=&quot;1ac3965c1f134dad_资料审查&quot;&nbsp;code=&quot;8&quot;&nbsp;name=&quot;资料审查&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;NORMAL&quot;&gt;&lt;approvers&gt;&lt;allocation&gt;ALL&lt;/allocation&gt;&lt;method&gt;ActivityApproverByGroup.group1&lt;/method&gt;&lt;refuse&gt;investigate&lt;/refuse&gt;&lt;/approvers&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;1ac3965c1f134dad_资料审查_co0&quot;&nbsp;code=&quot;pass&quot;&nbsp;name=&quot;通过&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;1ac3965c1f134dad_资料审查_co1&quot;&nbsp;code=&quot;return&quot;&nbsp;name=&quot;退回&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;null&quot;&gt;&lt;/conclusion&gt;&lt;/conclusions&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;1ac3965c1f134dad_调查&lt;/target&gt;&lt;conclusion&gt;1ac3965c1f134dad_资料审查_co1&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;1ac3965c1f134dad_待放款&lt;/target&gt;&lt;conclusion&gt;1ac3965c1f134dad_资料审查_co0&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;activity&nbsp;id=&quot;1ac3965c1f134dad_待放款&quot;&nbsp;code=&quot;512&quot;&nbsp;name=&quot;待放款&quot;&nbsp;nodetype=&quot;0&quot;&nbsp;&nbsp;type=&quot;NORMAL&quot;&gt;&lt;approvers&gt;&lt;allocation&gt;ALL&lt;/allocation&gt;&lt;method&gt;ActivityApproverByGroup.group1&lt;/method&gt;&lt;refuse&gt;investigate&lt;/refuse&gt;&lt;/approvers&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;1ac3965c1f134dad_待放款_co0&quot;&nbsp;code=&quot;pass&quot;&nbsp;name=&quot;通过&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;1ac3965c1f134dad_待放款_co1&quot;&nbsp;code=&quot;return&quot;&nbsp;name=&quot;退回&quot;&gt;&lt;condition&gt;ONE&lt;/condition&gt;&lt;/conclusion&gt;&lt;conclusion&nbsp;id=&quot;null&quot;&gt;&lt;/conclusion&gt;&lt;/conclusions&gt;&lt;lines&gt;&lt;line&nbsp;id=&quot;&quot;&nbsp;code=&quot;&quot;&nbsp;name=&quot;&quot;&gt;&lt;target&gt;1ac3965c1f134dad_调查&lt;/target&gt;&lt;conclusion&gt;1ac3965c1f134dad_待放款_co1&lt;/conclusion&gt;&lt;/line&gt;&lt;line&nbsp;id=&quot;null&quot;&gt;&lt;/line&gt;&lt;/lines&gt;&lt;/activity&gt;&lt;/activities&gt;&lt;conclusions&gt;&lt;conclusion&nbsp;id=&quot;1ac3965c1f134dad_待放款_co0&quot;&nbsp;code=&quot;Pass&quot;&nbsp;name=&quot;待放款&quot;&nbsp;/&gt;&lt;/conclusions&gt;&lt;/template&gt;";
        System.out.println(StringEscapeUtils.unescapeHtml4(b));
        System.out.println(StringEscapeUtils.unescapeHtml4(a));
        System.out.println(StringEscapeUtils.unescapeHtml4(c));
        System.out.println(StringEscapeUtils.unescapeHtml4(d));
    }

}
