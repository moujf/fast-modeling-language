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

package com.aliyun.fastmodel.core.tree.expr;

import com.aliyun.fastmodel.core.tree.expr.literal.LongLiteral;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Desc:
 *
 * @author panguanjing
 * @date 2020/11/20
 */
public class DereferenceExpressionTest {

    @Test
    public void testEquals() {
        DereferenceExpression dereferenceExpression = new DereferenceExpression(
            new LongLiteral("1"),
            new Identifier("1")
        );
        DereferenceExpression dereferenceExpression2 = new DereferenceExpression(
            new LongLiteral("1"),
            new Identifier("1"));
        assertEquals(dereferenceExpression, dereferenceExpression2);
    }
}