package com.cafeteria.servicio;

import java.sql.SQLException;

import com.cafeteria.descuento.CalculadoraDescuento;
import com.cafeteria.descuento.ResultadoDescuento;
import com.cafeteria.persistencia.PedidoRepositorio;

/**
 * Punto de entrada de la lógica de negocio para la interfaz gráfica.
 * La vista solo conoce este servicio: no sabe cómo se calcula el descuento ni cómo se guarda el pedido.
 */
public class ServicioCafeteria {

    private final CalculadoraDescuento calculadora;
    private final PedidoRepositorio repositorio;

    public ServicioCafeteria(CalculadoraDescuento calculadora, PedidoRepositorio repositorio) {
        this.calculadora = calculadora;
        this.repositorio = repositorio;
    }

    public String iniciarBaseDatos() throws ClassNotFoundException, SQLException {
        return repositorio.inicializar();
    }

    public ResultadoDescuento calcularDescuento(double monto) {
        return calculadora.calcular(monto);
    }

    public long registrarPedido(ResultadoDescuento resultado) throws ClassNotFoundException, SQLException {
        return repositorio.guardar(resultado);
    }
}
