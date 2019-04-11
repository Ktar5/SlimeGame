package com.ktar5.gameengine.cooldown;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;
import com.ktar5.gameengine.util.Updatable;

import java.util.*;

public class CooldownManager implements Updatable {

    private final ObjectMap<UUID, HashMap<UUID, Cooldown>> cooldowns;

    public CooldownManager() {
        cooldowns = new ObjectMap<>();
    }

    private Optional<HashMap<UUID, Cooldown>> getCooldowns(UUID entity) {
        if (cooldowns.containsKey(entity)) {
            return Optional.of(cooldowns.get(entity));
        } else {
            return Optional.empty();
        }
    }


    /**
     * @return 0 if complete, -1 if not exist
     */
    public int timeLeft(UUID entity, CooldownType cooldown) {
        Optional<HashMap<UUID, Cooldown>> opt = getCooldowns(entity);
        if (opt.isPresent() && opt.get().containsKey(cooldown.getId())) {
            int i = MathUtils.clamp(opt.get().get(cooldown.getId()).timeLeft(), 0, Integer.MAX_VALUE);
            if (i <= 0) {
                removeCooldown(entity, cooldown);
                return 0;
            }
            return i;
        }
        return -1;
    }

    public boolean isCooling(UUID entity, CooldownType cooldown) {
        Optional<HashMap<UUID, Cooldown>> opt = getCooldowns(entity);
        if (opt.isPresent() && opt.get().containsKey(cooldown.getId())) {
            if (opt.get().get(cooldown.getId()).isFinished()) {
                removeCooldown(entity, cooldown);
                return false;
            }
            return true;
        }
        return false;
    }

    public void addCooldown(UUID entity, CooldownType cooldown, int miliseconds) {
        Optional<HashMap<UUID, Cooldown>> opt = getCooldowns(entity);
        if (opt.isPresent() && opt.get().containsKey(cooldown.getId())) {
            removeCooldown(entity, cooldown);
        } else {
            Cooldown cool = Pools.obtain(Cooldown.class);
            cool.start(miliseconds);
            HashMap<UUID, Cooldown> uuidCooldownHashMap = new HashMap<>();
            uuidCooldownHashMap.put(cooldown.getId(), cool);
            cooldowns.put(entity, uuidCooldownHashMap);
        }
    }

    public void removeCooldown(UUID entity, CooldownType cooldown) {
        Optional<HashMap<UUID, Cooldown>> opt = getCooldowns(entity);
        if (opt.isPresent() && opt.get().containsKey(cooldown.getId())) {
            Pools.free(cooldowns.get(entity).remove(cooldown.getId()));
        }
    }

    @Override
    public void update(float dTime) {
        for (ObjectMap.Entry<UUID, HashMap<UUID, Cooldown>> cooldown : cooldowns.iterator()) {
            final Iterator<Map.Entry<UUID, Cooldown>> iterator = cooldown.value.entrySet().iterator();
            while (iterator.hasNext()) {
                final Cooldown value = iterator.next().getValue();
                if (value.isFinished()) {
                    iterator.remove();
                    Pools.free(value);
                }
            }
        }
    }
}
