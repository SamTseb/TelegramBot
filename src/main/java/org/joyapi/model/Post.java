package org.joyapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Post {
    @JsonProperty("preview_url")
    private String previewUrl;

    @JsonProperty("sample_url")
    private String sampleUrl;

    @JsonProperty("file_url")
    private String fileUrl;

    @JsonProperty("id")
    private int id;

    @JsonProperty("score")
    private int score;

    @JsonProperty("tags")
    private String tags;
}
