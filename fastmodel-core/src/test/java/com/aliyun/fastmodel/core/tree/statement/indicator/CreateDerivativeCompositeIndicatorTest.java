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

import com.aliyun.fastmodel.core.tree.QualifiedName;
import com.aliyun.fastmodel.core.tree.statement.element.CreateElement;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Desc:
 *
 * @author panguanjing
 * @date 2021/1/4
 */
public class CreateDerivativeCompositeIndicatorTest {

    @Test(expected = NullPointerException.class)
    public void testCheckReference() {
        CreateDerivativeCompositeIndicator derivativeCompositeIndicator = new CreateDerivativeCompositeIndicator(
            CreateElement.builder().qualifiedName(QualifiedName.of("a.b")).build(),
            null, null, null
        );
        assertNotNull(derivativeCompositeIndicator);
    }
}