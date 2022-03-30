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

package com.aliyun.fastmodel.core.tree.util;

import java.util.List;

import com.aliyun.fastmodel.core.tree.datatype.BaseDataType;
import com.aliyun.fastmodel.core.tree.datatype.DataTypeEnums;
import com.aliyun.fastmodel.core.tree.datatype.DataTypeParameter;
import com.aliyun.fastmodel.core.tree.datatype.GenericDataType;
import com.aliyun.fastmodel.core.tree.expr.Identifier;
import com.google.common.collect.ImmutableList;

/**
 * DataType Util
 *
 * @author panguanjing
 * @date 2020/11/12
 */
public class DataTypeUtil {
    /**
     * simpleType 处理
     *
     * @param dataTypeEnums 标示名称
     * @param parameters    参数
     * @return {@link BaseDataType}
     */
    public static BaseDataType simpleType(DataTypeEnums dataTypeEnums, DataTypeParameter... parameters) {
        return new GenericDataType(new Identifier(dataTypeEnums.name(), false), ImmutableList.copyOf(parameters));
    }

    /**
     * use dataType
     *
     * @param dataType
     * @param parameters
     * @return {@link BaseDataType}
     */
    public static BaseDataType simpleType(String dataType, List<DataTypeParameter> arguments) {
        return new GenericDataType(new Identifier(dataType), arguments);
    }

    /**
     * 转换下处理
     *
     * @param srcDataType
     * @param dataTypeEnums
     * @return
     * @throws UnsupportedOperationException 如果srcDataType不是GenericDataType
     */
    public static BaseDataType convert(BaseDataType srcDataType, DataTypeEnums dataTypeEnums) {
        if (srcDataType instanceof GenericDataType) {
            return new GenericDataType(new Identifier(dataTypeEnums.name()),
                ((GenericDataType)srcDataType).getArguments());
        }
        throw new UnsupportedOperationException("unSupported convert not GenericDataType:" + srcDataType.getClass());
    }

}
