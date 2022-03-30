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

package com.aliyun.fastmodel.driver.server.command;

import com.aliyun.fastmodel.core.tree.statement.table.CreateDimTable;
import com.aliyun.fastmodel.core.tree.statement.table.CreateFactTable;
import com.aliyun.fastmodel.core.tree.statement.table.CreateTable;
import com.aliyun.fastmodel.driver.model.DriverResult;

/**
 * Desc:
 *
 * @author panguanjing
 * @date 2020/12/9
 */
@Command({CreateTable.class, CreateDimTable.class, CreateFactTable.class})
public class CreateTableCommand implements FmlCommand<CreateTable, Void> {

    @Override
    public DriverResult<Void> execute(CreateTable params) {
        //  invoke the ddd domain service or application service
        return new DriverResult<>(null, true, null);
    }
}
