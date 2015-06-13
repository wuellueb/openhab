/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.DecimalValue;



/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MServo</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.3.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getVelocity <em>Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getAcceleration <em>Acceleration</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getMaxPosition <em>Max Position</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getMinPosition <em>Min Position</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPulseWidthMin <em>Pulse Width Min</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPulseWidthMax <em>Pulse Width Max</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPeriod <em>Period</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getOutputVoltage <em>Output Voltage</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getTargetPosition <em>Target Position</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MSensor<org.openhab.binding.tinkerforge.internal.model.MDecimalValue> org.openhab.binding.tinkerforge.internal.model.ProgrammableSwitchActor org.openhab.binding.tinkerforge.internal.model.MSubDevice<org.openhab.binding.tinkerforge.internal.model.MBrickServo> org.openhab.binding.tinkerforge.internal.model.MoveActor org.openhab.binding.tinkerforge.internal.model.SetPointActor<org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration>"
 * @generated
 */
public interface MServo extends MSensor<DecimalValue>, ProgrammableSwitchActor, MSubDevice<MBrickServo>, MoveActor, SetPointActor<TFServoConfiguration>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"servo"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_DeviceType()
   * @model default="servo" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Velocity</b></em>' attribute.
   * The default value is <code>"65535"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Velocity</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Velocity</em>' attribute.
   * @see #setVelocity(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_Velocity()
   * @model default="65535" unique="false"
   * @generated
   */
  int getVelocity();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getVelocity <em>Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Velocity</em>' attribute.
   * @see #getVelocity()
   * @generated
   */
  void setVelocity(int value);

  /**
   * Returns the value of the '<em><b>Acceleration</b></em>' attribute.
   * The default value is <code>"65535"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Acceleration</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Acceleration</em>' attribute.
   * @see #setAcceleration(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_Acceleration()
   * @model default="65535" unique="false"
   * @generated
   */
  int getAcceleration();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getAcceleration <em>Acceleration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Acceleration</em>' attribute.
   * @see #getAcceleration()
   * @generated
   */
  void setAcceleration(int value);

  /**
   * Returns the value of the '<em><b>Max Position</b></em>' attribute.
   * The default value is <code>"9000"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Max Position</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Max Position</em>' attribute.
   * @see #setMaxPosition(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_MaxPosition()
   * @model default="9000" unique="false"
   * @generated
   */
  Short getMaxPosition();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getMaxPosition <em>Max Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Max Position</em>' attribute.
   * @see #getMaxPosition()
   * @generated
   */
  void setMaxPosition(Short value);

  /**
   * Returns the value of the '<em><b>Min Position</b></em>' attribute.
   * The default value is <code>"-9000"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Min Position</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Min Position</em>' attribute.
   * @see #setMinPosition(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_MinPosition()
   * @model default="-9000" unique="false"
   * @generated
   */
  Short getMinPosition();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getMinPosition <em>Min Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Min Position</em>' attribute.
   * @see #getMinPosition()
   * @generated
   */
  void setMinPosition(Short value);

  /**
   * Returns the value of the '<em><b>Pulse Width Min</b></em>' attribute.
   * The default value is <code>"1000"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pulse Width Min</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pulse Width Min</em>' attribute.
   * @see #setPulseWidthMin(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_PulseWidthMin()
   * @model default="1000" unique="false"
   * @generated
   */
  int getPulseWidthMin();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPulseWidthMin <em>Pulse Width Min</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pulse Width Min</em>' attribute.
   * @see #getPulseWidthMin()
   * @generated
   */
  void setPulseWidthMin(int value);

  /**
   * Returns the value of the '<em><b>Pulse Width Max</b></em>' attribute.
   * The default value is <code>"2000"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pulse Width Max</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pulse Width Max</em>' attribute.
   * @see #setPulseWidthMax(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_PulseWidthMax()
   * @model default="2000" unique="false"
   * @generated
   */
  int getPulseWidthMax();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPulseWidthMax <em>Pulse Width Max</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pulse Width Max</em>' attribute.
   * @see #getPulseWidthMax()
   * @generated
   */
  void setPulseWidthMax(int value);

  /**
   * Returns the value of the '<em><b>Period</b></em>' attribute.
   * The default value is <code>"19500"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Period</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Period</em>' attribute.
   * @see #setPeriod(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_Period()
   * @model default="19500" unique="false"
   * @generated
   */
  int getPeriod();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPeriod <em>Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Period</em>' attribute.
   * @see #getPeriod()
   * @generated
   */
  void setPeriod(int value);

  /**
   * Returns the value of the '<em><b>Output Voltage</b></em>' attribute.
   * The default value is <code>"5000"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Output Voltage</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Output Voltage</em>' attribute.
   * @see #setOutputVoltage(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_OutputVoltage()
   * @model default="5000" unique="false"
   * @generated
   */
  int getOutputVoltage();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getOutputVoltage <em>Output Voltage</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Output Voltage</em>' attribute.
   * @see #getOutputVoltage()
   * @generated
   */
  void setOutputVoltage(int value);

  /**
   * Returns the value of the '<em><b>Target Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Target Position</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Target Position</em>' attribute.
   * @see #setTargetPosition(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_TargetPosition()
   * @model unique="false"
   * @generated
   */
  short getTargetPosition();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getTargetPosition <em>Target Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Target Position</em>' attribute.
   * @see #getTargetPosition()
   * @generated
   */
  void setTargetPosition(short value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model annotation="http://www.eclipse.org/emf/2002/GenModel body=''"
   * @generated
   */
  void init();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model unique="false" positionUnique="false" velocityUnique="false" accelerationUnique="false"
   * @generated
   */
  boolean setPoint(Short position, int velocity, int acceleration);

} // MServo
