package config;

import lombok.extern.slf4j.Slf4j;
import model.exception.ConnectionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

@Slf4j(topic = "Connection")
public class Connection {

    public static Document getConnection(String url) throws ConnectionException {
        log.info("Start connection");
        try {
            Document con = Jsoup.connect(url)
                    .cookie("ya_sess_id", "3:1706949191.5.0.1700471637371:U8oIHw:7.1.2:1|794103964.0.2.3:1700471637|30:10222327.123808.I59LttNJzW_zMdzlF4tqbzOC6mc")
                    .cookie("sessar", "1.1186.CiCgH0thVELsoxvuyDb2HcUfu7D7h_Oxi4965NAEEOP8Rg.oxZawv7yJmqXJICnpmtYkpXaC7FdoWUnAjsNfkI86YE")
                    .cookie("yandex_login", "saetovo")
                    .cookie("ys", "udn.cDpzYWV0b3Zv#c_chck.2310811761")
                    .cookie("i", "AQLP3gEbHT15NsHi6rwCmeFlT9Joz7U5+zeBjBDg8oOMDDkY4SvecFE8dpL7Zzh13vEm5dU1D3il3HzgCzqBik57CM4")
                    .cookie("yandexuid", "8146254251700469067")
                    .cookie("L", "amh/R2cBQ2VyCltFB1RdZUJ0YXIERm9hKggpIjYQFw")
                    .cookie("mda2_beacon", "1706949191616")
                    .cookie("sso_status", "sso.passport.yandex.ru:synchronized")
                    .cookie("_csrf", "h8xp1TQkW61RpzArt39cel-H")
                    .cookie("no-re-reg-required", "1")
                    .cookie("desktop_session_key", "bf64c53b5794e41decdc63bffbfdccc99c37bc2ca773cb18814d4b5ef454ef22b97a660508547ee59de52afb125e96664584028c4b41e517931a7b2bc245813d5aee6427cf7110fc6bd0b59e039a03203deaee8df997bafe6e21408e50e7ef75")
                    .cookie("desktop_session_key.sig", "Uwh8PEcVsghUKw3hbvNrH1CTFjg")
                    .cookie("PHPSESSID", "59b66d831d4b810c5dc929dec2475731")
                    .cookie("user_country", "ru")
                    .cookie("yandex_gid", "101199")
                    .cookie("tc", "1")
                    .cookie("uid", "48908681")
                    .cookie("location", "1")
                    .cookie("my_perpages", "%5B%5D")
                    .cookie("_csrf_csrf_token", "hLhlN0bkbVxB24w7qV7MyL9vfvofi8g9RPqRmlelzzk")
                    .cookie("mobile", "no")
                    .cookie("mda_exp_enabled", "1")
                    .cookie("crookie", "3fRD1BYkFcTTApecffOk9ccGJkC/X1zMi1dRpCVOwiATaGnmKZAJO7/pETV5BLik7Rc6lXqfqa4zFr18L121e+s9tEw")
                    .cookie("cmtchd", "MTcwNjk0OTQ0MDY2Nw")
                    .cookie("coockoos", "1")
                    .cookie("_yasc", "WmJKpDxngetmhxVXDk2mDQ/hmooFCRreNxydY6xavgwpWclL45Uk+/37vy2I6dVk")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36")
                    .method(org.jsoup.Connection.Method.GET)
                    .ignoreContentType(true)
                    .get();
            log.info("successful connection");
            return con;
        } catch (IOException e) {
            throw new ConnectionException("Invalid url or check internet connection");
        }
    }

}
