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

package com.aliyun.fastmodel.core.tree.expr.atom.datetime;

import com.aliyun.fastmodel.core.tree.AstVisitor;
import com.aliyun.fastmodel.core.tree.NodeLocation;
import com.aliyun.fastmodel.core.tree.expr.BaseExpression;
import com.aliyun.fastmodel.core.tree.expr.atom.IntervalExpression;
import com.aliyun.fastmodel.core.tree.expr.literal.StringLiteral;

/**
 * @author panguanjing
 * @date 2021/4/13
 */
public class DateTimeAddStartExpression extends DateTimeAddExpression {
    public DateTimeAddStartExpression(NodeLocation location, String origin,
                                      BaseExpression dateTimeExpression,
                                      IntervalExpression intervalExpression,
                                      StringLiteral startDate) {
        super(location, origin, dateTimeExpression, intervalExpression, startDate);
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitDateTimeAddStartException(this, context);
    }
}
