package ru.gpncr.services;

import java.util.List;
import ru.gpncr.model.Equation;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
