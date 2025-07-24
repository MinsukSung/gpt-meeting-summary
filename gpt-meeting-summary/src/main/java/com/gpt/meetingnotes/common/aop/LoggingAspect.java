package com.gpt.meetingnotes.common.aop;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gpt.meetingnotes.common.annotation.Masking;
import com.gpt.meetingnotes.common.enums.MaskingType;
import com.gpt.meetingnotes.common.utils.LogUtils;
import com.gpt.meetingnotes.common.utils.MaskingUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {
	
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    /**
     * Controller 계층의 모든 메서드에 대해 요청/응답 로그를 출력한다.
     * - 요청 파라미터는 마스킹 처리 후 JSON 형태로 출력
     * - 응답 값 역시 마스킹 처리 후 출력
     * - Http Header 및 QueryString 정보도 함께 기록
     * - 처리 시간(ms) 측정
     */
    @Around("execution(* com.gpt.meetingnotes..controller..*(..))")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();

        // 요청 로그 처리
        try {
            Object[] args = joinPoint.getArgs();
            List<Object> maskedArgs = Arrays.stream(args)
                    .map(this::deepMask)
                    .collect(Collectors.toList());

            String queryString = request.getQueryString();
            String headers = LogUtils.extractHeaders(request);

            log.debug("[REQUEST] {}\n-- Headers --\n{}\n-- Query --\n{}\n-- Params --\n{}", methodName,
                    headers, queryString != null ? queryString : "(none)", toJson(maskedArgs));

        } catch (Exception e) {
            log.warn("[REQUEST_LOGGING_ERROR] {} - {}", methodName, e.getMessage(), e);
        }

        Object result = null;
        try {
            result = joinPoint.proceed();
            Object maskedResult = deepMask(result);
            long end = System.currentTimeMillis();
            log.debug("[RESPONSE] {}\n-- Result --\n{}\n-- Duration --\n{} ms", methodName,
                    toJson(maskedResult), (end - start));
            return result;
        } catch (Throwable e) {
            log.error("[EXCEPTION] {} - {}", methodName, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 객체를 JSON 직렬화 → 역직렬화 → 마스킹 재귀 적용하여 새 객체 반환
     */
    private Object deepMask(Object obj) {
        if (obj == null) return null;
        try {
            ObjectWriter writer = objectMapper.writerFor(obj.getClass());
            String json = writer.writeValueAsString(obj);

            ObjectReader reader = objectMapper.readerFor(obj.getClass());
            Object clone = reader.readValue(json);

            maskFieldsRecursive(clone, new HashSet<>());
            return clone;
        } catch (Exception e) {
            log.warn("[MASKING_FAIL] Object: {} - {}", obj.getClass().getSimpleName(), e.getMessage(), e);
            return "[Unserializable]";
        }
    }

    /**
     * 재귀적으로 객체의 모든 필드를 탐색하여 @Masking 이 있는 경우 값 마스킹
     */
    private void maskFieldsRecursive(Object obj, Set<Object> visited) {
    	 if (obj == null || visited.contains(obj)) return;
    	    visited.add(obj);

    	    Class<?> clazz = obj.getClass();
    	    for (Field field : clazz.getDeclaredFields()) {
    	        field.setAccessible(true);
    	        try {
    	            Object value = field.get(obj);

    	            if (field.isAnnotationPresent(Masking.class)) {
    	                if (value instanceof String) {
    	                    try {
    	                        Masking masking = field.getAnnotation(Masking.class);
    	                        MaskingType type = masking.type(); // 예: EMAIL, NAME, DEFAULT 등
    	                        String masked = MaskingUtils.autoMask((String) value, type);
    	                        field.set(obj, masked);
    	                    } catch (Exception e) {
    	                        log.warn("[FIELD_MASKING_FAIL] {}.{} (type: {}) - {}",
    	                                clazz.getSimpleName(),
    	                                field.getName(),
    	                                field.getAnnotation(Masking.class).type(),
    	                                e.getMessage(), e);
    	                    }
    	                } else {
    	                    log.warn("[FIELD_MASKING_WARNING] {}.{} - @Masking은 String 타입에만 적용됩니다. 현재 타입: {}",
    	                            clazz.getSimpleName(), field.getName(), field.getType().getSimpleName());
    	                }
    	            }

    	            // 재귀 처리: String 외 객체들에 대해 계속 탐색
    	            if (value != null && !isPrimitiveOrWrapper(value.getClass())) {
    	                maskFieldsRecursive(value, visited);
    	            }

    	        } catch (Exception e) {
    	            log.warn("[FIELD_ACCESS_ERROR] {}.{} - {}", clazz.getSimpleName(), field.getName(), e.getMessage(), e);
    	        }
    	    }
    }

    /**
     * 기본형, 래퍼 클래스, String 여부 판단 (마스킹 대상 아님)
     */
    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.equals(String.class) ||
                Number.class.isAssignableFrom(clazz) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(Void.class);
    }

    /**
     * 객체를 pretty JSON 문자열로 변환
     */
    private String toJson(Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("[JSON_SERIALIZATION_FAIL] - {}", e.getMessage(), e);
            return "[Unserializable]";
        }
    }
}
