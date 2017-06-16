package cn.sampson.android.xiandou.utils.des;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DES3Model {

    // public static final String DEFAULT_CIPHER_ALGORITHM =
    // "DESede/ECB/PKCS5Padding";
    public static final String DEFAULT_CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    public static final String KEY = "3b38e11ffd65698aedeb5ffc";

    public static Key getSessionKey() {
        try {
            DESKeySpec dks = new DESKeySpec(KEY.getBytes());
            Key secretKey = SecretKeyFactory.getInstance("DES").generateSecret(dks);
            return secretKey;
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    // 对文件进行加密
    public static void encryptionFile(String srcFile, String destionFile) throws Exception {
        int len = 0;
        byte[] buffer = new byte[5 * 1024];
        byte[] cipherbuffer = null;
        byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};
        IvParameterSpec paramSpec = new IvParameterSpec(iv);

        // 使用会话密钥对文件加密。
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM, new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, getSessionKey(), paramSpec);

        FileInputStream fis = new FileInputStream(new File(srcFile));
        FileOutputStream fos = new FileOutputStream(new File(destionFile));
        // 读取原文，加密并写密文到输出文件。
        while ((len = fis.read(buffer)) != -1) {
            cipherbuffer = cipher.update(buffer, 0, len);
            fos.write(cipherbuffer);
            fos.flush();
        }
        cipherbuffer = cipher.doFinal();
        fos.write(cipherbuffer);
        fos.flush();

        if (fis != null)
            fis.close();
        if (fos != null)
            fos.close();
    }

    // 根据密钥解密文件
    public static void descryptionFile(String srcFile, String destionFile) throws Exception {
        int len = 0;
        byte[] buffer = new byte[5 * 1024];
        byte[] plainbuffer = null;
        byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};
        IvParameterSpec paramSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM, new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, getSessionKey(), paramSpec);

        FileInputStream fis = new FileInputStream(new File(srcFile));
        FileOutputStream fos = new FileOutputStream(new File(destionFile));

        while ((len = fis.read(buffer)) != -1) {
            plainbuffer = cipher.update(buffer, 0, len);
            fos.write(plainbuffer);
            fos.flush();
        }

        plainbuffer = cipher.doFinal();
        fos.write(plainbuffer);
        fos.flush();

        if (fis != null)
            fis.close();
        if (fos != null)
            fos.close();
    }

    // 根据密钥解密文件
    public static void descryptionInputStream(InputStream is, String destionFile) throws Exception {
        byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};
        IvParameterSpec paramSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM, new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, getSessionKey(), paramSpec);

        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        FileOutputStream fos = new FileOutputStream(new File(destionFile));

        if (is != null) {
            byte[] buffer = new byte[5 * 1024];
            byte[] cipherbuffer = null;
            try {
                int n;
                while ((n = is.read(buffer)) != -1) {
                    cipherbuffer = cipher.update(buffer, 0, n);
                    fos.write(cipherbuffer);
                    fos.flush();
                }
            } finally {
                if (is != null)
                    is.close();
                if (fos != null)
                    fos.close();
            }
        }

    }

}