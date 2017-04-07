package ru.javaops.masterjava.persist.model;

import lombok.*;

/**
 * Created by root on 03.04.2017.
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class City {

    private @NonNull String id;
    private @NonNull String name;

}
