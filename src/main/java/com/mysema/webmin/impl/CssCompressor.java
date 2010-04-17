package com.mysema.webmin.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CssCompressor provides
 *
 * @author tiwe
 * @version $Id$
 *
 */
public class CssCompressor {

    private static final Pattern PATTERN1 = Pattern.compile("(^|\\})(([^\\{:])+:)+([^\\{]*\\{)");

    private static final Pattern PATTERN10 = Pattern.compile("background-position:0;");

    private static final Pattern PATTERN11 = Pattern.compile("(:|\\s)0+\\.(\\d+)");

    private static final Pattern PATTERN12 = Pattern.compile("([^\"'=\\s])(\\s*)#([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");

    private static final Pattern PATTERN13 = Pattern.compile("[^\\}]+\\{;\\}");

    private static final Pattern PATTERN2 = Pattern.compile("\\s+([!{};:>+\\(\\)\\],])");

    private static final Pattern PATTERN3 = Pattern.compile("___PSEUDOCLASSCOLON___");

    private static final Pattern PATTERN4 = Pattern.compile("([!{}:;>+\\(\\[,])\\s+");

    private static final Pattern PATTERN5 = Pattern.compile("([^;\\}])}");

    private static final Pattern PATTERN6 = Pattern.compile("([\\s:])(0)(px|em|%|in|cm|mm|pc|pt|ex)");

    private static final Pattern PATTERN7 = Pattern.compile(":0 0 0 0;");

    private static final Pattern PATTERN8 = Pattern.compile(":0 0 0;");

    private static final Pattern PATTERN9 = Pattern.compile(":0 0;");

    private static final Pattern RGB_PATTERN = Pattern.compile("rgb\\s*\\(\\s*([0-9,\\s]+)\\s*\\)");

    private static final Pattern WHITE_SPACE = Pattern.compile("\\s+");
    
    private StringBuffer srcsb = new StringBuffer();

    public CssCompressor(Reader in) throws IOException {
        // Read the stream...
        int c;
        while ((c = in.read()) != -1) {
            srcsb.append((char) c);
        }
    }

    public void compress(Writer out, int linebreakpos)
            throws IOException {

        Matcher m;
        String css;
        StringBuffer sb;
        int startIndex, endIndex;

        // Remove all comment blocks...
        sb = new StringBuffer(srcsb.toString());
        while ((startIndex = sb.indexOf("/*")) >= 0) {
            endIndex = sb.indexOf("*/", startIndex + 2);
            if (endIndex >= startIndex + 2){
                sb.delete(startIndex, endIndex + 2);
            }                
        }

        css = sb.toString();

        // Normalize all whitespace strings to single spaces. Easier to work with that way.
        css = WHITE_SPACE.matcher(css).replaceAll(" ");

        // Remove the spaces before the things that should not have spaces before them.
        // But, be careful not to turn "p :link {...}" into "p:link{...}"
        // Swap out any pseudo-class colons with the token, and then swap back.
        sb = new StringBuffer();
        m = PATTERN1.matcher(css);
        while (m.find()) {
            String s = m.group();
            s = s.replaceAll(":", "___PSEUDOCLASSCOLON___");
            m.appendReplacement(sb, s);
        }
        m.appendTail(sb);
        css = sb.toString();
        css = PATTERN2.matcher(css).replaceAll("$1");
        css = PATTERN3.matcher(css).replaceAll(":");

        // Remove the spaces after the things that should not have spaces after them.
        css = PATTERN4.matcher(css).replaceAll("$1");
        
        // Add the semicolon where it's missing.
        css = PATTERN5.matcher(css).replaceAll("$1;}");

        // Replace 0(px,em,%) with 0.
        css = PATTERN6.matcher(css).replaceAll("$1$2");

        // Replace 0 0 0 0; with 0.
        css = PATTERN7.matcher(css).replaceAll(":0;");
        css = PATTERN8.matcher(css).replaceAll(":0;");
        css = PATTERN9.matcher(css).replaceAll(":0;");
        // Replace background-position:0; with background-position:0 0;
        css = PATTERN10.matcher(css).replaceAll("background-position:0 0;");
        
        // Replace 0.6 to .6, but only when preceded by : or a white-space
        css = PATTERN11.matcher(css).replaceAll("$1.$2");

        // Shorten colors from rgb(51,102,153) to #336699
        // This makes it more likely that it'll get further compressed in the next step.
        m = RGB_PATTERN.matcher(css);
        sb = new StringBuffer();
        while (m.find()) {
            String[] rgbcolors = m.group(1).split(",");
            StringBuffer hexcolor = new StringBuffer("#");
            for (int i = 0; i < rgbcolors.length; i++) {
                int val = Integer.parseInt(rgbcolors[i]);
                if (val < 16) {
                    hexcolor.append("0");
                }
                hexcolor.append(Integer.toHexString(val));
            }
            m.appendReplacement(sb, hexcolor.toString());
        }
        m.appendTail(sb);
        css = sb.toString();

        // Shorten colors from #AABBCC to #ABC. Note that we want to make sure
        // the color is not preceded by either ", " or =. Indeed, the property
        //     filter: chroma(color="#FFFFFF");
        // would become
        //     filter: chroma(color="#FFF");
        // which makes the filter break in IE.
        m = PATTERN12.matcher(css);
        sb = new StringBuffer();
        while (m.find()) {
            // Test for AABBCC PATTERN
            if (m.group(3).equalsIgnoreCase(m.group(4)) &&
                    m.group(5).equalsIgnoreCase(m.group(6)) &&
                    m.group(7).equalsIgnoreCase(m.group(8))) {
                m.appendReplacement(sb, m.group(1) + m.group(2) + "#" + m.group(3) + m.group(5) + m.group(7));
            } else {
                m.appendReplacement(sb, m.group());
            }
        }
        m.appendTail(sb);
        css = sb.toString();

        // Remove empty rules.
        css = PATTERN13.matcher(css).replaceAll("");

        if (linebreakpos >= 0) {
            // Some source control tools don't like it when files containing lines longer
            // than, say 8000 characters, are checked in. The linebreak option is used in
            // that case to split long lines after a specific column.
            int i = 0;
            int linestartpos = 0;
            sb = new StringBuffer(css);
            while (i < sb.length()) {
                char c = sb.charAt(i++);
                if (c == '}' && i - linestartpos > linebreakpos) {
                    sb.insert(i, '\n');
                    linestartpos = i;
                }
            }

            css = sb.toString();
        }

        // Trim the final string (for any leading or trailing white spaces)
        css = css.trim();

        // Write the output...
        out.write(css);
    }
}