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
import com.aliyun.fastmodel.core.tree.expr.enums.IntervalQualifiers;
import com.aliyun.fastmodel.core.tree.expr.literal.BaseLiteral;
import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 区间表达式
 *
 * @author panguanjing
 * @date 2020/10/3
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class IntervalExpression extends BaseExpression {
    /**
     * value
     */
    private final BaseLiteral intervalValue;

    /**
     * qualifiers 修饰符
     */
    private final IntervalQualifiers intervalQualifiers;

    /**
     * 表达式
     */
    private final BaseExpression baseExpression;

    /**
     * 是否有interval的value
     */
    private final Boolean interval;

    public IntervalExpression(NodeLocation location, String origin,
                              BaseLiteral intervalValue,
                              IntervalQualifiers intervalQualifiers,
                              BaseExpression baseExpression, Boolean interval) {
        super(location, origin);
        this.intervalValue = intervalValue;
        this.intervalQualifiers = intervalQualifiers;
        this.baseExpression = baseExpression;
        this.interval = interval;
    }

    public IntervalExpression(
        BaseLiteral intervalValue,
        IntervalQualifiers intervalQualifiers
    ) {
        this(null, null, intervalValue, intervalQualifiers, null, true);
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitIntervalExpr(this, context);
    }

    @Override
    public List<? extends Node> getChildren() {
        return ImmutableList.of(intervalValue, baseExpression);
    }
}

