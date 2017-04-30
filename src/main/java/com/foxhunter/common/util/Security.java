package com.foxhunter.common.util;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Security {
    //  小程序仅第一次登陆时的keycode 即sessionkey有效
    //	private static String keycode = "g86yUczO4ZtM66eVVoQonA==";
    //	private static String ivKey = "SAo2Uwa8VmsdL74fl/41zg==";
    public static String decrypt(String strIn, String key, String ivKey) {
        try {
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(strIn);
            byte[] keys = new BASE64Decoder().decodeBuffer(key);
            byte[] ivKeys = new BASE64Decoder().decodeBuffer(ivKey);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(keys, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(ivKeys);
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
//	public static void main(String[] args) {
//		String key = "V0tobDRkbGVxcHIyaUZGSnhHemZIWU5xMDJCdHY5UzVRWS9FK1dTcUxOYjVsK1VQNjlGRmdBT1JSL1gwS2NCMmJjeXpHVXdsV05mS3h1angwTFRTQnNOMm5NRXJYT01Pb29UYjVMWExQNEhkS0NuSXFuTVZtQWpGM3dKZHpZMGZNS0hrUVlnbmsrajZ5VnN4QzBsN0lEK2ZhNkVZWnlWbC9lTjB0UEcwK3dMRkJHV2xDY0RGd25VVGJxMnNOellFTEd2NGRtRVlTZndRT09xNWl2d0JYaUwzdjZBaXRZVGZKSGtBUkh4TmdWT3g1dWVmYWVpazR5bjVCSTVkdFpOb05qbys2VHJ0YldZZUx2d01TdjlMemFLakJidDhRNEluek1Ta1UyYTB4aUxEcWdYUnVuME9QZExSWCtoNTJrUmNndDdZVDJEaG9jSSttQXBrWmpOTEtWZFJhUHZxcW1uSkJtWHp6c0x1TVV6R2xhTGJ1MVYrZmlhT2dlWU9hZ0VsbWVzZXNYZTlESXJrSm42cHAzM0RMQ3R3RkdsNnRCL0EyQndtcDdOeFptQ2FLTkU4d2ZWdFQzUUlqdGdaQlNWRTdyUnRKa3Y5TCtsZzM5alVWZzlPL1E9PQ==";
//		//String keys = "MIDG7t58lDDQCBpzOpbDpigpsvQu4hUuMV34mpkyDkKUYpn8yXa/HQWzXVXvRt9Vh7kIBt1uVuV9n0iogVGyxuuBF9Hdlo1PoQ4MLSbdo6xZhTbmM1BqK8RaUSLVUmuVg3LVFiIP6BhUch0plaOlYngdFsJGi0Lq9p3cfrClScX4RD1+/kbM/+nJty4iQpEaBIfMRx150xV4tTPMYH4GEQl2TgRyTCTUHmHfe7csEqYWkZJ9bKsIqcLHKZOOECBTMdAbU092UmD+SgrQFYL02uA0KY2Ek5VNf1Ulk00BjtHm5Y2tXf9YMlQRxJimMbNXHXKur1AwR5yHHjs8qeQM0LtPvIeI4dvyxndme3VlXALdnAi2mUjVHEjxq/gSi1Tnk6Ozoz90uPJ6D9FYuRLgPgKtU1r6qZU6AcdnWp1GfVcrra5DGCiK68/ZWq1UC/mz2WJW3D1pC4Oed0XW1xfbNA==";
//		String keys= Base64.decode(key);
//		System.out.println(decrypt(keys,keycode,ivKey));
//	}
}