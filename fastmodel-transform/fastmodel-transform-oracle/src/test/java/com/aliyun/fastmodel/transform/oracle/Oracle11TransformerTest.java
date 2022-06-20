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

package com.aliyun.fastmodel.transform.oracle;

import com.aliyun.fastmodel.core.tree.BaseStatement;
import com.aliyun.fastmodel.core.tree.Comment;
import com.aliyun.fastmodel.core.tree.QualifiedName;
import com.aliyun.fastmodel.core.tree.datatype.BaseDataType;
import com.aliyun.fastmodel.core.tree.datatype.DataTypeEnums;
import com.aliyun.fastmodel.core.tree.datatype.GenericDataType;
import com.aliyun.fastmodel.core.tree.datatype.NumericParameter;
import com.aliyun.fastmodel.core.tree.expr.Identifier;
import com.aliyun.fastmodel.core.tree.statement.constants.TableDetailType;
import com.aliyun.fastmodel.core.tree.statement.table.ColumnDefinition;
import com.aliyun.fastmodel.core.tree.statement.table.CreateTable;
import com.aliyun.fastmodel.core.tree.statement.table.constraint.BaseConstraint;
import com.aliyun.fastmodel.core.tree.util.DataTypeUtil;
import com.aliyun.fastmodel.transform.api.context.ReverseContext;
import com.aliyun.fastmodel.transform.api.dialect.DialectMeta;
import com.aliyun.fastmodel.transform.api.dialect.DialectName;
import com.aliyun.fastmodel.transform.api.dialect.DialectNode;
import com.aliyun.fastmodel.transform.api.dialect.transform.DialectTransform;
import com.aliyun.fastmodel.transform.api.dialect.transform.DialectTransformParam;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Oracle11TransformerTest
 *
 * @author panguanjing
 * @date 2021/7/24
 */
public class Oracle11TransformerTest {
    Oracle11Transformer oracle11Transformer = new Oracle11Transformer();

    @Test
    public void transform() {
        ColumnDefinition col1 = ColumnDefinition.builder().colName(new Identifier("col1"))
                .dataType(new GenericDataType(new Identifier(DataTypeEnums.DATE
                        .name()), null))
                .comment(new Comment("测试DATE类型")).build();
        ColumnDefinition col2 = ColumnDefinition.builder().colName(new Identifier("col2"))
                .dataType(new GenericDataType(new Identifier(DataTypeEnums.DATE
                        .name()), null))
                .notNull(true)
                .comment(new Comment("测试必填")).build();
        ColumnDefinition id = ColumnDefinition.builder().colName(new Identifier("id"))
                .dataType(DataTypeUtil.simpleType("NUMBER", ImmutableList.of(new NumericParameter("36"))))
                .primary(true)
                .comment(new Comment("测试主键")).build();
        ColumnDefinition b = ColumnDefinition.builder().colName(new Identifier("b"))
                .dataType(DataTypeUtil.simpleType(DataTypeEnums.VARCHAR, new NumericParameter("100")))
                .build();
        List<ColumnDefinition> columns = ImmutableList.of(
                id, col1, col2, b
        );
        CreateTable createTable = CreateTable.builder()
                .tableName(QualifiedName.of("dim_shop")).comment(new Comment("测试表"))
                .detailType(TableDetailType.NORMAL_DIM).columns(columns)
                .build();
        DialectNode sourceNode = oracle11Transformer.transform(createTable);
        DialectTransformParam param = DialectTransformParam.builder()
                .sourceMeta(DialectMeta.getByName(DialectName.FML))
                .sourceNode(sourceNode)
                .targetMeta(DialectMeta.getByName(DialectName.ORACLE))
                .build();
        DialectNode dialectNode = DialectTransform.transform(param);
        assertEquals(dialectNode.getNode(), "CREATE TABLE dim_shop;");
    }

    @Test
    public void reverse() {
        String plsql = "CREATE TABLE DIM_SHOP (\n"
                + "  CAR INT  \n"
                + ");";
        BaseStatement reverse = oracle11Transformer.reverse(new DialectNode(plsql),
                ReverseContext.builder().reverseTableType(TableDetailType.DWS).build());
        CreateTable createTable = (CreateTable) reverse;
        assertEquals(createTable.getQualifiedName(), QualifiedName.of("dim_shop"));
        List<ColumnDefinition> columnDefines = createTable.getColumnDefines();
        assertEquals(columnDefines.size(), 1);
        ColumnDefinition c = columnDefines.get(0);
        assertEquals(c.getColName(), new Identifier("car"));
    }

    @Test
    public void testCase() {
        String plsql = "create table dim_shop(car varchar(10));";
        BaseStatement reverse = oracle11Transformer.reverse(new DialectNode(plsql));
        assertNotNull(reverse);
        CreateTable createTable = (CreateTable) reverse;
        List<ColumnDefinition> columnDefines = createTable.getColumnDefines();
        assertEquals(1, columnDefines.size());
        ColumnDefinition columnDefinition = columnDefines.get(0);
        BaseDataType dataType = columnDefinition.getDataType();
        assertEquals(dataType.getTypeName(), DataTypeEnums.VARCHAR);
    }

    @Test
    public void testUnique() {
        String plsql = "create table dim_shop(car varchar(10), unique (car), primary key(car));";
        BaseStatement reverse = oracle11Transformer.reverse(new DialectNode(plsql));
        CreateTable createTable = (CreateTable) reverse;
        List<BaseConstraint> constraintStatements = createTable.getConstraintStatements();
        assertEquals(2, constraintStatements.size());
    }
}