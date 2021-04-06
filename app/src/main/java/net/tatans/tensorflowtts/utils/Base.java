package net.tatans.tensorflowtts.utils;

import java.util.ArrayList;

public class Base {


	public static String func(int a) {  
	    int yuan = a;
	    if (a == 0) {
	        return "零";
	    }
	    int flag = 0, tmp;
	    String strtmp="";
	    String result="";
	    int atemp = a;     //设定a的临时存储值，防止每次进入循环都进行末尾零的判断
	    while (a != 0)
	    {

	        while (atemp % 10 == 0)
	        {
	            flag++;
	            atemp /= 10;
	            a /= 10;
	        }

	        tmp = a % 10;
	        if (tmp != 0) { 
	            switch (tmp)
	            {
	            case 0:
	                strtmp = "零";
	                break;
	            case 1:
	                strtmp = "一";
	                break;
	            case 2:
	                strtmp = "二";
	                break;
	            case 3:
	                strtmp = "三";
	                break;
	            case 4:
	                strtmp = "四";
	                break;
	            case 5:
	                strtmp = "五";
	                break;
	            case 6:
	                strtmp = "六";
	                break;
	            case 7:
	                strtmp = "七";
	                break;
	            case 8:
	                strtmp = "八";
	                break;
	            case 9:
	                strtmp = "九";
	                break;
	                
	            }

	            switch (flag)
	            {
	            case 1:
	                strtmp += "十";
	                break;
	            case 2:
	                strtmp += "百";
	                break;
	            case 3:
	                strtmp += "千";
	                break;
	            case 4:
	                strtmp += "万";
	                break;
	            

	            }

	        }
	            
	        else if (tmp == 0) {
	            strtmp = "零";
	        }
	        if (yuan >= 10 && yuan <= 19&&strtmp.equals("一十")) {
	            result = "十" + result;
	        }
	        else {
	            result = strtmp + result;
	        }
	        
	        a /= 10;
	        flag++;
	    }
	    
	    return result;
	}
	public static String numtoshi(int num) {


	    String result, temp;
	    if (num < 100000)
	        result = func(num);
	    else
	    {
	        temp = func(num / 10000);
	        result = temp + "万" + func(num - num / 10000 * 10000);
	    }
	    return result;

	}
	public static String numtohan(String num) { 
	    String res = "";
	    for (int i = 0; i < num.length(); i++) {
	         
	        switch (num.charAt(i))
	        {
	        case '0':
	            res += "零";
	            break;
	        case '1':
	            res += "一";
	            break;
	        case '2':
	            res += "二";
	            break;
	        case '3':
	            res += "三";
	            break;
	        case '4':
	            res += "四";
	            break;
	        case '5':
	            res += "五";
	            break;
	        case '6':
	            res += "六";
	            break;
	        case '7':
	            res += "七";
	            break;
	        case '8':
	            res += "八";
	            break;
	        case '9':
	            res += "九";
	            break;

	        }
	    }
	    return res;
	}



	public static ArrayList<String> biaozhun(String chars) {
	    ArrayList<String> words=new ArrayList<String>();
	    String input=chars;
	    int len = input.length();
	    int i = 0;

	    while (i < len) {
//	        assert((input.charAt(i) & 0xF8) <= 0xF0);
	        int next = 1;
//	        if ((input.charAt(i)  & 0x80) == 0x00) {
//	           
//	        }
//	        else if ((input.charAt(i)  & 0xE0) == 0xC0) {
//	            next = 2;
//	           
//	        }
//	        else if ((input.charAt(i)  & 0xF0) == 0xE0) {
//	            next = 3;
//	            
//	        }
//	        else if ((input.charAt(i)  & 0xF8) == 0xF0) {
//	            next = 4;
//	            
//	        }
	        words.add(input.substring(i, i+next));
	        i += next;
	    }
	    return words;
	}
}
