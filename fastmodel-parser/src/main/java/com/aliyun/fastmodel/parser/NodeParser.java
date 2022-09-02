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

package com.aliyun.fastmodel.parser;

import java.util.List;
import java.util.function.Function;

import com.aliyun.fastmodel.common.parser.ThrowingErrorListener;
import com.aliyun.fastmodel.common.parser.lexer.CaseChangingCharStream;
import com.aliyun.fastmodel.core.exception.ParseException;
import com.aliyun.fastmodel.core.parser.DomainLanguage;
import com.aliyun.fastmodel.core.parser.FastModelParser;
import com.aliyun.fastmodel.core.parser.LanguageParser;
import com.aliyun.fastmodel.core.tree.BaseStatement;
import com.aliyun.fastmodel.core.tree.Node;
import com.aliyun.fastmodel.core.tree.datatype.BaseDataType;
import com.aliyun.fastmodel.core.tree.expr.BaseExpression;
import com.aliyun.fastmodel.core.tree.expr.atom.TableOrColumn;
import com.aliyun.fastmodel.core.tree.statement.CompositeStatement;
import com.aliyun.fastmodel.parser.generate.FastModelGrammarParser;
import com.aliyun.fastmodel.parser.generate.FastModelLexer;
import com.aliyun.fastmodel.parser.visitor.AnnotationProcessVisitor;
import com.aliyun.fastmodel.parser.visitor.AstExtractVisitor;
import com.google.auto.service.AutoService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

/**
 * antlr实现内容
 *
 * @author panguanjing
 * @date 2020/9/3
 */
@AutoService(LanguageParser.class)
@Slf4j
public class NodeParser implements FastModelParser {

    public static final ThrowingErrorListener LISTENER = new ThrowingErrorListener();

    @Override
    public BaseStatement parse(DomainLanguage domainLanguage) throws ParseException {
        return (BaseStatement)invokerParser(domainLanguage.getText(), FastModelGrammarParser::sqlStatement);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BaseStatement> T parseStatement(String text) throws ParseException {
        return (T)invokerParser(text, FastModelGrammarParser::root);
    }

    @Override
    public List<BaseStatement> multiParse(DomainLanguage script) throws ParseException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Function<FastModelGrammarParser, ParserRuleContext> parseFunction =  FastModelGrammarParser::root;
        log.info("multiParse第{}步运行时间：{}", 1, stopWatch.getTime());
        // 设置split停止标记
        stopWatch.reset();
        //重置后必须使用start方法
        stopWatch.start();
        Node node = invokerParser(script.getText(), parseFunction);
        log.info("multiParse第{}步运行时间：{}", 2, stopWatch.getTime());
        // 设置split停止标记
        stopWatch.reset();
        //重置后必须使用start方法
        stopWatch.start();
        if (node instanceof CompositeStatement) {
            CompositeStatement compositeStatement = (CompositeStatement)node;
            return compositeStatement.getStatements();
        }
        stopWatch.stop();
        log.info("multiParse第{}步运行时间：{}", 3, stopWatch.getTime());
        return Lists.newArrayList((BaseStatement)node);
    }

    @Override
    public BaseExpression parseExpr(DomainLanguage expr) throws ParseException {
        return (BaseExpression)invokerParser(expr.getText(), FastModelGrammarParser::expression);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T parseExpression(String text) throws ParseException {
        return (T)invokerParser(text, FastModelGrammarParser::expression);
    }

    @Override
    public BaseDataType parseDataType(DomainLanguage dataTypeExpr) throws ParseException {
        return (BaseDataType)invokerParser(dataTypeExpr.getText(), FastModelGrammarParser::typeDbCol);
    }

    @Override
    public List<TableOrColumn> extract(DomainLanguage domainLanguage) throws ParseException {
        BaseExpression node = (BaseExpression)invokerParser(domainLanguage.getText(),
            FastModelGrammarParser::expression);
        AstExtractVisitor exprExtractVisitor = new AstExtractVisitor();
        exprExtractVisitor.process(node, null);
        return exprExtractVisitor.getTableOrColumnList();
    }

    private Node invokerParser(String dsl,
                               Function<FastModelGrammarParser, ParserRuleContext> parseFunction) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        if (StringUtils.isBlank(dsl)) {
            throw new ParseException("dsl can't be blank");
        }
        CodePointCharStream input = CharStreams.fromString(dsl);
        CaseChangingCharStream charStream = new CaseChangingCharStream(input, true);
        FastModelLexer lexer = new FastModelLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(LISTENER);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        FastModelGrammarParser fastModelGrammarParser = new FastModelGrammarParser(commonTokenStream);
        fastModelGrammarParser.removeErrorListeners();
        fastModelGrammarParser.addErrorListener(LISTENER);
        log.info("invokerParser第{}步运行时间：{}", 1, stopWatch.getTime());
        // 设置split停止标记
        stopWatch.reset();
        //重置后必须使用start方法
        stopWatch.start();
        ParserRuleContext tree;
        try {
            fastModelGrammarParser.getInterpreter().setPredictionMode(PredictionMode.SLL);
            tree = parseFunction.apply(fastModelGrammarParser);
        } catch (Throwable e) {
            commonTokenStream.seek(0);
            fastModelGrammarParser.reset();
            fastModelGrammarParser.getInterpreter().setPredictionMode(PredictionMode.LL);
            tree = parseFunction.apply(fastModelGrammarParser);
        }
        log.info("invokerParser第{}步运行时间：{}", 2, stopWatch.getTime());
        // 设置split停止标记
        stopWatch.reset();
        //重置后必须使用start方法
        stopWatch.start();
        Node node = new AnnotationProcessVisitor().visit(tree);
        stopWatch.stop();
        log.info("invokerParser第{}步运行时间：{}", 3, stopWatch.getTime());
        return node;
    }
}
//
//DialectTransform.transform(DialectTransformParam)  (com.aliyun.fastmodel.transform.api.dialect.transform)
//
//        FmlTransformer.reverse(DialectNode, ReverseContext)  (com.aliyun.fastmodel.transform.fml) 1435
//        NodeParser.multiParse(DomainLanguage)  (com.aliyun.fastmodel.parser)
//        FastModelGrammarParser::root 603
//        NodeParser.invokerParser(String, Function<FastModelGrammarParser, ParserRuleContext>)  (com.aliyun.fastmodel.parser) 791
//        new AnnotationProcessVisitor().visit(tree) 646