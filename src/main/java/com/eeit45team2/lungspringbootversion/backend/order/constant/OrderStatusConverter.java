package com.eeit45team2.lungspringbootversion.backend.order.constant;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {
    /*
     * https://www.baeldung.com/jpa-persisting-enums-in-jpa
     * */

    @Override
    public String convertToDatabaseColumn(OrderStatus orderStatus) {
        if (orderStatus == null) {
            return null;
        }
        return orderStatus.getCode();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(OrderStatus.values())
                .filter(o -> o.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

    }


}
