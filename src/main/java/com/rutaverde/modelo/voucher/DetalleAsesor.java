package com.rutaverde.modelo.voucher;

import com.rutaverde.modelo.dominio.Actividad;
import com.rutaverde.modelo.dominio.Reserva;

public class DetalleAsesor implements DetalleVoucher {
    @Override
    public String generar(Reserva r) {
        StringBuilder sb = new StringBuilder();
        String nl = System.lineSeparator();
        sb.append("Reserva No. ").append(r.getNumero()).append(nl);
        sb.append("Cliente: ").append(r.getCliente().getNombreCompleto())
          .append(" (").append(r.getCliente().getDocumento()).append(")").append(nl);
        sb.append("Producto: ").append(r.getProducto().getNombre()).append(nl);
        sb.append("Fecha del viaje: ").append(r.getFechaViaje()).append(nl);
        sb.append("Viajeros: ").append(r.getViajeros()).append(nl);
        sb.append("Actividades:").append(nl);
        for (Actividad a : r.getProducto().getActividades()) {
            sb.append("  - ").append(a.getNombre()).append(" | ").append(a.getMunicipio())
              .append(" | ").append(a.getDuracionHoras()).append(" h | ")
              .append(Formato.pesos(a.getPrecioPorPersona())).append(" p/p").append(nl);
        }
        sb.append("Guía: ").append(r.getGuia().map(g -> g.getNombre()).orElse("Sin guía")).append(nl);
        sb.append("Transporte desde el hotel: ").append(r.incluyeTransporte() ? "Sí" : "No").append(nl);
        sb.append("Seguro de viaje: ").append(r.incluyeSeguro() ? "Sí" : "No").append(nl);
        if (!r.getObservaciones().isBlank()) {
            sb.append("Observaciones: ").append(r.getObservaciones()).append(nl);
        }
        sb.append("Autorización de pago: ").append(r.getAutorizacion()).append(nl);
        sb.append("TOTAL: ").append(Formato.pesos(r.getValor()));
        return sb.toString();
    }
}
