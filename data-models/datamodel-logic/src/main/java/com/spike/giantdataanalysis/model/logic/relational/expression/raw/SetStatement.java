package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.UserName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.PasswordFunctionClause;

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
      StringBuilder sb = new StringBuilder();
      sb.append("SET ");
      int size = variableClauses.size();
      List<String> literals = Lists.newArrayList();
      for (int i = 0; i < size; i++) {
        literals.add(variableClauses.get(i).literal() + " = " + expressions.get(i).literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      return sb.toString();
    }

  }

  public static class SetCharset implements SetStatement {
    public final CharsetName charsetName;

    SetCharset(CharsetName charsetName) {
      this.charsetName = charsetName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SET CHARSET ");
      if (charsetName != null) {
        sb.append(charsetName.literal());
      } else {
        sb.append("DEFAULT");
      }
      return sb.toString();
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
      StringBuilder sb = new StringBuilder();
      sb.append("SET NAMES ");
      if (charsetName != null) {
        sb.append(charsetName.literal());
        if (collationName != null) {
          sb.append(" COLLATE ").append(collationName.literal());
        }
      } else {
        sb.append("DEFAULT");
      }
      return sb.toString();
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
      StringBuilder sb = new StringBuilder();
      sb.append("SET ").append(fullId.literal()).append(" = ").append(expression.literal());
      return sb.toString();
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
      StringBuilder sb = new StringBuilder();
      sb.append("SET PASSWORD ");
      if (userName != null) {
        sb.append("FOR ").append(userName.literal()).append(" ");
      }
      sb.append("= ");
      if (passwordFunctionClause != null) {
        sb.append(passwordFunctionClause.literal());
      } else {
        sb.append(password);
      }
      return sb.toString();
    }
  }
}
