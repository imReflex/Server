package org.endeavor.engine.utility;

/**
 * Created with Eclipse. Date: 12/08/2013 Time: 14:57:42
 * 
 * @author Sebastian <juan.2114@hotmail.com>
 * @see java.lang.Object
 */
public class ArrayToEnum {

	public static int TZ_KIH = 2627, TZ_KEK_SPAWN = 2738, TZ_KEK = 2630, TOK_XIL = 2631, YT_MEJKOT = 2741,
			KET_ZEK = 2743, TZTOK_JAD = 2745;

	private static String[] WAVES = new String[] { "{TZ_KIH}", "{TZ_KIH,TZ_KIH}", "{TZ_KEK}", "{TZ_KEK,TZ_KIH}",
			"{TZ_KEK,TZ_KIH,TZ_KIH}", "{TZ_KEK,TZ_KEK}", "{TOK_XIL}", "{TOK_XIL,TZ_KIH}", "{TOK_XIL,TZ_KIH,TZ_KIH}",
			"{TOK_XIL,TZ_KEK}", "{TOK_XIL,TZ_KEK,TZ_KIH}", "{TOK_XIL,TZ_KEK,TZ_KIH,TZ_KIH}", "{TOK_XIL,TZ_KEK,TZ_KEK}",
			"{TOK_XIL,TOK_XIL}", "{YT_MEJKOT}", "{YT_MEJKOT,TZ_KIH}", "{YT_MEJKOT,TZ_KIH,TZ_KIH}",
			"{YT_MEJKOT,TZ_KEK}", "{YT_MEJKOT,TZ_KEK,TZ_KIH}", "{YT_MEJKOT,TZ_KEK,TZ_KIH,TZ_KIH}",
			"{YT_MEJKOT,TZ_KEK,TZ_KEK}", "{YT_MEJKOT,TOK_XIL}", "{YT_MEJKOT,TOK_XIL,TZ_KIH}",
			"{YT_MEJKOT,TOK_XIL,TZ_KIH,TZ_KIH}", "{YT_MEJKOT,TOK_XIL,TZ_KEK}", "{YT_MEJKOT,TOK_XIL,TZ_KEK,TZ_KIH}",
			"{YT_MEJKOT,TOK_XIL,TZ_KEK,TZ_KIH,TZ_KIH}", "{YT_MEJKOT,TOK_XIL,TZ_KEK,TZ_KEK}",
			"{YT_MEJKOT,TOK_XIL,TOK_XIL}", "{YT_MEJKOT,YT_MEJKOT}", "{KET_ZEK}", "{KET_ZEK,TZ_KIH}",
			"{KET_ZEK,TZ_KIH,TZ_KIH}", "{KET_ZEK,TZ_KEK}", "{KET_ZEK,TZ_KEK,TZ_KIH}", "{KET_ZEK,TZ_KEK,TZ_KIH,TZ_KIH}",
			"{KET_ZEK,TZ_KEK,TZ_KEK}", "{KET_ZEK,TOK_XIL}", "{KET_ZEK,TOK_XIL,TZ_KIH}",
			"{KET_ZEK,TOK_XIL,TZ_KIH,TZ_KIH}", "{KET_ZEK,TOK_XIL,TZ_KEK}", "{KET_ZEK,TOK_XIL,TZ_KEK,TZ_KIH}",
			"{KET_ZEK,TOK_XIL,TZ_KEK,TZ_KIH,TZ_KIH}", "{KET_ZEK,TOK_XIL,TZ_KEK,TZ_KEK}", "{KET_ZEK,TOK_XIL,TOK_XIL}",
			"{KET_ZEK,YT_MEJKOT}", "{KET_ZEK,YT_MEJKOT,TZ_KIH}", "{KET_ZEK,YT_MEJKOT,TZ_KIH,TZ_KIH}",
			"{KET_ZEK,YT_MEJKOT,TZ_KEK}", "{KET_ZEK,YT_MEJKOT,TZ_KEK,TZ_KIH}",
			"{KET_ZEK,YT_MEJKOT,TZ_KEK,TZ_KIH,TZ_KIH}", "{KET_ZEK,YT_MEJKOT,TZ_KEK,TZ_KEK}",
			"{KET_ZEK,YT_MEJKOT,TOK_XIL}", "{KET_ZEK,YT_MEJKOT,TOK_XIL,TZ_KIH}",
			"{KET_ZEK,YT_MEJKOT,TOK_XIL,TZ_KIH,TZ_KIH}", "{KET_ZEK,YT_MEJKOT,TOK_XIL,TZ_KEK}",
			"{KET_ZEK,YT_MEJKOT,TOK_XIL,TZ_KEK,TZ_KIH}", "{KET_ZEK,YT_MEJKOT,TOK_XIL,TZ_KEK,TZ_KIH,TZ_KIH}",
			"{KET_ZEK,YT_MEJKOT,TOK_XIL,TZ_KEK,TZ_KEK}", "{KET_ZEK,YT_MEJKOT,TOK_XIL,TOK_XIL}",
			"{KET_ZEK,YT_MEJKOT,YT_MEJKOT}", "{KET_ZEK,KET_ZEK}", "{TZTOK_JAD}" };

	public static void main(String args[]) {
		String text = null;
		for (int i = 0; i < WAVES.length; i++) {
			text = WAVES[i];
			int indexOf = text.toString().indexOf('{') + 1;
			int end = text.toString().indexOf('}');
			text = text.toString().substring(indexOf, end);
			String textt[] = text.toString().split(",");
			String result = "WAVE_" + i + "(new short[] { ";
			byte count = 0;
			for (String s : textt) {
				count++;
				result += "NPCS_DETAILS." + s + (count == textt.length ? "}" : ", ");
			}
			System.out.println(result + "),");
		}
	}
}
