package model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ViewerReview {
    private String authorName;
    private String linkToAuthor;
    private String header;
    private String body;
    private String directLink;
    private LocalDateTime createdAt;
    private ReviewType reviewType;
    private Long likes;
    private Long dislikes;
}
