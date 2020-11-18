package com.blockchain.backend.util;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.data.util.Pair;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * 计算工具类
 *
 * @author OD
 */
public class CalculateUtil {

    private CalculateUtil() {
    }

    /**
     * 应用sha256算法，根据输入随机生成一个哈希值
     *
     * @param str 传入的字符串
     * @return 生成的哈希值
     */
    public static String applySha256(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (Exception e) {
            System.err.println("getSHA256 is error" + e.getMessage());
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                builder.append("0");
            }
            builder.append(temp);
        }
        return builder.toString();
    }

    /**
     * 运用椭圆曲线算法生成私钥和公钥
     *
     * @return 私钥和公钥
     */
    public static Pair<String, Pair<String, String>> generateKeys() {
        X9ECParameters ecp = SECNamedCurves.getByName("secp256r1");
        ECDomainParameters domainParameters = new ECDomainParameters
                (ecp.getCurve(), ecp.getG(), ecp.getN(), ecp.getH(), ecp.getSeed());
        AsymmetricCipherKeyPair keyPair;
        ECKeyGenerationParameters keyGenerationParameters
                = new ECKeyGenerationParameters(domainParameters, new SecureRandom());
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        generator.init(keyGenerationParameters);
        keyPair = generator.generateKeyPair();
        ECPrivateKeyParameters privateKeyParameters = (ECPrivateKeyParameters) keyPair.getPrivate();
        ECPublicKeyParameters publicKeyParameters = (ECPublicKeyParameters) keyPair.getPublic();
        String privateKey = privateKeyParameters.getD().toString(16);
        String publicKeyX = publicKeyParameters.getQ().getXCoord().toBigInteger().toString(16);
        String publicKeyY = publicKeyParameters.getQ().getYCoord().toBigInteger().toString(16);
        return Pair.of(privateKey, Pair.of(publicKeyX, publicKeyY));
    }

    /**
     * 运用双哈希生成比特币地址
     *
     * @param publicKey 公钥
     * @return 比特币地址
     */
    public static String generateAddress(Pair<String, String> publicKey) {
        String firstEncrypt = applySha256(publicKey.getFirst() + publicKey.getSecond());
        byte[] bytes = firstEncrypt.getBytes(StandardCharsets.UTF_8);
        Digest digest = new RIPEMD160Digest();
        digest.update(bytes, 0, bytes.length);
        byte[] rsData = new byte[digest.getDigestSize()];
        digest.doFinal(rsData, 0);
        return Hex.toHexString(rsData);
    }

}
