package service;

import config.Connection;
import config.LocalDateTimeConvertor;
import lombok.extern.slf4j.Slf4j;
import model.ReviewType;
import model.ViewerReview;
import model.exception.ConnectionException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import repository.ViewerReviewRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j(topic = "ParserImpl")
public class ParserImpl implements Parser {

    private final ViewerReviewRepository viewerReviewRepository;
    private final LocalDateTimeConvertor localDateTimeConvertor;
    private final String URL;

    public ParserImpl(ViewerReviewRepository viewerReviewRepository, int prePages) {
        this.viewerReviewRepository = viewerReviewRepository;
        this.localDateTimeConvertor = new LocalDateTimeConvertor();
        this.URL = String.format(
                "https://www.kinopoisk.ru/film/326/reviews/ord/date/status/all/perpage/%s/page/",
                prePages
        );
    }

    @Override
    public void save(List<List<ViewerReview>> pagesReviews) {
        log.info("Start saving.");
        for (List<ViewerReview> pagerReviews : pagesReviews) {
            viewerReviewRepository.save(pagerReviews);
        }
    }

    @Override
    public void parse() {
        log.info("Parsing started.");
        int amountOfPages = parseAmountOfPages();
        ExecutorService executorService = Executors.newFixedThreadPool(amountOfPages);
        List<Callable<List<ViewerReview>>> tasks = new ArrayList<>();

        for (int i = 1; i <= amountOfPages; i++) {
            String url = URL + i + "/";
            tasks.add(() -> parseReviews(url));
        }

        List<Future<List<ViewerReview>>> futures;
        try {
            futures = executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<List<ViewerReview>> reviewsLists = futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        log.info("Error during parsing", e);
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        save(reviewsLists);
        executorService.shutdown();

        log.info("Parsing completed.");
    }

    private List<ViewerReview> parseReviews(String url) {
        List<ViewerReview> parsedReviews = new ArrayList<>();
        try {
            Document doc = Connection.getConnection(url);
            Elements reviewElements = doc.getElementsByClass("reviewItem userReview");

            for (Element reviewElement : reviewElements) {
                parsedReviews.add(parseReviewElement(reviewElement));
            }
        } catch (ConnectionException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return parsedReviews;
    }

    private ViewerReview parseReviewElement(Element reviewElement) {
        ViewerReview review = new ViewerReview();
        review.setAuthorName(parseAuthorName(reviewElement));
        review.setLinkToAuthor(parseLinkToAuthor(reviewElement));
        review.setHeader(parseHeader(reviewElement));
        review.setBody(parseBody(reviewElement));
        review.setDirectLink(parseDirectLink(reviewElement));
        review.setCreatedAt(parseCreatedAt(reviewElement));
        review.setReviewType(parseReviewType(reviewElement));

        long[] likesAndDislikes = parseLikesAndDislikes(reviewElement);
        review.setLikes(likesAndDislikes[0]);
        review.setDislikes(likesAndDislikes[1]);

        return review;
    }

    private int parseAmountOfPages() {
        String URL = this.URL + "1/";
        try {
            Document doc = Connection.getConnection(URL);
            List<Node> childNodes = doc
                    .getElementsByClass("list")
                    .get(0)
                    .childNodes();
            String href = childNodes
                    .get(childNodes.size() - 2)
                    .childNode(0)
                    .attr("href");

            StringBuilder sb = new StringBuilder();

            for (int i = href.length() - 2; i >= 0; i--) {
                if (href.charAt(i) == '/') break;
                sb.append(href.charAt(i));
            }

            return Integer.parseInt(sb.reverse().toString());
        } catch (ConnectionException e) {
            log.error("Error during parse amount of pages");
            throw new RuntimeException(e);
        }

    }

    private String parseBody(Element element) {
        return element.getElementsByClass("_reachbanner_")
                .text();
    }

    private String parseAuthorName(Element element) {
        return element.getElementsByAttributeValue("itemprop", "name")
                .text();
    }

    private String parseHeader(Element element) {
        return element.getElementsByClass("sub_title")
                .text();
    }

    private String parseLinkToAuthor(Element element) {
        return "https://www.kinopoisk.ru" + element
                .getElementsByAttributeValue("itemprop", "name")
                .attr("href");
    }

    private LocalDateTime parseCreatedAt(Element element) {
        return localDateTimeConvertor
                .convert(element.getElementsByClass("date").text());
    }

    private String parseDirectLink(Element element) {
        try {
            return "https://www.kinopoisk.ru" + element
                    .getElementsByClass("links")
                    .get(0)
                    .child(0)
                    .attr("href");
        } catch (IndexOutOfBoundsException ignore) {
        }
        return "No link";
    }

    private ReviewType parseReviewType(Element element) {
        String reviewType = element
                .getElementsByAttributeValue("itemprop", "reviews")
                .attr("class");

        return switch (reviewType) {
            case "response good" -> ReviewType.GOOD;
            case "response neutral" -> ReviewType.NEUTRAL;
            default -> ReviewType.BAD;
        };
    }

    private long[] parseLikesAndDislikes(Element element) {
        String[] likesAndDislikes = element
                .getElementsByClass("useful")
                .get(0)
                .child(2)
                .text()
                .split("/");

        return Arrays.stream(likesAndDislikes)
                .mapToLong(it -> Long.parseLong(it.trim()))
                .toArray();
    }

}