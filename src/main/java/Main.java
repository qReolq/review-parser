import lombok.extern.slf4j.Slf4j;
import repository.ViewerReviewInCSV;
import service.Parser;
import service.ParserImpl;

import java.util.Set;

@Slf4j(topic = "Main")
public class Main {

    public static void main(String[] args) {
        if (args.length < 2)
            throw new IllegalArgumentException("Usage: Main <csvFilePath> <prePages>");

        Parser reviewParser = new ParserImpl(
                new ViewerReviewInCSV(args[0]),
                validatePrePages(args[1])
        );

        reviewParser.parse();

        System.out.printf("Parsing is complete. All reviews are saved in %s\\reviews.csv.%n", args[0]);
    }

    private static int validatePrePages(String inputPrePages) {
        try {
            Set<Integer> validPrePages = Set.of(50, 100, 200);
            int prePages = Integer.parseInt(inputPrePages);

            if (validPrePages.contains(prePages))
                return prePages;

        } catch (NumberFormatException e) {
            log.error("Pre pages should be integer.");
        }
        log.info("Set pre pages default value: 100");
        return 100;
    }

}
