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

package com.aliyun.aliyun.transform.zen.parser.tree;

import java.util.List;

import com.aliyun.aliyun.transform.zen.parser.BaseZenAstVisitor;
import com.aliyun.fastmodel.core.tree.Node;
import com.google.common.collect.ImmutableList;

/**
 * BaseZenExpression
 *
 * @author panguanjing
 * @date 2021/7/14
 */
public abstract class BaseZenExpression implements BaseZenNode {

    @Override
    public <R, C> R accept(BaseZenAstVisitor<R, C> visitor, C context) {
        return visitor.visitBaseZenExpression(this, context);
    }

    @Override
    public List<? extends Node> getChildren() {
        return ImmutableList.of();
    }
}
