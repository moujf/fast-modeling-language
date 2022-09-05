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

package com.aliyun.fastmodel.transform.api.compare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import com.aliyun.fastmodel.core.tree.BaseStatement;
import com.aliyun.fastmodel.transform.api.dialect.Dialect;
import com.aliyun.fastmodel.transform.api.dialect.DialectMeta;
import com.aliyun.fastmodel.transform.api.dialect.DialectName;
import com.aliyun.fastmodel.transform.api.dialect.DialectNode;
import com.aliyun.fastmodel.transform.api.dialect.transform.DialectTransform;
import com.aliyun.fastmodel.transform.api.dialect.transform.DialectTransformParam;

/**
 * Node Compare Factory
 *
 * @author panguanjing
 * @date 2021/8/29
 */
public class NodeCompareFactory {
    private static final NodeCompareFactory INSTANCE = new NodeCompareFactory();

    private final Map<DialectMeta, NodeCompare> maps = new HashMap<>();

    private NodeCompareFactory() {
        ServiceLoader<NodeCompare> load = ServiceLoader.load(NodeCompare.class);
        for (NodeCompare transformer : load) {
            Dialect annotation = transformer.getClass().getAnnotation(Dialect.class);
            if (annotation != null) {
                DialectMeta dialectMeta = new DialectMeta(annotation.value(), annotation.version());
                maps.put(dialectMeta, transformer);
            }
        }
    }

    public static NodeCompareFactory getInstance() {
        return INSTANCE;
    }

    public List<BaseStatement> compare(DialectMeta dialectMeta,
                                       String before, String after, CompareContext context) {
        CompareResult result = compareResult(dialectMeta, before, after, context);
        return result.getDiffStatements();
    }

    public String compareAndFormat(DialectMeta dialectMeta,
                                   String before, String after) {
        return this.compareAndFormat(dialectMeta, before, after, CompareContext.builder().build());
    }

    public String compareAndFormat(DialectMeta dialectMeta,
                                   String before, String after, CompareContext context) {
        CompareResult result = compareResult(dialectMeta, before, after, context);
        return result.getDiffStatements().stream().map(diffStatement -> {
            DialectNode sourceNode = new DialectNode(diffStatement.toString());
            DialectTransformParam param = DialectTransformParam.builder()
                    .sourceMeta(DialectMeta.getByName(DialectName.FML))
                    .sourceNode(sourceNode)
                    .targetMeta(dialectMeta)
                    .build();
            DialectNode dialectNode = DialectTransform.transform(param);
            return dialectNode.getNode();
        }).collect(Collectors.joining(";\n"));
    }

    public CompareResult compareResult(DialectMeta dialectMeta,
                                       String before, String after, CompareContext context) {
        NodeCompare nodeCompare = maps.get(dialectMeta);
        if (nodeCompare == null) {
            throw new UnsupportedOperationException("unsupport the dialect diff:" + dialectMeta);
        }
        return nodeCompare.compareResult(new DialectNode(before), new DialectNode(after), context);
    }
}
