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

package net.momirealms.customfishing.mechanic.competition.actionbar;

import net.momirealms.customfishing.adventure.AdventureManagerImpl;
import net.momirealms.customfishing.api.CustomFishingPlugin;
import net.momirealms.customfishing.api.mechanic.competition.ActionBarConfig;
import net.momirealms.customfishing.api.scheduler.CancellableTask;
import net.momirealms.customfishing.mechanic.competition.Competition;
import net.momirealms.customfishing.setting.Locale;
import net.momirealms.customfishing.util.DynamicText;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ActionBarSender {

    private final Player player;
    private int refreshTimer;
    private int switchTimer;
    private int counter;
    private final DynamicText[] texts;
    private CancellableTask senderTask;
    private final ActionBarConfig config;
    private boolean isShown;
    private final Competition competition;
    private final HashMap<String, String> privatePlaceholders;

    public ActionBarSender(Player player, ActionBarConfig config, Competition competition) {
        this.player = player;
        this.config = config;
        this.isShown = false;
        this.competition = competition;
        this.privatePlaceholders = new HashMap<>();
        this.privatePlaceholders.put("{player}", player.getName());
        this.updatePrivatePlaceholders();

        String[] str = config.getTexts();
        texts = new DynamicText[str.length];
        for (int i = 0; i < str.length; i++) {
            texts[i] = new DynamicText(player, str[i]);
            texts[i].update(privatePlaceholders);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private void updatePrivatePlaceholders() {
        this.privatePlaceholders.put("{score}", String.format("%.2f", competition.getRanking().getPlayerScore(player.getName())));
        int rank = competition.getRanking().getPlayerRank(player.getName());
        this.privatePlaceholders.put("{rank}", rank != -1 ? String.valueOf(rank) : Locale.MSG_No_Rank);
        this.privatePlaceholders.putAll(competition.getPublicPlaceholders());
    }

    public void show() {
        this.isShown = true;
        senderTask = CustomFishingPlugin.get().getScheduler().runTaskAsyncTimer(() -> {
            switchTimer++;
            if (switchTimer > config.getSwitchInterval()) {
                switchTimer = 0;
                counter++;
            }
            if (refreshTimer < config.getRefreshRate()){
                refreshTimer++;
            } else {
                refreshTimer = 0;
                DynamicText text = texts[counter % (texts.length)];
                updatePrivatePlaceholders();
                text.update(privatePlaceholders);
                AdventureManagerImpl.getInstance().sendActionbar(
                        player,
                        AdventureManagerImpl.getInstance().legacyToMiniMessage(text.getLatestValue())
                );
            }
        }, 50, 50, TimeUnit.MILLISECONDS);
    }

    public void hide() {
        if (senderTask != null && !senderTask.isCancelled())
            senderTask.cancel();
        this.isShown = false;
    }

    public boolean isVisible() {
        return this.isShown;
    }

    public ActionBarConfig getConfig() {
        return config;
    }
}
