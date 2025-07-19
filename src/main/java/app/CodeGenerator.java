package app;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class CodeGenerator {
    @Value("${spring.datasource.url}")
    private String url;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;

    public void generateCode() {
        String outputDir = System.getProperty("user.dir") + "/src/main/java";

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("0109") // 设置作者名
                            .outputDir(outputDir) // 输出路径
                            .commentDate("yyyy-MM-dd");
                })
                .packageConfig(builder -> {
                    builder.parent("app")
                            .moduleName("");
                })
                .strategyConfig(builder -> {
                    builder.addInclude("user","product") // 设置要生成的表名，支持通配符如 user.*
                            .addTablePrefix("t_", "sys_"); // 过滤表前缀
                })
                .templateEngine(new VelocityTemplateEngine()) // 使用 Velocity 引擎
                .execute();
    }
}