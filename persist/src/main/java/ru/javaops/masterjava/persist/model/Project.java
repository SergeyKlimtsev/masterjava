package ru.javaops.masterjava.persist.model;

import lombok.*;

/**
 * Created by root on 03.04.2017.
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Project extends BaseEntity {

    private
    @NonNull
    String description;

    private
    @NonNull
    String name;

}
