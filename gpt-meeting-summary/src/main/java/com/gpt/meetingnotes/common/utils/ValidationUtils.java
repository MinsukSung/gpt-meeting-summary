package com.gpt.meetingnotes.common.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidationUtils {
	
	private static final int[] JUMIN_WEIGHTS = {2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5};

    public static boolean isValidJumin(String input) {
        if (input == null) return false;
        String normalized = input.replaceAll("-", "");
        return isValidJuminFormat(normalized)
                && isValidJuminBirthDate(normalized)
                && isValidJuminGenderCode(normalized)
                && isValidJuminChecksum(normalized);
    }

    public static boolean isValidJuminFormat(String s) {
        return s != null && s.matches("\\d{13}");
    }

    public static boolean isValidJuminGenderCode(String s) {
        if (s.length() < 7) return false;
        char genderCode = s.charAt(6);
        return genderCode >= '1' && genderCode <= '4';
    }

    public static boolean isValidJuminBirthDate(String s) {
        if (s.length() < 7) return false;
        String birth = s.substring(0, 6);
        char genderCode = s.charAt(6);

        String centuryPrefix;
        switch (genderCode) {
            case '1': case '2': centuryPrefix = "19"; break;
            case '3': case '4': centuryPrefix = "20"; break;
            default: return false;
        }

        String fullDate = centuryPrefix + birth;
        try {
            LocalDate.parse(fullDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidJuminChecksum(String s) {
        if (s.length() != 13) return false;
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(s.charAt(i));
            sum += digit * JUMIN_WEIGHTS[i];
        }
        int mod = sum % 11;
        int checkDigit = (11 - mod) % 10;
        return checkDigit == Character.getNumericValue(s.charAt(12));
    }
}
