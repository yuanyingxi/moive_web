package com.movie.DTO;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 注册接口
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;  // 注册用户名
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "密码需至少8位，包含大小写字母和数字")
    private String password;  // 设置密码
    private String phone;
    private String email;

    // 验证手机号合理性
    @AssertTrue(message = "手机号格式不正确")  // @AssertTrue 表示方法返回 True 是验证通过
    private boolean isPhoneValid() {
        if (phone != null) {
            return phone.matches("^1[3-9]\\d{9}$");
        }
        // 说明是空值，用户选择的是邮箱校验
        return true;
    }

    // 验证邮箱号合理性
    @AssertTrue(message = "邮箱格式不正确")  // @AssertTrue 表示方法返回 True 是验证通过
    private boolean isEmailValid() {
        if (email != null) {
            return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        }
        // 说明是空值，用户选择的是手机号校验
        return true;
    }

    // 手机号和邮箱不能同时为空
    @AssertTrue(message = "手机号和邮箱不能同时为空")  // @AssertTrue 表示方法返回 True 是验证通过
    private boolean isContactValid() {
        return phone != null || email != null;
    }

}
