package com.rutaverde.vista;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.util.Arrays;

/** Conjunto de palmas de cera de distintas alturas, repartidas en un ancho fijo. */
public class PaisajePalmas extends Pane {
    private static final Color HOJA = Color.web("#12B8D6");
    private static final Color HOJA_CLARA = Color.web("#27D6F5");

    public PaisajePalmas(double ancho, double... alturas) {
        double alto = Arrays.stream(alturas).max().orElse(60) * 1.2;
        setPrefSize(ancho, alto);
        setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        setMouseTransparent(true);

        double paso = ancho / alturas.length;
        for (int i = 0; i < alturas.length; i++) {
            PalmaCera palma = new PalmaCera(alturas[i], HOJA, HOJA_CLARA);
            palma.setLayoutX(paso * (i + 0.5));
            palma.setLayoutY(alto);
            getChildren().add(palma);
        }
    }
}
