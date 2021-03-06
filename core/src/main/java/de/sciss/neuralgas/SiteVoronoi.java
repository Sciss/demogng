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

/**
 * A class representing a site in the Voronoi diagram
 *
 */
public class SiteVoronoi {
    /**
     * The coordinate
     */
    public PointFloat2D coord = null;

    /**
     * The number of the site
     */
    public int idx = 0;

    /**
     * The reference counter of the site
     */
    public int refCnt = 0;

    /**
     * The constructor of the SiteVoronoi class.
     */
    public SiteVoronoi() {
        coord = new PointFloat2D();
        idx = -1;
    }

    /**
     * The constructor of the SiteVoronoi class.
     *
     * @param p       The coordinates
     * @param siteNBR The identifier of the site
     */
    public SiteVoronoi(PointFloat2D p, int siteNBR) {
        coord = p;
        this.idx = siteNBR;
    }

    @Override
    public String toString() {
        return String.format("SiteVoronoi(idx: %d, coord: %s)", idx, coord);
    }
}
