package com.example; 

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CalculatorController {

    private List<Calculation> previousCalculations = new ArrayList<>();

    @GetMapping("/add")
    public Calculation addNumbers(
            @RequestParam(name = "operand1") int operand1,
            @RequestParam(name = "operand2") int operand2
    ) {
        int sum = operand1 + operand2;
        Calculation calculation = new Calculation(operand1, operand2, sum);
        previousCalculations.add(calculation); /
        return calculation;
    }

    @GetMapping("/searchAdditions")
    public List<Calculation> searchAdditions(
            @RequestParam(name = "searchValue", required = false) Integer searchValue,
            @RequestParam(name = "sortOrder") String sortOrder
    ) {
        List<Calculation> matchingCalculations;

        if (searchValue != null) {
          
            matchingCalculations = previousCalculations.stream()
                    .filter(calculation -> calculation.getOperand1() == searchValue
                            || calculation.getOperand2() == searchValue
                            || calculation.getSum() == searchValue)
                    .collect(Collectors.toList());
        } else {
            matchingCalculations = previousCalculations;
        }

        if (sortOrder.equalsIgnoreCase("asc")) {
            matchingCalculations.sort(Comparator.comparingInt(Calculation::getSum));
        } else if (sortOrder.equalsIgnoreCase("desc")) {
            matchingCalculations.sort((a, b) -> Integer.compare(b.getSum(), a.getSum()));
        }

        return matchingCalculations;
    }
}