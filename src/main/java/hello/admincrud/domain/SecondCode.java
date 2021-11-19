package hello.admincrud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * book :책(도서)
 * media :음반, DVD
 */
@Data
@AllArgsConstructor
public class SecondCode {

    private String code;
    private String displayName;
}
