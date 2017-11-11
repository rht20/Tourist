package com.arm.tourist.Models;

/**
 * Created by rht on 11/7/17.
 */

public class PostComment {

    String postId, userName, userImage, commentText, commentTime;

    public PostComment(){}

    public PostComment(String postId, String userName, String userImage, String commentText, String commentTime) {
        this.postId = postId;
        this.userName = userName;
        this.userImage = userImage;
        this.commentText = commentText;
        this.commentTime = commentTime;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }
}
