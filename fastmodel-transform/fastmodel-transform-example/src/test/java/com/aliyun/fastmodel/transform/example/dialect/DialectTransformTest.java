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

package com.aliyun.fastmodel.transform.example.dialect;

import com.aliyun.fastmodel.core.tree.Comment;
import com.aliyun.fastmodel.core.tree.QualifiedName;
import com.aliyun.fastmodel.core.tree.datatype.DataTypeEnums;
import com.aliyun.fastmodel.core.tree.datatype.GenericDataType;
import com.aliyun.fastmodel.core.tree.datatype.NumericParameter;
import com.aliyun.fastmodel.core.tree.expr.Identifier;
import com.aliyun.fastmodel.core.tree.expr.literal.StringLiteral;
import com.aliyun.fastmodel.core.tree.statement.constants.TableDetailType;
import com.aliyun.fastmodel.core.tree.statement.table.ColumnDefinition;
import com.aliyun.fastmodel.core.tree.statement.table.CreateTable;
import com.aliyun.fastmodel.core.tree.statement.table.constraint.BaseConstraint;
import com.aliyun.fastmodel.core.tree.statement.table.constraint.PrimaryConstraint;
import com.aliyun.fastmodel.core.tree.util.DataTypeUtil;
import com.aliyun.fastmodel.transform.api.context.TransformContext;
import com.aliyun.fastmodel.transform.api.dialect.DialectMeta;
import com.aliyun.fastmodel.transform.api.dialect.DialectName;
import com.aliyun.fastmodel.transform.api.dialect.DialectNode;
import com.aliyun.fastmodel.transform.api.dialect.transform.DialectTransform;
import com.aliyun.fastmodel.transform.api.dialect.transform.DialectTransformParam;
import com.aliyun.fastmodel.transform.fml.FmlTransformer;
import com.aliyun.fastmodel.transform.fml.datatype.Fml2OracleDataTypeConverter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Dialect Transform test
 *
 * @author panguanjing
 * @date 2021/7/26
 */
public class DialectTransformTest {

    @Test
    public void testTransformOracleToMysql() {
        DialectTransformParam param = DialectTransformParam.builder()
                .sourceMeta(DialectMeta.getByName(DialectName.ORACLE))
                .sourceNode(new DialectNode("CREATE TABLE a (b BIGINT);"))
                .targetMeta(DialectMeta.DEFAULT_MYSQL)
                .build();
        DialectNode transform = DialectTransform.transform(param);
        String node = transform.getNode();
        assertEquals(node, "CREATE TABLE a\n"
                + "(\n"
                + "   b BIGINT\n"
                + ")"
        );
    }

    @Test
    public void testTransformOracelToMysqlMerge() {
        DialectTransformParam param = DialectTransformParam.builder()
                .sourceMeta(DialectMeta.getByName(DialectName.ORACLE))
                .sourceNode(new DialectNode("CREATE TABLE a (b BIGINT); COMMENT ON TABLE a IS  'comment';"))
                .targetMeta(DialectMeta.DEFAULT_MYSQL)
                .build();
        DialectNode transform = DialectTransform.transform(param);
        assertEquals(transform.getNode(), "CREATE TABLE a\n"
                + "(\n"
                + "   b BIGINT\n"
                + ") COMMENT 'comment'"
        );
    }

    @Test
    public void testTransformOracelToHiveMerge() {
        DialectTransformParam param = DialectTransformParam.builder()
                .sourceMeta(DialectMeta.getByName(DialectName.ORACLE))
                .sourceNode(new DialectNode("CREATE TABLE a (b BIGINT); COMMENT ON TABLE a IS  'comment';"))
                .targetMeta(DialectMeta.DEFAULT_HIVE)
                .build();
        DialectNode transform = DialectTransform.transform(param);
        assertEquals(transform.getNode(), "CREATE TABLE a\n"
                + "(\n"
                + "   b BIGINT\n"
                + ")\n"
                + "COMMENT 'comment'"
        );
    }

    @Test
    public void testMultiFile() {
        DialectNode sourceNode = new DialectNode(
                "COMMENT ON TABLE dim_shop IS 'comment';\n COMMENT ON TABLE dim_shop IS 'comment';");
        DialectTransformParam param = DialectTransformParam.builder()
                .sourceMeta(DialectMeta.getByName(DialectName.ORACLE))
                .sourceNode(sourceNode)
                .targetMeta(DialectMeta.getByName(DialectName.FML))
                .build();
        DialectNode dialectNode = DialectTransform.transform(param);
        assertEquals(dialectNode.getNode(), "ALTER TABLE dim_shop SET COMMENT 'comment';\n"
                + "ALTER TABLE dim_shop SET COMMENT 'comment';");
    }

    @Test
    public void testFmlToOracleMultiFile() {
        DialectNode sourceNode = new DialectNode(
                "ALTER TABLE dim_shop SET COMMENT 'comment';\n ALTER TABLE dim_shop SET COMMENT 'comment';");
        DialectTransformParam param = DialectTransformParam.builder()
                .sourceMeta(DialectMeta.getByName(DialectName.FML))
                .sourceNode(sourceNode)
                .targetMeta(DialectMeta.getByName(DialectName.ORACLE))
                .build();
        DialectNode dialectNode = DialectTransform.transform(param);
        assertEquals(dialectNode.getNode(), "COMMENT ON TABLE dim_shop IS 'comment';\n"
                + "COMMENT ON TABLE dim_shop IS 'comment';");
    }

    @Test
    public void testFmlToHive() {
        ColumnDefinition id = ColumnDefinition.builder().colName(new Identifier("id"))
                .dataType(DataTypeUtil.simpleType("NUMBER", ImmutableList.of(new NumericParameter("36"))))
                .primary(true)
                .comment(new Comment("测试主键")).build();
        ColumnDefinition col1 = ColumnDefinition.builder().colName(new Identifier("col1"))
                .dataType(new GenericDataType(new Identifier(DataTypeEnums.DATE
                        .name()), null))
                .comment(new Comment("测试DATE类型")).build();
//        ColumnDefinition col2 = ColumnDefinition.builder().colName(new Identifier("col2"))
//                .dataType(new GenericDataType(new Identifier(DataTypeEnums.DATE
//                        .name()), null))
//                .notNull(true)
//                .comment(new Comment("测试必填")).build();
//        ColumnDefinition i1 = ColumnDefinition.builder().colName(new Identifier("i1"))
//                .dataType(new GenericDataType(new Identifier(DataTypeEnums.BIGINT
//                        .name()), null))
//                .comment(new Comment("测试数值")).build();
//        ColumnDefinition b = ColumnDefinition.builder().colName(new Identifier("b"))
//                .dataType(DataTypeUtil.simpleType(DataTypeEnums.VARCHAR, new NumericParameter("100")))
//                .build();
        List<ColumnDefinition> columns = ImmutableList.of(
                id, col1
        );
        CreateTable createTable = CreateTable.builder()
                .tableName(QualifiedName.of("dim_shop")).comment(new Comment("测试表"))
                .detailType(TableDetailType.NORMAL_DIM).columns(columns)
                .build();
        FmlTransformer fmlTransformer = new FmlTransformer();
        DialectNode sourceNode = fmlTransformer.transform(createTable);
        DialectTransformParam param = DialectTransformParam.builder()
                .sourceMeta(DialectMeta.getByName(DialectName.FML))
                .sourceNode(sourceNode)
                .targetMeta(DialectMeta.getByName(DialectName.HIVE))
//                .transformContext(TransformContext.builder().dataTypeTransformer(fml2OracleDataTypeConverter).build())
                .build();
        DialectNode dialectNode = DialectTransform.transform(param);
        assertEquals("CREATE TABLE dim_shop\n" +
                "(\n" +
                "   id   CUSTOM(36) COMMENT '测试主键',\n" +
                "   col1 DATE COMMENT '测试DATE类型'\n" +
                ")\n" +
                "COMMENT '测试表'", dialectNode.getNode());
        DialectTransform.transform(param);
    }

    @Test
    public void testFmlToOracle() {
        ColumnDefinition id = ColumnDefinition.builder().colName(new Identifier("id"))
                .dataType(DataTypeUtil.simpleType("NUMBER", ImmutableList.of(new NumericParameter("36"))))
//                .primary(true)
                .comment(new Comment("测试主键")).build();
        ColumnDefinition id1 = ColumnDefinition.builder().colName(new Identifier("id1"))
                .dataType(DataTypeUtil.simpleType("NUMBER", ImmutableList.of(new NumericParameter("36"))))
//                .primary(true)
                .comment(new Comment("测试主键1")).build();
        ColumnDefinition col1 = ColumnDefinition.builder().colName(new Identifier("year"))
                .dataType(new GenericDataType(new Identifier(DataTypeEnums.DATE
                        .name()), null))
                .comment(new Comment("测试DATE类型")).build();
        ColumnDefinition col2 = ColumnDefinition.builder().colName(new Identifier("col2"))
//                .dataType(DataTypeUtil.simpleType("String", ImmutableList.of(new NumericParameter("36"))))
                .dataType(new GenericDataType(new Identifier(DataTypeEnums.VARCHAR
                        .name()), ImmutableList.of(new NumericParameter("36"))))
                .notNull(true)
                //默认值
                .defaultValue(new StringLiteral("123"))
                .comment(new Comment("测试默认值")).build();
//        ColumnDefinition i1 = ColumnDefinition.builder().colName(new Identifier("i1"))
//                .dataType(new GenericDataType(new Identifier(DataTypeEnums.BIGINT
//                        .name()), null))
//                .comment(new Comment("测试数值")).build();
//        ColumnDefinition b = ColumnDefinition.builder().colName(new Identifier("b"))
//                .dataType(DataTypeUtil.simpleType(DataTypeEnums.VARCHAR, new NumericParameter("100")))
//                .build();
        List<ColumnDefinition> columns = ImmutableList.of(
                id, id1, col1, col2
        );
        List<BaseConstraint> constraints = ImmutableList.of(new PrimaryConstraint(
                new Identifier("abc"),
                Lists.newArrayList(new Identifier("id"),new Identifier("id1"))
        ));
        CreateTable createTable = CreateTable.builder()
                .tableName(QualifiedName.of("dim_shop")).comment(new Comment("测试表"))
                .detailType(TableDetailType.NORMAL_DIM).columns(columns)
                .constraints(constraints)
                .build();
        FmlTransformer fmlTransformer = new FmlTransformer();
        DialectNode sourceNode = fmlTransformer.transform(createTable);
        DialectTransformParam param = DialectTransformParam.builder()
                .sourceMeta(DialectMeta.getByName(DialectName.FML))
                .sourceNode(sourceNode)
                .targetMeta(DialectMeta.getByName(DialectName.ORACLE))
//                .transformContext(TransformContext.builder().dataTypeTransformer(fml2OracleDataTypeConverter).build())
                .build();
        DialectNode dialectNode = DialectTransform.transform(param);
        assertEquals("CREATE TABLE dim_shop (\n" +
                "   id     NUMBER(36) NOT NULL,\n" +
                "   id1    NUMBER(36) NOT NULL,\n" +
                "   year DATE,\n" +
                "   col2   VARCHAR(36) DEFAULT '123' NOT NULL,\n" +
                "   CONSTRAINT abc PRIMARY KEY(id,id1)\n" +
                ");\n" +
                "COMMENT ON TABLE dim_shop IS '测试表';\n" +
                "COMMENT ON COLUMN dim_shop.id IS '测试主键';\n" +
                "COMMENT ON COLUMN dim_shop.id1 IS '测试主键1';\n" +
                "COMMENT ON COLUMN dim_shop.year IS '测试DATE类型';\n" +
                "COMMENT ON COLUMN dim_shop.col2 IS '测试默认值';", StringUtils.replace(StringUtils.replace(dialectNode.getNode(), SINGLE_QUOTE, ""), SINGLE_QUOTE, ""));
        DialectTransform.transform(param);
    }


    @Test
    public void testFmlToMysql() {
        ColumnDefinition id = ColumnDefinition.builder().colName(new Identifier("id"))
                .dataType(DataTypeUtil.simpleType(DataTypeEnums.INT))
//                .primary(true)
                .comment(new Comment("测试主键")).build();
        ColumnDefinition id1 = ColumnDefinition.builder().colName(new Identifier("id1"))
                .dataType(DataTypeUtil.simpleType(DataTypeEnums.DOUBLE))
//                .primary(true)
                .comment(new Comment("测试主键1")).build();
        ColumnDefinition col1 = ColumnDefinition.builder().colName(new Identifier("year"))
                .dataType(new GenericDataType(new Identifier(DataTypeEnums.DATE
                        .name()), null))
                .comment(new Comment("测试DATE类型")).build();
        ColumnDefinition col2 = ColumnDefinition.builder().colName(new Identifier("col2"))
//                .dataType(DataTypeUtil.simpleType("String", ImmutableList.of(new NumericParameter("36"))))
                .dataType(new GenericDataType(new Identifier(DataTypeEnums.VARCHAR
                        .name()), ImmutableList.of(new NumericParameter("36"))))
                .notNull(true)
                //默认值
                .defaultValue(new StringLiteral("123"))
                .comment(new Comment("测试默认值")).build();
//        ColumnDefinition i1 = ColumnDefinition.builder().colName(new Identifier("i1"))
//                .dataType(new GenericDataType(new Identifier(DataTypeEnums.BIGINT
//                        .name()), null))
//                .comment(new Comment("测试数值")).build();
//        ColumnDefinition b = ColumnDefinition.builder().colName(new Identifier("b"))
//                .dataType(DataTypeUtil.simpleType(DataTypeEnums.VARCHAR, new NumericParameter("100")))
//                .build();
        List<ColumnDefinition> columns = ImmutableList.of(
                id, id1, col1, col2
        );
        List<BaseConstraint> constraints = ImmutableList.of(new PrimaryConstraint(
                new Identifier("abc"),
                Lists.newArrayList(new Identifier("id"),new Identifier("id1"))
        ));
        CreateTable createTable = CreateTable.builder()
                .tableName(QualifiedName.of("dim_shop")).comment(new Comment("测试表"))
                .detailType(TableDetailType.NORMAL_DIM).columns(columns)
                .constraints(constraints)
                .build();
        FmlTransformer fmlTransformer = new FmlTransformer();
        DialectNode sourceNode = fmlTransformer.transform(createTable);
        DialectTransformParam param = DialectTransformParam.builder()
                .sourceMeta(DialectMeta.getByName(DialectName.FML))
                .sourceNode(sourceNode)
                .targetMeta(DialectMeta.getByName(DialectName.MYSQL))
//                .transformContext(TransformContext.builder().dataTypeTransformer(fml2OracleDataTypeConverter).build())
                .build();
        DialectNode dialectNode = DialectTransform.transform(param);
        assertEquals("CREATE TABLE dim_shop\n" +
                "(\n" +
                "   id     INT NOT NULL COMMENT '测试主键',\n" +
                "   id1    DOUBLE NOT NULL COMMENT '测试主键1',\n" +
                "   year DATE COMMENT '测试DATE类型',\n" +
                "   col2   VARCHAR(36) NOT NULL DEFAULT '123' COMMENT '测试默认值',\n" +
                "   PRIMARY KEY(id,id1)\n" +
                ") COMMENT '测试表'", StringUtils.replace(StringUtils.replace(dialectNode.getNode(), SINGLE_QUOTE, ""), SINGLE_QUOTE, ""));
        DialectTransform.transform(param);
    }

    /**
     * 单引号
     */
    private static final String SINGLE_QUOTE = "`";

    @Test
    public void testOracleToFml() {
        DialectNode sourceNode = new DialectNode(
                "CREATE TABLE dim_shop(a VARCHAR2(10)); "
        );
        DialectTransformParam param = DialectTransformParam.builder()
                .sourceMeta(DialectMeta.getByName(DialectName.ORACLE))
                .sourceNode(sourceNode)
                .targetMeta(DialectMeta.getByName(DialectName.FML))
                .build();
        DialectNode dialectNode = DialectTransform.transform(param);
        assertEquals(dialectNode.getNode(), "CREATE DIM TABLE dim_shop \n"
                + "(\n"
                + "   a VARCHAR(10)\n"
                + ")");
    }

    @Test
    public void testOracleToFmlChar() {
        DialectNode sourceNode = new DialectNode(
                "CREATE TABLE dim_shop(a CHAR(10)); "
        );
        DialectTransformParam param = DialectTransformParam.builder()
                .sourceMeta(DialectMeta.getByName(DialectName.ORACLE))
                .sourceNode(sourceNode)
                .targetMeta(DialectMeta.getByName(DialectName.FML))
                .build();
        DialectNode dialectNode = DialectTransform.transform(param);
        assertEquals(dialectNode.getNode(), "CREATE DIM TABLE dim_shop \n"
                + "(\n"
                + "   a CHAR(10)\n"
                + ")");
    }

    @Test
    public void testOracleToMysql() {
        DialectNode source = new DialectNode(
                "create table dim_shop(a LONG);"
        );
        DialectTransformParam param = DialectTransformParam.builder()
                .sourceMeta(DialectMeta.getByName(DialectName.ORACLE))
                .sourceNode(source)
                .targetMeta(DialectMeta.getByName(DialectName.MYSQL))
                .build();
        DialectNode transform = DialectTransform.transform(param);
        assertEquals("CREATE TABLE dim_shop\n"
                + "(\n"
                + "   a LONGTEXT\n"
                + ")", transform.getNode());
    }
}
