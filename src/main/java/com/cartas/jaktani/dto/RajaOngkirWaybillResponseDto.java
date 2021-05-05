package com.cartas.jaktani.dto;

public class RajaOngkirWaybillResponseDto {
    RajaongkirWaybill rajaongkir;


    // Getter Methods

    public RajaongkirWaybill getRajaongkir() {
        return rajaongkir;
    }

    // Setter Methods

    public void setRajaongkir(RajaongkirWaybill rajaongkirObject) {
        this.rajaongkir = rajaongkirObject;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RajaOngkirWaybillResponseDto{");
        sb.append("RajaongkirObject=").append(rajaongkir);
        sb.append('}');
        return sb.toString();
    }
}












