package com.formento.spa.controller;

import com.formento.spa.model.Problem;

/**
 * Created by Keval on 13.10.2022.
 */

/**
 * Abstract class from which will derive 2 types of algorithms: a random one and a stable one.
 */
public abstract class Solver {
    Problem problem;

    public Solver(Problem problem) {
        this.problem = problem;
    }

    public abstract void execute();
}
