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
     *
     * @param volume  - the volume of the package
     */
    public PolyBag(BigDecimal volume) {
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
        return volume;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.getMaterial(), volume);
    }
}
