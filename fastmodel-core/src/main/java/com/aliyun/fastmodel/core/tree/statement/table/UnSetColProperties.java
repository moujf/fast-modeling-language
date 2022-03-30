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

package com.aliyun.fastmodel.core.tree.statement.table;

import java.util.List;

import com.aliyun.fastmodel.core.tree.AstVisitor;
import com.aliyun.fastmodel.core.tree.QualifiedName;
import com.aliyun.fastmodel.core.tree.expr.Identifier;
import com.aliyun.fastmodel.core.tree.statement.BaseUnSetProperties;
import lombok.Getter;

/**
 * UnSet Col properties
 * <p>
 * {code}
 * ALTER TABLE a.b CHANGE COLUMN col1 UNSET PROPERTIES('code'='value')
 * {code}
 *
 * @author panguanjing
 * @date 2021/3/9
 */
@Getter
public class UnSetColProperties extends BaseUnSetProperties {

    private final Identifier changeColumn;

    public UnSetColProperties(QualifiedName source,
                              Identifier changeColumn, List<String> propertyList) {
        super(source, propertyList);
        this.changeColumn = changeColumn;
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitUnSetColProperties(this, context);
    }
}
