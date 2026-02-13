package com.nic.tfw.genetics;

import com.nic.tfw.genetics.genetypes.GeneType;

import java.awt.*;

public record GeneticData<T>(GeneType<T> geneType, T value, Color color, GeneticOrigin origin) {

}
