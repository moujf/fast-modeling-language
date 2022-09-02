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

package com.aliyun.fastmodel.transform.api.dialect.transform;

import com.aliyun.fastmodel.core.tree.Node;
import com.aliyun.fastmodel.transform.api.Transformer;
import com.aliyun.fastmodel.transform.api.TransformerFactory;
import com.aliyun.fastmodel.transform.api.context.TransformContext;
import com.aliyun.fastmodel.transform.api.datatype.DataTypeConverter;
import com.aliyun.fastmodel.transform.api.datatype.DataTypeConverterFactory;
import com.aliyun.fastmodel.transform.api.dialect.DialectMeta;
import com.aliyun.fastmodel.transform.api.dialect.DialectNode;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.Objects;

/**
 * 转换服务
 *
 * @author panguanjing
 * @date 2021/7/26
 */
@Slf4j
public class DialectTransform {
    /**
     * 支持将一个方言的code转换为另外一个方言的code的处理
     *
     * @param dialectTransformParam
     * @return {@link DialectNode}
     */
    public static DialectNode transform(DialectTransformParam dialectTransformParam) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Preconditions.checkNotNull(dialectTransformParam, "param can't be null");
        DialectMeta sourceMeta = dialectTransformParam.getSourceMeta();
        DialectMeta targetMeta = dialectTransformParam.getTargetMeta();
        DialectNode sourceNode = dialectTransformParam.getSourceNode();
        Preconditions.checkNotNull(sourceMeta, "source dialect meta can't be null");
        Preconditions.checkNotNull(targetMeta, "target dialect meta can't be null");
        Preconditions.checkNotNull(sourceNode, "source node can't be null");
        log.info("第{}步运行时间：{}", 1, stopWatch.getTime());
        // 设置split停止标记
        stopWatch.reset();
        //重置后必须使用start方法
        stopWatch.start();
        //if source meta equal target meta
        if (Objects.equals(sourceMeta, targetMeta)) {
            return dialectTransformParam.getSourceNode();
        }
        Transformer sourceTransformer = TransformerFactory.getInstance().get(sourceMeta);
        if (sourceTransformer == null) {
            throw new UnsupportedOperationException(
                    "can't find source transformer with meta:" + sourceMeta);
        }
        Transformer targetTransformer = TransformerFactory.getInstance().get(targetMeta);
        if (targetTransformer == null) {
            throw new UnsupportedOperationException(
                    "can't find target transformer with meta:" + targetMeta);
        }
        log.info("第{}步运行时间：{}", 2, stopWatch.getTime());
        // 设置split停止标记
        stopWatch.reset();
        //重置后必须使用start方法
        stopWatch.start();
        Node reverse = sourceTransformer.reverse(sourceNode, dialectTransformParam.getReverseContext());
        if (reverse == null) {
            throw new UnsupportedOperationException(
                    "unsupported reverse the sourceNode:" + dialectTransformParam.getSourceNode());
        }
        log.info("第{}步运行时间：{}", 3, stopWatch.getTime());
        // 设置split停止标记
        stopWatch.reset();
        //重置后必须使用start方法
        stopWatch.start();
        TransformContext transformContext = dialectTransformParam.getTransformContext();
        if (transformContext == null) {
            transformContext = TransformContext.builder().build();
        }
        if (transformContext.getDataTypeTransformer() == null) {
            DataTypeConverter dataTypeTransformer = DataTypeConverterFactory.getInstance()
                    .get(sourceMeta, targetMeta);
            transformContext.setDataTypeTransformer(dataTypeTransformer);
        }
        DialectNode dialectNode = targetTransformer.transform(reverse, transformContext);
        stopWatch.stop();
        log.info("第{}步运行时间：{}", 4, stopWatch.getTime());
        return dialectNode;
    }

    public static void main(String[] args) throws InterruptedException {

        // 暂停计时测试
        StopWatch stopWatch = StopWatch.createStarted();
        Thread.sleep(2000);
        System.out.println(stopWatch.getTime()); // 2005,暂停之后不再计时，所以暂停之后的2s不会计入
        // 设置split停止标记
        stopWatch.reset();
        //重置后必须使用start方法
        stopWatch.start();
        Thread.sleep(2000);
        System.out.println(stopWatch.getTime()); // 这里返回split标记到开始计时时的时间间隔，4010
        // 设置split停止标记
        stopWatch.reset();
        //重置后必须使用start方法
        stopWatch.start();
        Thread.sleep(5000);
        stopWatch.stop();
        System.out.println(stopWatch.getTime()); // 这里返回split标记到开始计时时的时间间隔，4010
    }

}
