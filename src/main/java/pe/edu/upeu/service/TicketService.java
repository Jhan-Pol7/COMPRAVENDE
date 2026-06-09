package pe.edu.upeu.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import pe.edu.upeu.model.Transaccion;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TicketService {

    public void mostrarTicket(Transaccion t, String nombreComprador) {

        try {
            // Carga el archivo .jrxml desde resources
            InputStream jrxml = getClass().getResourceAsStream(
                    "/pe/edu/upeu/reportes/ticket.jrxml"
            );

            // Compila el .jrxml a un objeto JasperReport usable
            JasperReport reporte = JasperCompileManager.compileReport(jrxml);

            // Parámetros que se inyectan en la plantilla del ticket
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("numero",    t.getId());
            parametros.put("producto",  t.getNombreProducto());
            parametros.put("precio",    t.getPrecio());
            parametros.put("comprador", nombreComprador);
            parametros.put("fecha",     t.getFecha());

            // Genera el reporte; JREmptyDataSource porque no usamos filas de BD
            JasperPrint print = JasperFillManager.fillReport(
                    reporte, parametros, new JREmptyDataSource()
            );

            // Abre el visor PDF integrado de JasperReports
            JasperViewer.viewReport(print, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}