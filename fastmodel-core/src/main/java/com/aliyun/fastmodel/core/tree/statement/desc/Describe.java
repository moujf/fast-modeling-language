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

package com.aliyun.fastmodel.core.tree.statement.desc;

import java.util.List;

import com.aliyun.fastmodel.core.tree.AstVisitor;
import com.aliyun.fastmodel.core.tree.Node;
import com.aliyun.fastmodel.core.tree.QualifiedName;
import com.aliyun.fastmodel.core.tree.statement.BaseQueryStatement;
import com.aliyun.fastmodel.core.tree.statement.constants.ShowType;
import com.aliyun.fastmodel.core.tree.statement.constants.StatementType;
import com.google.common.collect.ImmutableList;
import lombok.Getter;

/**
 * 基本描述语句
 *
 * @author panguanjing
 * @date 2020/11/30
 */
@Getter
public class Describe extends BaseQueryStatement {

    /**
     * 描述的类型的内容
     */
    private final ShowType descType;

    private final String identifier;

    public Describe(QualifiedName qualifiedName,
                    ShowType descType) {
        super(qualifiedName.getFirstIdentifierIfSizeOverOne());
        this.descType = descType;
        identifier = qualifiedName.getSuffixPath();
        setStatementType(StatementType.DESCRIBE);
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitDescribe(this, context);
    }

    @Override
    public List<? extends Node> getChildren() {
        return ImmutableList.of();
    }
}
