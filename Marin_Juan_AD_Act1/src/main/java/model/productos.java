package model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class productos {
    @JsonProperty("id")
    private int id;

    @JsonProperty("title")
    private String nombre;

    @JsonProperty("description")
    private String descripcion;

    @JsonProperty("stock")
    private int cantidad;

    @JsonProperty("price")
    private double precio;
}

