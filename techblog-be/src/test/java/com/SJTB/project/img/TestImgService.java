package com.SJTB.project.img;

import com.SJTB.framework.utils.FrameDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class TestImgService {

    @Test
    public void 이미지해시명변환() {
        try {
            String filename = "noneThumbnail.jpg";
            String filenameWithoutExtension = filename;
            int lastDotIndex = filename.lastIndexOf('.');
            if (lastDotIndex != -1) {
                filenameWithoutExtension = filename.substring(0, lastDotIndex);
            }
            String timestamp = FrameDateUtil.getDate("yyyyMMddHHmmss");
            String combined = filenameWithoutExtension + "_" + timestamp;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(combined.getBytes());
            log.info("##### imgName :" + bytesToHex(encodedhash));
        } catch( Exception e){
            e.printStackTrace();
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
