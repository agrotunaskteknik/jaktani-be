package com.cartas.jaktani.dto;

import java.util.List;

public class CostParent {
    private RajaOngkirCost rajaongkir;

    public CostParent(RajaOngkirCost rajaongkir) {
        this.rajaongkir = rajaongkir;
    }

    public RajaOngkirCost getRajaongkir() {
        return rajaongkir;
    }

    public void setRajaongkir(RajaOngkirCost rajaongkir) {
        this.rajaongkir = rajaongkir;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CostParent{");
        sb.append("rajaongkir=").append(rajaongkir);
        sb.append('}');
        return sb.toString();
    }
}



