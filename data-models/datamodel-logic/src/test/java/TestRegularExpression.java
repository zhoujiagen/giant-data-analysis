import java.util.regex.Pattern;

/**
 * REF: https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html REF:
 * https://stackoverflow.com/questions/406230/regular-expression-to-match-a-line-that-doesnt-contain-a-word
 */
public class TestRegularExpression {
  public static void main(String[] args) {
    // fragment BQUOTA_STRING: '`' ( '\\'. | '``' | ~('`'|'\\'))* '`';
    // ^((?!hede).)*$
    String regex = "ARMSCII8|ASCII|BIG5|BINARY|CP1250" //
        + "|CP1251|CP1256|CP1257|CP850" //
        + "|CP852|CP866|CP932|DEC8|EUCJPMS" //
        + "|EUCKR|GB2312|GBK|GEOSTD8|GREEK" //
        + "|HEBREW|HP8|KEYBCS2|KOI8R|KOI8U" //
        + "|LATIN1|LATIN2|LATIN5|LATIN7" //
        + "|MACCE|MACROMAN|SJIS|SWE7|TIS620" //
        + "|UCS2|UJIS|UTF16|UTF16LE|UTF32" //
        + "|UTF8|UTF8MB3|UTF8MB4";
    String input = "UTF8MB4";

    System.out.println(regex);
    System.out.println(input);
    System.out.println(Pattern.matches(regex, input));
  }
}
