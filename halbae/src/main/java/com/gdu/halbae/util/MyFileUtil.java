package com.gdu.halbae.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Matcher;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

@Component
public class MyFileUtil {

	// 경로 구분자
	private String sep = Matcher.quoteReplacement(File.separator);
	
	// String path 만들기
	public String getPath() {
		LocalDate now = LocalDate.now();
		// 루트/storage/2023/05/08
		return "/storage" + sep + now.getYear() + sep + String.format("%02d", now.getMonthValue()) + sep + String.format("%02d", now.getDayOfMonth());
//		return "/Users" + sep + "mong" + sep + "UserProfile";
	}
	
	 // String summernoteImagePath 만들기
    public String getSummernoteImagePath() {
        return "/storage" + sep + "summernote";
    }

    // String filesystemName 만들기
    public String getFilesystemName(String originName) {

        // 원래 첨부 파일의 확장자 꺼내기
        String extName = null;

        // 확장자에 마침표(.)가 포함된 예외 상황 처리
        if (originName.endsWith("tar.gz")) {
            extName = "tar.gz";
        } else {
            // String.split(정규식)
            // 정규식에서 마침표(.)는 모든 문자를 의미하므로 이스케이프 처리하거나 문자클래스로 처리한다.
            // 이스케이프 처리 : \.
            // 문자클래스 처리 : [.]
            String[] array = originName.split("\\.");
            extName = array[array.length - 1];
        }

        // 결과 반환
        // UUID.extName
        return UUID.randomUUID().toString().replace("-", "") + "." + extName;

    }

    // String tempPath 만들기
    public String getTempPath() {
        return "/storage" + sep + "temp";
    }

    // String tempfileName 만들기 (zip 파일)
    public String getTempfileName() {
        return UUID.randomUUID().toString().replace("-", "") + ".zip";
    }

    // String yesterdayPath 만들기
    public String getYesterdayPath() {
        LocalDate date = LocalDate.now();
        date = date.minusDays(1);  // 1일 전
        return "/storage" + sep + date.getYear() + sep + String.format("%02d", date.getMonthValue()) + sep + String.format("%02d", date.getDayOfMonth());
    }

    // 파일 확장자 가져오기
    public static String getFileExtension(String filename) {
        if (filename.lastIndexOf(".") != -1 && filename.lastIndexOf(".") != 0) {
            return filename.substring(filename.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    // 파일 업로드
    public static String uploadFile(String uploadPath, String filename, byte[] fileData) throws IOException {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String filePath = uploadPath + File.separator + filename;
        File dest = new File(filePath);
        FileUtils.writeByteArrayToFile(dest, fileData);
        return filePath;
    }
	
}