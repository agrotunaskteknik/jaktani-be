package com.cartas.jaktani.dto;

public class CityParentSingle {
    private RajaOngkirCitySingle rajaongkir;

    public CityParentSingle(RajaOngkirCitySingle rajaongkir) {
        this.rajaongkir = rajaongkir;
    }

    public RajaOngkirCitySingle getRajaongkir() {
        return rajaongkir;
    }

    public void setRajaongkir(RajaOngkirCitySingle rajaongkir) {
        this.rajaongkir = rajaongkir;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CityParentSingle{");
        sb.append("rajaongkir=").append(rajaongkir);
        sb.append('}');
        return sb.toString();
    }
}


