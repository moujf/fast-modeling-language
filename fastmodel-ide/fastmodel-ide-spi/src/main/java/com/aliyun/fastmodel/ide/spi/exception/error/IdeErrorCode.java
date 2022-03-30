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

package com.aliyun.fastmodel.ide.spi.exception.error;

/**
 * ErrorCode
 *
 * @author panguanjing
 * @date 2022/1/5
 */
public enum IdeErrorCode implements IErrorCode {

    /**
     * 参数不合法
     */
    ARGUMENT_NOT_VALIE,

    /**
     * 文件超过限制
     */
    FILE_SIZE_OVER_LIMIT,
    /**
     * FML语句不合法
     */
    FML_IS_NOT_VALID;

    @Override
    public String code() {
        return this.name();
    }
}
