package org.live.module.login.util;

import android.content.Context;
import android.widget.Toast;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表单校验器
 * Created by KAM on 2017/4/12.
 */

public class Validator {
    /**
     * 错误信息
     */
    private String errorMsg;
    private Context context;

    public Validator(Context context) {
        this.context = context;
    }

    /**
     * 开启校验
     */
    public boolean validate(Map<String, String> vals, Map<String, String> labels, Map<String, Map<String, Object>> rules) {
        boolean result = true; // 校验结果
        String errorKey = null; // 定位检验不通过的输入项
        for (String key : vals.keySet()) {
            String val = vals.get(key); // 待校验字符
            Map<String, Object> rule = rules.get(key); // 校验规则组
            for (String ruleName : rule.keySet()) {
                Object ruleVal = rule.get(ruleName);
                switch (ruleName) {
                    case "required":
                        if (!required(val) && (boolean) ruleVal) {
                            result = false;
                            errorKey = key;
                        }
                        break;
                    case "maxLength":
                        if (!maxLength(val, (Integer) ruleVal)) {
                            result = false;
                            errorKey = key;
                        }
                        break;
                    case "number":
                        if (!number(val) && (boolean) ruleVal) {
                            result = false;
                            errorKey = key;
                        }
                        break;
                    case "confirm":
                        if (!confirm(val, (String) ruleVal)) {
                            result = false;
                            errorKey = key;
                        }
                        break;
                }
                if (!result && errorKey != null) {
                    showErrorMsg(labels.get(errorKey) + "错误，" + errorMsg); // 显示错误信息
                    return result; // 校验不通过
                }
            }
        }
        return result; // 校验通过
    }

    /**
     * 确认信息
     *
     * @param val        确认值
     * @param confirmVal 待确认的信息
     * @return
     */
    private boolean confirm(String val, String confirmVal) {
        if (val.length() > 0 && val != null && !val.equals(confirmVal)) {
            errorMsg = "两次输入不一致";
            return false;
        }
        return true;
    }

    /**
     * 必填选项
     *
     * @param val
     * @return
     */
    private boolean required(String val) {
        if (val.length() == 0 || val == null) {
            errorMsg = "这是必填字段";
            return false;
        }
        return true;
    }

    /**
     * 限制最大长度
     *
     * @param val
     * @param length
     * @return
     */
    private boolean maxLength(String val, int length) {
        if (val.length() > length) {
            errorMsg = "最多可输入" + length + "个字符";
            return false;
        }
        return true;
    }

    /**
     * 有效数字
     *
     * @param val
     * @return
     */
    private boolean number(String val) {
        if (!matchRegEx(val, "^[0-9]*$")) {
            errorMsg = "请输入有效的数字";
            return false;
        }
        return true;
    }

    /**
     * 正则表达式校验
     *
     * @param str   待校验字符
     * @param regEx 校验规则
     * @return
     */
    private boolean matchRegEx(String str, String regEx) {
        // 编译正则表达式
        //Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        // 字符串是否与正则表达式相匹配
        boolean rs = matcher.matches();
        return rs;
    }

    /**
     * 显示错误信息
     *
     * @param errorMsg
     */
    private void showErrorMsg(String errorMsg) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
    }
}
