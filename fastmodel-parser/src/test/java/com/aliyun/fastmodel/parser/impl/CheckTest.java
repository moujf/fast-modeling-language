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

package com.aliyun.fastmodel.parser.impl;

import com.aliyun.fastmodel.core.exception.ParseException;
import com.aliyun.fastmodel.core.parser.DomainLanguage;
import com.aliyun.fastmodel.parser.NodeParser;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * 用于校验的处理
 *
 * @author panguanjing
 * @date 2020/9/17
 */
public class CheckTest {
    NodeParser fastModelAntlrParser = new NodeParser();

    @Test
    public void testCreateTable() {
        DomainLanguage domainLanguage = setDomainLanguage(
            "create table t1  (col bigint comment 'abc', COL varchar(10) comment 'bc') comment '表名' with "
                + "tblproperties(\"type\" = \"normal_dim\")");
        try {
            fastModelAntlrParser.parse(domainLanguage);
        } catch (ParseException e) {
            assertNotNull(e);
        }
    }


    @Test
    public void testCreateConstraint(){
        DomainLanguage domainLanguage = setDomainLanguage("create table t1 (col1 bigint, constraint c1 dim key (col1) references t1)");
        try {
            fastModelAntlrParser.parse(domainLanguage);
        } catch (ParseException e) {
            assertNotNull(e);
        }
    }


    @Test
    public void testCreateConstraintByLevel(){
        DomainLanguage domainLanguage = setDomainLanguage("create fact table t1(col1 bigint, constraint c1 level <col1:(col)>) comment 'table' with tblproperties('type'='tx_fact')");
        try {
            fastModelAntlrParser.parse(domainLanguage);
        } catch (ParseException e) {
            assertNotNull(e);
            assertTrue(e.getMessage().contains("constraint"));
        }
    }

    private DomainLanguage setDomainLanguage(String sql) {
        return new DomainLanguage(sql);
    }

}
