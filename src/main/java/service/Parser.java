package service;

import model.ViewerReview;

import java.util.List;

public interface Parser {

    void save(List<List<ViewerReview>> listOfReviews);

    void parse();
}
