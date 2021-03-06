/*
 *  Loc.scala
 *  (NeuralGas)
 *
 *  Copyright (c) 2018 Hanns Holger Rutz. All rights reserved.
 *
 *	This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 *	For further information, please contact Hanns Holger Rutz at
 *	contact@sciss.de
 */

package de.sciss.neuralgas.sphere

trait Loc {
  def theta     : Double
  def phi       : Double

//  def cosTheta  : Double
//  def sinTheta  : Double

  def toPolar   : Polar
}
