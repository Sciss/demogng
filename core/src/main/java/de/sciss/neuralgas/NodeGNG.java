// ========================================================================== ;
//                                                                            ;
// Copyright 1996-1998 Hartmut S. Loos, Instit. f. Neuroinformatik, Bochum    ;
// Copyright 2012-2013 Bernd Fritzke                                          ;
//                                                                            ;
// This program is free software; you can redistribute it and/or modify       ;
// it under the terms of the GNU General Public License as published by       ;
// the Free Software Foundation; either version 1, or (at your option)        ;
// any later version.                                                         ;
//                                                                            ;
// This program is distributed in the hope that it will be useful,            ;
// but WITHOUT ANY WARRANTY; without even the implied warranty of             ;
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              ;
// GNU General Public License for more details.                               ;
//                                                                            ;
// You should have received a copy of the GNU General Public License          ;
// along with this program; if not, write to the Free Software                ;
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.                  ;
//                                                                            ;
// ========================================================================== ;

package de.sciss.neuralgas;

import java.util.*;

/**
 * A class representing a node for all neural algorithms.
 *
 */
public class NodeGNG {
    final int TRACE_LEN = 100;

    float traceX[] = new float[TRACE_LEN];
    float traceY[] = new float[TRACE_LEN];
    int traceIdx=0;
    int cntSig=0;
    Vector<Float> tr= new Vector<Float>(TRACE_LEN*2);

    public void adapt(float dx, float dy){
        if (traceIdx==0){
            traceX[0]=x;
            traceY[0]=y;
            traceIdx++;
        }
        // store old values in trace
        traceX[traceIdx]=x;
        traceY[traceIdx]=y;
        // increment traceIndex
        traceIdx+=1;
        traceIdx=traceIdx%TRACE_LEN;
        // apply deltas
        x += dx;
        y += dy;
        cntSig++;
    }
    // returns trace as array x,y,x-1,y-1 ....
    public Vector<Float> getTrace() {
        tr.clear();
        if (cntSig==0)
            return tr;
        for (int i=1;i<=Math.min(cntSig, TRACE_LEN);i++){
            tr.add(traceX[(traceIdx-i+TRACE_LEN)%TRACE_LEN]);
            tr.add(traceY[(traceIdx-i+TRACE_LEN)%TRACE_LEN]);
        }
        return tr;
    }
    public NodeGNG() {
        traceIdx=0;
    }
    /**
     * The maximum number of neighbors of a node
     */
    public final int MAX_NEIGHBORS = 10;
    /**
     * The flag for mouse-selection
     */
    public boolean isMouseSelected = false;
    /**
     * The flag for the winner.
     *  This node is nearest to the input signal.
     */
    public boolean isWinner = false;
    /**
     * The flag for the second winner.
     *  This node is second nearest to the input signal.
     */
    public boolean isSecond = false;
    /**
     * The flag for movement.
     *  This node moved from the last position (GNG, LBG).
     */
    public boolean hasMoved = false;
    /**
     * The flag for insertion.
     *  This node was inserted last (GNG).
     */
    public boolean isMostRecentlyInserted = false;

    /**
     * The x index in the grid (GG, SOM).
     */
    public int x_grid = -1;
    /**
     * The y index in the grid (GG, SOM).
     */
    public int y_grid = -1;
    /**
     * The x-position of the node.
     */
    public float x = 0.0f;
    /**
     * The old x-position of the moved node.
     */
    protected float x_old = 0.0f;
    /**
     * The y-position of the node.
     */
    public float y = 0.0f;
    /**
     * The old y-position of the moved node.
     */
    protected float y_old = 0.0f;
    /**
     * The error of the node.
     */
    public float error = 0.0f;
    /**
     * for GG tau counter
     */
    public float tau = 0.0f;
    /**
     * The distance from the input signal.
     */
    public float sqrDist = Float.MAX_VALUE;
    /**
     * The utility for GNG-U and LBG-U
     */
    public float utility = 0.0f;
    /**
     * The number of neighbors.
     */
    protected int nNeighbor = 0;
    /**
     * The list of neighbor cells.
     */
    public  int neighbor[] = new int[MAX_NEIGHBORS];
    /**
     * The list of neighboring signals (specified by their number).
     */
    protected Vector<Integer> signals = new Vector<Integer>();

    @Override
    public String toString() {
        return String.format("Node(x: %f, y: %f, error: %f, sqrDist: %f, utility: %f, nNeighbor: %d, isWinner: %b, isSecond: %b, hasMoved %b",
                x, y, error, sqrDist, utility, nNeighbor, isWinner, isSecond, hasMoved);
    }

    public void assignVector(NodeGNG source){
        x = source.x;
        y = source.y;
    }
    public void subtractVector(NodeGNG source){
        x -= source.x;
        y -= source.y;
    }
    public void addVector(NodeGNG source){
        x += source.x;
        y += source.y;
    }
    /**
     * Return number of neighboring numSignals.
     *
     * @return		number of numSignals
     */
    public int numSignals() {
        return(signals.size());
    }

    /**
     * Add a signal index.
     *
     * @param sig		The index of the signal
     */
    public void addSignal(int sig) {
        signals.addElement( new Integer(sig) );
    }

    /**
     * Remove a signal index and return the index.
     *
     * @return		The index of the signal or -1.
     */
    public int removeSignal() {
        int size = signals.size();
        if (size < 1)
            return(-1);

        // remove last element from the vector and return it
        Integer lastSignal = signals.lastElement();
        signals.removeElementAt(size-1);
        return(lastSignal.intValue());
    }

    /**
     * Return the number of neighbors.
     *
     * @return	Number of neighbors
     */
    public int numNeighbors() {
        return nNeighbor;
    }

    /**
     * Is there space for more neighbors?
     *
     * @return	Space enough?
     */
    public boolean moreNeighbors() {
        return (MAX_NEIGHBORS != nNeighbor);
    }

    /**
     * Returns the i-th neighbor.
     *
     * @param i	The index of a neighbor
     * @return	The index of a node
     */
    public int neighbor(int i) {
        return neighbor[i];
    }

    /**
     * Deletes the node from the list of neighbors.
     *
     * @param node	The index of a node
     */
    public void deleteNeighbor(int node) {
        for (int i = 0; i < nNeighbor; i++) {
            if (node == neighbor[i]) {
                nNeighbor--;
                neighbor[i] = neighbor[nNeighbor];
                neighbor[nNeighbor] = -1;
                return;
            }
        }
    }

    /**
     * Replaces the old node with a new node.
     *
     * @param old		The index of a node
     * @param newN	The index of a node
     * @see ComputeGNG#deleteNode
     */
    public void replaceNeighbor(int old, int newN) {
        for (int i = 0; i < nNeighbor; i++) {
            if (old == neighbor[i]) {
                neighbor[i] = newN;
                return;
            }
        }
    }

    /**
     * Is the node a neighbor?
     *
     * @param n		The index of a node
     * @return		Neighbor?
     */
    public boolean isNeighbor(int n) {
        for (int i = 0; i < nNeighbor; i++)
            if (n == neighbor[i])
                return true;
        return false;
    }

    /**
     * Add a node to the neighborhood.
     *
     * @param node	The index of a node
     */
    public void addNeighbor(int node) {
        if (nNeighbor == MAX_NEIGHBORS)
            return;

        neighbor[nNeighbor] = node;
        nNeighbor++;
    }
}
