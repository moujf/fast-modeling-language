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

package com.aliyun.fastmodel.parser.visitor;

import java.util.ArrayList;
import java.util.List;

import com.aliyun.fastmodel.core.tree.AstVisitor;
import com.aliyun.fastmodel.core.tree.Node;
import com.aliyun.fastmodel.core.tree.expr.BaseExpression;
import com.aliyun.fastmodel.core.tree.expr.atom.FunctionCall;
import com.aliyun.fastmodel.core.tree.expr.atom.TableOrColumn;
import lombok.Getter;

/**
 * Desc:
 *
 * @author panguanjing
 * @date 2020/11/5
 */
@Getter
public class AstExtractVisitor extends AstVisitor<Void, Void> {
    private final List<TableOrColumn> tableOrColumnList = new ArrayList<>();


    @Override
    public Void visitExpression(BaseExpression expression, Void context) {
        List<? extends Node> children = expression.getChildren();
        for (Node n : children) {
            process(n);
        }
        return null;
    }

    @Override
    public Void visitTableOrColumn(TableOrColumn tableOrColumn, Void context) {
        tableOrColumnList.add(tableOrColumn);
        return null;
    }
}

