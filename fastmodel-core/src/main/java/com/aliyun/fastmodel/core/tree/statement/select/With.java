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

package com.aliyun.fastmodel.core.tree.statement.select;

import java.util.List;

import com.aliyun.fastmodel.core.tree.AbstractNode;
import com.aliyun.fastmodel.core.tree.AstVisitor;
import com.aliyun.fastmodel.core.tree.Node;
import com.aliyun.fastmodel.core.tree.NodeLocation;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Desc:
 *
 * @author panguanjing
 * @date 2020/11/3
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class With extends AbstractNode {

    private final boolean recursive;

    private final List<WithQuery> queries;

    public With(NodeLocation location, boolean recursive,
                List<WithQuery> queries) {
        super(location);
        this.recursive = recursive;
        this.queries = queries;
    }

    public With(boolean recursive, List<WithQuery> queries) {
        this.recursive = recursive;
        this.queries = queries;
    }

    @Override
    public List<? extends Node> getChildren() {
        return queries;
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitWith(this, context);
    }
}
