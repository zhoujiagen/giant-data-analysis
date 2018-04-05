package com.spike.giantdataanalysis.task.store.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 实体基类
 * @author zhoujiagen
 * @since 2016年10月8日 下午9:20:39
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class BaseModel implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected long id;

  @Temporal(TemporalType.TIMESTAMP)
  protected Date createDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  protected Date updateDate = new Date();

  public long getId() {
    return id;
  }

  protected void setId(long id) {
    this.id = id;
  }

  public Date getCreateDate() {
    return createDate;
  }

  protected void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  protected void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BaseModel other = (BaseModel) obj;
    if (id != other.id) return false;
    return true;
  }

}
