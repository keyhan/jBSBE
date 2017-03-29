package org.mashad.jbsbe.schema.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.mashad.jbsbe.iso.I50Type;

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
