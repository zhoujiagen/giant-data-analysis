package com.spike.giantdataanalysis.commons.guava.eventbus.domain;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;

import com.google.common.base.MoreObjects;
import com.spike.giantdataanalysis.commons.guava.cache.domain.ExampleCacheTradeAccount;

/**
 * <pre>
 * 账户交易事件
 * </pre>
 *
 * @author zhoujiagen
 */
public class ExampleTradeAccountEvent implements ExampleBaseTradeEvent {
  private double amount;
  private Date tradeExecutionTime;
  private ExampleTradeType tradeType;
  private ExampleCacheTradeAccount tradeAccount;

  public ExampleTradeAccountEvent(double amount, Date tradeExecutionTime,
      ExampleTradeType tradeType, ExampleCacheTradeAccount tradeAccount) {
    checkArgument(amount > 0.0, "Trade can't be less than zero");
    this.amount = amount;

    this.tradeExecutionTime = checkNotNull(tradeExecutionTime, "ExecutionTime can't be null");
    this.tradeType = checkNotNull(tradeType, "TradeType can't be null");
    this.tradeAccount = checkNotNull(tradeAccount, "Account can't be null");
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public Date getTradeExecutionTime() {
    return tradeExecutionTime;
  }

  public void setTradeExecutionTime(Date tradeExecutionTime) {
    this.tradeExecutionTime = tradeExecutionTime;
  }

  public ExampleTradeType getTradeType() {
    return tradeType;
  }

  public void setTradeType(ExampleTradeType tradeType) {
    this.tradeType = tradeType;
  }

  public ExampleCacheTradeAccount getTradeAccount() {
    return tradeAccount;
  }

  public void setTradeAccount(ExampleCacheTradeAccount tradeAccount) {
    this.tradeAccount = tradeAccount;
  }

  @Override
  public String toString() {

    return MoreObjects.toStringHelper(this)//
        .omitNullValues()//
        .add("amount", amount)//
        .add("tradeExecutionTime", tradeExecutionTime)//
        .add("tradeType", tradeType)//
        .add("tradeAccount", tradeAccount.toString())//
        .toString();
  }
}
