/**
 * Licensed under Creative Commons Attribution 3.0 Unported license.
 * http://creativecommons.org/licenses/by/3.0/
 * You are free to copy, distribute and transmit the work, and
 * to adapt the work.  You must attribute android-plist-parser
 * to Free Beachler (http://www.freebeachler.com).
 * <p>
 * The Android PList parser (android-plist-parser) is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.
 */
package cn.sampson.android.xiandou.utils.plist;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Wrapper for {@link StringBuilder}.
 *
 * @author fbeachler
 */
public class Stringer {

    private StringBuilder builder;

    /**
     *
     */
    public Stringer() {
        builder = new StringBuilder();
    }

    /**
     *
     */
    public Stringer(String val) {
        builder = new StringBuilder(val);
    }

    /**
     * Clear the class-global stringbuilder.
     *
     * @return fluent interface to builder
     */
    public StringBuilder newBuilder() {
        builder.setLength(0);
        return builder;
    }

    /**
     * Get the class-global stringbuilder.
     *
     * @return fluent interface to builder
     */
    public StringBuilder getBuilder() {
        return builder;
    }

    /**
     * Converts an {@link InputStream} to a stringer.
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static Stringer convert(InputStream is) throws IOException {
        /*
         * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the Stringer class to
		 * produce the string.
		 */
        Stringer ret = new Stringer();
        if (is != null) {
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    ret.getBuilder().append(buffer, 0, n);
                }
            } finally {
                is.close();
            }
        }
        return ret;
    }

    public static Stringer convertDecode(InputStream is) throws Exception {
        byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};
        DESKeySpec dks = new DESKeySpec("3b38e11ffd65698aedeb5ffc".getBytes());
        Key secretKey = SecretKeyFactory.getInstance("DES").generateSecret(dks);
        IvParameterSpec paramSpec = new IvParameterSpec(iv);

        // 使用会话密钥对文件加密。
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding", new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);

        Stringer ret = new Stringer();
        if (is != null) {
            byte[] buffer = new byte[5 * 1024];
            byte[] cipherbuffer = null;
            try {

                int n;
                while ((n = is.read(buffer)) != -1) {
                    cipherbuffer = cipher.update(buffer, 0, n);
                    char[] chars = getChars(cipherbuffer);
                    ret.getBuilder().append(chars);
                }
            } finally {
                is.close();
            }
        }
        return ret;
    }

    // byte转char

    private static char[] getChars(byte[] bytes) {
        int length = bytes.length;
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

}
