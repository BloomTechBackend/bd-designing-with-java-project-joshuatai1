package com.amazon.ata.types;

import java.math.BigDecimal;
import java.util.Objects;

public class PolyBag extends Packaging {

    /**
     * This packaging's material.
     */
    private Material material;
    /**
     * This packaging's volume.
     */
    private BigDecimal volume;

    /**
     * Instantiates a new Packaging object.
     * @param material something.
     * @param volume  - the volume of the package
     */
    public PolyBag(Material material, BigDecimal volume) {
        this.material = Material.LAMINATED_PLASTIC;
        this.volume = volume;
    }

    @Override
    public boolean canFitItem(Item item) {
        BigDecimal endsArea = item.getLength().multiply(item.getWidth()).multiply(new BigDecimal("2"));
        BigDecimal shortSidesArea = item.getLength().multiply(item.getHeight()).multiply(new BigDecimal("2"));
        BigDecimal longSidesArea = item.getWidth().multiply(item.getHeight()).multiply(new BigDecimal("2"));
        BigDecimal mass = endsArea.add(shortSidesArea).add(longSidesArea);
        return mass.compareTo(volume) < 0;
    }

    @Override
    public BigDecimal getMass() {
        double mass = Math.ceil(Math.sqrt(volume.doubleValue()) * 0.6);
        return BigDecimal.valueOf(mass);
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PolyBag polyBag = (PolyBag) o;
        return Objects.equals(volume, polyBag.volume) &&
                material == polyBag.material;
    }

    @Override
    public int hashCode() {
        return Objects.hash(volume, material);
    }
}
