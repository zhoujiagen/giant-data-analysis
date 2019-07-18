package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.UserName;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.PasswordFunctionClause;

/**
 * <pre>
 setStatement
    : SET variableClause ('=' | ':=') expression
      (',' variableClause ('=' | ':=') expression)*                 #setVariable
    | SET (CHARACTER SET | CHARSET) (charsetName | DEFAULT)         #setCharset
    | SET NAMES
        (charsetName (COLLATE collationName)? | DEFAULT)            #setNames
    | setPasswordStatement                                          #setPassword
    | setTransactionStatement                                       #setTransaction
    | setAutocommitStatement                                        #setAutocommit
    | SET fullId ('=' | ':=') expression                            #setNewValueInsideTrigger
    ;
 * </pre>
 */
public interface SetStatement extends AdministrationStatement {

  public static class SetVariable implements SetStatement {
    public final List<VariableClause> variableClauses;
    public final List<Expression> expressions;

    SetVariable(List<VariableClause> variableClauses, List<Expression> expressions) {
      Preconditions.checkArgument(variableClauses != null && variableClauses.size() > 0);
      Preconditions.checkArgument(expressions != null && expressions.size() > 0);
      Preconditions.checkArgument(variableClauses.size() == expressions.size());

      this.variableClauses = variableClauses;
      this.expressions = expressions;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  public static class SetCharset implements SetStatement {
    public final CharsetName charsetName;

    SetCharset(CharsetName charsetName) {
      this.charsetName = charsetName;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  public static class SetNames implements SetStatement {
    public final CharsetName charsetName;
    public final CollationName collationName;

    SetNames(CharsetName charsetName, CollationName collationName) {
      this.charsetName = charsetName;
      this.collationName = collationName;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  public static class SetNewValueInsideTrigger implements SetStatement {
    public final FullId fullId;
    public final Expression expression;

    SetNewValueInsideTrigger(FullId fullId, Expression expression) {
      Preconditions.checkArgument(fullId != null);
      Preconditions.checkArgument(expression != null);

      this.fullId = fullId;
      this.expression = expression;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }

  }

  /**
   * <pre>
  setPasswordStatement
    : SET PASSWORD (FOR userName)?
      '=' ( passwordFunctionClause | STRING_LITERAL)
    ;
   * </pre>
   */
  public static class SetPasswordStatement implements SetStatement {
    public final UserName userName;
    public final PasswordFunctionClause passwordFunctionClause;
    public final String password;

    SetPasswordStatement(UserName userName, PasswordFunctionClause passwordFunctionClause,
        String password) {
      Preconditions.checkArgument(!(passwordFunctionClause == null && password == null));

      this.userName = userName;
      this.passwordFunctionClause = passwordFunctionClause;
      this.password = password;
    }

    @Override
    public String literal() {
      // TODO Implement RelationalAlgebraExpression.literal
      return null;
    }
  }
}
