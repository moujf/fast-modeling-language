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

package com.aliyun.fastmodel.core.tree.expr.atom;

import java.util.List;

import com.aliyun.fastmodel.core.tree.AstVisitor;
import com.aliyun.fastmodel.core.tree.Node;
import com.aliyun.fastmodel.core.tree.NodeLocation;
import com.aliyun.fastmodel.core.tree.expr.BaseExpression;
import com.aliyun.fastmodel.core.tree.expr.enums.DateTimeEnum;
import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * extract expression
 *
 * @author panguanjing
 * @date 2020/9/23
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class Extract extends BaseExpression {

    private final DateTimeEnum dateTimeEnum;

    private final BaseExpression expression;

    public Extract(DateTimeEnum dateTimeEnum,
                   BaseExpression expression) {
        super(null, null);
        this.dateTimeEnum = dateTimeEnum;
        this.expression = expression;
    }

    public Extract(NodeLocation location, String origin,
                   DateTimeEnum dateTimeEnum, BaseExpression expression) {
        super(location, origin);
        this.dateTimeEnum = dateTimeEnum;
        this.expression = expression;
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitExtract(this, context);
    }

    @Override
    public List<? extends Node> getChildren() {
        return ImmutableList.of(expression);
    }
}
