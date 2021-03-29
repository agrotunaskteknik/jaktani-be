package com.cartas.jaktani.dto;

import java.util.List;

public class CityParent {
    private RajaOngkirCity rajaongkir;

    public CityParent(RajaOngkirCity rajaongkir) {
        this.rajaongkir = rajaongkir;
    }

    public RajaOngkirCity getRajaongkir() {
        return rajaongkir;
    }

    public void setRajaongkir(RajaOngkirCity rajaongkir) {
        this.rajaongkir = rajaongkir;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CityParent{");
        sb.append("rajaongkir=").append(rajaongkir);
        sb.append('}');
        return sb.toString();
    }
}




