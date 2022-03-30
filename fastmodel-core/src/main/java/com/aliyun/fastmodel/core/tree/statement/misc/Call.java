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

package com.aliyun.fastmodel.core.tree.statement.misc;

import java.util.List;

import com.aliyun.fastmodel.core.tree.AstVisitor;
import com.aliyun.fastmodel.core.tree.BaseStatement;
import com.aliyun.fastmodel.core.tree.Node;
import com.aliyun.fastmodel.core.tree.expr.atom.FunctionCall;
import com.aliyun.fastmodel.core.tree.statement.constants.StatementType;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lombok.Getter;

/**
 * call语句不属于query或者operator语句
 *
 * @author panguanjing
 * @date 2021/3/11
 */
@Getter
public class Call extends BaseStatement {

    private final FunctionCall functionCall;

    public Call(FunctionCall functionCall) {
        super(null);
        Preconditions.checkNotNull(functionCall);
        this.functionCall = functionCall;
        setStatementType(StatementType.CALL);
    }

    @Override
    public List<? extends Node> getChildren() {
        return ImmutableList.of(functionCall);
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitCall(this, context);
    }
}
