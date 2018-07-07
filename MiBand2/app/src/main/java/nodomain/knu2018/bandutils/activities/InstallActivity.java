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
package nodomain.knu2018.bandutils.activities;

import nodomain.knu2018.bandutils.model.ItemWithDetails;

/**
 * The interface Install activity.
 */
public interface InstallActivity {
    /**
     * Gets info text.
     *
     * @return the info text
     */
    CharSequence getInfoText();

    /**
     * Sets info text.
     *
     * @param text the text
     */
    void setInfoText(String text);

    /**
     * Sets install enabled.
     *
     * @param enable the enable
     */
    void setInstallEnabled(boolean enable);

    /**
     * Clear install items.
     */
    void clearInstallItems();

    /**
     * Sets install item.
     *
     * @param item the item
     */
    void setInstallItem(ItemWithDetails item);

}
