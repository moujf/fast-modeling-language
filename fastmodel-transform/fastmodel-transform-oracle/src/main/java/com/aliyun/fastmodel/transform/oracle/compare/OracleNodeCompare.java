package com.aliyun.fastmodel.transform.oracle.compare;

import com.aliyun.fastmodel.compare.CompareNodeExecute;
import com.aliyun.fastmodel.core.tree.BaseStatement;
import com.aliyun.fastmodel.transform.api.compare.CompareContext;
import com.aliyun.fastmodel.transform.api.compare.CompareResult;
import com.aliyun.fastmodel.transform.api.compare.NodeCompare;
import com.aliyun.fastmodel.transform.api.dialect.Dialect;
import com.aliyun.fastmodel.transform.api.dialect.DialectName;
import com.aliyun.fastmodel.transform.api.dialect.DialectNode;
import com.aliyun.fastmodel.transform.oracle.Oracle11Transformer;
import com.google.auto.service.AutoService;

import java.util.List;

/**
 * OracleNodeCompare
 *
 * @author moujf <br>
 * @version 1.0 <br>
 * @date 2022/9/2 17:37 <br>
 */
@Dialect(DialectName.ORACLE)
@AutoService(NodeCompare.class)
public class OracleNodeCompare  implements NodeCompare {

    private Oracle11Transformer oracle11Transformer = new Oracle11Transformer();

    @Override
    public CompareResult compareResult(DialectNode before, DialectNode after, CompareContext context) {
        BaseStatement beforeStatement = (before != null && before.isExecutable()) ? oracle11Transformer.reverse(before) : null;
        BaseStatement afterStatement = (after != null && after.isExecutable()) ? oracle11Transformer.reverse(after) : null;
        List<BaseStatement> compare = CompareNodeExecute.getInstance().compare(beforeStatement, afterStatement,
                context.getCompareStrategy());
        return new CompareResult(beforeStatement, afterStatement, compare);
    }
}
