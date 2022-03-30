/*
 * Copyright 2021-2022 Alibaba Group Holding Ltd.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.aliyun.fastmodel.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 去除字符串两边的引号内容
 *
 * @author panguanjing
 * @date 2020/9/4
 */
public class StripUtils {

    public static final String SUFFIX = ";";

    /**
     * 将字符串坐下strip
     *
     * @param src 原始字符串
     * @return 处理后的字符串
     */
    public static String strip(String src) {
        String prefix = "'";
        if (src.startsWith(prefix)) {
            return StringUtils.strip(src, prefix);
        } else {
            String prefix1 = "\"";
            if (src.startsWith(prefix1)) {
                return StringUtils.strip(src, prefix1);
            }
        }
        return src;
    }

    /**
     * 追加分号，如果结尾没有增加
     *
     * @param code
     * @return appendSemicolon
     */
    public static String appendSemicolon(String code) {
        if (!code.endsWith(SUFFIX)) {
            return code + SUFFIX;
        }
        return code;
    }

    /**
     * 删除所有空行
     *
     * @param dsl
     * @return
     */
    public static String removeEmptyLine(String dsl) {
        if (dsl == null) {
            return null;
        }
        String adjusted = dsl.replaceAll("(?m)^[ \t]*\r?\n", "");
        String removeLine = StringUtils.removeEnd(adjusted, "\n");
        return StringUtils.removeEnd(removeLine, "\r");
    }

}
