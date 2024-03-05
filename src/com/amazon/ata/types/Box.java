package com.amazon.ata.types;

import java.math.BigDecimal;
import java.util.Objects;

public class Box extends Packaging {
    /**
     * This packaging's material.
     */
    private Material material;

    /**
     * This packaging's length.
     */
    private BigDecimal length;

    /**
     * This packaging's smallest dimension.
     */
    private BigDecimal width;

    /**
     * This packaging's largest dimension.
     */
    private BigDecimal height;

    /**
     * Instantiates a new Packaging object.
     *
     * @param length   - the length of the package
     * @param width    - the width of the package
     * @param height   - the height of the package
     */
    public Box(BigDecimal length, BigDecimal width, BigDecimal height) {
        this.material = Material.CORRUGATE;
        this.length = length;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean canFitItem(Item item) {
        return this.length.compareTo(item.getLength()) > 0 &&
                this.width.compareTo(item.getWidth()) > 0 &&
                this.height.compareTo(item.getHeight()) > 0;
    }

    /**
     * getMass.
     * @return mass.
     */
    public BigDecimal getMass() {
        BigDecimal endsArea = length.multiply(width).multiply(new BigDecimal("2"));
        BigDecimal shortSidesArea = length.multiply(height).multiply(new BigDecimal("2"));
        BigDecimal longSidesArea = width.multiply(height).multiply(new BigDecimal("2"));
        BigDecimal mass = endsArea.add(shortSidesArea).add(longSidesArea);
        return mass;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getMaterial(), length, width, height);
    }
}