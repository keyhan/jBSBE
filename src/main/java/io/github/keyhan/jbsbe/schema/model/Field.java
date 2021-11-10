package io.github.keyhan.jbsbe.schema.model;

import io.github.keyhan.jbsbe.iso.I50Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * Created by keyhan on 2017-03-28.
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"title","type","length","mask"})
@ToString
public class Field {

    @NonNull
    private Short position;
    @NonNull
    private String title;
    @NonNull
    private I50Type type;

    private Short length;

    private String mask;

}
