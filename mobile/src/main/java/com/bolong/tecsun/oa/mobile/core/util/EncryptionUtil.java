package com.bolong.tecsun.oa.mobile.core.util;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;

/**
 * <p></p>
 * <p/>
 * <p>Project: TecsunOA.</p>
 * <p>Date: 2015/11/03.</p>
 * <p>Description:
 *
 * @author SemperChen
 * @version 1.0-SNAPSHOT
 */
public class EncryptionUtil {

    private final static String TAG=EncryptionUtil.class.getSimpleName();
    private final static boolean DEBUG=true;

    //字符串加密算法名称
    private final String ALGORITHM = "RSA";

    //字符串加密填充名称
    private final String PADDING = "RSA/NONE/NoPadding";

    //字符串安全提供者
    private final String PROVIDER = "BC";

    //储存卡路径
    private String mobileCacheDir =Environment.getExternalStorageDirectory().getPath();

    //字符串的私钥路径
    private String privateKeyPath = mobileCacheDir +"/private.key";

    //保存字符串公钥路径
    private String publicKeyPath = mobileCacheDir +"/public.key";

    //保存Base64密码字符串默认文件名

    private String cipherBase64File= mobileCacheDir +"/cipher";

    private static EncryptionUtil instance=null;

    private EncryptionUtil(Context context){
        //获取手机缓存目录
        mobileCacheDir =context.getCacheDir().getPath();
        privateKeyPath= mobileCacheDir +"/private.key";
        publicKeyPath= mobileCacheDir +"/public.key";
        cipherBase64File= mobileCacheDir +"/cipher";
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public static void init(Context context){
        if(instance==null){
            instance=new EncryptionUtil(context);
        }
    }

    /**
     * 获取唯一实例
     *
     * @return 数据源实例
     */
    public static EncryptionUtil getInstance(){
        if (instance == null) {
            throw new IllegalStateException(TAG + "未初始化");
        }
        return instance;
    }


    /**
     *生成密钥包含一对私钥和公钥
     *
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void generateKey() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
            keyGen.initialize(256);
            final KeyPair key = keyGen.generateKeyPair();

            File privateKeyFile = new File(privateKeyPath);
            File publicKeyFile = new File(publicKeyPath);

            // 创建文件来存储公共和私人密钥
/*            if (privateKeyFile.getParentFile() != null) {
                privateKeyFile.getParentFile().mkdirs();
            }
            privateKeyFile.createNewFile();

            if (publicKeyFile.getParentFile() != null) {
                publicKeyFile.getParentFile().mkdirs();
            }
            publicKeyFile.createNewFile();*/

            // 保存公钥
            ObjectOutputStream publicKeyOS = new ObjectOutputStream(
                    new FileOutputStream(publicKeyFile));
            publicKeyOS.writeObject(key.getPublic());
            publicKeyOS.close();

            // 保存私钥
            ObjectOutputStream privateKeyOS = new ObjectOutputStream(
                    new FileOutputStream(privateKeyFile));
            privateKeyOS.writeObject(key.getPrivate());
            privateKeyOS.close();
        } catch (Exception e) {
            if (DEBUG){
                Log.e(TAG, "生成密钥对出错:" + e.getMessage());
            }
        }

    }

    /**
     * 检查公钥和私钥是否存在
     *
     * @return boolean.
     */
    public boolean areKeysPresent() {

        File privateKey = new File(privateKeyPath);
        File publicKey = new File(publicKeyPath);

        return privateKey.exists() && publicKey.exists();
    }

    /**
     * 使用公钥加密纯文本
     *
     * @param text
     *            : 原文本
     * @param key
     *           公钥
     * @return 加密文本
     * @throws Exception
     */
    public byte[] encrypt(String text, PublicKey key) {
        byte[] cipherText = null;
        try {
            // 得到一个RSA密码对象和供应商
            Security.addProvider(new BouncyCastleProvider());
            final Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);

            // 使用公钥加密纯文本
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            if (DEBUG){
                Log.e(TAG, "使用公钥加密出错:" + e.getMessage());
            }
        }
        return cipherText;
    }

    /**
     * 使用私钥解密文本
     *
     * @param text
     *            :编译文本
     * @param key
     *            :私钥
     * @return 纯文本
     * @throws Exception
     */
    public String decrypt(byte[] text, PrivateKey key) {
        byte[] decryptedText = null;
        try {
            Security.addProvider(new BouncyCastleProvider());
            final Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);

            // 使用私钥解密文本
            cipher.init(Cipher.DECRYPT_MODE, key);
            decryptedText = cipher.doFinal(text);

        } catch (Exception e) {
            if (DEBUG){
                Log.e(TAG, "使用私钥解密文本出错:" + e.getMessage());
            }
        }
        if (decryptedText!=null) {
            return new String(decryptedText);
        }
        return null;
    }

    /**
     * 加密
     *
     * @param originalText 原文本
     */
    public void encryptText(String originalText){

        // 检查是否有秘钥对如果一对密钥生成.
        if (!areKeysPresent()) {

            generateKey();
        }
        ObjectInputStream inputStream ;

        // 使用公钥加密文本
        try {
            inputStream = new ObjectInputStream(new FileInputStream(
                    publicKeyPath));
            final PublicKey publicKey = (PublicKey) inputStream.readObject();
            final byte[] cipherText = encrypt(originalText, publicKey);

            // 使用字符串保存密码二进制数据
            String cipherTextBase64 = encodeBase64(cipherText);
            // 将Base64密码写入内存卡
            writeDataToStorageCard(cipherBase64File,cipherTextBase64.getBytes());
        } catch (Exception e) {
            if (DEBUG){
                Log.e(TAG, "加密数据出错:" + e.getMessage());
            }
        }

    }

    /**
     *解密
     */
    public String decryptText(){

        ObjectInputStream inputStream ;

        byte[] cipher=readDataFromStorageCard(cipherBase64File);
        String cipherTextBase64=null;
        if (cipher!=null){
            cipherTextBase64=new String(cipher);
        }
        try {

            // 找回密码二进制数据的字符串
            byte[] cipherTextArray = decodeBase64(cipherTextBase64);

            // 使用私钥解密密文
            inputStream = new ObjectInputStream(new FileInputStream(
                    privateKeyPath));
            final PrivateKey privateKey = (PrivateKey) inputStream.readObject();

            return decrypt(cipherTextArray, privateKey);
        } catch (Exception e) {
            if (DEBUG){
                Log.e(TAG, "解密数据出错:" + e.getMessage());
            }
        }
        return null;
    }

    /*******************************************************************************************************************
     *Base64处理
     */

    /**
     * Base64编码
     *
     * @param bytes 字节数组
     * @return String
     */
    public String encodeBase64(byte[] bytes){
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    /**
     * Base64解码
     *
     * @param cipherTextBase64 密码
     * @return 字节数组
     */
    public byte[] decodeBase64(String cipherTextBase64){
        return Base64.decode(cipherTextBase64.getBytes(),Base64.DEFAULT);
    }

    /*******************************************************************************************************************
     * 文件读取写入管理
     */

    /**
     * 写入数据到内存卡
     *
     * @param filePath 文件路径
     * @param buffer 字节数组
     */
    public void writeDataToStorageCard(String filePath,byte[] buffer){
        OutputStream outputStream=null;
        try {
            File file=new File(filePath);

            outputStream=new FileOutputStream(file);
            outputStream.write(buffer);
        } catch (IOException e) {
            if (DEBUG){
                Log.e(TAG, "从内存卡写入数据出错:" + e.getMessage());
            }
        } finally {
                try {
                    if (outputStream!=null){
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }

    /**
     * 从内存卡读取数据
     *
     * @param filePath 文件路径
     */
    public byte[] readDataFromStorageCard(String filePath){
        InputStream inputStream=null;
        try {
            File file=new File(filePath);
            inputStream=new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            inputStream.read(bytes);
            if(bytes.length!=0){
                return bytes;
            }
        } catch (IOException e) {
            if (DEBUG){
                Log.e(TAG,"从内存卡读取数据出错:"+e.getMessage());
            }
            return null;
        } finally {
            try {
                if (inputStream!=null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
