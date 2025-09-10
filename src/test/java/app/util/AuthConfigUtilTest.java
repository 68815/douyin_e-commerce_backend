package app.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthConfigUtilTest {

    @Test
    void testRegexWhiteListMatching() {
        // 添加正则表达式模式
        AuthConfigUtil.addWhiteList("^/api/v\\d+/.*$");
        AuthConfigUtil.addWhiteList("^/static/.+\\.(css|js|png)$");
        
        // 测试正则表达式匹配
        assertTrue(AuthConfigUtil.isWhiteList("/api/v1/users"));
        assertTrue(AuthConfigUtil.isWhiteList("/api/v2/products"));
        assertTrue(AuthConfigUtil.isWhiteList("/static/style.css"));
        assertTrue(AuthConfigUtil.isWhiteList("/static/app.js"));
        
        // 测试不匹配的情况
        assertFalse(AuthConfigUtil.isWhiteList("/api/users"));
        assertFalse(AuthConfigUtil.isWhiteList("/static/image.jpg"));
        
        // 测试普通路径模式仍然有效
        AuthConfigUtil.addWhiteList("/public/**");
        assertTrue(AuthConfigUtil.isWhiteList("/public/index.html"));
        assertTrue(AuthConfigUtil.isWhiteList("/public/css/style.css"));
    }

    @Test
    void testInvalidRegexFallback() {
        // 添加无效的正则表达式
        AuthConfigUtil.addWhiteList("^[invalid-regex");
        
        // 应该回退到普通匹配
        assertTrue(AuthConfigUtil.isWhiteList("^[invalid-regex"));
    }
}