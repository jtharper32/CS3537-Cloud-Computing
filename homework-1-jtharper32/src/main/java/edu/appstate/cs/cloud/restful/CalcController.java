package edu.appstate.cs.cloud.restful;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalcController {
    private static final Logger logger = LoggerFactory.getLogger(CalcController.class);

    @GetMapping("/add")
    public int add(@RequestParam int n, @RequestParam int m) {
        logger.info(String.format("Adding %d + %d", n, m));
        return n + m;
    }

    @GetMapping("/subtract")
    public int subtract(@RequestParam int n, @RequestParam int m) {
        logger.info(String.format("Subtracting %d - %d", n, m));
        return n - m;
    }

    @GetMapping("/times")
    public int times(@RequestParam int n, @RequestParam int m) {
        logger.info(String.format("Multiplying %d * %d", n, m));
        return n * m;
    }

    @GetMapping("/div")
    public int div(@RequestParam int n, @RequestParam int m) {
        logger.info(String.format("Dividing %d / %d", n, m));
        return n / m;
    }

    @GetMapping("/negate")
    public int negate(@RequestParam int n) {
        logger.info(String.format("Negating %d", n));
        return -n;
    }
}
