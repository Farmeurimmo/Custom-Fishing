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

package net.momirealms.customfishing.gui.icon.property.item;

import net.momirealms.customfishing.adventure.AdventureHelper;
import net.momirealms.customfishing.adventure.component.ShadedAdventureComponentWrapper;
import net.momirealms.customfishing.gui.SectionPage;
import net.momirealms.customfishing.setting.CFLocale;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class TagItem extends AbstractItem {

    private final SectionPage itemPage;

    public TagItem(SectionPage itemPage) {
        this.itemPage = itemPage;
    }

    @Override
    public ItemProvider getItemProvider() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.TOTEM_OF_UNDYING)
                .setDisplayName(new ShadedAdventureComponentWrapper(AdventureHelper.getInstance().getComponentFromMiniMessage(
                        CFLocale.GUI_ITEM_TAG
                )));
        itemBuilder.addLoreLines(new ShadedAdventureComponentWrapper(AdventureHelper.getInstance().getComponentFromMiniMessage(
                        CFLocale.GUI_CURRENT_VALUE + itemPage.getSection().getBoolean("tag", true)
                )))
                .addLoreLines("")
                .addLoreLines(new ShadedAdventureComponentWrapper(AdventureHelper.getInstance().getComponentFromMiniMessage(
                        CFLocale.GUI_CLICK_TO_TOGGLE
                )));
        return itemBuilder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        itemPage.getSection().set("tag", !itemPage.getSection().getBoolean("tag", true));
        itemPage.save();
        itemPage.reOpen();
    }
}
