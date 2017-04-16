package ru.javaops.masterjava.service.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * gkislin
 * 15.11.2016
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Addressee {
    private String email;
    private String name;
}
