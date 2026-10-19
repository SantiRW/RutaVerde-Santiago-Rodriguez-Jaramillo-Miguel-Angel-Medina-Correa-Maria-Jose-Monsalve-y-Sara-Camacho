package com.rutaverde.vista;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurveTo;


public class PalmaCera extends Group {

    public PalmaCera(double altura, Color hoja, Color hojaClara) {
        double ancho = Math.max(4, altura * 0.055);
        double anchoCima = ancho * 0.65;

        Polygon tronco = new Polygon(
                -ancho / 2, 0, ancho / 2, 0,
                anchoCima / 2, -altura, -anchoCima / 2, -altura);
        tronco.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#E4F3F5")), new Stop(1, Color.web("#B9D6DC"))));
        getChildren().add(tronco);

        int anillos = Math.max(5, (int) (altura / 16));
        for (int i = 1; i < anillos; i++) {
            double t = (double) i / anillos;
            double y = -altura * t;
            double w = ancho + (anchoCima - ancho) * t;
            Line anillo = new Line(-w / 2, y, w / 2, y);
            anillo.setStroke(Color.web("#9FC3CB"));
            anillo.setStrokeWidth(1);
            getChildren().add(anillo);
        }

        double largo = altura * 0.30;
        double[] angulos = {-80, -55, -30, -8, 8, 30, 55, 80};
        for (int i = 0; i < angulos.length; i++) {
            getChildren().add(fronda(0, -altura, largo, angulos[i], i % 2 == 0 ? hoja : hojaClara));
        }
        getChildren().add(fronda(0, -altura, largo * 0.8, 0, hojaClara));
    }

    private Path fronda(double x0, double y0, double largo, double grados, Color color) {
        double rad = Math.toRadians(grados);
        double ex = Math.sin(rad) * largo;
        double ey = -Math.cos(rad) * largo * 0.55 + Math.abs(Math.sin(rad)) * largo * 0.22;
        double mx = ex * 0.5;
        double my = ey * 0.5 - largo * 0.22;
        double len = Math.max(1, Math.hypot(ex, ey));
        double nx = -ey / len;
        double ny = ex / len;
        double grosor = largo * 0.13;

        Path hojaPath = new Path(
                new MoveTo(x0, y0),
                new QuadCurveTo(x0 + mx + nx * grosor, y0 + my + ny * grosor, x0 + ex, y0 + ey),
                new QuadCurveTo(x0 + mx - nx * grosor, y0 + my - ny * grosor, x0, y0),
                new ClosePath());
        hojaPath.setFill(color);
        hojaPath.setStroke(null);
        hojaPath.setOpacity(0.92);
        return hojaPath;
    }
}
