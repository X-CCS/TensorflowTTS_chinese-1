package tn;

import java.util.ArrayList;
import tn.Rules;
import tn.Base;

public class Zhuan {

	public static ArrayList<String> qie = new ArrayList<String>();
	public static ArrayList<String> has_pipei = new ArrayList<String>();
	public static String source = "";
	public static String longs = "";

	public static ArrayList<String> bz = new ArrayList<String>();

	public static String yuchuli(String ss) {

		String out = Rules.deal_tianshu(ss, "号");
		out = Rules.deal_tianshu(out, "日");
		out = Rules.deal_liang(out, "块", "块");
		out = Rules.deal_liang(out, "元", "元");
		out = Rules.deal_liang(out, "kg", "千克");
		out = Rules.deal_liang(out, "g", "克");
		out = Rules.deal_liang(out, "°", "度");
		out = Rules.deal_liang(out, "个", "个");
		out = Rules.deal_liang(out, "月", "月");
		out = Rules.deal_liang(out, "米", "米");
		out = Rules.deal_liang(out, "所", "所");
		out = Rules.deal_liang(out, "岁", "岁");
		out = Rules.deal_liang(out, "种", "种");
		out = Rules.deal_liang(out, "只", "只");
		out = Rules.deal_liang(out, "头", "头");
		out = Rules.deal_liang(out, "天", "天");
		out = Rules.deal_liang(out, "台", "台");
		out = Rules.deal_liang(out, "两", "两");
		out = Rules.deal_liang(out, "万", "万");
		out = Rules.deal_liang(out, "￥", "人民币");

		return out;
	}

	public static void toXu() {

		String tt = "";
		for (int i = 0; i < bz.size(); i++) {
			char ch = bz.get(i).charAt(0);
			if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {

				tt += "Y";

			} else if (ch >= '0' && ch <= '9') {

				tt += "S";

			} else if (ch == '.' || ch == '%' || ch == '-' || ch == '$' || ch == '/' || ch == ':' || ch == '<'
					|| ch == '>' || ch == '+' || ch == '=' || ch == '*') {

				tt += ch;

			} else if (ch == '–') {
				tt += "-";
				ch = '-';
			}
			/*
			 * else if (bz[i] == ","|| bz[i] == "!" || bz[i] == "?" || bz[i] == ":" || bz[i]
			 * == "，" || bz[i] == "！" || bz[i] == "？" || bz[i] == "："|| bz[i] == " "|| bz[i]
			 * == "(" || bz[i] == ")" || bz[i] == "（" || bz[i] == "）" ||bz[i]=="。") {
			 * 
			 * tt += "#";
			 * 
			 * }
			 */
			else {

				tt += "H";

			}
		}

		char n = '0';
		for (int i = 1; i < tt.length(); i++) {
			if (tt.charAt(i - 1) == tt.charAt(i)) {
				n += 1;
			} else {
				source += tt.charAt(i - 1);
				longs += (char)(n + 1);
				n = '0';
			}
		}
		source += tt.charAt(tt.length() - 1);
		longs += (char)(n + 1);
	}

	public static String getSub(int end, String rule) {
		int start = end - rule.length();
		int i;
		int sum = 0;
		for (i = 0; i < start; i++) {

			sum += longs.charAt(i) - '0';

		}
		int len = 0;
		for (int j = start; j < start + rule.length(); j++) {
			len += longs.charAt(j) - '0';
		}
		String out = "";
		for (i = sum; i < sum + len; i++) {
			out += bz.get(i);
		}
		return out;
	}

	public static void pipei() {

		/*
		 * const char* d = "H#"; char* p; String bak = source; p =
		 * strtok((char*)bak.data(), d); while (p) { String tmp = ""; tmp += p;
		 * qie.add(tmp); p = strtok(NULL, d); }
		 */
		String feihan = "";
		for (int j = 0; j <= source.length(); j++) {

			if (j == source.length() || source.charAt(j) == 'H') {

				if (feihan .equals( "S.S%") ){
					String sub = getSub(j, "S.S%");
					has_pipei.add(Rules.deal_baifenbi(sub));
				} else if (feihan .equals( "S%") ){
					String sub = getSub(j, "S%");
					has_pipei.add(Rules.deal_baifenbi(sub));
				} else if (feihan .equals( "S.S") ){
					String sub = getSub(j, "S.S");
					has_pipei.add(Rules.deal_xiaoshu(sub));
				} else if (feihan .equals( "S:S") ){
					String sub = getSub(j, "S:S");
					has_pipei.add(Rules.deal_shijian(sub));
				} else if (feihan .equals( "S.SY")) {
					String sub = getSub(j, "S.SY");
					has_pipei.add(Rules.deal_shudotshuying(sub));
				} else if (feihan .equals( "S-S") ){
					String sub = getSub(j, "S-S");
					has_pipei.add(Rules.deal_qujian(sub));
				} else if (feihan .equals( "S<S" )|| feihan .equals( "S>S" )|| feihan .equals( "S.S<S") || feihan .equals( "S.S>S")
						|| feihan .equals( "S<S.S") || feihan .equals( "S>S.S") || feihan .equals( "S.S<S.S" )|| feihan .equals( "S.S>S.S")
						|| feihan .equals( "S<=S") || feihan .equals( "S>=S" )|| feihan .equals( "S.S<=S" )|| feihan .equals( "S.S>=S")
						|| feihan .equals( "S<=S.S" )|| feihan .equals( "S>=S.S" )|| feihan .equals( "S.S<=S.S" )|| feihan .equals( "S.S>=S.S")) {
					String sub = getSub(j, feihan);
					has_pipei.add(Rules.deal_daxiaoyu(sub, feihan));
				} else if (feihan .equals( "S+S=S") || feihan .equals( "S-S=S") || feihan .equals( "S*S=S" )|| feihan .equals( "S/S=S")
						|| feihan .equals( "S+S.S=S") || feihan .equals( "S-S.S=S") || feihan .equals( "S*S.S=S") || feihan .equals( "S/S.S=S")
						|| feihan .equals( "S+S=S.S") || feihan .equals( "S-S=S.S") || feihan .equals( "S*S=S.S") || feihan .equals( "S/S=S.S")
						|| feihan .equals( "S+S.S=S.S" )|| feihan .equals( "S-S.S=S.S") || feihan .equals( "S*S.S=S.S")
						|| feihan .equals( "S/S.S=S.S")) {
					String sub = getSub(j, feihan);
					has_pipei.add(Rules.deal_shizi(sub, feihan.substring(1, 2)));
				} else if (feihan .equals( "S.S+S=S") || feihan .equals( "S.S-S=S") || feihan .equals( "S.S*S=S") || feihan .equals( "S.S/S=S")
						|| feihan .equals( "S.S+S.S=S") || feihan .equals( "S.S-S.S=S") || feihan .equals( "S.S*S.S=S")
						|| feihan .equals( "S.S/S.S=S") || feihan .equals( "S.S+S=S.S") || feihan .equals( "S.S-S=S.S")
						|| feihan .equals( "S.S*S=S.S") || feihan .equals( "S.S/S=S.S") || feihan .equals( "S.S+S.S=S.S")
						|| feihan .equals( "S.S-S.S=S.S") || feihan .equals( "S.S*S.S=S.S") || feihan .equals( "S.S/S.S=S.S")) {
					String sub = getSub(j, feihan);
					has_pipei.add(Rules.deal_shizi(sub, feihan.substring(3, 4)));
				} else if (feihan.equals("S")) {
					String sub = getSub(j, "S");
					has_pipei.add(Rules.deal_shuzi(sub));
				} else if (feihan.equals("S$")) {
					String sub = getSub(j, "S$");
					has_pipei.add(Rules.deal_meiyuan(sub));
				} else if (feihan.equals("S/S")) {
					String sub = getSub(j, "S/S");
					has_pipei.add(Rules.deal_fenshu(sub));
				} else if (feihan.equals("S/S/S") || feihan.equals("S-S-S")) {
					String sub = getSub(j, feihan);
					has_pipei.add(Rules.deal_riqi(sub, feihan.substring(1, 2)));
				} else if (feihan.equals("YS")) {
					String sub = getSub(j, "YS");
					has_pipei.add(Rules.deal_yingshu(sub));
				} else if (feihan.equals("SY")) {
					String sub = getSub(j, "SY");
					has_pipei.add(Rules.deal_shuying(sub));
				} else if (feihan .equals( "YSY")) {
					String sub = getSub(j, "YSY");
					has_pipei.add(Rules.deal_yingshuying(sub));
				} else if (feihan .equals( "Y") ){
					String sub = getSub(j, "Y");
					has_pipei.add(sub);
				} else {
					has_pipei.add("");
				}
				qie.add(feihan);
				feihan = "";
			} else {
				feihan += source.charAt(j);
			}

		}

	}

	public static String zuzhuang(String ss) {
		String yuchu = yuchuli(ss);

		bz = Base.biaozhun(yuchu);
		toXu();
		pipei();
		String out = "";
		int index = 0;
		int has_count = 0;
		int i;
		for (i = 0; i < source.length(); i++) {

			if (source.charAt(i) == 'H') {
				for (int j = index; j < index + longs.charAt(i) - '0'; j++) {
					out += bz.get(j);
				}
				index += longs.charAt(i) - '0';
			}
			/*
			 * else if (source[i] == '#') { out += "#"; index += longs[i] - '0'; }
			 */
			else {
				out += has_pipei.get(has_count);

				for (int j = i; j < i + qie.get(has_count).length(); j++) {
					index += longs.charAt(j) - '0';
				}
				i += qie.get(has_count).length() - 1;
				has_count += 1;
			}

		}
		has_pipei.clear();
		qie.clear();
		bz.clear();
		source = "";
		longs = "";

		return out;
	}

}
