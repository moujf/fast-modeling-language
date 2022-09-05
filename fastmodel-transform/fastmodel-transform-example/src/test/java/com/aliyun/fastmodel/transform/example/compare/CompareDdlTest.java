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

package com.aliyun.fastmodel.transform.example.compare;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.aliyun.fastmodel.core.tree.AliasedName;
import com.aliyun.fastmodel.core.tree.BaseStatement;
import com.aliyun.fastmodel.core.tree.Comment;
import com.aliyun.fastmodel.core.tree.QualifiedName;
import com.aliyun.fastmodel.core.tree.datatype.DataTypeEnums;
import com.aliyun.fastmodel.core.tree.expr.Identifier;
import com.aliyun.fastmodel.core.tree.statement.constants.ColumnCategory;
import com.aliyun.fastmodel.core.tree.statement.table.ColumnDefinition;
import com.aliyun.fastmodel.core.tree.statement.table.CreateTable;
import com.aliyun.fastmodel.core.tree.statement.table.PartitionedBy;
import com.aliyun.fastmodel.core.tree.util.DataTypeUtil;
import com.aliyun.fastmodel.transform.api.Transformer;
import com.aliyun.fastmodel.transform.api.TransformerFactory;
import com.aliyun.fastmodel.transform.api.compare.CompareContext;
import com.aliyun.fastmodel.transform.api.compare.NodeCompareFactory;
import com.aliyun.fastmodel.transform.api.dialect.DialectMeta;
import com.aliyun.fastmodel.transform.api.dialect.DialectName;
import com.aliyun.fastmodel.transform.api.dialect.DialectNode;
import com.aliyun.fastmodel.transform.api.dialect.transform.DialectTransform;
import com.aliyun.fastmodel.transform.api.dialect.transform.DialectTransformParam;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Compare DDL
 *
 * @author panguanjing
 * @date 2021/9/3
 */
public class CompareDdlTest {

    @Test
    public void testCompare() {
        List<ColumnDefinition> columnDefines = beforeColumnDefine();
        CreateTable before = getCreateTable(columnDefines);
        Transformer transformer = TransformerFactory.getInstance().get(DialectMeta.DEFAULT_MYSQL);
        DialectNode beforeDialectNode = transformer.transform(before);
        String mysqlDDL
                = "create table dim_shop (c1 bigint comment 'abc', c2 varchar(128) comment 'c2') COMMENT 'comment'";
        assertEquals("ALTER TABLE dim_shop DROP COLUMN a;\n" +
                "ALTER TABLE dim_shop ADD\n" +
                "(\n" +
                "   c2 VARCHAR(128) COMMENT 'c2'\n" +
                ")", NodeCompareFactory.getInstance()
                .compareAndFormat(DialectMeta.DEFAULT_MYSQL, beforeDialectNode.getNode(), mysqlDDL));
    }

    @Test
    public void testCompareMysql() {
        String beforeDDL
                = "CREATE TABLE `dim_shop`  (\n" +
                "  `id` bigint(36) NOT NULL,\n" +
                "  `col1` date NULL DEFAULT NULL,\n" +
                "  `col2` varchar(36) NULL DEFAULT '123',\n" +
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ") ENGINE = InnoDB;";
        String afterDDL
                = "CREATE TABLE `dim_shop`  (\n" +
                "  `id` bigint(37) NOT NULL,\n" +
                "  `col1` date NULL DEFAULT NULL,\n" +
                "  `col2` varchar(36) NULL DEFAULT '123',\n" +
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ") ENGINE = InnoDB;";
        List<BaseStatement> delegate = NodeCompareFactory.getInstance()
                .compare(DialectMeta.getByName(DialectName.MYSQL), beforeDDL, afterDDL,
                        CompareContext.builder().build());
        String print = print(delegate);
        assertEquals("ALTER TABLE `dim_shop` CHANGE COLUMN `id` `id` BIGINT(37)", print);
    }

    @Test
    public void testCompareOracleModifyCol() {
        String beforeDDL
                = "CREATE TABLE sdfdsf (\n" +
                "   name VARCHAR(10) DEFAULT '',\n" +
                "   PRIMARY KEY(name)\n" +
                ");\n" +
                "COMMENT ON TABLE sdfdsf IS '水电费费';\n" +
                "COMMENT ON COLUMN sdfdsf.name IS '水电费';";
        String afterDDL
                = "CREATE TABLE sdfdsf (\n" +
                "   name VARCHAR(11) DEFAULT '',\n" +
                "   PRIMARY KEY(name)\n" +
                ");\n" +
                "COMMENT ON TABLE sdfdsf IS '水电费费';\n" +
                "COMMENT ON COLUMN sdfdsf.name IS '水电费';";
        assertEquals("ALTER TABLE dim_shop MODIFY id VARCHAR(37)", NodeCompareFactory.getInstance()
                .compareAndFormat(DialectMeta.getByName(DialectName.ORACLE), beforeDDL, afterDDL));
    }

    @Test
    public void testCompareOracleRenameCol() {
        String beforeDDL
                = "CREATE TABLE dim_shop (\n" +
                "   id   NUMBER(36),\n" +
                "   col1 DATE,\n" +
                "   col2 VARCHAR(36) DEFAULT '123' NOT NULL,\n" +
                "   PRIMARY KEY(id)\n" +
                ");\n";
        String afterDDL
                = "CREATE TABLE dim_shop (\n" +
                "   id1   VARCHAR(37),\n" +
                "   col1 DATE,\n" +
                "   col2 VARCHAR(36) DEFAULT '123' NOT NULL,\n" +
                "   PRIMARY KEY(id1)\n" +
                ");\n";
        assertEquals("ALTER TABLE dim_shop DROP COLUMN id;\n" +
                "ALTER TABLE IF EXISTS dim_shop ADD COLUMN id1 VARCHAR(37);\n" +
                ";\n" +
                "ALTER TABLE dim_shop DROP CONSTRAINT sys_57bcbbea32664b318fb2331dc518891c;\n" +
                "ALTER TABLE dim_shop ADD PRIMARY KEY(id1);", NodeCompareFactory.getInstance()
                .compareAndFormat(DialectMeta.getByName(DialectName.ORACLE), beforeDDL, afterDDL));
    }

    private String print(List<BaseStatement> delegate) {
        String collect = delegate.stream().map(BaseStatement::toString).collect(Collectors.joining(";\n"));
        System.out.println(collect);
        return collect;
    }

    private CreateTable getCreateTable(List<ColumnDefinition> columnDefines) {
        CreateTable before = CreateTable.builder()
                .tableName(QualifiedName.of("dim_shop"))
                .comment(new Comment("comment"))
                .aliasedName(new AliasedName("alias"))
                .columns(columnDefines)
                .partition(new PartitionedBy(
                        ImmutableList.of(
                                ColumnDefinition.builder()
                                        .colName(new Identifier("a"))
                                        .dataType(DataTypeUtil.simpleType(DataTypeEnums.BIGINT))
                                        .category(ColumnCategory.CORRELATION)
                                        .build()
                        )
                ))
                .build();
        return before;
    }

    @Test
    public void testZenCoding() {
        List<ColumnDefinition> columnDefines = beforeColumnDefine();
        String zencoding = "user_id\nuser_name";
        CreateTable before = getCreateTable(columnDefines);
        Transformer transformer = TransformerFactory.getInstance().get(DialectMeta.getByName(DialectName.ZEN));
        DialectNode beforeDialectNode = transformer.transform(before);
        List<BaseStatement> diff = NodeCompareFactory.getInstance().compare(DialectMeta.getByName(DialectName.ZEN),
                beforeDialectNode.getNode(),
                zencoding, CompareContext.builder().qualifiedName(QualifiedName.of("dim_shop")).build());
        assertEquals(print(diff), "ALTER TABLE dim_shop DROP COLUMN c1;\n"
                + "ALTER TABLE dim_shop ADD COLUMNS\n"
                + "(\n"
                + "   user_id   STRING COMMENT 'user_id',\n"
                + "   user_name STRING COMMENT 'user_name'\n"
                + ")");
    }

    private List<ColumnDefinition> beforeColumnDefine() {
        List<ColumnDefinition> list = new ArrayList<>();
        ColumnDefinition build = ColumnDefinition.builder().colName(new Identifier("c1"))
                .dataType(DataTypeUtil.simpleType(DataTypeEnums.BIGINT))
                .comment(new Comment("abc"))
                .build();
        list.add(build);
        return list;
    }
}
