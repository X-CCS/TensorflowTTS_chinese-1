package net.tatans.tensorflowtts.utils;

import net.tatans.tensorflowtts.utils.Base;

public class Rules { 

	public static String deal_baifenbi(String raw) {
	     

	    
	    String out = "百分之";
	    boolean hasDot = false;

	    
	        String num = "";
	        int i;
	        for (i = 0; i < raw.length(); i++) {

	            if (raw.charAt(i) == '.') {
	                hasDot = true;

	            }
	            else if ((raw.charAt(i) < '0' || raw.charAt(i)>'9')) {
	                break;
	            }
	            num += raw.charAt(i);

	        }
	       
	            
	            if (hasDot) {
	                out += deal_xiaoshu(num);
	            }
	            else {
	                out += Base.numtoshi(Integer.valueOf(num));
	            }
	        
	       
	        return out;
	    
	  
	}


	public static String deal_xiaoshu(String raw) {

	    int res = raw.indexOf(".");
	    int zheng = Integer.valueOf((raw.substring(0, res)));
	    String xiao = raw.substring(res + 1, raw.length() );

	    String out = "";
	    out += Base.numtoshi(zheng);
	    out += "点";

	    out += Base.numtohan(xiao);

	    return out;
	}

	public static String deal_shudotshuying(String raw) {

	    int res = raw.indexOf(".");
	    int zheng =Integer.valueOf((raw.substring(0, res)));
	    int index=0;
	    for (int i = raw.length() - 1; i >= 0; i--) {
	        if (raw.charAt(i) >= '0' && raw.charAt(i) <= '9') {
	            index = i;
	            break;
	        }
	    }

	    String xiao = raw.substring(res + 1, raw.length() - index + res );
	    String ying= raw.substring(index + 1);

	    String out = "";
	    out += Base.numtoshi(zheng);
	    out += "点";

	    out += Base.numtohan(xiao);
	    if (ying .equals( "cm")) {
	        out += "厘米";
	    }
	    else {
	        out += ying;
	    }
	    

	    return out;
	}

	public static String deal_shuzi(String raw) {
	     
	    if (raw .equals( "110")) {
	        return "幺幺零";
	    }else if (raw .equals( "211") ){
	        return "二幺幺";
	    }
	    else if (raw .equals( "911")) {
	        return "九幺幺";
	    }

	    String out = "";
	    

	    out += Base.numtohan(raw);

	    return out;
	}


	public static String deal_yingshu(String raw) {
	    int index=0;
	    for (int i = 0; i < raw.length(); i++) {
	        if (raw.charAt(i) >= '0' && raw.charAt(i) <= '9') {
	            index = i;
	            break;
	        }
	    }
	    String ying = raw.substring(0, index);
	    String shu = raw.substring(index, raw.length());

	    String out = "";
	    out += ying;
	    

	    out += Base.numtohan(shu);

	    return out;
	}

	public static String deal_shuying(String raw) {
	     int index=0;
	    for (int i = 0; i < raw.length(); i++) {
	        if (raw.charAt(i) < '0' || raw.charAt(i) > '9') {
	            index = i;
	            break;
	        }
	    }
	    String out = "";
	    String shu = raw.substring(0, index);
	    out += Base.numtohan(shu);
	    String ying = raw.substring(index, raw.length() );
	    out += ying;
	        return out;
	    
	    
	}

	public static String deal_shuyingshu(String raw) {
	    int index=0;
	    for (int i = 0; i < raw.length(); i++) {
	        if (raw.charAt(i) < '0' || raw.charAt(i) > '9') {
	            index = i;
	            break;
	        }
	    }
	    String shu = raw.substring(0, index);
	    String yingshu = raw.substring(index, raw.length() );

	    String out = "";
	    out += Base.numtohan(shu);
	    out += deal_yingshu(yingshu);

	    return out;
	}

	public static String deal_yingshuying(String raw) {
	    int index=0;
	    for (int i = raw.length() - 1; i >= 0; i--) {
	        if (raw.charAt(i) >= '0' && raw.charAt(i) <= '9') {
	            index = i;
	            break;
	        }
	    }
	    String yingshu = raw.substring(0, index+1);
	    String ying = raw.substring(index+1);

	    String out = "";
	    out += deal_yingshu(yingshu);
	    out += ying;

	    return out;
	}

	public static String deal_qujian(String raw) {
	    int res= raw.indexOf("-");
	    int zheng = Integer.valueOf(raw.substring(0, res));
	    int xiao = Integer.valueOf(raw.substring( res+1));

	    String out = "";
	    out += Base.numtoshi(zheng);
	    out += "到";

	    out += Base.numtoshi(xiao);

	    return out;
	}


	public static String deal_meiyuan(String raw) {
	    
	    int zheng = Integer.valueOf(raw.substring(0, raw.length()-1));
	    

	    String out = "";
	    out += Base.numtoshi(zheng);
	    out += "美元";

	    return out;
	}


	public static String deal_fenshu(String raw) {

	    int res = raw.indexOf("/");
	    int zheng = Integer.valueOf(raw.substring(0, res));
	    int mu =Integer.valueOf(raw.substring(res+1)); 

	    String out = "";
	    out += Base.numtoshi(mu);
	    out += "分之";
	    out += Base.numtoshi(zheng);
	   
	    return out;
	}


	public static String deal_tianshu(String raw,String teshu) {
	    String tmp = raw;
	    int index = tmp.indexOf(teshu);
	    String out = "";
	    
	    while (index != -1) {
	        String num = "";
	        int i;
	        for (i = index - 1; i >= 0; i--) {
	            if (tmp.charAt(i) < '0' || tmp.charAt(i)>'9') {
	                break;
	            }
	            else {
	                num = tmp.charAt(i)+ num;
	            }
	            
	        }
	        if (num != "") {
	            out = tmp.substring(0, i + 1);
	            out += Base.numtoshi(Integer.valueOf(num));
	            int tt = index;
	            index = out.length() + teshu.length(); 
	            out += tmp.substring(tt );
	        }
	        else {
	            return tmp;
	        }
	        tmp = out;
	        index = tmp.indexOf(teshu, index);
	    }
	    return tmp;
	}


	public static String deal_liang( String raw, String teshu,String huan) {
	    String tmp = raw;
	    String out = "";
	    int index = tmp.indexOf(teshu);
	    while (index!=-1) {
	        boolean hasDot = false;

	       
	        String num = "";
	        int i;
	        for (i = index - 1; i >= 0; i--) {

	            if (tmp.charAt(i) == '.') {
	                hasDot = true;

	            }
	            else if ((tmp.charAt(i)< '0' || tmp.charAt(i)>'9')) {
	                break;
	            }
	            num = tmp.charAt(i) + num;

	        }
	        out = tmp.substring(0, i + 1);
	        if (num != "") {
	            
	            if (hasDot) {
	                out += deal_xiaoshu(num);
	            }
	            else {
	                String nn = Base.numtoshi(Integer.valueOf(num));
	                if ((teshu.equals("块") || teshu .equals( "元" )|| teshu .equals( "kg" )|| teshu .equals( "g") || teshu .equals( "°")
	                    ||teshu .equals( "个") || teshu .equals( "米") || teshu .equals( "所") || teshu .equals( "岁") || teshu .equals( "种")
	                    || teshu .equals( "只") || teshu .equals( "头") || teshu .equals( "天") || teshu .equals( "台") || teshu .equals( "万")) && nn .equals( "二")) {
	                    out += "两";
	                }
	                else {
	                    out += nn;
	                }

	            }
	            
	           
	        }
	        out += huan;
	        int tt = index;
	        index = out.length();
	        out += tmp.substring(tt + teshu.length());

	        tmp = out;
	        index= tmp.indexOf(teshu, index);
	        
	        
	    }
	    return tmp;
	    
	    
	}

	public static String deal_riqi(String raw,String fuhao) {

	    int res = raw.indexOf(fuhao);
	    String nian = raw.substring(0, res);
	    int res2= raw.indexOf(fuhao, res+1);
	    int yue = Integer.valueOf(raw.substring(res+1, res2));
	    int ri = Integer.valueOf(raw.substring(res2 + 1));

	    String out = "";
	    out += Base.numtohan(nian);
	    out += "年";
	    out += Base.numtoshi(yue);
	    out += "月";
	    out += Base.numtoshi(ri);
	    out += "日";

	    return out;
	}

	public static String deal_shijian(String raw) {

	    int res = raw.indexOf(":");
	    int fen = Integer.valueOf(raw.substring(0, res));
	    int miao = Integer.valueOf(raw.substring(res+1));  

	    String out = "";
	    out += Base.numtoshi(fen);
	    out += "点";

	    out += Base.numtoshi(miao);
	    out += "分";

	    return out;
	}


	public static String deal_daxiaoyu(String raw, String shizi) {
	    String out = "";
	    String huan = "";
	    String fuhao = "";
	    int deng = raw.indexOf("=", 0);
	    int xiao= raw.indexOf("<", 0);
	    int da = raw.indexOf(">", 0);
	    if (deng == -1) {
	        if (xiao == -1) {
	            fuhao = ">";
	            huan = "大于";
	        }
	        else {
	            fuhao = "<";
	            huan = "小于";
	        }
	    }
	    else {
	        if (xiao == -1) {
	            fuhao = ">=";
	            huan = "大于等于";
	        }
	        else {
	            fuhao = "<=";
	            huan = "小于等于";
	        }
	    }
	    
	    int res = raw.indexOf(fuhao, 0);
	    out += deal_liang(raw.substring(0, res + fuhao.length()), fuhao, huan);
	    out += deal_liang(raw.substring( res + fuhao.length())+"#", "#", "");
	    /*int zheng = atoi((char*)raw.substr(0, res).data());
	    int xiao = atoi((char*)raw.substr(res + fuhao.size()).data());

	   
	    out += Base.numtoshi(zheng);
	    out += huan;

	    out += Base.numtoshi(xiao);*/

	    return out;
	}

	public static String deal_shizi(String raw,String fuhao) {

	    String huan="";
	    if (fuhao.equals( "+") ){
	        huan = "加";
	    }
	    else if (fuhao .equals( "-")) {
	        huan = "减";
	    }
	    else if (fuhao .equals( "*")) {
	        huan = "乘以";
	    }
	    else if (fuhao .equals( "/") ){
	        huan = "除以";
	    }
	    String out = "";
	    int res = raw.indexOf(fuhao, 0);
	    out+=deal_liang(raw.substring(0, res + 1), fuhao, huan);
	    int res2 = raw.indexOf("=", res + 1);
	    out+=deal_liang(raw.substring(res+1, res2+1), "=", "等于");
	    out+=deal_liang(raw.substring(res2 + 1)+"#", "#", "");
	    return out;
	  
	}
}
