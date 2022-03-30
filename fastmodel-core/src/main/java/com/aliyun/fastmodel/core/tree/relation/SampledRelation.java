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

package com.aliyun.fastmodel.core.tree.relation;

import java.util.List;

import com.aliyun.fastmodel.core.tree.AstVisitor;
import com.aliyun.fastmodel.core.tree.BaseRelation;
import com.aliyun.fastmodel.core.tree.Node;
import com.aliyun.fastmodel.core.tree.NodeLocation;
import com.aliyun.fastmodel.core.tree.expr.BaseExpression;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Desc:
 *
 * @author panguanjing
 * @date 2020/11/10
 */
@Getter
@Setter
@ToString
public class SampledRelation extends BaseRelation {


    private final BaseRelation relation;

    private final SampledType sampledType;

    private final BaseExpression samplePercentage;

    public SampledRelation(BaseRelation relation, SampledType sampledType,
                           BaseExpression samplePercentage) {
        this(null, relation, sampledType, samplePercentage);
    }

    public SampledRelation(NodeLocation location,
                           BaseRelation relation, SampledType sampledType,
                           BaseExpression samplePercentage) {
        super(location);
        this.relation = relation;
        this.sampledType = sampledType;
        this.samplePercentage = samplePercentage;
    }

    @Override
    public List<? extends Node> getChildren() {
        return ImmutableList.of(relation, samplePercentage);
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitSampledRelation(this, context);
    }
}
