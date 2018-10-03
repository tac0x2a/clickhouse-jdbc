package net.tac42.clickhouse.response;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import net.tac42.clickhouse.util.guava.StreamUtils;


public class ByteFragmentTest {

    @DataProvider(name = "stringEscape")
    public Object[][] stringEscape() {
        return new Object[][]{
                {"abc'xyz", "abc\\'xyz"},
                {"\rabc\n\0xyz\b\f\txyz\\", "\\rabc\\n\\Nxyz\\b\\f\\txyz\\\\"},
                {"\n汉字'", "\\n汉字\\'"},
                {"юникод,'юникод'\n", "юникод,\\'юникод\\'\\n"}
        };
    }

    @Test(dataProvider = "stringEscape")
    public void testEscape(String str, String escapedStr) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteFragment.escape(str.getBytes(StreamUtils.UTF_8), out);
        assertEquals(out.toString(StreamUtils.UTF_8.name()), escapedStr);
    }

    @Test(dataProvider = "stringEscape")
    public void testUnescape(String str, String escapedStr) throws IOException {
        byte[] bytes = escapedStr.getBytes(StreamUtils.UTF_8);
        ByteFragment byteFragment = new ByteFragment(bytes, 0, bytes.length);
        assertEquals(new String(byteFragment.unescape(), StreamUtils.UTF_8.name()), str);
    }

}
