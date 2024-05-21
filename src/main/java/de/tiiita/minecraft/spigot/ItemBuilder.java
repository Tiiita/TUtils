package de.tiiita.minecraft.spigot;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class ItemBuilder {

    protected ItemStack stack;
    protected ItemMeta meta;

    public ItemBuilder(Material type) {
        stack = new ItemStack(type);
        meta = stack.getItemMeta();
    }

    public ItemBuilder(Material type, int amount) {
        stack = new ItemStack(type, amount);
        meta = stack.getItemMeta();
    }

    public ItemBuilder(Material type, List<String> lore) {
        stack = new ItemStack(type);
        meta = stack.getItemMeta();
        meta.setLore(lore);
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder(int typeID, int amount, int subID) {
        stack = new ItemStack(typeID, amount, (short) subID);
        meta = stack.getItemMeta();
    }

    public ItemBuilder(Material material, int amount, int subID) {
        stack = new ItemStack(material, amount, (short) subID);
        meta = stack.getItemMeta();
    }

    public ItemBuilder(ItemStack stack) {
        this.stack = stack.clone();
        meta = stack.getItemMeta().clone();
    }

    public ItemBuilder() {
        this(Material.AIR);
    }

    public ItemBuilder enchant(Enchantment ench, int level) {
        meta.addEnchant(ench, level, true);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            meta.addEnchant(entry.getKey(), entry.getValue(), true);
        }
        return this;
    }

    public ItemBuilder hideEnchants() {
        flag(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench) {
        meta.removeEnchant(ench);
        return this;
    }

    public ItemBuilder clearEnchantments() {
        meta.getEnchants().forEach((enchantment, integer) -> meta.removeEnchant(enchantment));
        return this;
    }

    public ItemBuilder amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemBuilder setLore(List<String> loreLines) {
        meta.setLore(loreLines);
        return this;
    }

    public ItemBuilder setLore(String... loreLines) {
        meta.setLore(Arrays.asList(loreLines));
        return this;
    }

    /**
     * Sets the lore but applies a formatter function to each lore line.
     * @param formatter The formatter function that will be applied to each line.
     * @param loreLines The array of "raw" lore lines.
     * @return The ItemBuilder itself.
     */
    public ItemBuilder setLore(Function<String, String> formatter, String... loreLines) {
        setLore(formatter, Arrays.asList(loreLines));
        return this;
    }

    /**
     * Sets the lore but applies a formatter function to each lore line.
     * @param formatter The formatter function that will be applied to each line.
     * @param loreLines The list of "raw" lore lines.
     * @return The ItemBuilder itself.
     */
    public ItemBuilder setLore(Function<String, String> formatter, List<String> loreLines) {
        List<String> formattedLore = new ArrayList<>(loreLines.size());
        for (String s : loreLines) {
            formattedLore.add(formatter.apply(s));
        }
        meta.setLore(formattedLore);
        return this;
    }

    public ItemBuilder appendLore(String ... loreLines) {
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            ArrayList<String> newLore = new ArrayList<>(lore);
            newLore.addAll(Arrays.asList(loreLines));
            meta.setLore(newLore);
        }
        return this;
    }

    public ItemBuilder durability(short durability) {
        stack.setDurability(durability);
        return this;
    }

    public ItemBuilder durability(int durability) {
        stack.setDurability((short) durability);
        return this;
    }

    public ItemBuilder material(Material type) {
        stack.setType(type);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder data(byte data) {
        stack.setData(new MaterialData(stack.getType(), data));
        return this;
    }

    public ItemBuilder name(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder unbreakable() {
        meta.spigot().setUnbreakable(true);
        return this;
    }

    public ItemBuilder setData(MaterialData materialData) {
        stack.setData(materialData);
        return this;
    }

    public ItemBuilder hideAttributes() {
        return flag(ItemFlag.HIDE_ATTRIBUTES);
    }

    public ItemBuilder flag(ItemFlag flag) {
        meta.addItemFlags(flag);
        return this;
    }

    /**
     * Often the final method used with an ItemBuilder.
     * Applies the internal {@link ItemMeta} to the {@link ItemStack} and returns it.
     * You can technically still use the ItemBuilder object after using this method.
     * @return Returns the itemStack with everything applied.
     */
    public ItemStack build() {
        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * @return Returns a clone of the item meta without applying anything. Should only be used to read information from the item meta
     */
    public ItemMeta getItemMeta() {
        return meta.clone();
    }

    /**
     * @return Returns a clone of the item stack without applying anything. Should only be used to read information from the item stack.
     */
    public ItemStack getItemStack() {
        return stack.clone();
    }

    public static class ItemEditor extends ItemBuilder {

        /**
         * This class is not building new items, but editing an existing item. It is still REQUIRED to call
         * {@link ItemBuilder#build()}
         */
        public ItemEditor(ItemStack stack) {
            this.stack = stack;
            this.meta = stack.getItemMeta();
        }
    }

    public static class DyeBuilder extends ItemBuilder {

        protected DyeColor dyeColor;

        public DyeBuilder(DyeColor dyeColor) {
            super(Material.INK_SACK);
            this.dyeColor = dyeColor;
        }

        @SuppressWarnings("deprecation")
        @Override
        public ItemStack build() {
            super.durability(dyeColor.getDyeData());
            return super.build();
        }

        @Override
        public ItemBuilder durability(int durability) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ItemBuilder durability(short durability) {
            throw new UnsupportedOperationException();
        }
    }

    public static class WoolBuilder extends ItemBuilder {

        protected Wool wool;

        public WoolBuilder(DyeColor color) {
            super(Material.WOOL);
            wool = new Wool(color);
        }

        @SuppressWarnings("deprecation")
        @Override
        public ItemStack build() {
            super.durability(wool.getData());
            return super.build();
        }

        @Override
        public ItemBuilder durability(int durability) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ItemBuilder durability(short durability) {
            throw new UnsupportedOperationException();
        }

    }

    public static class PotionBuilder extends ItemBuilder {

        protected Potion potion;
        protected int amount;

        public PotionBuilder(PotionType type) {
            amount = 1;
            potion = new Potion(type);
            meta = Bukkit.getItemFactory().getItemMeta(Material.POTION);
        }

        public PotionBuilder() {
            this(PotionType.WATER);
        }

        public PotionBuilder splash() {
            potion.splash();
            return this;
        }

        public PotionBuilder extend() {
            potion.extend();
            return this;
        }

        public PotionBuilder level(int level) {
            potion.setLevel(level);
            return this;
        }

        public PotionBuilder addEffect(PotionEffect potionEffect) {
            ((PotionMeta) meta).addCustomEffect(potionEffect, true);
            return this;
        }

        @Override
        public ItemBuilder amount(int amount) {
            this.amount = amount;
            return this;
        }

        @Override
        public ItemStack build() {
            stack = potion.toItemStack(amount);
            return super.build();
        }

    }

    public static class LeatherArmorBuilder extends ItemBuilder {

        public LeatherArmorBuilder(Material type, Color color) {
            super(type);
            if (!Bukkit.getItemFactory()
                    .isApplicable(Bukkit.getItemFactory().getItemMeta(Material.LEATHER_BOOTS), type)) {
                throw new IllegalArgumentException(
                        "The provided type is not applicable for a leather armor");
            }
            ((LeatherArmorMeta) meta).setColor(color);
        }
    }

    public static class SkullBuilder extends ItemBuilder {

        public SkullBuilder(String name) {
            super(Material.SKULL_ITEM);
            durability((short) 3);
            ((SkullMeta) meta).setOwner(name);
        }

    }

    public static class WrittenBookBuilder extends ItemBuilder {

        protected BookMeta bm;

        public WrittenBookBuilder() {
            super(Material.WRITTEN_BOOK);
            this.bm = (BookMeta) meta;
        }

        public WrittenBookBuilder pages(String... pages) {
            this.bm.setPages(pages);
            return this;
        }

        public WrittenBookBuilder pages(List<String> pages) {
            this.bm.setPages(new ArrayList<>());
            return this;
        }

        public WrittenBookBuilder author(String author) {
            this.bm.setAuthor(author);
            return this;
        }

        public WrittenBookBuilder title(String title) {
            this.bm.setTitle(title);
            return this;
        }

    }

    public static class EnchantedBookBuilder extends ItemBuilder {

        protected EnchantmentStorageMeta enchantmentStorageMeta;

        public EnchantedBookBuilder() {
            super(Material.ENCHANTED_BOOK);
            this.enchantmentStorageMeta = (EnchantmentStorageMeta) meta;
        }

        public EnchantedBookBuilder storeEnchant(Enchantment ench, int level) {
            this.enchantmentStorageMeta.addStoredEnchant(ench, level, true);
            return this;
        }

    }
}

