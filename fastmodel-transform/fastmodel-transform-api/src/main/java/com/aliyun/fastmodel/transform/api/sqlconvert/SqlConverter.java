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

package com.aliyun.fastmodel.transform.api.sqlconvert;

import com.aliyun.fastmodel.core.tree.QualifiedName;
import com.aliyun.fastmodel.core.tree.statement.rule.function.BaseFunction;

/**
 * SqlConverter
 *
 * @author panguanjing
 * @date 2021/6/1
 */
public interface SqlConverter<T extends BaseFunction> {
    /**
     * 将函数转换为指定的内容
     *
     * @param tableName       表名
     * @param baseFunction    函数
     * @param matchExpression 表达式
     * @return
     */
    public String convert(QualifiedName tableName, T baseFunction, String matchExpression);

    /**
     * 是否match
     *
     * @param baseFunction
     * @return
     */
    default boolean isMatch(T baseFunction) {
        return true;
    }
}
