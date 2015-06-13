/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.types;

import java.math.BigDecimal;

/**
 * The decimal value uses a BigDecimal internally and thus can be used for integers, longs and
 * floating point numbers alike (inspired by {@link org.openhab.core.library.types.DecimalType}).
 * 
 * @author Theo Weiss
 * @since 1.4.0
 */
public class DecimalValue extends Number implements TinkerforgeValue, Comparable<DecimalValue> {
  private static final long serialVersionUID = -165912774170068480L;

  final static public DecimalValue ZERO = new DecimalValue(0);

  protected BigDecimal value;

  public DecimalValue() {
    this.value = BigDecimal.ZERO;
  }

  public DecimalValue(BigDecimal value) {
    this.value = value;
  }

  public DecimalValue(long value) {
    this.value = new BigDecimal(value);
  }

  public DecimalValue(double value) {
    this.value = new BigDecimal(value);
  }

  public DecimalValue(String value) {
    this.value = new BigDecimal(value);
  }

  public String toString() {
    return value.toPlainString();
  }

  public static DecimalValue valueOf(String value) {
    return new DecimalValue(value);
  }

  public static DecimalValue valueOf(BigDecimal value) {
    return new DecimalValue(value);
  }

  @Override
  public int intValue() {
    return value.intValue();
  }

  @Override
  public long longValue() {
    return value.longValue();
  }

  @Override
  public float floatValue() {
    return value.floatValue();
  }

  @Override
  public double doubleValue() {
    return value.doubleValue();
  }

  public BigDecimal bigDecimalValue() {
    return value;
  }

  public OnOffValue onOffValue(int scale) {
    BigDecimal scaledvalue = value.setScale(0, BigDecimal.ROUND_UP);
    return scaledvalue.compareTo(BigDecimal.ZERO) == 0 ? OnOffValue.OFF : OnOffValue.ON;
  }

  @Override
  public int compareTo(DecimalValue o) {
    return value.compareTo(o.toBigDecimal());
  }

  /*
   * Compare two values taking a threshold into account.
   * 
   * If the absolute difference between the values is less then threshold 0 is returned elsewhere
   * the values are compared using the compareTo function of BigDecimal.
   */
  public int compareTo(DecimalValue o, BigDecimal threshold) {
    if (o == null) {
      return 1;
    } else if (threshold == null) {
      return value.compareTo(o.toBigDecimal().abs());
    } else if (o != null && threshold != null) {
      BigDecimal difference = value.subtract(o.toBigDecimal()).abs();
      if (difference.compareTo(threshold) < 0) {
        return 0;
      }
      return value.compareTo(o.toBigDecimal().abs());
    }
    return 0;
  }

  private BigDecimal toBigDecimal() {
    return value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    DecimalValue other = (DecimalValue) obj;
    if (value == null) {
      if (other.value != null) return false;
    } else if (!value.equals(other.value)) return false;
    return true;
  }
}
