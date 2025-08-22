package edu.appstate.cs.cloud.restful;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalcController {
    private static String logClass = "CalcController";
    private Logger logger = LoggerFactory.getLogger(logClass);

    @GetMapping("/add")
    private int add(int n, int m) {
        int result = n + m;
        logger.info(String.format("Adding %d and %d, with the result %d", n, m, result));
        return result;
    }

    @GetMapping("/subtract")
    private int subtract(int n, int m) {
        int result = n - m;
        logger.info(String.format("Subtracting %d from %d, with the result %d", m, n, result));
        return result;
    }

    @GetMapping("/times")
    private int times(int n, int m) {
        int result = n * m;
        logger.info(String.format("Multiplying %d by %d, with the result %d", n, m, result));
        return result;
    }

    @GetMapping("/div")
    private int div(int n, int m) {
        int result = n / m;
        logger.info(String.format("Dividing %d by %d, with the result %d", n, m, result));
        return result;
    }

    @GetMapping("/negate")
    private int negate(int n) {
        int result = -n;
        logger.info(String.format("Negating %d, with the result %d", n, result));
        return result;
    }
}
