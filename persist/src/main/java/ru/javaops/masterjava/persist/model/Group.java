package ru.javaops.masterjava.persist.model;

import lombok.*;

/**
 * Created by root on 03.04.2017.
 */

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Group extends BaseEntity {

    private @NonNull String name;
    private @NonNull GroupType type;
    private @NonNull Project project;

}
