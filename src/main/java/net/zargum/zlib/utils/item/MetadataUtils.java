package net.zargum.zlib.utils.item;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import java.lang.reflect.Field;
import java.util.List;

public class MetadataUtils {

    public static void setMetadata(org.bukkit.inventory.ItemStack item, String metadata, Object value){
        setMetadata(CraftItemStack.asNMSCopy(item), metadata, value);
    }

    public static void setMetadata(ItemStack item, String metadata, Object value){
        NBTTagCompound tag = item.getTag();
        if(tag == null){
            tag = new NBTTagCompound();
        }
        setTag(tag, metadata, value);
        item.setTag(tag);
    }

    public static boolean hasMetadata(org.bukkit.inventory.ItemStack item, String metadata){
        return hasMetadata(CraftItemStack.asNMSCopy(item), metadata);
    }

    public static boolean hasMetadata(ItemStack item, String metadata){
        return item.getTag().hasKey(metadata);
    }

    public static Object getMetadata(org.bukkit.inventory.ItemStack item, String metadata){
        return getMetadata(CraftItemStack.asNMSCopy(item), metadata);
    }

    public static Object getMetadata(ItemStack item, String metadata){
        return getObject(item.getTag().get(metadata));
    }

    private static NBTTagCompound setTag(NBTTagCompound tag, String tagString, Object value) {

        if (value instanceof Boolean) {
            tag.setBoolean(tagString, (Boolean) value);
        } else if (value instanceof Integer) {
            tag.setInt(tagString, (Integer) value);
        } else if (value instanceof Byte) {
            tag.setByte(tagString, (Byte) value);
        } else if (value instanceof Double) {
            tag.setDouble(tagString, (Double) value);
        } else if (value instanceof Float) {
            tag.setFloat(tagString, (Float) value);
        } else if (value instanceof String) {
            tag.setString(tagString, (String) value);
        } else if (value instanceof Short) {
            tag.setShort(tagString, (Short) value);
        } else if (value instanceof Long) {
            tag.setLong(tagString, (Long) value);
        }

        return tag;
    }

    @SuppressWarnings("unchecked")
    private static Object getObject(NBTBase tag) {
        if(tag instanceof NBTTagEnd){
            return null;
        }else if(tag instanceof NBTTagByte){
            return ((NBTTagByte) tag).f();
        }else if(tag instanceof NBTTagShort){
            return ((NBTTagShort) tag).e();
        }else if(tag instanceof NBTTagInt){
            return ((NBTTagInt) tag).d();
        }else if(tag instanceof NBTTagLong){
            return ((NBTTagLong) tag).c();
        }else if(tag instanceof NBTTagFloat){
            return ((NBTTagFloat) tag).h();
        }else if(tag instanceof NBTTagDouble){
            return ((NBTTagDouble) tag).g();
        }else if(tag instanceof NBTTagByteArray){
            return ((NBTTagByteArray) tag).c();
        }else if(tag instanceof NBTTagString){
            return ((NBTTagString) tag).a_();
        }else if(tag instanceof NBTTagList){
            List<NBTBase> list = null;
            try {
                Field field = tag.getClass().getDeclaredField("list");
                field.setAccessible(true);
                list = (List<NBTBase>)field.get(tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(list == null)return null;
            List<Object> toReturn = Lists.newArrayList();
            for(NBTBase base : list){
                toReturn.add(getObject(base));
            }
            return toReturn;
        }else if(tag instanceof NBTTagCompound){
            return tag;
        }else if(tag instanceof NBTTagIntArray){
            return ((NBTTagIntArray) tag).c();
        }
        return null;
    }

}
