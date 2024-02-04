package repository;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import model.ViewerReview;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j(topic = "ViewerReviewInCSV")
public class ViewerReviewInCSV implements ViewerReviewRepository {
    private StatefulBeanToCsv<ViewerReview> reviewToCsv;

    public ViewerReviewInCSV(String fullPathToSave) {
        try {
            Writer writer = new OutputStreamWriter(
                    new FileOutputStream(fullPathToSave + "\\reviews.cvg"), StandardCharsets.UTF_8
            );
            this.reviewToCsv = new StatefulBeanToCsvBuilder<ViewerReview>(writer).build();
        } catch (IOException e) {
            log.error("Error initializing ViewerReviewInCSV", e);
            e.printStackTrace();
        }
    }

    @Override
    public void save(List<ViewerReview> reviews) {
        try {
            reviewToCsv.write(reviews);
            log.info("{} reviews successfully saved to CSV.", reviews.size());
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error("Error writing reviews to CSV", e);
            throw new RuntimeException();
        }
    }

}

