package models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

/**
 * Created by athakur on 2/11/2018.
 */
@Getter
@Setter
public class Profile {

    private boolean enabled;
    private boolean used;
    private String profileName;
    private String teamName;

    @Tolerate
    public Profile() {
    }

}
