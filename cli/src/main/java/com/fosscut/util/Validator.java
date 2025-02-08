package com.fosscut.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.type.cutting.order.Order;

public class Validator {

    private static final Logger logger = LoggerFactory.getLogger(Validator.class);

    public void validateOrder(Order order) {
        logger.info("Running order validation...");
        order.validate();
        logger.info(Messages.ORDER_VALID);
    }

}
