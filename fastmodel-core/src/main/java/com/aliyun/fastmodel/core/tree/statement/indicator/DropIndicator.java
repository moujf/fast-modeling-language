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

package com.aliyun.fastmodel.core.tree.statement.indicator;

import com.aliyun.fastmodel.core.tree.AstVisitor;
import com.aliyun.fastmodel.core.tree.QualifiedName;
import com.aliyun.fastmodel.core.tree.statement.BaseDrop;
import com.aliyun.fastmodel.core.tree.statement.constants.StatementType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 删除指标的语句
 * <p>
 * DSL举例
 * <pre>
 *         DROP INDICATOR ut.indicator_name;
 *     </pre>
 *
 * @author panguanjing
 * @date 2020/9/7
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class DropIndicator extends BaseDrop {

    public DropIndicator(QualifiedName qualifiedName) {
        super(qualifiedName);
        this.setStatementType(StatementType.INDICATOR);
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitDropIndicator(this, context);
    }
}
