/*
 *  Copyright (C) <2022> <XiaoMoMi>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.momirealms.customfishing.api.data;

import com.google.gson.annotations.SerializedName;

public class InventoryData {

    @SerializedName("inventory")
    public String serialized;

    @SerializedName("size")
    public int size;

    public InventoryData(String serialized, int size) {
        this.serialized = serialized;
        this.size = size;
    }

    public static InventoryData empty() {
        return new InventoryData("", 9);
    }
}
