<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head><title>서비스 오류</title></head>
<body>
    <h2>오류가 발생했습니다</h2>
    <p>오류 코드: ${requestScope.errorCode}</p>
    <p>오류 메시지: ${requestScope.errorMessage}</p>
    <p>누락된 값 : ${requestScope.fieldErrors}</p>
</body>
</html>