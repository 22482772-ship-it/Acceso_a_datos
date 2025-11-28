package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor

public class pedidos {
    private int id;
    private int cantidad;
    private double precio_total;
    private int id_producto;
    private int id_empleado;
}
