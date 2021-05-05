package com.cartas.jaktani.dto;

public class RajaongkirWaybill {
    QueryWaybill query;
    StatusWaybill status;
    ResultWaybill result;


    // Getter Methods

    public QueryWaybill getQuery() {
        return query;
    }

    public StatusWaybill getStatus() {
        return status;
    }

    public ResultWaybill getResult() {
        return result;
    }

    // Setter Methods

    public void setQuery(QueryWaybill queryObject) {
        this.query = queryObject;
    }

    public void setStatus(StatusWaybill statusObject) {
        this.status = statusObject;
    }

    public void setResult(ResultWaybill resultObject) {
        this.result = resultObject;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RajaongkirWaybill{");
        sb.append("QueryObject=").append(query);
        sb.append(", StatusObject=").append(status);
        sb.append(", ResultObject=").append(result);
        sb.append('}');
        return sb.toString();
    }
}
