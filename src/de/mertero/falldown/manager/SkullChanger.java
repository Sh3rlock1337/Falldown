package de.mertero.falldown.manager;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public class SkullChanger {
    public static Class<?> skullMetaClass;
    public static Class<?> tileEntityClass;
    public static Class<?> blockPositionClass;

    static {
        String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            skullMetaClass = Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftMetaSkull");

            tileEntityClass = Class.forName("net.minecraft.server." + version + ".TileEntitySkull");

            blockPositionClass = Class.forName("net.minecraft.server." + version + ".BlockPosition");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ItemStack getSkull(Player player) {
        return getSkull(getSignature(player), getValue(player), 1);
    }

    public static ItemStack getSkull(String signature, String value) {
        return getSkull(signature, value, 1);
    }

    public static ItemStack getSkull(String signature, String value, int amount) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        try {
            Field profileField = skullMetaClass.getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, getProfile(signature, value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        skull.setItemMeta(meta);
        return skull;
    }

    public static boolean setBlock(Location loc, String signature, String value) {
        return setBlock(loc.getBlock(), signature, value);
    }

    public static boolean setBlock(Block block, String signature, String value) {
        if ((block.getType() != Material.SKULL) && (block.getType() != Material.SKULL_ITEM)) {
            block.setType(Material.SKULL);
        }
        try {
            Object nmsWorld = block.getWorld().getClass().getMethod("getHandle", new Class[0]).invoke(block.getWorld(), new Object[0]);

            Object tileEntity = null;

            Method getTileEntity = nmsWorld.getClass().getMethod("getTileEntity", new Class[]{blockPositionClass});

            tileEntity = tileEntityClass.cast(getTileEntity.invoke(nmsWorld, new Object[]{getBlockPositionFor(block.getX(), block.getY(), block.getZ())}));


            tileEntityClass.getMethod("setGameProfile", new Class[]{GameProfile.class}).invoke(tileEntity, new Object[]{getProfile(signature, value)});
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getSignature(Player p) {
        try {
            return getSignature((GameProfile) p.getClass().getMethod("getProfile", new Class[0]).invoke(p, new Object[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSignature(GameProfile gp) {
        Property textures = (Property) gp.getProperties().get("textures").iterator().next();
        return textures.getSignature();
    }

    public static String getValue(Player p) {
        try {
            return getValue((GameProfile) p.getClass().getMethod("getProfile", new Class[0]).invoke(p, new Object[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getValue(GameProfile gp) {
        Property textures = (Property) gp.getProperties().get("textures").iterator().next();
        return textures.getValue();
    }

    private static GameProfile getProfile(String signature, String value) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        Property property = new Property("textures", value, signature);
        profile.getProperties().put("textures", property);
        return profile;
    }

    private static Object getBlockPositionFor(int x, int y, int z) {
        Object blockPosition = null;
        try {
            Constructor<?> cons = blockPositionClass.getConstructor(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});

            blockPosition = cons.newInstance(new Object[]{Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blockPosition;
    }
}
