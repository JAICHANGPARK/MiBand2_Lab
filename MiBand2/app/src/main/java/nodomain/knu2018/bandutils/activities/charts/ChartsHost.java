/*  Copyright (C) 2015-2018 Andreas Shimokawa, Carsten Pfeiffer

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. */
package nodomain.knu2018.bandutils.activities.charts;

import android.view.ViewGroup;

import java.util.Date;

import nodomain.knu2018.bandutils.impl.GBDevice;

/**
 * The interface Charts host.
 */
public interface ChartsHost {
    /**
     * The constant DATE_PREV.
     */
    String DATE_PREV = ChartsActivity.class.getName().concat(".date_prev");
    /**
     * The constant DATE_NEXT.
     */
    String DATE_NEXT = ChartsActivity.class.getName().concat(".date_next");
    /**
     * The constant REFRESH.
     */
    String REFRESH = ChartsActivity.class.getName().concat(".refresh");

    /**
     * Gets device.
     *
     * @return the device
     */
    GBDevice getDevice();

    /**
     * Sets start date.
     *
     * @param startDate the start date
     */
    void setStartDate(Date startDate);

    /**
     * Sets end date.
     *
     * @param endDate the end date
     */
    void setEndDate(Date endDate);

    /**
     * Gets start date.
     *
     * @return the start date
     */
    Date getStartDate();

    /**
     * Gets end date.
     *
     * @return the end date
     */
    Date getEndDate();

    /**
     * Sets date info.
     *
     * @param dateInfo the date info
     */
    void setDateInfo(String dateInfo);

    /**
     * Gets date bar.
     *
     * @return the date bar
     */
    ViewGroup getDateBar();
}
