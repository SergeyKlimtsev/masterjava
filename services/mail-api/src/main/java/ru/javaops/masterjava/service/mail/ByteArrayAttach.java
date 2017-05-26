package ru.javaops.masterjava.service.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by root on 18.05.2017.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ByteArrayAttach implements Serializable{
    private String name;
    private byte[] data;
}
