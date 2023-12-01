package lk.ijse.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private String customerId;
    private String name;
    private int contact;
}