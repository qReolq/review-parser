package repository;

import model.ViewerReview;

import java.util.List;

public interface ViewerReviewRepository {

    void save(List<ViewerReview> reviews);

}
